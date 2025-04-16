package com.nauh.threaddemo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Bai2 extends AppCompatActivity {

    private ProgressBar progressBar;
    private Button startButton;
    private TextView tvPercentage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bt2);

        progressBar = findViewById(R.id.progressBar);
        startButton = findViewById(R.id.startButton);
        tvPercentage = findViewById(R.id.tvPercentage);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LoadingTask().execute();
            }
        });
    }

    private class LoadingTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            startButton.setEnabled(false);
            tvPercentage.setText("0%");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            for (int i = 0; i <= 100; i += 5) {
                try {
                    Thread.sleep(200);
                    publishProgress(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            int progress = values[0];
            progressBar.setProgress(progress);
            tvPercentage.setText(progress + "%");
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressBar.setVisibility(View.GONE);
            startButton.setEnabled(true);
            tvPercentage.setText("Hoàn tất!");
        }
    }
}
