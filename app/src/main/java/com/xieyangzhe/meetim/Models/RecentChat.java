package com.xieyangzhe.meetim.Models;

import java.util.Calendar;

/**
 * Created by joseph on 6/15/18.
 */

public class RecentChat {
    private Contact contact;
    private Calendar chatTime;
    private String chatMsg;

    public RecentChat(Contact contact, Calendar chatTime, String chatMsg) {
        this.contact = contact;
        this.chatTime = chatTime;
        this.chatMsg = chatMsg;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public Calendar getChatTime() {
        return chatTime;
    }

    public void setChatTime(Calendar chatTime) {
        this.chatTime = chatTime;
    }

    public String getChatMsg() {
        return chatMsg;
    }

    public void setChatMsg(String chatMsg) {
        this.chatMsg = chatMsg;
    }
}
