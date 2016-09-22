package com.github.drizzleme.client.event;

/**
 * Created with drizzleIM
 *
 * @author ; DRIZZLEME
 *         DATE : 2016/9/22
 */
public interface ChatBaseEvent {
     void onLoginMessage(int paramInt1, int paramInt2);

     void onLinkCloseMessage(int paramInt);
    
}
