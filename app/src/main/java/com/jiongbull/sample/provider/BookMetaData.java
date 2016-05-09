package com.jiongbull.sample.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * book的元数据.
 */
public class BookMetaData {

    /** 授权地地址. */
    public static final String AUTHORITY = "com.jiongbull.sample.provider.BookProvider";
    /** 授权uri. */
    public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

    private BookMetaData() {
    }

    /**
     * book表对象.
     */
    public static final class Book implements BaseColumns {

        private Book() {
        }

        /** 表名. */
        public static final String TABLE_NAME = "book";
        /** 该表的content uri. */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, "books");

        /** book集合的MIME类型. */
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.com.jiongbull.provider.book";
        /** 单个book的MIME类型. */
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.com.jiongbull.provider.book";

        /** 默认排序. */
        public static final String DEFAULT_SORT_ORDER = "modified DESC";

        /* 列定义. */
        public static final String NAME = "name";
        public static final String ISBN = "isbn";
        public static final String AUTHOR = "author";
        public static final String CREATED = "created";
        public static final String MODIFIED = "modified";
    }
}