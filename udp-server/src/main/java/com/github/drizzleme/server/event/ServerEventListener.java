package com.github.drizzleme.server.event;

import org.apache.mina.core.session.IoSession;

/**
 * Created with drizzleIM
 *
 * @author ; DRIZZLEME
 *         DATE : 2016/9/22
 */
public interface ServerEventListener {
    int onVerifyUserCallBack(String paramString1, String paramString2, String extra);

    void onUserLoginAction_CallBack(int paramInt, String paramString, IoSession paramIoSession);

    void onUserLogoutAction_CallBack(int paramInt, Object paramObject);

    boolean onTransBuffer_CallBack(int paramInt1, int paramInt2, String paramString1, String paramString2);

    void onTransBuffer_C2C_CallBack(int paramInt1, int paramInt2, String paramString);

    boolean onTransBuffer_C2C_RealTimeSendFaild_CallBack(int paramInt1, int paramInt2, String paramString1, String paramString2);
}
