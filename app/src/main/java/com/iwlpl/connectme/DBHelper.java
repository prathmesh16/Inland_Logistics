package com.iwlpl.connectme;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

import com.iwlpl.connectme.data_handler.DataNotification;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyDBName.db";
    public static final String NOTIFICATION_TABLE_NAME = "notification";
    public static final String NOTIFICATION_COLUMN_ID = "id";
    public static final String NOTIFICATION_COLUMN_TITLE = "title";
    public static final String NOTIFICATION_COLUMN_TIME = "time";
    public static final String NOTIFICATION_COLUMN_MSG = "msg";
    private HashMap hp;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table notification " +
                        "(id integer primary key, title text,time text,msg text) "
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS notification");
        onCreate(db);
    }

    public boolean insertNotification (String title,String time,String msg) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        contentValues.put("time", time);
        contentValues.put("msg", msg);
        db.insert("notification", null, contentValues);
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from notification where id="+id+"", null );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, NOTIFICATION_TABLE_NAME);
        return numRows;
    }

    public boolean updateNotification (Integer id,String title,String time,String msg ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        contentValues.put("time", time);
        contentValues.put("msg", msg);
        db.update("notification", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteNotification (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("notification",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public ArrayList<DataNotification> getAllNotifications() {
        ArrayList<DataNotification> array_list = new ArrayList<DataNotification>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from notification", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            DataNotification dataNotification=new DataNotification(res.getString(res.getColumnIndex(NOTIFICATION_COLUMN_TITLE)),res.getString(res.getColumnIndex(NOTIFICATION_COLUMN_TIME)),res.getString(res.getColumnIndex(NOTIFICATION_COLUMN_MSG)));
            array_list.add(dataNotification);
            res.moveToNext();
        }
        return array_list;
    }
}