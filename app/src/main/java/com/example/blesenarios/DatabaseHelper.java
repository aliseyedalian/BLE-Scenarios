package com.example.blesenarios;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "BLE_Scenarios.db";

    //ObstacleType(obstacleID,obstacle)
    //Phone(


    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    //test
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table ObstacleType"+
                "(obstacleID integer primary key," +
                "obstacle varchar(50))"
        );
        sqLiteDatabase.execSQL("create table Phone"+
                "(Phonename varchar(50) primary key," +
                "Manufacturer varchar(50)," +
                "bleVersion varchar(50))"
        );

//        sqLiteDatabase.execSQL("create table "+TABLE_NAME+
//                "(Scenario_id integer primary key autoincrement," +
//                "module_name varchar(30)," +
//                "module_bleVersion varchar(30)," +
//                "config_cintMin varchar(30)," +
//                "config_cintMax varchar(30)," +
//                "config_rfpm varchar(30)," +
//                "config_aint varchar(30)," +
//                "config_ctout varchar(30)," +
//                "config_baud varchar(30)," +
//                "config_parity varchar(30)," +
//                "phone_model varchar(30)," +
//                "phone_bleVersion varchar(30)," +
//                "phone_manufacturer varchar(30)," +
//                "distance varchar(30)," +
//                "place varchar(30)," +
//                "obstacle_name varchar(30)," +
//                "obstacle_count varchar(30)," +
//                "humidity varchar(30)," +
//                "wifi varchar(30)," +
//                "ipv6 varchar(30)," +
//                "timestamp varchar(30)," +
//                "explanation varchar(30))"
//        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        //sqLiteDatabase.execSQL("drop table if exists "+TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    private SQLiteDatabase db = this.getWritableDatabase();
//    boolean insertNewStudent(String fname, String lname, int mark){
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(COL_2,fname);
//        contentValues.put(COL_3,lname);
//        contentValues.put(COL_4,mark);
//        long result = db.insert(TABLE_NAME,null,contentValues);
//        return result != -1;
//    }
//
//    Cursor getStudentTable(){
//        return db.rawQuery("select * from "+TABLE_NAME,null);
//    }
}
