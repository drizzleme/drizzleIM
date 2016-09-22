package com.github.drizzleme.client.ui;

import com.github.drizzleme.client.ClientCoreSDK;
import com.github.drizzleme.client.cnf.ConfigEntity;
import com.github.drizzleme.client.ui.event.impl.ChatBaseEventImpl;
import com.github.drizzleme.client.ui.event.impl.ChatTransDataEventImpl;
import com.github.drizzleme.client.ui.event.impl.MessageQoSEventImpl;

/**
 * Created with drizzleIM
 *
 * @author ; DRIZZLEME
 *         DATE : 2016/9/22
 */
public class IMClientManager {
    private static String TAG = IMClientManager.class.getSimpleName();

    private static IMClientManager instance = null;

    /** MobileIMSDK是否已被初始化. true表示已初化完成，否则未初始化. */
    private boolean init = false;

    //
    private ChatBaseEventImpl baseEventListener = null;
    //
    private ChatTransDataEventImpl transDataListener = null;
    //
    private MessageQoSEventImpl messageQoSListener = null;

    public static IMClientManager getInstance()
    {
        if(instance == null)
            instance = new IMClientManager();
        return instance;
    }

    private IMClientManager()
    {
        initMobileIMSDK();
    }

    public void initMobileIMSDK()
    {
        if(!init)
        {
            // 设置AppKey
            ConfigEntity.appKey = "5418023dfd98c579b6001741";

            // 设置服务器ip和服务器端口
//			ConfigEntity.serverIP = "192.168.82.138";
//			ConfigEntity.serverIP = "rbcore.openmob.net";
//			ConfigEntity.serverUDPPort = 7901;

            // MobileIMSDK核心IM框架的敏感度模式设置
//			ConfigEntity.setSenseMode(SenseMode.MODE_10S);

            // 开启/关闭DEBUG信息输出
//	    	ClientCoreSDK.DEBUG = false;

            // 设置事件回调
            baseEventListener = new ChatBaseEventImpl();
            transDataListener = new ChatTransDataEventImpl();
            messageQoSListener = new MessageQoSEventImpl();
            ClientCoreSDK.getInstance().setChatBaseEvent(baseEventListener);
            ClientCoreSDK.getInstance().setChatTransDataEvent(transDataListener);
            ClientCoreSDK.getInstance().setMessageQoSEvent(messageQoSListener);

            init = true;
        }
    }

    public void release()
    {
        ClientCoreSDK.getInstance().release();
    }

    public ChatTransDataEventImpl getTransDataListener()
    {
        return transDataListener;
    }
    public ChatBaseEventImpl getBaseEventListener()
    {
        return baseEventListener;
    }
    public MessageQoSEventImpl getMessageQoSListener()
    {
        return messageQoSListener;
    }
}
