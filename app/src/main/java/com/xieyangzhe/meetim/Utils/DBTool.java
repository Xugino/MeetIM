package com.xieyangzhe.meetim.Utils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.xieyangzhe.meetim.Models.ChatMessage;
import com.xieyangzhe.meetim.Models.ChatMessageList;

import java.util.Calendar;

/**
 * Created by joseph on 5/25/18.
 */

public class DBTool {
    private SQLiteDatabase db;
    private DBHelper helper;

    public DBTool() {
        helper = new DBHelper(IMApplication.getAppContext());
    }

    public void addSingleMessage(ChatMessage chatMessage) {
        db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("content", chatMessage.getMsgBody());
        values.put("fromuser", chatMessage.getUsername());
        values.put("sendtime", chatMessage.getMsgTime().getTime().getTime());
        values.put("isme", chatMessage.isMe() ? 1 : 0);
        db.insert("messages", null, values);
        db.close();
    }

    public void addMessageList(ChatMessageList chatMessageList) {
        db = helper.getWritableDatabase();
        ContentValues values;
        for (ChatMessage chatMessage : chatMessageList.getMessageList()) {
            values = new ContentValues();
            values.put("content", chatMessage.getMsgBody());
            values.put("fromuser", chatMessage.getUsername());
            values.put("sendtime", chatMessage.getMsgTime().getTime().getTime());
            values.put("isme", chatMessage.isMe() ? 1 : 0);
            db.insert("messages", null, values);
        }
        db.close();
    }

    public ChatMessageList get() {
        ChatMessageList chatMessageList = new ChatMessageList();
        db = helper.getReadableDatabase();
        Cursor cursor = db.query("messages", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String content = cursor.getString(cursor.getColumnIndex("content"));
                String fromuser = cursor.getString(cursor.getColumnIndex("fromuser"));
                Calendar sendtime = Calendar.getInstance();
                sendtime.setTimeInMillis(cursor.getLong(cursor.getColumnIndex("sendtime")));
                boolean isme = (cursor.getInt(cursor.getColumnIndex("isme")) == 1) ? true : false;
                chatMessageList.addChatMessage(new ChatMessage(content, fromuser, sendtime, isme));
            } while (cursor.moveToNext());
        }

        return chatMessageList;

    }
}
