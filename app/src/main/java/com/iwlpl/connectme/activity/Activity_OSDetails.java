package com.iwlpl.connectme.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.iwlpl.connectme.API.APIConstants;
import com.iwlpl.connectme.PaymentActivities.WebViewActivity;
import com.iwlpl.connectme.R;
import com.iwlpl.connectme.adapter.Adapter_OS_Details;
import com.iwlpl.connectme.data_handler.DataOSDetails;
import com.iwlpl.connectme.errorHandler.ErrorManager;
import com.iwlpl.connectme.utility.AvenuesParams;
import com.iwlpl.connectme.utility.Constants;
import com.iwlpl.connectme.utility.ServiceUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Activity_OSDetails extends AppCompatActivity implements Adapter_OS_Details.setAmt{

    private SharedPreferences userDetails;
    private ProgressBar progressBar;
    private ImageButton btnBack;
    private DataOSDetails dataOSDetails;
    private ArrayList<DataOSDetails> list;
    private Adapter_OS_Details adapter_os_details;
    private RecyclerView recyclerView;
    private TextView total;
    private FloatingActionButton payButton;
    private int totalAmount=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
        setContentView(R.layout.activity_os_details);
        total=findViewById(R.id.total);
        userDetails=getSharedPreferences("userDetails",MODE_PRIVATE);
        progressBar = findViewById(R.id.pbOsDetails);
        btnBack = findViewById(R.id.btnBackOSDetails);
        recyclerView  =findViewById(R.id.rvOsDetails);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fetchOSDetails();

        payButton=findViewById(R.id.payButton);
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Activity_OSDetails.this, WebViewActivity.class);
                totalAmount=1;
                intent.putExtra(AvenuesParams.ACCESS_CODE, ServiceUtility.chkNull(Constants.ACCESS_CODE).toString().trim());
                intent.putExtra(AvenuesParams.MERCHANT_ID, ServiceUtility.chkNull(Constants.MERCHANTID).toString().trim());
                intent.putExtra(AvenuesParams.ORDER_ID, ServiceUtility.chkNull(genOrderId()).toString().trim());
                intent.putExtra(AvenuesParams.CURRENCY, ServiceUtility.chkNull(Constants.CURRENCY).toString().trim());
                intent.putExtra(AvenuesParams.AMOUNT, ServiceUtility.chkNull(totalAmount).toString().trim());

                intent.putExtra(AvenuesParams.REDIRECT_URL, ServiceUtility.chkNull(Constants.REDIRECT_URL).toString().trim());
                intent.putExtra(AvenuesParams.CANCEL_URL, ServiceUtility.chkNull(Constants.CANCEL_URL).toString().trim());
                intent.putExtra(AvenuesParams.RSA_KEY_URL, ServiceUtility.chkNull(Constants.RSA_KEY_URL).toString().trim());

                startActivity(intent);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), Activity_Navigation.class);
//                startActivity(intent);
                finish();
            }
        });
        }catch (Exception e) { handleError(e,"onCreate()"); }
    }

    private void fetchOSDetails() {

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


        AndroidNetworking.post(APIConstants.OSDetails)
                .addJSONObjectBody(object) // posting json
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            JSONArray   data = new JSONArray(response.toString());

                            for(int i=0;i<data.length();i++)
                            {
                                JSONObject  osData=data.getJSONObject(i);

                                //TODO :  GET LIST OF MAIN DATA HERE


                                for(int j=0;j<osData.getJSONArray("BIllDetails").length();j++)
                                {
                                    //TODO : GET LIST OF INVOICE DATA HERE

                                   JSONObject obj =osData.getJSONArray("BIllDetails").getJSONObject(j);
//                                    Log.e("dockets",obj.getString("DocketNo"));

                                    dataOSDetails = new DataOSDetails(obj.getString("Dueamount"),
                                            obj.getString("Duedate"),
                                            obj.getString("Invoiceamount"),
                                            obj.getString("Invoicedate"),
                                            obj.getString("Invoiceno"),
                                            obj.getString("PendingDays"),
                                            obj.getString("Submitteddate"));

                                       list.add(dataOSDetails);
                                }
                            }
                            //TODO : ATTACH ABOVE OBJECT TO ADAPter

                           adapter_os_details = new Adapter_OS_Details(Activity_OSDetails.this,list);
                           recyclerView.setAdapter(adapter_os_details);
                            progressBar.setVisibility(View.GONE);



                        } catch (JSONException e) { handleError(e,"fetchOSDetails() AndroidNetworking...");
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("Error : ",anError.getMessage());
                        new ErrorManager(Activity_OSDetails.this,Activity_OSDetails.class.getName(),
                                anError.getClass().toString(),anError.getMessage(),"Network ERROR");
                        //Toast.makeText(Activity_Master.this, "Connection Error", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_OSDetails.this);
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


    @Override
    public void setAmount(int Amount) {
        totalAmount=Amount;
        total.setText("Total: "+Amount);
    }

    void handleError(Exception e,String loc){
        new ErrorManager(Activity_OSDetails.this,Activity_OSDetails.class.getName(),
                e.getClass().toString(),e.getMessage(),loc);
    }
    private String genOrderId()
    {
        return ServiceUtility.randInt(0, 9999999)+"";
    }
}
