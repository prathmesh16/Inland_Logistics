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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.iwlpl.connectme.API.APIConstants;
import com.iwlpl.connectme.R;
import com.iwlpl.connectme.adapter.Adapter_Stocks;
import com.iwlpl.connectme.data_handler.DataStocks;
import com.iwlpl.connectme.errorHandler.ErrorManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Activity_Stocks extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SharedPreferences userDetails;
    private ArrayList<DataStocks> list;
    private DataStocks DataStocks;
    private Adapter_Stocks Adapter_Stocks;
    private ProgressBar progressBar;
    private ImageButton backBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
        setContentView(R.layout.activity__stocks);
        AndroidNetworking.initialize(getApplicationContext());
        

        recyclerView = findViewById(R.id.rv_stocks);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        userDetails = getSharedPreferences("userDetails", MODE_PRIVATE);
        progressBar = findViewById(R.id.progressBarStocks);

        backBtn=findViewById(R.id.btnBackStocks);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        fetchDDListData();
        }catch (Exception e) { handleError(e,"onCreate)"); }
    }
    public void fetchDDListData() throws Exception
    {
        list = new ArrayList<>();

        final JSONObject object = new JSONObject();
        try {
            //input your API parameters
            object.put("CustomerCode", userDetails.getString("CustomerCode", ""));
            object.put("TokenNo", userDetails.getString("TokenNo", ""));
            object.put("UserId", userDetails.getString("UserID", ""));
        } catch (JSONException e) {  handleError(e,"fetchDDListData)");
            e.printStackTrace();
        }

        AndroidNetworking.post(APIConstants.StockList)
                .addJSONObjectBody(object) // posting json
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {


                        try {

                            if (response.length() > 0) {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject obj = response.getJSONObject(i);
                                    DataStocks = new DataStocks(obj.getString("Docketno"),obj.getString("Docketdate"),obj.getString("Sender"),obj.getString("Receiver"),obj.getString("Orglocation"),obj.getString("Dstlocation"),obj.getString("packages"),obj.getString("actualwt"),obj.getString("warehousename"));
                                    list.add(DataStocks);
                                }

                                Adapter_Stocks = new Adapter_Stocks(Activity_Stocks.this, list);
                                recyclerView.setAdapter(Adapter_Stocks);
                            } else {
                                Toast.makeText(Activity_Stocks.this, "No Data Found", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), Activity_Navigation.class);
                                startActivity(intent);
                            }
                            progressBar.setVisibility(View.GONE);


                        } catch (JSONException e) { handleError(e,"fetchDDListData)");
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("Error : ", anError.getMessage());
                        new ErrorManager(Activity_Stocks.this,Activity_Stocks.class.getName(),
                                anError.getClass().toString(),anError.getMessage(),"Network ERROR");
                        progressBar.setVisibility(View.GONE);
                        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Stocks.this);
                        builder.setTitle("Network Error");
                        builder.setMessage("Please Check Network Connection")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent intent = new Intent(getApplicationContext(), Activity_Navigation.class);
                                        startActivity(intent);
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                });
    }

    void handleError(Exception e,String loc){
        new ErrorManager(Activity_Stocks.this,Activity_Stocks.class.getName(),
                e.getClass().toString(),e.getMessage(),loc);
    }
}
