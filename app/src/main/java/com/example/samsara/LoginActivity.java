package com.example.samsara;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    private EditText username;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = (Button) findViewById(R.id.next_page);
        username = (EditText) findViewById(R.id.et_username);

        loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, IntroductionActivity.class);
            startActivity(intent);
        });

    }
}