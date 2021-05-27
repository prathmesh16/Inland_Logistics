package com.iwlpl.connectme.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.iwlpl.connectme.API.APIConstants;
import com.iwlpl.connectme.R;
import com.iwlpl.connectme.activity.Activity_Navigation;
import com.iwlpl.connectme.activity.Activity_Track;
import com.iwlpl.connectme.activity.Activity_pickup_request;
import com.iwlpl.connectme.data_handler.DataPickupRequest;
import com.iwlpl.connectme.errorHandler.ErrorManager;

import static android.content.Context.MODE_PRIVATE;

public class Adapter_PickupRequest extends RecyclerView.Adapter<Adapter_PickupRequest.MyViewHolder> {

    Context context;
    ArrayList<DataPickupRequest> dataMasters;
    String masterName;


    public Adapter_PickupRequest(Context c , ArrayList<DataPickupRequest> dm,String from) {

        dataMasters = dm;
        context = c;
        masterName = from;
    }

    @NonNull
    @Override
    public Adapter_PickupRequest.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.cv_pickup_master,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_PickupRequest.MyViewHolder holder, final int position) {
        try {

        holder.PA.setText("PA  : "+dataMasters.get(position).getPickup_Address());
        holder.DA.setText("DA  : "+dataMasters.get(position).getReceiver_Address());
        holder.Date.setText("Date  : "+dataMasters.get(position).getPickup_Date());
        holder.Quantity.setText("Quantity  : "+dataMasters.get(position).getQuantity());
        holder.Weight.setText("Weight  : "+dataMasters.get(position).getWeight());
        holder.CancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //dialog
               final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.custom_dialog_ui);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCancelable(false);
                Window window = dialog.getWindow();
                window.setGravity(Gravity.CENTER);
                window.getAttributes().windowAnimations = R.style.DialogAnimation;
                final WindowManager.LayoutParams params = window.getAttributes();
                params.height = WindowManager.LayoutParams.WRAP_CONTENT;
                params.width = WindowManager.LayoutParams.MATCH_PARENT;
                window.setAttributes(params);
                dialog.show();
                //initializations
                TextView title = dialog.findViewById(R.id.dialog_title);
                TextView msg = dialog.findViewById(R.id.dialog_msg);
                Button positive = dialog.findViewById(R.id.dialog_button_positive);
                Button negative = dialog.findViewById(R.id.dialog_button_negative);
                ImageView icon = dialog.findViewById(R.id.dialog_icon);

                icon.setImageDrawable(context.getResources().getDrawable(R.drawable.delete_icon));
                title.setText("CANCEL PICKUP REQUEST");
                msg.setText("Do you surely want to cancel pickup request ?");

                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //do delete stuff
                        cancelRequest(dataMasters.get(position).getId());
                    }
                });

                negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

            }
        });

        holder.onClick(position);

    } catch (Exception e) { handleError(e,"Adapter_PickupRequest");}
    }

    private void cancelRequest(String id) {

        AndroidNetworking.initialize(context);
        SharedPreferences userDetails= context.getSharedPreferences("userDetails",MODE_PRIVATE);
        String uid =  userDetails.getString("UserID","");
        String token = userDetails.getString("TokenNo","");


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ID", id);
            jsonObject.put("TokenNo",token);
            jsonObject.put("UserId",uid);
        } catch (JSONException e) { handleError(e,"Adapter_PickupRequest: cancelRequest()");
            e.printStackTrace();
        }

        AndroidNetworking.post(APIConstants.PickupRequestCancel)
                    .addJSONObjectBody(jsonObject) // posting json
                    .setTag("test")
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONObject data1 = null;

                            data1=response;

                            try {
                                if(data1.getBoolean("IsSuccess")) {
                                    Log.e("MSg",data1.getString("Msg"));
                                    Toast.makeText(context,data1.getString("Msg"),Toast.LENGTH_LONG).show();
                                    // TODO 1: GOTO DAHSBOARd

                                    Intent intent = new Intent(context, Activity_Navigation.class);
                                    context.startActivity(intent);
                                }
                            } catch (JSONException e) { handleError(e,"Adapter_PickupRequest: cancelRequest()");
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            new ErrorManager(context,Activity_Navigation.class.getName(),
                                    anError.getClass().toString(),anError.getMessage(),"Network ERROR (Adapter_PickupRequest)");
                            Log.e("error",anError.toString());
                        }
                    });

        }


    @Override
    public int getItemCount() {
        return dataMasters.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder
    {

        TextView PA,DA,Date,Quantity,Weight;
        ConstraintLayout cvLayout;
        ImageView CancelBtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);  try {

            PA = itemView.findViewById(R.id.PA);
            DA = itemView.findViewById(R.id.DA);
            Date = itemView.findViewById(R.id.Date);
            Quantity = itemView.findViewById(R.id.Quantity);
            Weight = itemView.findViewById(R.id.Weight);
            cvLayout = itemView.findViewById(R.id.cvPickupCardMain);
            CancelBtn=itemView.findViewById(R.id.cancel_button);

        } catch (Exception e) { handleError(e,"Adapter_PickupRequest");}
        }

        public void onClick(final int position) {

            cvLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { try {
                        //update pickup request activity
                    Intent intent = new Intent(context, Activity_pickup_request.class);
                    intent.putExtra("previous","pickup_details");
                    intent.putExtra("pickup_adr_id",dataMasters.get(position).getPickup_AddressID().trim());
                    intent.putExtra("del_adr_id",dataMasters.get(position).getReceiver_AddressID().trim());

                    intent.putExtra("requested_by",dataMasters.get(position).getRequested_By().trim());
                    intent.putExtra("sender_name",dataMasters.get(position).getSender_ContactName());
                    intent.putExtra("sender_no",dataMasters.get(position).getSender_ContactMobileNo());

                    intent.putExtra("receiver_name",dataMasters.get(position).getReceiver_ContactName());
                    intent.putExtra("receiver_no",dataMasters.get(position).getReceiver_ContactMobileNo());

                    intent.putExtra("weight",dataMasters.get(position).getWeight());
                    intent.putExtra("quantity",dataMasters.get(position).getQuantity());
                    intent.putExtra("value",dataMasters.get(position).getDeclareValue());
                    intent.putExtra("remark",dataMasters.get(position).getRemark());

                    intent.putExtra("loadType",dataMasters.get(position).getLoadType());

                    intent.putExtra("productName",dataMasters.get(position).getProductname());

                    intent.putExtra("pickUpDateTime",dataMasters.get(position).getPickup_Date());

                    intent.putExtra("innoviceNo",dataMasters.get(position).getInvoiceNo());
                    intent.putExtra("saidContains",dataMasters.get(position).getSaidContains());
                    intent.putExtra("requestID",dataMasters.get(position).getRequestID());
                    context.startActivity(intent);

                } catch (Exception e) { handleError(e,"Adapter_PickupRequest: cvLayout.onClick()");}
                }
            });

        }



    }
    void handleError(Exception e,String loc){
        new ErrorManager(context,loc,
                e.getClass().toString(),e.getMessage(),loc);
    }
}

