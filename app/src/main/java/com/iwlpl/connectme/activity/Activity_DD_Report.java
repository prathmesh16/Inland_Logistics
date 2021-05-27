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

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.iwlpl.connectme.API.APIConstants;
import com.iwlpl.connectme.R;
import com.iwlpl.connectme.adapter.Adpater_DD_Report;
import com.iwlpl.connectme.data_handler.DataDDList;
import com.iwlpl.connectme.errorHandler.ErrorManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Activity_DD_Report extends AppCompatActivity {

    private SharedPreferences userDetails;
    private DataDDList dataDDList;
    private Adpater_DD_Report adpater_dd_report;
    ArrayList<DataDDList> list;
    RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
        setContentView(R.layout.activity_dd_report);

        userDetails=getSharedPreferences("userDetails",MODE_PRIVATE);

        progressBar = findViewById(R.id.progressBarDDReport);
        back = findViewById(R.id.btnBackDDRequest);

        recyclerView = findViewById(R.id.rv_dd_report);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fetch_DD_RequestList_Data();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        }catch (Exception e) { handleError(e,"onCreate()"); }
    }

    private void fetch_DD_RequestList_Data() throws Exception{

        list = new ArrayList<>();

        final JSONObject object = new JSONObject();
        try {
            //input your API parameters
            object.put("CustomerCode",userDetails.getString("CustomerCode",""));
            object.put("TokenNo",userDetails.getString("TokenNo",""));
            object.put("UserId",userDetails.getString("UserID",""));

        } catch (JSONException e) { handleError(e,"fetch_DD_RequestList_Data()");
            e.printStackTrace();
        }

        AndroidNetworking.post(APIConstants.DDRequestList)
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
                                JSONObject  ddData=data.getJSONObject(i);

                                int size = Integer.parseInt(ddData.getString("TotDocket"));

                                //TODO : GET MAIN DOCKET DATA HERE
                                Log.e("request date",ddData.getString("DeliveryDatetime"));

                                String wt[] = new String[size];
                                String qn[] = new String[size];
                                String dok[] = new String[size];


                                for(int j=0;j<ddData.getJSONArray("DocketList").length();j++)
                                {
                                    //TODO : GET LIST OF DOCKET DATA HERE

                                    JSONObject obj =ddData.getJSONArray("DocketList").getJSONObject(j);
                                    Log.e("dockets",obj.getString("DocketNo"));
                                    wt[j] = obj.getString("DocketNo");
                                    qn[j] = obj.getString("Packages");
                                    dok[j] = obj.getString("Weight");

                                }

                                dataDDList = new DataDDList(ddData.getString("DeliveryDatetime"),
                                        ddData.getString("TotDocket"),
                                        ddData.getString("TotPackages"),
                                        ddData.getString("TotWeight"),
                                        dok,qn,wt);

                                list.add(dataDDList);
                            }
                            //TODO : ATTACH ABOVE OBJECT TO ADAPter

                            adpater_dd_report = new Adpater_DD_Report(Activity_DD_Report.this,list);
                            recyclerView.setAdapter(adpater_dd_report);
                            progressBar.setVisibility(View.GONE);



                        } catch (JSONException e) {  handleError(e,"fetch_DD_RequestList_Data() AndroidNetworking.post(..");
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("Error : ",anError.getMessage());
                        new ErrorManager(Activity_DD_Report.this,Activity_DD_Report.class.getName(),
                                anError.getClass().toString(),anError.getMessage(),"Network ERROR");
                        //Toast.makeText(Activity_Master.this, "Connection Error", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_DD_Report.this);
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
        new ErrorManager(Activity_DD_Report.this,Activity_DD_Report.class.getName(),
                e.getClass().toString(),e.getMessage(),loc);
    }
}
