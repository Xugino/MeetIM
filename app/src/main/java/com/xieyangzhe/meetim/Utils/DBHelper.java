package com.xieyangzhe.meetim.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by joseph on 5/25/18.
 */

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "chat_messages.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table messages (" +
                "id integer primary key autoincrement, " +
                "content varcahr(200), " +
                "fromuser varchar(30)," +
                " sendtime Integer," +
                " isme Integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
