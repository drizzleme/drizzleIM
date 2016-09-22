package com.github.drizzleme.client.ui.event.impl;

import com.github.drizzleme.client.event.MessageQoSEvent;
import com.github.drizzleme.client.ui.MainGUI;
import com.github.drizzleme.core.bo.Protocal;
import com.github.drizzleme.core.util.Log;

import java.util.ArrayList;

/**
 * Created with drizzleIM
 *
 * @author ; DRIZZLEME
 *         DATE : 2016/9/22
 */
public class MessageQoSEventImpl implements MessageQoSEvent {
    private final static String TAG = MessageQoSEventImpl.class.getSimpleName();

    private MainGUI mainGUI = null;

    @Override
    public void messagesLost(ArrayList<Protocal> lostMessages)
    {
        Log.d(TAG
                , "【DEBUG_UI】收到系统的未实时送达事件通知，当前共有"
                        +lostMessages.size()+"个包QoS保证机制结束，判定为【无法实时送达】！");

        if(this.mainGUI != null)
        {
            this.mainGUI.showIMInfo_brightred("[消息未成功送达]共"+lostMessages.size()+"条!(网络状况不佳或对方id不存在)");
        }
    }

    @Override
    public void messagesBeReceived(String theFingerPrint)
    {
        if(theFingerPrint != null)
        {
            Log.d(TAG, "【DEBUG_UI】收到对方已收到消息事件的通知，fp="+theFingerPrint);

            if(this.mainGUI != null)
            {
                this.mainGUI.showIMInfo_blue("[收到对方消息应答]fp="+theFingerPrint);
            }
        }
    }

    public MessageQoSEventImpl setMainGUI(MainGUI mainGUI)
    {
        this.mainGUI = mainGUI;
        return this;
    }
}
