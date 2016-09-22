package com.github.drizzleme.client.event;

/**
 * Created with drizzleIM
 *
 * @author ; DRIZZLEME
 *         DATE : 2016/9/22
 */
public interface ChatTransDataEvent {
     void onTransBuffer(String paramString1, int paramInt, String paramString2);

     void onErrorResponse(int paramInt, String paramString);
}
