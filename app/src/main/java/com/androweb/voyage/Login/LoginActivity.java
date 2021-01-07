package com.androweb.voyage.Login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.androweb.voyage.R;
import com.bumptech.glide.Glide;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity /*implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener*/ {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 320;
    private GoogleApiClient mGoogleApiClient;
    private SignInButton btnSignIn;
    public LoginButton loginButton;
    public Button facebookLogin, googleLogin;
    public CallbackManager callbackManager;
    public String id, name, email, gender, birthDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

//        callbackManager = CallbackManager.Factory.create();
//        facebookLogin = findViewById(R.id.fb);
//        googleLogin = findViewById(R.id.google);
//        loginButton = findViewById(R.id.login_button);
//        List < String > permissionNeeds = Arrays.asList("user_photos", "email",
//                "user_birthday", "public_profile", "AccessToken");
//        loginButton.registerCallback(callbackManager, new FacebookCallback < LoginResult > () {@Override
//
//        public void onSuccess(LoginResult loginResult) {
//            System.out.println("onSuccess");
//            String accessToken = loginResult.getAccessToken()
//                    .getToken();
//            Log.i("accessToken", accessToken);
//            GraphRequest request = GraphRequest.newMeRequest(
//                    loginResult.getAccessToken(),
//                    new GraphRequest.GraphJSONObjectCallback() {@Override
//                    public void onCompleted(JSONObject object,
//                                            GraphResponse response) {
//                        Log.i("LoginActivity",
//                                response.toString());
//                        try {
//                            id = object.getString("id");
//                            try {
//                                URL profile_pic = new URL(
//                                        "http://graph.facebook.com/" + id + "/picture?type=large");
//                                Log.i("profile_pic",
//                                        profile_pic + "");
//
//                            } catch (MalformedURLException e) {
//                                e.printStackTrace();
//                            }
//                            Log.e("UserDate", String.valueOf(object));
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    });
//            Bundle parameters = new Bundle();
//            parameters.putString("fields","id,name,email,gender, birthday");
//            request.setParameters(parameters);
//            request.executeAsync();
//        }
//
//            @Override
//            public void onCancel() {
//                System.out.println("onCancel");
//            }
//            @Override
//            public void onError(FacebookException exception) {
//                System.out.println("onError");
//                Log.v("LoginActivity", Objects.requireNonNull(exception.getCause()).toString());
//            }
//        });
//        initializeControls();
//        initializeGPlusSettings();
//    }
//
//    private void initializeControls() {
//        btnSignIn = findViewById(R.id.btn_sign_in);
//        btnSignIn.setOnClickListener(this);
//    }
//
//    private void initializeGPlusSettings() {
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail()
//                .build();
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .enableAutoManage(this, this)
//                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//                .build();
//        btnSignIn.setSize(SignInButton.SIZE_STANDARD);
//        btnSignIn.setScopes(gso.getScopeArray());
//    }
//
//    private void signIn() {
//        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
//        startActivityForResult(signInIntent, RC_SIGN_IN);
//    }
//
//
//    private void handleGPlusSignInResult(GoogleSignInResult result) {
//        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
//        if (result.isSuccess()) {
//            GoogleSignInAccount acct = result.getSignInAccount();
//            //Fetch values
//            String personName = acct.getDisplayName();
//            String personPhotoUrl = acct.getPhotoUrl().toString();
//            String email = acct.getEmail();
//            String familyName = acct.getFamilyName();
//            Log.d(TAG, "Name: " + personName +", email: " + email + ", Image: " + personPhotoUrl +", Family Name: " + familyName);
//            updateUI(true);
//        } else {
//            updateUI(false);
//        }
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//
//        OptionalPendingResult opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
//        if (opr.isDone()) {
//            Log.d(TAG, "Got cached sign-in");
//            Result result = opr.get();
//            handleGPlusSignInResult((GoogleSignInResult) result);
//        } else {
//            opr.setResultCallback(result -> handleGPlusSignInResult((GoogleSignInResult) result));
//        }
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
//        // be available.
//        Log.d(TAG, "onConnectionFailed:" + connectionResult);
//    }
//
//
//    @Override
//    public void onClick(View view) {
//        if (view == facebookLogin) {
//            loginButton.performClick();
//        } else if (view == googleLogin) {
//            signIn();
//        }
//    }
//
//    private void updateUI(boolean isSignedIn) {
//        if (isSignedIn) {
//            btnSignIn.setVisibility(View.GONE);
//        } else {
//            btnSignIn.setVisibility(View.VISIBLE);
//        }
    }
}
