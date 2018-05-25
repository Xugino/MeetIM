package com.xieyangzhe.meetim.Models;

import com.github.bassaer.chatmessageview.model.Message;

import org.jivesoftware.smackx.time.packet.Time;

import java.util.Calendar;

/**
 * Created by joseph on 5/22/18.
 */

public class ChatMessage {

    private String msgBody;
    private String username;
    private Calendar msgTime;
    private boolean isMe;

    public ChatMessage(String msgBody, String username, Calendar msgTime, boolean isMe) {
        this.msgBody = msgBody;
        this.username = username;
        this.msgTime = msgTime;
        this.isMe = isMe;
    }

    public String getMsgBody() {
        return msgBody;
    }

    public void setMsgBody(String msgBody) {
        this.msgBody = msgBody;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Calendar getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(Calendar msgTime) {
        this.msgTime = msgTime;
    }

    public boolean isMe() {
        return isMe;
    }

    public void setMe(boolean me) {
        isMe = me;
    }
}
