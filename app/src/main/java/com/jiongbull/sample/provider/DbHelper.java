package com.jiongbull.sample.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库操作辅助类.
 */
public class DbHelper extends SQLiteOpenHelper {

    /** 数据库名称. */
    private static final String DB_NAME = "provider.db";
    /** 版本号. */
    private static final int DB_VERSION = 1;

    /** 创建book表的SQL. */
    private static final String SQL_CREATE_BOOK = "CREATE TABLE " + BookMetaData.Book.TABLE_NAME
            + "(" + BookMetaData.Book._ID + " INTEGER PRIMARY KEY,"
            + BookMetaData.Book.NAME + " TEXT,"
            + BookMetaData.Book.ISBN + " TEXT,"
            + BookMetaData.Book.AUTHOR + " TEXT,"
            + BookMetaData.Book.CREATED + " INTEGER,"
            + BookMetaData.Book.MODIFIED + " INTEGER"
            + ");";

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_BOOK);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}