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

public class StartScreenActivity extends ASAPActivity implements NewSensorDataReceivedListener {

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
    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
      this, android.R.layout.simple_spinner_item, spinnerArray
    );
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    Spinner sItems = (Spinner) findViewById(R.id.BNs_spinner);
    sItems.setAdapter(adapter);
  }

  public void getAllForId(View v){
    String text = spinner.getSelectedItem().toString();
    System.out.println("text is"+text);
    ArrayList<SensorData> list = (ArrayList<SensorData>)sensorComponent.getSensorDataForId(text);

    if(list!=null&&!list.isEmpty()) {
      //Intent intent = new Intent(this, TableViewActivity.class);
      Intent intent = new Intent(this, GraphViewActivity.class);
      intent.putExtra("list", list);
      startActivity(intent);
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
