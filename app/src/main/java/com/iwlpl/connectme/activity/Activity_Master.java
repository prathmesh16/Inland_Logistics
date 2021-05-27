package com.iwlpl.connectme.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import com.iwlpl.connectme.adapter.Adapter_Master;
import com.iwlpl.connectme.data_handler.DataMaster;
import com.iwlpl.connectme.errorHandler.ErrorManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Activity_Master extends AppCompatActivity {

    ImageView floatingActionButton;
    RecyclerView recyclerView;
    DataMaster dataMaster;
    ArrayList<DataMaster> list;
    Adapter_Master adapter_master;
    String previousActivity;
    EditText search;
    SharedPreferences userDetails;
    ProgressBar progressBar;
    String fetchString;
    TextView titleTv;
    ImageButton backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
        setContentView(R.layout.activity_master);


        AndroidNetworking.initialize(getApplicationContext());

        Intent intent = getIntent();
        previousActivity = intent.getStringExtra("activity");

        search = findViewById(R.id.etSearchMaster);
        progressBar = findViewById(R.id.progressBarMaster);
        floatingActionButton = findViewById(R.id.floatingActionButton);
        recyclerView = findViewById(R.id.rvCustomerList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userDetails=getSharedPreferences("userDetails",MODE_PRIVATE);
        titleTv=findViewById(R.id.title);
        backBtn=findViewById(R.id.backBtnActivity);


        if(previousActivity.equalsIgnoreCase("warehouse"))
        {
            fetchString = "WHA";
            titleTv.setText("Warehouse Master");
        }
        else
        {
            fetchString = "CMA";
            titleTv.setText("Customer Master");

        }

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        fetchMasterList2();


        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                filter(s.toString());

            }
        });


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {try {

                if(previousActivity.equalsIgnoreCase("warehouse"))
                {
                    Intent intent = new Intent(getApplicationContext(), Activity_CMA_WHA.class);
                    intent.putExtra("previous","warehouse_add");
                    startActivityForResult(intent,0);
                }
                else if(previousActivity.equalsIgnoreCase("customer"))
                {
                    Intent intent = new Intent(getApplicationContext(), Activity_CMA_WHA.class);
                    intent.putExtra("previous","customer_add");
                    startActivityForResult(intent,1);

                }
            }catch (Exception e) { handleError(e,"floatingActionButton().onClick"); }
            }
        });

        }catch (Exception e) { handleError(e,"onCreate()"); }
        }

        private void fetchMasterList2()
        {
            list = new ArrayList<>();

            final JSONObject object = new JSONObject();
            try {
                //input your API parameters
                object.put("CustomerCode",userDetails.getString("CustomerCode",""));
                object.put("TokenNo",userDetails.getString("TokenNo",""));
                object.put("UserId",userDetails.getString("UserID",""));
            } catch (JSONException e) { handleError(e,"fetchMasterList2()");
                e.printStackTrace();
            }

            AndroidNetworking.post(APIConstants.AddressList)
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
                                for(int i=0;i<WHA1.getJSONArray(fetchString).length();i++)
                                {

                                    JSONObject obj =WHA1.getJSONArray(fetchString).getJSONObject(i);
                                    dataMaster = new DataMaster(obj.getString("Name"),obj.getString("Address"),obj.getString("Contact_Name"),obj.getString("MobileNo"),obj.getString("AddressID"),obj.toString());

                                    list.add(dataMaster);
                                }

                            } catch (JSONException e) { handleError(e,"fetchMasterList2()");
                                e.printStackTrace();
                            }
                            adapter_master = new Adapter_Master(Activity_Master.this,list,previousActivity);
                            recyclerView.setAdapter(adapter_master);
                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(ANError anError) {
                            Log.e("Error : ",anError.getMessage());
                            new ErrorManager(Activity_Master.this,Activity_Master.class.getName(),
                                    anError.getClass().toString(),anError.getMessage(),"Network ERROR");
                            //Toast.makeText(Activity_Master.this, "Connection Error", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Master.this);
                            builder.setTitle("Network Error");
                            builder.setMessage("Please Check Network Connection")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                          finish();                                           }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    });

        }



    private void filter(String text) {
    try{
        ArrayList<DataMaster> filteredList = new ArrayList<>();

        for(DataMaster dataMaster : list)
        {

            if(dataMaster.toString().toLowerCase().contains(text.toLowerCase()))
            {

                filteredList.add(dataMaster);
            }
        }
        adapter_master.filterList(filteredList);
    }catch (Exception e) { handleError(e,"filter()"); }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onPause() {
        super.onPause();

        AndroidNetworking.cancelAll();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_CANCELED) {
            //Write your code if there's no result
        }
        else
        {
            Log.e("Fetch","again");
            fetchMasterList2();
        }
    }
    void handleError(Exception e,String loc){
        new ErrorManager(Activity_Master.this,Activity_Master.class.getName(),
                e.getClass().toString(),e.getMessage(),loc);
    }
}
