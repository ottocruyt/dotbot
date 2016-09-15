package tmc.dotbotandroid_v1;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.ArrayAdapter;

import java.util.Set;
import java.util.UUID;

/**
 * Created by Steven on 4/06/2016.
 */
public class Bluetooth {
    private Context mContext;
    BluetoothAdapter mBluetoothAdapter;
    private ArrayAdapter<String> mArrayAdapter;
    private UUID myUUID;

    public enum returnCodes {
        BLUETOOTH_NOT_SUPPORTED,
        BLUETOOTH_NOT_ENABLED,
        BLUETOOTH_ENABLED
    }

    Bluetooth(Context context) {

        this.mContext = context;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

        //Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothDevice.ACTION_UUID);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        mContext.registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
    }

    public returnCodes isBluetoothEnabled() {
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


//    private void queryPairedDevices(){
//        mArrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1);
//        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
//        if (pairedDevices.size() > 0) {
//            for (BluetoothDevice device : pairedDevices) { // Loop through paired devices
//                mArrayAdapter.add(device.getName() + "\n" + device.getAddress()); // Save the MAC address and name of the Bluetooth device in an ArrayAdapter
//            }
//        }
//    }
//
//    private void discoverNewDevices(){
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(BluetoothDevice.ACTION_FOUND);
//        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
//        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
//
//        mContext.registerReceiver(mReceiver, filter);
//        mBluetoothAdapter.startDiscovery(); // Starts the discovery process
//
//        // Loop which makes sure the code will only continue once the discovery process is finished
//        while (mBluetoothAdapter.isDiscovering()) {
//        }
//    }
//
//    private void createDevice() {
//        BluetoothDevice mDevice = mBluetoothAdapter.getRemoteDevice("98:D3:31:90:3E:D7");
//        //todo: implement way to see a list of possible bluetooth devices and pick the correct one (arrayadapter?)
//        try {
//            mSocket = mDevice.createRfcommSocketToServiceRecord(myUUID); //create a RFCOMM (SPP) connection
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    public void start() {
//
//        // Performing the connect function to make a connection
//        // Make sure the discovery is cancelled
//        mBluetoothAdapter.cancelDiscovery();
//        //todo: Make the connect in a seperate thread (adviced by Android page) because it is a blocking call
//        try {
//            // Connect the device through the socket. This will block
//            // until it succeeds or throws an exception
//            mSocket.connect();
//        } catch (IOException connectException) {
//            // Unable to connect due to faillure or timeout; close the socket and get out
//            try {
//                mSocket.close();
//            } catch (IOException closeException) {
//            }
//            return;
//        }
//        //The work to manage the connection has to be done in a seperate thread
//        //Read and write functions are blocking calls
//        //todo: create seperate thread to manage connection
//
//        bluetoothIsConnected = true;
//
//        try {
//            mOutput = mSocket.getOutputStream();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }





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
