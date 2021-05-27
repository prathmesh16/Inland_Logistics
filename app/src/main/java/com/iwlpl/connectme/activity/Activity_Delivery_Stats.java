package com.iwlpl.connectme.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.iwlpl.connectme.API.APIConstants;
import com.iwlpl.connectme.R;
import com.iwlpl.connectme.errorHandler.ErrorManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Activity_Delivery_Stats extends AppCompatActivity {

    private ImageButton back;
    private SharedPreferences userDetails;
    private JSONObject bkg_15;
    private JSONObject bkg_30;
    private JSONObject bkg_60;
    private JSONObject bkg_90;
    private ProgressBar progressBar;
    private LineChart lineChart;
    private PieChart pieChart;
    private Button btn_15,btn_30,btn_60,btn_90;
    private TextView day_tv;
    XAxis xAxis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
        setContentView(R.layout.activity__delivery__stats);
        AndroidNetworking.initialize(getApplicationContext());

        userDetails = getSharedPreferences("userDetails", MODE_PRIVATE);
        back = findViewById(R.id.backBtn_del);
        progressBar = findViewById(R.id.pb_Stats);
        lineChart = findViewById(R.id.linechart1);
        pieChart = findViewById(R.id.halfpie1);
        day_tv = findViewById(R.id.title);

        btn_15 = findViewById(R.id.day_15);
        btn_30 = findViewById(R.id.day_30);
        btn_60 = findViewById(R.id.day_60);
        btn_90 = findViewById(R.id.day_90);

        fetchDeliveryStats();


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    day_tv.setText("Delivery Stats - 15 Days");
                    intializeLineChart(15);
                    intializePieChart(15);

                } catch (JSONException e) { handleError(e,"btn_15.onClick()");
                    e.printStackTrace();
                }

            }
        });
        btn_30.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    day_tv.setText("Delivery Stats - 30 Days");

                    intializeLineChart(30);
                    intializePieChart(30);

                } catch (JSONException e) { handleError(e,"btn_30.onClick()");
                    e.printStackTrace();
                }

            }
        });
        btn_60.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    day_tv.setText("Delivery Stats - 60 Days");

                    intializeLineChart(60);
                    intializePieChart(60);

                } catch (JSONException e) { handleError(e,"btn_60.onClick()");
                    e.printStackTrace();
                }

            }
        });
        btn_90.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    day_tv.setText("Delivery Stats - 90 Days");
                    intializeLineChart(90);
                    intializePieChart(90);

                } catch (JSONException e) { handleError(e,"btn_90.onClick()");
                    e.printStackTrace();
                }

            }
        });



        }catch (Exception e) { handleError(e,"onCreate()"); }
    }

    private void intializeLineChart(int days) throws JSONException {

        LineDataSet dataSet = new LineDataSet(datavalues(days),"Deliveries");
        dataSet.setFillAlpha(100);
        dataSet.setColor(Color.RED);
        dataSet.setLineWidth(3f);
        dataSet.setValueTextSize(15f);
        dataSet.setValueTextColor(Color.BLACK);

        ArrayList<LineDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSet);

        LineData lineData = new LineData(dataSet);
        lineChart.setDragEnabled(true);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.setDrawGridBackground(true);
        lineChart.invalidate();

        lineChart.setData(lineData);

        String vals[]={"","On Time","Next Day","2 Days"};

        xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new XAxisValueFormatter(vals));
        xAxis.setGranularity(1);
        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        xAxis.setDrawGridLines(true);
        xAxis.setDrawGridLinesBehindData(false);

    }

    private ArrayList<Entry> datavalues(int days) throws JSONException {

        ArrayList<Entry> dataVals = new ArrayList<Entry>();

        //int fif =

        dataVals.add(new Entry(0,0));

        if(days==15)
        {
            dataVals.add(new Entry(1,Float.parseFloat(bkg_15.getString("Ontime"))));
            dataVals.add(new Entry(2,Float.parseFloat(bkg_15.getString("NextDay"))));
            dataVals.add(new Entry(3,Float.parseFloat(bkg_15.getString("greaterthan2day"))));
        }
        else if(days==30)
        {
            dataVals.add(new Entry(1,Float.parseFloat(bkg_30.getString("Ontime"))));
            dataVals.add(new Entry(2,Float.parseFloat(bkg_30.getString("NextDay"))));
            dataVals.add(new Entry(3,Float.parseFloat(bkg_30.getString("greaterthan2day"))));
        }
        else if(days==60)
        {
            dataVals.add(new Entry(1,Float.parseFloat(bkg_60.getString("Ontime"))));
            dataVals.add(new Entry(2,Float.parseFloat(bkg_60.getString("NextDay"))));
            dataVals.add(new Entry(3,Float.parseFloat(bkg_60.getString("greaterthan2day"))));
        }
        else if(days==90)
        {
            dataVals.add(new Entry(1,Float.parseFloat(bkg_90.getString("Ontime"))));
            dataVals.add(new Entry(2,Float.parseFloat(bkg_90.getString("NextDay"))));
            dataVals.add(new Entry(3,Float.parseFloat(bkg_90.getString("greaterthan2day"))));
        }

        return dataVals;
    }

    public class XAxisValueFormatter extends ValueFormatter {

        private String[] mVal;

        public XAxisValueFormatter(String[] values)
        {
            this.mVal=values;
        }

        @Override
        public String getFormattedValue(float value) {
            return mVal[(int)value];
        }

    }

    private void fetchDeliveryStats() {
        JSONObject object = new JSONObject();
        try {
            object.put("CustomerCode", userDetails.getString("CustomerCode", ""));
            object.put("TokenNo", userDetails.getString("TokenNo", ""));
            object.put("UserId", userDetails.getString("UserID", ""));
        } catch (JSONException e) { handleError(e,"fetchDeliveryStats()");
            e.printStackTrace();
        }

        AndroidNetworking.post(APIConstants.Delivery_Chart)
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

                            bkg_15 = WHA1.getJSONArray("Servicelevel").getJSONObject(0);
                            bkg_30 = WHA1.getJSONArray("Servicelevel").getJSONObject(1);
                            bkg_60 = WHA1.getJSONArray("Servicelevel").getJSONObject(2);
                            bkg_90 = WHA1.getJSONArray("Servicelevel").getJSONObject(3);

                            Log.e("3days", bkg_30.toString());


                        } catch (JSONException e) { handleError(e,"fetchDeliveryStats() AndroidNetworking.post(..");
                            e.printStackTrace();
                        }
                        progressBar.setVisibility(View.GONE);
                        try {
                            intializeLineChart(15);
                            intializePieChart(15);
                        } catch (JSONException e) { handleError(e,"intializeLineChart()");
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("Error : ",anError.getMessage());
                        new ErrorManager(Activity_Delivery_Stats.this,Activity_Delivery_Stats.class.getName(),
                                anError.getClass().toString(),anError.getMessage(),"Network ERROR");
                        //Toast.makeText(Activity_Master.this, "Connection Error", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Delivery_Stats.this);
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

    private void intializePieChart(int i) throws JSONException {

        pieChart.setBackgroundColor(Color.WHITE);
        pieChart.setUsePercentValues(false);
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setMaxAngle(180);
        pieChart.setRotationAngle(180);
        pieChart.setCenterTextOffset(0,-20);
        pieChart.animateY(1000, Easing.EaseInOutCubic);
        pieChart.setHoleRadius(50f);//18f
        pieChart.setCenterTextSize(13);//30
        pieChart.setRotationEnabled(false);
        pieChart.setExtraOffsets(5f,0f,5f,-200f);
        pieChart.getLegend().setEnabled(false);

        setPieData(i);
    }

    private void setPieData(int days) throws JSONException {

        ArrayList<PieEntry> pieEntries = new ArrayList<>();



        if(days==15)
        {
            Float ontime_per = Float.parseFloat(bkg_15.getString("Ontime"))/Float.parseFloat(bkg_15.getString("TotalShipment"))*100;
            Float remaining = Float.parseFloat(bkg_15.getString("NextDay"))/Float.parseFloat(bkg_15.getString("TotalShipment"))*100+
                    Float.parseFloat(bkg_15.getString("greaterthan2day"))/Float.parseFloat(bkg_15.getString("TotalShipment"))*100;

            pieEntries.add(new PieEntry(ontime_per,"On Time"));
            pieEntries.add(new PieEntry(remaining,"Delay"));

            DecimalFormat df = new DecimalFormat("#.#");
            pieChart.setCenterText(df.format(ontime_per)+"% Deliveries On Time");//new line

            //xAxis.setAxisMaximum(Float.parseFloat(bkg_15.getString("TotalShipment")));
            //lineChart.setVisibleXRangeMaximum();
        }
        else if(days==30)
        {
            Float ontime_per = Float.parseFloat(bkg_30.getString("Ontime"))/Float.parseFloat(bkg_30.getString("TotalShipment"))*100;
            Float remaining = Float.parseFloat(bkg_30.getString("NextDay"))/Float.parseFloat(bkg_30.getString("TotalShipment"))*100+
                    Float.parseFloat(bkg_30.getString("greaterthan2day"))/Float.parseFloat(bkg_30.getString("TotalShipment"))*100;

            pieEntries.add(new PieEntry(ontime_per,"On Time"));
            pieEntries.add(new PieEntry(remaining,"Delay"));

            DecimalFormat df = new DecimalFormat("#.#");
            pieChart.setCenterText(df.format(ontime_per)+"% Deliveries On Time");//new line

            //xAxis.setAxisMaximum(Float.parseFloat(bkg_30.getString("TotalShipment")));

        }
        else if(days==60)
        {
            Float ontime_per = Float.parseFloat(bkg_60.getString("Ontime"))/Float.parseFloat(bkg_60.getString("TotalShipment"))*100;
            Float remaining = Float.parseFloat(bkg_60.getString("NextDay"))/Float.parseFloat(bkg_60.getString("TotalShipment"))*100+
                    Float.parseFloat(bkg_60.getString("greaterthan2day"))/Float.parseFloat(bkg_60.getString("TotalShipment"))*100;

           // ontime_per.floatValue();

            DecimalFormat df = new DecimalFormat("#.#");
            //System.out.print(df.format(d));

            pieEntries.add(new PieEntry(ontime_per,"On Time"));
            pieEntries.add(new PieEntry(remaining,"Delay"));

            pieChart.setCenterText(df.format(ontime_per)+"% Deliveries On Time");//new line
            //xAxis.setAxisMaximum(Float.parseFloat(bkg_60.getString("TotalShipment")));

        }
        else if(days==90)
        {
            Float ontime_per = Float.parseFloat(bkg_90.getString("Ontime"))/Float.parseFloat(bkg_90.getString("TotalShipment"))*100;
            Float remaining = Float.parseFloat(bkg_90.getString("NextDay"))/Float.parseFloat(bkg_90.getString("TotalShipment"))*100+
                    Float.parseFloat(bkg_90.getString("greaterthan2day"))/Float.parseFloat(bkg_90.getString("TotalShipment"))*100;

            pieEntries.add(new PieEntry(ontime_per,"On Time"));
            pieEntries.add(new PieEntry(remaining,"Delay"));

            DecimalFormat df = new DecimalFormat("#.#");

            pieChart.setCenterText(df.format(ontime_per)+"% Deliveries On Time");//new line
            //xAxis.setAxisMaximum(Float.parseFloat(bkg_90.getString("TotalShipment")));

        }
//        pieEntries.add(new PieEntry(Float.parseFloat(bkg_15.getString("Ontime_per")),"On Time"));
//        pieEntries.add(new PieEntry(Float.parseFloat(bkg_15.getString("NextDay_per"))+Float.parseFloat(bkg_15.getString("GraterThen2day_per")),"Delay"));
      //  pieEntries.add(new PieEntry(Float.parseFloat(bkg_15.getString("GraterThen2day_per")),"2 Days"));

        PieDataSet dataSet = new PieDataSet(pieEntries,"Deliveries");
        dataSet.setSelectionShift(0f);
        dataSet.setSliceSpace(3f);
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);


        PieData data = new PieData(dataSet);
        data.setValueTextSize(15f);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setHighlightPerTapEnabled(true);

        Legend l = pieChart.getLegend();
        l.setWordWrapEnabled(true);

        pieChart.invalidate();

    }

    void handleError(Exception e,String loc){
        new ErrorManager(Activity_Delivery_Stats.this,Activity_Delivery_Stats.class.getName(),
                e.getClass().toString(),e.getMessage(),loc);
    }
}