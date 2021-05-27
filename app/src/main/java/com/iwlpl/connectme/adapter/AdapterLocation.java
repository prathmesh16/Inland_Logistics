package com.iwlpl.connectme.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;

import java.util.ArrayList;

import com.iwlpl.connectme.R;
import com.iwlpl.connectme.activity.Activity_Navigation;
import com.iwlpl.connectme.errorHandler.ErrorManager;

public class AdapterLocation extends RecyclerView.Adapter<AdapterLocation.MyViewHolder> implements Filterable {

    Context context;
    ArrayList<String> dataMasters;

    ArrayList<String> dataMasterso;
    ArrayList<String> listFiltered;
    String oldSeq="";

    public interface AdapterCallback{
        void selectedLocation(int position) throws JSONException;
    }

    AdapterCallback callback;


    public AdapterLocation(Context c , ArrayList<String> dm,AdapterCallback callback) {

        dataMasters = dm;
        if (dm==null)
        {
            dataMasters=new ArrayList<String>();
            dataMasters.add("No Data Found");
        }

        context = c;
        dataMasterso=dm;
        this.callback=callback;

    }


    @NonNull
    @Override
    public AdapterLocation.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.location_item, parent, false);
        return new AdapterLocation.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterLocation.MyViewHolder holder, final int position) {
    try {
        String location=dataMasters.get(position);
        holder.loc.setText(location);
//        locationPref=context.getSharedPreferences("LocationManager",Context.MODE_PRIVATE);
//        String selected=locationPref.getString("selectedLocation","");
//        int locationTypeIndex=locationPref.getInt("locationTypeIndex",0);
//
//        if (!selected.equals("")||!selected.equals(null))
//        {
//            if (holder.loc.getText().toString().toLowerCase().equals(selected)){
//                holder.check.setVisibility(View.VISIBLE);
//            }
//            else
//                holder.check.setVisibility(View.INVISIBLE);
//        }

        if (position==dataMasters.size()-1)
            holder.divider.setVisibility(View.INVISIBLE);


        holder.loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callback!=null){
                    int i=0;
                    for (String s:dataMasterso)
                    {
                        if(s.equals(dataMasters.get(position)))
                        {
                            try {
                                callback.selectedLocation(i);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        i++;
                    }

                }
            }
        });


    } catch (Exception e) { handleError(e,"AdapterLocation");}


    }

    @Override
    public int getItemCount() {
        return dataMasters.size();
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if(oldSeq.length()>charString.length())
                    dataMasters=dataMasterso;
                if (charString.isEmpty()) {
                    listFiltered = dataMasterso;
                } else {
                    oldSeq=charString;
                    ArrayList<String> filteredList = new ArrayList<>();
                    for (String row : dataMasters) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    listFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = listFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listFiltered = (ArrayList<String>) filterResults.values;
                dataMasters=listFiltered;
                notifyDataSetChanged();
            }
        };
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView loc;
        RelativeLayout layout;
        TextView divider;
        ImageView check;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);     try {
            loc = itemView.findViewById(R.id.locationTV);
            layout=itemView.findViewById(R.id.main_loc_item);
            divider=itemView.findViewById(R.id.divider);
            check=itemView.findViewById(R.id.selected_icon);

        } catch (Exception e) { handleError(e,"AdapterLocation");}
        }

    }
    void handleError(Exception e,String loc){
        new ErrorManager(context,loc,
                e.getClass().toString(),e.getMessage(),loc);
    }
    }

