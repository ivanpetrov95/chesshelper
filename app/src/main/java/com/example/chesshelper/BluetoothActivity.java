package com.example.chesshelper;

import androidx.appcompat.app.AppCompatActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;
import java.util.UUID;

public class BluetoothActivity extends AppCompatActivity {

    TextToSpeech textToSpeech;
    String figureData;
    TextView figureTextView;
    ImageView figureImageView;
    static final UUID ARDUINO_B_MODULE_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    static final String BLUETOOTH_MODULE_ADDRESS = "98:D3:51:F5:C3:38";
    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private ConnectThread mConnectThread;
    boolean isBluetoothConnected;
    private ConnectedThread mConnectedThread;
    private String figureInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth2);
        Intent receivingIntent = getIntent();
        figureData = receivingIntent.getStringExtra("figureData");
        StorerClass storerClass = new StorerClass();
        figureInformation = storerClass.retrieveFigureMovementText(figureData);
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    textToSpeech.setLanguage(Locale.ENGLISH);
                    textToSpeechMethod(figureInformation);
                }
            }
        });
        figureTextView = findViewById(R.id.figureTextViewId);
        figureImageView = findViewById(R.id.figureImageViewId);
        figureTextView.setText(figureData);
        figureImageView.setImageResource(R.drawable.pawn);

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothDevice arduinoBluetoothModule = bluetoothAdapter.getRemoteDevice(BLUETOOTH_MODULE_ADDRESS);
        BluetoothSocket bluetoothSocket = null;
        connect(arduinoBluetoothModule);
    }

    private void connect(BluetoothDevice arduinoBluetoothModule) {
        mConnectThread = new ConnectThread(arduinoBluetoothModule);
        mConnectThread.start();
    }


    private void textToSpeechMethod(String textToBeSpoken) {
        textToSpeech.speak(textToBeSpoken, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }


    private class ConnectThread extends Thread {
        private BluetoothSocket mmSocket;
        private BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice arduinoBluetoothModule) {
            mmDevice = arduinoBluetoothModule;
            BluetoothSocket temp = null;
            try {
                temp = arduinoBluetoothModule.createInsecureRfcommSocketToServiceRecord(ARDUINO_B_MODULE_UUID);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
            mmSocket = temp;
        }

        @Override
        public void run() {
            bluetoothAdapter.cancelDiscovery();

            try {
                isBluetoothConnected = true;
                mmSocket.connect();

            } catch (IOException exception) {
                isBluetoothConnected = false;
                try {
                    mmSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                exception.printStackTrace();
            }
            mConnectedThread = new ConnectedThread(mmSocket);
            mConnectedThread.start();
            try {
                mConnectedThread.write(figureData);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }

    }

    private class ConnectedThread extends Thread {
        private BluetoothSocket mmSocket;
        private InputStream mmInStream;
        private OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tempIn = null;
            OutputStream tempOut = null;
            try {
                tempIn = socket.getInputStream();
                tempOut = socket.getOutputStream();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
            mmInStream = tempIn;
            mmOutStream = tempOut;
        }

        @Override
        public void run() {
            char info;
            while (true) {
                synchronized (this) {
                    try {
                        info = (char) mmInStream.read();
                        System.out.println(info);
                        //cancel();
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                }
            }
        }

        public void write(String figureData) throws IOException {
            System.out.println("I got here");
            switch (figureData) {
                case "pawn":
                    mmOutStream.write(49);
                    break;
                case "bishop":
                    mmOutStream.write(50);
                    break;
                case "king":
                    mmOutStream.write(51);
                    break;
                case "rook":
                    mmOutStream.write(52);
                    break;
                case "knight":
                    mmOutStream.write(53);
                    break;
                case "queen":
                    mmOutStream.write(54);
                    break;
            }
        }
        //novo
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }
}
//    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//    BluetoothDevice arduinoBluetoothModule = bluetoothAdapter.getRemoteDevice(BLUETOOTH_MODULE_ADDRESS);
//    BluetoothSocket bluetoothSocket = null;
//    int numberOfTries = 1;
//            do {
//                    try {
//                    bluetoothSocket = arduinoBluetoothModule.createRfcommSocketToServiceRecord(ARDUINO_B_MODULE_UUID);
//                    bluetoothSocket.connect();
//                    } catch (IOException exception) {
//                    exception.printStackTrace();
//                    }
//                    numberOfTries++;
//                    } while (!bluetoothSocket.isConnected() && numberOfTries <= 5);
//
//                    try {
//                    OutputStream outputStreamTowardsBluetoothModule = bluetoothSocket.getOutputStream();
//                    switch (figureData) {
//                    case "pawn":
//                    outputStreamTowardsBluetoothModule.write(49);
//                    break;
//                    case "bishop":
//                    outputStreamTowardsBluetoothModule.write(50);
//                    break;
//                    case "king":
//                    outputStreamTowardsBluetoothModule.write(51);
//                    break;
//                    case "rook":
//                    outputStreamTowardsBluetoothModule.write(52);
//                    break;
//                    case "knight":
//                    outputStreamTowardsBluetoothModule.write(53);
//                    break;
//                    case "queen":
//                    outputStreamTowardsBluetoothModule.write(54);
//                    break;
//                    }
//                    } catch (IOException exception) {
//                    exception.printStackTrace();
//                    }
//
//
//                    try {
//                    InputStream inputStreamFromBluetoothModule = bluetoothSocket.getInputStream();
//                    inputStreamFromBluetoothModule.skip(inputStreamFromBluetoothModule.available());
//                    char b = (char) inputStreamFromBluetoothModule.read();
//                    System.out.println(b);
//                    } catch (IOException exception) {
//                    exception.printStackTrace();
//                    }
//
//                    try {
//                    bluetoothSocket.close();
//                    } catch (IOException exception) {
//                    exception.printStackTrace();
//                    }