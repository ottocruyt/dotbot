package tmc.dotbotandroid_v1;
//test
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.Set;
import java.util.UUID;

import android.view.WindowManager;

public class Main extends AppCompatActivity implements SensorEventListener {

    // Static variables
    private final static int REQUEST_ENABLE_BT = 1;

    // Declaration sensor variables
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;

    // Declaration bluetooth variables
    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private ArrayAdapter<String> mArrayAdapter;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private boolean bluetoothIsConnected; // flag to determine if the bluetooth connection is made, so the sensordata can be send.
    private OutputStream mOutput = null;
    private BluetoothSocket mSocket = null;



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

        // Initialization flags
        startButtonPressed = false;
        connectButtonPressed = false;
        bluetoothIsConnected = false;

        //Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothDevice.ACTION_UUID);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
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

                if (bluetoothIsConnected){
                    sendData(result[0], result[1], result [2], result [3]);
                }
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

            closeBluetooth();
        }
        else{
            connectButton.setText("Disconnect");
            connectButtonPressed = true;

            startBluetooth();
        }
    }

    public void startBluetooth() {
        // Enable Bluetooth
        if (mBluetoothAdapter == null) { // Device does not support Bluetooth
        }
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        // Query paired devices
        mArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) { // Loop through paired devices
                mArrayAdapter.add(device.getName() + "\n" + device.getAddress()); // Save the MAC address and name of the Bluetooth device in an ArrayAdapter
            }
        }

        // Discover new devices
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        registerReceiver(mReceiver, filter);
        mBluetoothAdapter.startDiscovery(); // Starts the discovery process

        // Loop which makes sure the code will only continue once the discovery process is finished
        while (mBluetoothAdapter.isDiscovering()) {

        }

        // Creation of Bluetooth device
        BluetoothDevice mDevice = mBluetoothAdapter.getRemoteDevice("98:D3:31:90:3E:D7");
        try {
            mSocket = mDevice.createRfcommSocketToServiceRecord(myUUID); //create a RFCOMM (SPP) connection
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Performing the connect function to make a connection
        // Make sure the discovery is cancelled
        mBluetoothAdapter.cancelDiscovery();
        //todo: Make the connect in a seperate thread (adviced by Android page) because it is a blocking call
        try {
            // Connect the device through the socket. This will block
            // until it succeeds or throws an exception
            mSocket.connect();
        } catch (IOException connectException) {
            // Unable to connect due to faillure or timeout; close the socket and get out
            try {
                mSocket.close();
            } catch (IOException closeException) {
            }
            return;
        }
        //The work to manage the connection has to be done in a seperate thread
        //Read and write functions are blocking calls
        //todo: create seperate thread to manage connection
        //todo: connection does not always work from the first time. Figure out why

        bluetoothIsConnected = true;

        try {
            mOutput = mSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void closeBluetooth() {
        try {
            mSocket.close();
        } catch (IOException e) { }
        bluetoothIsConnected = false;

    }

    public void sendData(float motorLeft, float motorRight, float power, float steering) {

        String message = "woop";
        byte[] msgBytes = message.getBytes();
        try {
            mOutput.write(msgBytes);
        } catch (IOException e) { }
    }
    //Code for closing the connection
        /*
        try {
            mSocket.close();
        } catch (IOException e) { }
*/
            /*ListView deviceListView = (ListView) findViewById(R.id.lvItems); // listview is used to make user select correct BT device
            deviceListView.setAdapter(mArrayAdapter);
            :todo make listview display the list of arrayadapter
            :todo make listview pop up in seperate window
            :todo be able to click item in listview and store in device (needs onclicklistener?)
        }
*/


    protected void onPause() {
        super.onPause();
        senSensorManager.unregisterListener(this);
    }

    protected void onResume() {
        super.onResume();
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBluetoothAdapter != null) {
            mBluetoothAdapter.cancelDiscovery();
        }
        unregisterReceiver(mReceiver);
    }
    // Create a BroadcastReceiver for ACTION_FOUND and specify which task to perform, based on received message
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                //discovery starts, we can show progress dialog or perform other tasks
            }
            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                //discovery finishes, dismiss progress dialog
                mBluetoothAdapter.cancelDiscovery();
            }
            else if (BluetoothDevice.ACTION_FOUND.equals(action)) { // When discovery finds a device
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE); // Get the BluetoothDevice object from the Intent
                mArrayAdapter.add(device.getName() + "\n" + device.getAddress()); // Add the name and address to an array adapter to show in a ListView
            }
        }
    };
}
