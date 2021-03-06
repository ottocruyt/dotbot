package tmc.dotbotandroid_v1;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by Steven on 4/06/2016.
 */
public class Controller extends Thread implements SensorEventListener  {

    private Context mContext;
    private Calculator mCalculator;
    public Cache mCache;
    public Bluetooth mBluetooth;
    private SensorManager senSensorManager; //controller
    private Sensor senAccelerometer;//controller
    private long lastUpdate = 0; //controller

    Controller(Context context) {
        this.mContext = context;
        mCalculator = new Calculator();
        mCache = new Cache();
        mBluetooth = new Bluetooth(mContext);
        senSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE); //controller
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER); //controller
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL); //controller
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { //controller
    }

    @Override
    // This method is called every time the sensor values change
    public void onSensorChanged(SensorEvent sensorEvent) { //controller
        Sensor mySensor = sensorEvent.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            // Call the current time so the sensor values are only updated after 100 ms.
            long curTime = System.currentTimeMillis();
            if ((curTime - lastUpdate) > 100) {
                lastUpdate = curTime;
                float x = sensorEvent.values[0];
                float y = sensorEvent.values[1];
                float z = sensorEvent.values[2];

                // Only the y- and z-axis are used to control the dotbot
                // Once the new values are known, the Arduino input values need to be calculated
                int[] motorInputs = mCalculator.calculateMotorInputs(y,z,mCache.startButtonPressed);
                mCache.motorLeft = motorInputs[0];
                mCache.motorRight = motorInputs[1];
                mCache.power = motorInputs[2];
                mCache.steering = motorInputs[3];

                // The new values need to be displayed in the GUI
                //refreshGUI(result[0], result[1], result[2], result[3]);

                  if (mCache.bluetoothConnected){
                    mBluetooth.sendData(mCache.motorLeft, mCache.motorRight);

                  }
            }
        }
    }

    public Bluetooth.returnCodes enableBluetooth(){
        return mBluetooth.enable();
    }
    public Bluetooth.returnCodes startBluetooth(){
        return mBluetooth.start();
    }
    public Bluetooth.returnCodes disableBluetooth(){
        return mBluetooth.disable();
    }


    public void Pause(){
        senSensorManager.unregisterListener(this);
    }

    public void Resume(){
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }
}