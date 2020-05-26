package com.example.appiii.ui.Member;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.appiii.C_Dictionary;

public class C_Member_SQLite extends SQLiteOpenHelper {

//    public static final String MY_Table_Name = "MY_Table_Name";
//    public static final String TABLE_SCHEMA_DATE = "COLUMN_NAME_DATE";
//    public static final String TABLE_SCHEMA_QUEUE = "COLUMN_NAME_QUEUE";
//    public static final String TABLE_SCHEMA_NODE_NAME = "TABLE_SCHEMA_NODE_NAME";
//    public static final String TABLE_SCHEMA_NODE_LONGITUDE = "TABLE_SCHEMA_NODE_LONGITUDE";
//    public static final String TABLE_SCHEMA_NODE_LATITUDE = "TABLE_SCHEMA_NODE_LATITUDE";
//    public static final String VALUE_TYPE_DOUBLE = "DOUBLE NOT NULL";
//    public static final String VALUE_TYPE_INT = "INT NOT NULL";
//    public static final String VALUE_TYPE_STRING = "VARCHAR(30) NOT NULL";
//    public static final String VALUE_TYPE_TEXT = "TEXT NOT NULL";
    private static final String DATABASE_NAME = "userTableList.db";
    private static final int DATABASE_VERSION = 2;
    int _id;
    private SQLiteDatabase db;
    private String SQL_CREATE_ENTRIES = C_Dictionary.CREATE_TABLE_if_not_exists + C_Dictionary.TRAVEL_Table_Name
            +" ("+C_Dictionary.TABLE_SCHEMA_id +C_Dictionary.VALUE_TYPE_INT + C_Dictionary.INTEGER_PRIMARY_KEY_AUTOINCREMENT + C_Dictionary.VALUE_COMMA_SEP
            + C_Dictionary.TABLE_SCHEMA_DATE+C_Dictionary.VALUE_TYPE_STRING + C_Dictionary.VALUE_COMMA_SEP
            + C_Dictionary.TABLE_SCHEMA_QUEUE+C_Dictionary.VALUE_TYPE_INT + C_Dictionary.VALUE_COMMA_SEP
            + C_Dictionary.TABLE_SCHEMA_NODE_NAME+C_Dictionary.VALUE_TYPE_STRING + C_Dictionary.VALUE_COMMA_SEP
            + C_Dictionary.TABLE_SCHEMA_NODE_LATITUDE+C_Dictionary.VALUE_TYPE_DOUBLE + C_Dictionary.VALUE_COMMA_SEP
            + C_Dictionary.TABLE_SCHEMA_NODE_LONGITUDE+C_Dictionary.VALUE_TYPE_DOUBLE
            +")";
// + C_Dictionary.INTEGER_PRIMARY_KEY_AUTOINCREMENT +" NOT NULL"+ C_Dictionary.VALUE_COMMA_SEP
    private String SQL_CREATE_TRAVEL_LIST = "CREATE TABLE if NOT exists " + C_Dictionary.TRAVEL_LIST_Table_Name
            +" ( "+ C_Dictionary.TRAVEL_LIST_SCHEMA_PLAN_NAME+C_Dictionary.VALUE_TYPE_STRING + C_Dictionary.VALUE_COMMA_SEP
            + C_Dictionary.TABLE_SCHEMA_DATE_START+C_Dictionary.VALUE_TYPE_STRING + C_Dictionary.VALUE_COMMA_SEP
            + C_Dictionary.TABLE_SCHEMA_DATE_END+C_Dictionary.VALUE_TYPE_STRING + C_Dictionary.VALUE_COMMA_SEP
            + C_Dictionary.TRAVEL_TABLE_VISIBILITY+C_Dictionary.VALUE_TYPE_INT +")";

    public C_Member_SQLite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public C_Member_SQLite (Context context){
        super(context, DATABASE_NAME,null, DATABASE_VERSION );

        Log.i("C_Member_SQLite ", "C_Member_SQLite 建構子:"+context);
//        db = this.getWritableDatabase();
    };

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TRAVEL_LIST);
        Log.i("C_Member_SQLite ", "C_Member_SQLite onCreate :"+ db);
//        Log.i("C_Member_SQLite ", "Version this db is :"+this.db.getVersion());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_CREATE_TRAVEL_LIST);
        onCreate(db);
    }
}
