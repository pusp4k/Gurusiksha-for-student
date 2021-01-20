package com.androweb.voyage.UserDetails;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.androweb.voyage.CustomDialog.CameraGalleryDialog;
import com.androweb.voyage.CustomDialog.GenderPickerDialog;
import com.androweb.voyage.LocationService.FetchLocationActivity;
import com.androweb.voyage.R;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class UserDetails extends AppCompatActivity {


    private static String TAG = UserDetails.class.getSimpleName();

    private ImageView userPhoto;
    private ImageButton btnUserPicEdit;
    private Button btEditUserAdd;
    private TextView userName;
    private TextView userAddress;

    private EditText userEmail;
    private EditText userPhone;
    private TextView userDob;
    private TextView userGender;

    private LinearLayout fbDetails;
    private LinearLayout googleDetails;
    private TextView userFbDetails;
    private TextView userGoogleDetails;

    private boolean isNormalLogin;
    private boolean isFacebookLogin;
    private boolean isGoogleLogin;

    private double latitude = 0.0;
    private double longitude = 0.0;

    private SharedPreferences appPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        userPhoto = findViewById(R.id.userPhoto);
        btnUserPicEdit = findViewById(R.id.btUserPicEdit);
        btEditUserAdd = findViewById(R.id.edit_user_address);
        userName = findViewById(R.id.user_name);
        userAddress = findViewById(R.id.user_address);
        userEmail = findViewById(R.id.user_email);
        userPhone = findViewById(R.id.user_phone);
        userDob = findViewById(R.id.user_dob);
        userGender = findViewById(R.id.user_gender);

        fbDetails = findViewById(R.id.facebook_details);
        googleDetails = findViewById(R.id.google_details);
        userFbDetails = findViewById(R.id.user_facebook_details);
        userGoogleDetails = findViewById(R.id.user_google_details);

        // Photo setup
        initCamera();

        initDoBSelect(userDob.getText().toString().trim());

        initGenderChooser();

        selectAddress();

    }

    private void initCamera() {
        userPhoto.setOnClickListener(view -> openCameraGalleryDialog());

        btnUserPicEdit.setOnClickListener(view -> openCameraGalleryDialog());
    }

    private void openCameraGalleryDialog() {
        CameraGalleryDialog.newInstance(imageUri -> CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAllowRotation(true)
                .setAllowCounterRotation(true)
                .setAutoZoomEnabled(true)
                .setFixAspectRatio(true)
                .setAspectRatio(1, 1)
                .start(this)).show(getSupportFragmentManager(), CameraGalleryDialog.TAG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                Uri userImage = result.getUri();

                Picasso.get()
                        .load(userImage)
                        .placeholder(R.drawable.ic_user_default)
                        .error(R.drawable.ic_user_default)
                        .into(userPhoto);
            }
        }
    }

    /**
     * User Dob Select dialog
     */
    private void initDoBSelect(String date) {
        userDob.setOnClickListener(view -> openDateSelectDialog(date));
    }

    private void openDateSelectDialog(String date) {
        Calendar calendar = Calendar.getInstance();

        if (!date.isEmpty() || (date.equalsIgnoreCase(getResources().getString(R.string.dob_error)))) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

            try {
                calendar.setTime(Objects.requireNonNull(sdf.parse(date)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (datePicker, mYear, mMonth, mDay) -> {
                    calendar.set(Calendar.YEAR, mYear);
                    calendar.set(Calendar.MONTH, mMonth);
                    calendar.set(Calendar.DAY_OF_MONTH, mDay);

                    userDob.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.getTime()));
                }, year, month, day);

        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
        datePickerDialog.show();

    }

    private void initGenderChooser() {
        userGender.setOnClickListener(view -> openGenderSelectDialog());

    }

    private void openGenderSelectDialog() {
        GenderPickerDialog.newInstance(this::setGenderText).show(getSupportFragmentManager(),GenderPickerDialog.TAG);
    }

    private void setGenderText(String genderSelector) {
        userGender.setText(genderSelector.trim());
    }


    private void selectAddress() {
        btEditUserAdd.setOnClickListener(v ->{


            LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
            @SuppressLint("MissingPermission") Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }

        FetchLocationActivity.getLocation(UserDetails.this,101,
                "Select Location", latitude, longitude);
        });
    }
}
