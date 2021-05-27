package com.iwlpl.connectme.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.iwlpl.connectme.API.APIConstants;
import com.iwlpl.connectme.R;
import com.iwlpl.connectme.activity.Activity_Navigation;
import com.iwlpl.connectme.activity.Activity_PickupMaster;
import com.iwlpl.connectme.adapter.Adapter_PickupRequest;
import com.iwlpl.connectme.data_handler.DataPickupRequest;
import com.iwlpl.connectme.errorHandler.ErrorManager;

import static android.content.Context.MODE_PRIVATE;

public class PickupMasterListFragment extends Fragment  {
    RecyclerView recyclerView;
    int position;
    ProgressBar progressBar;
    Adapter_PickupRequest adapter_pickupRequest;
    DataPickupRequest dataPickupRequest;
    ArrayList<DataPickupRequest> Ilist,Olist;
    SharedPreferences userDetails;
    public PickupMasterListFragment(int position)
    {
        this.position=position;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        AndroidNetworking.initialize(getContext());
        View v=inflater.inflate(R.layout.fragment_pickup_master_ui, container, false);
        userDetails=getContext().getSharedPreferences("userDetails",MODE_PRIVATE);
        recyclerView=v.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        progressBar=v.findViewById(R.id.progressbar);
        Olist= new ArrayList<>();
        Ilist=new ArrayList<>();
        final JSONObject object = new JSONObject();
        try {
            //input your API parameters
            object.put("CustomerCode",userDetails.getString("CustomerCode",""));
            object.put("TokenNo",userDetails.getString("TokenNo",""));
            object.put("UserId",userDetails.getString("UserID",""));
        } catch (JSONException e) { handleError(e,"PickupMasterListFragment");
            e.printStackTrace();
        }

        AndroidNetworking.post(APIConstants.PickupRequestList)
                .addJSONObjectBody(object) // posting json
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            JSONArray   data = new JSONArray(response.toString());

                            // JSONObject  WHA1=data.getJSONObject(0);
                            for(int i=0;i<response.length();i++)
                            {

                                JSONObject obj =response.getJSONObject(i);
                                dataPickupRequest=new DataPickupRequest(obj.getString("RequestID"),obj.getString("CustomerName"),obj.getString("DeclareValue"),obj.getString("Pickup_Address"),obj.getString("Pickup_AddressID"),obj.getString("Pickup_Date"),obj.getString("Productname"),obj.getString("Quantity"),obj.getString("Receiver_Address"),obj.getString("Receiver_AddressID"),obj.getString("Receiver_ContactMobileNo"),
                                        obj.getString("Receiver_ContactName"),obj.getString("Remark"),obj.getString("RequestID"),
                                        obj.getString("Requested_By"),obj.getString("SaidContains"),obj.getString("Sender_ContactMobileNo"),
                                        obj.getString("Sender_ContactName"),"none",obj.getString("Weight"),"(F/S)");

                                if(obj.getString("Requested_By").contains("Consignor"))
                                    Olist.add(dataPickupRequest);
                                else
                                    Ilist.add(dataPickupRequest);
                            }

                            switch (position) {
                                case 0:
                                    adapter_pickupRequest = new Adapter_PickupRequest(getContext(),Ilist,"");
                                    recyclerView.setAdapter(adapter_pickupRequest);
                                    progressBar.setVisibility(View.INVISIBLE);
                                    break;
                                case 1:
                                    adapter_pickupRequest = new Adapter_PickupRequest(getContext(),Olist,"");
                                    recyclerView.setAdapter(adapter_pickupRequest);
                                    progressBar.setVisibility(View.INVISIBLE);
                                    break;
                                case 2:
                                                ///case 2
                                    break;
                                case 3:
                                            //case 3
                                    break;
                            }
                            ((Activity_PickupMaster)getActivity()).setCntListner(Ilist.size(),Olist.size());


                        } catch (JSONException e) { handleError(e,"PickupMasterListFragment");
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.e("Error : ",anError.getMessage());
                        new ErrorManager(getContext(),"PickupMasterListFragment",
                                anError.getClass().toString(),anError.getMessage(),"Network ERROR");
                        Toast.makeText(getContext(), "Connection Error", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Network Error");
                        builder.setMessage("Please Check Network Connection")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent intent = new Intent(getContext(), Activity_Navigation.class);
                                        startActivity(intent);                                            }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                });

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
    }


    public interface setCntListner {
        void setCntListner(int cnt1,int cnt2);
    }
    void handleError(Exception e,String loc){
        new ErrorManager(getContext(),"PickupMasterListFragment",
                e.getClass().toString(),e.getMessage(),loc);
    }
}
