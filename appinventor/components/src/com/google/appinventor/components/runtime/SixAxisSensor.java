// -*- mode: java; c-basic-offset: 2; -*-
// Copyright 2009-2011 Google, All Rights reserved
// Copyright 2011-2012 MIT, All rights reserved
// Released under the MIT License https://raw.github.com/mit-cml/app-inventor/master/mitlicense.txt


package com.google.appinventor.components.runtime;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.common.YaVersion;
import com.google.appinventor.components.runtime.util.ErrorMessages;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

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
    category = ComponentCategory.SENSORS,
    nonVisible = true,
    iconName = "images/accelerometersensor.png")
@SimpleObject
public class SixAxisSensor extends AndroidNonvisibleComponent
    implements OnStopListener, OnResumeListener, SensorComponent, SensorEventListener, Deleteable {

  // Shake thresholds - derived by trial
  private static final double weakShakeThreshold = 5.0;
  private static final double moderateShakeThreshold = 13.0;
  private static final double strongShakeThreshold = 20.0;

  // Cache for shake detection
  private static final int SENSOR_CACHE_SIZE = 10;
  private final Queue<Float> X_CACHE = new LinkedList<Float>();
  private final Queue<Float> Y_CACHE = new LinkedList<Float>();
  private final Queue<Float> Z_CACHE = new LinkedList<Float>();

  // Backing for sensor values
  private float xAccel;
  private float yAccel;
  private float zAccel;
  private float angleAcceleration;
  private float angleGyroscope;
  private float angleKalmanFilter;

  private int accuracy;

  private int sensitivity;

  // Sensor manager
  private final SensorManager sensorManager;

  // Indicates whether the accelerometer should generate events
  private boolean enabled;

  //Specifies the minimum time interval between calls to Shaking()
  private int minimumInterval;

  //Specifies the time when Shaking() was last called
  private long timeLastShook;

  private Sensor accelerometerSensor;
  private Sensor gyroscopeSensor;
  
  private static float Q_angle=0.005f, Q_gyro=0.003f, R_angle=0.5f, dt=0.010f;
  private static float q_bias=0, angle_err, PCt_0, PCt_1, E, K_0, K_1, t_0, t_1;
  private static float P[][] = { { 1, 0 }, { 0, 1 } };
  private static float Pdot[] ={0,0,0,0};
  private static char C_0 = 1;
  
  private float Sum_Measure_Gyroscope;
  private float angle;
  private float gyroscope;
  private float Angle_Acceleration;
  
  private long Current_Time = 0, Early_Time = 0, Use_Time = 0;
	

  /**
   * Creates a new AccelerometerSensor component.
   *
   * @param container  ignored (because this is a non-visible component)
   */
  public SixAxisSensor(ComponentContainer container) {
    super(container.$form());
    form.registerForOnResume(this);
    form.registerForOnStop(this);

    enabled = true;
    sensorManager = (SensorManager) container.$context().getSystemService(Context.SENSOR_SERVICE);
    accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
    startListening();
    //MinimumInterval(10);
    //Sensitivity(Component.ACCELEROMETER_SENSITIVITY_MODERATE);
  }

  /**
   * Indicates the angle measured from acceleration sensor. 
   */
  @SimpleEvent
  public void AngleAccelerationChanged(float angleAcceleration) {
    this.angleAcceleration = angleAcceleration;
    
    EventDispatcher.dispatchEvent(this, "AngleAccelerationChanged", angleAcceleration);
  }
  
  /**
   * Indicates the angle measured from gyroscope sensor. 
   */
  @SimpleEvent
  public void AngleGyroscopeChanged(float angleGyroscope) {
    this.angleGyroscope = angleGyroscope;
    
    EventDispatcher.dispatchEvent(this, "AngleGyroscopeChanged", angleGyroscope);
  }
  
  /**
   * Indicates the angle measured from kalman filter. 
   */
  @SimpleEvent
  public void AngleKalmanFilterChanged(float angle, float gyroscope) {
    this.angle = angle;
    this.gyroscope = gyroscope;
    
    EventDispatcher.dispatchEvent(this, "AngleKalmanFilterChanged", angle, gyroscope);
  }
  
  /**
   * Available property getter method (read-only property).
   *
   * @return {@code true} indicates that an accelerometer sensor is available,
   *         {@code false} that it isn't
   */
  @SimpleProperty(
      category = PropertyCategory.BEHAVIOR)
  public boolean Available() {
    List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
    return (sensors.size() > 0);
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

  // Assumes that sensorManager has been initialized, which happens in constructor
  private void startListening() {
    //sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_GAME);
    sensorManager.registerListener(this, accelerometerSensor, 20000);
    sensorManager.registerListener(this, gyroscopeSensor, 10000);
  }

  // Assumes that sensorManager has been initialized, which happens in constructor
  private void stopListening() {
    sensorManager.unregisterListener(this);
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
      defaultValue = "True")
  @SimpleProperty
  public void Enabled(boolean enabled) {
    if (this.enabled == enabled) {
      return;
    }
    this.enabled = enabled;
    if (enabled) {
      startListening();
    } else {
      stopListening();
    }
  }

  /**
   * Returns the acceleration in the X-dimension in SI units (m/s^2).
   * The sensor must be enabled to return meaningful values.
   *
   * @return  X acceleration
   */
  @SimpleProperty(
      category = PropertyCategory.BEHAVIOR)
  public float AngleAcceleration() {
    return angleAcceleration;
  }

  /**
   * Returns the acceleration in the X-dimension in SI units (m/s^2).
   * The sensor must be enabled to return meaningful values.
   *
   * @return  X acceleration
   */
  @SimpleProperty(
      category = PropertyCategory.BEHAVIOR)
  public float AngleGyroscope() {
    return angleGyroscope;
  }
  
  /**
   * Returns the acceleration in the X-dimension in SI units (m/s^2).
   * The sensor must be enabled to return meaningful values.
   *
   * @return  X acceleration
   */
  @SimpleProperty(
      category = PropertyCategory.BEHAVIOR)
  public float Angle() {
    return angle;
  }
  
  /**
   * Returns the acceleration in the X-dimension in SI units (m/s^2).
   * The sensor must be enabled to return meaningful values.
   *
   * @return  X acceleration
   */
  @SimpleProperty(
      category = PropertyCategory.BEHAVIOR)
  public float Gyroscope() {
    return gyroscope;
  }
  
  /*
   * Updating sensor cache, replacing oldest values.
   */
  private void addToSensorCache(Queue<Float> cache, float value) {
    if (cache.size() >= SENSOR_CACHE_SIZE) {
      cache.remove();
    }
    cache.add(value);
  }

  // SensorListener implementation
  @Override
  public void onSensorChanged(SensorEvent event) {
    if (enabled) {
    	if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			Angle_Acceleration =  Angle_Acceleration_X(
					event.values[SensorManager.DATA_Z],
					event.values[SensorManager.DATA_Y]) - 90;
			AngleAccelerationChanged(Angle_Acceleration);
		}else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
        	Current_Time=event.timestamp;
        	Current_Time= System.currentTimeMillis();
        	if(Early_Time==0)
        		Use_Time=0;
        	else
        		Use_Time=(Current_Time-Early_Time);
			Early_Time=Current_Time;
			//dt = (float)Use_Time *(1.0f / 1000.0f);
			float Measure_Gyroscope = 57.3f * (event.values[SensorManager.DATA_X]);
			//float Measure_Gyroscope = -1*57.3f * (event.values[SensorManager.DATA_Y]);
			Sum_Measure_Gyroscope += Measure_Gyroscope * dt;
			AngleGyroscopeChanged(Sum_Measure_Gyroscope);
			Kalman_Filter(Angle_Acceleration,Measure_Gyroscope);
		}
      //final float[] values = event.values;
      //xAccel = values[0];
      //yAccel = values[1];
      //zAccel = values[2];
      //accuracy = event.accuracy;
      //AccelerationChanged(xAccel, yAccel, zAccel);
    }
  }

  @Override
  public void onAccuracyChanged(Sensor sensor, int accuracy) {
    // TODO(markf): Figure out if we actually need to do something here.
  }

  // OnResumeListener implementation

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

  // Deleteable implementation

  @Override
  public void onDelete() {
    if (enabled) {
      stopListening();
    }
  }
	
	public void Kalman_Filter(float angleaccelerometer,float anglegyroscope)		
	{
		angle += (anglegyroscope - q_bias) * dt;
		Pdot[0] = Q_angle - P[0][1] - P[1][0];
		Pdot[1] = -P[1][1];
		Pdot[2] = -P[1][1];
		Pdot[3] = Q_gyro;

		P[0][0] += Pdot[0] * dt;
		P[0][1] += Pdot[1] * dt;
		P[1][0] += Pdot[2] * dt;
		P[1][1] += Pdot[3] * dt;

		angle_err = angleaccelerometer - angle;

		PCt_0 = C_0 * P[0][0];
		PCt_1 = C_0 * P[1][0];

		E = R_angle + C_0 * PCt_0;

		K_0 = PCt_0 / E;
		K_1 = PCt_1 / E;

		t_0 = PCt_0;
		t_1 = C_0 * P[0][1];

		P[0][0] -= K_0 * t_0;
		P[0][1] -= K_0 * t_1;
		P[1][0] -= K_1 * t_0;
		P[1][1] -= K_1 * t_1;

		angle += K_0 * angle_err;
		q_bias += K_1 * angle_err;
		gyroscope = anglegyroscope - q_bias;
		AngleKalmanFilterChanged(angle,gyroscope);
	}
	
	public float Angle_Acceleration_X(float y,float z)		
	{
		float g = (float)Math.sqrt(z * z + y*y );//竖放
		float cos = y / g;//竖放
		float AccelerometerAngle = (float) Math.acos(cos) * 57.3f;// 横放
		return AccelerometerAngle;
	}
	
}
