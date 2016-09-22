package com.github.drizzleme.client.ui.event.impl;

import com.github.drizzleme.client.event.ChatTransDataEvent;
import com.github.drizzleme.client.ui.MainGUI;
import com.github.drizzleme.core.util.Log;

/**
 * Created with drizzleIM
 *
 * @author ; DRIZZLEME
 *         DATE : 2016/9/22
 */
public class ChatTransDataEventImpl implements ChatTransDataEvent {
    private final static String TAG = ChatTransDataEventImpl.class.getSimpleName();

    private MainGUI mainGUI = null;

    @Override
    public void onTransBuffer(String fingerPrintOfProtocal, int dwUserid, String dataContent)
    {
        Log.d(TAG, "【DEBUG_UI】收到来自用户"+dwUserid+"的消息:"+dataContent);

        if(mainGUI != null)
        {
//			this.mainGUI.showToast(dwUserid+"说："+dataContent);
            this.mainGUI.showIMInfo_black(dwUserid+"说："+dataContent);
        }
    }

    public ChatTransDataEventImpl setMainGUI(MainGUI mainGUI)
    {
        this.mainGUI = mainGUI;
        return this;
    }

    @Override
    public void onErrorResponse(int errorCode, String errorMsg)
    {
        Log.d(TAG, "【DEBUG_UI】收到服务端错误消息，errorCode="+errorCode+", errorMsg="+errorMsg);
        this.mainGUI.showIMInfo_red("Server反馈错误码："+errorCode+",errorMsg="+errorMsg);
    }
}
