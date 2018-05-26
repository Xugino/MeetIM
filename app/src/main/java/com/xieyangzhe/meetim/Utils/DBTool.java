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

    public void addSingleMessage(ChatMessage chatMessage, String username) {

        db = helper.getWritableDatabase();
        //Log.d("INFO", "addSingleMessage: " + username);
        db.execSQL("create table if not exists " + username +
                "(id integer primary key autoincrement, " +
                "content varcahr(200), " +
                "fromuser varchar(30)," +
                " sendtime Integer," +
                " isme Integer)"
        );

        ContentValues values = new ContentValues();
        values.put("content", chatMessage.getMsgBody());
        values.put("fromuser", chatMessage.getUsername());
        values.put("sendtime", chatMessage.getMsgTime().getTime().getTime());
        values.put("isme", chatMessage.isMe() ? 1 : 0);
        db.insert(username, null, values);
        db.close();
    }

    public ChatMessageList get(String username) {
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
                chatMessageList.addChatMessage(new ChatMessage(content, fromuser, sendtime, isme));
            } while (cursor.moveToNext());
        }
        db.close();
        return chatMessageList;

    }
}
