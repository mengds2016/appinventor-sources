// -*- mode: java; c-basic-offset: 2; -*-
// Copyright 2009-2011 Google, All Rights reserved
// Copyright 2011-2012 MIT, All rights reserved
// Released under the MIT License https://raw.github.com/mit-cml/app-inventor/master/mitlicense.txt

package com.google.appinventor.components.runtime;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.common.YaVersion;
import com.google.appinventor.components.runtime.util.BluetoothReflection;
import com.google.appinventor.components.runtime.util.ErrorMessages;
import com.google.appinventor.components.runtime.util.SdkLevel;
import com.google.appinventor.components.runtime.util.TextViewUtil;

import com.google.appinventor.components.runtime.USBAccessoryManager;
import com.google.appinventor.components.runtime.USBAccessoryManagerMessage;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.content.Context;
import android.content.Intent;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * BluetoothClient component
 *
 * @author lizlooney@google.com (Liz Looney)
 */
@DesignerComponent(version = YaVersion.BLUETOOTHCLIENT_COMPONENT_VERSION,
    description = "UsbAccessory component",
    category = ComponentCategory.CONNECTIVITY,
    nonVisible = true,
    iconName = "images/usb.png")
@SimpleObject
@UsesPermissions(permissionNames =
                 "android.permission.BLUETOOTH, " +
                 "android.permission.BLUETOOTH_ADMIN")
public final class UsbAccessory  extends AndroidNonvisibleComponent
implements OnStopListener, OnResumeListener {
  private final List<DigitalRead> attachedComponents = new ArrayList<DigitalRead>();
  private final List<AndroidNonvisibleComponent> attachedComponents1 = new ArrayList<AndroidNonvisibleComponent>();
  private Set<Integer> acceptableDeviceClasses;
  private USBAccessoryManager accessoryManager;
  private final static int USBAccessoryWhat = 0;
  private int mtest;
  // Indicates whether the accelerometer should generate events
  private boolean enabled;
  private String TAG = "UsbAccessory";
  private int Flag = 0;
  byte[] commandPacket = new byte[13];
	
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			// super.handleMessage(msg);
			switch(msg.what)
			{	
				case USBAccessoryWhat:
				switch(((USBAccessoryManagerMessage)msg.obj).type) {
				case READ:
					//if(accessoryManager.isConnected() == false) {
					//	return;
					//}		
					//TimeText.setText("数据长度: " + Float.toString(accessoryManager.available())); // 刷新
						if(accessoryManager.available() < 13) {
							//All of our commands in this example are 2 bytes.  If there are less
							//  than 12 bytes left, it is a partial command
							break;
						}
						accessoryManager.read(commandPacket);
						if(commandPacket[0] == 0x00){
							//attachedComponents.get(0).digitalRead1((int)(commandPacket[5]));
							attachedComponents1.get(0).digitalRead2((int)(commandPacket[5]));
							Log.d(TAG,"attachedComponents.get(0).digitalRead1()");
						}
					}
				break;
			}
		}		
	};
  /**
   * Creates a new BluetoothClient.
   */
  public UsbAccessory(ComponentContainer container){
	    super(container.$form());
	    form.registerForOnResume(this);
	    form.registerForOnStop(this);
	    enabled = true;
    //super(container, "UsbAccessory");
    accessoryManager = new USBAccessoryManager(handler, USBAccessoryWhat);
    accessoryManager.enable(container.$context());
  }

  boolean attachComponent(AndroidNonvisibleComponent digitalRead, Set<Integer> acceptableDeviceClasses) {
    if (attachedComponents1.isEmpty()) {
      // If this is the first/only attached component, we keep the acceptableDeviceClasses.
      this.acceptableDeviceClasses = (acceptableDeviceClasses == null)
          ? null
          : new HashSet<Integer>(acceptableDeviceClasses);

    } else {
      // If there is already one or more attached components, the acceptableDeviceClasses must be
      // the same as what we already have.
      if (this.acceptableDeviceClasses == null) {
        if (acceptableDeviceClasses != null) {
          return false;
        }
      } else {
        if (acceptableDeviceClasses == null) {
          return false;
        }
        if (!this.acceptableDeviceClasses.containsAll(acceptableDeviceClasses)) {
          return false;
        }
        if (!acceptableDeviceClasses.containsAll(this.acceptableDeviceClasses)) {
          return false;
        }
      }
    }

    attachedComponents1.add(digitalRead);
    return true;
  }

  void detachComponent(AndroidNonvisibleComponent digitalRead) {
    attachedComponents1.remove(digitalRead);
    if (attachedComponents1.isEmpty()) {
      acceptableDeviceClasses = null;
    }
  }

  @SimpleProperty(
	      category = PropertyCategory.BEHAVIOR,
	      description = "The minimum interval between phone shakes")
	  public int Test() {
	    return mtest;
	  }
  
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_NON_NEGATIVE_INTEGER,
	      defaultValue = "800") //Default value derived by trial of 12 people on 3 different devices
	  @SimpleProperty
	  public void Test(int test) {
	    mtest = test;
	    byte[] USBCommandPacket = new byte[13];
		USBCommandPacket[0] = 0x05;//控制方式1：PWM直接控制
		USBCommandPacket[4] = 0x08;
		USBCommandPacket[5] = 0x00;
		USBCommandPacket[6] = 0x00;
		USBCommandPacket[7] = 0x03;
		USBCommandPacket[8] = 0x00;
		USBCommandPacket[9] = 0x00;
		USBCommandPacket[10] = 0x00;
		USBCommandPacket[11] = 0x00;
		USBCommandPacket[12] = (byte)0xFF;
	    accessoryManager.write(USBCommandPacket);
	    USBCommandPacket[11] = (byte)((int)USBCommandPacket[11] + 1);
	    if((int)USBCommandPacket[11]==20){
	    	USBCommandPacket[11] = 0x00;
	    }
	  }
  
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_NON_NEGATIVE_INTEGER,
	      defaultValue = "800") //Default value derived by trial of 12 people on 3 different devices
	  @SimpleProperty
	  public void Shoot(int test) {
	    mtest = test;
	    byte[] USBCommandPacketShoot = new byte[13];
		int speed = 200;
		for (int i = 5; i < 9; i++) {
			USBCommandPacketShoot[i] = (byte) (speed >>> (24 - (i-5) * 8));
		}
		//for (int i = 9; i < 13; i++) {
		//	USBCommandPacketShoot[i] = (byte) (right >>> (24 - (i-9)  * 8));
		//}				
		USBCommandPacketShoot[0] = 0x01;
		USBCommandPacketShoot[4] = 0x08;
		accessoryManager.write(USBCommandPacketShoot);
	  }
  
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_NON_NEGATIVE_INTEGER,
	      defaultValue = "800") //Default value derived by trial of 12 people on 3 different devices
	  @SimpleProperty
	  public void StopShoot(int test) {
	    mtest = test;
	    byte[] USBCommandPacketShoot = new byte[13];
		int speed = 50;
		for (int i = 5; i < 9; i++) {
			USBCommandPacketShoot[i] = (byte) (speed >>> (24 - (i-5) * 8));
		}
		//for (int i = 9; i < 13; i++) {
		//	USBCommandPacketShoot[i] = (byte) (right >>> (24 - (i-9)  * 8));
		//}				
		USBCommandPacketShoot[0] = 0x01;
		USBCommandPacketShoot[4] = 0x08;
		accessoryManager.write(USBCommandPacketShoot);
	  }

  /**
   * Decodes the given number String to an integer and writes it as two bytes
   * to the output stream.
   *
   * If the number could not be decoded to an integer, or the integer would not
   * fit in two bytes, then the Form's ErrorOccurred event is triggered and this
   * method returns without writing any bytes to the output stream.
   *
   * @param number the number to write
   */
  @SimpleFunction(description = "Send CMD to the HippoADK.")
  public void Send2ByteNumber(String number) {
    String functionName = "Send2ByteNumber";
    byte[] USBCommandPacketShoot = new byte[13];
	int speed = 50;
	//for (int i = 5; i < 9; i++) {
	//	USBCommandPacketShoot[i] = (byte) (speed >>> (24 - (i-5) * 8));
	//}
	//for (int i = 9; i < 13; i++) {
	//	USBCommandPacketShoot[i] = (byte) (right >>> (24 - (i-9)  * 8));
	//}				
	USBCommandPacketShoot[0] = 0x00;
	USBCommandPacketShoot[4] = 0x08;
	if(Flag == 0){
		USBCommandPacketShoot[5] = 0x00;
		Flag = 1;
	}else{
		USBCommandPacketShoot[5] = 0x01;
		Flag = 0;
	}
    accessoryManager.write(USBCommandPacketShoot);

  }
  
  /**
   * Specifies the text displayed by the label.
   *
   * @param text  new caption for label
   */
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING,
      defaultValue = "")
  @SimpleProperty
  public void SendCMD(String cmd) {
    //TextViewUtil.setText(view, text);
	  Log.d(TAG,"SendCMD" + cmd);
	    byte[] USBCommandPacketShoot = new byte[13];
		int speed = 50;
		for (int i = 5; i < 9; i++) {
			USBCommandPacketShoot[i] = (byte) (speed >>> (24 - (i-5) * 8));
		}
		//for (int i = 9; i < 13; i++) {
		//	USBCommandPacketShoot[i] = (byte) (right >>> (24 - (i-9)  * 8));
		//}				
		USBCommandPacketShoot[0] = 0x00;
		USBCommandPacketShoot[4] = 0x08;
	    accessoryManager.write(USBCommandPacketShoot);
  }
  
  protected final  void SendCMD1(String cmd) {
	    //TextViewUtil.setText(view, text);
	  Log.d(TAG,"SendCMD" + cmd);
	    byte[] USBCommandPacketShoot = new byte[13];
		int speed = 50;
		//for (int i = 5; i < 9; i++) {
		//	USBCommandPacketShoot[i] = (byte) (speed >>> (24 - (i-5) * 8));
		//}
		//for (int i = 9; i < 13; i++) {
		//	USBCommandPacketShoot[i] = (byte) (right >>> (24 - (i-9)  * 8));
		//}				
		USBCommandPacketShoot[0] = 0x00;
		USBCommandPacketShoot[4] = 0x08;
		if(Flag == 0){
			USBCommandPacketShoot[5] = 0x00;
			Flag = 1;
		}else{
			USBCommandPacketShoot[5] = 0x01;
			Flag = 0;
		}
	    accessoryManager.write(USBCommandPacketShoot);
	    
	  }
  
  @Override
  public void onResume() {
    if (enabled) {
      startListening();
    }
  }

  // OnStopListener implementation

  @Override
  public void onStop() {
    if (enabled) {
      stopListening();
    }
    
  }
  
  // Assumes that sensorManager has been initialized, which happens in constructor
  private void startListening() {
    //sensorManager.registerListener(this, gyroscopeSensor, SensorManager.SENSOR_DELAY_GAME);
	//sensorManager.registerListener(this, gyroscopeSensor, 10000);
  }

  // Assumes that sensorManager has been initialized, which happens in constructor
  private void stopListening() {
    //sensorManager.unregisterListener(this);
  }

  protected final int read(String functionName, int numberOfBytes) {
	  	int i = 0;
	    return 0;
	  }


  



  
}
