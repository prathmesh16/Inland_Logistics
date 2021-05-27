package com.iwlpl.connectme.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.iwlpl.connectme.R;
import com.iwlpl.connectme.activity.Activity_CMA_WHA;
import com.iwlpl.connectme.activity.Activity_Navigation;
import com.iwlpl.connectme.data_handler.DataMaster;
import com.iwlpl.connectme.errorHandler.ErrorManager;

public class Adapter_Master extends RecyclerView.Adapter<Adapter_Master.MyViewHolder> {

    Context context;
    ArrayList<DataMaster> dataMasters;
    String masterName;

    public Adapter_Master(Context c , ArrayList<DataMaster> dm,String from) {

        dataMasters = dm;
        context = c;
        masterName = from;
    }

    @NonNull
    @Override
    public Adapter_Master.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.cv_master,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Master.MyViewHolder holder, int position) {

        JSONObject details = null;

        try
        {
            details=new JSONObject(dataMasters.get(position).getObj());
            holder.name.setText(details.getString("Name"));
            holder.address.setText("Address  : "+details.getString("Address"));
            holder.contactNumber.setText("Contact Number  : "+details.getString("MobileNo"));
            holder.contactPerson.setText("Contact Person  : "+details.getString("Contact_Name"));

        }
        catch (JSONException e)
        { handleError(e,"Adapter_Master");
            e.printStackTrace();
        }


        holder.onClick(position);
    }

    @Override
    public int getItemCount() {
        return dataMasters.size();
    }

    public void filterList(ArrayList<DataMaster> filteredList)
    {
        dataMasters = filteredList;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {

        TextView name,address,contactPerson,contactNumber;
        ConstraintLayout cvLayout;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            try {

            name = itemView.findViewById(R.id.tvMasterName);
            address = itemView.findViewById(R.id.tvMasterAddress);
            contactPerson = itemView.findViewById(R.id.tvMasterContactPerson);
            contactNumber = itemView.findViewById(R.id.tvMasterContactNumber);
            cvLayout = itemView.findViewById(R.id.cvMasterCardMain);

            } catch (Exception e) { handleError(e,"Adapter_Master");}
        }

        public void onClick(final int position) {

            cvLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { try {
                    if(masterName.equalsIgnoreCase("warehouse"))
                    {
                        //Warehouse Master
//
                        Intent intent = new Intent(context,Activity_CMA_WHA.class);
                        intent.putExtra("previous","warehouse_details");
                        intent.putExtra("details",dataMasters.get(position).getObj());
                        ((Activity) context).startActivityForResult(intent, 1);


                    }
                    else if(masterName.equalsIgnoreCase("customer"))
                    {
                        //Customer Master
                        Intent intent = new Intent(context, Activity_CMA_WHA.class);
                        intent.putExtra("previous","customer_details");
                        intent.putExtra("details",dataMasters.get(position).getObj());
                        ((Activity) context).startActivityForResult(intent, 1);

                    }
                } catch (Exception e) { handleError(e,"Adapter_Master: cvLayout.onClick()");}
                }
            });

             }

        }
    void handleError(Exception e,String loc){
        new ErrorManager(context, loc,
                e.getClass().toString(),e.getMessage(),loc);
    }

    }

