package com.iwlpl.connectme.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iwlpl.connectme.R;
import com.iwlpl.connectme.activity.Activity_Navigation;
import com.iwlpl.connectme.activity.Activity_OSDetails;
import com.iwlpl.connectme.data_handler.DataOSDetails;
import com.iwlpl.connectme.errorHandler.ErrorManager;

import java.util.ArrayList;

public class Adapter_OS_Details  extends RecyclerView.Adapter<Adapter_OS_Details.MyViewHolder> {

    Context context;
    ArrayList<DataOSDetails> dataOSDetails;
    int total=0;
    public Adapter_OS_Details(Context context, ArrayList<DataOSDetails> dataOSDetails) {
        this.context = context;
        this.dataOSDetails = dataOSDetails;
    }

    @NonNull
    @Override
    public Adapter_OS_Details.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.cv_os_details,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final Adapter_OS_Details.MyViewHolder holder, final int position) {
    try {

        holder.Invno.setText(dataOSDetails.get(position).getInvoiceno());
        holder.Dueamt.setText("Due Amount : "+dataOSDetails.get(position).getDueamount());
        holder.Duedt.setText("Due Date : "+dataOSDetails.get(position).getDuedate());
        holder.InvAmt.setText("Inv. Amount : "+dataOSDetails.get(position).getInvoiceamount());
        holder.Invdate.setText(dataOSDetails.get(position).getInvoicedate());
        holder.PendingDays.setText("Pending Days : "+dataOSDetails.get(position).getPendingDays());
        holder.Submitdate.setText("Submit Date : "+dataOSDetails.get(position).getSubmitteddate());
        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(dataOSDetails.get(position).getChecked());

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) { try {
                if (holder.checkBox.isChecked())
                {
                    dataOSDetails.get(position).setChecked(true);
                    total+=(int)(Float.parseFloat(dataOSDetails.get(position).getDueamount()));
                    ((Activity_OSDetails) context).setAmount(total);

                }
                else
                {
                    dataOSDetails.get(position).setChecked(false);
                    total-=(int)(Float.parseFloat(dataOSDetails.get(position).getDueamount()));
                    ((Activity_OSDetails) context).setAmount(total);
                }
            } catch (Exception e) { handleError(e,"Adapter_OS_Details: checkBox.onCheckedChanged()");}
            }
        });
    } catch (Exception e) { handleError(e,"Adapter_OS_Details");}
    }

    @Override
    public int getItemCount() {
        return dataOSDetails.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView Dueamt,Duedt,InvAmt,Invdate,Invno,PendingDays,Submitdate;
        CheckBox checkBox;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        try {

            Dueamt = itemView.findViewById(R.id.cvOsDueAmt);
            Duedt = itemView.findViewById(R.id.cvOsDuedate);
            InvAmt = itemView.findViewById(R.id.cvOsInvAmt);
            Invdate = itemView.findViewById(R.id.cvOsInvoiceDate);
            Invno = itemView.findViewById(R.id.cvOsInvoiceNo);
            PendingDays = itemView.findViewById(R.id.cvOSDays);
            Submitdate = itemView.findViewById(R.id.cvOsSubmitDate);
            checkBox=itemView.findViewById(R.id.cvOSCheckbox);

        } catch (Exception e) { handleError(e,"Adapter_OS_Details");}
        }
    }
    public  interface setAmt{
         void setAmount(int Amount);
    }

    void handleError(Exception e,String loc){
        new ErrorManager(context, loc,
                e.getClass().toString(),e.getMessage(),loc);
    }
}

