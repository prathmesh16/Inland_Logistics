package com.iwlpl.connectme.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.resources.TextAppearance;
import com.iwlpl.connectme.R;
import com.iwlpl.connectme.activity.Activity_Navigation;
import com.iwlpl.connectme.data_handler.DataDDList;
import com.iwlpl.connectme.data_handler.DataDoorDelivery;
import com.iwlpl.connectme.errorHandler.ErrorManager;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Adpater_DD_Report extends RecyclerView.Adapter<Adpater_DD_Report.MyViewHolder>{

    Context context;
    ArrayList<DataDDList> dataDDLists;

    public Adpater_DD_Report(Context context, ArrayList<DataDDList> dataDDLists) {
        this.context = context;
        this.dataDDLists = dataDDLists;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.cv_dd_report,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
    try {
        //holder.docketNo.setText(""+dataDoorDelivery.get(position).getDocketno());
        TableRow tableRow1 = null;
        TableRow tableRow2 = null;

        holder.date.append(dataDDLists.get(position).getDeliveryDatetime());

        ///getting array here\

        String w[] = dataDDLists.get(position).getWeightList();
        String q[] = dataDDLists.get(position).getQuantityList();
        String d[] = dataDDLists.get(position).getDocketList();

        Log.e("c", String.valueOf(w.length));

        Typeface typeface = ResourcesCompat.getFont(context, R.font.nunito_semibold);

        for(int i= 0 ; i< w.length;i++)
        {



            tableRow1 = new TableRow( context);
            tableRow1.setGravity(Gravity.CENTER);
            if (i % 2 == 0)
                tableRow1.setBackgroundColor(context.getResources().getColor(R.color.white));
            else
                tableRow1.setBackgroundColor(context.getResources().getColor(R.color.faint));

            tableRow1.setPadding(dptoPixel(0), dptoPixel(4), dptoPixel(0), dptoPixel(4));

            TextView rowdata3 = new TextView(context);
            rowdata3.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            rowdata3.setText(w[i]);
            rowdata3.setGravity(Gravity.CENTER);
            rowdata3.setTextColor(context.getResources().getColor(R.color.heading));
            rowdata3.setTypeface(typeface);
            tableRow1.addView(rowdata3);

            TextView rowdata2 = new TextView(context);
            rowdata2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            rowdata2.setText(q[i]);
            rowdata2.setGravity(Gravity.CENTER);
            rowdata2.setTextColor(context.getResources().getColor(R.color.heading));
            rowdata2.setTypeface(typeface);
            tableRow1.addView(rowdata2);

            TextView rowdata = new TextView(context);
            rowdata.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            rowdata.setText(d[i]);
            rowdata.setGravity(Gravity.CENTER);
            rowdata.setTextColor(context.getResources().getColor(R.color.heading));
            rowdata.setTypeface(typeface);
            tableRow1.addView(rowdata);

            holder.table.addView(tableRow1);


        }

        tableRow2 = new TableRow(context);
        tableRow2.setGravity(Gravity.CENTER);
        tableRow2.setPadding(dptoPixel(0), dptoPixel(3), dptoPixel(0), dptoPixel(3));
        //tableRow2.setBackgroundColor(context.getResources().getColor(R.color.green));
        tableRow2.setBackground(context.getResources().getDrawable(R.drawable.table_footer_bg));


        TextView rowData3 = new TextView(context);
        rowData3.setTypeface(typeface);
        rowData3.setText("Total");
        rowData3.setGravity(Gravity.CENTER);
        rowData3.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        rowData3.setTextColor(context.getResources().getColor(R.color.black));
        tableRow2.addView(rowData3);

        TextView rowData2 = new TextView(context);
        rowData2.setTypeface(typeface);
        rowData2.setText(dataDDLists.get(position).getQuantityCount());
        rowData2.setGravity(Gravity.CENTER);
        rowData2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        rowData2.setTextColor(context.getResources().getColor(R.color.black));
        tableRow2.addView(rowData2);

        TextView rowData = new TextView(context);
        rowData.setTypeface(typeface);
        rowData.setText(dataDDLists.get(position).getWeightCount());
        rowData.setGravity(Gravity.CENTER);
        rowData.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        rowData.setTextColor(context.getResources().getColor(R.color.black));
        tableRow2.addView(rowData);

        holder.table.addView(tableRow2);  //total of weight and quantity row added here


        //table.addView(tableRow1);
    } catch (Exception e) { handleError(e,"Adpater_DD_Report");}
    }

    @Override
    public int getItemCount() {
        return dataDDLists.size();
    }

    private int dptoPixel(int p) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, p, this.context.getResources().getDisplayMetrics());
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TableLayout table;
        TextView date;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);     try {

            date = itemView.findViewById(R.id.tvDate_DD_report);
            table = itemView.findViewById(R.id.dd_report_tbl);
            table.setStretchAllColumns(true);

        } catch (Exception e) { handleError(e,"Adpater_DD_Report");}
        }
    }
    void handleError(Exception e,String loc){
        new ErrorManager(context,loc,
                e.getClass().toString(),e.getMessage(),loc);
    }
}
