package net.sharksystem.sensor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import net.sharksystem.SharkException;
import net.sharksystem.sensor.model.DataType;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class GraphViewActivity extends AppCompatActivity implements View.OnClickListener, NewSensorDataReceivedListener{

  private GraphView graph;
  LineGraphSeries<DataPoint> series;
  private Button humidity, temperature, soil;
  private TextView text;
  private int daysBack;
  private List<SensorData> sensorDataList;
  private DataType currentType;
  private String sensorId;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_graph_view);
    try {
      SharkSensorComponent sensorComponent = (SharkSensorComponentImpl)
        SharkPeerSingleton.getSharkPeer().getComponent(SharkSensorComponent.class);
      sensorComponent.addNewSensorDataReceivedListener(this);

    } catch (SharkException e) {
      e.printStackTrace();
    }

    Intent intent = getIntent();
    Log.d("sharkUI","Start graph activity");

    this.sensorDataList = (ArrayList<SensorData>)intent.getSerializableExtra("list");
    if(sensorDataList.size()>0){
      this.sensorId = sensorDataList.get(0).getBn();
    }
    daysBack = 0;
    graph = (GraphView) findViewById(R.id.graph);
    graph.setVisibility(View.VISIBLE);
    text = findViewById(R.id.dateText);
    humidity = findViewById(R.id.btn_humidity);
    temperature = findViewById(R.id.btn_temperature);
    soil = findViewById(R.id.btn_soil);
    this.resetBtnColor();
    humidity.setBackgroundColor(Color.parseColor("#006600"));
    this.currentType = DataType.HUMIDITY;
    loadGraph(this.currentType);
  }

  @Override
  public void onClick(View view) {
    JSONObject json = null;
    int viewId = view.getId();
    switch (viewId) {
      case R.id.btn_humidity:
        this.resetBtnColor();
        humidity.setBackgroundColor(Color.parseColor("#006600"));
        this.currentType = DataType.HUMIDITY;
        break;
      case R.id.btn_soil:
        this.resetBtnColor();
        soil.setBackgroundColor(Color.parseColor("#006600"));
        this.currentType = DataType.SOIL;
        break;
      case R.id.btn_temperature:
        this.resetBtnColor();
        temperature.setBackgroundColor(Color.parseColor("#006600"));
        this.currentType = DataType.TEMPERATURE;
        break;
      case R.id.btn_back:
        System.out.println("back");
        daysBack--;
        break;
      case R.id.btn_next:
        System.out.println("next");
        daysBack++;
        break;
    }
    loadGraph(this.currentType);
  }

  private void resetBtnColor() {
    humidity.setBackgroundColor(Color.parseColor("#7CB342"));
    soil.setBackgroundColor(Color.parseColor("#7CB342"));
    temperature.setBackgroundColor(Color.parseColor("#7CB342"));
  }
  //Daten laden, die im Graphen dargestellt werden sollen
  private void loadGraph(DataType type) {
    Log.d("sharkUI","load Graph");
    graph.removeAllSeries();
    try {
      this.text.setText(getDateString(daysBack));
      List<SensorData> todayList = getAllEntriesOfSameDay(getDateString(daysBack));
      showGraph(todayList, type);
    } catch (IllegalArgumentException e) {
      Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
    }
  }
  //Erzeugung eines Datums des aktuellen Tages, von welchem die als Parameter übergebenen Tage abgezogen,
  //bzw hinzugefügt werden. Das Datum wird zu einem String konvertiert
   private String getDateString(int day){
    Calendar cal = new GregorianCalendar();
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.add(Calendar.DAY_OF_MONTH, day);
    Date date = new Date(cal.getTime().getTime());
    SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY);
    return df.format(date);
  }
  //Darstellung des Graphen
  private void showGraph(List<SensorData> todayList, DataType type){
    if (!todayList.isEmpty()) {
      DataPoint[] dataPoint = new DataPoint[todayList.size()];
      for (int i = 0; i < todayList.size(); i++) {
        SensorData data = todayList.get(i);
        Date date = new Date((long)data.getDt()*1000);
        float time = date.getHours() + ((date.getMinutes()) / 60.0f);
        dataPoint[i] = new DataPoint(time,this.getDataWithType(type,data));
      }
      this.series = new LineGraphSeries<>(dataPoint);
      graph.addSeries(series);
      graph.getGridLabelRenderer().setNumHorizontalLabels(12);
      graph.getViewport().setMinX(0);
      graph.getViewport().setMaxX(24);
      graph.getViewport().setXAxisBoundsManual(true);
    }
  }
   List<SensorData> getAllEntriesOfSameDay(String dateString){
    List<SensorData> sameDayList = new ArrayList<>();
    SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY);
    for(SensorData data: this.sensorDataList){
      Date date = new Date((long)data.getDt()*1000);
      String ds = (df.format(date));

      if(dateString.equals(ds)){
        sameDayList.add(data);
      }
    }
    Collections.sort(sameDayList, new Comparator<SensorData>() {
      @Override
      public int compare(SensorData lhs, SensorData rhs) {
        return lhs.getDt()>rhs.getDt() ? 1 : (lhs.getDt() < rhs.getDt()) ? -1 : 0;
      }
    });
    return sameDayList;
  }
   double getDataWithType(DataType type, SensorData data){
    switch(type){
      case SOIL:
        return data.getSoil();
      case HUMIDITY:
        return data.getHum();
      case TEMPERATURE:
        return data.getTemp();
    }
    return -1.0f;
  }

  @Override
  public void dataReceived() {
    try {
      SharkSensorComponent component = (SharkSensorComponent)SharkPeerSingleton.getSharkPeer().getComponent(SharkSensorComponentImpl.class);
      component.getSensorDataForId(this.sensorId);
      loadGraph(this.currentType);
    } catch (SharkException e) {
      e.printStackTrace();
    }
  }
  @Override
  public void onDestroy() {
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
