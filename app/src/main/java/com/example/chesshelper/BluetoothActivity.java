package com.example.chesshelper;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;
import java.util.UUID;

public class BluetoothActivity extends AppCompatActivity {

    TextToSpeech textToSpeech;
    String figureData;
    static final UUID ARDUINO_B_MODULE_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    static final String BLUETOOTH_MODULE_ADDRESS = "98:D3:51:F5:C3:38";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth2);
        Intent receivingIntent = getIntent();
        figureData = receivingIntent.getStringExtra("figureData");
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    textToSpeech.setLanguage(Locale.ENGLISH);
                    textToSpeechMethod("This is a " + figureData);
                }
            }
        });





        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothDevice arduinoBluetoothModule = bluetoothAdapter.getRemoteDevice(BLUETOOTH_MODULE_ADDRESS);
        BluetoothSocket bluetoothSocket = null;
        int numberOfTries = 1;
        do {
            try {
                bluetoothSocket = arduinoBluetoothModule.createRfcommSocketToServiceRecord(ARDUINO_B_MODULE_UUID);
                bluetoothSocket.connect();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
            numberOfTries++;
        } while (!bluetoothSocket.isConnected() && numberOfTries <= 5);

        try {
            OutputStream outputStreamTowardsBluetoothModule = bluetoothSocket.getOutputStream();
            switch (figureData) {
                case "pawn":
                    outputStreamTowardsBluetoothModule.write(49);
                    break;
                case "bishop":
                    outputStreamTowardsBluetoothModule.write(50);
                    break;
                case "king":
                    outputStreamTowardsBluetoothModule.write(51);
                    break;
                case "rook":
                    outputStreamTowardsBluetoothModule.write(52);
                    break;
                case "knight":
                    outputStreamTowardsBluetoothModule.write(53);
                    break;
                case "queen":
                    outputStreamTowardsBluetoothModule.write(54);
                    break;
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }


        try {
            InputStream inputStreamFromBluetoothModule = bluetoothSocket.getInputStream();
            inputStreamFromBluetoothModule.skip(inputStreamFromBluetoothModule.available());
            char b = (char) inputStreamFromBluetoothModule.read();
            System.out.println(b);
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        try {
            bluetoothSocket.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }


    }

    private void textToSpeechMethod (String textToBeSpoken){
        textToSpeech.speak(textToBeSpoken, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public void onPause() {
        super.onPause();
        if(textToSpeech != null)
        {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }
}
