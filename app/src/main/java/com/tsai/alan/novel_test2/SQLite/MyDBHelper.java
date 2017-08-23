package com.tsai.alan.novel_test2.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Alan on 2017/7/22.
 */

public class MyDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MySQL";
    private static final int DATABASE_VERSION = 1;
    public MyDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE  TABLE NOVEL " +
                "(_id INTEGER PRIMARY KEY  NOT NULL , " +
                "title text NOT NULL , " +
                "author text, " +
                "classification text, " +
                "page text, " +
                "url text, " +
                "Bookmarks INTEGER, " +
                "BookmarksPage INTEGER"+
                "mark BOOLEAN,)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
