package com.example.gyroapplication;

import android.bluetooth.BluetoothSocket;
import android.os.SystemClock;
import android.util.Log;
import android.widget.TextView;
import android.os.Handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

class ConnectedThread extends Thread {
    private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;
    private boolean ThreadFlag = true;

    private String result = "";

    TextView contextText;

    Handler handler = new Handler();

    public ConnectedThread(BluetoothSocket socket, TextView contexttext) {
        mmSocket = socket;
        contextText = contexttext;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        // Get the input and output streams, using temp objects because
        // member streams are final
        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {
        }

        mmInStream = tmpIn;
        mmOutStream = tmpOut;
    }

    public String getResult() {
        return result;
    }

    @Override
    public void run() {

        byte[] buffer = new byte[1024];  // buffer store for the stream
        int bytes; // bytes returned from read()
        // Keep listening to the InputStream until an exception occurs

        while (ThreadFlag) {
            try {
                // Read from the InputStream
                bytes = mmInStream.available();
                if (bytes != 0) {
                    buffer = new byte[1024];
                    SystemClock.sleep(10); //pause and wait for rest of data. Adjust this depending on your sending speed.
                    bytes = mmInStream.available(); // how many bytes are ready to be read?
                    bytes = mmInStream.read(buffer, 0, bytes); // record how many bytes we actually read


                    byte[] slice = Arrays.copyOfRange(buffer, 0, bytes);

                    result = new String(slice);

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            String Text = "Angle : " + result.split(",")[0] + "\n" +
                                          "Avg : " + result.split(",")[1] + "\n" +
                                          "Status : " + result.split(",")[2];
                            contextText.setText(Text);
                        }
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();

                break;
            }
        }
    }

    /* Call this from the main activity to send data to the remote device */
    public void write(byte input) {
        byte bytes[] = {input, '\n'};        //converts entered String into bytes
        try {
            mmOutStream.write(bytes);
            Log.e("success","success write");
        } catch (IOException e) {
        }
    }

    /* Call this from the main activity to shutdown the connection */
    public void cancel() {
        try {
            Log.e("Test","Socket close success");
            mmInStream.close();
            mmOutStream.close();
            mmSocket.close();
            ThreadFlag = false;
            this.interrupt();
        } catch (IOException e) {
        }
    }

    public boolean isOpen(){
        return mmSocket.isConnected();
    }
}