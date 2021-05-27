package com.iwlpl.connectme.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.iwlpl.connectme.API.APIConstants;
import com.iwlpl.connectme.DBHelper;
import com.iwlpl.connectme.R;
import com.iwlpl.connectme.adapter.Adpater_DD_Request;
import com.iwlpl.connectme.data_handler.DataDoorDelivery;
import com.iwlpl.connectme.errorHandler.ErrorManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Activity_DD_Request extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SharedPreferences userDetails;
    private ArrayList<DataDoorDelivery> list;
    private DataDoorDelivery dataDoorDelivery;
    private Adpater_DD_Request adpater_dd_request;
    private ProgressBar progressBar;
    private Button btnRequest;
    private JSONArray jsonArray;
    private String date, remark;
    private Dialog dialog;
    private DBHelper mydb;
    private ImageButton backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
        setContentView(R.layout.activity_dd_request);
        AndroidNetworking.initialize(getApplicationContext());

        mydb = new DBHelper(this);

        recyclerView = findViewById(R.id.rvDDrequest);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        userDetails = getSharedPreferences("userDetails", MODE_PRIVATE);
        progressBar = findViewById(R.id.DDprogressBar);

        btnRequest = findViewById(R.id.btnDDrequest);
        backBtn=findViewById(R.id.btnBackActivity);

//        showDialog();
        btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { try {

                // Log.e("ccc",count.toString());
                if (adpater_dd_request.getSelectedCount() > 0) {
                    showDialog();
                } else {
                    Toast.makeText(Activity_DD_Request.this, "Please Select Dockets For Request", Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e) { handleError(e,"btnRequest.onClick()"); }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        fetchDDListData();
        //btnRequest.setVisibility(View.VISIBLE);

        }catch (Exception e) { handleError(e,"onCreate()"); }
    }

    private void showDialog() throws Exception{

        //dialog creation
        dialog = new Dialog(Activity_DD_Request.this);
        dialog.setContentView(R.layout.dialog_dd_request);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.getAttributes().windowAnimations = R.style.DialogAnimation;
        final WindowManager.LayoutParams params = window.getAttributes();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);
        dialog.show();

        // intializations
        //ListView listView = dialog.findViewById(R.id.rv_dockets);
        TableLayout table = dialog.findViewById(R.id.main_table);
        table.setStretchAllColumns(true);

        final TextView date = dialog.findViewById(R.id.textView3);
        final EditText remark = dialog.findViewById(R.id.tvremark);
        LinearLayout submit = dialog.findViewById(R.id.buttonDDRequest);


        final Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);


        final DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, final int i, final int i1, final int i2) { try {
//                pickUpDate.setText(new StringBuilder().append(i2).append("/")
//                        .append(i1 + 1).append("/").append(i));

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Activity_DD_Request.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) { try{
                        date.setText(dateConversion(i, i1 + 1, i2) + " " + selectedHour + ":" + selectedMinute);

                    }catch (Exception e) { handleError(e,"onTimeSet()"); }
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Pickup Time");
                mTimePicker.show();
            }catch (Exception e) { handleError(e,"DatePickerDialog.onDateSet()"); }
            }
        };

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { try {
                DatePickerDialog d = new DatePickerDialog(Activity_DD_Request.this, R.style.DateDialogTheme, myDateListener, year, month, day);
                d.getDatePicker().setBackgroundColor(Color.parseColor("#ffffff"));
                d.getDatePicker().setMinDate(System.currentTimeMillis());
                long now = System.currentTimeMillis() - 1000;
//                        d.getDatePicker().setMaxDate((long) (now+(1000*60*60*24*5d ))); //After 5 Days from Now
                d.show();
            }catch (Exception e) { handleError(e,"date.onClick()"); }
            }
        });

        //TODO : SHOW DIALOG WITH DATE + TIME PICKER , REMARK AND SELECTED DOCKET LIST

        JSONObject dataDocket;
        jsonArray = new JSONArray();
        try {

            ArrayList<String> myStringArray = new ArrayList<String>();

            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getSelected()) {
                    dataDocket = new JSONObject();

                    //TODO : GET SELECTED DOCKET NO FROM HERE
                    Typeface typeface = ResourcesCompat.getFont(this, R.font.nunito_semibold);

                    TableRow tableRow1;
                    tableRow1 = new TableRow(getApplicationContext());
                    tableRow1.setGravity(Gravity.CENTER);
                    if (i % 2 == 0)
                        tableRow1.setBackgroundColor(getResources().getColor(R.color.white));
                    else
                        tableRow1.setBackgroundColor(getResources().getColor(R.color.faint));

                    tableRow1.setPadding(dptoPixel(0), dptoPixel(4), dptoPixel(0), dptoPixel(4));

                    TextView no = new TextView(getApplicationContext());
                    no.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    no.setText(list.get(i).getDocketno());
                    no.setGravity(Gravity.CENTER);
                    no.setTextColor(getResources().getColor(R.color.heading));
                    no.setTypeface(typeface);
                    tableRow1.addView(no);

                    TextView pckg = new TextView(getApplicationContext());
                    pckg.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    pckg.setTextColor(getResources().getColor(R.color.heading));
                    pckg.setText(list.get(i).getPackages());
                    pckg.setGravity(Gravity.CENTER);
                    pckg.setTypeface(typeface);
                    tableRow1.addView(pckg);

                    TextView wt = new TextView(getApplicationContext());
                    wt.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    wt.setTextColor(getResources().getColor(R.color.heading));
                    wt.setText(list.get(i).getWeight());
                    wt.setGravity(Gravity.CENTER);
                    wt.setTypeface(typeface);
                    tableRow1.addView(wt);

                    table.addView(tableRow1);  //add row to table


                    myStringArray.add(list.get(i).getDocketno()); //fetching string here

                    dataDocket.put("DocketNo", list.get(i).getDocketno());
                    dataDocket.put("Packages", list.get(i).getPackages());
                    dataDocket.put("Weight", list.get(i).getWeight());

                    jsonArray.put(dataDocket);
                }
            }

//            ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(Activity_DD_Request.this, android.R.layout.simple_list_item_1, myStringArray);
//            listView.setAdapter(itemsAdapter);

        } catch (JSONException e) {    handleError(e,"showDialog()");
            e.printStackTrace();
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {try {
                if (date.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(Activity_DD_Request.this, "Select Delivery Date & Time", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    submitSelectedRequest(date.getText().toString(), remark.getText().toString());
                }
            }catch (Exception e) { handleError(e,"submit.onClick()"); }
            }

        });

    }

    private int dptoPixel(int p) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, p, this.getResources().getDisplayMetrics());
    }

    private void submitSelectedRequest(String date, String remark) throws Exception{

        //TODO : GET SELECTED DOCKETS AND SEND REQUEST

        JSONObject jsonObjectMain = new JSONObject();
        try {
            jsonObjectMain.put("DeliveryDatetime", date);
            jsonObjectMain.put("Flag", "");
            jsonObjectMain.put("ID", "");
            jsonObjectMain.put("Remark", remark);

            jsonObjectMain.put("CustomerCode", userDetails.getString("CustomerCode", ""));
            jsonObjectMain.put("TokenNo", userDetails.getString("TokenNo", ""));
            jsonObjectMain.put("UserId", userDetails.getString("UserID", ""));

            jsonObjectMain.put("DocketList", jsonArray); // from show dialog method

        } catch (JSONException e) { handleError(e,"submitSelectedRequest()");
            e.printStackTrace();
        }

        AndroidNetworking.post(APIConstants.DDRequest)
                .addJSONObjectBody(jsonObjectMain) // posting json
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject data1 = null;

                        data1 = response;
                        String msg;

                        try {
                            if (data1.getBoolean("IsSuccess")) {
                                // Log.e("MSg",data1.getString("Msg"));
                                //  Toast.makeText(getApplicationContext(),data1.getString("Msg"),Toast.LENGTH_LONG).show();

                                //msg = data1.getString("Msg");
                                //notification and sqlite insertion


                                msg = data1.getString("Msg") + " Generated Successfully. Please note request id.";

                                Calendar c = Calendar.getInstance();
                                java.text.SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
                                String datetime = dateformat.format(c.getTime());

                                mydb.insertNotification("Door Delivery Request", datetime, data1.getString("Msg") + " Generated Succesfully!");
                                createNotificationChannel();
                                NotificationCompat.Builder builder2 = new NotificationCompat.Builder(Activity_DD_Request.this, "123")
                                        .setSmallIcon(R.drawable.notification_icon)
                                        .setContentTitle("Inland World Logistics")
                                        .setContentText("Door Delivery Request Generated Successfully")
                                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                                builder2.setAutoCancel(true);
                                Intent intent = new Intent(getApplicationContext(), Activity_Splash.class);
                                intent.putExtra("fromActivity","NotificationDD");
                                PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                                        intent, 0);

                                builder2.setContentIntent(contentIntent);

                                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(Activity_DD_Request.this);
                                // notificationId is a unique int for each notification that you must define
                                notificationManager.notify(12, builder2.build());


                                //TODO : goto dashnoard from here
                                AlertDialog.Builder builder = new AlertDialog.Builder(Activity_DD_Request.this);
                                builder.setMessage(msg)
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
                        } catch (Exception e) { handleError(e,"submitSelectedRequest()  AndroidNetworking.post(..");
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        new ErrorManager(Activity_DD_Request.this,Activity_DD_Request.class.getName(),
                                anError.getClass().toString(),anError.getMessage(),"Network ERROR");
                        Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_LONG).show();
                        Log.e("ERROR", anError.getMessage());
                    }
                });


    }

    private String dateConversion(int year, int month, int day) throws Exception{
        String m = String.valueOf(month);
        String d = String.valueOf(day);
        String y = String.valueOf(year);

        if (m.length() == 1) {
            m = "0" + m;
        }
        if (d.length() == 1) {
            d = "0" + d;
        }

        return d + "/" + m + "/" + y;

    }

    private void fetchDDListData() {
        list = new ArrayList<>();

        final JSONObject object = new JSONObject();
        try {
            //input your API parameters
            object.put("CustomerCode", userDetails.getString("CustomerCode", ""));
            object.put("TokenNo", userDetails.getString("TokenNo", ""));
            object.put("UserId", userDetails.getString("UserID", ""));
        } catch (JSONException e) { handleError(e,"fetchDDListData()");
            e.printStackTrace();
        }

        AndroidNetworking.post(APIConstants.DDList)
                .addJSONObjectBody(object) // posting json
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        btnRequest.setVisibility(View.VISIBLE);

                        try {

                            if (response.length() > 0) {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject obj = response.getJSONObject(i);
                                    dataDoorDelivery = new DataDoorDelivery(obj.getString("DDchrg"), obj.getString("Docketdate"), obj.getString("Docketno"), obj.getString("Doordelivery"),
                                            obj.getString("Freight"), obj.getString("FromLocation"), obj.getString("Packages"), obj.getString("ReciverName"), obj.getString("Saidtocontain"), obj.getString("SenderName"),
                                            obj.getString("ToLocation"), obj.getString("Weight"));
                                    list.add(dataDoorDelivery);
                                }

                                adpater_dd_request = new Adpater_DD_Request(Activity_DD_Request.this, list, btnRequest);
                                recyclerView.setAdapter(adpater_dd_request);
                            } else {
                                Toast.makeText(Activity_DD_Request.this, "No Data Found", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), Activity_Navigation.class);
                                startActivity(intent);
                            }
                            progressBar.setVisibility(View.GONE);


                        } catch (JSONException e) { handleError(e,"fetchDDListData() AndroidNetworking.post(...");
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("Error : ", anError.getMessage());
                        new ErrorManager(Activity_DD_Request.this,Activity_DD_Request.class.getName(),
                                anError.getClass().toString(),anError.getMessage(),"Network ERROR");
                        progressBar.setVisibility(View.GONE);
                        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_DD_Request.this);
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

    private void createNotificationChannel() throws Exception{
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Inland";
            String description = "Logistics";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("123", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    void handleError(Exception e,String loc){
        new ErrorManager(Activity_DD_Request.this,Activity_DD_Request.class.getName(),
                e.getClass().toString(),e.getMessage(),loc);
    }
}
