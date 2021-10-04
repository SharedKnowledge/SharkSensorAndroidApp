package net.sharksystem.sensor.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import net.sharksystem.sensor.SensorData;
import net.sharksystem.sensor.SensorRepository;
import net.sharksystem.sensor.Unit;

import java.util.ArrayList;
import java.util.List;

public class AndroidSensorRepository implements SensorRepository {

  private SQLiteOpenHelper dbHelper;
  public AndroidSensorRepository(SQLiteOpenHelper helper){
    this.dbHelper = helper;
    System.out.println("create helper");
    //dbHelper.onCreate(dbHelper.getWritableDatabase());
  }

  @Override
  public List<SensorData> selectAll() {
    return this.queryWithCondition(null, null);
  }

  @Override
  public List<SensorData> selectAllForId(String s) {
    String condition = SensorDataContract.SDEntry.COLUMN_NAME_BN + " = ?";
    return this.queryWithCondition(condition, new String[]{s});
  }

  @Override
  public SensorData selectSpecificEntry(String s, double v) {
    String condition = SensorDataContract.SDEntry.COLUMN_NAME_BN +" = ? AND "+
      SensorDataContract.SDEntry.COLUMN_NAME_BT + " = ?";
    String[] selectionArgs = {s, String.valueOf(v)};
    return this.queryWithCondition(condition,selectionArgs).get(0);
  }

  @Override
  public void insertNewEntries(List<SensorData> list) {
    SQLiteDatabase db = dbHelper.getWritableDatabase();
    for(SensorData data: list){
      ContentValues values = new ContentValues();
      values.put(SensorDataContract.SDEntry.COLUMN_NAME_BN, data.getBn());
      values.put(SensorDataContract.SDEntry.COLUMN_NAME_BT, data.getDt());
      values.put(SensorDataContract.SDEntry.COLUMN_NAME_TEMP, data.getTemp());
      values.put(SensorDataContract.SDEntry.COLUMN_NAME_TEMP_UNIT, data.getTempUnit().toString());
      values.put(SensorDataContract.SDEntry.COLUMN_NAME_HUM, data.getHum());
      values.put(SensorDataContract.SDEntry.COLUMN_NAME_HUM_UNIT, data.getHumUnit().toString());
      values.put(SensorDataContract.SDEntry.COLUMN_NAME_SOIL, data.getSoil());
      values.put(SensorDataContract.SDEntry.COLUMN_NAME_SOIL_UNIT, data.getSoilUnit().toString());

      long l = db.insert(SensorDataContract.SDEntry.TABLE_NAME, null, values);
      Log.println(Log.DEBUG,"insert","Id of inserted entry: " + l);
    }

  }

  private List<SensorData> queryWithCondition(String condition, String[] selectionArgs){
    SQLiteDatabase db = dbHelper.getReadableDatabase();
    System.out.println("Into query");
    String[] projection = {
      SensorDataContract.SDEntry.COLUMN_NAME_BN,
      SensorDataContract.SDEntry.COLUMN_NAME_BT,
      SensorDataContract.SDEntry.COLUMN_NAME_TEMP,
      SensorDataContract.SDEntry.COLUMN_NAME_TEMP_UNIT,
      SensorDataContract.SDEntry.COLUMN_NAME_HUM,
      SensorDataContract.SDEntry.COLUMN_NAME_HUM_UNIT,
      SensorDataContract.SDEntry.COLUMN_NAME_SOIL,
      SensorDataContract.SDEntry.COLUMN_NAME_SOIL_UNIT
    };


    // How you want the results sorted in the resulting Cursor
    String sortOrder =
      SensorDataContract.SDEntry.COLUMN_NAME_BT + " DESC";

    Cursor cursor = db.query(
      SensorDataContract.SDEntry.TABLE_NAME,   // The table to query
      projection,             // The array of columns to return (pass null to get all)
      condition,              // The columns for the WHERE clause
      selectionArgs,          // The values for the WHERE clause
      null,                   // don't group the rows
      null,                   // don't filter by row groups
      sortOrder               // The sort order
    );

    List<SensorData> sensorDataList = new ArrayList<>();
    while(cursor.moveToNext()) {
      int id = cursor.getColumnIndex(SensorDataContract.SDEntry.COLUMN_NAME_BN);
      String bn = cursor.getString(id);
      System.out.println("bn: "+bn);
      id = cursor.getColumnIndex(SensorDataContract.SDEntry.COLUMN_NAME_BT);
      double bt = cursor.getDouble(id);
      id = cursor.getColumnIndex(SensorDataContract.SDEntry.COLUMN_NAME_TEMP);
      double temp = cursor.getDouble(id);
      id = cursor.getColumnIndex(SensorDataContract.SDEntry.COLUMN_NAME_TEMP_UNIT);
      String temp_unit = cursor.getString(id);
      id = cursor.getColumnIndex(SensorDataContract.SDEntry.COLUMN_NAME_HUM);
      double hum = cursor.getDouble(id);
      id = cursor.getColumnIndex(SensorDataContract.SDEntry.COLUMN_NAME_HUM_UNIT);
      String hum_unit = cursor.getString(id);
      id = cursor.getColumnIndex(SensorDataContract.SDEntry.COLUMN_NAME_SOIL);
      double soil = cursor.getDouble(id);
      id = cursor.getColumnIndex(SensorDataContract.SDEntry.COLUMN_NAME_SOIL_UNIT);

      String soil_unit = cursor.getString(id);
      SensorData data = new SensorData(bn, temp, Unit.valueOf(temp_unit), hum,
        Unit.valueOf(hum_unit), soil, Unit.valueOf(soil_unit), bt);
      sensorDataList.add(data);
    }
    cursor.close();

    return sensorDataList;
  }
  @Override
  public List<SensorData> selectForIdNewerThan(double v, String s) {
    String condition = SensorDataContract.SDEntry.COLUMN_NAME_BN +" = ? AND "+
      SensorDataContract.SDEntry.COLUMN_NAME_BT + " > ?";
    String[] selectionArgs = {s, String.valueOf(v)};
    return this.queryWithCondition(condition, selectionArgs);
  }

  @Override
  public void updateEntries(List<SensorData> list) {

  }

  @Override
  public List<SensorData> selectEntriesWhereSentIsFalse(String s) {
    return null;
  }

  @Override
  public List<String> selectAllBNs() {
    SQLiteDatabase db = dbHelper.getReadableDatabase();
    Cursor cursor = db.query(true,
      SensorDataContract.SDEntry.TABLE_NAME,   // The table to query
      new String[]{"base_name"},             // The array of columns to return (pass null to get all)
      null,              // The columns for the WHERE clause
      null,          // The values for the WHERE clause
      SensorDataContract.SDEntry.COLUMN_NAME_BN,
      null,                   // don't group the rows
      null,                   // don't filter by row groups
      null               // The sort order
    );
    List<String> bnList = new ArrayList<>();
    while(cursor.moveToNext()) {
      int id = cursor.getColumnIndex(SensorDataContract.SDEntry.COLUMN_NAME_BN);
      bnList.add(cursor.getString(id));
    }
    cursor.close();
    return bnList;
  }

  public void dropTable() {
    Log.d("Test","dropTable");

    SQLiteDatabase db = dbHelper.getWritableDatabase();
    db.execSQL("DROP TABLE IF EXISTS "+SensorDataContract.SDEntry.TABLE_NAME+";");
  }
  public void createTable() {
    Log.d("Test","createTable");
    SQLiteDatabase db = dbHelper.getWritableDatabase();
    db.execSQL(SensorDataContract.SDEntry.getCreateString());
  }
}
