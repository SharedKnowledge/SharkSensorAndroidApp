package net.sharksystem.sensor;

import net.sharksystem.SharkPeer;
import net.sharksystem.SharkPeerFS;

public class SharkPeerSingleton {
  private static SharkPeer sharkPeer = null;
  public static SharkPeer getSharkPeer(){
    if(SharkPeerSingleton.sharkPeer != null){
      return sharkPeer;
    }
    return null;
  }
  public static void setSharkPeer(SharkPeer sharkPeer){
    SharkPeerSingleton.sharkPeer = sharkPeer;
  }
}
