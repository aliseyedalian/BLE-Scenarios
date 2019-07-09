package com.example.blesenarios;

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
        insertObstacleTypeData();
    }
    private void createTables(SQLiteDatabase sqLiteDatabase){
        sqLiteDatabase.execSQL("create table ObstacleType"+
                "(obstacleId integer primary key autoincrement," +
                "obstacle varchar(50))"
        );
        sqLiteDatabase.execSQL("create table Phone"+
                "(phoneId integer primary key autoincrement," +
                "phoneName varchar(20) not null default ''," +
                "phoneManufacturer varchar(20) not null default ''," +
                "phoneBLEVersion varchar(20) not null default '') "
        );
        sqLiteDatabase.execSQL("create table Module"+
                "(moduleId integer primary key autoincrement," +
                "moduleName varchar(20) not null default ''," +
                "moduleBLEVersion varchar(20) not null default '')"
        );
        sqLiteDatabase.execSQL("create table Config"+
                "(configId intiger primary key autoincrement," +
                "ATDEFAULT varchar(20) default 'No'," +
                "cintMin integer," +
                "cintMax integer," +
                "rfpm integer," +
                "aint integer," +
                "ctout integer,"+
                "led integer,"+
                "baudRate integer," +
                "parity varchar(5))"
        );
        sqLiteDatabase.execSQL("create table Scenario"+
                "(scenId intiger primary key autoincrement," +
                "configId integer," +
                "moduleId integer," +
                "distance integer," +
                "place varchar(20)," +
                "obstacleNo integer," +
                "obstacleId integer," +
                "humidityPercent integer," +
                "wifi varchar(5)," +
                "ipv6 varchar(5)," +
                "timeStamp integer," +
                "ber real," +
                "explanation varchar(50)," +
                "FOREIGN KEY (configId) REFERENCES Config(configId)" +
                "ON UPDATE CASCADE ON DELETE CASCADE ,"+
                "FOREIGN KEY (moduleId) REFERENCES Module(moduleId)" +
                "ON UPDATE CASCADE ON DELETE CASCADE ,"+
                "FOREIGN KEY (obstacleId) REFERENCES ObstacleType(obstacleId)" +
                "ON UPDATE CASCADE ON DELETE CASCADE)"
        );
    }
    private void insertObstacleTypeData() {
        myDb.execSQL("insert into ObstacleType(obstacleId,obstacle) values(1,'LOS:Without Obstacles')");
        myDb.execSQL("insert into ObstacleType(obstacleId,obstacle) values(2,'Glass')");
        myDb.execSQL("insert into ObstacleType(obstacleId,obstacle) values(3,'Wood')");
        myDb.execSQL("insert into ObstacleType(obstacleId,obstacle) values(4,'Metal')");
        myDb.execSQL("insert into ObstacleType(obstacleId,obstacle) values(5,'Brick')");
        myDb.execSQL("insert into ObstacleType(obstacleId,obstacle) values(6,'Concrete')");
        myDb.execSQL("insert into ObstacleType(obstacleId,obstacle) values(7,'Body')");
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
                              String distance, String place, String obstacleNo, String obstacleId,
                              String humidityPercent, String wifi, String ipv6, String timeStamp, String BER, String explanation){
        myDb.execSQL(
                "insert into Phone(phoneName,phoneManufacturer,phoneBLEVersion) " +
                        "values("+phoneName+","+phoneManufacturer+","+phoneBLEVersion +")" +
                        "where not exists(select * from Phone where Phone.phoneName="+phoneName+
                        " and Phone.phoneManufacturer="+phoneManufacturer +
                        " and Phone.phoneBLEVersion="+phoneBLEVersion +
                        ");"
        );
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(COL_2,fname);
//        contentValues.put(COL_3,lname);
//        contentValues.put(COL_4,mark);
//        long result = myDb.insert("ObstacleType",null,contentValues);
        return true;
    }

    Cursor getPhoneTable(){
        return myDb.rawQuery("select * from Phone",null);
    }

}
