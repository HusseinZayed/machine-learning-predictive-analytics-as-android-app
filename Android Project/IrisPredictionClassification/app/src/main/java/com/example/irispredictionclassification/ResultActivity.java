package com.example.irispredictionclassification;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    TextView tv_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_result);

        tv_result = findViewById(R.id.Result_TV);

        Bundle bundle = getIntent().getExtras();
        float output = bundle.getFloat("Prediction");
        Log.d("Prediction", "Prediction Is : " + output);

        Toast.makeText(this, "The Prediction Is : " + output, Toast.LENGTH_LONG).show();

        if (output <= 1.4761) {
            tv_result.setText("iris setosa".toUpperCase());
        } else if (output <= 2.05 && output > 1.10276) {
            tv_result.setText("iris versicolor".toUpperCase());
        } else {
            tv_result.setText("iris virginica".toUpperCase());
        }

    }

    public void StartNewPrediction(View view) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}