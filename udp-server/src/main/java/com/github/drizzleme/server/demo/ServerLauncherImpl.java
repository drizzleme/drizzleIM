package com.github.drizzleme.server.demo;

import com.github.drizzleme.server.ServerLauncher;
import com.github.drizzleme.server.qos.QoS4SendDaemonS2C;

import java.io.IOException;

/**
 * Created with drizzleIM
 *
 * @author ; DRIZZLEME
 *         DATE : 2016/9/22
 */
public class ServerLauncherImpl extends ServerLauncher{
    private static ServerLauncherImpl instance = null;

    public static ServerLauncherImpl getInstance() throws IOException
    {
        if(instance == null)
        {
            // 设置AppKey
            ServerLauncher.appKey = "5418023dfd98c579b6001741";
            QoS4SendDaemonS2C.DEBUG = true;
            ServerLauncherImpl.PORT = 7901;
//			ServerLauncherImpl.setSenseMode(SenseMode.MODE_10S);
            instance = new ServerLauncherImpl();
        }
        return instance;
    }

    public ServerLauncherImpl() throws IOException
    {
        super();
    }

    /**
     * 初始化消息处理事件监听者.
     */
    @Override
    protected void initListeners()
    {
        // ** 设置回调
        this.setServerEventListener(new ServerEventListenerImpl());
        this.setServerMessageQoSEventListener(new MessageQoSEventS2CListnerImpl());
    }

    public static void main(String[] args) throws IOException
    {
        ServerLauncherImpl.getInstance().startup();
    }
}
