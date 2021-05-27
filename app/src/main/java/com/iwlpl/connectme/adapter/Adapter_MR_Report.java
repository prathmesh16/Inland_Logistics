package com.iwlpl.connectme.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iwlpl.connectme.R;
import com.iwlpl.connectme.activity.Activity_Navigation;
import com.iwlpl.connectme.data_handler.DataMR;
import com.iwlpl.connectme.errorHandler.ErrorManager;

import java.util.ArrayList;

public class Adapter_MR_Report extends RecyclerView.Adapter<Adapter_MR_Report.MyViewHolder> {

    ArrayList<DataMR> dataDockets;
    Context context;

    public Adapter_MR_Report(ArrayList<DataMR> dataDockets, Context context) {
        this.dataDockets = dataDockets;
        this.context = context;
    }

    @NonNull
    @Override
    public Adapter_MR_Report.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.cv_docket_report,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_MR_Report.MyViewHolder holder, int position) {
        try {

        holder.no.setText(dataDockets.get(position).getMrno());
        holder.date.setText(dataDockets.get(position).getMrdate()
        );
        // holder.sender.setText(dataDockets.get(position).getSenderName());
        holder.rec.setText(dataDockets.get(position).getReciverName());
        holder.from.setText(dataDockets.get(position).getFromLocation());
        holder.to.setText(dataDockets.get(position).getToLocation());

    } catch (Exception e) { handleError(e,"Adapter_MR_Report");}
    }

    @Override
    public int getItemCount() {
        return dataDockets.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView no,date,sender,rec,from,to;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            try {

            no = itemView.findViewById(R.id.docket_no);
            date = itemView.findViewById(R.id.booking_date);
            //sender = itemView.findViewById(R.id.sender);

            rec = itemView.findViewById(R.id.receiver);

            from = itemView.findViewById(R.id.src);
            to = itemView.findViewById(R.id.destination);

        } catch (Exception e) { handleError(e,"Adapter_MR_Report");}
        }
    }
    void handleError(Exception e,String loc){
        new ErrorManager(context, loc,
                e.getClass().toString(),e.getMessage(),loc);
    }
}
