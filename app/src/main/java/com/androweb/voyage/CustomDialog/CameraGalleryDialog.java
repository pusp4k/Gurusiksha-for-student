package com.androweb.voyage.CustomDialog;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;

import com.androweb.voyage.R;
import com.androweb.voyage.databinding.DialogCameraGalleryBinding;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class CameraGalleryDialog extends DialogFragment {
    public static final String TAG = CameraGalleryDialog.class.getSimpleName();

    private DialogCameraGalleryBinding binding;
    private ImageSelectListener imageSelectListener;
    private static final int APP_PERMISSION_REQUEST_CODE = 301;
    private static final int REQUEST_TAKE_PHOTO = 201;
    private static final int REQUEST_GALLERY = 101;

    private String[] permissions = {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private static final String CAMERA = "CAMERA";
    private static final String GALLERY = "GALLERY";

    private String type;
    private String imageFilePath;

    // Instance Initialize
    public static CameraGalleryDialog newInstance(ImageSelectListener imageSelectListener) {
        CameraGalleryDialog cameraDialog = new CameraGalleryDialog();
        cameraDialog.imageSelectListener = imageSelectListener;
        return cameraDialog;
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
        binding = DialogCameraGalleryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.tvCamera.setOnClickListener(v -> {
            type = CAMERA;

            permissions = new String[]{
                    Manifest.permission.CAMERA
            };

            if (checkAndRequestPermission()) {
                openCameraGallery();
            }
        });

        binding.tvGallery.setOnClickListener(v -> {
            type = GALLERY;

            permissions = new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };

            if (checkAndRequestPermission()) {
                openCameraGallery();
            }
        });
    }

    private boolean checkAndRequestPermission() {
        // Check Which permission are granted
        List<String> listPermissions = new ArrayList<>();
        for (String perm : permissions) {
            if (ContextCompat.checkSelfPermission(requireContext(), perm) != PackageManager.PERMISSION_GRANTED) {
                listPermissions.add(perm);
            }
        }

        // Ask for non-granted permission
        if (!listPermissions.isEmpty()) {
            requestPermissions(listPermissions.toArray(new String[0]),
                    APP_PERMISSION_REQUEST_CODE);
            return false;
        }
        // App all permission
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        HashMap<String, Integer> permissionResult = new HashMap<>();
        int deniedCount = 0;

        // Gather permission grant result
        for (int i = 0; i < grantResults.length; i++) {

            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                permissionResult.put(permissions[i], grantResults[i]);
                deniedCount++;
            }
        }

        if (deniedCount == 0) {
            openCameraGallery();
        } else {
            for (Map.Entry<String, Integer> entry : permissionResult.entrySet()) {
                String permName = entry.getKey();

                // ------------------------------------------------------------------------------------ //
                // Permission is Denied (This is the first time, When "Never Ask Again" is Not Checked)
                // So ask Again Explaining User the usage of Permission
                // ShouldShowRequestPermissionRational will return true
                // -----------------------------------------------------------------------------//
                if (ActivityCompat.shouldShowRequestPermissionRationale(Objects.requireNonNull(getActivity()), permName)) {
                    showDialog(R.string.app_permission_msg, true);

                } else {
                    // Permission is denied [Never ask again is checked]
                    // Ask user to go to settings and manually allow permission

                    showDialog(R.string.permission_denied_msg, false);

                    break;
                }
            }
        }
    }

    private void openCameraGallery() {
        if (type.equals(CAMERA)) {
            cameraIntent();
        } else {
            galleryIntent();
        }
    }

    private void cameraIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Camera Activity Null Check
        if (takePictureIntent.resolveActivity(Objects.requireNonNull(getContext()).getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                Toast.makeText(getContext(), "Photo file can't be created, please try again", Toast.LENGTH_SHORT).show();
                return;
            }

            // Continue only if the file was successfully created
            Uri photoUri = FileProvider.getUriForFile(getContext(),
                    "com.androweb.voyage.fileprovider",
                    photoFile);

            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
        }
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), REQUEST_GALLERY);
    }

    private void showDialog(int msg,
                            boolean shouldShowRequest) {

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = this.getLayoutInflater();
        @SuppressLint("InflateParams") View dialogInterface = inflater.inflate(R.layout.custom_dialog, null);
        builder.setView(dialogInterface);

        TextView dialogTitle = dialogInterface.findViewById(R.id.dialogTitle);
        TextView dialogMessage = dialogInterface.findViewById(R.id.dialogMessage);
        Button dialogPositive = dialogInterface.findViewById(R.id.dialogPositive);
        Button dialogNegative = dialogInterface.findViewById(R.id.dialogNegative);

        dialogTitle.setText(R.string.app_permission_title);
        dialogMessage.setText(msg);

        AlertDialog alert = builder.create();
        alert.setCancelable(false);

        dialogPositive.setOnClickListener(view -> {
            if (shouldShowRequest) {
                alert.dismiss();
                checkAndRequestPermission();
            } else {
                // Go to Settings
                alert.dismiss();
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", Objects.requireNonNull(getContext()).getPackageName(), null));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        dialogNegative.setOnClickListener(v -> alert.dismiss());

        alert.show();
    }

    private File createImageFile() throws IOException {
        // Create an image file
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date());
        String imageFileName = "Voyager" + timeStamp + "_";

        File storageDir = Objects.requireNonNull(getContext()).getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,   /* prefix */
                ".jpg",   /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with Action_view intents
        imageFilePath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_TAKE_PHOTO:
                if (resultCode == Activity.RESULT_OK) {
                    File imgFile = new File(imageFilePath);
                    if (imgFile.exists()) {
                        imageSelectListener.onImageSelected((Uri.fromFile(imgFile)));
                        dismiss();
                    }
                }
                break;

            case REQUEST_GALLERY:
                if (resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
                    imageSelectListener.onImageSelected(data.getData());
                    dismiss();
                }
                break;
        }
    }

    public interface ImageSelectListener {
        void onImageSelected(Uri imageUri);
    }
}
