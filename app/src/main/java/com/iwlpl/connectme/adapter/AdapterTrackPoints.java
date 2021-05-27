package com.iwlpl.connectme.adapter;


import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import com.iwlpl.connectme.R;
import com.iwlpl.connectme.activity.Activity_Navigation;
import com.iwlpl.connectme.activity.Activity_Track;
import com.iwlpl.connectme.data_handler.DataTrackPoints;
import com.iwlpl.connectme.errorHandler.ErrorManager;


public class AdapterTrackPoints extends RecyclerView.Adapter<AdapterTrackPoints.MyViewHolder> {

    Context context;
    ArrayList<DataTrackPoints> dataTrackPoints;

    public AdapterTrackPoints(Context c , ArrayList<DataTrackPoints> dm) {

        dataTrackPoints = dm;
        context = c;
   }

    @NonNull
    @Override
    public AdapterTrackPoints.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.cv_track_details,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterTrackPoints.MyViewHolder holder, int position) {
    try {
        holder.status.setText(dataTrackPoints.get(position).getStatus());
        holder.origin.setText(dataTrackPoints.get(position).getOrogin());
        holder.date.setText(dataTrackPoints.get(position).getDate());

        if (position==0){
            holder.line1.setVisibility(View.INVISIBLE);
        }

        if (position==dataTrackPoints.size()-1){
            holder.line2.setVisibility(View.INVISIBLE);
        }

        if (dataTrackPoints.get(position).getStatus().toLowerCase().contains("delivered")){
            holder.dot.setVisibility(View.INVISIBLE);
            holder.completedIV.setVisibility(View.VISIBLE);
        }
        else{
            holder.dot.setVisibility(View.VISIBLE);
            holder.completedIV.setVisibility(View.INVISIBLE);
        }
    } catch (Exception e) { handleError(e,"AdapterTrackPoints");}
    }

    @Override
    public int getItemCount() {
        return dataTrackPoints.size();
    }



    class MyViewHolder extends RecyclerView.ViewHolder
    {

       TextView status,date,origin,line1,line2,dot;
       ImageView completedIV;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);    try {

           status = itemView.findViewById(R.id.cvTrackStatus);
           date = itemView.findViewById(R.id.cvTrackDate);
           origin = itemView.findViewById(R.id.cvTrackOrigin);
           line1=itemView.findViewById(R.id.line1);
           line2=itemView.findViewById(R.id.line2);
           dot=itemView.findViewById(R.id.dot);
           completedIV=itemView.findViewById(R.id.completed_icon);

            Typeface typeface = ResourcesCompat.getFont(context, R.font.nunito_semibold);
            Typeface typeface2 = ResourcesCompat.getFont(context, R.font.nunito_bold);
            date.setTypeface(typeface);
            origin.setTypeface(typeface);
            status.setTypeface(typeface2);

        } catch (Exception e) { handleError(e,"AdapterTrackPoints");}
        }

    }

    void handleError(Exception e,String loc){
        new ErrorManager(context,loc,
                e.getClass().toString(),e.getMessage(),loc);
    }
}
