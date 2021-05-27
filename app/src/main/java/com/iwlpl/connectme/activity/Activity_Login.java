package com.iwlpl.connectme.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iwlpl.connectme.API.APIConstants;
import com.iwlpl.connectme.R;
import com.iwlpl.connectme.errorHandler.ErrorManager;

import org.json.JSONException;
import org.json.JSONObject;

public class Activity_Login extends AppCompatActivity  {

    private EditText username,password;
    private Button login;
    //private TextView forgotPassword,register;
    private SharedPreferences userDetails;
    private TextView verion,register;
    private ProgressBar progressBar;
    private SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
        setContentView(R.layout.activity_login);

        AndroidNetworking.initialize(getApplicationContext());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("auth");
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if(!dataSnapshot.getValue(Boolean.class))
                    {
                        finishAffinity();
                        System.exit(0);
                    }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("error", "Failed to read value.", error.toException());
            }
        });
        pref=getSharedPreferences("mpin",MODE_PRIVATE);
        username = findViewById(R.id.etUsername);
        password = findViewById(R.id.etPassword);
        login = findViewById(R.id.btnLogin);
      //  forgotPassword = findViewById(R.id.tvForgotPassword);
        register = findViewById(R.id.tvRegister);
        progressBar = findViewById(R.id.progressBarLogin);
        userDetails=getSharedPreferences("userDetails",MODE_PRIVATE);
        verion = findViewById(R.id.tvVersion);

        try {
            PackageInfo pInfo = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            verion.setText("Version - "+version);

        } catch (PackageManager.NameNotFoundException e) {
            new ErrorManager(Activity_Login.this,Activity_Navigation.class.getName(),
                    e.getClass().toString(),e.getMessage(),"onCreate");
            e.printStackTrace();
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  validateCredentials();
                    if(validateCredentials())
                    {
                        progressBar.setVisibility(View.VISIBLE);
                        //RequestQueue queue = Volley.newRequestQueue(Activity_Login.this);
                        JSONObject object = new JSONObject();
                        try {
                            object.put("PWD",""+password.getText().toString());
                            object.put("UserID",""+username.getText().toString());
                        } catch (JSONException e) { handleError(e,"login().onClick()");
                            e.printStackTrace();
                        }

                        AndroidNetworking.post(APIConstants.UserLogin)
                                .addJSONObjectBody(object) // posting json
                                .setTag("test")
                                .setPriority(Priority.HIGH)
                                .build()
                                .getAsJSONObject(new JSONObjectRequestListener() {
                                        @Override
                                    public void onResponse(JSONObject response) {

                                        JSONObject data1 = null;

                                        data1=response;

                                        try {
                                            if(data1.getBoolean("IsSuccess")) {

                                                userDetails.edit().putString("UserName",data1.getString("UserName")).commit();
                                                userDetails.edit().putString("MobileNo",data1.getString("MobileNo")).commit();
                                                userDetails.edit().putString("CustomerCode",data1.getString("CustomerCode")).commit();
                                                Log.e("cust code",userDetails.getString("CustomerCode",""));
                                                userDetails.edit().putString("TokenNo",data1.getString("TokenNo")).commit();
                                                Log.e("TOKEN",userDetails.getString("TokenNo",""));

                                                userDetails.edit().putString("UserID",data1.getString("UserID")).commit();
                                                userDetails.edit().putString("Password",password.getText().toString()).commit();
                                                progressBar.setVisibility(View.GONE);
                                                Log.e("pre",pref.getString("preUserID",""));
                                                if(pref.getString("preUserID","").equals(username.getText().toString()))
                                                {
                                                    Intent intent = new Intent(getApplicationContext(), Activity_Authenticate.class);
                                                    startActivity(intent);
                                                }
                                                else {
                                                    Intent intent = new Intent(getApplicationContext(), Activity_Set_Mpin.class);
                                                    intent.putExtra("preUserID",username.getText().toString());
                                                    startActivity(intent);
                                                }
                                            }
                                            else
                                            {
                                                Toast.makeText(Activity_Login.this,"Username or password is incorrect !", Toast.LENGTH_SHORT).show();
                                                progressBar.setVisibility(View.GONE);

                                            }
                                        } catch (JSONException e) { handleError(e,"login().onClick()");
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onError(ANError anError) {
                                        new ErrorManager(Activity_Login.this,Activity_Login.class.getName(),
                                                anError.getClass().toString(),anError.getMessage(),"Network ERROR");
                                        Toast.makeText(Activity_Login.this,"Network ERROR",Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);

                                    }
                                });

                        }


            }
        });

//        forgotPassword.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), Activity_Forgot_Password.class);
//                startActivity(intent);
//                //
//            }
//        });



        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Activity_Register.class);
                startActivity(intent);
            }
        });

        }catch (Exception e) { handleError(e,"onCreate()"); }
    }

    private boolean validateCredentials() {

        if(username.getText().toString().isEmpty())
        {
            Toast.makeText(this, "Enter Username", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(password.getText().toString().isEmpty())
        {
            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
            return false;
        }
    return true;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AndroidNetworking.cancelAll();
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
        new ErrorManager(Activity_Login.this,Activity_Login.class.getName(),
                e.getClass().toString(),e.getMessage(),loc);
    }
}
