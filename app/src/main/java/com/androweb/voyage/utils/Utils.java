package com.androweb.voyage.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.androweb.voyage.Helper.Constant;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.splitinstall.SplitInstallManager;
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory;
import com.google.android.play.core.splitinstall.SplitInstallRequest;
import com.google.android.play.core.splitinstall.SplitInstallSessionState;
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener;
import com.google.android.play.core.splitinstall.model.SplitInstallErrorCode;
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus;
import com.google.android.play.core.tasks.OnFailureListener;
import com.google.android.play.core.tasks.OnSuccessListener;

import com.androweb.voyage.R;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

public class Utils {

    public static Bitmap getBitmapFromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }
    }

    public static BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public static boolean isValidMobile(String phone) {
        if (!Pattern.matches("[a-zA-Z]+", phone)) {
            return phone.length() > 6 && phone.length() <= 13;
        }
        return false;
    }

    public static boolean isValidMail(String email) {
        String EMAIL_STRING = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        return Pattern.compile(EMAIL_STRING).matcher(email).matches();

    }

    public static void showSnackBar(View view, String message) {
        Snackbar snackBar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);

        snackBar.setAction(R.string.ok, v -> {
            snackBar.dismiss();
        });

        snackBar.show();
    }

    // Android App Bundle method to open dynamic feature module...
    //------------------------------------------------------------
    public static void openDynamicFeatureModule(Context context, String dynamicModuleName, String toIntentClassName) {
        // Initializes a variable to later track the session ID for a given request...
        int mySessionId = 0;

        // Creates an instance of SplitInstallManager...
        SplitInstallManager splitInstallManager =
                SplitInstallManagerFactory.create(context);

        // Creates a request to install a module...
        SplitInstallRequest request =
                SplitInstallRequest
                        .newBuilder()
                        .addModule(dynamicModuleName)
                        .build();

        // Creates a listener for request status updates...
        SplitInstallStateUpdatedListener updatedListener = new SplitInstallStateUpdatedListener() {
            @Override
            public void onStateUpdate(SplitInstallSessionState state) {
                //Log.e("Session State", state+"");
                if (state.status() == SplitInstallSessionStatus.FAILED
                        && state.errorCode() == SplitInstallErrorCode.SERVICE_DIED) {
                    // Retry the request.
                    return;
                }
                if (state.sessionId() == mySessionId) {
                    ProgressDialog progressDialog = ProgressDialog.show(context, null, "Downloading...", true, false);
                    switch (state.status()) {
                        case SplitInstallSessionStatus.DOWNLOADING:
                            long totalBytes = state.totalBytesToDownload();
                            long progress = state.bytesDownloaded();
                            // Update progress bar.
                            /*Toast.makeText(context,
                                    "Downloading Dynamic Module", Toast.LENGTH_LONG).show();*/
                            //Log.e("Downloading Module", progress+"");
                            progressDialog.dismiss();
                            break;

                        case SplitInstallSessionStatus.INSTALLED:

                            // After a module is installed, you can start accessing its content or
                            // fire an intent to start an activity in the installed module.
                            // For other use cases, see access code and resources from installed modules.

                            // If the request is an on demand module for an Android Instant App
                            // running on Android 8.0 (API level 26) or higher, you need to
                            // update the app context using the SplitInstallHelper API.

                            progressDialog.dismiss();
                            try {
                                Context mContext = context.createPackageContext(context.getPackageName(),
                                        0);
                                // If you use AssetManager to access your app’s raw asset files, you’ll need
                                // to generate a new AssetManager instance from the updated context.
                                AssetManager am = mContext.getAssets();
                                //Log.e("Asset Manager", am+"");
                            } catch (PackageManager.NameNotFoundException e) {
                                e.printStackTrace();
                            }

                                    /*if (BuildCompat.isAtLeastO()) {
                                        // Updates the app’s context with the code and resources of the
                                        // installed module.
                                        SplitInstallHelper.updateAppInfo(MainActivity.this);
                                        new Handler().post(new Runnable() {
                                            @Override
                                            public void run() {
                                                AssetManager am = MainActivity.this.getAssets();

                                            }
                                        });

                                    }*/

                            break;
                        case SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION:
                            progressDialog.dismiss();
                            try {
                                context.startIntentSender(state.resolutionIntent().getIntentSender(),
                                        null,
                                        0,
                                        0,
                                        0);
                            } catch (IntentSender.SendIntentException e) {
                                e.printStackTrace();
                            }

                    }
                }

            }
        };

        // Registers the listener...
        splitInstallManager.registerListener(updatedListener);

        splitInstallManager
                // Submits the request to install the module through the
                // asynchronous startInstall() task. Your app needs to be
                // in the foreground to submit the request.
                .startInstall(request)
                // You should also be able to gracefully handle
                // request state changes and errors. To learn more, go to
                // the section about how to Monitor the request state
                .addOnSuccessListener(new OnSuccessListener<Integer>() {
                    @Override
                    public void onSuccess(Integer sessionID) {
                        /*Toast.makeText(context,
                                "Dynamic Module successful & Session ID:"+sessionID,
                                Toast.LENGTH_LONG).show();*/
                        try {
                            Class dynamicModuleCLS = Class.forName(toIntentClassName);
                            context.startActivity(new Intent(context, dynamicModuleCLS));
                            //((AppCompatActivity) context).finish();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        e.printStackTrace();
                        Toast.makeText(context,
                                "Dynamic Module failed", Toast.LENGTH_LONG).show();


                    }
                });

        // When your app no longer requires further updates, unregister the listener...
        splitInstallManager.unregisterListener(updatedListener);
    }

    public static void openLinkInBrowser(Context context, String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        context.startActivity(i);
    }

    public static void launchPlayStore(Context context) {
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + context.getPackageName())));
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
        }
    }

    public static void setAppRated(Context context, int rating) {
        PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext())
                .edit()
                .putInt(Constant.APP_RATING, rating)
                .putBoolean(Constant.IS_APP_RATED, true)
                .apply();
    }

    public static boolean isAppRated(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext())
                .getBoolean(Constant.IS_APP_RATED, false);
    }

    public static int getAppRating(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext())
                .getInt(Constant.APP_RATING, 0);
    }

    public static void setPlatformAction(Context context, int action) {
        PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext())
                .edit()
                .putInt(Constant.PLATFORM_ACTION, action)
                .apply();
    }

    public static int getPlatformAction(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext())
                .getInt(Constant.PLATFORM_ACTION, 0);
    }

    public static String getMentorID(Context context) {
        String MyPREFERENCES = "Registration";
        return context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE).getString("MentorID", "");
    }

    public static String getLoginType(Context context) {
        String MyPREFERENCES = "Registration";
        return context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE).getString("LoginType", "");
    }

    public static void setMentorName(Context context, String mentorName) {
        String MyPREFERENCES = "Registration";
        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("MentorName", mentorName);
        editor.apply();
    }

    public static String getMentorName(Context context) {
        String MyPREFERENCES = "Registration";
        return context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE).getString("MentorName", "");
    }

    public static void setProfilePic(Context context, String profilePic) {
        String MyPREFERENCES = "Registration";
        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("ProfilePic", profilePic);
        editor.apply();
    }

    public static String getProfilePic(Context context) {
        String MyPREFERENCES = "Registration";
        return context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE).getString("ProfilePic", null);
    }

    public static String changeDateFormat(String date, String oldFormat, String newFormat) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(oldFormat);

        Date sourceDate = null;
        try {
            sourceDate = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat targetFormat = new SimpleDateFormat(newFormat);
        return targetFormat.format(sourceDate);
    }

    public static String getCurrentDateTime(String format) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(c.getTime());
    }

    public static int getWeekDayNumberFromDateString(String stringDate, String dateTimeFormat) {
        int dayOfWeek = 0;

        SimpleDateFormat formatter = new SimpleDateFormat(dateTimeFormat);
        Date date;

        try {
            date = formatter.parse(stringDate);
            Calendar c = Calendar.getInstance();
            c.setTime(date);

            dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dayOfWeek;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public static String getJsonFromAssets(Context context, String fileName) {
        String jsonString;
        try {
            InputStream is = context.getAssets().open(fileName);

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            jsonString = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return jsonString;
    }

    public static boolean isValidImage(String filePath) {
        String ext = null;
        int i = filePath.lastIndexOf('.');


        if (i > 0 && i < filePath.length() - 1) {
            ext = filePath.substring(i + 1).toLowerCase();
        }
        if (ext == null)
            return false;
        else
            return ext.equalsIgnoreCase("jpg") || ext.equalsIgnoreCase("jpeg") || ext.equalsIgnoreCase("png") || ext.equalsIgnoreCase("gif");
    }

    // Enquiry attachment validation check
    public static boolean isValidImageForAttachment(String filePath) {
        String ext = null;
        int i = filePath.lastIndexOf('.');


        if (i > 0 && i < filePath.length() - 1) {
            ext = filePath.substring(i + 1).toLowerCase();
        }
        if (ext == null)
            return false;
        else
            return ext.equalsIgnoreCase("jpg") || ext.equalsIgnoreCase("jpeg") || ext.equalsIgnoreCase("png");
    }

    public static boolean isValidPdf(String filePath) {
        String ext = null;
        int i = filePath.lastIndexOf('.');


        if (i > 0 && i < filePath.length() - 1) {
            ext = filePath.substring(i + 1).toLowerCase();
        }
        if (ext == null)
            return false;
        else
            return ext.equalsIgnoreCase("pdf");
    }

    public static boolean isValidWord(String filePath) {
        String ext = null;
        int i = filePath.lastIndexOf('.');


        if (i > 0 && i < filePath.length() - 1) {
            ext = filePath.substring(i + 1).toLowerCase();
        }
        if (ext == null)
            return false;
        else
            return ext.equalsIgnoreCase("doc") || ext.equalsIgnoreCase("docx");
    }

    public static boolean isConnected(Context context) {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return false;
    }

    public static String abbreviateString(String input, int maxLength) {
        return (input.length() > maxLength) ? input.substring(0, maxLength - 1).concat("…") : input.concat("…");
    }

    public static boolean isValidExpiration(String date) {
        // use SimpleDateFormat to parse values like '0609' as date 'June 2009'
        DateFormat sdf = new SimpleDateFormat("MMyy");

        // establish current date as last day of previous month at 23:59:59
        Calendar now = Calendar.getInstance();
        now.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), 0, 23, 59, 59);

        try {
            // actual parsing of the date, wrapped in try/catch
            // parses as 1st day of month at midnight since only providing month and year
            // e.g., '0609' becomes 'June 01, 2009 00:00:00'
            Date exp = sdf.parse(date);

            // if parsed date is before current month, then return invalid!
            if (exp.before(now.getTime())) return false;
        } catch (ParseException e) {
            return false;  // if not MMYY then reject
        }

        // valid if made it this far, can do in reverse by using !exp.before and return true above.
        return true;
    }

    public static boolean isPackageInstalled(String packageName, PackageManager packageManager) {
        try {
            return packageManager.getApplicationInfo(packageName, 0).enabled;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static String getAppName(String packageName, PackageManager packageManager) {
        try {
            return (String) packageManager.getApplicationLabel(packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA));
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
    }

    public static Drawable getAppIcon(String packageName, PackageManager packageManager) {
        try {
            return packageManager.getApplicationIcon(packageName);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    public static double calculateMonthlyPayment(int loanAmount, int termInMonths, double interestRate) {
        // Convert interest rate into a decimal
        // eg. 6.5% = 0.065
        interestRate /= 100.0;

        // Monthly interest rate
        // is the yearly rate divided by 12
        double monthlyRate = interestRate / 12.0;

        // Calculate the monthly payment
        // Typically this formula is provided so
        // we won't go into the details

        // The Math.pow() method is used calculate values raised to a power
        double monthlyPayment = (loanAmount * monthlyRate) /
                (1 - Math.pow(1 + monthlyRate, -termInMonths));

        return monthlyPayment;
    }
}
