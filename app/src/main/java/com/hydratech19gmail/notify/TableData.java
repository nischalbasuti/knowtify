package com.hydratech19gmail.notify;

import android.provider.BaseColumns;

/**
 * Created by zappereton on 15/9/16.
 */
public class TableData {

    public TableData(){

    }

    public static abstract class TableInfo implements BaseColumns{
        public static final String BROADCAST_NAME = "broadcast_name";
        public static final String NOTIFICATION_NAME = "notification_name";
        public static final String NOTIFICATION_SUBJECT = "notification_subject";
        public static final String NOTIFICATION_CONTENT = "notification_content";
        public static final String NOTIFICATIONS_TIMESTAMP = "notification_timestamp";
        public static final String DATABASE_NAME = "notify";
        public static final String TABLE_NAME = "notifications";
    }
}
