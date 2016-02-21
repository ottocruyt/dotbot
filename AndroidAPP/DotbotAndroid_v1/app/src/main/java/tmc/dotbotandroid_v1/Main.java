package tmc.dotbotandroid_v1;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.text.DecimalFormat;


public class Main extends AppCompatActivity implements SensorEventListener {

    private SensorManager senSensorManager;
    private Sensor senAccelerometer;

    private boolean startButtonPressed;
    private boolean connectButtonPressed;

    private long lastUpdate = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        startButtonPressed = false;
        connectButtonPressed = false;
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

        int power = Math.round(z * 10);
        int steering = Math.round(y * 5);

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
        if (!startButtonPressed){
            motorRight = 0;
            motorLeft = 0;
        }

        return new int[]{motorLeft, motorRight, power, steering};
    }

    public void refreshGUI(float motorLeft, float motorRight, float power, float steering) {
        TextView text = (TextView)findViewById(R.id.number_1);

        //format to not show decimals
        DecimalFormat nd = new DecimalFormat("###");
        text.setText("Motor Left: " + nd.format(motorLeft));
        text = (TextView)findViewById(R.id.number_2);
        text.setText("Motor Right: " + nd.format(motorRight));
        text = (TextView)findViewById(R.id.number_3);
        text.setText(nd.format(power));
        text = (TextView)findViewById(R.id.number_4);
        text.setText(nd.format(steering));
    }

    public void startButtonClick(View view) {

        final Button startButton = (Button) findViewById(R.id.button);

        if (startButtonPressed){
            startButton.setText("Start");
            startButton.setTextColor(ContextCompat.getColor(this,R.color.startColor));
            startButtonPressed = false;
        }
        else{
            startButton.setText("Stop");
            startButton.setTextColor(ContextCompat.getColor(this,R.color.stopColor));
            startButtonPressed = true;
        }

    }

    public void connectButtonClick(View view) {

        final Button connectButton = (Button) findViewById(R.id.button2);

        if (connectButtonPressed){
            connectButton.setText("Connect");
            connectButton.setTextColor(ContextCompat.getColor(this,R.color.startColor));
            connectButtonPressed = false;
        }
        else{
            connectButton.setText("Disconnect");
            connectButton.setTextColor(ContextCompat.getColor(this,R.color.stopColor));
            connectButtonPressed = true;
        }

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
