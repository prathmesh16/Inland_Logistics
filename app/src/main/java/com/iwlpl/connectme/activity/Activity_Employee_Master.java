package com.iwlpl.connectme.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.iwlpl.connectme.API.APIConstants;
import com.iwlpl.connectme.R;
import com.iwlpl.connectme.adapter.Adapter_Emp_Master;
import com.iwlpl.connectme.data_handler.DataEmployee;
import com.iwlpl.connectme.errorHandler.ErrorManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Activity_Employee_Master extends AppCompatActivity {

    ImageView floatingActionButton;
    ImageButton backBtn;
    RecyclerView recyclerView;
    SharedPreferences userDetails;
    ProgressBar progressBar;

    DataEmployee dataEmployee;
    ArrayList<DataEmployee> list;
    Adapter_Emp_Master adapter_emp_master;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
        setContentView(R.layout.activity__employee__master);
        AndroidNetworking.initialize(getApplicationContext());


        backBtn = findViewById(R.id.backBtnActivity);
        floatingActionButton = findViewById(R.id.floatingActionButton);
        progressBar = findViewById(R.id.progressBarEmpMaster);
        recyclerView = findViewById(R.id.rvEmployeeList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        userDetails=getSharedPreferences("userDetails",MODE_PRIVATE);
        
        fetchEmployeeDetails();



        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Employee_Master.this,Activity_Employee.class);
                intent.putExtra("previous","add");
               // startActivity(intent);
                startActivityForResult(intent,1);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        }catch (Exception e) { handleError(e,"onCreate()"); }
    }

    private void fetchEmployeeDetails() {
        list = new ArrayList<>();

        final JSONObject object = new JSONObject();
        try {
            //input your API parameters
            object.put("CustomerCode",userDetails.getString("CustomerCode",""));
            object.put("TokenNo",userDetails.getString("TokenNo",""));
            object.put("UserId",userDetails.getString("UserID",""));
        } catch (JSONException e) { handleError(e,"fetchEmployeeDetails()");
            e.printStackTrace();
        }

        AndroidNetworking.post(APIConstants.EmployeeList)
                .addJSONObjectBody(object) // posting json
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            JSONArray   data = new JSONArray(response.toString());

                            JSONObject  WHA1=data.getJSONObject(0);
                            for(int i=0;i<WHA1.getJSONArray("CustomerContactsList").length();i++)
                            {

                                JSONObject obj =WHA1.getJSONArray("CustomerContactsList").getJSONObject(i);
                                dataEmployee = new DataEmployee(obj.getString("ContactName"),obj.getString("ID"),obj.getString("DesignationName"),obj.getString("Level"),obj.getString("MobileNo")
                                ,obj.getString("Phone"),obj.getString("DepartmentName"),obj.getString("EmailID"),obj.getString("Related"),obj.toString());

                                list.add(dataEmployee);
                            }

                        } catch (JSONException e) { handleError(e,"fetchEmployeeDetails()");
                            e.printStackTrace();
                        }
                        adapter_emp_master = new Adapter_Emp_Master(Activity_Employee_Master.this,list);
                        recyclerView.setAdapter(adapter_emp_master);
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("Error : ",anError.getMessage());
                        new ErrorManager(Activity_Employee_Master.this,Activity_Employee_Master.class.getName(),
                                anError.getClass().toString(),anError.getMessage(),"Network ERROR");
                        //Toast.makeText(Activity_Master.this, "Connection Error", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Employee_Master.this);
                        builder.setTitle("Network Error");
                        builder.setMessage("Please Check Network Connection")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                      finish();                                            }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_CANCELED) {
            //Write your code if there's no result
        }
        else
        {
            //Log.e("Fetch","again");
            fetchEmployeeDetails();
        }
    }

    void handleError(Exception e,String loc){
        new ErrorManager(Activity_Employee_Master.this,Activity_Employee_Master.class.getName(),
                e.getClass().toString(),e.getMessage(),loc);
    }
}