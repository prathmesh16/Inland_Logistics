package com.iwlpl.connectme.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.goodiebag.pinview.Pinview;
import com.iwlpl.connectme.R;
import com.iwlpl.connectme.errorHandler.ErrorManager;

public class Activity_Set_Mpin extends AppCompatActivity {

    private Pinview pin,pin2;
    private SharedPreferences pref,userDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
        setContentView(R.layout.activity__set__mpin);

        pin = findViewById(R.id.pinview);
        pin2 = findViewById(R.id.pinview2);
        pref=getSharedPreferences("mpin",MODE_PRIVATE);
        userDetails=getSharedPreferences("userDetails",MODE_PRIVATE);


        pin.setPinViewEventListener(new Pinview.PinViewEventListener() {
            @Override
            public void onDataEntered(Pinview pinview, boolean fromUser) {
                //Make api calls here or what not
            }
        });

        pin2.setPinViewEventListener(new Pinview.PinViewEventListener() {
            @Override
            public void onDataEntered(Pinview pinview, boolean fromUser) { try {
                //Make api calls here or what not
                if(pin.getValue().equals(pin2.getValue()))
                {
                    pref.edit().clear().apply();
                    Toast.makeText(Activity_Set_Mpin.this, "MPIN SET SUCCESSFUL", Toast.LENGTH_SHORT).show();
                    pref.edit().putString("mpin_number",pin2.getValue()).commit();
                    pref.edit().putString("preUserID",userDetails.getString("UserID","")).commit();
                    Intent intent = new Intent(Activity_Set_Mpin.this, Activity_Authenticate.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(Activity_Set_Mpin.this, "MISMATCH PIN",Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e) { handleError(e,"pin2.onClick()"); }
            }
        });
        }catch (Exception e) { handleError(e,"onCreate()"); }
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
        new ErrorManager(Activity_Set_Mpin.this,Activity_Set_Mpin.class.getName(),
                e.getClass().toString(),e.getMessage(),loc);
    }
}
