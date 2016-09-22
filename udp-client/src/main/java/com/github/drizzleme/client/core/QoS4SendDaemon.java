package com.github.drizzleme.client.core;

import com.github.drizzleme.client.ClientCoreSDK;
import com.github.drizzleme.core.bo.Protocal;
import com.github.drizzleme.core.util.Log;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with drizzleIM
 *
 * @author ; DRIZZLEME
 *         DATE : 2016/9/22
 */
public class QoS4SendDaemon {
    private static final String TAG = QoS4SendDaemon.class.getSimpleName();

    // 并发Hash，因为本类中可能存在不同的线程同时remove或遍历之
    private ConcurrentHashMap<String, Protocal> sentMessages = new ConcurrentHashMap<>();
    // 并发Hash，因为本类中可能存在不同的线程同时remove或遍历之
    private ConcurrentHashMap<String, Long> sendMessagesTimestamp = new ConcurrentHashMap<>();

    public static final int CHECH_INTERVAL = 5000;
    public static final int MESSAGES_JUST$NOW_TIME = 3000;
    public static final int QOS_TRY_COUNT = 3;

    private boolean running = false;
    private boolean _excuting = false;
    private Timer timer = null;

    private static QoS4SendDaemon instance = null;

    public static QoS4SendDaemon getInstance()
    {
        if (instance == null) {
            instance = new QoS4SendDaemon();
        }
        return instance;
    }

    private QoS4SendDaemon()
    {
        init();
    }

    private void init()
    {
        this.timer = new Timer(CHECH_INTERVAL, new ActionListener()
        {
            public void actionPerformed(ActionEvent e) {
                QoS4SendDaemon.this.run();
            }
        });
    }

    public void run()
    {
        // 极端情况下本次循环内可能执行时间超过了时间间隔，此处是防止在前一
        // 次还没有运行完的情况下又重复执行，从而出现无法预知的错误
        if (!this._excuting)
        {
            ArrayList lostMessages = new ArrayList();

            this._excuting = true;
            try
            {
                if (ClientCoreSDK.DEBUG) {
                    Log.d(TAG, "【IMCORE】【QoS】=========== 消息发送质量保证线程运行中, 当前需要处理的列表长度为" + this.sentMessages.size() + "...");
                }

                for (String key : this.sentMessages.keySet())
                {
                    Protocal p = (Protocal)this.sentMessages.get(key);
                    if ((p != null) && (p.isQoS()))
                    {
                        if (p.getRetryCount() >= QOS_TRY_COUNT)
                        {
                            if (ClientCoreSDK.DEBUG) {
                                Log.d(TAG, "【IMCORE】【QoS】指纹为" + p.getFp() +
                                        "的消息包重传次数已达" + p.getRetryCount() + "(最多" + QOS_TRY_COUNT + "次)上限，将判定为丢包！");
                            }

                            lostMessages.add((Protocal)p.clone());

                            remove(p.getFp());
                        }
                        else
                        {
                            long delta = System.currentTimeMillis() - ((Long)this.sendMessagesTimestamp.get(key)).longValue();

                            if (delta <= MESSAGES_JUST$NOW_TIME)
                            {
                                if (ClientCoreSDK.DEBUG) {
                                    Log.w(TAG, "【IMCORE】【QoS】指纹为" + key + "的包距\"刚刚\"发出才" + delta +
                                            "ms(<=" + MESSAGES_JUST$NOW_TIME + "ms将被认定是\"刚刚\"), 本次不需要重传哦.");
                                }
                            }
                            else
                            {
                                new LocalUDPDataSender.SendCommonDataAsync(p)
                                {
                                    protected void onPostExecute(Integer code)
                                    {
                                        if (code.intValue() == 0)
                                        {
                                            this.p.increaseRetryCount();

                                            if (ClientCoreSDK.DEBUG)
                                                Log.d(QoS4SendDaemon.TAG, "【IMCORE】【QoS】指纹为" + this.p.getFp() +
                                                        "的消息包已成功进行重传，此次之后重传次数已达" +
                                                        this.p.getRetryCount() + "(最多" + QOS_TRY_COUNT + "次).");
                                        }
                                        else
                                        {
                                            Log.w(QoS4SendDaemon.TAG, "【IMCORE】【QoS】指纹为" + this.p.getFp() +
                                                    "的消息包重传失败，它的重传次数之前已累计为" +
                                                    this.p.getRetryCount() + "(最多" + QOS_TRY_COUNT + "次).");
                                        }
                                    }
                                }
                                        .execute();
                            }

                        }

                    }
                    else
                    {
                        remove(key);
                    }
                }
            }
            catch (Exception eee)
            {
                Log.w(TAG, "【IMCORE】【QoS】消息发送质量保证线程运行时发生异常," + eee.getMessage(), eee);
            }

            if ((lostMessages != null) && (lostMessages.size() > 0))
            {
                notifyMessageLost(lostMessages);
            }

            this._excuting = false;
        }
    }

    protected void notifyMessageLost(ArrayList<Protocal> lostMessages)
    {
        if (ClientCoreSDK.getInstance().getMessageQoSEvent() != null)
        {
            ClientCoreSDK.getInstance().getMessageQoSEvent().messagesLost(lostMessages);
        }
    }

    public void startup(boolean immediately)
    {
        stop();

        if (immediately)
            this.timer.setInitialDelay(0);
        else
            this.timer.setInitialDelay(CHECH_INTERVAL);
        this.timer.start();

        this.running = true;
    }

    public void stop()
    {
        if (this.timer != null) {
            this.timer.stop();
        }

        this.running = false;
    }

    public boolean isRunning()
    {
        return this.running;
    }

    boolean exist(String fingerPrint)
    {
        return this.sentMessages.get(fingerPrint) != null;
    }

    public void put(Protocal p)
    {
        if (p == null)
        {
            Log.w(TAG, "Invalid arg p==null.");
            return;
        }
        if (p.getFp() == null)
        {
            Log.w(TAG, "Invalid arg p.getFp() == null.");
            return;
        }

        if (!p.isQoS())
        {
            Log.w(TAG, "This protocal is not QoS pkg, ignore it!");
            return;
        }

        if (this.sentMessages.get(p.getFp()) != null) {
            Log.w(TAG, "【IMCORE】【QoS】指纹为" + p.getFp() + "的消息已经放入了发送质量保证队列，该消息为何会重复？（生成的指纹码重复？还是重复put？）");
        }

        // save it
        sentMessages.put(p.getFp(), p);
        // 同时保存时间戳
        sendMessagesTimestamp.put(p.getFp(), System.currentTimeMillis());
    }

    public void remove(final String fingerPrint)
    {
        new SwingWorker<Protocal, Object>(){
            @Override
            protected Protocal doInBackground()
            {
                sendMessagesTimestamp.remove(fingerPrint);
                return sentMessages.remove(fingerPrint);
            }

            @Override
            protected void done()
            {
                Protocal result = null;
                try
                {
                    result = get();
                }
                catch (Exception e)
                {
                    Log.w(TAG, e.getMessage());
                }

                Log.w(TAG, "【IMCORE】【QoS】指纹为"+fingerPrint+"的消息已成功从发送质量保证队列中移除(可能是收到接收方的应答也可能是达到了重传的次数上限)，重试次数="
                        +(result != null?((Protocal)result).getRetryCount():"none呵呵."));
            }
        }.execute();
    }

    public int size()
    {
        return this.sentMessages.size();
    }
}
