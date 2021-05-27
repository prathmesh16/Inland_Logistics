package com.iwlpl.connectme.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.iwlpl.connectme.R;
import com.iwlpl.connectme.activity.Activity_Navigation;
import com.iwlpl.connectme.errorHandler.ErrorManager;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterVH> {

    private Context context;
    private List<Integer> mainList;

    public SliderAdapter(Context context, List<Integer> imagesList) {
        this.context = context;
        this.mainList=imagesList;
    }



    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_adapter, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(final SliderAdapterVH viewHolder, int position) {
    try {
       //Picasso.with(context.getApplicationContext()).load(mainList.get(position)).into(viewHolder.imageViewBackground);
        Picasso.get().load(mainList.get(position)).into(viewHolder.imageViewBackground);

    } catch (Exception e) { handleError(e,"SliderAdapter");}
    }

    @Override
    public int getCount() {
        return mainList.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageViewBackground;


        public SliderAdapterVH(final View itemView) {
            super(itemView);     try {
             imageViewBackground = itemView.findViewById(R.id.iv);

             this.itemView = itemView;
            } catch (Exception e) { handleError(e,"SliderAdapter");}
        }
    }
    void handleError(Exception e,String loc){
        new ErrorManager(context,loc,
                e.getClass().toString(),e.getMessage(),loc);
    }
}