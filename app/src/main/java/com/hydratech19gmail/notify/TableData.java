package com.hydratech19gmail.notify;

import android.provider.BaseColumns;

/**
 * Created by zappereton on 15/9/16.
 */
public class TableData {

    public TableData(){

    }

    public static abstract class TableInfo implements BaseColumns{
        public static final String USERNAME = "user_name";
        public static final String TOKEN = "token";
        public static final String DATABASE_NAME = "notify";
        public static final String TABLE_NAME = "user_info";
    }
}
