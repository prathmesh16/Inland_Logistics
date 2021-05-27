package com.iwlpl.connectme.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;

import com.iwlpl.connectme.DBHelper;
import com.iwlpl.connectme.R;
import com.iwlpl.connectme.adapter.Adapter_Notification;
import com.iwlpl.connectme.data_handler.DataNotification;
import com.iwlpl.connectme.errorHandler.ErrorManager;

public class Activity_Notification extends AppCompatActivity {
    private DBHelper mydb;
    ArrayList<DataNotification> dataNotifications;
    RecyclerView rv;
    Adapter_Notification adapter_notification;
    ProgressBar progressBar;
    ImageView backbrn;
    LinearLayout NoNotificationLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
        setContentView(R.layout.activity__notification);
        mydb=new DBHelper(this);
        backbrn = findViewById(R.id.btnBackNotification);
        progressBar=findViewById(R.id.notification_progressbar);
        NoNotificationLayout=findViewById(R.id.no_notification_layout);
        rv=findViewById(R.id.notification_rv);
        dataNotifications=mydb.getAllNotifications();

        if (dataNotifications.size()==0)
        {
            progressBar.setVisibility(View.INVISIBLE);
            NoNotificationLayout.setVisibility(View.VISIBLE);
            rv.setVisibility(View.INVISIBLE);
        }else {
            NoNotificationLayout.setVisibility(View.GONE);
            rv.setVisibility(View.VISIBLE);
        }


        Collections.reverse(dataNotifications);


        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter_notification=new Adapter_Notification(Activity_Notification.this,dataNotifications);
        progressBar.setVisibility(View.INVISIBLE);
        rv.setAdapter(adapter_notification);


        backbrn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
        }catch (Exception e) { handleError(e,"onCreate()"); }
    }

    void handleError(Exception e,String loc){
        new ErrorManager(Activity_Notification.this,Activity_Notification.class.getName(),
                e.getClass().toString(),e.getMessage(),loc);
    }
}
