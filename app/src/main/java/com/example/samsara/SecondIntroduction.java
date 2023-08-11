package com.example.samsara;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SecondIntroduction extends AppCompatActivity {

    private Button nextPage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_introduction);

        nextPage = (Button) findViewById(R.id.next_page);
        nextPage.setOnClickListener(v -> {
            Intent intent = new Intent(SecondIntroduction.this, CreatorRecipientChoice.class);
            startActivity(intent);
        });

    }
}
