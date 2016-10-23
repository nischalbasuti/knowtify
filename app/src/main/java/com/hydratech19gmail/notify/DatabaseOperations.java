package com.hydratech19gmail.notify;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by zappereton on 15/9/16.
 */
public class DatabaseOperations extends SQLiteOpenHelper{
    private final String TAG = "DatabaseOperations";

    public static final int database_version = 1;
    public String CREATE_QUERY = "CREATE TABLE "+TableData.TableInfo.TABLE_NAME+
            "("+TableData.TableInfo.BROADCAST_NAME+" TEXT,"+
            TableData.TableInfo.NOTIFICATION_NAME+" TEXT,"+
            TableData.TableInfo.NOTIFICATION_SUBJECT+" TEXT,"+
            TableData.TableInfo.NOTIFICATION_CONTENT+" TEXT,"+
            TableData.TableInfo.NOTIFICATIONS_TIMESTAMP+" TEXT);";

    public DatabaseOperations(Context context) {
        super(context, TableData.TableInfo.DATABASE_NAME, null, database_version);

        Log.d("Database Operations","Database created");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_QUERY);

        Log.d("Database Operations","Table created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void putNotification(DatabaseOperations dop,Notification notification){
        SQLiteDatabase sqldb = dop.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(TableData.TableInfo.BROADCAST_NAME,notification.getBroadcast());
        cv.put(TableData.TableInfo.NOTIFICATION_NAME,notification.getName());
        cv.put(TableData.TableInfo.NOTIFICATION_SUBJECT,notification.getSubject());
        cv.put(TableData.TableInfo.NOTIFICATION_CONTENT,notification.getContent());
        cv.put(TableData.TableInfo.NOTIFICATIONS_TIMESTAMP,notification.getTimeStamp());

        long k = sqldb.insert(TableData.TableInfo.TABLE_NAME,null,cv);

        Log.d("Database Operations","one row inserted "+k);

    }

    public Cursor getAllNotifications(DatabaseOperations dop){
        SQLiteDatabase sqldb = dop.getReadableDatabase();

        String[] columns = {TableData.TableInfo.BROADCAST_NAME,TableData.TableInfo.NOTIFICATION_NAME,
                            TableData.TableInfo.NOTIFICATION_SUBJECT,TableData.TableInfo.NOTIFICATION_CONTENT,
                            TableData.TableInfo.NOTIFICATIONS_TIMESTAMP};

        Cursor CR = sqldb.query(TableData.TableInfo.TABLE_NAME,columns,null,null,null,null,null);

        return CR;
    }

    public void deleteNotification(DatabaseOperations dop,String broadcastName,String notificationName,
                                   String notificationSubject){
        String selection = TableData.TableInfo.BROADCAST_NAME+ " LIKE ? AND "+
                TableData.TableInfo.NOTIFICATION_NAME+ " LIKE ? AND "+
                TableData.TableInfo.NOTIFICATION_SUBJECT+ " LIKE ?";

        String args[] = {broadcastName,notificationName,notificationSubject};
        SQLiteDatabase sqldb = dop.getWritableDatabase();

        sqldb.delete(TableData.TableInfo.TABLE_NAME,selection,args);

    }

    public void deleteAllNotification(DatabaseOperations dop){
        SQLiteDatabase sqldb = dop.getWritableDatabase();
        sqldb.execSQL("delete * from "+ TableData.TableInfo.TABLE_NAME);
    }
}
