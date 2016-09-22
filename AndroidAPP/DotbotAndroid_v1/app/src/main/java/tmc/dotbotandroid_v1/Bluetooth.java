package tmc.dotbotandroid_v1;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Steven on 4/06/2016.
 */
public class Bluetooth {
    private Context mContext;
    BluetoothAdapter mBluetoothAdapter;
    private ArrayAdapter<String> mArrayAdapter;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private OutputStream mOutput = null; // Outputstream object to be able to send data to arduino
    private BluetoothSocket mSocket = null; // Socket object to be able to send data to arduino

    public enum returnCodes {
        BLUETOOTH_NOT_SUPPORTED,
        BLUETOOTH_NOT_ENABLED,
        BLUETOOTH_ENABLED,
        BLUETOOTH_SUCCEEDED,
        BLUETOOTH_CONNECTION_ERROR,
        BLUETOOTH_SEND_ERROR,
        BLUETOOTH_CLOSE_ERROR,
        BLUETOOTH_CREATION_ERROR,
        BLUETOOTH_OUTPUT_STREAM_ERROR
    }

    Bluetooth(Context context) {
        this.mContext = context;

        //Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothDevice.ACTION_UUID);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        mContext.registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
    }

    // This function starts the communication with a bluetooth device. This function assumes that
    // bluetooth has been enabled.
    // returns errorcode which describes what might have gone wrong
    public returnCodes start() {

        queryPairedDevices();
        discoverNewDevices();

        returnCodes retVal = createDevice();
        if (retVal != returnCodes.BLUETOOTH_SUCCEEDED){
            return retVal;
        }

        retVal = connect();
        if (retVal != returnCodes.BLUETOOTH_SUCCEEDED){
            return retVal;
        }

        return returnCodes.BLUETOOTH_SUCCEEDED;
    }

    // This function attempts to create and enable the bluetooth adapter.
    // returns errorcode which describes what might have gone wrong
    public returnCodes enable() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // Enable Bluetooth
        if (mBluetoothAdapter == null) { // Device does not support Bluetooth
            return returnCodes.BLUETOOTH_NOT_SUPPORTED;
        }
        if (mBluetoothAdapter.isEnabled()) {
            return returnCodes.BLUETOOTH_ENABLED;
        }
        else {
            return returnCodes.BLUETOOTH_NOT_ENABLED;
        }
    }

    // This function will attempt to send data to the bluetooth device. Calling this function requirs
    // initializing, calling init(), the bluetooth device.
    // returns errorcode which describes what might have gone wrong
    public returnCodes sendData(char motorLeft, char motorRight) {
        //ints can be 4 bytes, possible overflow
        byte[] msgBytes = new byte[2];
        msgBytes[0] = (byte)motorLeft;
        msgBytes[1] = (byte)motorRight;
        try {
            mOutput.write(msgBytes);
        } catch (IOException ex) {
            Log.e("Bluetooth", "Unable to send message" + ex.toString());
            return returnCodes.BLUETOOTH_SEND_ERROR;
        }
        return returnCodes.BLUETOOTH_SUCCEEDED;
    }

    public void destroy() {
        if (mBluetoothAdapter != null) {
            mBluetoothAdapter.cancelDiscovery();
        }
        mContext.unregisterReceiver(mReceiver);
    }

    private void queryPairedDevices(){
        mArrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1);
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) { // Loop through paired devices
                mArrayAdapter.add(device.getName() + "\n" + device.getAddress()); // Save the MAC address and name of the Bluetooth device in an ArrayAdapter
            }
        }
    }

    // This function will discover new devices
    // returns void
    private void discoverNewDevices(){
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        mContext.registerReceiver(mReceiver, filter);
        mBluetoothAdapter.startDiscovery(); // Starts the discovery process

        // Loop which makes sure the code will only continue once the discovery process is finished
        while (mBluetoothAdapter.isDiscovering()) {
        }
    }

    // This function will attempt to create a bluetooth device and setup the socket
    // returns errorcode which describes what might have gone wrong
    private returnCodes createDevice() {
        BluetoothDevice mDevice = mBluetoothAdapter.getRemoteDevice("98:D3:31:90:3E:D7");
        //todo: implement way to see a list of possible bluetooth devices and pick the correct one (arrayadapter?)
        try {
            mSocket = mDevice.createRfcommSocketToServiceRecord(myUUID); //create a RFCOMM (SPP) connection
        } catch (IOException ex) {
            Log.e("Bluetooth", "Unable to create device: " + ex.toString());
            ex.printStackTrace();
            return returnCodes.BLUETOOTH_CREATION_ERROR;
        }
        return returnCodes.BLUETOOTH_SUCCEEDED;
    }

    // This function will stop discovery and attempt to connect to a bluetooth device. If this works,
    // It will also setup a outputstream for writing data to the bluetooth device via the socket
    // returns errorcode which describes what might have gone wrong
    private returnCodes connect() {
        // Performing the connect function to make a connection
        // Make sure the discovery is cancelled
        mBluetoothAdapter.cancelDiscovery();
        //todo: Make the connect in a seperate thread (adviced by Android page) because it is a blocking call
        try {
            // Connect the device through the socket. This will block
            // until it succeeds or throws an exception
            mSocket.connect();
        } catch (IOException ex) {
            Log.e("Bluetooth", "Unable to connect: " + ex.toString());
            return returnCodes.BLUETOOTH_CONNECTION_ERROR;
            // Unable to connect due to faillure or timeout; close the socket and get out
        } finally {
            // finally is normally used to deal with cleanup, catch is used to deal with unexpected errors
            try {
                mSocket.close();
            } catch (IOException ex) {
                Log.e("Bluetooth", "Unable to close: " + ex.toString());
                return returnCodes.BLUETOOTH_CLOSE_ERROR;
            }
        }
        try {
            mOutput = mSocket.getOutputStream();
        } catch (IOException ex) {
            ex.printStackTrace();
            return returnCodes.BLUETOOTH_OUTPUT_STREAM_ERROR;
        }
        return returnCodes.BLUETOOTH_SUCCEEDED;
    }



    //Create a BroadcastReceiver for ACTION_FOUND and specify which task to perform, based on received message
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
