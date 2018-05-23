package com.xieyangzhe.meetim.Utils;

import android.content.Context;
import android.util.Log;

import com.xieyangzhe.meetim.Models.ChatMessage;
import com.xieyangzhe.meetim.Models.Contact;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.iqregister.AccountManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joseph on 5/22/18.
 */

public class XMPPTool extends XMPPTCPConnection {

    private static XMPPTool xmppTool;

    private final static String SERVER_NAME = "xmpp.xieyangzhe.com";
    private final static String SERVER_IP = "39.105.73.30";
    private final static int PORT_NUMBER = 5222;
    private final static int TIME_OUT = 6000;

    private static ChatManager chatManager;

    private XMPPTool(XMPPTCPConnectionConfiguration config) {
        super(config);
    }

    public static synchronized XMPPTool getXmppTool() {
        if (xmppTool == null) {
            try {
                XMPPTCPConnectionConfiguration.Builder builder = XMPPTCPConnectionConfiguration.builder();
                builder.setConnectTimeout(TIME_OUT);
                builder.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
                builder.setResource("Android");
                builder.setServiceName(SERVER_IP);
                builder.setHost(SERVER_IP);
                builder.setPort(PORT_NUMBER);
                builder.setDebuggerEnabled(true);
                builder.setCompressionEnabled(false);
                xmppTool = new XMPPTool(builder.build());
                xmppTool.connect();
                chatManager = ChatManager.getInstanceFor(getXmppTool());

                chatManager.addChatListener(new ChatManagerListener() {
                    @Override
                    public void chatCreated(Chat chat, boolean createdLocally) {
                        if (!createdLocally) {
                            chat.addMessageListener(new ChatMessageListener() {
                                @Override
                                public void processMessage(Chat chat, Message message) {
                                    //TODO: Process message
                                    Log.d("CHAT", message.toString());
                                }
                            });
                        }
                    }
                });
                //tool.setPacketReplyTimeout(TIME_OUT);
            } catch (Exception e) {
                Log.d("ERROR", "getXmppTool: " + e.getMessage());
            }
        }
        return xmppTool;
    }

    private void checkConnection() {
        if (!getXmppTool().isConnected()) {
            try {
                getXmppTool().connect();
            } catch (Exception e) {
                Log.d("ERROR", "connectStatus: " + e.getMessage());
            }
        }
    }

    private void cutConnection() {
        if (xmppTool == null) {
            return;
        } else if (getXmppTool().isConnected()) {
            getXmppTool().disconnect();
            xmppTool = null;
        }
    }

    private void checkLogin() {
        if (!getXmppTool().isAuthenticated()) {
            try {
                getXmppTool().login(
                        (String) PreferencesUtils.getInstance().getData("username", ""),
                        (String) PreferencesUtils.getInstance().getData("password", "")
                );
            } catch (Exception e) {
                Log.d("ERROR", "checkLogin: " + e.getMessage());
            }
        }
    }

    public boolean register(String username, String password) {
        checkConnection();
        AccountManager accountManager = AccountManager.getInstance(getXmppTool());
        accountManager.sensitiveOperationOverInsecureConnection(true);
        try {
            accountManager.createAccount(username, password);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean doLogin(String username, String password) {
        try {
            checkConnection();
            if (getXmppTool().isAuthenticated()) {
                return true;
            } else {
                getXmppTool().login(username, password);
                return getXmppTool().isAuthenticated();
            }
        } catch (Exception e) {
            Log.d("ERROR", "doLogin: " + e.getMessage());
        }
        return false;
    }

    public List<Contact> getContactList() {
        List<Contact> contactList = new ArrayList<>();
        for (RosterEntry entry : Roster.getInstanceFor(getXmppTool()).getEntries()) {
            Contact contact = new Contact(entry.getUser(), entry.getName());
            contactList.add(contact);
        }
        return contactList;
    }

    public boolean sendMessage(String msgBody, Contact contact) {
        checkConnection();
        checkLogin();
        Chat chat = chatManager.createChat(contact.getJid() + "@" + SERVER_IP);
        try {
            chat.sendMessage(msgBody);
        } catch (Exception e) {
            Log.d("ERROR", "sendMessage: " + e.getMessage());
            return false;
        }
        chat.close();
        return true;
    }

    public boolean addContact(String username) {
        try {
            checkConnection();
            checkLogin();
            Roster roster = Roster.getInstanceFor(getXmppTool());
            roster.createEntry(username, username, null);
        } catch (Exception e) {
            Log.d("ERROR", "addContact: " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean deleteContact(Contact contact) {
        try {
            checkConnection();
            checkLogin();
            Roster roster = Roster.getInstanceFor(getXmppTool());
            roster.removeEntry(roster.getEntry(contact.getJid()));
        } catch (Exception e) {
            Log.d("ERROR", "deleteContact: " + e.getMessage());
            return false;
        }
        return true;
    }
}
