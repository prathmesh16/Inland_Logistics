package com.iwlpl.connectme.adapter;

import android.app.Activity;
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
import com.iwlpl.connectme.activity.Activity_Employee;
import com.iwlpl.connectme.activity.Activity_Navigation;
import com.iwlpl.connectme.data_handler.DataEmployee;
import com.iwlpl.connectme.errorHandler.ErrorManager;

import java.util.ArrayList;

public class Adapter_Emp_Master extends RecyclerView.Adapter<Adapter_Emp_Master.MyViewHolder> {

    Context context;
    ArrayList<DataEmployee> dataEmployees;

    public Adapter_Emp_Master(Context c , ArrayList<DataEmployee> dm) {

        dataEmployees = dm;
        context = c;
    }

    @NonNull
    @Override
    public Adapter_Emp_Master.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.cv_employee_master,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Emp_Master.MyViewHolder holder, int position) {
        try {

        holder.name.setText(dataEmployees.get(position).getName());
        holder.DeptName.setText("Department : "+dataEmployees.get(position).getDepartment());
        holder.Desig.setText("Designation : "+dataEmployees.get(position).getDesignation());
//        holder.Level.setText("Level : "+dataEmployees.get(position).getLevel());
//        holder.ID.setText("ID : "+dataEmployees.get(position).getID());
//        holder.Related.setText("Related : "+dataEmployees.get(position).getRelated());
        holder.Email.setText("Email : "+dataEmployees.get(position).getEmail());
        holder.Mobile.setText("Mobile : "+dataEmployees.get(position).getMobile());
//        holder.Phone.setText("Phone : "+dataEmployees.get(position).getPhone());
        holder.onClick(position);

        } catch (Exception e) { handleError(e,"Adapter_Emp_Master");}
    }

    @Override
    public int getItemCount() {
        return dataEmployees.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name,DeptName,Mobile,Phone,Email,Related,ID,Desig,Level;
        ConstraintLayout cvLayout;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            try {

            name = itemView.findViewById(R.id.tvEmpName);
            DeptName = itemView.findViewById(R.id.tvEmpDeptName);
            Mobile=itemView.findViewById(R.id.tvEmpMobile);
//            Phone=itemView.findViewById(R.id.tvEmpPhone);
            Email=itemView.findViewById(R.id.tvEmpEmail);
//            Related=itemView.findViewById(R.id.tvEmpRelated);
//            ID=itemView.findViewById(R.id.tvEmpID);
            Desig=itemView.findViewById(R.id.tvEmpDesigName);
//            Level=itemView.findViewById(R.id.tvEmpLevel);
            cvLayout = itemView.findViewById(R.id.cvMasterCardMain);

            } catch (Exception e) { handleError(e,"Adapter_Emp_Master");}
        }


        public void onClick(final int position) {

            cvLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { try {

                        Intent intent = new Intent(context, Activity_Employee.class);
                        intent.putExtra("previous","details");
                        intent.putExtra("details",dataEmployees.get(position).getObj());
                        ((Activity) context).startActivityForResult(intent, 1);

                } catch (Exception e) { handleError(e,"Adapter_Emp_Master: cvLayout.onClick()");}
                }
            });

        }
    }
    void handleError(Exception e,String loc){
        new ErrorManager(context, loc,
                e.getClass().toString(),e.getMessage(),loc);
    }
}
