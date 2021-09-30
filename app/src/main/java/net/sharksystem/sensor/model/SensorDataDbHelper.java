package net.sharksystem.sensor.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SensorDataDbHelper extends SQLiteOpenHelper {
  public static final int DATABASE_VERSION = 1;
  public static final String DATABASE_NAME = "sensordb.db";
  SQLiteDatabase db;



  public SensorDataDbHelper(Context context){
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
    this.db = getWritableDatabase();
    //this.onCreate(this.db);
  }
  @Override
  public void onCreate(SQLiteDatabase db) {
    Log.println(Log.DEBUG,"create","db created");
    System.out.println("Create db");
    db.execSQL(SensorDataContract.SDEntry.getCreateString());
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int i, int i1) {
    db.execSQL(SensorDataContract.SDEntry.getDeleteString());
    onCreate(db);
  }
}
