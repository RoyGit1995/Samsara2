package com.example.samsara;

import static android.Manifest.permission.RECORD_AUDIO;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;


public class ContentCreate extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 100;

    private ImageView mediaImageView;
    private Button recordButton ;
    private Button playButton;
    private ToggleButton publicToggleButton;
    private ToggleButton recipientsToggleButton;
    private Button speicificRecipientButton;
    private Button setRecipientsButton;
    private Button guidePromptsButton;

    private Button createAnotherButton;

    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private boolean isRecording = false;
    private boolean isPlaying = false;

    private ActivityResultLauncher<String> getContentLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_content);

        publicToggleButton = findViewById(R.id.publicToggle);
        recipientsToggleButton = findViewById(R.id.recipientsToggle);



        //Add video/picture
        mediaImageView = findViewById(R.id.imageVideoImageView);
        getContentLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            if (result != null) {
                mediaImageView.setImageURI(result);
                mediaImageView.setOnClickListener(view -> {
                    Uri mediaUri = result;
                    String mimeType = getContentResolver().getType(mediaUri);

                    if (mimeType != null && mimeType.startsWith("image")) {

                    } else if (mimeType != null && mimeType.startsWith("video")) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, mediaUri);
                        intent.setDataAndType(mediaUri, "video/*");
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, "Unsupported media type", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        mediaImageView.setOnClickListener(view -> {
            getContentLauncher.launch("image/* video/*");
        });




        //Record and play
        recordButton  = findViewById(R.id.recordAudioButton);
        playButton = findViewById(R.id.playRecordingButton);
        playButton.setEnabled(false);

        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isRecording) {
                    if (checkSelfPermission(RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                        startRecording();
                        recordButton.setText("Stop Recording");
                        recordButton.setTextColor(Color.RED);
                    } else {
                        requestPermissions(new String[]{RECORD_AUDIO}, PERMISSION_REQUEST_CODE);
                    }
                } else {
                    stopRecording();
                    recordButton.setText("Record");
                    recordButton.setTextColor(Color.WHITE);
                    playButton.setEnabled(true);

                }
                isRecording = !isRecording;
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isPlaying) {
                    startPlaying();
                    playButton.setText("Stop");
                } else {
                    stopPlaying();
                    playButton.setText("Play");
                }
                isPlaying = !isPlaying;
            }
        });


        //specific recipients button
        speicificRecipientButton = findViewById(R.id.speicificRecipientButton);
        speicificRecipientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToggleButtonPopup();
            }
        });


        //guide prompts
        guidePromptsButton = findViewById(R.id.guidePromptsButtonpopup);
        guidePromptsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialogFragment dialogFragment = new CustomDialogFragment();
                dialogFragment.show(getSupportFragmentManager(), "custom_dialog");
            }
        });


        //recipients prompts
        setRecipientsButton = findViewById(R.id.setRecipientsButton);
        setRecipientsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomPopup();
            }
        });

        createAnotherButton = findViewById(R.id.createanothercontent);
        createAnotherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContentCreate.this, ContentCreate.class);
                startActivity(intent);
            }
        });


    }

    private void startRecording() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(getExternalCacheDir().getAbsolutePath() + "/temp_audio.3gp");
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopRecording() {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }

    private void startPlaying() {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(getExternalCacheDir().getAbsolutePath() + "/temp_audio.3gp");
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                stopPlaying();
                playButton.setText("Play");
            }
        });

    }



    private void stopPlaying() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopRecording();
        stopPlaying();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startRecording();
                recordButton.setText("Stop Recording");
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void showCustomPopup() {
        // Create a custom dialog
        Dialog customDialog = new Dialog(this);
        customDialog.setContentView(R.layout.promptpopuprecipient);
        customDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        // Initialize UI components
        Button closeButton = customDialog.findViewById(R.id.closeButton);
        Button addTextBoxButton = customDialog.findViewById(R.id.addTextBoxButton);
        LinearLayout textBoxContainer = customDialog.findViewById(R.id.textBoxContainer);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        // Restore saved texts
        int textBoxCount = sharedPreferences.getInt("textBoxCount", 0);
        for (int i = 0; i < textBoxCount; i++) {
            String text = sharedPreferences.getString("text" + i, "");
            EditText editText = new EditText(this);
            editText.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            editText.setHint("Enter recipients mailId");
            editText.setText(text); // Set the saved text
            textBoxContainer.addView(editText);
        }

        // Add editable text boxes dynamically
        addTextBoxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText = new EditText(ContentCreate.this);
                editText.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                ));
                editText.setHint("Enter recipients GmailId");
                textBoxContainer.addView(editText);
            }
        });

        // Close the dialog
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Save entered texts to SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("textBoxCount", textBoxContainer.getChildCount());
                for (int i = 0; i < textBoxContainer.getChildCount(); i++) {
                    View childView = textBoxContainer.getChildAt(i);
                    if (childView instanceof EditText) {
                        EditText editText = (EditText) childView;
                        editor.putString("text" + i, editText.getText().toString());
                    }
                }
                editor.apply();

                customDialog.dismiss();
            }
        });

        // Show the dialog
        customDialog.show();
    }

    private void showToggleButtonPopup() {
        // Create a custom dialog
        Dialog toggleButtonDialog = new Dialog(this);
        toggleButtonDialog.setContentView(R.layout.promptpopupspecificrecipient);
        toggleButtonDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        // Initialize UI components
        Button closeButton = toggleButtonDialog.findViewById(R.id.closeButton);
        LinearLayout toggleButtonContainer = toggleButtonDialog.findViewById(R.id.toggleButtonContainer);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        // Create toggle buttons for each saved text
        for (int i = 0; i < sharedPreferences.getInt("textBoxCount", 0); i++) {
            String text = sharedPreferences.getString("text" + i, "");

            // Create a toggle button and set its text
            ToggleButton toggleButton = new ToggleButton(this);
            toggleButton.setTextOff(text);
            toggleButton.setTextOn(text);

            // Add the toggle button to the container and set its initial state
            toggleButtonContainer.addView(toggleButton);
            toggleButton.setChecked(true); // Set the initial state to match the text
        }

        // Close the dialog
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleButtonDialog.dismiss();
            }
        });

        // Show the dialog
        toggleButtonDialog.show();
    }











}
