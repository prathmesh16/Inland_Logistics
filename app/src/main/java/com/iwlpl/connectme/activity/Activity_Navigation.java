package com.iwlpl.connectme.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.iwlpl.connectme.API.APIConstants;
import com.iwlpl.connectme.R;
import com.iwlpl.connectme.adapter.SliderAdapter;
import com.iwlpl.connectme.errorHandler.ErrorManager;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Activity_Navigation extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //navigation
    ImageView menuBtn;
    DrawerLayout drawer;
    NavigationView navigationView;
    private long backpresstime;
    ImageView navigateProfileBtn;
    int DashType;
    TextView VersionTv;
    TextView headerProfileText, headerProfileUserName;
    ImageView notificationBtn;

    // Order and Finance
    CardView AddCustomerCard, PickupRequestCard, WarehouseCard, SampleCard, DoorDeliveryCard, DDreportCard,IOcard,StocksCard,OrderStatsCard;
    CardView EmployeeMasterCard,PendingInvoiceCard,DocketReportCard,MrReportCard,FinanceStatsCard;
    SliderAdapter sliderAdapter;
    SliderView sliderView;
    EditText docketNo;
    TextView heading;
    List<Integer> ImageList;
    TextView orderOption, financeOption;
    ImageView graphOption;
    ConstraintLayout OrderCardsLayout, FinanceCardsLayout;
    TabLayout tabLayout;

    //Profile
    TextView mobile, username, email, id, profileText;
    SharedPreferences userDetails;
    ImageView logoutBtn;
    ImageView updateEmailBtn;
    TextView updateMobileBtn;
    TextView updateAddressBtn;
    Button changePass;
    TextView version;
    String updatedMobile="";

    RequestQueue queue;


    //graphs
    //private SharedPreferences userDetails;
    private JSONObject bkg_15;
    private JSONObject bkg_30;
    private JSONObject bkg_60;
    private JSONObject bkg_90;
    private ProgressBar progressBar;
    private BarChart lineChart;
   // private PieChart pieChart;
    private Button btn_15,btn_30,btn_60,btn_90;
    private TextView day_tv;
    XAxis xAxis;
    ImageView stats_menu_btn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
        setContentView(R.layout.activity_navigation);

        closeKeyboard();
        //fullscreen
        Window window = getWindow();
        WindowManager.LayoutParams winParams = window.getAttributes();
        winParams.flags &= ~WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        window.setAttributes(winParams);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        userDetails = getSharedPreferences("userDetails", MODE_PRIVATE);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        menuBtn = findViewById(R.id.drawer_toggle);
        notificationBtn = findViewById(R.id.notification_btn);


        navigationView.bringToFront();
        navigationView.setCheckedItem(R.id.nav_dashboard);
        navigationView.setNavigationItemSelectedListener(this);

        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyboard();
                if (drawer.isDrawerVisible(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else
                    drawer.openDrawer(GravityCompat.START);
            }
        });


        //navigation header components
        headerProfileText = navigationView.getHeaderView(0).findViewById(R.id.profile_text);
        headerProfileUserName = navigationView.getHeaderView(0).findViewById(R.id.profile_name);
        navigateProfileBtn = navigationView.getHeaderView(0).findViewById(R.id.header_next_btn);

        navigateProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { try {
                closeKeyboard();
                navigationView.setCheckedItem(R.id.nav_profile);
                drawer.closeDrawer(GravityCompat.START);
                notificationBtn.setVisibility(View.INVISIBLE);
                changeDashboard(3);
                DashType = 3;
            }catch (Exception e) { handleError(e,"navigateProfileBtn"); }
            }
        });


        String name = userDetails.getString("UserName", "");
        headerProfileUserName.setText(name);
        headerProfileText.setText(name.substring(0, 1));

        //version
        VersionTv = navigationView.findViewById(R.id.tvVersion);
        try {
            PackageInfo pInfo = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            VersionTv.setText("Version - " + version);

        } catch (PackageManager.NameNotFoundException e) { handleError(e,"onCreate()");
            e.printStackTrace();
        }

        //notification access

        notificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { try {
                closeKeyboard();
                drawer.closeDrawer(GravityCompat.START);
                startActivity(new Intent(Activity_Navigation.this, Activity_Notification.class));
            }catch (Exception e) { handleError(e,"notificationBtn.onClick()"); }
            }
        });

        service();


        //orders and finance dashboard
        queue = Volley.newRequestQueue(this);

        heading = findViewById(R.id.heading_label);
        docketNo = findViewById(R.id.etDocketNo);
        tabLayout = findViewById(R.id.tab_layout);
        OrderCardsLayout = findViewById(R.id.order_cards_layout);
        FinanceCardsLayout = findViewById(R.id.finance_cards_layout);


        OrderCardsLayout = findViewById(R.id.order_cards_layout);
        FinanceCardsLayout =findViewById(R.id.finance_cards_layout);
        WarehouseCard = findViewById(R.id.warehouse_card);
        PickupRequestCard =findViewById(R.id.pickup_request_card);
        AddCustomerCard = findViewById(R.id.customer_card);
        SampleCard = findViewById(R.id.pickup_report_card);
        DoorDeliveryCard =findViewById(R.id.door_delivery_card);
        DDreportCard = findViewById(R.id.dd_report_card);
        IOcard=findViewById(R.id.inbound_outbond_card);
        StocksCard=findViewById(R.id.stocks_card);
        OrderStatsCard=findViewById(R.id.order_stats_card);
        OrderStatsCard.setVisibility(View.GONE);  //visiblility chnaged here
//        finance cards
        PendingInvoiceCard = findViewById(R.id.pending_invoice_card);
        EmployeeMasterCard=findViewById(R.id.employee_master_card);
        MrReportCard=findViewById(R.id.MR_report_card);
        DocketReportCard=findViewById(R.id.docket_report_card);
        FinanceStatsCard=findViewById(R.id.finance_stats_card);
        FinanceStatsCard.setVisibility(View.GONE);  //visiblility chnaged here


        orderOption = tabLayout.getTabAt(1).getCustomView().findViewById(R.id.tv);
        financeOption = tabLayout.getTabAt(2).getCustomView().findViewById(R.id.tv);
        graphOption=tabLayout.getTabAt(0).getCustomView().findViewById(R.id.iv);
        heading.setText("DASHBOARD");


        docketNo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // code to execute when EditText loses focus
                }
            }
        });

        docketNo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) { try {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;


                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    if (event.getRawX() >= (docketNo.getRight() - docketNo.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

                        if (docketNo.getText().toString().isEmpty()) {
                            Toast.makeText(Activity_Navigation.this, "Please Enter Docket/Ref No. !", Toast.LENGTH_SHORT).show();
                            return false;

                        } else {
                            Intent intent = new Intent(Activity_Navigation.this, Activity_Track.class);
                            intent.putExtra("docket_no", docketNo.getText().toString());
                            startActivity(intent);
                            return true;
                        }

                    }

                }
            }catch (Exception e) { handleError(e,"docketNo.onTouch()"); }
                return false;
            }
        });

        //order cards actions

        AddCustomerCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {try {
                Intent intent = new Intent(Activity_Navigation.this, Activity_Master.class);
                intent.putExtra("activity", "customer");
                startActivity(intent);

            }catch (Exception e) { handleError(e,"AddCustomerCard.onClick()"); }
            }
        });
        WarehouseCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {try {
                Intent intent = new Intent(Activity_Navigation.this, Activity_Master.class);
                intent.putExtra("activity", "warehouse");
                startActivity(intent);

            }catch (Exception e) { handleError(e,"WarehouseCard.onClick()"); }
            }
        });
        PickupRequestCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {try {
                Intent intent = new Intent(Activity_Navigation.this, Activity_pickup_request.class);
                intent.putExtra("previous", "pickup_add");
                startActivity(intent);
            }catch (Exception e) { handleError(e,"PickupRequestCard.onClick()"); }
            }
        });
        SampleCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { try {
                startActivity(new Intent(Activity_Navigation.this, Activity_PickupMaster.class));
            }catch (Exception e) { handleError(e,"SampleCard.onClick()"); }
            }
        });

        DoorDeliveryCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {try {
                startActivity(new Intent(Activity_Navigation.this, Activity_DD_Request.class));
            }catch (Exception e) { handleError(e,"DoorDeliveryCard.onClick()"); }
            }
        });
        DDreportCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { try {
                startActivity(new Intent(Activity_Navigation.this, Activity_DD_Report.class));
                }catch (Exception e) { handleError(e,"DDreportCard.onClick()"); }
            }
        });
        StocksCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {try {
                startActivity(new Intent(Activity_Navigation.this, Activity_Stocks.class));
            }catch (Exception e) { handleError(e,"StocksCard.onClick()"); }
            }
        });
        IOcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {try {
                startActivity(new Intent(Activity_Navigation.this, Activity_InboundOutbound.class));
            }catch (Exception e) { handleError(e,"IOcard.onClick()"); }
            }
        });

        OrderStatsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { try {
                startActivity(new Intent(Activity_Navigation.this, Activity_Delivery_Stats.class));
            }catch (Exception e) { handleError(e,"OrderStatsCard.onClick()"); }
            }
        });


        //finance cards actions

        PendingInvoiceCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {try {
                Intent intent = new Intent(Activity_Navigation.this, Activity_OSDetails.class);
                startActivity(intent);
            }catch (Exception e) { handleError(e,"OrderStatsCard.onClick()"); }
            }
        });

        EmployeeMasterCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { try {
                Intent intent = new Intent(Activity_Navigation.this, Activity_Employee_Master.class);
                startActivity(intent);
            }catch (Exception e) { handleError(e,"EmployeeMasterCard.onClick()"); }
            }
        });

        DocketReportCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { try {
                Intent intent = new Intent(Activity_Navigation.this, Activity_DocketReport.class);
                startActivity(intent);
            }catch (Exception e) { handleError(e,"DocketReportCard.onClick()"); }
            }
        });

        MrReportCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { try {
                Intent intent = new Intent(Activity_Navigation.this, Activity_MR_Report.class);
                startActivity(intent);
            }catch (Exception e) { handleError(e,"MrReportCard.onClick()"); }
            }
        });


        FinanceStatsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        /////

        imageSlider();


        //switching between orders and finance
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) { try {
                int pos = tab.getPosition();
                if (pos == 0) {
                    if (DashType != 4) {
                        changeDashboard(4);
                        DashType = 4;
                    }
                    orderOption.setTextColor(getResources().getColor(R.color.heading));
                    financeOption.setTextColor(getResources().getColor(R.color.heading));
                    graphOption.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));

                } else if (pos == 1) {
                    if (DashType != 1) {
                        changeDashboard(1);
                        DashType = 1;
                    }
                    orderOption.setTextColor(getResources().getColor(R.color.colorPrimary));
                    financeOption.setTextColor(getResources().getColor(R.color.heading));
                    graphOption.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.heading)));
                } else if (pos==2){
                    if(DashType!=2){
                        changeDashboard(2);
                        DashType=2;
                    }
                    orderOption.setTextColor(getResources().getColor(R.color.heading));
                    financeOption.setTextColor(getResources().getColor(R.color.colorPrimary));
                    graphOption.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.heading)));
                }
            }catch (Exception e) { handleError(e,"tabLayout.onTabSelected()"); }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //Profile/////////////////////////////////////////////////////////////////////////////
        mobile =findViewById(R.id.mobile_no);
        username = findViewById(R.id.profile_name);
        email = findViewById(R.id.profile_email);
        id = findViewById(R.id.profile_userid);
        profileText =findViewById(R.id.profile_text);
        logoutBtn = findViewById(R.id.logout_btn);
        updateAddressBtn =findViewById(R.id.update_address_btn);
        updateEmailBtn = findViewById(R.id.update_email_btn);
        updateMobileBtn =findViewById(R.id.update_mobile_btn);
        changePass = findViewById(R.id.change_pass_btn);
        version = findViewById(R.id.tvVerProfile);

        findViewById(R.id.profile_drawer_toggle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyboard();
                if (drawer.isDrawerVisible(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else
                    drawer.openDrawer(GravityCompat.START);
            }
        });

        userDetails = getSharedPreferences("userDetails", MODE_PRIVATE);
        String uname = userDetails.getString("UserName", "");
        mobile.setText(userDetails.getString("MobileNo", ""));
        id.setText(userDetails.getString("UserID", ""));
        username.setText(uname);
        profileText.setText(uname.substring(0, 1));

        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String ver = pInfo.versionName;
            version.setText("Version - " + ver);

        } catch (PackageManager.NameNotFoundException e) {
            new ErrorManager(Activity_Navigation.this,Activity_Navigation.class.getName(),
                    e.getClass().toString(),e.getMessage()," PackageInfo pInfo ..");
            e.printStackTrace();
        }

        //change password

        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { try {
                updateDialogs("CHANGE PASSWORD", "change_pass");
            }catch (Exception e) { handleError(e,"changePass.onClick()"); }
            }

        });

        updateMobileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { try {
                updateDialogs("UPDATE MOBILE NO.","change_mobile");
            }catch (Exception e) { handleError(e,"updateMobileBtn.onClick()"); }
            }
        });


        //logout
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finishAffinity();
                System.exit(0);
            }
        });


        ////////graphs methods
        AndroidNetworking.initialize(getApplicationContext());

        progressBar = findViewById(R.id.pb_Stats);
        lineChart = findViewById(R.id.linechart1);
        //pieChart = findViewById(R.id.halfpie2);
        day_tv = findViewById(R.id.title);

        btn_15 = findViewById(R.id.day_15);
        btn_30 = findViewById(R.id.day_30);
        btn_60 = findViewById(R.id.day_60);
        btn_90 = findViewById(R.id.day_90);
      //  stats_menu_btn=findViewById(R.id.menu_btn_stats);
        fetchDeliveryStats(15);

        btn_15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    //day_tv.setText("Delivery Stats - 15 Days");

                    if(bkg_15==null)
                        fetchDeliveryStats(15);
                    else
                    intializeLineChart(15);


                  //  intializePieChart(15);    /////instializa pie chart here

                } catch (JSONException e) {handleError(e,"btn_15.onClick()");
                    e.printStackTrace();
                }

            }
        });
        btn_30.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    //day_tv.setText("Delivery Stats - 30 Days");

                    if(bkg_15==null)
                        fetchDeliveryStats(30);
                    else
                    intializeLineChart(30);
                   // intializePieChart(30);

                } catch (JSONException e) {handleError(e,"btn_30.onClick()");
                    e.printStackTrace();
                }

            }
        });
        btn_60.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                   // day_tv.setText("Delivery Stats - 60 Days");
                    if(bkg_15==null)
                        fetchDeliveryStats(60);
                    else
                        intializeLineChart(60);
                   // intializePieChart(60);

                } catch (JSONException e) {handleError(e,"btn_60.onClick()");
                    e.printStackTrace();
                }

            }
        });
        btn_90.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    //day_tv.setText("Delivery Stats - 90 Days");
                    if(bkg_15==null)
                        fetchDeliveryStats(90);
                    else
                    intializeLineChart(90);
                   // intializePieChart(90);

                } catch (JSONException e) {handleError(e,"btn_90.onClick()");
                    e.printStackTrace();
                }

            }
        });

//        stats_menu_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                closeKeyboard();
//                if (drawer.isDrawerVisible(GravityCompat.START)) {
//                    drawer.closeDrawer(GravityCompat.START);
//                } else
//                    drawer.openDrawer(GravityCompat.START);
//            }
//        });

    }catch (Exception e) { handleError(e,"onCreate()"); }
    }

    //*********navigation methods

    //menu selection handler
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        try {
        switch (id) {
            case R.id.nav_dashboard:
                closeKeyboard();
                if (DashType == 4) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    changeDashboard(4);
                    DashType = 4;
                    notificationBtn.setVisibility(View.VISIBLE);
                    tabLayout.setVisibility(View.VISIBLE);
                    drawer.closeDrawer(GravityCompat.START);
                }
                break;
            case R.id.nav_orders:
                closeKeyboard();
                if (DashType == 1) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    changeDashboard(1);
                    DashType = 1;
                    notificationBtn.setVisibility(View.VISIBLE);
                    tabLayout.setVisibility(View.VISIBLE);
                    drawer.closeDrawer(GravityCompat.START);
                }
                break;
            case R.id.nav_switch_customer:
                closeKeyboard();
                    drawer.closeDrawer(GravityCompat.START);
                   startActivity(new Intent(Activity_Navigation.this, SwitchCustomerActivity.class));

                break;

            case R.id.nav_finance:
                closeKeyboard();
                if (DashType == 2) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    changeDashboard(2);
                    DashType = 1;
                    notificationBtn.setVisibility(View.VISIBLE);
                    tabLayout.setVisibility(View.VISIBLE);
                    drawer.closeDrawer(GravityCompat.START);
                }
                break;
            case R.id.nav_profile:
                closeKeyboard();
                if (DashType == 3) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    changeDashboard(3);
                    DashType = 3;
                    notificationBtn.setVisibility(View.INVISIBLE);
                    drawer.closeDrawer(GravityCompat.START);
                }

                break;
            case R.id.nav_logout:
                Activity_Navigation.this.finishAffinity();
                System.exit(0);
                break;
        }
        }catch (Exception e) { handleError(e,"onNavigationItemSelected()"); }
        return true;
    }

    //backpress handler
    @Override
    public void onBackPressed() {
        closeKeyboard();
        if (drawer.isDrawerVisible(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (backpresstime + 2000 > System.currentTimeMillis()) {
                super.onBackPressed();
                //finish();
                finishAffinity();
            } else {
                Toast.makeText(getApplicationContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
            }
            backpresstime = System.currentTimeMillis();

        }

    }

    //method for closing soft keyboard
    private void closeKeyboard() {

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    //service
    private void service() throws Exception{
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.d("", "getInstanceId failed", task.getException());
                            Toast.makeText(Activity_Navigation.this, "getInstanceId failed " + task.getException(), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        String msg = "Token : " + token;
                        Log.d("token", msg);
                        //Toast.makeText(Activity_Navigation.this, msg, Toast.LENGTH_SHORT).show();
                    }

                });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("MyNotification", "MyNotification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        FirebaseMessaging.getInstance().subscribeToTopic("inland")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Successfull!";
                        if (!task.isSuccessful()) {
                            msg = "Failed!";
                        }
                        //.makeText(Activity_Navigation.this,msg,Toast.LENGTH_SHORT).show();
                    }
                });
    }


    //method to change dashboard view
    public void changeDashboard(int dashType) throws Exception{
        if (dashType == 1) {
            findViewById(R.id.dashboard_layout).setVisibility(View.VISIBLE);
            findViewById(R.id.profile_layout).setVisibility(View.INVISIBLE);
            findViewById(R.id.stats_layout).setVisibility(View.INVISIBLE);
            // heading.setText("ORDERS");
            tabLayout.selectTab(tabLayout.getTabAt(1));
            navigationView.setCheckedItem(R.id.nav_orders);
            OrderCardsLayout.setVisibility(View.VISIBLE);
            FinanceCardsLayout.setVisibility(View.GONE);
            tabLayout.setVisibility(View.VISIBLE);
            DashType = 1;
        } else if (dashType == 2) {
            findViewById(R.id.dashboard_layout).setVisibility(View.VISIBLE);
            findViewById(R.id.profile_layout).setVisibility(View.INVISIBLE);
            findViewById(R.id.stats_layout).setVisibility(View.INVISIBLE);
            tabLayout.selectTab(tabLayout.getTabAt(2));
            navigationView.setCheckedItem(R.id.nav_finance);
            // heading.setText("FINANCE");
            OrderCardsLayout.setVisibility(View.GONE);
            FinanceCardsLayout.setVisibility(View.VISIBLE);
            DashType=2;
        } else if (dashType == 3) {
            findViewById(R.id.dashboard_layout).setVisibility(View.INVISIBLE);
            findViewById(R.id.stats_layout).setVisibility(View.INVISIBLE);
            findViewById(R.id.profile_layout).setVisibility(View.VISIBLE);
            tabLayout.setVisibility(View.INVISIBLE);
            DashType=3;
        }
        else if(dashType==4){
            tabLayout.selectTab(tabLayout.getTabAt(0));
            findViewById(R.id.dashboard_layout).setVisibility(View.VISIBLE);
            findViewById(R.id.profile_layout).setVisibility(View.INVISIBLE);
            findViewById(R.id.stats_layout).setVisibility(View.VISIBLE);
            navigationView.setCheckedItem(R.id.nav_dashboard);
            DashType=4;
            OrderCardsLayout.setVisibility(View.GONE);
            FinanceCardsLayout.setVisibility(View.GONE);
        }
    }

    //***********dashboard methods

    //method for image slider
    private void imageSlider() throws Exception{
        ImageList = new ArrayList<>();
        ImageList.add(R.drawable.image4);
        ImageList.add(R.drawable.image3);
        ImageList.add(R.drawable.image1);
        ImageList.add(R.drawable.image2);

        sliderView = findViewById(R.id.imageSlider);
        sliderAdapter = new SliderAdapter(Activity_Navigation.this, ImageList);
        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setSliderTransformAnimation(SliderAnimations.FADETRANSFORMATION);
        sliderView.setIndicatorSelectedColor(Color.parseColor("#00000000"));
        sliderView.setIndicatorUnselectedColor(Color.parseColor("#00000000"));
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setScrollTimeInSec(4);
        sliderView.startAutoCycle();
        sliderView.getSliderPager().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
    }

    //*************profile methods

    //dialogs for updatingprofile info
    private void updateDialogs(final String titleLabel, final String type) throws Exception{
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.update_info_dialog);
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
        //initializations
        TextView title = dialog.findViewById(R.id.title_label);
        final EditText mobileET = dialog.findViewById(R.id.updated_et);
        final EditText passwordET = dialog.findViewById(R.id.updated_et_pass);
        final EditText otpET=dialog.findViewById(R.id.et_otp);
        final Button updateInfoBtn = dialog.findViewById(R.id.update_info_btn);
        final Button verifyOTPbtn = dialog.findViewById(R.id.verify_otp_btn);


        //title
        title.setText(titleLabel);

        //edit text
        if (type.equalsIgnoreCase("change_pass")) {
            mobileET.setVisibility(View.INVISIBLE);
            updateInfoBtn.setText("Update");
            verifyOTPbtn.setVisibility(View.INVISIBLE);
            otpET.setVisibility(View.INVISIBLE);
            passwordET.setVisibility(View.VISIBLE);
            updateInfoBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (passwordET.getText().toString().isEmpty()) {
                        Toast.makeText(Activity_Navigation.this, "Please Enter New Password", Toast.LENGTH_SHORT).show();
                    } else {
                        changePassword(passwordET.getText().toString());
                        dialog.dismiss();
                    }
                }
            });


        } else if (type.equalsIgnoreCase("change_mobile")) {
            updateInfoBtn.setText("SEND OTP");
            mobileET.setVisibility(View.VISIBLE);
            passwordET.setVisibility(View.INVISIBLE);
            verifyOTPbtn.setVisibility(View.INVISIBLE);
            updateInfoBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mobileET.getText().toString().isEmpty()) {
                        Toast.makeText(Activity_Navigation.this, "Please Enter New Mobile No.", Toast.LENGTH_SHORT).show();
                    } else {
                        mobileET.setVisibility(View.INVISIBLE);
                        otpET.setVisibility(View.VISIBLE);
                        updateInfoBtn.setVisibility(View.INVISIBLE);
                        verifyOTPbtn.setVisibility(View.VISIBLE);
                        updatedMobile=mobileET.getText().toString();
                        //changeMobile(mobileET.getText().toString());
                        //dialog.dismiss();
                    }
                }
            });

            //otp verify btn
            verifyOTPbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

        }

    }

    //method for updating password
    private void changePassword(final String pass) {

        final JSONObject object = new JSONObject();
        try {
            //input your API parameters
            object.put("IMEI", "");
            object.put("Password", pass);
            object.put("UserID", id.getText().toString());

        } catch (JSONException e) { handleError(e,"changePassword");
            e.printStackTrace();
        }

        AndroidNetworking.post(APIConstants.ChangePassword)
                .addJSONObjectBody(object) // posting json
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject data1 = null;

                        data1 = response;


                        try {
                            if (data1.getBoolean("IsSuccess")) {
                                Toast.makeText(Activity_Navigation.this, data1.getString("Msg"), Toast.LENGTH_SHORT).show();
                                Log.e("msg", data1.getString("Msg"));
                                userDetails = getSharedPreferences("userDetails", MODE_PRIVATE);
                                userDetails.edit().putString("Password",pass).commit();
                            } else {
                                Toast.makeText(Activity_Navigation.this, response.getString("Msg"), Toast.LENGTH_SHORT).show();
                                Log.e("msg", data1.getString("Msg"));

                            }
                        } catch (JSONException e) { handleError(e,"changePassword AndroidNetworking..");
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        new ErrorManager(Activity_Navigation.this,Activity_Navigation.class.getName(),
                                anError.getClass().toString(),anError.getMessage(),"Network ERROR");
                        Log.e("Error : ", anError.getErrorDetail());
                        Toast.makeText(Activity_Navigation.this, "Connection Error", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //update mobile method
    private void changeMobile(final String mobile) {
        final JSONObject object = new JSONObject();
        try {
            //input your API parameters
            object.put("MobileNo", mobile);
            object.put("UserID", id.getText().toString());

        } catch (JSONException e) { handleError(e,"changeMobile");
            e.printStackTrace();
        }

        AndroidNetworking.post(APIConstants.GetOTP)
                .addJSONObjectBody(object) // posting json
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject data1 = null;

                        data1 = response;


                        try {
                            if (data1.getBoolean("IsSuccess")) {
                                Toast.makeText(Activity_Navigation.this, data1.getString("Msg"), Toast.LENGTH_SHORT).show();
                                Log.e("msg", data1.getString("Msg"));

                            } else {
                                Toast.makeText(Activity_Navigation.this, response.getString("Msg"), Toast.LENGTH_SHORT).show();
                                Log.e("msg", data1.getString("Msg"));

                            }
                        } catch (JSONException e) {handleError(e,"changeMobile");
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        new ErrorManager(Activity_Navigation.this,Activity_Navigation.class.getName(),
                                anError.getClass().toString(),anError.getMessage(),"Network ERROR");
                        Log.e("Error : ", anError.getErrorDetail());
                        Toast.makeText(Activity_Navigation.this, "Connection Error", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    //graph functions and methods

    private void intializeLineChart(int days) throws JSONException {

        BarDataSet dataSet = new BarDataSet(datavalues(days),"Service Level In "+days+" Days");
        //dataSet.setFillAlpha(100);
        //dataSet.setColor(getResources().getColor(R.color.colorPrimary));
        //dataSet.setLineWidth(3f);
        dataSet.setValueTextSize(13f);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        Legend legend = lineChart.getLegend();
        legend.setTextSize(15f);


        ArrayList<BarDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSet);


        BarData lineData = new BarData(dataSet);
        lineChart.setDragEnabled(true);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.setDrawGridBackground(true);
        lineChart.getDescription().setEnabled(false);
        lineChart.setData(lineData);
       // lineChart.setExtraTopOffset(50f);
        //lineChart.setVisibleYRange();
        //lineChart.setVisibleYRangeMaximum(10, YAxis.AxisDependency.RIGHT);
        //lineChart.setViewPortOffsets(0f, 0f, 0f, 0f);

        lineChart.fitScreen();
        lineChart.invalidate();



        String vals[]={"","On Time","Next Day","2 Days"};

        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(105f);

        xAxis = lineChart.getXAxis();
        //xAxis.setAxisMinimum(0f);
        xAxis.setValueFormatter(new XAxisValueFormatter(vals));
        xAxis.setGranularity(1);
        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        xAxis.setDrawGridLines(true);
        xAxis.setDrawGridLinesBehindData(false);
        //xAxis.setAxisMinValue(150);

    }

    private ArrayList<BarEntry> datavalues(int days) throws JSONException {

        ArrayList<BarEntry> dataVals = new ArrayList<BarEntry>();

        //int fif =

      //  dataVals.add(new BarEntry(0,0));

        if(days==15)
        {
            dataVals.add(new BarEntry(1,Float.parseFloat(bkg_15.getString("Ontime_per"))));
            Log.e("On tme perce",(bkg_15.getString("Ontime_per")));
            dataVals.add(new BarEntry(2,Float.parseFloat(bkg_15.getString("NextDay_per"))));
            dataVals.add(new BarEntry(3,Float.parseFloat(bkg_15.getString("greaterthan2day_per"))));
        }
        else if(days==30)
        {
            dataVals.add(new BarEntry(1,Float.parseFloat(bkg_30.getString("Ontime_per"))));
            dataVals.add(new BarEntry(2,Float.parseFloat(bkg_30.getString("NextDay_per"))));
            dataVals.add(new BarEntry(3,Float.parseFloat(bkg_30.getString("greaterthan2day_per"))));
        }
        else if(days==60)
        {
            dataVals.add(new BarEntry(1,Float.parseFloat(bkg_60.getString("Ontime_per"))));
            dataVals.add(new BarEntry(2,Float.parseFloat(bkg_60.getString("NextDay_per"))));
            dataVals.add(new BarEntry(3,Float.parseFloat(bkg_60.getString("greaterthan2day_per"))));
        }
        else if(days==90)
        {
            dataVals.add(new BarEntry(1,Float.parseFloat(bkg_90.getString("Ontime_per"))));
            dataVals.add(new BarEntry(2,Float.parseFloat(bkg_90.getString("NextDay_per"))));
            dataVals.add(new BarEntry(3,Float.parseFloat(bkg_90.getString("greaterthan2day_per"))));
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

    private void fetchDeliveryStats(int flag) {
        progressBar.setVisibility(View.VISIBLE);
        JSONObject object = new JSONObject();
        try {
            object.put("CustomerCode", userDetails.getString("CustomerCode", ""));
            object.put("TokenNo", userDetails.getString("TokenNo", ""));
            object.put("UserId", userDetails.getString("UserID", ""));
        } catch (JSONException e) { handleError(e,"fetchDeliveryStats");
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

                            Log.e("15days", bkg_15.toString());
                            Log.e("30days", bkg_30.toString());
                            Log.e("60days", bkg_60.toString());
                            Log.e("90days", bkg_90.toString());



                        } catch (JSONException e) { handleError(e,"fetchDeliveryStats AndroidNetworking..");
                            e.printStackTrace();
                        }
                        progressBar.setVisibility(View.GONE);
                        try {
                            if(flag==15)
                            intializeLineChart(15);
                            else if(flag==30)
                                intializeLineChart(30);
                            else if(flag==60)
                                intializeLineChart(60);
                            else if(flag==90)
                                intializeLineChart(90);
                          //  intializePieChart(15);
                        } catch (JSONException e) { handleError(e,"fetchDeliveryStats AndroidNetworking..");
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("Error : ",anError.getMessage());
                        new ErrorManager(Activity_Navigation.this,Activity_Navigation.class.getName(),
                                anError.getClass().toString(),anError.getMessage(),"Network ERROR");
                        //Toast.makeText(Activity_Master.this, "Connection Error", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
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
    void handleError(Exception e,String loc){
        new ErrorManager(Activity_Navigation.this,Activity_Navigation.class.getName(),
                e.getClass().toString(),e.getMessage(),loc);
    }

    @Override
    protected void onResume() {
        super.onResume();

        drawer.close();
        userDetails = getSharedPreferences("userDetails", MODE_PRIVATE);
       }
}
