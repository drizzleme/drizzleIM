package com.github.drizzleme.client.core;

import com.github.drizzleme.client.ClientCoreSDK;
import static com.github.drizzleme.core.util.Log.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created with drizzleIM
 *
 * @author ; DRIZZLEME
 *         DATE : 2016/9/22
 */
public class AutoReLoginDaemon {
    private static final String TAG = AutoReLoginDaemon.class.getSimpleName();

    public static int AUTO_RE$LOGIN_INTERVAL = 2000;

    private boolean autoReLoginRunning = false;

    private boolean _excuting = false;

    private Timer timer = null;

    private static AutoReLoginDaemon instance = null;

    public static AutoReLoginDaemon getInstance()
    {
        if (instance == null)
            instance = new AutoReLoginDaemon();
        return instance;
    }

    private AutoReLoginDaemon()
    {
        init();
    }

    private void init()
    {
        this.timer = new Timer(AUTO_RE$LOGIN_INTERVAL, new ActionListener()
        {
            public void actionPerformed(ActionEvent e) {
                AutoReLoginDaemon.this.run();
            }
        });
    }

    public void run() {
        if (!this._excuting)
        {
            this._excuting = true;
            if (ClientCoreSDK.DEBUG)
                d(TAG, "【IMCORE】自动重新登陆线程执行中, autoReLogin?" + ClientCoreSDK.autoReLogin + "...");
            int code = -1;
            // 是否允许自动重新登陆哦
            if (ClientCoreSDK.autoReLogin)
            {
                LocalUDPSocketProvider.getInstance().closeLocalUDPSocket();

                // 发送重登陆请求
                code = LocalUDPDataSender.getInstance().sendLogin(
                        ClientCoreSDK.getInstance().getCurrentLoginName()
                        , ClientCoreSDK.getInstance().getCurrentLoginPsw()
                        , ClientCoreSDK.getInstance().getCurrentLoginExtra());
            }

            if (code == 0)
            {
                LocalUDPDataReciever.getInstance().startup();
            }

            this._excuting = false;
        }
    }

    public void stop()
    {
        if (this.timer != null) {
            this.timer.stop();
        }
        this.autoReLoginRunning = false;
    }

    public void start(boolean immediately)
    {
        stop();

        if (immediately)
            this.timer.setInitialDelay(0);
        else
            this.timer.setInitialDelay(AUTO_RE$LOGIN_INTERVAL);
        this.timer.start();

        this.autoReLoginRunning = true;
    }

    public boolean isautoReLoginRunning()
    {
        return this.autoReLoginRunning;
    }
}
