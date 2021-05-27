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
import com.iwlpl.connectme.data_handler.DataStocks;
import com.iwlpl.connectme.errorHandler.ErrorManager;

import java.util.ArrayList;

public class Adapter_Stocks extends RecyclerView.Adapter<Adapter_Stocks.MyViewHolder>   {

    Context context;
    ArrayList<DataStocks> DataStocks;


    public Adapter_Stocks(Context c , ArrayList<DataStocks> dm) {

        DataStocks = dm;
        context = c;

    }


    @NonNull
    @Override
    public Adapter_Stocks.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.cv_stocks,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final Adapter_Stocks.MyViewHolder holder, final int position) {
    try {
        holder.docketNo.setText(""+DataStocks.get(position).getDocketNo());
        holder.date.setText(""+DataStocks.get(position).getDate());
        holder.sender.setText("Sender : "+DataStocks.get(position).getSender());
        holder.receiver.setText("Receiver : "+DataStocks.get(position).getReceiver());
        holder.from.setText("From : "+DataStocks.get(position).getFrom());
        holder.to.setText("To : "+DataStocks.get(position).getTo());
        holder.packages.setText("Packages : "+DataStocks.get(position).getPackages());
        holder.weight.setText("Weight : "+DataStocks.get(position).getWeight());
        holder.wha.setText("Warehouse : "+DataStocks.get(position).getWharehouse());

    } catch (Exception e) { handleError(e,"Adapter_Stocks");}
    }

    @Override
    public int getItemCount() {
        return DataStocks.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView docketNo,sender,receiver,from,to,packages,weight,date,wha;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);    try {

            docketNo = itemView.findViewById(R.id.tvDDNo);
            date = itemView.findViewById(R.id.tvDDdate);
            sender = itemView.findViewById(R.id.tvDDSenderName);
            receiver = itemView.findViewById(R.id.tvDDReceiverName);
            from = itemView.findViewById(R.id.tvDDFrom);
            to = itemView.findViewById(R.id.tvDDTo);
            packages = itemView.findViewById(R.id.tvDDPackages);
            weight = itemView.findViewById(R.id.tvTrDDWeight);
            wha = itemView.findViewById(R.id.tvWHAName);

        } catch (Exception e) { handleError(e,"Adapter_Stocks");}
        }
    }
    void handleError(Exception e,String loc){
        new ErrorManager(context,loc,
                e.getClass().toString(),e.getMessage(),loc);
    }
}
