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

import java.util.ArrayList;

import com.iwlpl.connectme.R;
import com.iwlpl.connectme.activity.Activity_Navigation;
import com.iwlpl.connectme.data_handler.DataProuduct;
import com.iwlpl.connectme.errorHandler.ErrorManager;

public class AdaptorProduct extends RecyclerView.Adapter<AdaptorProduct.MyViewHolder> implements Filterable {

    Context context;
    ArrayList<DataProuduct> dataMasters;

    ArrayList<DataProuduct> dataMasterso;
    ArrayList<DataProuduct> listFiltered;
    String oldSeq="";

    public interface AdapterCallbackProduct{
        void selectedProduct(int position);
    }

    AdapterCallbackProduct callback;


    public AdaptorProduct(Context c , ArrayList<DataProuduct> dm, AdapterCallbackProduct callback) {

        dataMasters = dm;
        if (dm==null)
        {
            dataMasters=new ArrayList<DataProuduct>();
            //dataMasters.add("",""No Products");
        }

        context = c;
        dataMasterso=dm;
        this.callback=callback;

    }


    @NonNull
    @Override
    public AdaptorProduct.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.location_item, parent, false);
        return new AdaptorProduct.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdaptorProduct.MyViewHolder holder, final int position) {
    try {
        String productName=dataMasters.get(position).getProductName();
        holder.loc.setText(productName);


        if (position==dataMasters.size()-1)
            holder.divider.setVisibility(View.INVISIBLE);


        holder.loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callback!=null){

                    int i=0;
                    for (DataProuduct s:dataMasterso)
                    {
                        if(s.getProductName().equals(dataMasters.get(position).getProductName()) &&s.getProductCode().equals(dataMasters.get(position).getProductCode()) )
                        {
                            callback.selectedProduct(i);
                        }
                        i++;
                    }

                }
            }
        });




    } catch (Exception e) { handleError(e,"AdaptorProduct");}
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
                    ArrayList<DataProuduct> filteredList = new ArrayList<>();
                    for (DataProuduct row : dataMasters) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getProductName().toLowerCase().contains(charString.toLowerCase())) {
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
                listFiltered = (ArrayList<DataProuduct>) filterResults.values;
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

        } catch (Exception e) { handleError(e,"AdaptorProduct");}
        }

    }
    void handleError(Exception e,String loc){
        new ErrorManager(context,loc,
                e.getClass().toString(),e.getMessage(),loc);
    }
}

