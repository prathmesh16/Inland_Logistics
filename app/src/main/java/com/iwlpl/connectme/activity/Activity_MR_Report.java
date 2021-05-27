package com.iwlpl.connectme.activity;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import com.iwlpl.connectme.adapter.Adapter_MR_Report;
import com.iwlpl.connectme.data_handler.DataMR;
import com.iwlpl.connectme.errorHandler.ErrorManager;
import com.opencsv.CSVWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class Activity_MR_Report extends AppCompatActivity {

    private SharedPreferences userDetails;
    private ArrayList<DataMR> MRList;
    private DataMR dataMR;
    private ImageButton back;
    private RecyclerView rv;
    private Adapter_MR_Report adapter_mr_report;
    private ImageView exportAsPdf;
    TextView startDateTV,endDateTV;
    String startDate="",endDate="";
    ImageView startDateSelector,endDateSelector,XlsBtn;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
        setContentView(R.layout.activity_mr_report);
        AndroidNetworking.initialize(getApplicationContext());

        //TODO 1: Show start date and end DATE SELECT dialog , as like skyking.
        //TODO 2 : show export as pdf and excel button

        startDateSelector=findViewById(R.id.startDateSelector_mr);
        endDateSelector=findViewById(R.id.endDateSelector_mr);
        startDateTV=findViewById(R.id.startdate_mr);
        endDateTV=findViewById(R.id.endDate_mr);
        progressBar=findViewById(R.id.progressbar_mr);
        progressBar.setVisibility(View.INVISIBLE);


        userDetails = getSharedPreferences("userDetails", MODE_PRIVATE);
        exportAsPdf=findViewById(R.id.xls_btn_mr);

        final Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        startDateTV.setText(new StringBuilder().append(day).append("/")
                .append(month + 1).append("/").append(year));
        endDateTV.setText(new StringBuilder().append(day).append("/")
                .append(month + 1).append("/").append(year));
        //startDate=i2+"/"+(i1+1)+"/"+i;

        final DatePickerDialog.OnDateSetListener startDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) { try {
                startDateTV.setText(new StringBuilder().append(i2).append("/")
                        .append(i1 + 1).append("/").append(i));
                startDate=i2+"/"+(i1+1)+"/"+i;
                fetchDocketReport();
                //        date string for uploading...
                // DateToUpload=dateConversion(i,i1+1,i2);
            }catch (Exception e) { handleError(e,"DatePickerDialog() startDateListener"); }
            }
        };

        final DatePickerDialog.OnDateSetListener endDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {try {
                endDateTV.setText(new StringBuilder().append(i2).append("/")
                        .append(i1 + 1).append("/").append(i));
                endDate=i2+"/"+(i1+1)+"/"+i;
                fetchDocketReport();
                //        date string for uploading...
                // DateToUpload=dateConversion(i,i1+1,i2);
            }catch (Exception e) { handleError(e,"DatePickerDialog() endDateListener"); }
            }
        };




        startDateSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {try {
                DatePickerDialog d = new DatePickerDialog(Activity_MR_Report.this, R.style.DateDialogTheme, startDateListener, year, month, day);
                d.getDatePicker().setBackgroundColor(Color.parseColor("#ffffff"));
                d.getDatePicker().setMaxDate(System.currentTimeMillis());
                d.setCancelable(false);
                d.show();
            }catch (Exception e) { handleError(e,"startDateSelector.onClick()"); }
            }
        });

        endDateSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {try {
                DatePickerDialog d = new DatePickerDialog(Activity_MR_Report.this, R.style.DateDialogTheme, endDateListener, year, month, day);
                d.getDatePicker().setBackgroundColor(Color.parseColor("#ffffff"));
                d.getDatePicker().setMaxDate(System.currentTimeMillis());
                d.setCancelable(false);
                d.show();
            }catch (Exception e) { handleError(e,"endDateSelector.onClick()"); }
            }
        });



        exportAsPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject object = new JSONObject();
                try {
                    object.put("CustomerCode", userDetails.getString("CustomerCode", ""));
                    object.put("TokenNo", userDetails.getString("TokenNo", ""));
                    object.put("UserId", userDetails.getString("UserID", ""));

                    // TODO : TAKE DATES FROM SELECTION HERE
                    object.put("Fromdate", "01/01/2018");
                    object.put("Todate", "22/06/2020");

                } catch (JSONException e) { handleError(e,"exportAsPdf.onClick()");
                    e.printStackTrace();
                }

                AndroidNetworking.post(APIConstants.MR_Report)
                        .addJSONObjectBody(object) // posting json
                        .setTag("test")
                        .setPriority(Priority.HIGH)
                        .build()
                        .getAsJSONArray(new JSONArrayRequestListener() {
                            @Override
                            public void onResponse(JSONArray response) {
                                String FILE = Environment.getExternalStorageDirectory()+"";
                                File file = new File(FILE, "ExcelFile.csv");
                                try {

                                    file.createNewFile();
                                    CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
                                    String arrStr1[] ={"Mrdate", "Mrno", "FromLocation", "InvoiceNo","Packages","Privatemark","ReciverName","Saidtocontain"
                                    ,"SenderName","ToLocation","Weight","loadtype","paybasis"};
                                    csvWrite.writeNext(arrStr1);
                                    try {

                                        for (int i = 0; i < response.length(); i++) {

                                            JSONObject obj = response.getJSONObject(i);
                                            String arrStr[] ={obj.getString("Mrdate"), obj.getString("Mrno"), obj.getString("FromLocation"),
                                                    obj.getString("InvoiceNo"), obj.getString("Packages"), obj.getString("Privatemark"), obj.getString("ReciverName"), obj.getString("Saidtocontain")
                                                    , obj.getString("SenderName"), obj.getString("ToLocation"), obj.getString("Weight"), obj.getString("loadtype"), obj.getString("paybasis")};
                                            csvWrite.writeNext(arrStr);
                                        }
                                        // progressBar.setVisibility(View.GONE);

                                    } catch (JSONException e) { handleError(e,"exportAsPdf.onClick()");
                                        e.printStackTrace();
                                    }

                                    csvWrite.close();
                                    Toast.makeText(Activity_MR_Report.this,"Exported",Toast.LENGTH_SHORT).show();

                                }
                                catch (IOException e){ handleError(e,"exportAsPdf.onClick()");

                                    Log.e("erro",e.getMessage());


                                }


                            }

                            @Override
                            public void onError(ANError anError) {
                                Log.e("erro","1");
                                new ErrorManager(Activity_MR_Report.this,Activity_MR_Report.class.getName(),
                                        anError.getClass().toString(),anError.getMessage(),"Network ERROR");
                            }
                        });
            }
        });
        rv = findViewById(R.id.rv_mr_report);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));


        back = findViewById(R.id.bkbtndocketReportMR);
        //fetchDocketReport();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        }catch (Exception e) { handleError(e,"onCreate()"); }
    }

    private void fetchDocketReport() throws Exception{

        progressBar.setVisibility(View.VISIBLE);


        JSONObject object = new JSONObject();
        try {
            object.put("CustomerCode", userDetails.getString("CustomerCode", ""));
            object.put("TokenNo", userDetails.getString("TokenNo", ""));
            object.put("UserId", userDetails.getString("UserID", ""));

            // TODO : TAKE DATES FROM SELECTION HERE

            if (startDate!="")
            {
                object.put("Fromdate", startDate);
            }
            else
            {
                object.put("Fromdate", "01/01/2018");
            }
            if (endDate!="")
            {
                object.put("Todate", endDate);
            }
            else
            {
                object.put("Todate", "22/06/2020");
            }
//
//            object.put("Fromdate", "01/01/2018");
//            object.put("Todate", "22/06/2020");

        } catch (JSONException e) { handleError(e,"fetchDocketReport()");
            e.printStackTrace();
        }

        AndroidNetworking.post(APIConstants.MR_Report)
                .addJSONObjectBody(object) // posting json
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        MRList = new ArrayList<>();

                        try {

                            for (int i = 0; i < response.length(); i++) {

                                JSONObject obj = response.getJSONObject(i);

                               dataMR = new DataMR(obj.getString("Mrdate"), obj.getString("Mrno"), obj.getString("Mrno"), obj.getString("Mrno" +
                                       ""), obj.getString("FromLocation"),
                                        obj.getString("InvoiceNo"), obj.getString("Packages"), obj.getString("Privatemark"), obj.getString("ReciverName"), obj.getString("Saidtocontain")
                                        , obj.getString("SenderName"), obj.getString("ToLocation"), obj.getString("Weight"), obj.getString("loadtype"), obj.getString("paybasis"));
                                MRList.add(dataMR);
                            }

                            adapter_mr_report = new Adapter_MR_Report(MRList,Activity_MR_Report.this);
                            rv.setAdapter(adapter_mr_report);
                            progressBar.setVisibility(View.GONE);

                        } catch (JSONException e) {handleError(e,"fetchDocketReport() AndroidNetworking...");
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        new ErrorManager(Activity_MR_Report.this,Activity_MR_Report.class.getName(),
                                anError.getClass().toString(),anError.getMessage(),"Network ERROR");
                    }
                });


    }

    void handleError(Exception e,String loc){
        new ErrorManager(Activity_MR_Report.this,Activity_MR_Report.class.getName(),
                e.getClass().toString(),e.getMessage(),loc);
    }
}