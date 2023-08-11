package com.example.samsara;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;


public class CreateYourProfile extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView profileImage;
    private ImageView addContentImage;

    private ActivityResultLauncher<String> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createprofile);


        profileImage = findViewById(R.id.profileImage);
        addContentImage = findViewById(R.id.addContentImage);


        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                result -> {
                    if (result != null) {
                        // Handle the selected image
                        profileImage.setImageURI(result);
                    }
                }
        );

        profileImage.setOnClickListener(v -> {
            // Open the gallery to select an image
            imagePickerLauncher.launch("image/*");
        });

        addContentImage.setOnClickListener(v -> {
            Intent intent = new Intent(CreateYourProfile.this, ContentCreate.class);
            startActivity(intent);
        });
    }

}