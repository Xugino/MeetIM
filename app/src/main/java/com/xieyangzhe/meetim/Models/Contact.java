package com.xieyangzhe.meetim.Models;

import android.graphics.Bitmap;

import com.github.bassaer.chatmessageview.model.IChatUser;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by joseph on 5/22/18.
 */

public class Contact implements IChatUser {
    private String jid;
    private String name;

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    private String head;

    public Contact(String jid, String name, String head) {
        this.jid = jid;
        this.name = name;
        this.head = head;
    }

    public String getJid() {
        return jid;
    }

    public void setJid(String jid) {
        this.jid = jid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotNull
    @Override
    public String getId() {
        return jid;
    }

    @Nullable
    @Override
    public Bitmap getIcon() {
        return null;
    }

    @Override
    public void setIcon(Bitmap bitmap) {

    }
}
