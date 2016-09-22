package com.github.drizzleme.server.event;

import com.github.drizzleme.core.bo.Protocal;

import java.util.ArrayList;

/**
 * Created with drizzleIM
 *
 * @author ; DRIZZLEME
 *         DATE : 2016/9/22
 */
public interface MessageQoSEventListenerS2C {
   void messagesLost(ArrayList<Protocal> paramArrayList);

    void messagesBeReceived(String paramString);
}
