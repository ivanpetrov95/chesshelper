package com.example.chesshelper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.tensorflow.lite.Interpreter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class ImageIdentifierActivity extends AppCompatActivity {

    private static final int RESULTS_NUMBER = 3;
    private static final int IMAGE_MEAN = 128;
    private static final float IMAGE_STD = 128.0f;
    private final Interpreter.Options tensorflowLiteOptions = new Interpreter.Options();
    private Interpreter tensorflowLite;
    private List<String> labelList;
    private ByteBuffer imageData = null;
    private float[][] labelProbabilityArray = null;
    private String[] topLabels = null;
    private String[] topConfidence = null;
    private String model;
    private int IMG_SIZE_X = 640;
    private int IMG_SIZE_Y = 640;
    private int PIXEL_SIZE = 3;
    private int[] intValues;
    private ImageView cameraCapturedImage;
    private Button classifyButton;
    private Button backButton;
    private TextView labelOne;
    private TextView labelTwo;
    private TextView labelThree;
    private TextView confidenceOne;
    private TextView confidenceTwo;
    private TextView confidenceThree;

    private PriorityQueue<Map.Entry<String, Float>> sortedLabels =
            new PriorityQueue<>(
                    RESULTS_NUMBER,
                    new Comparator<Map.Entry<String, Float>>() {
                        @Override
                        public int compare(Map.Entry<String, Float> o1, Map.Entry<String, Float> o2) {
                            return (o1.getValue()).compareTo(o2.getValue());
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        model = (String)getIntent().getStringExtra("model");
        intValues = new int[IMG_SIZE_X * IMG_SIZE_Y];
        super.onCreate(savedInstanceState);
        try
        {
            tensorflowLite = new Interpreter(loadModelFile(), tensorflowLiteOptions);
            labelList = loadLabelList();
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
        imageData = ByteBuffer.allocateDirect(IMG_SIZE_X * IMG_SIZE_Y * PIXEL_SIZE);
        imageData.order(ByteOrder.nativeOrder());
        labelProbabilityArray = new float[1][labelList.size()];

        setContentView(R.layout.activity_image_identifier);

        labelOne = findViewById(R.id.labelOne);
        labelTwo = findViewById(R.id.labelTwo);
        labelThree = findViewById(R.id.labelThree);
        confidenceOne = findViewById(R.id.confidenceOne);
        confidenceTwo = findViewById(R.id.confidenceTwo);
        confidenceThree = findViewById(R.id.confidenceThree);
        cameraCapturedImage = findViewById(R.id.capturedImageView);
        topLabels = new String[RESULTS_NUMBER];
        topConfidence = new String[RESULTS_NUMBER];
        backButton = findViewById(R.id.backButton);
        classifyButton = findViewById(R.id.classifyButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ImageIdentifierActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        classifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmapOriginal = ((BitmapDrawable)cameraCapturedImage.getDrawable()).getBitmap();
                Bitmap bitmap = getResizedBitmap(bitmapOriginal, IMG_SIZE_X, IMG_SIZE_Y);
                convertBitmapToByteBuffer(bitmap);
                tensorflowLite.run(imageData, labelProbabilityArray);
                printTopLabels();
            }
        });

        Uri uri = getIntent().getParcelableExtra("resID_uri");
        try
        {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            cameraCapturedImage.setImageBitmap(bitmap);
            cameraCapturedImage.setRotation(cameraCapturedImage.getRotation() + 90);
        }
        catch (IOException exception)
        {
            exception.printStackTrace();
        }
    }

    private void printTopLabels()
    {
        for (int i = 0; i < labelList.size(); ++i)
        {
            sortedLabels.add(new AbstractMap.SimpleEntry<>(labelList.get(i), labelProbabilityArray[0][i]));
            if(sortedLabels.size() > RESULTS_NUMBER)
            {
                sortedLabels.poll();
            }
        }

        final int size = sortedLabels.size();
        for (int i = 0; i < size; ++i)
        {
            Map.Entry<String, Float> label = sortedLabels.poll();
            topLabels[i] = label.getKey();
            topConfidence[i] = String.format("%.0f%%", label.getValue()*100);
        }

        labelOne.setText("1. "+topLabels[2]);
        labelTwo.setText("2. "+topLabels[1]);
        labelThree.setText("3. "+topLabels[0]);
        confidenceOne.setText(topConfidence[2]);
        confidenceTwo.setText(topConfidence[1]);
        confidenceThree.setText(topConfidence[0]);
    }

    private void convertBitmapToByteBuffer(Bitmap bitmap)
    {
        if (imageData == null)
        {
            return;
        }
        imageData.rewind();
        bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        int pixel = 0;
        for (int i = 0; i < IMG_SIZE_X; ++i)
        {
            for (int j = 0; j < IMG_SIZE_Y; ++j)
            {
                final int val = intValues[pixel++];
                imageData.putFloat((((val >> 16) & 0xFF)-IMAGE_MEAN)/IMAGE_STD);
                imageData.putFloat((((val >> 8) & 0xFF)-IMAGE_MEAN)/IMAGE_STD);
                imageData.putFloat((((val) & 0xFF)-IMAGE_MEAN)/IMAGE_STD);
            }
        }
    }

    private Bitmap getResizedBitmap(Bitmap bitmapOriginal, int img_size_x, int img_size_y)
    {
        int width = bitmapOriginal.getWidth();
        int height = bitmapOriginal.getHeight();
        float scaleWidth = ((float) img_size_x)/width;
        float scaleHeight = ((float) img_size_y)/height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmapOriginal, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }

    private List<String> loadLabelList() throws IOException
    {
        List<String> labelList = new ArrayList<String>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(this.getAssets().open("labelMap.txt")));
        String line;
        while ((line = reader.readLine()) != null)
        {
            labelList.add(line);
        }
        reader.close();
        return labelList;
    }

    private ByteBuffer loadModelFile() throws IOException
    {
        AssetFileDescriptor assetFileDescriptor = this.getAssets().openFd(model);
        FileInputStream fileInputStream = new FileInputStream(assetFileDescriptor.getFileDescriptor());
        FileChannel fileChannel = fileInputStream.getChannel();
        long startOffset = assetFileDescriptor.getStartOffset();
        long declaredLength = assetFileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }
}