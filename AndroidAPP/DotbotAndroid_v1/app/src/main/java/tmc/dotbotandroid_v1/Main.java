package tmc.dotbotandroid_v1;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;


public class Main extends AppCompatActivity implements SensorEventListener {

    private SensorManager senSensorManager;
    private Sensor senAccelerometer;

    private long lastUpdate = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;
                float x = sensorEvent.values[0];
                float y = sensorEvent.values[1];
                float z = sensorEvent.values[2];

                int result[] = calculateArduinoInputs(y,z);

                refreshGUI(result[0], result[1], result [2], result [3]);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public int[] calculateArduinoInputs(float y, float z) {
        int power = Math.round(z*10);
        int steering = Math.round(y*5);

        int motorLeft = power - steering;
        int motorRight = power + steering;

        if (motorLeft < -100){
            motorLeft = -100;
        }
        if (motorLeft > 100){
            motorLeft = 100;
        }
        if (motorRight < -100){
            motorRight = -100;
        }
        if (motorRight > 100){
            motorRight = 100;
        }
        if (Math.abs(motorLeft) < 10) {
            motorLeft = 0;
        }
        if (Math.abs(motorRight) < 10) {
            motorRight = 0;
        }
        return new int[]{motorLeft, motorRight, power, steering};
    }

    public void refreshGUI(float motorLeft, float motorRight, float power, float steering) {
        TextView text = (TextView)findViewById(R.id.number_1);
        text.setText("Motor Left: "+motorLeft);
        text = (TextView)findViewById(R.id.number_2);
        text.setText("Motor Right: "+motorRight);
        text = (TextView)findViewById(R.id.number_3);
        text.setText("Power: "+power);
        text = (TextView)findViewById(R.id.number_4);
        text.setText("Steering: "+steering);
    }

    protected void onPause() {
        super.onPause();
        senSensorManager.unregisterListener(this);
    }

    protected void onResume() {
        super.onResume();
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }
}
