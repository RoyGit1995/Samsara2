package com.example.samsara;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class RecipientOptionsScreen extends AppCompatActivity{

    private Button geolocationButton;
    private NfcAdapter nfcAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipientoptionsscreen);

        geolocationButton = findViewById(R.id.geolocationbasedcontent);

        geolocationButton.setOnClickListener(v -> {
            Intent intent = new Intent(RecipientOptionsScreen.this, LocationContentActivity.class);
            startActivity(intent);
        });



        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (nfcAdapter != null) {
            enableNfcForegroundDispatch();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (nfcAdapter != null) {
            disableNfcForegroundDispatch();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent); // Save the new intent
        if (nfcAdapter != null && NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            Log.e("MainActivity", "nfcAdapter for NFC");
            handleNfcTag(intent);
        }
    }

    private void enableNfcForegroundDispatch() {
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
    }

    private void disableNfcForegroundDispatch() {
        // Disable NFC foreground dispatch if needed
    }

    private void handleNfcTag(Intent intent) {
        Tag tag = getIntent().getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (tag != null)
        {
            byte[] id = tag.getId();
            // Convert id to a string (you may need to adjust the conversion)
            String nfcId = byteArrayToHexString(id);


            Log.e("NFC ID................. =", byteArrayToHexString(id));

            // Trigger content based on NFC ID
            if ("043D5402B83480".equals(nfcId)) {
                setContentView(R.layout.memoriachairpublicnfccontent);
            } else if ("0405B6B2003E81".equals(nfcId)) {
                setContentView(R.layout.urn_nfc_content);
            } else if ("0404E45AA84A81".equals(nfcId)) {
                setContentView(R.layout.spectacles_nfc_content);
            }else if ("049FB07AB83480".equals(nfcId)) {
                setContentView(R.layout.camera_nfc_content);
            } 
        }

    }

    // Helper method to convert byte array to hexadecimal string
    private String byteArrayToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }

}
