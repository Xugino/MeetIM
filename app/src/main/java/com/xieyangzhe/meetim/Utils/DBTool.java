package com.xieyangzhe.meetim.Utils;

import android.app.Fragment;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.xieyangzhe.meetim.Models.ChatMessage;
import com.xieyangzhe.meetim.Models.ChatMessageList;
import com.xieyangzhe.meetim.Models.Contact;
import com.xieyangzhe.meetim.Models.RecentChat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by joseph on 5/25/18.
 */

public class DBTool {
    private SQLiteDatabase db;
    private DBHelper helper;

    public DBTool() {
        helper = new DBHelper(IMApplication.getAppContext());
    }

    public void addSingleMessage(ChatMessage chatMessage, String username) {

        db = helper.getWritableDatabase();
        //Log.d("INFO", "addSingleMessage: " + username);
        db.execSQL("create table if not exists " + username +
                "(id integer primary key autoincrement, " +
                "content varcahr(1024), " +
                "fromuser varchar(30)," +
                " sendtime Integer," +
                " isme Integer," +
                "type Integer)"
        );

        ContentValues values = new ContentValues();
        values.put("content", chatMessage.getMsgBody());
        values.put("fromuser", chatMessage.getUsername());
        values.put("sendtime", chatMessage.getMsgTime().getTime().getTime());
        values.put("isme", chatMessage.isMe() ? 1 : 0);
        values.put("type", chatMessage.isPic() ? 1 : 0);
        db.insert(username, null, values);
        db.close();
    }

    public ChatMessageList get(String username) {
        Log.d("qqqqqq", username);
        ChatMessageList chatMessageList = new ChatMessageList();
        db = helper.getReadableDatabase();
        Cursor cursor;
        try {
            cursor = db.query(username, null, null, null, null, null, null);
        } catch (Exception e) {
            return null;
        }

        if (cursor.moveToFirst()) {
            do {
                String content = cursor.getString(cursor.getColumnIndex("content"));
                String fromuser = cursor.getString(cursor.getColumnIndex("fromuser"));
                Calendar sendtime = Calendar.getInstance();
                sendtime.setTimeInMillis(cursor.getLong(cursor.getColumnIndex("sendtime")));
                boolean isme = (cursor.getInt(cursor.getColumnIndex("isme")) == 1) ? true : false;
                boolean isPic = (cursor.getInt(cursor.getColumnIndex("type")) == 1) ? true : false;
                chatMessageList.addChatMessage(new ChatMessage(content, fromuser, sendtime, isme, isPic));
            } while (cursor.moveToNext());
        }
        db.close();
        return chatMessageList;

    }

    public ArrayList<RecentChat> getRecentChats() {
        ArrayList<RecentChat> recentChats = new ArrayList<>();
        db = helper.getReadableDatabase();
        String tmpUsername;
        Cursor cursor;

        for (Contact contact : XMPPTool.getXmppTool().contactList) {

            tmpUsername = contact.getName();
            try {
                cursor = db.query(tmpUsername, null, null, null, null, null, null);
                if (cursor.getCount() != 0) {
                    cursor.moveToLast();

                    String chatMsg = cursor.getString(cursor.getColumnIndex("content"));
                    if (chatMsg.contains(".png")) {
                        chatMsg = "[Picture]";
                    }
                    Calendar sendtime = Calendar.getInstance();
                    sendtime.setTimeInMillis(cursor.getLong(cursor.getColumnIndex("sendtime")));
                    RecentChat recentChat = new RecentChat(contact, sendtime, chatMsg);
                    Log.d("aaaaaaaaaaa", chatMsg);
                    recentChats.add(recentChat);

                    Log.d("test", chatMsg);
                }
            } catch (Exception e) {
                Log.d("Error", "getRecentChats: " + e.getMessage());
            }
        }
        db.close();
        Collections.sort(recentChats, new SortByTime());

        return recentChats;
    }

    class SortByTime implements Comparator {
        @Override
        public int compare(Object o1, Object o2) {
            RecentChat chat1 = (RecentChat) o1;
            RecentChat chat2 = (RecentChat) o2;
            if (chat1.getChatTime().getTimeInMillis() < chat2.getChatTime().getTimeInMillis()) {
                return 1;
            } else {
                return -1;
            }
        }
    }
}
