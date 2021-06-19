package com.example.irispredictionclassification;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MainActivity extends AppCompatActivity {

    TextInputEditText SepalLength, SepalWidth, PetalLength, PetalWidth;

    Interpreter interpreter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        SepalLength = findViewById(R.id.SepalLength);
        SepalWidth = findViewById(R.id.SepalWidth);
        PetalLength = findViewById(R.id.PetalLength);
        PetalWidth = findViewById(R.id.SepalWidth);
        try {
            interpreter = new Interpreter(LoadModelFile(), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void StartPrediction(View view) {

        if (SepalLength.getText().length() > 0 && SepalWidth.getText().length() > 0 &&
                PetalLength.getText().length() > 0 && PetalWidth.getText().length() > 0) {

            float sepalLength = Float.parseFloat(SepalLength.getText().toString());
            float sepalWidth = Float.parseFloat(SepalWidth.getText().toString());
            float petalLength = Float.parseFloat(PetalLength.getText().toString());
            float petalWidth = Float.parseFloat(PetalWidth.getText().toString());


            float[] input = {sepalLength, sepalWidth, petalLength, petalWidth};
            float output = doInference(input);
            Intent i = new Intent(this, ResultActivity.class);
            Bundle b = new Bundle();
            b.putFloat("Prediction", output);
            i.putExtras(b);
            startActivity(i);
            finish();
        } else {
            Toast.makeText(this, "Please Fill All Fields With Data ..", Toast.LENGTH_LONG).show();
        }
    }

    private MappedByteBuffer LoadModelFile() throws IOException {
        AssetFileDescriptor assetFileDescriptor = this.getAssets().openFd("linear.tflite");
        FileInputStream fileInputStream = new FileInputStream(assetFileDescriptor.getFileDescriptor());
        FileChannel fileChannel = fileInputStream.getChannel();
        long startOffset = assetFileDescriptor.getStartOffset();
        long length = assetFileDescriptor.getLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, length);
    }

    public float doInference(float[] input) {
        float output[][] = new float[1][1];
        interpreter.run(input, output);
        return output[0][0];
    }
}