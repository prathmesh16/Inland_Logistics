package com.iwlpl.connectme.activity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import com.goodiebag.pinview.Pinview;
import java.util.concurrent.Executor;
import com.iwlpl.connectme.R;
import com.iwlpl.connectme.errorHandler.ErrorManager;

public class Activity_Authenticate extends AppCompatActivity {

    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    private Pinview pin;
    private SharedPreferences pref,userDetails;
    private ImageView fingerprint;
    private Button Logout;
    private TextView forgotMPIN;
//    private String fromActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {

            setContentView(R.layout.activity__authenticate);

            userDetails = getSharedPreferences("userDetails", MODE_PRIVATE);
            forgotMPIN = findViewById(R.id.forgotMPIN);
            forgotMPIN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {  try{
                    Intent intent = new Intent(Activity_Authenticate.this, Activity_Set_Mpin.class);
                    intent.putExtra("preUserID", userDetails.getString("UserName", ""));
                    startActivity(intent);

                }catch (Exception e) { handleError(e,"forgotMPIN.onClick()"); }
                }
            });
//        fromActivity=getIntent().getStringExtra("fromActivity");

            Logout = findViewById(R.id.btnLogout);

            if (check_Biometric_Availabilty()) {
                executor = ContextCompat.getMainExecutor(this);
                biometric_Login();
                biometricPrompt.authenticate(promptInfo);
            }


            pin = findViewById(R.id.pinview3);
            pref = getSharedPreferences("mpin", MODE_PRIVATE);
            fingerprint = findViewById(R.id.ivFinger);

            fingerprint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { try{
                    biometricPrompt.authenticate(promptInfo);

                }catch (Exception e) { handleError(e,"fingerprint.onClick()"); }
                }
            });

            pin.setPinViewEventListener(new Pinview.PinViewEventListener() {
                @Override
                public void onDataEntered(Pinview pinview, boolean fromUser) { try{
                    //Make api calls here or what not
                    String m = pref.getString("mpin_number", "");
                    if (pin.getValue().equals(m)) {
                        Toast.makeText(Activity_Authenticate.this, "Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Activity_Authenticate.this, Activity_Navigation.class);
                        startActivity(intent);
                    }
                }catch (Exception e) { handleError(e,"pin.onDataEntered()"); }
                }
            });

            Logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { try{
                    SharedPreferences preferences = getSharedPreferences("userDetails", Context.MODE_PRIVATE);
                    preferences.edit().clear().apply();
                    SharedPreferences preferences1 = getSharedPreferences("mpin", Context.MODE_PRIVATE);
                    preferences1.edit().clear().apply();


                    startActivity(new Intent(Activity_Authenticate.this, Activity_Login.class));
                    finish();
                }catch (Exception e) { handleError(e,"Logout.onClick()"); }
                }
            });

            new ErrorManager(Activity_Authenticate.this);
        }catch (Exception e) {  }
    }

    private void biometric_Login() throws Exception{

        biometricPrompt = new BiometricPrompt(Activity_Authenticate.this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(),
                        "Enter Login Pin", Toast.LENGTH_SHORT)
                        .show();
//                finish();
//                System.exit(0);
            }
            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(),
                        "Authentication Succeeded!", Toast.LENGTH_SHORT).show();
//                Log.e("errrrr","dsd"+fromActivity);
//                if (fromActivity.equals(""))
//                {
                    Intent intent = new Intent(Activity_Authenticate.this, Activity_Navigation.class);
                    startActivity(intent);
//                }
//                else if(fromActivity.equals("NotificationPickUp"))
//                {
//                    Intent intent = new Intent(Activity_Authenticate.this, Activity_PickupMaster.class);
//                    startActivity(intent);
//                }
//                else if(fromActivity.equals("NotificationDD"))
//                {
//                    Intent intent = new Intent(Activity_Authenticate.this, Activity_PickupMaster.class);
//                    startActivity(intent);
//                }


            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Authentication Failed",
                        Toast.LENGTH_SHORT)
                        .show();

            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Fingerprint Login")
                .setSubtitle("Log in using your Fingerprint")
                .setNegativeButtonText("Enter Login Pin")
                .build();


    }

    private boolean check_Biometric_Availabilty() throws Exception{

        BiometricManager biometricManager = BiometricManager.from(Activity_Authenticate.this);
        switch (biometricManager.canAuthenticate()) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                Log.e("MY_APP_TAG", "App can authenticate using biometrics.");
                return true;

            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Log.e("MY_APP_TAG", "No biometric features available on this device.");
                return false;

            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Log.e("MY_APP_TAG", "Biometric features are currently unavailable.");
                return false;

            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Log.e("MY_APP_TAG", "The user hasn't associated " +
                        "any biometric credentials with their account.");
                return false;

        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
        finish();

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    void handleError(Exception e,String loc){
    new ErrorManager(Activity_Authenticate.this,Activity_Authenticate.class.getName(),
            e.getClass().toString(),e.getMessage(),loc);

    }
}
