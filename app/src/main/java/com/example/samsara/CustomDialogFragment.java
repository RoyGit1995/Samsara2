package com.example.samsara;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
public class CustomDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(requireContext());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.promptpopup);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(android.view.Gravity.BOTTOM);


        Button openSecondPopupButton = dialog.findViewById(R.id.promptpageid);
        openSecondPopupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SecondPopupFragment secondPopupFragment = new SecondPopupFragment();
                secondPopupFragment.show(getParentFragmentManager(), "second_popup");
            }
        });


        return dialog;
    }
}
