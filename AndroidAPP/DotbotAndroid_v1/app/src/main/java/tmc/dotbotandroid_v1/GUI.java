package tmc.dotbotandroid_v1;


import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.text.DecimalFormat;
import android.view.WindowManager;
import android.widget.Toast;

public class GUI extends AppCompatActivity {

    // Static variables
 //   private final static int REQUEST_ENABLE_BT = 1;    // Declaration sensor variables
    Controller mController;
    Handler mHandler;
    private final static int REQUEST_ENABLE_BT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mController = new Controller(this);
        mHandler = new Handler();

        setContentView(R.layout.activity_gui);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // Functionality that keeps the screen on while being in the app
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
          /* do what you need to do */
                refreshGUI(mController.mCache.motorLeft, mController.mCache.motorRight, mController.mCache.power, mController.mCache.steering);
          /* and here comes the "trick" */
                mHandler.postDelayed(this, 100);
            }
        };
        mHandler.post(runnable);

    }

    public void refreshGUI(float motorLeft, float motorRight, float power, float steering) { //gui
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mController.mBluetooth.destroy();
    }

    // This function is called when the start button is clicked
    public void startButtonClick(View view) {

        // Button object initialization
        Button startButton = (Button) findViewById(R.id.button);

        // Change of appearance when the start button is pressed
        if (mController.mCache.startButtonPressed){
            startButton.setText("Start");
            startButton.setTextColor(ContextCompat.getColor(this, R.color.startColor));
            startButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_start, 0, 0, 0);
            mController.mCache.startButtonPressed = false;
        }
        else{
            startButton.setText("Stop");
            startButton.setTextColor(ContextCompat.getColor(this, R.color.stopColor));
            startButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_stop, 0, 0, 0);
            mController.mCache.startButtonPressed = true;
        }
    }
    // This function is called when the connect button is clicked
   public void connectButtonClick(View view) {

        // Button object initialization
        Button connectButton = (Button) findViewById(R.id.button2);
        Bluetooth.returnCodes retVal;
        CharSequence toastReturnCodeBluetoothConnection="No return code! (Connection)";

        // Change of appearance when the connect button is pressed
        if (mController.mCache.connectButtonPressed){


            retVal = mController.disableBluetooth();

            switch(retVal) {
                case BLUETOOTH_CLOSED:
                    mController.mCache.bluetoothConnected = false;
                    connectButton.setText("Connect");
                    mController.mCache.connectButtonPressed = false;
                    toastReturnCodeBluetoothConnection = "Bluetooth Closed!";
                    break;
                case BLUETOOTH_CLOSE_ERROR:
                    toastReturnCodeBluetoothConnection = "Bluetooth Closing Error!";
                    break;
                default:
                    // exit , should not come here
                    break;
            }

        }
        else{

            //CharSequence toastReturnCodeBluetoothEnable="No return code! (Enable)";
            Bluetooth.returnCodes retVal_enable = mController.enableBluetooth();

            switch(retVal_enable) {
                case BLUETOOTH_ENABLED:
                    //toastReturnCodeBluetoothEnable="Bluetooth Enabled!";
                    break;
                case BLUETOOTH_NOT_ENABLED:
                    // ask user to enable bluetooth
                    Intent enableBtIntent = new Intent(mController.mBluetooth.mBluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                    //toastReturnCodeBluetoothEnable="Bluetooth was NOT Enabled! Bluetooth is enabled automatically";
                    break;
                case BLUETOOTH_NOT_SUPPORTED:
                    //toastReturnCodeBluetoothEnable="Bluetooth NOT supported!";
                    break;
                default:
                    // exit , should not come here
                    break;
            }
            //Toast toast_enable = Toast.makeText(this, toastReturnCodeBluetoothEnable, Toast.LENGTH_SHORT);
            //toast_enable.show();

            retVal = mController.startBluetooth();

            switch(retVal) {
                case BLUETOOTH_SUCCEEDED:
                    toastReturnCodeBluetoothConnection="Bluetooth Succeeded!";
                    mController.mCache.bluetoothConnected = true;
                    break;
                case BLUETOOTH_CREATION_ERROR:
                    // ask user to enable bluetooth
                    Intent enableBtIntent = new Intent(mController.mBluetooth.mBluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                    toastReturnCodeBluetoothConnection="Bluetooth Creation Error, enabling Bluetooth!";
                    break;
                case BLUETOOTH_CONNECTION_ERROR:
                    toastReturnCodeBluetoothConnection="Bluetooth Connection Error!";
                    break;
                case BLUETOOTH_CLOSE_ERROR:
                    toastReturnCodeBluetoothConnection="Bluetooth Close Error!";
                    break;
                case BLUETOOTH_OUTPUT_STREAM_ERROR:
                    toastReturnCodeBluetoothConnection="Bluetooth Output Stream Error!";
                    break;
                default:
                    // exit , should not come here
                    break;
            }




        }
       Toast toast = Toast.makeText(this, toastReturnCodeBluetoothConnection, Toast.LENGTH_SHORT);
       toast.show();

       if (retVal == Bluetooth.returnCodes.BLUETOOTH_SUCCEEDED || retVal == Bluetooth.returnCodes.BLUETOOTH_CLOSE_ERROR) {
           connectButton.setText("Disconnect");
           mController.mCache.connectButtonPressed = true;
       } else {
           connectButton.setText("Connect");
           mController.mCache.connectButtonPressed = false;
       }
    }
}