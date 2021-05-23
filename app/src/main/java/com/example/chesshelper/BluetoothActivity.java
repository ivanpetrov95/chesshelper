package com.example.chesshelper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;

import java.util.Locale;

public class BluetoothActivity extends AppCompatActivity {

    TextToSpeech textToSpeech;
    Button textToSpeechButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth2);
        textToSpeechButton = findViewById(R.id.textToSpeechButton);

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR)
                {
                    textToSpeech.setLanguage(Locale.ENGLISH);
                }
            }
        });

        textToSpeechButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textToBeSpoken = "Say this sample.";
                textToSpeech.speak(textToBeSpoken, TextToSpeech.QUEUE_FLUSH, null);
            }
        });

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