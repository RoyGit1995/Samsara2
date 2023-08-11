package com.example.samsara;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class CreatorRecipientChoice extends AppCompatActivity {

    private Button creatorButton;
    private Button recipientButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creator_recipient);

        creatorButton = findViewById(R.id.creatorChoice);
        creatorButton.setOnClickListener(v -> {
            Intent intent = new Intent(CreatorRecipientChoice.this, SampleProfile.class);
            startActivity(intent);
        });

        recipientButton = findViewById(R.id.recipientChoice);
        recipientButton.setOnClickListener(v -> {
            Intent intent = new Intent(CreatorRecipientChoice.this, UniqueIdCapture.class);
            startActivity(intent);
        });

    }
}
