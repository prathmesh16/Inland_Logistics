package com.iwlpl.connectme.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.iwlpl.connectme.API.APIConstants;
import com.iwlpl.connectme.R;
import com.iwlpl.connectme.errorHandler.ErrorManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Activity_Employee extends AppCompatActivity {
    EditText Name,DeptName,Mobile,Phone,Email,ID,Desig;
    Button add,delete,update;
    Spinner Related,Level;
    SharedPreferences userDetails;
    private String previousActivity,obj;
    private LinearLayout UpdateDelete;
    JSONObject details;
    Intent intent;
    TextView header;
    ImageButton back;
    ArrayList<String> relate,relateID;
    String empId;
    Button serviceDetails;
    JSONArray serviceList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
        setContentView(R.layout.activity_employee);

        userDetails=getSharedPreferences("userDetails",MODE_PRIVATE);

        serviceDetails = findViewById(R.id.serviceDetailsBtn);
        Name=findViewById(R.id.Name);
        DeptName=findViewById(R.id.DeptName);
        Desig=findViewById(R.id.DesigName);
        Mobile=findViewById(R.id.Mobile);
        Phone=findViewById(R.id.Phone);
        Email=findViewById(R.id.Email);
        Related=findViewById(R.id.Related);
        //ID=findViewById(R.id.ID);
        Level=findViewById(R.id.Level);
        add=findViewById(R.id.btnAdd);
        update=findViewById(R.id.btnUpdate);
        delete=findViewById(R.id.btnDelete);
        UpdateDelete=findViewById(R.id.update_delete_btns);

        empId = "";

        intent = getIntent();
        previousActivity = intent.getStringExtra("previous");

        fetchRelatedList();

        ArrayList<String> LevelArray = new ArrayList<>();
        LevelArray.add("1");
        LevelArray.add("2");
        LevelArray.add("3");
        LevelArray.add("4");

        Level.setAdapter(new ArrayAdapter<String>(Activity_Employee.this, android.R.layout.simple_spinner_dropdown_item, (List<String>) LevelArray));


        header = findViewById(R.id.header_emp);
        back = findViewById(R.id.backBtnActivityemp);

        if(previousActivity.equalsIgnoreCase("details"))
        {
            obj=intent.getStringExtra("details");
            try {
                details=new JSONObject(obj);
                header.setText("Contact Details");


                Name.setText(details.getString("ContactName"));
                DeptName.setText(details.getString("DepartmentName"));
                Desig.setText(details.getString("DesignationName"));
                Mobile.setText(details.getString("MobileNo"));
                Phone.setText(details.getString("Phone"));
                Email.setText(details.getString("EmailID"));
//                ID.setText(details.getString("ID"));
                empId = details.getString("ID");
                //Level.setText(details.getString("Level"));
                Log.e("Level","dd"+details.getString("Level"));

                if(details.getString("Level").isEmpty())
                {
                    Level.setSelection(0);  //chnage this
                }
                else
                {
                     Level.setSelection(Integer.parseInt(details.getString("Level"))-1);  //chnage this
                }
                //Related.setText(details.getString("Related"));
                add.setVisibility(View.INVISIBLE);
                UpdateDelete.setVisibility(View.VISIBLE);
                serviceList=details.getJSONArray("ServiceList");

            } catch (JSONException e) { handleError(e,"onCreate()");
                e.printStackTrace();
            }


        }
        else if(previousActivity.equalsIgnoreCase("add"))
        {
            Log.e("Previous","add new");
            header.setText("Add Contact");
            UpdateDelete.setVisibility(View.GONE);
        serviceDetails.setVisibility(View.GONE);
            add.setVisibility(View.VISIBLE);
            //
            serviceList=new JSONArray();

        }
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateData())
                    addUpdateEmployee();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteEmployee();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateData())
                    addUpdateEmployee();
            }
        });

        serviceDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { try {
                //TOdo : Show Service Details dialog here and load previously added services
//dialog creation
                final Dialog dialog = new Dialog(Activity_Employee.this);
                dialog.setContentView(R.layout.dilog_services_type);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCancelable(true);
                Window window = dialog.getWindow();
                window.setGravity(Gravity.CENTER);
                window.getAttributes().windowAnimations = R.style.DialogAnimation;
                final WindowManager.LayoutParams params = window.getAttributes();
                params.height = WindowManager.LayoutParams.WRAP_CONTENT;
                params.width = WindowManager.LayoutParams.MATCH_PARENT;
                window.setAttributes(params);
                dialog.show();
                Button updateServices=dialog.findViewById(R.id.update_services_btn);
                final TableLayout table=dialog.findViewById(R.id.table);
                table.setStretchAllColumns(true);

                final Typeface typeface = ResourcesCompat.getFont(Activity_Employee.this, R.font.nunito_semibold);

                JSONObject newEmployee=new JSONObject();
                try {
                    newEmployee.put("CustomerCode",userDetails.getString("CustomerCode",""));
                    newEmployee.put("TokenNo",userDetails.getString("TokenNo",""));
                    newEmployee.put("UserId",userDetails.getString("UserID",""));

                } catch (JSONException e) { handleError(e,"serviceDetails.onClick()");
                    e.printStackTrace();
                }
                final ArrayList<String> checkedIDs = new ArrayList<>();
                AndroidNetworking.post(APIConstants.ServiceList)
                        .addJSONObjectBody(newEmployee) // posting json
                        .setTag("test")
                        .setPriority(Priority.HIGH)
                        .build()
                        .getAsJSONArray(new JSONArrayRequestListener() {
                            @Override
                            public void onResponse(JSONArray response) {
                                JSONArray data1 = null;
                                //fetch related data here
                                data1=response;

                                for(int i=0;i<response.length();i++)
                                {

                                    JSONObject obj = null;
                                    try {
                                        obj = response.getJSONObject(i);

                                        TableRow tableRow1;
                                        tableRow1 = new TableRow(getApplicationContext());
                                        tableRow1.setGravity(Gravity.CENTER);
//                                if (i % 2 == 0)
//                                    tableRow1.setBackgroundColor(getResources().getColor(R.color.white));
//                                else
//                                    tableRow1.setBackgroundColor(getResources().getColor(R.color.faint));

                                        tableRow1.setPadding(dptoPixel(0), dptoPixel(4), dptoPixel(0), dptoPixel(4));

                                        TextView sid = new TextView(getApplicationContext());
                                        sid.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                        sid.setText(obj.getString("ID"));
                                        sid.setGravity(Gravity.CENTER);
                                        sid.setTextColor(getResources().getColor(R.color.heading));
                                        sid.setTypeface(typeface);
                                        tableRow1.addView(sid);

                                        TextView service_name = new TextView(getApplicationContext());
                                        service_name.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                        service_name.setTextColor(getResources().getColor(R.color.heading));
                                        service_name.setText(obj.getString("Name"));
                                        service_name.setGravity(Gravity.CENTER);
                                        service_name.setTypeface(typeface);
                                        tableRow1.addView(service_name);
                                        TableRow.LayoutParams rowParams=(TableRow.LayoutParams) service_name.getLayoutParams();
                                        rowParams.span=2;
                                        service_name.setLayoutParams(rowParams);

                                        TextView service_type = new TextView(getApplicationContext());
                                        service_type.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                        service_type.setTextColor(getResources().getColor(R.color.heading));
                                        service_type.setText("ghgv");
                                        service_type.setGravity(Gravity.CENTER);
                                        service_type.setTypeface(typeface);
                                        tableRow1.addView(service_type);
                                        TableRow.LayoutParams rowParams1=(TableRow.LayoutParams) service_type.getLayoutParams();
                                        rowParams1.span=2;
                                        service_type.setLayoutParams(rowParams1);


                                        final CheckBox checkBox=new CheckBox(getApplicationContext());
                                        checkBox.setGravity(Gravity.END);
                                        checkBox.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                                        tableRow1.addView(checkBox);

                                        for (int j=0;j<serviceList.length();j++)
                                        {
                                            if(obj.getString("ID").equals(serviceList.getJSONObject(j).getString("ID")))
                                            {
                                                checkBox.setChecked(true);
                                                break;
                                            }
                                        }
                                        final JSONObject finalObj = obj;
                                        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                            @Override
                                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                                if(checkBox.isChecked())
                                                {
                                                    try {
                                                        checkedIDs.add(finalObj.getString("ID"));
                                                    } catch (JSONException e) { handleError(e,"serviceDetails.onClick()");
                                                        e.printStackTrace();
                                                    }
                                                }
                                                else
                                                {
                                                    try {
                                                        checkedIDs.remove(finalObj.getString("ID"));
                                                    } catch (JSONException e) { handleError(e,"serviceDetails.onClick()");
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }
                                        });

                                        table.addView(tableRow1);  //add row to table
                                    } catch (JSONException e) { handleError(e,"serviceDetails.onClick()");
                                        e.printStackTrace();
                                    }


                                    Related.setAdapter(new ArrayAdapter<String>(Activity_Employee.this, android.R.layout.simple_spinner_dropdown_item, relate));

                                    // list.add(dataMaster);
                                }

                            }

                            @Override
                            public void onError(ANError anError) {
                                new ErrorManager(Activity_Employee.this,Activity_Employee.class.getName(),
                                        anError.getClass().toString(),anError.getMessage(),"Network ERROR");
                                Toast.makeText(Activity_Employee.this,"Network ERROR",Toast.LENGTH_LONG).show();

                            }
                        });




                updateServices.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        for (int i=0;i<checkedIDs.size();i++)
                        {
                            int tmp=0;
                            for (int j=0;j<serviceList.length();j++)
                            {
                                try {
                                    if(checkedIDs.get(i).equals(serviceList.getJSONObject(j).getString("ID")))
                                    {
                                        tmp=1;
                                        break;
                                    }
                                } catch (JSONException e) { handleError(e,"updateServices.onClick()");
                                    e.printStackTrace();
                                }
                            }
                            if (tmp==0)
                            {
                                JSONObject newEmployee=new JSONObject();
                                try {
                                    newEmployee.put("ServiceID",checkedIDs.get(i));
                                    newEmployee.put("ID",details.getString("ID"));
                                    newEmployee.put("TokenNo",userDetails.getString("TokenNo",""));
                                    newEmployee.put("UserId",userDetails.getString("UserID",""));

                                } catch (JSONException e) { handleError(e,"updateServices.onClick()");
                                    e.printStackTrace();
                                }

                                final int finalI = i;
                                AndroidNetworking.post(APIConstants.AddService)
                                        .addJSONObjectBody(newEmployee) // posting json
                                        .setTag("test")
                                        .setPriority(Priority.HIGH)
                                        .build()
                                        .getAsJSONObject(new JSONObjectRequestListener() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                JSONObject data1 = null;

                                                data1=response;

                                                try {
                                                    if(data1.getBoolean("IsSuccess")) {
                                                        Toast.makeText(Activity_Employee.this,data1.getString("Msg"),Toast.LENGTH_LONG).show();
                                                        JSONObject tmp= new JSONObject();
                                                        tmp.put("ID",checkedIDs.get(finalI));

                                                        serviceList.put(tmp);
                                                    }
                                                    else
                                                    {
                                                        Toast.makeText(Activity_Employee.this,data1.getString("Msg"),Toast.LENGTH_LONG).show();
                                                    }
                                                } catch (JSONException e) { handleError(e,"updateServices.onClick()");
                                                    e.printStackTrace();
                                                }


                                            }

                                            @Override
                                            public void onError(ANError anError) {
                                                new ErrorManager(Activity_Employee.this,Activity_Employee.class.getName(),
                                                        anError.getClass().toString(),anError.getMessage(),"Network ERROR");
                                                Toast.makeText(Activity_Employee.this,"Network ERROR",Toast.LENGTH_LONG).show();

                                            }
                                        });
                            }

                        }
                        //return
                        dialog.dismiss();
                        Intent returnIntent = new Intent();
                        setResult(Activity.RESULT_OK,returnIntent);
                        finish();
                    }
                });



            }catch (Exception e) { handleError(e,"serviceDetails.onClick()"); }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        }catch (Exception e) { handleError(e,"onCreate()"); }
    }

    private void fetchRelatedList() {

        JSONObject newEmployee=new JSONObject();
        try {
            newEmployee.put("CustomerCode",userDetails.getString("CustomerCode",""));
            newEmployee.put("TokenNo",userDetails.getString("TokenNo",""));
            newEmployee.put("UserId",userDetails.getString("UserID",""));

        } catch (JSONException e) { handleError(e,"fetchRelatedList()");
            e.printStackTrace();
        }

        AndroidNetworking.post(APIConstants.RelatedList)
                .addJSONObjectBody(newEmployee) // posting json
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        JSONArray data1 = null;
//fetch related data here
                        data1=response;
                        relate = new ArrayList<>();
                        relateID = new ArrayList<>();
                        for(int i=0;i<response.length();i++)
                        {

                            JSONObject obj = null;
                            try {
                                obj = response.getJSONObject(i);
                                String d = obj.getString("Name");
                                Log.e("related",d);
                                relate.add(d);
                                relateID.add(obj.getString("ID"));
                            } catch (JSONException e) { handleError(e,"fetchRelatedList()AndroidNetworking.post(....");
                                e.printStackTrace();
                            }


                            Related.setAdapter(new ArrayAdapter<String>(Activity_Employee.this, android.R.layout.simple_spinner_dropdown_item, relate));
                            if(previousActivity.equalsIgnoreCase("details"))
                            {
                                for (int k=0;k<relateID.size();k++)
                                {
                                    try {
                                        if(relateID.get(k).equals(details.getString("Related")))
                                        {
                                            Related.setSelection(k);
                                        }
                                    } catch (JSONException e) { handleError(e,"fetchRelatedList()AndroidNetworking.post(....");
                                        e.printStackTrace();
                                    }
                                }

                            }
                            // list.add(dataMaster);
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        new ErrorManager(Activity_Employee.this,Activity_Employee.class.getName(),
                                anError.getClass().toString(),anError.getMessage(),"Network ERROR");
                        Toast.makeText(Activity_Employee.this,"Network ERROR",Toast.LENGTH_LONG).show();

                    }
                });


    }

    public void addUpdateEmployee()
    {
        String url = APIConstants.EmployeeAdd;
        JSONObject newEmployee=new JSONObject();
        try {
//            previousActivity = intent.getStringExtra("previous");

            assert previousActivity != null;
            if(previousActivity.equalsIgnoreCase("details") )
            {
                url = APIConstants.EmployeeUpdate;
            }

            newEmployee.put("ContactName",Name.getText().toString());
            newEmployee.put("DepartmentName",DeptName.getText().toString());
            newEmployee.put("DesignationName",Desig.getText().toString());
            newEmployee.put("EmailID",Email.getText().toString());
            newEmployee.put("ID",empId);
            newEmployee.put("Level",Level.getSelectedItem());
            newEmployee.put("MobileNo",Mobile.getText().toString());
            newEmployee.put("Phone",Phone.getText().toString());
            newEmployee.put("Related",relateID.get(Related.getSelectedItemPosition()));
            newEmployee.put("CustomerCode",userDetails.getString("CustomerCode",""));
            newEmployee.put("TokenNo",userDetails.getString("TokenNo",""));
            newEmployee.put("UserId",userDetails.getString("UserID",""));

        } catch (JSONException e) { handleError(e,"addUpdateEmployee()");
            e.printStackTrace();
        }

        AndroidNetworking.post(url)
                .addJSONObjectBody(newEmployee) // posting json
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject data1 = null;

                        data1=response;

                        try {
                            if(data1.getBoolean("IsSuccess")) {
                                Toast.makeText(Activity_Employee.this,data1.getString("Msg"),Toast.LENGTH_SHORT).show();
                                assert previousActivity != null;
                                if(!previousActivity.equalsIgnoreCase("details") )
                                {
                                    //TODO : SHOW DIALOG TO ADD SERVICES
//dialog creation
                                    final Dialog dialog = new Dialog(Activity_Employee.this);
                                    dialog.setContentView(R.layout.dilog_services_type);
                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    dialog.setCancelable(true);
                                    Window window = dialog.getWindow();
                                    window.setGravity(Gravity.CENTER);
                                    window.getAttributes().windowAnimations = R.style.DialogAnimation;
                                    final WindowManager.LayoutParams params = window.getAttributes();
                                    params.height = WindowManager.LayoutParams.WRAP_CONTENT;
                                    params.width = WindowManager.LayoutParams.MATCH_PARENT;
                                    window.setAttributes(params);
                                    dialog.show();
                                    Button updateServices=dialog.findViewById(R.id.update_services_btn);
                                    final TableLayout table=dialog.findViewById(R.id.table);
                                    table.setStretchAllColumns(true);

                                    final Typeface typeface = ResourcesCompat.getFont(Activity_Employee.this, R.font.nunito_semibold);

                                    JSONObject newEmployee=new JSONObject();
                                    try {
                                        newEmployee.put("CustomerCode",userDetails.getString("CustomerCode",""));
                                        newEmployee.put("TokenNo",userDetails.getString("TokenNo",""));
                                        newEmployee.put("UserId",userDetails.getString("UserID",""));

                                    } catch (JSONException e) { handleError(e,"addUpdateEmployee() AndroidNetworking.post(..");
                                        e.printStackTrace();
                                    }
                                    final ArrayList<String> checkedIDs = new ArrayList<>();
                                    AndroidNetworking.post(APIConstants.ServiceList)
                                            .addJSONObjectBody(newEmployee) // posting json
                                            .setTag("test")
                                            .setPriority(Priority.HIGH)
                                            .build()
                                            .getAsJSONArray(new JSONArrayRequestListener() {
                                                @Override
                                                public void onResponse(JSONArray response) {
                                                    JSONArray data1 = null;
                                                    //fetch related data here
                                                    data1=response;

                                                    for(int i=0;i<response.length();i++)
                                                    {

                                                        JSONObject obj = null;
                                                        try {
                                                            obj = response.getJSONObject(i);

                                                            TableRow tableRow1;
                                                            tableRow1 = new TableRow(getApplicationContext());
                                                            tableRow1.setGravity(Gravity.CENTER);
//                                if (i % 2 == 0)
//                                    tableRow1.setBackgroundColor(getResources().getColor(R.color.white));
//                                else
//                                    tableRow1.setBackgroundColor(getResources().getColor(R.color.faint));

                                                            tableRow1.setPadding(dptoPixel(0), dptoPixel(4), dptoPixel(0), dptoPixel(4));

                                                            TextView sid = new TextView(getApplicationContext());
                                                            sid.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                                            sid.setText(obj.getString("ID"));
                                                            sid.setGravity(Gravity.CENTER);
                                                            sid.setTextColor(getResources().getColor(R.color.heading));
                                                            sid.setTypeface(typeface);
                                                            tableRow1.addView(sid);

                                                            TextView service_name = new TextView(getApplicationContext());
                                                            service_name.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                                            service_name.setTextColor(getResources().getColor(R.color.heading));
                                                            service_name.setText(obj.getString("Name"));
                                                            service_name.setGravity(Gravity.CENTER);
                                                            service_name.setTypeface(typeface);
                                                            tableRow1.addView(service_name);
                                                            TableRow.LayoutParams rowParams=(TableRow.LayoutParams) service_name.getLayoutParams();
                                                            rowParams.span=2;
                                                            service_name.setLayoutParams(rowParams);

                                                            TextView service_type = new TextView(getApplicationContext());
                                                            service_type.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                                            service_type.setTextColor(getResources().getColor(R.color.heading));
                                                            service_type.setText("ghgv");
                                                            service_type.setGravity(Gravity.CENTER);
                                                            service_type.setTypeface(typeface);
                                                            tableRow1.addView(service_type);
                                                            TableRow.LayoutParams rowParams1=(TableRow.LayoutParams) service_type.getLayoutParams();
                                                            rowParams1.span=2;
                                                            service_type.setLayoutParams(rowParams1);


                                                            final CheckBox checkBox=new CheckBox(getApplicationContext());
                                                            checkBox.setGravity(Gravity.END);
                                                            checkBox.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                                                            tableRow1.addView(checkBox);

                                                            for (int j=0;j<serviceList.length();j++)
                                                            {
                                                                if(obj.getString("ID").equals(serviceList.getJSONObject(j).getString("ID")))
                                                                {
                                                                    checkBox.setChecked(true);
                                                                    break;
                                                                }
                                                            }
                                                            final JSONObject finalObj = obj;
                                                            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                                @Override
                                                                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                                                    if(checkBox.isChecked())
                                                                    {
                                                                        try {
                                                                            checkedIDs.add(finalObj.getString("ID"));
                                                                        } catch (JSONException e) { handleError(e,"addUpdateEmployee() AndroidNetworking.post(..");
                                                                            e.printStackTrace();
                                                                        }
                                                                    }
                                                                    else
                                                                    {
                                                                        try {
                                                                            checkedIDs.remove(finalObj.getString("ID"));
                                                                        } catch (JSONException e) { handleError(e,"addUpdateEmployee() AndroidNetworking.post(..");
                                                                            e.printStackTrace();
                                                                        }
                                                                    }
                                                                }
                                                            });

                                                            table.addView(tableRow1);  //add row to table
                                                        } catch (JSONException e) { handleError(e,"addUpdateEmployee() AndroidNetworking.post(..");
                                                            e.printStackTrace();
                                                        }


                                                        Related.setAdapter(new ArrayAdapter<String>(Activity_Employee.this, android.R.layout.simple_spinner_dropdown_item, relate));

                                                        // list.add(dataMaster);
                                                    }

                                                }

                                                @Override
                                                public void onError(ANError anError) {
                                                    new ErrorManager(Activity_Employee.this,Activity_Employee.class.getName(),
                                                            anError.getClass().toString(),anError.getMessage(),"Network ERROR");
                                                    Toast.makeText(Activity_Employee.this,"Network ERROR",Toast.LENGTH_LONG).show();

                                                }
                                            });




                                    updateServices.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            for (int i=0;i<checkedIDs.size();i++)
                                            {
                                                int tmp=0;
                                                for (int j=0;j<serviceList.length();j++)
                                                {
                                                    try {
                                                        if(checkedIDs.get(i).equals(serviceList.getJSONObject(j).getString("ID")))
                                                        {
                                                            tmp=1;
                                                            break;
                                                        }
                                                    } catch (JSONException e) { handleError(e,"updateServices.onClick()");
                                                        e.printStackTrace();
                                                    }
                                                }
                                                if (tmp==0)
                                                {
                                                    JSONObject newEmployee=new JSONObject();
                                                    try {
                                                        newEmployee.put("ServiceID",checkedIDs.get(i));
                                                        newEmployee.put("ID",details.getString("ID"));
                                                        newEmployee.put("TokenNo",userDetails.getString("TokenNo",""));
                                                        newEmployee.put("UserId",userDetails.getString("UserID",""));

                                                    } catch (JSONException e) { handleError(e,"updateServices.onClick()");
                                                        e.printStackTrace();
                                                    }

                                                    final int finalI = i;
                                                    AndroidNetworking.post(APIConstants.AddService)
                                                            .addJSONObjectBody(newEmployee) // posting json
                                                            .setTag("test")
                                                            .setPriority(Priority.HIGH)
                                                            .build()
                                                            .getAsJSONObject(new JSONObjectRequestListener() {
                                                                @Override
                                                                public void onResponse(JSONObject response) {
                                                                    JSONObject data1 = null;

                                                                    data1=response;

                                                                    try {
                                                                        if(data1.getBoolean("IsSuccess")) {
                                                                            Toast.makeText(Activity_Employee.this,data1.getString("Msg"),Toast.LENGTH_LONG).show();
                                                                            JSONObject tmp= new JSONObject();
                                                                            tmp.put("ID",checkedIDs.get(finalI));

                                                                            serviceList.put(tmp);
                                                                        }
                                                                        else
                                                                        {
                                                                            Toast.makeText(Activity_Employee.this,data1.getString("Msg"),Toast.LENGTH_LONG).show();
                                                                        }
                                                                    } catch (JSONException e) { handleError(e,"updateServices.onClick() AndroidNetworking.post(..");
                                                                        e.printStackTrace();
                                                                    }


                                                                }

                                                                @Override
                                                                public void onError(ANError anError) {
                                                                    new ErrorManager(Activity_Employee.this,Activity_Employee.class.getName(),
                                                                            anError.getClass().toString(),anError.getMessage(),"Network ERROR");
                                                                    Toast.makeText(Activity_Employee.this,"Network ERROR",Toast.LENGTH_LONG).show();

                                                                }
                                                            });
                                                }

                                            }
                                            dialog.dismiss();
                                            Intent returnIntent = new Intent();
                                            setResult(Activity.RESULT_OK,returnIntent);



                                            finish();
                                        }
                                    });
                                }



                            }
                            else
                            {
                                Toast.makeText(Activity_Employee.this,"Something went wrong !", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {handleError(e,"updateServices.onClick() ");
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        new ErrorManager(Activity_Employee.this,Activity_Employee.class.getName(),
                                anError.getClass().toString(),anError.getMessage(),"Network ERROR");
                        Toast.makeText(Activity_Employee.this,"Network ERROR",Toast.LENGTH_LONG).show();

                    }
                });
    }
    private void deleteEmployee() {
        JSONObject newEmployee=new JSONObject();
        try {
            newEmployee.put("AddressID",empId);
            newEmployee.put("TokenNo",userDetails.getString("TokenNo",""));
            newEmployee.put("UserId",userDetails.getString("UserID",""));

        } catch (JSONException e) { handleError(e,"deleteEmployee()");
            e.printStackTrace();
        }

        AndroidNetworking.post(APIConstants.EmployeeDelete)
                .addJSONObjectBody(newEmployee) // posting json
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject data1 = null;

                        data1=response;

                        try {
                            if(data1.getBoolean("IsSuccess")) {
                                Toast.makeText(Activity_Employee.this,data1.getString("Msg"),Toast.LENGTH_SHORT).show();
                                Intent returnIntent = new Intent();
                                setResult(Activity.RESULT_OK,returnIntent);
                                finish();
                            }
                            else
                            {
                                Toast.makeText(Activity_Employee.this,"Something went wrong !", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) { handleError(e,"deleteEmployee() AndroidNetworking.post(..");
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        new ErrorManager(Activity_Employee.this,Activity_Employee.class.getName(),
                                anError.getClass().toString(),anError.getMessage(),"Network ERROR");
                        Toast.makeText(Activity_Employee.this,"Network ERROR",Toast.LENGTH_LONG).show();

                    }
                });

    }
    public boolean validateData() {
        if (Name.getText().toString().equals("")) {
            Toast.makeText(Activity_Employee.this, "Enter Name!", Toast.LENGTH_LONG).show();
            return false;
        }
        else if (DeptName.getText().toString().equals("")) {
            Toast.makeText(Activity_Employee.this, "Enter Department Name!", Toast.LENGTH_LONG).show();
            return false;
        }
        else if (Desig.getText().toString().equals("")) {
            Toast.makeText(Activity_Employee.this, "Enter Designation Name!", Toast.LENGTH_LONG).show();
            return false;
        }
        else if (Mobile.getText().toString().equals("")) {
            Toast.makeText(Activity_Employee.this, "Enter Mobile!", Toast.LENGTH_LONG).show();
            return false;
        }
        else if (Mobile.getText().length()!=10) {
            Toast.makeText(Activity_Employee.this, "Invalid Mobile No!", Toast.LENGTH_LONG).show();
            return false;
        }
        else if (Phone.getText().toString().equals("")) {
            Toast.makeText(Activity_Employee.this, "Enter Phone!", Toast.LENGTH_LONG).show();
            return false;
        }
        else if (Email.getText().toString().equals("")) {
            Toast.makeText(Activity_Employee.this, "Enter Email!", Toast.LENGTH_LONG).show();
            return false;
        }
//        else if (ID.getText().toString().equals("")) {
//            Toast.makeText(Activity_Employee.this, "Enter ID!", Toast.LENGTH_LONG).show();
//            return false;
//        }
//        else if (Level.getText().toString().equals("")) {
//            Toast.makeText(Activity_Employee.this, "Enter Level!", Toast.LENGTH_LONG).show();
//            return false;
//        }
//        else if (Related.getText().toString().equals("")) {
//            Toast.makeText(Activity_Employee.this, "Enter Related!", Toast.LENGTH_LONG).show();
//            return false;
//        }
        return true;
    }
    private int dptoPixel(int p) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, p, this.getResources().getDisplayMetrics());
    }

    void handleError(Exception e,String loc){
        new ErrorManager(Activity_Employee.this,Activity_Employee.class.getName(),
                e.getClass().toString(),e.getMessage(),loc);
    }
}
