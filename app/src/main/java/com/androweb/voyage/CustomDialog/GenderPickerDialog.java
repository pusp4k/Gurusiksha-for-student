package com.androweb.voyage.CustomDialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.androweb.voyage.databinding.DialogGenderBinding;

import java.util.Objects;

public class GenderPickerDialog extends DialogFragment {
    public static final String TAG = GenderPickerDialog.class.getSimpleName();

    private DialogGenderBinding binding;
    private GenderSelector genderSelector;

    private static final String MALE = "MALE";
    private static final String FEMALE = "FEMALE";

    private String type;

    public static GenderPickerDialog newInstance(GenderSelector genderSelector) {
        GenderPickerDialog dialog = new GenderPickerDialog();
        dialog.genderSelector = genderSelector;
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            Objects.requireNonNull(dialog.getWindow()).setLayout(width, height);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogGenderBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.cbMale.setOnClickListener(v -> {
            type = MALE;
            genderSelector.onGenderSelected(type);
            dismiss();
        });

        binding.cbFemale.setOnClickListener(v -> {
            type = FEMALE;
            genderSelector.onGenderSelected(type);
            dismiss();
        });

    }

    public interface GenderSelector {
        void onGenderSelected(String gender);
    }
}
