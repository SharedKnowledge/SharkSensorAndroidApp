package net.sharksystem.sensor.model;


import android.provider.BaseColumns;

public final class SensorDataContract {
  // To prevent someone from accidentally instantiating the contract class,
  // make the constructor private.
  private SensorDataContract() {}

  /* Inner class that defines the table contents */
  public static class SDEntry implements BaseColumns {
    public static final String TABLE_NAME = "sensor_data";
    public static final String COLUMN_NAME_BN = "BASE_NAME";
    public static final String COLUMN_NAME_BT = "DT";
    public static final String COLUMN_NAME_TEMP = "TEMP";
    public static final String COLUMN_NAME_TEMP_UNIT   = "TEMP_UNIT";
    public static final String COLUMN_NAME_HUM = "HUM";
    public static final String COLUMN_NAME_HUM_UNIT = "HUM_UNIT";
    public static final String COLUMN_NAME_SOIL = "SOIL";
    public static final String COLUMN_NAME_SOIL_UNIT = "SOIL_UNIT";

    public static String getCreateString(){
      return "CREATE TABLE IF NOT EXISTS sensor_data(" +
        COLUMN_NAME_BN +" VARCHAR(255) NOT NULL," +
        COLUMN_NAME_BT +" DOUBLE NOT NULL," +
        COLUMN_NAME_TEMP +" DOUBLE," +
        COLUMN_NAME_TEMP_UNIT +" Char(1)," +
        COLUMN_NAME_HUM +" DOUBLE," +
        COLUMN_NAME_HUM_UNIT + " Char(1)," +
        COLUMN_NAME_SOIL + " DOUBLE," +
        COLUMN_NAME_SOIL_UNIT + " Char(1)," +
        "CONSTRAINT ID PRIMARY KEY ("+COLUMN_NAME_BN+","+ COLUMN_NAME_BT+")) ;";
    }
    public static String getDeleteString(){
      return "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
  }
}
