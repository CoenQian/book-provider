package com.jiongbull.sample.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.HashMap;

/**
 * Book provider.
 */
public class BookProvider extends ContentProvider {

    /** uri匹配books的标识. */
    private static final int BOOKS = 1;
    /** uri匹配book id的标识. */
    private static final int BOOK_ID = 2;

    private DbHelper mDbHelper;

    /** uri适配器. */
    private static UriMatcher sUriMatcher;
    /** book表的列映射. */
    private static HashMap<String, String> sBooksProjectionMap;

    static {
        /* 配置UriMatcher. */
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(BookMetaData.AUTHORITY, "books", BOOKS);
        sUriMatcher.addURI(BookMetaData.AUTHORITY, "books/#", BOOK_ID);

        /* 列的映射. */
        sBooksProjectionMap = new HashMap<>();
        sBooksProjectionMap.put(BookMetaData.Book._ID, BookMetaData.Book._ID);
        sBooksProjectionMap.put(BookMetaData.Book.NAME, BookMetaData.Book.NAME);
        sBooksProjectionMap.put(BookMetaData.Book.ISBN, BookMetaData.Book.ISBN);
        sBooksProjectionMap.put(BookMetaData.Book.AUTHOR, BookMetaData.Book.AUTHOR);
        sBooksProjectionMap.put(BookMetaData.Book.CREATED, BookMetaData.Book.CREATED);
        sBooksProjectionMap.put(BookMetaData.Book.MODIFIED, BookMetaData.Book.MODIFIED);
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new DbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(BookMetaData.Book.TABLE_NAME);
        switch (sUriMatcher.match(uri)) {
            case BOOKS:
                queryBuilder.setProjectionMap(sBooksProjectionMap);
                break;
            case BOOK_ID:
                queryBuilder.setProjectionMap(sBooksProjectionMap);
                queryBuilder.appendWhere(BookMetaData.Book._ID + " = " + uri.getPathSegments().get(1));
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        String orderBy = TextUtils.isEmpty(sortOrder) ? BookMetaData.Book.DEFAULT_SORT_ORDER : sortOrder;
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        return queryBuilder.query(
                db,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                orderBy);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case BOOKS:
                return BookMetaData.Book.CONTENT_TYPE;
            case BOOK_ID:
                return BookMetaData.Book.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        if (sUriMatcher.match(uri) != BOOKS) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        long rowId = db.insert(BookMetaData.Book.TABLE_NAME, null, values);

        if (rowId > 0) {
            return ContentUris.withAppendedId(BookMetaData.Book.CONTENT_URI, rowId);
        }
        throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        int count;
        String finalSelect;
        switch (sUriMatcher.match(uri)) {
            case BOOKS:
                count = db.delete(BookMetaData.Book.TABLE_NAME, selection, selectionArgs);
                break;
            case BOOK_ID:
                finalSelect = BookMetaData.Book._ID + " = " + uri.getPathSegments().get(1);
                if (!TextUtils.isEmpty(selection)) {
                    finalSelect += " AND " + selection;
                }
                count = db.delete(BookMetaData.Book.TABLE_NAME, finalSelect, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        int count;
        String finalSelect;
        switch (sUriMatcher.match(uri)) {
            case BOOKS:
                count = db.update(BookMetaData.Book.TABLE_NAME, values, selection, selectionArgs);
                break;
            case BOOK_ID:
                finalSelect = BookMetaData.Book._ID + " = " + uri.getPathSegments().get(1);
                if (!TextUtils.isEmpty(selection)) {
                    finalSelect += " AND " + selection;
                }
                count = db.update(BookMetaData.Book.TABLE_NAME, values, finalSelect, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        return count;
    }
}