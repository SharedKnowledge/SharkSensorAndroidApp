package net.sharksystem.sensor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.sharksystem.SharkException;
import net.sharksystem.SharkPeer;
import net.sharksystem.SharkPeerFS;
import net.sharksystem.asap.ASAP;
import net.sharksystem.asap.ASAPException;
import net.sharksystem.asap.android.Util;
import net.sharksystem.asap.android.apps.ASAPAndroidPeer;
import net.sharksystem.sensor.model.AndroidSensorRepository;
import net.sharksystem.sensor.model.SensorDataDbHelper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class ASAPInitialActivity extends Activity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);


    try {
      // initialize ASAP peer (application side)
      if (!ASAPAndroidPeer.peerInitialized()) {
        CharSequence savedPeerId = this.getASAPPeerId(this);

        if (savedPeerId == null) {
          savedPeerId = ASAP.createUniqueID();
        }
        Collection<CharSequence> formats = new ArrayList<>();

        // add a name that identifies your app which also identifies general message format
        formats.add(getString(R.string.app_format));
        ASAPAndroidPeer.initializePeer(savedPeerId, formats, this);
      }
      CharSequence peerId = ASAPAndroidPeer.getASAPAndroidPeer().getPeerID();
      SensorRepository repo = new AndroidSensorRepository(
        new SensorDataDbHelper(this));
      File rootDir = Util.getASAPRootDirectory(
        this, getString(R.string.APP_FOLDER_NAME), peerId);
      SharkPeer sharkPeer = new SharkPeerFS(peerId, rootDir.toString());
      SharkSensorComponentFactory factory = new SharkSensorComponentFactory(repo,
        new SharkSensorSerializerImpl(new ObjectMapper()), null, peerId.toString());

      sharkPeer.addComponent(factory, SharkSensorComponent.class);

      //sensorComponent = (SharkSensorComponent) sharkPeer.getComponent(SharkSensorComponent.class);

      ASAPAndroidPeer applicationSideASAPPeer = null;

      if (!ASAPAndroidPeer.peerStarted()) {
        System.out.println("Start my ASAP Peer");
        applicationSideASAPPeer = ASAPAndroidPeer.startPeer(this);
      }
      System.out.println("Start my ASAP Shark Peer");

      sharkPeer.start(applicationSideASAPPeer);
      SharkPeerSingleton.setSharkPeer(sharkPeer);



      ArrayList<SensorData> list = new ArrayList<>();
      list.add(new SensorData("123",17.4,Unit.C,67.4,Unit.P,52.1,Unit.P,(System.currentTimeMillis()/1000.0)-20000));
      list.add(new SensorData("test",33.3,Unit.C,71.1,Unit.P,42.2,Unit.P,(System.currentTimeMillis()/1000.0)-13400));
      list.add(new SensorData("345",-42.6,Unit.C,55.4,Unit.P,43.1,Unit.P,(System.currentTimeMillis()/1000.0)));
      list.add(new SensorData("test",12.4,Unit.C,63.9,Unit.P,43.8,Unit.P,(System.currentTimeMillis()/1000.0)));
      list.add(new SensorData("123",22.1,Unit.C,64.7,Unit.P,44.6,Unit.P,(System.currentTimeMillis()/1000.0)-19000));
      list.add(new SensorData("123",22.1,Unit.C,44.7,Unit.P,44.6,Unit.P,(System.currentTimeMillis()/1000.0)-18000));
      list.add(new SensorData("123",22.1,Unit.C,54.7,Unit.P,44.6,Unit.P,(System.currentTimeMillis()/1000.0)-15000));
      list.add(new SensorData("123",22.1,Unit.C,64.7,Unit.P,44.6,Unit.P,(System.currentTimeMillis()/1000.0)-10000));
      list.add(new SensorData("123",22.1,Unit.C,64.7,Unit.P,44.6,Unit.P,(System.currentTimeMillis()/1000.0)-7000));
      list.add(new SensorData("123",22.1,Unit.C,74.7,Unit.P,44.6,Unit.P,(System.currentTimeMillis()/1000.0)-2000));
      list.add(new SensorData("123",22.1,Unit.C,54.7,Unit.P,44.6,Unit.P,(System.currentTimeMillis()/1000.0)));

      repo.insertNewEntries(list);



    } catch (IOException | ASAPException | SharkException e) {
      e.printStackTrace();
      Toast.makeText(this,
        "fatal: " + e.getLocalizedMessage(),
        Toast.LENGTH_LONG).show();
    }
    // launch real first activity
    this.finish();
    Intent intent = new Intent(this, StartScreenActivity.class);
    this.startActivity(intent);
  }

  private CharSequence getASAPPeerId(Context context) {
    SharedPreferences sharedPref = context.getSharedPreferences(
      "ASAPPeerApplicationSideInitSettings", Context.MODE_PRIVATE);

    if (sharedPref.contains("ASAPPeer_Owner")) {
      return sharedPref.getString("ASAPPeer_Owner", "DEFAULT_OWNER_ID");
    } else return null;
  }

}

