package com.nauh.threaddemo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Bai1 extends AppCompatActivity {

    private EditText editTextTimer1;
    private EditText editTextTimer2;
    private Button buttonStart;
    private Button buttonOpenGallery;
    private TextView textViewTimer1;
    private TextView textViewTimer2;

    private int timer1Value = 0;
    private int timer2Value = 0;

    private boolean timer1Finished = false;
    private boolean timer2Finished = false;
    private boolean timersRunning = false;

    private Handler handler1;
    private Handler handler2;

    private Runnable timer1Runnable;
    private Runnable timer2Runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize UI elements
        editTextTimer1 = findViewById(R.id.editTextTimer1);
        editTextTimer2 = findViewById(R.id.editTextTimer2);
        buttonStart = findViewById(R.id.buttonStart);
        buttonOpenGallery = findViewById(R.id.buttonOpenGallery);
        textViewTimer1 = findViewById(R.id.textViewTimer1);
        textViewTimer2 = findViewById(R.id.textViewTimer2);

        // Initialize handlers
        handler1 = new Handler(Looper.getMainLooper());
        handler2 = new Handler(Looper.getMainLooper());

        // Define timer1 runnable
        timer1Runnable = new Runnable() {
            @Override
            public void run() {
                if (timer1Value > 0) {
                    timer1Value--;
                    textViewTimer1.setText(String.valueOf(timer1Value));
                    handler1.postDelayed(this, 1000); // Run again after 1 second
                } else {
                    timer1Finished = true;
                    Toast.makeText(Bai1.this, "Timer 1 finished!", Toast.LENGTH_SHORT).show();
                    checkBothTimersFinished();
                }
            }
        };

        // Define timer2 runnable
        timer2Runnable = new Runnable() {
            @Override
            public void run() {
                if (timer2Value > 0) {
                    timer2Value--;
                    textViewTimer2.setText(String.valueOf(timer2Value));
                    handler2.postDelayed(this, 1000); // Run again after 1 second
                } else {
                    timer2Finished = true;
                    Toast.makeText(Bai1.this, "Timer 2 finished!", Toast.LENGTH_SHORT).show();
                    checkBothTimersFinished();
                }
            }
        };

        // Set up start button click listener
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!timersRunning) {
                    startTimersWithUserInput();
                } else {
                    Toast.makeText(Bai1.this, "Timers are already running", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set up gallery button click listener
        buttonOpenGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Bai1.this, Bai3.class);
                startActivity(intent);
            }
        });
    }

    private void startTimersWithUserInput() {
        // Get values from input fields
        String timer1Input = editTextTimer1.getText().toString().trim();
        String timer2Input = editTextTimer2.getText().toString().trim();

        // Validate input
        if (timer1Input.isEmpty() || timer2Input.isEmpty()) {
            Toast.makeText(this, "Please enter values for both timers", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Parse input values
            timer1Value = Integer.parseInt(timer1Input);
            timer2Value = Integer.parseInt(timer2Input);

            // Validate that values are positive
            if (timer1Value <= 0 || timer2Value <= 0) {
                Toast.makeText(this, "Please enter positive values for both timers", Toast.LENGTH_SHORT).show();
                return;
            }

            // Reset timer states
            timer1Finished = false;
            timer2Finished = false;
            timersRunning = true;

            // Update display
            textViewTimer1.setText(String.valueOf(timer1Value));
            textViewTimer2.setText(String.valueOf(timer2Value));

            // Start timers
            startTimers();

            // Disable input fields while timers are running
            editTextTimer1.setEnabled(false);
            editTextTimer2.setEnabled(false);
            buttonStart.setText("Running...");

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid numbers", Toast.LENGTH_SHORT).show();
        }
    }

    private void startTimers() {
        // Start timer 1
        handler1.post(timer1Runnable);

        // Start timer 2
        handler2.post(timer2Runnable);
    }

    private void checkBothTimersFinished() {
        if (timer1Finished && timer2Finished) {
            // Both timers have finished, navigate to celebration activity
            Intent intent = new Intent(Bai1.this, CelebrationActivity.class);
            startActivity(intent);
            finish(); // Optional: close this activity
        } else if (timer1Finished || timer2Finished) {
            // One timer has finished but the other is still running
            // We'll keep the app running but show a message
            String message = timer1Finished ? "Waiting for Timer 2 to finish..." : "Waiting for Timer 1 to finish...";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    private void resetUI() {
        // Re-enable input fields
        editTextTimer1.setEnabled(true);
        editTextTimer2.setEnabled(true);
        buttonStart.setText("Start Countdown");
        timersRunning = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove callbacks to prevent memory leaks
        handler1.removeCallbacks(timer1Runnable);
        handler2.removeCallbacks(timer2Runnable);
    }
}