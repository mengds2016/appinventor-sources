// -*- mode: java; c-basic-offset: 2; -*-
// Copyright 2009-2011 Google, All Rights reserved
// Copyright 2011-2012 MIT, All rights reserved
// Released under the MIT License https://raw.github.com/mit-cml/app-inventor/master/mitlicense.txt


package com.google.appinventor.components.runtime;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.common.YaVersion;
import com.google.appinventor.components.runtime.util.ErrorMessages;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Physical world component that can detect shaking and measure
 * acceleration in three dimensions.  It is implemented using
 * android.hardware.SensorListener
 * (http://developer.android.com/reference/android/hardware/SensorListener.html).
 *
 * <p>From the Android documentation:
 * "Sensor values are acceleration in the X, Y and Z axis, where the X axis
 * has positive direction toward the right side of the device, the Y axis has
 * positive direction toward the top of the device and the Z axis has
 * positive direction toward the front of the device. The direction of the
 * force of gravity is indicated by acceleration values in the X, Y and Z
 * axes. The typical case where the device is flat relative to the surface of
 * the Earth appears as -STANDARD_GRAVITY in the Z axis and X and Y values
 * close to zero. Acceleration values are given in SI units (m/s^2)."
 *
 */
// TODO(user): ideas - event for knocking
@DesignerComponent(version = YaVersion.ACCELEROMETERSENSOR_COMPONENT_VERSION,
    description = "Non-visible component that can detect shaking and " +
    "measure acceleration approximately in three dimensions using SI units " +
    "(m/s<sup>2</sup>).  The components are: <ul>\n" +
    "<li> <strong>xAccel</strong>: 0 when the phone is at rest on a flat " +
    "     surface, positive when the phone is tilted to the right (i.e., " +
    "     its left side is raised), and negative when the phone is tilted " +
    "     to the left (i.e., its right size is raised).</li>\n " +
    "<li> <strong>yAccel</strong>: 0 when the phone is at rest on a flat " +
    "     surface, positive when its bottom is raised, and negative when " +
    "     its top is raised. </li>\n " +
    "<li> <strong>zAccel</strong>: Equal to -9.8 (earth's gravity in meters per " +
    "     second per second when the device is at rest parallel to the ground " +
    "     with the display facing up, " +
    "     0 when perpindicular to the ground, and +9.8 when facing down.  " +
    "     The value can also be affected by accelerating it with or against " +
    "     gravity. </li></ul>",
    category = ComponentCategory.CONNECTIVITY,
    nonVisible = true,
    iconName = "images/wifi.png")
@SimpleObject
@UsesPermissions(permissionNames =
				"android.permission.INTERNET")
public class UDPSocket extends AndroidNonvisibleComponent  {
  private static final int TOY_ROBOT = 0x0804; // from android.bluetooth.BluetoothClass.Device.
  
  protected UsbAccessory usbaccessory;
  private String TAG = "UDPSocket";
  private String RemotePort = "";
  private String LocalPort = "";
  private String RemoteIPAdress = "";
  private int Flag = 0;
	
	DatagramSocket socket;
	private boolean UDPisConnecting = false;
	private Thread mUDPThread = null;
	public static String msgText = "12345678";
	public static String UDPIPText = "192.168.1.104:8082";
    byte[] WIFICommandPacket = new byte[13];
    byte[] WIFISendCommandPacket = new byte[13];
	byte[] repacs;						
	byte[] repac;							
	private String recvMessageClient = "";
	private int Event = 0;
	private final static int MessageWhat0 = 0;
	private final static int MessageWhat1 = 1;
	private final static int MessageWhat2 = 2;
	private final static int MessageWhat3 = 3;
	private final static int MessageWhat4 = 4;
	  // Indicates whether the accelerometer should generate events
	  private boolean enabled;
	  
		private Handler handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				// super.handleMessage(msg);
				switch(msg.what)
				{	
					case MessageWhat0:
						Log.d(TAG, "socket0" + "\n");
						ReadCommand(0);
					break;
					case MessageWhat1:
						Log.d(TAG, "socket1" + "\n");
						ReadCommand(1);
					break;
					case MessageWhat2:
						Log.d(TAG, "socket2" + "\n");
						ReadCommand(2);
					break;
					case MessageWhat3:
						Log.d(TAG, "socket3" + "\n");
						ReadCommand(3);
					break;
					case MessageWhat4:
						Log.d(TAG, "socket4" + "\n");
						ReadCommand(4);
					break;
				}
			}		
		};
		
  /**
   * Creates a new AccelerometerSensor component.
   *
   * @param container  ignored (because this is a non-visible component)
   */
  public UDPSocket(ComponentContainer container){
    super(container.$form());
    //form.registerForOnResume(this);
    //form.registerForOnStop(this);
    enabled = false;
  }
  
  /**
   * If true, the sensor will generate events.  Otherwise, no events
   * are generated even if the device is accelerated or shaken.
   *
   * @return {@code true} indicates that the sensor generates events,
   *         {@code false} that it doesn't
   */
  @SimpleProperty(
      category = PropertyCategory.BEHAVIOR)
  public boolean Enabled() {
    return enabled;
  }
  
  /**
   * Specifies whether the sensor should generate events.  If true,
   * the sensor will generate events.  Otherwise, no events are
   * generated even if the device is accelerated or shaken.
   *
   * @param enabled  {@code true} enables sensor event generation,
   *                 {@code false} disables it
   */
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN,
      defaultValue = "False")
  @SimpleProperty
  public void Enabled(boolean enabled) {
    if (this.enabled == enabled) {
      return;
    }
    this.enabled = enabled;
    if (enabled) {
    	if (UDPisConnecting) 
    	{				
    		UDPisConnecting = false;
    		if(socket!=null)
    		{
    			socket.close();
    			socket = null;
    		} 
    		mUDPThread.interrupt();
    	}
    	else
    	{				
    		UDPisConnecting = true;
    		mUDPThread = new Thread(mUDPRunnable);
    		mUDPThread.start();				
    	}
    } else {
      //stopListening();
    }
  }
  
  /**

   */
  @SimpleProperty(category = PropertyCategory.BEHAVIOR,description = "The RemotePort used")
  public String RemotePort() {
    return RemotePort;
  }
  
  /**
   * Specifies the motor ports that are used for driving.
   */
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING,
      defaultValue = "")
  @SimpleProperty
  public void RemotePort(String remotePort) {
	  RemotePort = remotePort;
  }
  
  /**

   */
  @SimpleProperty(category = PropertyCategory.BEHAVIOR,description = "RemoteIPAdress set")
  public String RemoteIPAdress() {
    return RemoteIPAdress;
  }
  
  /**
   * Specifies the motor ports that are used for driving.
   */
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING,
      defaultValue = "")
  @SimpleProperty
  public void RemoteIPAdress(String remoteIPAdress) {
	  RemoteIPAdress = remoteIPAdress;
  }
  
  /**

   */
  @SimpleProperty(category = PropertyCategory.BEHAVIOR,description = "The LocalPort used")
  public String LocalPort() {
    return LocalPort;
  }
  
  /**
   * Specifies the motor ports that are used for driving.
   */
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING,
      defaultValue = "")
  @SimpleProperty
  public void LocalPort(String localPort) {
	  LocalPort = localPort;
  }
  
  /**
   * Returns the acceleration in the X-dimension in SI units (m/s^2).
   * The sensor must be enabled to return meaningful values.
   *
   * @return  X acceleration
   */
  @SimpleProperty(
      category = PropertyCategory.BEHAVIOR)
  public float Event() {
    return Event;
  }
    
	private Runnable mUDPRunnable = new Runnable() {
		public void run() {
			String msgText = UDPIPText;
			if (msgText.length() <= 0) {
				//Message msg = new Message();
				//msg.what = 2;
				return;
			}
			
			int start = msgText.indexOf(":");
			if ((start == -1) || (start + 1 >= msgText.length())) {
				//Message msg = new Message();
				//msg.what = 2;
				// mHandler.sendMessage(msg);
				return;
			}
			
			//String sIP = msgText.substring(0, start);
			//String sPort = msgText.substring(start + 1);
			int port = Integer.parseInt(LocalPort);

			try {
				socket = new DatagramSocket(port);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.d(TAG, "socket" + "\n");
			}

			char[] buffer = new char[256];
			int count = 0;
			boolean UDPisConnecting1 = false;
			while (UDPisConnecting) {

				// byte buff[]=new byte[20];
				// DatagramPacket packet01=new DatagramPacket(buff,buff.length);
				DatagramPacket packet01 = new DatagramPacket(WIFICommandPacket,
						WIFICommandPacket.length);
				try {
					//System.out.println("di " + sendtime + "ci receiver!");
					socket.receive(packet01); 
					repacs = packet01.getData();
					if (repacs[0] == 0x00) {
						Message msg = new Message();
						msg.what = 0;
						handler.sendMessage(msg);
					}else if(repacs[0] == 0x01){
						Message msg = new Message();
						msg.what = 1;
						handler.sendMessage(msg);
					}else if(repacs[0] == 0x02){
						Message msg = new Message();
						msg.what = 2;
						handler.sendMessage(msg);
					}else if(repacs[0] == 0x03){
						Message msg = new Message();
						msg.what = 3;
						handler.sendMessage(msg);
					}else if(repacs[0] == 0x04){
						Message msg = new Message();
						msg.what = 4;
						handler.sendMessage(msg);
					}
					//handler.obtainMessage(what).sendToTarget();


				} catch (IOException e)
				{

					e.printStackTrace();
				} 
			}
		}
	};
	
	public void UDPSendCmd(byte[] command) {
		if (UDPisConnecting) {
			if (command.length <= 0) {

			} else {
				try {
					if (UDPIPText.length() <= 0) {
						recvMessageClient = "IP不能为空！\n";
						return;
					}
					int startUDP = UDPIPText.indexOf(":");
					if ((startUDP == -1)
							|| (startUDP + 1 >= UDPIPText.length())) {
						recvMessageClient = "IP地址不合法\n";
						return;
					}
					//String sIPUDP = UDPIPText.substring(0, startUDP);
					//String sPortUDP = UDPIPText.substring(startUDP + 1);
					//String sIPUDP = UDPIPText.substring(0, startUDP);
					//String sPortUDP = UDPIPText.substring(startUDP + 1);
					int portUDP = Integer.parseInt(RemotePort);
					// byte data [] = msgText.getBytes();
					InetAddress serverAddressUDP = InetAddress
							.getByName(RemoteIPAdress);

					DatagramPacket packet = new DatagramPacket(command,
							command.length, serverAddressUDP, portUDP);
					socket.send(packet);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		} else {
			
		}
	}
	
	@SimpleFunction(description = "UDPSendCommand")
	public void UDPSendCommand(int command) {
		if(command == 0){
			WIFISendCommandPacket[0] = 0x00;
			UDPSendCmd(WIFISendCommandPacket);
		}else if(command == 1){
			WIFISendCommandPacket[0] = 0x01;
			UDPSendCmd(WIFISendCommandPacket);
		}else if(command == 2){
			WIFISendCommandPacket[0] = 0x02;
			UDPSendCmd(WIFISendCommandPacket);
		}else if(command == 3){
			WIFISendCommandPacket[0] = 0x03;
			UDPSendCmd(WIFISendCommandPacket);
		}else if(command == 4){
			WIFISendCommandPacket[0] = 0x04;
			UDPSendCmd(WIFISendCommandPacket);
		}
	}
	  /**
	   * Indicates the acceleration changed in the X, Y, and/or Z dimensions.
	   */
	  @SimpleEvent
	  public void ReadCommand(int event) {
	    this.Event = event;
	    EventDispatcher.dispatchEvent(this, "ReadCommand", Event);
	  }
}
