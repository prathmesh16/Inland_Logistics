package com.iwlpl.connectme.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import com.iwlpl.connectme.R;
import com.iwlpl.connectme.activity.Activity_Navigation;
import com.iwlpl.connectme.data_handler.DataDoorDelivery;
import com.iwlpl.connectme.errorHandler.ErrorManager;

public class Adpater_DD_Request extends RecyclerView.Adapter<Adpater_DD_Request.MyViewHolder>   {

    Context context;
    ArrayList<DataDoorDelivery> dataDoorDelivery;
    Button ddReq;
    Integer count;


    public Adpater_DD_Request(Context c , ArrayList<DataDoorDelivery> dm, Button button) {

        dataDoorDelivery = dm;
        context = c;
        count=0;
        ddReq = button;

    }

    public Integer getSelectedCount()
    {
        return count;
    }

    @NonNull
    @Override
    public Adpater_DD_Request.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.cv_dd_request,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final Adpater_DD_Request.MyViewHolder holder, final int position) {
    try {
        holder.docketNo.setText(""+dataDoorDelivery.get(position).getDocketno());
        holder.date.setText(""+dataDoorDelivery.get(position).getDocketdate());
        holder.sender.setText("Sender : "+dataDoorDelivery.get(position).getSenderName());
        holder.receiver.setText("Receiver : "+dataDoorDelivery.get(position).getReciverName());
        holder.from.setText("From : "+dataDoorDelivery.get(position).getFromLocation());
        holder.to.setText("To : "+dataDoorDelivery.get(position).getToLocation());
        holder.packages.setText("Packages : "+dataDoorDelivery.get(position).getPackages());
        holder.weight.setText("Weight : "+dataDoorDelivery.get(position).getWeight());
        holder.contain.setText("Contains : "+dataDoorDelivery.get(position).getSaidtocontain());
        holder.freight.setText("Freight : "+dataDoorDelivery.get(position).getFreight());
        holder.ddChg.setText("DD Charges : "+dataDoorDelivery.get(position).getDDchrg());

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                //set your object's last status
                //objIncome.setSelected(isChecked);
                if(isChecked)
                {
                    holder.checkBox.setText("Selected");
                    count++;
                    ddReq.setText("Request ("+count.toString()+")");
                }
                else
                {
                    holder.checkBox.setText("Select for DD Request");
                    count--;
                    ddReq.setText("Request ("+count.toString()+")");
                }
                dataDoorDelivery.get(position).setSelected(isChecked);
                Log.e("sele",dataDoorDelivery.get(position).getDocketno());

                } catch (Exception e) { handleError(e,"Adpater_DD_Request: checkBox.onCheckedChanged()");}
            }
        });
    } catch (Exception e) { handleError(e,"Adpater_DD_Request");}
    }

    @Override
    public int getItemCount() {
        return dataDoorDelivery.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView docketNo,sender,receiver,from,to,packages,weight,contain,freight,ddChg,date;
        CheckBox checkBox;

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
            contain = itemView.findViewById(R.id.tvDDSaidcontain);
            freight = itemView.findViewById(R.id.tvDDFreight);
            ddChg = itemView.findViewById(R.id.tvDDCharges);
            checkBox = itemView.findViewById(R.id.cvDDcheckbox);

        } catch (Exception e) { handleError(e,"Adpater_DD_Request");}
        }
    }
    void handleError(Exception e,String loc){
        new ErrorManager(context,loc,
                e.getClass().toString(),e.getMessage(),loc);
    }
}
