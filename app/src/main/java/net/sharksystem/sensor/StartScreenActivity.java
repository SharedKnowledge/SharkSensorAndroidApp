package net.sharksystem.sensor;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import net.sharksystem.SharkException;
import net.sharksystem.SharkPeer;
import net.sharksystem.asap.android.apps.ASAPActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class StartScreenActivity extends ASAPActivity implements NewSensorDataReceivedListener, View.OnClickListener {

  Spinner spinner;
  SharkSensorComponentImpl sensorComponent;
  SharkPeer sharkPeer;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main2);
    this.spinner = findViewById(R.id.BNs_spinner);
    Logger logger =  Logger.getLogger("creating repo");
    sharkPeer = SharkPeerSingleton.getSharkPeer();
    Log.d("ASAP Custom","Is Shark Peer null?: "+String.valueOf(sharkPeer==null));
    try {
      sensorComponent = (SharkSensorComponentImpl)sharkPeer.getComponent(SharkSensorComponent.class);
      sensorComponent.addNewSensorDataReceivedListener(this);
    } catch (SharkException e) {
      e.printStackTrace();
    }
    this.populateSpinner();

  }

  private void populateSpinner() {
    List<String> spinnerArray = sensorComponent.getAllBaseNames();
    spinnerArray.add(0,"Select Base name");
    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
      this, android.R.layout.simple_spinner_item, spinnerArray
    );
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    Spinner sItems = (Spinner) findViewById(R.id.BNs_spinner);
    sItems.setAdapter(adapter);
    sItems.setSelection(0);
  }

  @Override
  public void onClick(View view) {
    String text = spinner.getSelectedItem().toString();
    System.out.println("text is"+text);
    ArrayList<SensorData> list = (ArrayList<SensorData>)sensorComponent.getSensorDataForId(text);
    if(!list.isEmpty()){
      switch (view.getId()) {
        case R.id.graph_button:
          Intent intentGraph = new Intent(this, GraphViewActivity.class);
          intentGraph.putExtra("list", list);
          startActivity(intentGraph);
          break;
        case R.id.table_button:
          Intent intentTable = new Intent(this, TableViewActivity.class);
          intentTable.putExtra("list", list);
          startActivity(intentTable);
          break;
      }
    }
    else{
      Toast.makeText(this, "No entry found", Toast.LENGTH_SHORT).show();

    }
  }
  @Override
  public void dataReceived() {
    populateSpinner();
  }
}
