package com.iwlpl.connectme.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.iwlpl.connectme.API.APIConstants;
import com.iwlpl.connectme.R;
import com.iwlpl.connectme.errorHandler.ErrorManager;

import org.json.JSONException;
import org.json.JSONObject;

public class Activity_Register extends AppCompatActivity {

    ImageButton back;
    Button register;
    EditText uname,mob,email,adr,pin,compname,compPanno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
        setContentView(R.layout.activity_register);
        AndroidNetworking.initialize(getApplicationContext());

        back = findViewById(R.id.backBtnActivity);
        register = findViewById(R.id.btnRegister);

        uname = findViewById(R.id.Name);
        mob = findViewById(R.id.contactNo);
        email = findViewById(R.id.email);
        adr = findViewById(R.id.address);
        pin = findViewById(R.id.pincode);
        compname = findViewById(R.id.CompanyName);
        compPanno = findViewById(R.id.PANno);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { try {
                if(validate())
                {
                    try {
                        submitdata();
                    } catch (JSONException e) { handleError(e,"register.onClick()");
                        e.printStackTrace();
                    }
                }
            }catch (Exception e) { handleError(e,"register.onClick()"); }
            }
        });

        }catch (Exception e) { handleError(e,"onCreate()"); }
    }

    private void submitdata() throws JSONException {

        JSONObject newCustomer=new JSONObject();

        newCustomer.put("Address",adr.getText().toString());
        newCustomer.put("CompanyName",compname.getText().toString());
        newCustomer.put("CompanyPanno",compPanno.getText().toString());
        newCustomer.put("EmailID",email.getText().toString());
        newCustomer.put("IMEI","");
        newCustomer.put("MobileNo",mob.getText().toString());
        newCustomer.put("Pincode",pin.getText().toString());
        newCustomer.put("UserName",uname.getText().toString());



        AndroidNetworking.post(APIConstants.Registration)
                .addJSONObjectBody(newCustomer) // posting json
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
                                //Toast.makeText(Activity_CMA_WHA.this,data1.getString("Msg"),Toast.LENGTH_SHORT).show();
                               Toast.makeText(Activity_Register.this,"Registration Successful",Toast.LENGTH_LONG).show();
//                                Intent returnIntent = new Intent();
//                                setResult(Activity.RESULT_OK,returnIntent);
                                finish();
                            }
                            else
                            {
                                Toast.makeText(Activity_Register.this,"Something went wrong !", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) { handleError(e,"submitdata()");
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(Activity_Register.this,"Network ERROR",Toast.LENGTH_LONG).show();
                        new ErrorManager(Activity_Register.this,Activity_Register.class.getName(),
                                anError.getClass().toString(),anError.getMessage(),"Network ERROR");
                    }
                });
    }

    private boolean validate() throws Exception{

        if(uname.getText().toString().equals(""))
        {
            Toast.makeText(Activity_Register.this,"Enter User Name!",Toast.LENGTH_LONG).show();
            return false;
        }
        else if(mob.getText().toString().equals(""))
        {
            Toast.makeText(Activity_Register.this,"Enter Contact No",Toast.LENGTH_LONG).show();
            return false;
        }
        else if(mob.getText().toString().length()!=10)
        {
            Toast.makeText(Activity_Register.this,"Invalid Contact No !",Toast.LENGTH_LONG).show();
            return false;
        }
        else if(email.getText().toString().equals(""))
        {
            Toast.makeText(Activity_Register.this,"Enter Email",Toast.LENGTH_LONG).show();
            return false;
        }
        else if(adr.getText().toString().equals(""))
        {
            Toast.makeText(Activity_Register.this,"Enter Address",Toast.LENGTH_LONG).show();
            return false;
        }
        else if(pin.getText().toString().equals(""))
        {
            Toast.makeText(Activity_Register.this,"Enter Pincode",Toast.LENGTH_LONG).show();
            return false;
        }
        else if(pin.getText().toString().length()!=6)
        {
            Toast.makeText(Activity_Register.this,"Enter Correct Pincode",Toast.LENGTH_LONG).show();
            return false;
        }
        else if(compname.getText().toString().equals(""))
        {
            Toast.makeText(Activity_Register.this,"Enter Company Name",Toast.LENGTH_LONG).show();
            return false;
        }
        else if((compPanno.getText().toString().equals("")) || compPanno.getText().toString().length()!=10)
        {
            Toast.makeText(Activity_Register.this,"PAN No Must be 10 Digits",Toast.LENGTH_LONG).show();
            return false;
        }
    return true;
    }
    void handleError(Exception e,String loc){
        new ErrorManager(Activity_Register.this,Activity_Register.class.getName(),
                e.getClass().toString(),e.getMessage(),loc);
    }
}