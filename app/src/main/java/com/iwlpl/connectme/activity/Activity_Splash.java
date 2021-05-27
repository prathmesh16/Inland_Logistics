package com.iwlpl.connectme.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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

public class Activity_Splash extends AppCompatActivity{

    private SharedPreferences pref;
    Dialog noInternetDialog;
    Button CloseBtn;
    Handler handler;
    Runnable runnable;
    TextView version;
    String ver;
    String m;
    String id;
    String p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
        setContentView(R.layout.activity__splash);
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

        version = findViewById(R.id.tvVerSplash);

        try {

            PackageInfo pInfo = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0);
            ver = pInfo.versionName;
            version.setText("Version : "+ver);

        } catch (PackageManager.NameNotFoundException e) {handleError(e,"onCreate()");
            e.printStackTrace();
        }


        if (isConnected()) {

            pref=getSharedPreferences("userDetails",MODE_PRIVATE);

            m = pref.getString("UserName","");

            p=pref.getString("Password","");

            id=pref.getString("UserID","");

            updateAvailble();

        }
        else
        {

            noInternetDialog=new Dialog(Activity_Splash.this);
            noInternetDialog.setContentView(R.layout.no_internet_layout);
            noInternetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            noInternetDialog.setCancelable(false);
            Window window = noInternetDialog.getWindow();
            window.setGravity(Gravity.CENTER);
            window.getAttributes().windowAnimations=R.style.DialogAnimation;
            final WindowManager.LayoutParams params = window.getAttributes();
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(params);
            noInternetDialog.show();
            CloseBtn=noInternetDialog.findViewById(R.id.dialog_button_positive);
            CloseBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }
        }catch (Exception e) { handleError(e,"onCreate()"); }
    }

    private void updateAvailble() throws Exception{

        AndroidNetworking.get(APIConstants.AppVersionCheck)
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        //if(response.getString("AppVersion").equalsIgnoreCase(ver))  //no update available
                        if(true)
                        {
                            if(m.isEmpty())
                            {
                                handler =  new Handler();
                                runnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent startActivity = new Intent(Activity_Splash.this, Activity_Login.class);
                                        startActivity(startActivity);
                                        finish();
                                    }
                                };

                                handler.postDelayed(runnable, 2000);
                            }
                            else
                            {
                                JSONObject object = new JSONObject();
                                try {
                                    object.put("PWD",p);
                                    object.put("UserID",id);
                                } catch (JSONException e) {handleError(e,"updateAvailble()");
                                    e.printStackTrace();
                                }
                                Log.e("obj",object.toString());
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
                                                        handler =  new Handler();
                                                        runnable = new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                String action ="";
                                                                if (getIntent().getExtras()!=null){
                                                                    //Toast.makeText(Activity_Splash.this,getIntent().getExtras().getString("fromActivity"),Toast.LENGTH_SHORT).show();
                                                                    action=getIntent().getExtras().getString("fromActivity");

                                                                }

                                                                //startActivity.putExtra("fromActivity",getIntent().getExtras().getString("fromActivity"));
                                                                Intent startActivity = new Intent(Activity_Splash.this, Activity_Authenticate.class);
                                                                startActivity.putExtra("fromActivity",action);
                                                                startActivity(startActivity);
                                                                finish();
                                                            }
                                                        };
                                                        handler.postDelayed(runnable, 2000);

                                                    }
                                                    else
                                                    {



                                                        handler =  new Handler();
                                                        runnable = new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                Intent startActivity = new Intent(Activity_Splash.this, Activity_Login.class);
                                                                startActivity(startActivity);
                                                                finish();
                                                            }
                                                        };

                                                        handler.postDelayed(runnable, 2000);

                                                    }
                                                } catch (JSONException e) { handleError(e,"updateAvailble()");
                                                    e.printStackTrace();
                                                }
                                            }

                                            @Override
                                            public void onError(ANError anError) {
                                                handler =  new Handler();
                                                runnable = new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Intent startActivity = new Intent(Activity_Splash.this, Activity_Login.class);
                                                        startActivity(startActivity);
                                                        finish();
                                                    }
                                                };

                                                handler.postDelayed(runnable, 2000);
                                                //Toast.makeText(Activity_Splash.this,"Network ERROR",Toast.LENGTH_LONG).show();




                                            }
                                        });


                            }
                        }
                        else
                        {
                            //update available
                            //TODO : update availble redirect to playstore
                            //TODO : Show app update Dialog and redirect to playstore

                            final Dialog dialog = new Dialog(Activity_Splash.this);
                            dialog.setContentView(R.layout.custom_dialog_ui);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialog.setCancelable(false);
                            Window window = dialog.getWindow();
                            window.setGravity(Gravity.CENTER);
                            window.getAttributes().windowAnimations = R.style.DialogAnimation;
                            final WindowManager.LayoutParams params = window.getAttributes();
                            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
                            params.width = WindowManager.LayoutParams.MATCH_PARENT;
                            window.setAttributes(params);
                            dialog.show();
                            //initializations
                            TextView title = dialog.findViewById(R.id.dialog_title);
                            TextView msg = dialog.findViewById(R.id.dialog_msg);
                            Button positive = dialog.findViewById(R.id.dialog_button_positive);
                            Button negative = dialog.findViewById(R.id.dialog_button_negative);
                            ImageView icon = dialog.findViewById(R.id.dialog_icon);

                            //icon
                            icon.setImageDrawable(getResources().getDrawable(R.drawable.update_icon));
                            //title
                            title.setText("APP UPDATE");
                            //msg
                            msg.setText("This app is no longer functional. Please update the app to continue.");
                            //postive btn action
                            positive.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    final String appName = getPackageName();
                                    try {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appName)));
                                    } catch (android.content.ActivityNotFoundException anfe) {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appName)));
                                    }
                                    finish();
                                    dialog.dismiss();
                                    //TODO positive redirect to play store
                                }
                            });
                            //negative btn action
                            negative.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                    finish();
                                }
                            });


                            //Toast.makeText(Activity_Splash.this, "Update Avaible", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });

    }

    public boolean isConnected()  throws Exception{
        ConnectivityManager connector = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        assert connector != null;
        NetworkInfo info = connector.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        handler.removeCallbacks(runnable);

    }
    void handleError(Exception e,String loc){
        new ErrorManager(Activity_Splash.this,Activity_Splash.class.getName(),
                e.getClass().toString(),e.getMessage(),loc);
    }
}
