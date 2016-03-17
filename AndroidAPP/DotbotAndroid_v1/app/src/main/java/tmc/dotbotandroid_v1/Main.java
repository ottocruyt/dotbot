package tmc.dotbotandroid_v1;
//test
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
import android.view.WindowManager;

public class Main extends AppCompatActivity implements SensorEventListener {

    // Declaration sensor variables
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;

    // Initialize variable to check when sensor inputs have to be updated
    private long lastUpdate = 0;

    // Flags that give information whether a button is pressed or not
    private boolean startButtonPressed;
    private boolean connectButtonPressed;

    @Override
    // This method is called when the app is started.
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // Functionality that keeps the screen on while being in the app

        // Creation of sensor variables: a listener is created to keep track when the sensor values change
        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        // Initialization of button flags
        startButtonPressed = false;
        connectButtonPressed = false;
    }

    @Override
    // This method is called every time the sensor values change
    public void onSensorChanged(SensorEvent sensorEvent) {
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
                int result[] = calculateArduinoInputs(y,z);

                // The new values need to be displayed in the GUI
                refreshGUI(result[0], result[1], result [2], result [3]);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    // Calculates the Arduino inputs based on the accelerometer sensor values
    public int[] calculateArduinoInputs(float y, float z) {

        int power = Math.round(z * 10);
        int steering = Math.round(y * 5);
        int motorLeft = power - steering;
        int motorRight = power + steering;

        // Limitation to minimum and maximum values for servo motors
        if (motorLeft < -100){motorLeft = -100;}
        if (motorLeft > 100){motorLeft = 100;}
        if (motorRight < -100){motorRight = -100;}
        if (motorRight > 100){motorRight = 100;}

        // Deadband to keep the dotbot steady when the value is close to zero
        if (Math.abs(motorLeft) < 10) {motorLeft = 0;}
        if (Math.abs(motorRight) < 10) {motorRight = 0;}

        // Stop button functionality: the values are zero when the start button is not pressed
        if (!startButtonPressed){
            motorRight = 0;
            motorLeft = 0;
        }

        return new int[]{motorLeft, motorRight, power, steering};
    }

    // Refreshes the values that are displayed on the GUI screen
    public void refreshGUI(float motorLeft, float motorRight, float power, float steering) {
        TextView text = (TextView)findViewById(R.id.number_1);

        // Format so decimals are not shown
        DecimalFormat nd = new DecimalFormat("###");
        text.setText("Motor Left: " + nd.format(motorLeft));
        text = (TextView)findViewById(R.id.number_2);
        text.setText("Motor Right: " + nd.format(motorRight));
        text = (TextView)findViewById(R.id.number_3);
        text.setText(nd.format(power));
        text = (TextView)findViewById(R.id.number_4);
        text.setText(nd.format(steering));
    }

    // This function is called when the start button is clicked
    public void startButtonClick(View view) {

        // Button object initialization
        final Button startButton = (Button) findViewById(R.id.button);

        // Change of appearance when the start button is pressed
        if (startButtonPressed){
            startButton.setText("Start");
            startButton.setTextColor(ContextCompat.getColor(this, R.color.startColor));
            startButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_start, 0, 0, 0);
            startButtonPressed = false;
        }
        else{
            startButton.setText("Stop");
            startButton.setTextColor(ContextCompat.getColor(this, R.color.stopColor));
            startButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_stop, 0, 0, 0);

            startButtonPressed = true;
        }

    }
    // This function is called when the connect button is clicked
    public void connectButtonClick(View view) {

        // Button object initialization
        final Button connectButton = (Button) findViewById(R.id.button2);

        // Change of appearance when the connect button is pressed
        if (connectButtonPressed){
            connectButton.setText("Connect");
            connectButtonPressed = false;
        }
        else{
            connectButton.setText("Disconnect");
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
