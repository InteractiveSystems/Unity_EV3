package uk.co.dyadica.ev3api;

import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by dyadica.co.uk on 04/02/2016.
 * <p>
 * THIS CODE AND INFORMATION ARE PROVIDED "AS IS" WITHOUT WARRANTY OF ANY
 * KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR FITNESS FOR A
 * PARTICULAR PURPOSE.
 * <p/>
 */

public class BluetoothClient extends AsyncTask<Void, String, Void>
{
    private static final String TAG = "BluetoothClient";
    IDataReceived callback;

    private final BluetoothSocket btSocket;

    private final InputStream btInStream;
    private final OutputStream btOutStream;

    private Brick brick;

    public BluetoothClient(Brick brick, BluetoothSocket socket, IDataReceived listener)
    {
        this.brick = brick;

        Log.i(TAG, "Initialising Bluetooth Client!");

        // Set the final callback ref
        callback = listener;

        // Set the final socket ref
        btSocket = socket;

        // Set up temp input and output streams
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        // Get the input and output streams, using
        // temp objects because member streams are
        // final.

        try
        {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        }
        catch (IOException e)
        {
            Log.e(TAG, "Failed to create streams!");
        }

        btInStream = tmpIn;
        btOutStream = tmpOut;

        Log.i(TAG, "Bluetooth Client Initialised!");
    }

    // region AsyncTask

    public boolean writeData(byte[] data)
    {
        try
        {
            btOutStream.write(data);
            return true;
        }
        catch (IOException e)
        {
            return false;
        }
    }

    @Override
    protected Void doInBackground(Void... params)
    {
        loopReceiver();
        return null;
    }

    private void loopReceiver()
    {
        Log.i(TAG, "Initialising Bluetooth Receiver!");

        while (btSocket.isConnected())
        {
            // Log.i(TAG, "Looping Receiver!");

            try
            {
                int bytesAvailable = btInStream.available();

                if (bytesAvailable > 0) {

                    byte[] packetBytes = new byte[bytesAvailable];

                    btInStream.read(packetBytes);

                    byte[] data = new byte[2];
                    data[0] = packetBytes[0];
                    data[1] = packetBytes[1];

                    short msgLength = EndianConverter.swapToShort(data);

                    // Log.i(TAG, "StringID Length: " + msgLength);

                    byte[] byteData = new byte[msgLength - 2];

                    for(int i = 2; i<msgLength; i++)
                    {
                        byteData[i-2] = packetBytes[i];
                    }

                    callback.dataReceived(byteData);
                }
            }
            catch (Exception ex)
            {
                Log.e(TAG, "Failed to read data: " + ex.getMessage());
            }
        }
    }

    // endregion AsyncTask
}
