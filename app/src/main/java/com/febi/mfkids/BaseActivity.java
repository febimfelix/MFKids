package com.febi.mfkids;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by flock on 31/1/17.
 */

public class BaseActivity extends AppCompatActivity {

    private ProgressDialog mProgressDialog;

    public DrawerLayout mDrawerLayout;

    public SharedPreferences mPreferences;

    public DatabaseReference mKidsDatabaseReference;
    public DatabaseReference mMilestoneDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPreferences                = getSharedPreferences(AppUtils.APP_PREFERENCE, MODE_PRIVATE);
        mKidsDatabaseReference      = FirebaseDatabase.getInstance().getReference("kids");
        mMilestoneDatabaseReference = FirebaseDatabase.getInstance().getReference("milestones");
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public boolean checkInternet(boolean isAlertNeeded) {
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();
        if(!isConnected && isAlertNeeded) {
            showDismissiveAlerts("No Connection!!!");
        }
        return isConnected;
    }

    public void showDismissiveAlerts(String message){
        final AlertDialog alertDialog	=	 new AlertDialog.Builder(this).create();
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public void showProgressDialog(){
        if(mProgressDialog != null && mProgressDialog.isShowing())
            return;
        mProgressDialog = ProgressDialog.show(BaseActivity.this,
                "", "Loading...", true, false);
    }

    public void hideProgressDialog(){
        if(mProgressDialog != null && mProgressDialog.isShowing()){
            mProgressDialog.dismiss();
        }
    }

    //region Check Permission
    public boolean isPermissionGranted(String permission) {
        if(ContextCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }
    //endregion

    //region SHOW/HIDE KEYBOARD
    public void showKeyboard(EditText editText) {
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    public void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    //endregion
}
