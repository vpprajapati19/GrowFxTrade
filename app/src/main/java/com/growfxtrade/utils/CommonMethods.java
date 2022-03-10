package com.growfxtrade.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;

import androidx.annotation.RequiresApi;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.growfxtrade.R;

import org.aviran.cookiebar2.CookieBar;

public class CommonMethods {

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static void changeActivity(Context context, Class classname) {
        context.startActivity(new Intent(context, classname));
    }

    public static void PrintLog(String tag, String msg) {
        Log.e(tag, "Message ==>>  " + msg);
    }

    public static boolean getInternetStatus(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    public static void showInternetDialog(Context context) {
        final Dialog dialog1 = new Dialog(context, R.style.df_dialog);
        dialog1.setContentView(R.layout.dialog_no_internet);
        dialog1.setCancelable(true);
        dialog1.setCanceledOnTouchOutside(true);
        dialog1.findViewById(R.id.btnSpinAndWinRedeem).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
            }
        });
        dialog1.show();

    }


    public static Dialog showDialogProgressBarNew(Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialognew);

        ProgressBar progressBar = (ProgressBar) dialog.findViewById(R.id.spin_kit);
        Sprite doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);
        if (!dialog.isShowing()) {
            dialog.show();
        }
        return dialog;
    }

//    public static Dialog showDialogProgressBar(Context context) {
//        final Dialog dialog = new Dialog(context);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setCancelable(false);
//        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//        dialog.setContentView(R.layout.dialog);
//        ProgressBar progressBar = (ProgressBar) dialog.findViewById(R.id.progressBar);
//        progressBar.setIndeterminate(true);
//        if (!dialog.isShowing()) {
//            dialog.show();
//        }
//        return dialog;
//    }

    public static void simpleSnackbar(Context context, String title) {
        CookieBar.build((Activity) context)
//                .setTitle(msg)
                .setTitleColor(R.color.whiteTextColor)
                .setBackgroundColor(R.color.colorPrimary)
                .setCookiePosition(CookieBar.BOTTOM)
                .setDuration(2000) // 2 seconds
                .show();

    }


    public static void hideKeyBoard(Activity context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = context.getCurrentFocus();
        if (view == null) {
            view = new View(context);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }



}
