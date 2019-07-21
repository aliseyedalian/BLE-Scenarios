package com.example.blesenarios;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
                "moduleBLEVersion TEXT not null);"
        );
        sqLiteDatabase.execSQL("create table Config(" +
                "configId INTEGER PRIMARY KEY AUTOINCREMENT," +
                "ATDEFAULT TEXT default 'No'," +
                "cintMin TEXT," +
                "cintMax TEXT," +
                "rfpm TEXT," +
                "aint TEXT," +
                "ctout TEXT,"+
                "led TEXT,"+
                "baudRate TEXT);"
        );
        sqLiteDatabase.execSQL("create table Scenario(" +
                "scenId INTEGER PRIMARY KEY AUTOINCREMENT," +
                "configId integer," +
                "phoneName TEXT," +
                "moduleName TEXT," +
                "distance TEXT," +
                "place TEXT," +
                "obstacleNo TEXT," +
                "obstacle TEXT," +
                "humidityPercent TEXT," +
                "wifi TEXT," +
                "ipv6 TEXT," +
                "timeStamp TEXT," +
                "packetLossPercent TEXT," +
                "explanation TEXT default 'None'," +
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
        sqLiteDatabase.execSQL("drop table if exists Phone");
        sqLiteDatabase.execSQL("drop table if exists Module");
        sqLiteDatabase.execSQL("drop table if exists Config");
        sqLiteDatabase.execSQL("drop table if exists Scenario");
        onCreate(sqLiteDatabase);
    }




    boolean insertNewPhone(String phoneName, String phoneManufacturer, String phoneBLEVersion){
        if(isExistPhone(phoneName,phoneManufacturer,phoneBLEVersion)){
            Log.d("salis","insertNewPhone : ExistPhone");
            return false;
        }
        ContentValues contentValuesPhone = new ContentValues();
        contentValuesPhone.put("phoneName",phoneName);
        contentValuesPhone.put("phoneManufacturer",phoneManufacturer);
        contentValuesPhone.put("phoneBLEVersion",phoneBLEVersion);
        long resultPhone = myDb.insert("Phone",null,contentValuesPhone);
        return resultPhone!=-1;
    }
    boolean insertNewModule(String moduleName, String moduleBLEVersion){
        if(isExistModule(moduleName,moduleBLEVersion)){
            return false;
        }
        ContentValues contentValuesModule = new ContentValues();
        contentValuesModule.put("moduleName",moduleName);
        contentValuesModule.put("moduleBLEVersion",moduleBLEVersion);
        long resultModule = myDb.insert("Module",null,contentValuesModule);
        return resultModule!=-1;
    }
    boolean insertNewConfig(String ATDEFAULT,String cintMin,String cintMax,String rfpm,String aint,
                            String ctout ,String led ,String baudRate ){
        if(isExistConfig(ATDEFAULT,cintMin,cintMax,rfpm,aint,ctout,led,baudRate)){
            return false;
        }
        ContentValues contentValuesModule = new ContentValues();
        contentValuesModule.put("ATDEFAULT",ATDEFAULT);
        contentValuesModule.put("cintMin",cintMin);
        contentValuesModule.put("cintMax",cintMax);
        contentValuesModule.put("rfpm",rfpm);
        contentValuesModule.put("aint",aint);
        contentValuesModule.put("ctout",ctout);
        contentValuesModule.put("led",led);
        contentValuesModule.put("baudRate",baudRate);
        long resultConfig = myDb.insert("Config",null,contentValuesModule);
        return resultConfig!=-1;
    }
    boolean insertNewScenario(Integer configId, String phoneName, String moduleName, String distance, String place, String obstacleNo,
                              String obstacle, String humidityPercent, String wifi, String ipv6, String timeStamp, String packetLossPercent, String explanation){
        if(isExistScenario(configId,phoneName,moduleName,distance,place,obstacleNo,obstacle,humidityPercent,wifi,ipv6,timeStamp,packetLossPercent,explanation)){
            return false;
        }
        ContentValues contentValuesModule = new ContentValues();
        contentValuesModule.put("configId",configId);
        contentValuesModule.put("phoneName",phoneName);
        contentValuesModule.put("moduleName",moduleName);
        contentValuesModule.put("distance",distance);
        contentValuesModule.put("place",place);
        contentValuesModule.put("obstacleNo",obstacleNo);
        contentValuesModule.put("obstacle",obstacle);
        contentValuesModule.put("humidityPercent",humidityPercent);
        contentValuesModule.put("wifi",wifi);
        contentValuesModule.put("ipv6",ipv6);
        contentValuesModule.put("timeStamp",timeStamp);
        contentValuesModule.put("packetLossPercent",packetLossPercent);
        contentValuesModule.put("explanation",explanation);
        long resultScenario = myDb.insert("Scenario",null,contentValuesModule);
        return resultScenario!=-1;
    }



    Cursor getPhoneTable(){
        return myDb.rawQuery("select * from Phone",null);
    }
    Cursor getModuleTable(){
        return myDb.rawQuery("select * from Module",null);
    }
    Cursor getConfigTable(){
        return myDb.rawQuery("select * from Config",null);
    }
    Cursor getScenarioTable(){
        return myDb.rawQuery("select * from Scenario",null);
    }



    private boolean isExistPhone(String phoneName, String phoneManufacturer, String phoneBLEVersion) {
        String query = "select * from Phone where " +
                "phoneName ='"+phoneName+"' and phoneManufacturer='"+phoneManufacturer+"' and phoneBLEVersion='"+phoneBLEVersion+"';";
        @SuppressLint("Recycle") Cursor resultCursor = myDb.rawQuery(query,null);
        return resultCursor.getCount()!=0;
    }
    private boolean isExistModule(String moduleName, String moduleBLEVersion) {
        String query = "select * from Module where " +
                "moduleName ='"+moduleName+"' and moduleBLEVersion='"+moduleBLEVersion+"';";
        @SuppressLint("Recycle") Cursor resultCursor = myDb.rawQuery(query,null);
        return resultCursor.getCount()!=0;
    }
    private boolean isExistConfig(String ATDEFAULT, String cintMin, String cintMax, String rfpm,
                                  String aint, String ctout, String led, String baudRate){
        String query = "select ConfigId from Config where ATDEFAULT ='"+ATDEFAULT+"' and cintMin='"+cintMin+"' and "+
                "cintMax='"+cintMax+"' and rfpm='"+rfpm+"' and aint='"+aint+"' and ctout='"+ctout+"' and " +
                "led='"+led+"' and baudRate='"+baudRate+"';";
        @SuppressLint("Recycle") Cursor resultCursor = myDb.rawQuery(query,null);
        return resultCursor.getCount()!=0;
    }
    private boolean isExistScenario(Integer configId, String phoneName, String moduleName, String distance, String place,
                                    String obstacleNo, String obstacle, String humidityPercent, String wifi,
                                    String ipv6, String timeStamp, String packetLossPercent, String explanation) {
        String query = "select * from Scenario where configId ="+configId+" and phoneName='"+phoneName+"' and "+
                "moduleName='"+moduleName+"' and distance='"+distance+"' and place='"+place+"' and obstacleNo='"+obstacleNo+"' and " +
                "obstacle='"+obstacle+"' and humidityPercent='"+humidityPercent+"' and wifi='"+wifi+"' and ipv6='"+ipv6+"' and " +
                "timeStamp='"+timeStamp+"' and packetLossPercent='"+packetLossPercent+"' and explanation='"+explanation+"';";
        @SuppressLint("Recycle") Cursor resultCursor = myDb.rawQuery(query,null);
        return resultCursor.getCount()!=0;
    }





    Cursor getConfigId(String ATDEFAULT,String cintMin, String cintMax, String rfpm,
                       String aint, String ctout, String led, String baudRate) {
        String query = "select distinct ConfigId from Config where ATDEFAULT ='"+ATDEFAULT+"' and cintMin='"+cintMin+"' and "+
                "cintMax='"+cintMax+"' and rfpm='"+rfpm+"' and aint='"+aint+"' and ctout='"+ctout+"' and " +
                "led='"+led+"' and baudRate='"+baudRate+"';";
        return myDb.rawQuery(query,null);
    }

    private void rebuild() {
        myDb.execSQL("drop table if exists Phone");
        myDb.execSQL("drop table if exists Module");
        myDb.execSQL("drop table if exists Config");
        myDb.execSQL("drop table if exists Scenario");
        onCreate(myDb);
    }
}
