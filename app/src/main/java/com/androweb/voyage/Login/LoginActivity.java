package com.androweb.voyage.Login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.androweb.voyage.R;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 320;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInAccount mGoogleSignInAccount;
    private SignInButton btnSignIn;
    private LoginButton loginButton;
    private Button facebookLogin, googleLogin;
    private CallbackManager callbackManager;
    private boolean facebookLoggedIn;
    private AccessToken mAccessToken;
    private AccessTokenTracker mAccessTokenTracker;
    private String fbId, fbName, fbEmail, fbGender, fbBirthDate;
    private String gName, gPhoto, gEmail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        facebookLogin = findViewById(R.id.fb);
        googleLogin = findViewById(R.id.google);
        loginButton = findViewById(R.id.login_button);

        callbackManager = CallbackManager.Factory.create();
        // FaceBook Login
        initializeFacebookLogin();

        // Google Login
        initializeGPlusSettings();

        initializeControls();
    }

    private void initializeFacebookLogin() {
        mAccessToken = AccessToken.getCurrentAccessToken();
        facebookLoggedIn = mAccessToken != null && !mAccessToken.isExpired();

        if (facebookLoggedIn) {
            sendGraphRequest(mAccessToken);
        }

        loginButton.setPermissions(Collections.singletonList("public_profile, email, user_birthday,user_gender"));

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {

                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        sendGraphRequest(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(LoginActivity.this, "Login to Facebook cancel", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        System.out.println("onError");
                        Log.v("LoginActivity", Objects.requireNonNull(exception.getCause()).toString());
                    }
                });
    }

    private void sendGraphRequest(AccessToken mAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(mAccessToken,
                (object, response) -> {
                    getUserDetails(object);
                    Log.d(TAG, "onCompleted: " + response.toString());
                });

        Bundle params = new Bundle();
        params.putString("fields", "id,name,email,gender,birthday");
        request.setParameters(params);
        request.executeAsync();

        mAccessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if (currentAccessToken == null) {
                    // TODO when user log out.
                }
            }
        };
    }

    private void getUserDetails(JSONObject jsonObject) {
        Log.d(TAG, "getUserDetails: " + jsonObject.toString());
        try {

            fbId = jsonObject.getString("id");
            fbName = jsonObject.getString("name");
            fbEmail = jsonObject.getString("email");
            fbGender = jsonObject.getString("gender");
            fbBirthDate = jsonObject.getString("birth");

        }catch(JSONException e) {
            e.printStackTrace();
        }
    }

    private void initializeControls() {
        btnSignIn = findViewById(R.id.btn_sign_in);
        btnSignIn.setOnClickListener(this);
    }

    /**
     * Configure sign-in to request the user's ID, email address, and basic
     * profile. ID and basic profile are included in DEFAULT_SIGN_IN.
     */
    private void initializeGPlusSettings() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        //btnSignIn.setSize(SignInButton.SIZE_STANDARD);

    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if (account != null) {
                gName = account.getDisplayName();
                gPhoto = Objects.requireNonNull(account.getPhotoUrl()).toString();
                gEmail = account.getEmail();

                updateUI(mGoogleSignInAccount);
            }

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode() + "\n" + e.getLocalizedMessage() + "\nMEssg: " + e.getMessage());
        }
    }

    private void updateUI(GoogleSignInAccount mGoogleSignInAccount) {
        //TODO next activity
    }


    @Override
    public void onClick(View view) {
        if (view == facebookLogin) {
            loginButton.performClick();
        } else if (view == googleLogin) {
            signIn();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.

        mGoogleSignInAccount = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(mGoogleSignInAccount);
    }

}
