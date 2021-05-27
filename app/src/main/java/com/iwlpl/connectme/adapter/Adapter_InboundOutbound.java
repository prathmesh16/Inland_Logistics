package com.iwlpl.connectme.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.iwlpl.connectme.R;
import com.iwlpl.connectme.activity.Activity_Navigation;
import com.iwlpl.connectme.activity.Activity_Track;
import com.iwlpl.connectme.data_handler.DataInboundOutbound;
import com.iwlpl.connectme.errorHandler.ErrorManager;

import java.util.ArrayList;

public class Adapter_InboundOutbound extends RecyclerView.Adapter<Adapter_InboundOutbound.MyViewHolder> {

    Context context;
    ArrayList<DataInboundOutbound> dataMasters;
    String masterName;


    public Adapter_InboundOutbound(Context c , ArrayList<DataInboundOutbound> dm, String from) {

        dataMasters = dm;
        context = c;
        masterName = from;
    }

    @NonNull
    @Override
    public Adapter_InboundOutbound.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.cv_inboundoutbound_master,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_InboundOutbound.MyViewHolder holder, final int position) {
        try {

        holder.docketNo.setText(""+ dataMasters.get(position).getDocketNo());
        holder.date.setText(""+dataMasters.get(position).getDate());
        holder.sender.setText("Sender : "+dataMasters.get(position).getConsignor());
        holder.receiver.setText("Receiver : "+dataMasters.get(position).getConsignee());
        holder.from.setText("From : "+dataMasters.get(position).getSource());
        holder.to.setText("To : "+dataMasters.get(position).getDestination());
        holder.onClick(position);

        } catch (Exception e) { handleError(e,"Adapter_InboundOutbound");}
    }


    @Override
    public int getItemCount() {
        return dataMasters.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder
    {

        TextView docketNo,sender,receiver,from,to,date;
        ConstraintLayout cvLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            try {
            docketNo = itemView.findViewById(R.id.tvDDNo);
            date = itemView.findViewById(R.id.tvDDdate);
            sender = itemView.findViewById(R.id.tvDDSenderName);
            receiver = itemView.findViewById(R.id.tvDDReceiverName);
            from = itemView.findViewById(R.id.tvDDFrom);
            to = itemView.findViewById(R.id.tvDDTo);
            cvLayout = itemView.findViewById(R.id.cv_inout);

            } catch (Exception e) { handleError(e,"Adapter_InboundOutbound");}
        }

        public void onClick(final int position) {

            cvLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { try {
                        //update pickup request activity
                    Intent intent = new Intent(context, Activity_Track.class);
                    intent.putExtra("docket_no",dataMasters.get(position).getDocketNo().trim());
                    context.startActivity(intent);

                } catch (Exception e) { handleError(e,"Adapter_InboundOutbound: cvLayout.onClick()");}
                }
            });

        }



    }
    void handleError(Exception e,String loc){
        new ErrorManager(context, Activity_Navigation.class.getName(),
                e.getClass().toString(),e.getMessage(),loc);
    }
}

