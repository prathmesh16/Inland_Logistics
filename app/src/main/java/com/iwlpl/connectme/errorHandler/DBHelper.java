package com.iwlpl.connectme.errorHandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.iwlpl.connectme.data_handler.ErrorDetails;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    public static final String KEY_DATABASE_NAME = "MyDBName.db";
    public static final String KEY_ERROR_TABLE_NAME = "errors";
    public static final String KEY_ROWID = "id";
    public static final String KEY_TIMESTAMP="timestamp";
    public static final String KEY_ACTIVITY_NAME="activity_name";
    public static final String KEY_ERROR_CLASS="error_class";
    public static final String KEY_ERROR_MESSAGE="error_Message";
    public static final String KEY_CUSTOMER_CODE="customer_code";
    public static final String KEY_LOCATION="location";
    /*public static final String KEY_QUANTITY="quantity";
    public static final String KEY_CATAGORY="catagory";*/

    public DBHelper(Context context) {
        super(context, KEY_DATABASE_NAME , null, 3);
    }

    private static final String DATABASE_CREATE =
            "CREATE TABLE if not exists " + KEY_ERROR_TABLE_NAME + " (" +
                    KEY_ROWID + " integer PRIMARY KEY autoincrement," +
                    KEY_TIMESTAMP+"  DATETIME DEFAULT CURRENT_TIMESTAMP,"+KEY_ACTIVITY_NAME+","+KEY_ERROR_CLASS+","+KEY_ERROR_MESSAGE+","+KEY_CUSTOMER_CODE+","+KEY_LOCATION+")" ;

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(DATABASE_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS errors");
        onCreate(db);
    }

    public boolean insertData (String activity_name,String error_class,String error_message,String customerCode,String location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(KEY_ACTIVITY_NAME, activity_name);
        contentValues.put(KEY_ERROR_CLASS,error_class);
        contentValues.put(KEY_ERROR_MESSAGE,error_message);
        contentValues.put(KEY_CUSTOMER_CODE,customerCode);
        contentValues.put(KEY_LOCATION,location);
        db.insert(KEY_ERROR_TABLE_NAME, null, contentValues);
        return true;
    }

   /* public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from errors where id="+id+"", null );
        return res;
    }
    public int getQuantity(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from errors where id="+id+"", null );
        if (res.getCount()==0)
            return 0;
        res.moveToFirst();
        return res.getInt(res.getColumnIndex(KEY_ERROR));
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, errors_TABLE_NAME);
        return numRows;
    }

    public boolean updateData (Integer id,String name,String image,int price,int quantity,String catagory ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("price", price);
        contentValues.put("image",image);
        contentValues.put("quantity",quantity);
        contentValues.put("catagory",catagory);
        db.update("errors", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }
*/
    public Integer deleteData (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("errors",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public ArrayList<ErrorDetails> getAllData() {
        ArrayList<ErrorDetails> array_list = new ArrayList<ErrorDetails>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+KEY_ERROR_TABLE_NAME, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            ErrorDetails error=new ErrorDetails(
                    res.getInt(res.getColumnIndex(KEY_ROWID)),
                    res.getString(res.getColumnIndex(KEY_ACTIVITY_NAME)),
                    res.getString(res.getColumnIndex(KEY_CUSTOMER_CODE)),
                    res.getString(res.getColumnIndex(KEY_TIMESTAMP))
                    ,res.getString(res.getColumnIndex(KEY_ERROR_CLASS)),
                    res.getString(res.getColumnIndex(KEY_ERROR_MESSAGE)));
            array_list.add(error);
            res.moveToNext();
        }
        return array_list;
    }
}