package com.iwlpl.connectme.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iwlpl.connectme.R;
import com.iwlpl.connectme.data_handler.CustomerList;
import com.iwlpl.connectme.errorHandler.ErrorManager;

import java.util.ArrayList;

public class Adapter_Switch_Customer extends RecyclerView.Adapter<Adapter_Switch_Customer.MyViewHolder> {

    Dialog confirmDialog;
    ArrayList<CustomerList> list;
    Context context;
    int lastCheck;
    CustomerList customerList;
    ArrayList<Boolean> isCustomerSelected;
    private SharedPreferences userDetails;

    public Adapter_Switch_Customer(Context context, ArrayList<CustomerList> list, ArrayList<Boolean> isCustomerSelected) {
        this.list = list;
        this.context = context;
        this.isCustomerSelected = isCustomerSelected;
    }

    @NonNull
    @Override
    public Adapter_Switch_Customer.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.cv_switch_customer,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Switch_Customer.MyViewHolder holder, int position) {
        try {
            customerList = list.get(position);
            String name = customerList.getCUstomerName();
            if(isCustomerSelected.get(position))
                lastCheck=position;

            holder.customer_name.setText(name);
            name = name.charAt(0)+"";
            holder.profile_text.setText(name);
            holder.CustomerCheckbox.setChecked(isCustomerSelected.get(position));

            holder.CustomerCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        if(lastCheck!=position && isChecked) {

                            confirmDialog=new Dialog(context);
                            confirmDialog.setContentView(R.layout.no_internet_layout);
                            confirmDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            confirmDialog.setCancelable(false);
                            Window window = confirmDialog.getWindow();
                            window.setGravity(Gravity.CENTER);
                            window.getAttributes().windowAnimations=R.style.DialogAnimation;
                            final WindowManager.LayoutParams params = window.getAttributes();
                            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
                            params.width = WindowManager.LayoutParams.MATCH_PARENT;
                            window.setAttributes((WindowManager.LayoutParams) params);
                            ImageView Icon=confirmDialog.findViewById(R.id.dialog_icon);
                            TextView title=confirmDialog.findViewById(R.id.dialog_title);
                            TextView msg=confirmDialog.findViewById(R.id.dialog_msg);
                           Button backBtn=confirmDialog.findViewById(R.id.dialog_button_positive);
                           backBtn.setVisibility(View.GONE);
                           Button btn_confirm = confirmDialog.findViewById(R.id.btn_confirm);
                            btn_confirm.setVisibility(View.VISIBLE);
                            Button btn_cancel = confirmDialog.findViewById(R.id.btn_cancel);
                            btn_cancel.setVisibility(View.VISIBLE);
                            Icon.setImageDrawable(context.getResources().getDrawable(R.drawable.warning_icon2));
                            Icon.setBackground(context.getResources().getDrawable(R.drawable.dialog_icon_bg2));
                            title.setText("CONFIRM TO CHANGE CUSTOMER.");
                            msg.setText("Name: "+list.get(position).getCUstomerName());

                            confirmDialog.show();
                            btn_confirm.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    confirmDialog.dismiss();
                                    isCustomerSelected.set(lastCheck, false);
                                    isCustomerSelected.set(position, true);
                                    lastCheck = position;

                                    SharedPreferences preferences = context.getSharedPreferences("userDetails", Context.MODE_PRIVATE);
                                    preferences.edit().putString("CustomerCode", list.get(lastCheck).getCustomerCode()).apply();

                                    notifyDataSetChanged();
                                }
                            });

                            btn_cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    holder.CustomerCheckbox.setChecked(false);
                                    confirmDialog.dismiss();



                                }
                            });

                        }

                    }else if(lastCheck == position) holder.CustomerCheckbox.setChecked(true);

                }
            });



        } catch (Exception e) { handleError(e,"Adapter_Switch_Customer)");}
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView customer_name,profile_text;
        CheckBox CustomerCheckbox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);     try {

                customer_name= itemView.findViewById(R.id.customer_name);
                profile_text= itemView.findViewById(R.id.profile_text);
                CustomerCheckbox= itemView.findViewById(R.id.CustomerCheckbox);
                userDetails= PreferenceManager.getDefaultSharedPreferences(context);


            } catch (Exception e) { handleError(e,"Adapter_Switch_Customer)");}
        }
    }
        void handleError(Exception e,String loc){
            new ErrorManager(context, "Adapter_Switch_Customer",
                    e.getClass().toString(),e.getMessage(),loc);
        }
}
