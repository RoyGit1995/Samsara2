package com.example.samsara;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class UniqueIdCapture extends AppCompatActivity {

    private Button goButton;
    private Button skipButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.unique_id);

        goButton = findViewById(R.id.unique_go);
        goButton.setOnClickListener(v -> {
            Intent intent = new Intent(UniqueIdCapture.this, RecipientOptionsScreen.class);
            startActivity(intent);
        });

        skipButton = findViewById(R.id.unique_skip);
        skipButton.setOnClickListener(v -> {
            Intent intent = new Intent(UniqueIdCapture.this, RecipientOptionsScreen.class);
            startActivity(intent);
        });
    }

}
