package com.github.drizzleme.client.core;

import com.github.drizzleme.client.ClientCoreSDK;
import com.github.drizzleme.client.cnf.ConfigEntity;

import static com.github.drizzleme.core.util.Log.*;

import java.net.DatagramSocket;

/**
 * Created with drizzleIM
 *
 * @author ; DRIZZLEME
 *         DATE : 2016/9/22
 */
public class LocalUDPSocketProvider {
    private static final String TAG = LocalUDPSocketProvider.class.getSimpleName();

    private DatagramSocket localUDPSocket = null;

    private static LocalUDPSocketProvider instance = null;

    public static LocalUDPSocketProvider getInstance()
    {
        if (instance == null)
            instance = new LocalUDPSocketProvider();
        return instance;
    }

    private DatagramSocket resetLocalUDPSocket()
    {
        try
        {
            closeLocalUDPSocket();
            if (ClientCoreSDK.DEBUG)
                d(TAG, "【IMCORE】new DatagramSocket()中...");
            this.localUDPSocket = (ConfigEntity.localUDPPort == 0 ?
                    new DatagramSocket() : new DatagramSocket(ConfigEntity.localUDPPort));
            this.localUDPSocket.setReuseAddress(true);
            if (ClientCoreSDK.DEBUG) {
                d(TAG, "【IMCORE】new DatagramSocket()已成功完成.");
            }

            return this.localUDPSocket;
        }
        catch (Exception e)
        {
            w(TAG, "【IMCORE】localUDPSocket创建时出错，原因是：" + e.getMessage(), e);

            closeLocalUDPSocket();
        }

        return null;
    }

    private boolean isLocalUDPSocketReady()
    {
        return (this.localUDPSocket != null) && (!this.localUDPSocket.isClosed());
    }

    public DatagramSocket getLocalUDPSocket()
    {
        if (isLocalUDPSocketReady())
        {
            if (ClientCoreSDK.DEBUG)
                d(TAG, "【IMCORE】isLocalUDPSocketReady()==true，直接返回本地socket引用哦。");
            return this.localUDPSocket;
        }

        if (ClientCoreSDK.DEBUG)
            d(TAG, "【IMCORE】isLocalUDPSocketReady()==false，需要先resetLocalUDPSocket()...");
        return resetLocalUDPSocket();
    }

    public void closeLocalUDPSocket()
    {
        try
        {
            if (ClientCoreSDK.DEBUG)
                d(TAG, "【IMCORE】正在closeLocalUDPSocket()...");
            if (this.localUDPSocket != null)
            {
                this.localUDPSocket.close();
                this.localUDPSocket = null;
            }
            else
            {
                d(TAG, "【IMCORE】Socket处于未初化状态（可能是您还未登陆），无需关闭。");
            }
        }
        catch (Exception e)
        {
            w(TAG, "【IMCORE】lcloseLocalUDPSocket时出错，原因是：" + e.getMessage(), e);
        }
    }
}
