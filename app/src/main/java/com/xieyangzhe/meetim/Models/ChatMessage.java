package com.xieyangzhe.meetim.Models;

import org.jivesoftware.smackx.time.packet.Time;

/**
 * Created by joseph on 5/22/18.
 */

public class ChatMessage {

    private String msgBody;
    private int from;
    private Time msgTime;

    public ChatMessage(String msgBody, int from, Time msgTime) {
        this.msgBody = msgBody;
        this.from = from;
        this.msgTime = msgTime;
    }

    public String getMsgBody() {
        return msgBody;
    }

    public void setMsgBody(String msgBody) {
        this.msgBody = msgBody;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public Time getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(Time msgTime) {
        this.msgTime = msgTime;
    }
}
