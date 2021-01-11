package com.androweb.voyage.CustomProgressDialog;

import android.app.Activity;
import android.app.Dialog;
import android.widget.ImageView;

import com.androweb.voyage.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import static android.view.Window.FEATURE_NO_TITLE;

public class CustomProgressDialog {
    private Activity mActivity;
    private Dialog mDialog;

    public CustomProgressDialog(Activity activity) {
        this.mActivity = activity;
    }

    public void showProgress() {
        mDialog = new Dialog(mActivity);
        mDialog.requestWindowFeature(FEATURE_NO_TITLE);

        // Set cancelable false so that it's never get hidden
        mDialog.setCancelable(false);

        // Layout that will inflate later
        mDialog.setContentView(R.layout.progress_dialog_view);

        // Initialize the imageView form inflated layout
        ImageView gif = mDialog.findViewById(R.id.custom_loading_imageView);

        // To load gif into an ImageView before Glide and for doing
        // this we need DrawableImageViewTarget to that ImageView

        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(gif);

        // Load Gif as Dialog View
        Glide.with(mActivity)
                .load(R.drawable.loading)
                .placeholder(R.drawable.loading)
                .centerCrop()
                .crossFade()
                .into(imageViewTarget);

        mDialog.show();

    }

    // Hide the dialog when process finished
    public void processFinished () {
        mDialog.dismiss();
    }
}
