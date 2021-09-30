package net.sharksystem.sensor;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import net.sharksystem.SharkException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TableViewActivity extends AppCompatActivity implements NewSensorDataReceivedListener {

  ArrayList<SensorData> sensorDataList;
  String sensorId;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_table_view);
    System.out.println("on create");
    try {
      SharkSensorComponent sensorComponent = (SharkSensorComponentImpl)
        SharkPeerSingleton.getSharkPeer().getComponent(SharkSensorComponent.class);
      sensorComponent.addNewSensorDataReceivedListener(this);
    } catch (SharkException e) {
      e.printStackTrace();
    }
    Intent intent = getIntent();
    this.sensorDataList  = (ArrayList<SensorData>)intent.getSerializableExtra("list");
    if(sensorDataList.size()>0){
      this.sensorId = sensorDataList.get(0).getBn();
    }
    createTable(sensorDataList);
  }
  void createTable(List<SensorData> list){
    TableLayout table = findViewById(R.id.listView);
    TableRow.LayoutParams rowParams = new TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
    String[] headerNames = {"ID","Time","Temp","Hum","Soil"};
    table.addView(this.makeRow(rowParams,headerNames,"#5e5e5e", "#fcfcfc"));

    for(SensorData data: list){
      Date date = new Date((long)data.getDt()*1000);
      String dateString = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(date);
      String[] dataParams = {data.getBn(),dateString,
              String.valueOf(data.getTemp())+Unit.getStringValue(data.getTempUnit()),
              String.valueOf(data.getHum())+Unit.getStringValue(data.getHumUnit()),
              String.valueOf(data.getSoil())+Unit.getStringValue(data.getSoilUnit())};
      TableRow row = this.makeRow(rowParams, dataParams,"#ffffff","#000000");
      table.addView(row);
    }
  }

  TableRow makeRow(TableRow.LayoutParams rowParams, String[] values, String backColor, String textColor){
    TableRow row = new TableRow(this);
    row.setLayoutParams(rowParams);
    row.setBackgroundColor(Color.parseColor(backColor));

    for (String val : values) {
      TextView tv = new TextView(this);
      tv.setPadding(10,5,10,5);
      tv.setTextColor(Color.parseColor(textColor));
      tv.setText(val);
      row.addView(tv);
    }
    return row;
  }


  @Override
  public void dataReceived() {
    try {
      SharkSensorComponent sensorComponent = (SharkSensorComponentImpl)
        SharkPeerSingleton.getSharkPeer().getComponent(SharkSensorComponent.class);
      this.sensorDataList = (ArrayList<SensorData>) sensorComponent.getSensorDataForId(this.sensorId);
    } catch (SharkException e) {
      e.printStackTrace();
    }
    createTable(sensorDataList);
  }

  @Override
  public void onDestroy(){
    try {
      SharkSensorComponent sensorComponent = (SharkSensorComponentImpl)
        SharkPeerSingleton.getSharkPeer().getComponent(SharkSensorComponent.class);
      sensorComponent.removeSensorDataReceivedListener(this);
    } catch (SharkException e) {
      e.printStackTrace();
    }
    super.onDestroy();
  }
}
