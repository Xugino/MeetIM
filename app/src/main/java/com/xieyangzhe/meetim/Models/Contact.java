package com.xieyangzhe.meetim.Models;

/**
 * Created by joseph on 5/22/18.
 */

public class Contact {
    private String jid;
    private String name;

    public Contact(String jid, String name) {
        this.jid = jid;
        this.name = name;
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
}
