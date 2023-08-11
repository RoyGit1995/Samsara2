package com.example.samsara;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class IntroductionActivity extends AppCompatActivity {

    private Button nextPage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);

        nextPage = (Button) findViewById(R.id.next_page);
        nextPage.setOnClickListener(v -> {
            Intent intent = new Intent(IntroductionActivity.this, SecondIntroduction.class);
            startActivity(intent);
        });

    }
}
