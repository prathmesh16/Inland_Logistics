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
import com.iwlpl.connectme.API.APIConstants;
import com.iwlpl.connectme.R;
import com.iwlpl.connectme.activity.Activity_InboundOutbound;
import com.iwlpl.connectme.activity.Activity_Navigation;
import com.iwlpl.connectme.activity.Activity_Track;
import com.iwlpl.connectme.adapter.Adapter_InboundOutbound;
import com.iwlpl.connectme.data_handler.DataInboundOutbound;
import com.iwlpl.connectme.errorHandler.ErrorManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class InboundOutboundMasterListFragment extends Fragment  {
    RecyclerView recyclerView;
    int position;
    ProgressBar progressBar;
    Adapter_InboundOutbound Adapter_InboundOutbound;
    DataInboundOutbound DataInboundOutbound;
    ArrayList<DataInboundOutbound> Ilist,Olist;
    SharedPreferences userDetails;
    public InboundOutboundMasterListFragment(int position)
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
        } catch (JSONException e) { handleError(e,"InboundOutboundMasterListFragment");
            e.printStackTrace();
        }

        AndroidNetworking.post(APIConstants.InboundOutboundList)
                .addJSONObjectBody(object) // posting json
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            JSONArray   data = new JSONArray(response.toString());
                            //Log.e("data",data.toString());
                           // Toast.makeText(getContext(),"in",Toast.LENGTH_SHORT).show();
                            JSONObject object1= new JSONObject(response.getJSONObject(0).toString());
                            JSONArray In = object1.getJSONArray("In_Bound");
                            JSONArray Out = object1.getJSONArray("Out_Bound");
                            // JSONObject  WHA1=data.getJSONObject(0);
//                            for(int i=0;i<response.length();i++)
//                            {
//
//                                JSONObject obj =response.getJSONObject(i);
//                                DataInboundOutbound=new DataInboundOutbound(obj.getString("DocketNo"),obj.getString("Origin"),obj.getString("Destination"),obj.getString("ConsigneeName"),obj.getString("ConsignorName"));
//
//                                if(obj.getString("Requested_By").contains("Consignor"))
//                                    Olist.add(DataInboundOutbound);
//                                else
//                                    Ilist.add(DataInboundOutbound);
//                            }
                         ////   Log.e("In",In.toString());
                            Log.e("Out",Out.toString());
                            for(int i=0;i<In.length();i++)
                            {

                                JSONObject obj =In.getJSONObject(i);
                                DataInboundOutbound=new DataInboundOutbound(obj.getString("DocketNo"),obj.getString("Origin"),obj.getString("Destination"),obj.getString("ConsigneeName"),obj.getString("ConsignorName"),obj.getString("DocketDate"));

                                    Ilist.add(DataInboundOutbound);
                            }
                            for(int i=0;i<Out.length();i++)
                            {

                                JSONObject obj =Out.getJSONObject(i);
                                DataInboundOutbound=new DataInboundOutbound(obj.getString("DocketNo"),obj.getString("Origin"),obj.getString("Destination"),obj.getString("ConsigneeName"),obj.getString("ConsignorName"),obj.getString("DocketDate"));

                                    Olist.add(DataInboundOutbound);

                            }
                            switch (position) {
                                case 0:
                                    Adapter_InboundOutbound = new Adapter_InboundOutbound(getContext(),Ilist,"");
                                    recyclerView.setAdapter(Adapter_InboundOutbound);
                                    progressBar.setVisibility(View.INVISIBLE);
                                    break;
                                case 1:
                                    Adapter_InboundOutbound = new Adapter_InboundOutbound(getContext(),Olist,"");
                                    recyclerView.setAdapter(Adapter_InboundOutbound);
                                    progressBar.setVisibility(View.INVISIBLE);
                                    break;
                                case 2:
                                                ///case 2
                                    break;
                                case 3:
                                            //case 3
                                    break;
                            }
                            ((Activity_InboundOutbound)getActivity()).setCntListner(Ilist.size(),Olist.size());


                        } catch (JSONException e) { handleError(e,"InboundOutboundMasterListFragment");
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.e("Error : ",anError.getMessage());
                        new ErrorManager(getContext(),"InboundOutboundMasterListFragment",
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
        void setCntListner(int cnt1, int cnt2);
    }
    void handleError(Exception e,String loc){
        new ErrorManager(getContext(),"InboundOutboundMasterListFragment",
                e.getClass().toString(),e.getMessage(),loc);
    }
}
