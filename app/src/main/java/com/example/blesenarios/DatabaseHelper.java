package com.example.blesenarios;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "BLE_Scenarios.db";
    private SQLiteDatabase myDb = this.getWritableDatabase();

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    //test
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createTables(sqLiteDatabase);
    }
    private void createTables(SQLiteDatabase sqLiteDatabase){
        sqLiteDatabase.execSQL("create table Phone(" +
                "phoneName TEXT PRIMARY KEY," +
                "phoneManufacturer TEXT," +
                "phoneBLEVersion TEXT); "
        );
        sqLiteDatabase.execSQL("create table Module(" +
                "moduleName TEXT PRIMARY KEY," +
                "moduleBLEVersion TEXT);"
        );
        sqLiteDatabase.execSQL("create table Config(" +
                "configId INTEGER PRIMARY KEY AUTOINCREMENT," +
                "ATDEFAULT TEXT default 'No'," +
                "cintMin INTEGER," +
                "cintMax INTEGER," +
                "rfpm INTEGER," +
                "aint INTEGER," +
                "ctout INTEGER,"+
                "led INTEGER,"+
                "baudRate INTEGER," +
                "parity TEXT);"
        );
        sqLiteDatabase.execSQL("create table Scenario(" +
                "scenId INTEGER PRIMARY KEY AUTOINCREMENT," +
                "configId integer," +
                "moduleName TEXT," +
                "phoneName TEXT," +
                "distance integer," +
                "place TEXT," +
                "obstacleNo integer," +
                "obstacle TEXT," +
                "humidityPercent integer," +
                "wifi TEXT," +
                "ipv6 TEXT," +
                "timeStamp integer," +
                "ber REAL," +
                "explanation TEXT," +
                "FOREIGN KEY (configId) REFERENCES Config(configId)" +
                "ON UPDATE CASCADE ON DELETE CASCADE ,"+
                "FOREIGN KEY (moduleName) REFERENCES Module(moduleName)" +
                "ON UPDATE CASCADE ON DELETE CASCADE ,"+
                "FOREIGN KEY (phoneName) REFERENCES Phone(phoneName)" +
                "ON UPDATE CASCADE ON DELETE CASCADE)"
        );
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

    boolean insertNewScenario(String phoneName, String phoneManufacturer, String phoneBLEVersion,
                              String moduleName, String moduleBLEVersion,
                              String ATDEFAULT, String cintMin, String cintMax, String rfpm,
                              String aint, String ctout, String led, String baudRate, String parity,
                              String distance, String place, String obstacleNo, String obstacle,
                              String humidityPercent, String wifi, String ipv6, String timeStamp, String ber, String explanation){
//        myDb.execSQL(
//                "INSERT INTO Phone(phoneName,phoneManufacturer,phoneBLEVersion) " +
//                        "VALUES("+phoneName+","+phoneManufacturer+","+phoneBLEVersion +");" +
//                        "INSERT INTO Module(moduleName,moduleBLEVersion) "+
//                        "VALUES("+moduleName+","+moduleBLEVersion +");"
//        );
        ContentValues contentValues = new ContentValues();
        contentValues.put("phoneName",phoneName);
        contentValues.put("phoneManufacturer",phoneManufacturer);
        contentValues.put("phoneBLEVersion",phoneBLEVersion);
        long resultPhone = myDb.insert("Phone",null,contentValues);
        return resultPhone != -1;
    }

    Cursor getPhoneTable(){
        return myDb.rawQuery("select * from Phone",null);
    }

}
