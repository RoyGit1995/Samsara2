package com.example.samsara;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.List;

public class CustomDialogFragmentRecipient extends DialogFragment {

    private int maxTextBoxes = 8;
    private int textBoxCount = 0;
    private List<String> savedTexts = new ArrayList<>(); // Store the entered texts
    private static final String SAVED_TEXTS_KEY = "saved_texts_key";

    private LinearLayout textBoxContainer; // Store a reference to the container

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(requireContext());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.promptpopuprecipient);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        // Initialize UI components
        Button addTextBoxButton = dialog.findViewById(R.id.addTextBoxButton);
        textBoxContainer = dialog.findViewById(R.id.textBoxContainer); // Assign the reference

        // Restore saved texts
        if (savedInstanceState != null) {
            savedTexts = savedInstanceState.getStringArrayList(SAVED_TEXTS_KEY);
            for (String text : savedTexts) {
                EditText editText = new EditText(requireContext());
                editText.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                ));
                editText.setHint("Enter text");
                editText.setText(text); // Set the saved text
                textBoxContainer.addView(editText);
                textBoxCount++;
            }
        }

        addTextBoxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textBoxCount < maxTextBoxes) {
                    EditText editText = new EditText(requireContext());
                    editText.setLayoutParams(new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    ));
                    editText.setHint("Enter text");
                    textBoxContainer.addView(editText);
                    textBoxCount++;
                }
            }
        });

        return dialog;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the entered texts when the pop-up is dismissed
        ArrayList<String> texts = new ArrayList<>();
        for (int i = 0; i < textBoxContainer.getChildCount(); i++) {
            View childView = textBoxContainer.getChildAt(i);
            if (childView instanceof EditText) {
                EditText editText = (EditText) childView;
                texts.add(editText.getText().toString());
            }
        }
        outState.putStringArrayList(SAVED_TEXTS_KEY, texts);
    }
}