package com.github.drizzleme.server.demo;

import com.github.drizzleme.server.event.ServerEventListener;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with drizzleIM
 *
 * @author ; DRIZZLEME
 *         DATE : 2016/9/22
 */
public class ServerEventListenerImpl implements ServerEventListener{
    private static Logger logger = LoggerFactory.getLogger(ServerEventListenerImpl.class);

    @Override
    public int onVerifyUserCallBack(String lpUserName, String lpPassword, String extra)
    {
        logger.debug("正在调用回调方法：OnVerifyUserCallBack...(extra="+extra+")");
        return 0;
    }

    @Override
    public void onUserLoginAction_CallBack(int userId, String userName,
                                           IoSession session)
    {
        logger.debug("正在调用回调方法：OnUserLoginAction_CallBack...");
    }

    @Override
    public void onUserLogoutAction_CallBack(int userId, Object obj)
    {
        logger.debug("正在调用回调方法：OnUserLogoutAction_CallBack...");
    }

//	@Override
//	public void OnRecvUserFilterData_CallBack(int userId, Pointer buf, int len,
//			Pointer userValue)
//	{
//		logger.debug("正在调用回调方法：OnRecvUserFilterData_CallBack...");
//	}

    @Override
    public boolean onTransBuffer_CallBack(int userId, int from_user_id,
                                          String dataContent, String fingerPrint)
    {
        logger.debug("收到了客户端"+from_user_id+"发给服务端的消息：str="+dataContent);
        return true;
    }

    @Override
    public void onTransBuffer_C2C_CallBack(int userId, int from_user_id,
                                           String dataContent)
    {
        logger.debug("收到了客户端"+from_user_id+"发给客户端"+userId+"的消息：str="+dataContent);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onTransBuffer_C2C_RealTimeSendFaild_CallBack(int userId,
                                                                int from_user_id, String dataContent, String fingerPring)
    {
        logger.debug("客户端"+from_user_id+"发给客户端"+userId+"的消息：str="+dataContent
                +"因实时发送没有成功，需要上层应用作离线处理哦，否则此消息将被丢弃.");
        return false;
    }
}
