package com.androweb.voyage.UserDetails;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.androweb.voyage.CustomDialog.CameraGalleryDialog;
import com.androweb.voyage.R;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class UserDetails extends AppCompatActivity {


    private String TAG = UserDetails.class.getSimpleName();

    private Context mContext;
    private ImageView userPhoto;
    private ImageButton btnUserPicEdit;
    private ImageButton btEditUserName;
    private ImageButton btEditUserAdd;
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

    private SharedPreferences appPreferences;

//    public UserDetails (Context context) {
//        this.mContext = context;
//    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_details);

        userPhoto = findViewById(R.id.userPhoto);
        btnUserPicEdit = findViewById(R.id.btUserPicEdit);
        btEditUserName = findViewById(R.id.edit_user_name);
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
            if( data != null ) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                Uri userImage = result.getUri();

                Picasso.get()
                        .load(userImage)
                        .placeholder(R.drawable.gurusiksha_g_logo)
                        .error(R.drawable.gurusiksha_g_logo)
                        .into(userPhoto);
            }
        }
    }
}
