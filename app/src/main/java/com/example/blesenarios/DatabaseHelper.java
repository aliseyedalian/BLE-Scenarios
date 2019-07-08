package com.example.blesenarios;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "BLE_Scenarios.db";
    private SQLiteDatabase mydb = this.getWritableDatabase();

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    //test
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createTables(sqLiteDatabase);
        insertDataToTables(sqLiteDatabase);

    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("drop table if exists ObstacleType");
        sqLiteDatabase.execSQL("drop table if exists Phone");
        sqLiteDatabase.execSQL("drop table if exists Module");
        sqLiteDatabase.execSQL("drop table if exists Config");
        sqLiteDatabase.execSQL("drop table if exists ConfigModule");
        sqLiteDatabase.execSQL("drop table if exists Scenario");
        onCreate(sqLiteDatabase);
    }

//
//    boolean insertInto(String fname, String lname, int mark){
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(COL_2,fname);
//        contentValues.put(COL_3,lname);
//        contentValues.put(COL_4,mark);
//        long result = db.insert(TABLE_NAME,null,contentValues);
//        return result != -1;
//    }

//    Cursor getStudentTable(){
//        return db.rawQuery("select * from "+TABLE_NAME,null);
//    }

    private void createTables(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table ObstacleType"+
                "(obstacleId integer primary key autoincrement," +
                "obstacle varchar(50))"
        );
        sqLiteDatabase.execSQL("create table Phone"+
                "(phoneId integer primary key autoincrement," +
                "phoneName varchar(20)," +
                "manufacturer varchar(20)," +
                "bleVersion varchar(20))"
        );
        sqLiteDatabase.execSQL("create table Module"+
                "(moduleId integer primary key autoincrement," +
                "moduleName varchar(20)," +
                "bleVersion varchar(20))"
        );
        sqLiteDatabase.execSQL("create table Config"+
                "(configId intiger primary key autoincrement," +
                "cintMin integer," +
                "cintMax integer," +
                "rfpm integer," +
                "aint integer," +
                "ctout integer,"+
                "led integer,"+
                "baudRate integer," +
                "parity varchar(5))"
        );
        sqLiteDatabase.execSQL("create table ConfigModule"+
                "(configId intiger," +
                "moduleId integer," +
                "PRIMARY KEY (configId,moduleId)," +
                "FOREIGN KEY (configId) REFERENCES Config(configId)," +
                "FOREIGN KEY (moduleId) REFERENCES Module(moduleId))"
        );
        sqLiteDatabase.execSQL("create table Scenario"+
                "(scenId intiger primary key autoincrement," +
                "moduleId integer," +
                "distance integer," +
                "place varchar(20)," +
                "obstacleNo integer," +
                "obstacleId integer," +
                "humidityPercent integer," +
                "wifi varchar(5)," +
                "ipv6 varchar(5)," +
                "timeStamp integer," +
                "explanation varchar(50)," +
                "FOREIGN KEY (moduleId) REFERENCES Module(moduleId),"+
                "FOREIGN KEY (obstacleId) REFERENCES ObstacleType(obstacleId))"
        );
    }
    private void insertDataToTables(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("insert into ObstacleType values "
        );
    }

}
