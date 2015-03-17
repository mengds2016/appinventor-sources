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
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.common.YaVersion;
import com.google.appinventor.components.runtime.util.ErrorMessages;

import android.util.Log;

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
    category = ComponentCategory.HIPPOADK,
    nonVisible = true,
    iconName = "images/hippoadk.png")
@SimpleObject
public class PIDMotor extends AndroidNonvisibleComponent  {
  private static final int TOY_ROBOT = 0x0804; // from android.bluetooth.BluetoothClass.Device.
  
  protected UsbAccessory usbaccessory;
  private String TAG = "AnalogWrite";
  private String MotorPort = "";
  private int Flag = 0;
  
  public int KAngle = 0;
  public int KAngleSpeed = 0;
  public int KPosition = 0;
  public int KPositionSpeed = 0;
  public int KBaseAngle = 0;
  private int   SpeedMotorRight = 0;		 
  private int   SpeedMotorLeft = 0;		 
	private float Speed_r_l = 0;	
	private float Speed = 0;        
	private float Position = 0;	    
	private float SpeedNeed = 0;
	private float TurnNeed = 0;
	private float TurnNeed_US = 0;
	private int Mode = 0;
  
  /**
   * Creates a new AccelerometerSensor component.
   *
   * @param container  ignored (because this is a non-visible component)
   */
  public PIDMotor(ComponentContainer container){
    super(container.$form());
    //form.registerForOnResume(this);
    //form.registerForOnStop(this);
  }
  
  /**

   */
  @SimpleProperty(category = PropertyCategory.BEHAVIOR,description = "The Motor Port used")
  public String MotorPort() {
    return MotorPort;
  }
  
  /**
   * Specifies the motor ports that are used for driving.
   */
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING,
      defaultValue = "")
  @SimpleProperty
  public void MotorPort(String motorport) {
	  MotorPort = motorport;
  }
  
  /**

   */
  @SimpleProperty(category = PropertyCategory.BEHAVIOR,description = "PID kAngle set")
  public int KAngle() {
    return KAngle;
  }
  
  /**
   * Specifies the motor ports that are used for driving.
   */
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING,
      defaultValue = "")
  @SimpleProperty
  public void KAngle(int kAngle) {
	  KAngle = kAngle;
  }
  
  /**

   */
  @SimpleProperty(category = PropertyCategory.BEHAVIOR,description = "PID kAngle set")
  public float TurnNeed() {
    return TurnNeed;
  }
  
  /**
   * Specifies the motor ports that are used for driving.
   */
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING,
      defaultValue = "")
  @SimpleProperty
  public void TurnNeed(float turnNeed) {
	  TurnNeed = turnNeed;
  }
  
  /**

   */
  @SimpleProperty(category = PropertyCategory.BEHAVIOR,description = "PID kAngle set")
  public int SetMode() {
    return Mode;
  }
  
  /**
   * Specifies the motor ports that are used for driving.
   */
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING,
      defaultValue = "")
  @SimpleProperty
  public void SetMode(int mode) {
	  Mode = mode;
  }
  /**

   */
  @SimpleProperty(category = PropertyCategory.BEHAVIOR,description = "PID kAnglespeed set")
  public int KAngleSpeed() {
    return KAngleSpeed;
  }
  
  /**
   * Specifies the motor ports that are used for driving.
   */
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING,
      defaultValue = "")
  @SimpleProperty
  public void KAnglesSpeed(int kAngleSpeed) {
	  KAngleSpeed = kAngleSpeed;
  }
  
  /**

   */
  @SimpleProperty(category = PropertyCategory.BEHAVIOR,description = "PID KPosition set")
  public float KPosition() {
    return KPosition;
  }
  
  /**
   * Specifies the motor ports that are used for driving.
   */
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING,
      defaultValue = "")
  @SimpleProperty
  public void KPosition(int kPosition) {
	  KPosition = kPosition;
  }
  
  /**

   */
  @SimpleProperty(category = PropertyCategory.BEHAVIOR,description = "PID kPositionspeed set")
  public float KPositionSpeed() {
    return KPositionSpeed;
  }
  
  /**
   * Specifies the motor ports that are used for driving.
   */
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING,
      defaultValue = "")
  @SimpleProperty
  public void KPositionSpeed(int kPositionSpeed) {
	  KPositionSpeed = kPositionSpeed;
  }
  
  /**

   */
  @SimpleProperty(category = PropertyCategory.BEHAVIOR,description = "PID KBaseAngle set")
  public float KBaseAngle() {
    return KBaseAngle;
  }
  
  /**
   * Specifies the motor ports that are used for driving.
   */
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_STRING,
      defaultValue = "")
  @SimpleProperty
  public void KBaseAngle(int kBaseAngle) {
	  KBaseAngle = kBaseAngle;
  }

  @SimpleProperty
  public void SpeedMotorRight(int speedMotorRight) {
	  SpeedMotorRight = speedMotorRight;
  }
  
  @SimpleProperty
  public void SpeedMotorLeft(int speedMotorLeft) {
	  SpeedMotorLeft = speedMotorLeft;
	  Speed_r_l = (SpeedMotorRight + SpeedMotorLeft)*0.5f;
	    Speed *= 0.7f;		                  //车轮速度滤波
	    Speed += Speed_r_l*0.3f;
		//speed = speed_r_l;
	    Position += Speed;	                  //积分得到位移
	    if(Position<-360) Position = -360; 
	    if(Position> 360) Position =  360;
	    //if(position<-1000) position = -1000; 
	    //if(position> 1000) position =  1000;
	    Position += SpeedNeed;
  }
  
	@SimpleFunction(description = "MotorDrive")
	public void MotorDrive(float angle, float gyroscope) {
		float PWM = 0;
		if(Mode == 0){
			PWM = -1*(KAngle*(angle + 0.5f - KBaseAngle*0.15f)*1.2f + KAngleSpeed*gyroscope*0.08f + KPosition*Position*0.03f + KPositionSpeed*Speed*0.6f);//红米note中轮可用
		}else if(Mode == 1){
			PWM = -1*(KAngle*(angle - 7.0f + KBaseAngle*0.1f)*0.8f + KAngleSpeed*gyroscope*0.05f + KPosition*Position*0.01f + KPositionSpeed*Speed*0.3f);//nexus7大轮尚可0606
		}
		
		byte[] USBCommandPacket = new byte[13];
		int PWMLeft = (int)(PWM + TurnNeed + TurnNeed_US);
		int PWMRight = (int)(PWM - TurnNeed - TurnNeed_US);
		for (int i = 5; i < 9; i++) {
			USBCommandPacket[i] = (byte) (PWMLeft >>> (24 - (i - 5) * 8));
		}
		for (int i = 9; i < 13; i++) {
			USBCommandPacket[i] = (byte) (PWMRight >>> (24 - (i - 9)  * 8));
		}		
		USBCommandPacket[0] = 0x00;//控制方式1：PWM直接控制
		USBCommandPacket[4] = 0x08;//控制方式1：PWM直接控制
		usbaccessory.SendCommand(USBCommandPacket);
	}

  /**
   * Specifies the BluetoothClient component that should be used for communication.
   */
  @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_UsbAccessory,
      defaultValue = "")
  @SimpleProperty(userVisible = false)
  public void UsbAccessory(UsbAccessory usbaccessory1) {
    if (usbaccessory1 != null) {
    	usbaccessory = usbaccessory1;
    	//usbaccessory.attachComponent(this, Collections.singleton(TOY_ROBOT));
    }
  }
  
}
