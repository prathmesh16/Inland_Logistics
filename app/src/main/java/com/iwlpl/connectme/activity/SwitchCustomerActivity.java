package com.iwlpl.connectme.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.iwlpl.connectme.API.APIConstants;
import com.iwlpl.connectme.R;
import com.iwlpl.connectme.adapter.Adapter_Switch_Customer;
import com.iwlpl.connectme.data_handler.CustomerList;
import com.iwlpl.connectme.errorHandler.ErrorManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SwitchCustomerActivity extends AppCompatActivity {

    private RecyclerView rvSwitchCustomer;
    private Adapter_Switch_Customer docketReport;
    ProgressBar progressBar;
    private ImageButton back;
    SharedPreferences userDetails;
    ArrayList<CustomerList> list;
    CustomerList customerList;
    ArrayList<Boolean> isCustomerSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch_customer);


        try{
            userDetails=getSharedPreferences("userDetails",MODE_PRIVATE);

            rvSwitchCustomer = findViewById(R.id.rvSwitchCustomer);
            rvSwitchCustomer.setHasFixedSize(true);
            rvSwitchCustomer.setLayoutManager(new LinearLayoutManager(this));
            progressBar=findViewById(R.id.progressbar);
            progressBar.setVisibility(View.INVISIBLE);
            back = findViewById(R.id.bkbtndocketReport);

            back = findViewById(R.id.bkbtndocketReport);

            fetchCustomerList();

            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

        }catch (Exception e) { handleError(e,"onCreate()"); }
    }



    private void fetchCustomerList() {

        String CustCode = userDetails.getString("CustomerCode","");
        list = new ArrayList<>();

        final JSONObject object = new JSONObject();
        try {
            //input your API parameters
            object.put("CustomerCode",userDetails.getString("CustomerCode",""));
            object.put("TokenNo",userDetails.getString("TokenNo",""));
            object.put("UserId",userDetails.getString("UserID",""));
        } catch (JSONException e) { handleError(e,"fetchOSDetails()");
            e.printStackTrace();
        }


        AndroidNetworking.post(APIConstants.CustomerList)
                .addJSONObjectBody(object) // posting json
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        isCustomerSelected = new ArrayList<>();
                        try {
                            JSONArray   data = new JSONArray(response.toString());

                            //Toast.makeText(SwitchCustomerActivity.this, " "+response.toString(), Toast.LENGTH_SHORT).show();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject obj = response.getJSONObject(i);
                                if(CustCode.equals(obj.getString("CustomerCode")))
                                    isCustomerSelected.add(true);
                                else isCustomerSelected.add(false);
                               customerList = new CustomerList(obj.getString("CustomerCode"), obj.getString("CustomerName"));
                                list.add(customerList);
                            }

                            docketReport = new Adapter_Switch_Customer(SwitchCustomerActivity.this,list,isCustomerSelected);
                            rvSwitchCustomer.setAdapter(docketReport);
                            progressBar.setVisibility(View.GONE);


                        } catch (JSONException e) { handleError(e,"fetchCustomerDetails() AndroidNetworking...");
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("Error : ",anError.getMessage());
                        new ErrorManager(SwitchCustomerActivity.this,Activity_OSDetails.class.getName(),
                                anError.getClass().toString(),anError.getMessage(),"Network ERROR");
                        //Toast.makeText(Activity_Master.this, "Connection Error", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        AlertDialog.Builder builder = new AlertDialog.Builder(SwitchCustomerActivity.this);
                        builder.setTitle("Network Error");
                        builder.setMessage("Please Check Network Connection")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent intent = new Intent(getApplicationContext(), Activity_Navigation.class);
                                        startActivity(intent);                                            }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                });

    }

    void handleError(Exception e,String loc){
        new ErrorManager(SwitchCustomerActivity.this,SwitchCustomerActivity.class.getName(),
                e.getClass().toString(),e.getMessage(),loc);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }
}