package com.iwlpl.connectme.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.androidnetworking.AndroidNetworking;
import com.google.android.material.tabs.TabLayout;

import com.iwlpl.connectme.R;
import com.iwlpl.connectme.adapter.TabsAdapter;
import com.iwlpl.connectme.errorHandler.ErrorManager;
import com.iwlpl.connectme.fragment.PickupMasterListFragment;

public class Activity_PickupMaster extends AppCompatActivity  implements PickupMasterListFragment.setCntListner{

    TabsAdapter tabsAdapter;
    ViewPager viewPager;
    TabLayout tabLayout;
    TextView tab1Text,tab2Text;
    ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
        setContentView(R.layout.activity__pickup_master);

        AndroidNetworking.initialize(getApplicationContext());

        back = findViewById(R.id.btnBackPickup);
        viewPager=findViewById(R.id.viewpager);
        tabLayout=findViewById(R.id.tab_layout);
        tab1Text =tabLayout.getTabAt(0).getCustomView().findViewById(R.id.tv);
        tab2Text =tabLayout.getTabAt(1).getCustomView().findViewById(R.id.tv);

        tabsAdapter=new TabsAdapter(getSupportFragmentManager(),tabLayout.getTabCount(),this);
        viewPager.setAdapter(tabsAdapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) { try {
                viewPager.setCurrentItem(tab.getPosition());
                int pos=tab.getPosition();
                if (pos==0)
                {
                    tab1Text.setTextColor(getResources().getColor(R.color.colorPrimary));
                    tab2Text.setTextColor(getResources().getColor(R.color.heading));
                }
                else if (pos==1)
                {
                    tab1Text.setTextColor(getResources().getColor(R.color.heading));
                    tab2Text.setTextColor(getResources().getColor(R.color.colorPrimary));
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
        }catch (Exception e) { handleError(e,"onCreate()"); }
    }
    @Override
    public void setCntListner(int cnt1,int cnt2) {
        tab1Text.setText("INCOMING ("+cnt1+")");
        tab2Text.setText("OUTGOING ("+cnt2+")");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AndroidNetworking.cancelAll();
    }
    void handleError(Exception e,String loc){
        new ErrorManager(Activity_PickupMaster.this,Activity_PickupMaster.class.getName(),
                e.getClass().toString(),e.getMessage(),loc);
    }
}
