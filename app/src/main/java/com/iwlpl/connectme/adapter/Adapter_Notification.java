package com.iwlpl.connectme.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import com.iwlpl.connectme.R;
import com.iwlpl.connectme.activity.Activity_DD_Report;
import com.iwlpl.connectme.activity.Activity_Navigation;
import com.iwlpl.connectme.activity.Activity_PickupMaster;
import com.iwlpl.connectme.data_handler.DataNotification;
import com.iwlpl.connectme.errorHandler.ErrorManager;

public class Adapter_Notification extends RecyclerView.Adapter<Adapter_Notification.MyViewHolder> {

    Context context;
    ArrayList<DataNotification> dataMasters;


    public Adapter_Notification(Context c , ArrayList<DataNotification> dm) {

        dataMasters = dm;
        context = c;

    }

    @NonNull
    @Override
    public Adapter_Notification.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.cv_notification,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Notification.MyViewHolder holder, final int position) {
        try {

        holder.title.setText(dataMasters.get(position).getTitle());
        holder.time.setText(dataMasters.get(position).getTime());
        holder.msg.setText(dataMasters.get(position).getMsg());

        holder.notiCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { try {
                if(dataMasters.get(position).getTitle().equalsIgnoreCase("Pickup Request"))
                {
                    Intent intent = new Intent(context, Activity_PickupMaster.class);
                    context.startActivity(intent);
                }
                else if(dataMasters.get(position).getTitle().equalsIgnoreCase("Door Delivery Request"))
                {
                    Intent intent = new Intent(context, Activity_DD_Report.class);
                    context.startActivity(intent);
                }

            } catch (Exception e) { handleError(e,"Adapter_Notification: notiCard.onClick()");}
            }
        });

    } catch (Exception e) { handleError(e,"Adapter_Notification");}
    }

    @Override
    public int getItemCount() {
        return dataMasters.size();
    }

    public void filterList(ArrayList<DataNotification> filteredList)
    {
        dataMasters = filteredList;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {

        TextView title,time,msg;

        CardView notiCard;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        try {

            title = itemView.findViewById(R.id.title);
            time = itemView.findViewById(R.id.time);
            msg = itemView.findViewById(R.id.msg);
            notiCard = itemView.findViewById(R.id.notificationCard);

        } catch (Exception e) { handleError(e,"Adapter_Notification");}
        }



    }

    void handleError(Exception e,String loc){
        new ErrorManager(context, loc,
                e.getClass().toString(),e.getMessage(),loc);
    }
}

