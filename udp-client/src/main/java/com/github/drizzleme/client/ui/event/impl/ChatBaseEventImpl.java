package com.github.drizzleme.client.ui.event.impl;

import com.github.drizzleme.client.event.ChatBaseEvent;
import com.github.drizzleme.client.ui.MainGUI;
import com.github.drizzleme.core.util.Log;

import java.util.Observer;

/**
 * Created with drizzleIM
 *
 * @author ; DRIZZLEME
 *         DATE : 2016/9/22
 */
public class ChatBaseEventImpl implements ChatBaseEvent{
    private final static String TAG = ChatBaseEventImpl.class.getSimpleName();

    private MainGUI mainGUI = null;

    // 本Observer目前仅用于登陆时（因为登陆与收到服务端的登陆验证结果
    // 是异步的，所以有此观察者来完成收到验证后的处理）
    private Observer loginOkForLaunchObserver = null;

    @Override
    public void onLoginMessage(int dwUserId, int dwErrorCode)
    {
        if (dwErrorCode == 0)
        {
            Log.p(TAG, "【DEBUG_UI】登录成功，当前分配的user_id=！"+dwUserId);

            // TODO 以下代码仅用于DEMO哦
            if(this.mainGUI != null)
            {
                this.mainGUI.refreshMyid();
                this.mainGUI.showIMInfo_green("登录成功,id="+dwUserId);
            }
        }
        else
        {
            Log.e(TAG, "【DEBUG_UI】登录失败，错误代码：" + dwErrorCode);

            // TODO 以下代码仅用于DEMO哦
            if(this.mainGUI != null)
            {
                this.mainGUI.refreshMyid();
                this.mainGUI.showIMInfo_red("登录失败,code="+dwErrorCode);
            }
        }

        // 此观察者只有开启程序首次使用登陆界面时有用
        if(loginOkForLaunchObserver != null)
        {
            loginOkForLaunchObserver.update(null, dwErrorCode);
            loginOkForLaunchObserver = null;
        }
    }

    @Override
    public void onLinkCloseMessage(int dwErrorCode)
    {
        Log.e(TAG, "【DEBUG_UI】网络连接出错关闭了，error：" + dwErrorCode);

        // TODO 以下代码仅用于DEMO哦
        if(this.mainGUI != null)
        {
            this.mainGUI.refreshMyid();
            this.mainGUI.showIMInfo_red("服务器连接已断开,error="+dwErrorCode);
        }
    }

    public void setLoginOkForLaunchObserver(Observer loginOkForLaunchObserver)
    {
        this.loginOkForLaunchObserver = loginOkForLaunchObserver;
    }

    public ChatBaseEventImpl setMainGUI(MainGUI mainGUI)
    {
        this.mainGUI = mainGUI;
        return this;
    }
}
