package com.iwlpl.connectme.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.internal.$Gson$Preconditions;
import com.iwlpl.connectme.API.APIConstants;
import com.iwlpl.connectme.DBHelper;
import com.iwlpl.connectme.R;
import com.iwlpl.connectme.adapter.AdapterLocation;
import com.iwlpl.connectme.adapter.AdaptorProduct;
import com.iwlpl.connectme.data_handler.DataMaster;
import com.iwlpl.connectme.data_handler.DataProuduct;
import com.iwlpl.connectme.errorHandler.ErrorManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Activity_pickup_request extends AppCompatActivity implements AdapterLocation.AdapterCallback,AdaptorProduct.AdapterCallbackProduct {

    private DBHelper mydb ;
    EditText contactPerson, contactPersonNo,  contactPerson2, contactPersonNo2;
    TextView address,address2,product;
    TextView pickUpDate,invoiceNo;
    String finalProductCode="";
    EditText weight;
    EditText noPackages;
    EditText value;
    EditText specialRemark;
    EditText saidContains;
    Dialog activeDialog;
    Dialog locationDialog;
    Spinner loadSpinner;
    ConstraintLayout pickupLocationLayout;
    TextView pickupTV, deliveryTV;
    ArrayList<String> Dnames1,Dnames2;
    ArrayList<DataProuduct> productDnames;
    TextView activeTextView;
    ArrayList<String> activeList;
    String label;
    String previousActivity="";
    int warehouseCode = 101;
    int customerCode = 101;
    int locationTypeIndex;
    LinearLayout shipmentBtns;
    SharedPreferences userDetails;
    ArrayList<DataMaster> list_wha;
    ArrayList<DataMaster> list_cma;
    ArrayList<DataProuduct> list_product;
    DataMaster dataMaster;
    String pickupAddressID, deliveryAddressID;
    int cma_position,wha_position;
    boolean isConsignor=true;
    Intent intent;
    RadioGroup rg;
    Dialog dialog;
    String pincode1,pincode2;
    TextView label1,label2;
    TextView titleTv;
    ImageButton backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
        setContentView(R.layout.activity_pickup_request);
        mydb = new DBHelper(this);
        AndroidNetworking.initialize(getApplicationContext());

        if (ContextCompat.checkSelfPermission(Activity_pickup_request.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Activity_pickup_request.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
        }
        rg = findViewById(R.id.rg);

        pincode1="x";
        pincode2="y";

        pickupAddressID="";
        deliveryAddressID="";
        final TextView nextBtnPick = findViewById(R.id.next_btn_pick);
        TextView backBtnShipment = findViewById(R.id.back_btn_shipment_details);
        TextView submitBtnShipment = findViewById(R.id.next_btn_shipment_details);
       // final TextView heading = findViewById(R.id.pickupName);

        address = findViewById(R.id.address);
        address2 = findViewById(R.id.address2);
        contactPerson = findViewById(R.id.contactPersonName);
        contactPerson2 = findViewById(R.id.contactPersonName2);
        contactPersonNo = findViewById(R.id.contactPersonNo);
        contactPersonNo2 = findViewById(R.id.contactPersonNo2);

        product = findViewById(R.id.pickupProduct2);
        product.setClickable(false);
        pickUpDate = findViewById(R.id.pickupDate);
        invoiceNo = findViewById(R.id.invoiceNo);
        weight = findViewById(R.id.weight);
        value = findViewById(R.id.Value);
        specialRemark = findViewById(R.id.specialRemark);
        noPackages = findViewById(R.id.noPackages);
        shipmentBtns=findViewById(R.id.shipment_btns_layout);
        loadSpinner=findViewById(R.id.selectLoadSpinner);
        saidContains=findViewById(R.id.saidContains);

        pickupLocationLayout = findViewById(R.id.pickup_location_layout);
        pickupTV = findViewById(R.id.pickup_location);
        deliveryTV = findViewById(R.id.delivery_location);
        userDetails=getSharedPreferences("userDetails",MODE_PRIVATE);

        label1 = findViewById(R.id.location_1);
        label2 = findViewById(R.id.location_2);
        titleTv=findViewById(R.id.title);
        backBtn=findViewById(R.id.btnBackActivity);

        intent = getIntent();
        previousActivity = intent.getStringExtra("previous");

        fetch_WHA_CMA();
        fetch_Products();
       // submitPickupData();



        if(previousActivity.equalsIgnoreCase("pickup_details"))
        {

            //address.setText(intent.getStringExtra("pickup_adr_id"));
            //fetchPickupAddress(intent.getStringExtra("pickup_adr_id"));
            fetchPreviousDetails();


        }

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { try {
                // dialog to select Product
                dialog = new Dialog(Activity_pickup_request.this);
                dialog.setContentView(R.layout.dialog_select_product);
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

                //intializations

                RecyclerView rvProduct = dialog.findViewById(R.id.rv_options);
                EditText search =dialog.findViewById(R.id.search_et);
                rvProduct.setHasFixedSize(true);
                rvProduct.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                final AdaptorProduct adaptorProduct=new AdaptorProduct(Activity_pickup_request.this,productDnames,Activity_pickup_request.this);

                search.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        adaptorProduct.getFilter().filter(charSequence);
                        adaptorProduct.notifyDataSetChanged();
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
                rvProduct.setAdapter(adaptorProduct);
            }catch (Exception e) { handleError(e,"product.onClick()"); }
            }
        });


        final Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);


        final DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, final int i, final int i1, final int i2) { try {
//                pickUpDate.setText(new StringBuilder().append(i2).append("/")
//                        .append(i1 + 1).append("/").append(i));

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Activity_pickup_request.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) { try {
                        pickUpDate.setText(dateConversion(i,i1+1,i2)+" "+selectedHour + ":" + selectedMinute);
                    }catch (Exception e) { handleError(e,"onTimeSet()"); }
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Pickup Time");
                mTimePicker.show();
            }catch (Exception e) { handleError(e,"DatePickerDialog myDateListener"); }
            }
        };

        pickUpDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { try {
                DatePickerDialog d = new DatePickerDialog(Activity_pickup_request.this, R.style.DateDialogTheme, myDateListener, year, month, day);
                d.getDatePicker().setBackgroundColor(Color.parseColor("#ffffff"));
                d.getDatePicker().setMinDate(System.currentTimeMillis());
                long now = System.currentTimeMillis() - 1000;
                d.getDatePicker().setMaxDate((long) (now+(1000*60*60*24*5d ))); //After 5 Days from Now
                d.show();
            }catch (Exception e) { handleError(e,"pickUpDate.onClick()"); }
            }
        });



        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { try {

                if(address.getText().toString().equalsIgnoreCase(""))
                {
                    //no address
                }
                else
                {
                    // dialog to confirm chnage addrees
                    final Dialog dialog = new Dialog(Activity_pickup_request.this);
                    dialog.setContentView(R.layout.custom_dialog_ui);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.setCancelable(false);
                    Window window = dialog.getWindow();
                    window.setGravity(Gravity.CENTER);
                    window.getAttributes().windowAnimations = R.style.DialogAnimation;
                    final WindowManager.LayoutParams params = window.getAttributes();
                    params.height = WindowManager.LayoutParams.WRAP_CONTENT;
                    params.width = WindowManager.LayoutParams.MATCH_PARENT;
                    window.setAttributes(params);
                    dialog.show();
                    //initializations
                    TextView title = dialog.findViewById(R.id.dialog_title);
                    TextView msg = dialog.findViewById(R.id.dialog_msg);
                    Button positive = dialog.findViewById(R.id.dialog_button_positive);
                    Button negative = dialog.findViewById(R.id.dialog_button_negative);
                    ImageView icon = dialog.findViewById(R.id.dialog_icon);

                    //title
                    title.setText("CONFIRM");
                    //msg
                    msg.setText("Do you surely want to change the Address ?");

                    //postive btn action
                    positive.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();

//                            if (isConsignor)
//                            {
                                Intent intent = new Intent(getApplicationContext(), Activity_CMA_WHA.class);
                                intent.putExtra("previous", "warehouse_details");
                                intent.putExtra("details",list_wha.get(wha_position).getObj());
                                startActivityForResult(intent, warehouseCode);
//                            }
//                            else
//                            {
//                                Intent intent = new Intent(getApplicationContext(), Activity_CMA_WHA.class);
//                                intent.putExtra("previous", "customer_details");
//                                intent.putExtra("details",list_cma.get(cma_position).getObj());
//                                startActivityForResult(intent, customerCode);
//                            }


                        }
                    });
                    //negative btn action
                    negative.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                }



            }catch (Exception e) { handleError(e,"address.onClick()"); }
            }
        });

        address2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { try {

                if(address2.getText().toString().equalsIgnoreCase(""))
                {
                    //no address
                }
                else {


                    final Dialog dialog = new Dialog(Activity_pickup_request.this);
                    dialog.setContentView(R.layout.custom_dialog_ui);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.setCancelable(false);
                    Window window = dialog.getWindow();
                    window.setGravity(Gravity.CENTER);
                    window.getAttributes().windowAnimations = R.style.DialogAnimation;
                    final WindowManager.LayoutParams params = window.getAttributes();
                    params.height = WindowManager.LayoutParams.WRAP_CONTENT;
                    params.width = WindowManager.LayoutParams.MATCH_PARENT;
                    window.setAttributes(params);
                    dialog.show();
                    //initializations
                    TextView title = dialog.findViewById(R.id.dialog_title);
                    TextView msg = dialog.findViewById(R.id.dialog_msg);
                    Button positive = dialog.findViewById(R.id.dialog_button_positive);
                    Button negative = dialog.findViewById(R.id.dialog_button_negative);
                    ImageView icon = dialog.findViewById(R.id.dialog_icon);

                    //title
                    title.setText("CONFIRM");
                    //msg
                    msg.setText("Do you surely want to change the Address ?");

                    //postive btn action
                    positive.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();


//                            if (isConsignor)
//                            {
                                Intent intent = new Intent(getApplicationContext(), Activity_CMA_WHA.class);
                                intent.putExtra("previous", "customer_details");
                                intent.putExtra("details",list_cma.get(cma_position).getObj());
                                startActivityForResult(intent, customerCode);
//                            }
//                            else
//                            {
//                                Intent intent = new Intent(getApplicationContext(), Activity_CMA_WHA.class);
//                                intent.putExtra("previous", "warehouse_details");
//                                intent.putExtra("details",list_wha.get(wha_position).getObj());
//                                startActivityForResult(intent, warehouseCode);
//                            }



                        }
                    });
                    //negative btn action
                    negative.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                }

            }catch (Exception e) { handleError(e,"address2.onClick()"); }
            }
        });


        contactPersonNo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) { try {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (contactPersonNo.getText().toString().isEmpty())
                    return false;
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (event.getRawX() >= (contactPersonNo.getRight() - contactPersonNo.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        if (ContextCompat.checkSelfPermission(Activity_pickup_request.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(Activity_pickup_request.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                        } else {
                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:" + contactPersonNo.getText().toString()));
                            startActivity(callIntent);
                            return true;
                        }

                    }
                }
            }catch (Exception e) { handleError(e,"contactPersonNo.onTouch()"); }
                return false;
            }
        });

        contactPersonNo2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) { try {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;
                if (contactPersonNo2.getText().toString().isEmpty())
                    return false;
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (event.getRawX() >= (contactPersonNo2.getRight() - contactPersonNo2.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        if (ContextCompat.checkSelfPermission(Activity_pickup_request.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(Activity_pickup_request.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                        } else {
                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:" + contactPersonNo2.getText().toString()));
                            startActivity(callIntent);
                            return true;
                        }

                    }
                }
            }catch (Exception e) { handleError(e,"contactPersonNo2.onTouch()"); }
                return false;
            }
        });
        pickupTV.setEnabled(false);
        deliveryTV.setEnabled(false);
        titleTv.setText("Pickup");
        pickupTV.setText("Select Pickup Location");
        pickupTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {try {

                if(isConsignor)
                {
                    createDialog(locationDialog, Dnames1, "Warehouse Location", pickupTV, 1);
                }
                else
                {
                    createDialog(locationDialog, Dnames1, "Warehouse Location", pickupTV, 1);
                }
            }catch (Exception e) { handleError(e,"pickupTV.onClick()"); }
            }
        });
        deliveryTV.setText("Select Delivery Location");
        deliveryTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { try {

                if(isConsignor)
                {
                    createDialog(locationDialog, Dnames2, "Customer Location", deliveryTV, 2);
                }
                else
                {
                    createDialog(locationDialog, Dnames2, "Customer Location", deliveryTV, 2);
                }
            }catch (Exception e) { handleError(e,"deliveryTV.onClick()"); }
            }
        });


        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) { try {
                RadioButton button = radioGroup.findViewById(i);
                if (i == R.id.consignor) {
                    isConsignor=true;
                    titleTv.setText("Pickup");
                    clearFields();
                    label1.setText("Pickup Location");
                    label2.setText("Drop Location");
                    pickupTV.setText("Select Location");
                    deliveryTV.setText("Select Location");

                } else {
                    isConsignor=false;
                    titleTv.setText("Delivery");
                    clearFields();
                    label2.setText("Pickup Location");
                    label1.setText("Drop Location");
                    pickupTV.setText("Select Location");
                    deliveryTV.setText("Select Location");
                }
            }catch (Exception e) { handleError(e,"rg.onCheckedChanged()"); }
            }
        });


        nextBtnPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { try {
                if (validatePickup()) {
                    findViewById(R.id.pickup).setVisibility(View.GONE);
                    findViewById(R.id.layout_shipment_details).setVisibility(View.VISIBLE);
                    shipmentBtns.setVisibility(View.VISIBLE);
                    titleTv.setText("Shipment Details");
                    nextBtnPick.setVisibility(View.INVISIBLE);
                    closeKeyboard();
                }
            }catch (Exception e) { handleError(e,"nextBtnPick.onClick()"); }
            }
        });

        backBtnShipment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { try {
                closeKeyboard();
                findViewById(R.id.pickup).setVisibility(View.VISIBLE);
                findViewById(R.id.layout_shipment_details).setVisibility(View.GONE);
                shipmentBtns.setVisibility(View.GONE);
                titleTv.setText("Pickup");
                nextBtnPick.setVisibility(View.VISIBLE);
            }catch (Exception e) { handleError(e,"backBtnShipment.onClick()"); }
            }
        });
        submitBtnShipment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { try {
                if (validateShipmentDetails()) {
                    closeKeyboard();
                    submitPickupData();
                }
            }catch (Exception e) { handleError(e,"submitBtnShipment.onClick()"); }
            }
        });
    }catch (Exception e) { handleError(e,"Logout.onClick()"); }
    }

    private void fetchPreviousDetails() throws Exception {

        RadioButton button1 = rg.findViewById(R.id.consignor);
        RadioButton button2 = rg.findViewById(R.id.consignee);

        Log.e("pre",intent.getStringExtra("requested_by"));

        String prev = intent.getStringExtra("requested_by");
        assert prev != null;

        if("consignor".equalsIgnoreCase(prev))   // THIS IS ALWAYS FALSe
        {
            button1.setChecked(true);
            button2.setChecked(false);
            label1.setText("Pickup Location");
            label2.setText("Drop Location");
            isConsignor=true;
        }
        else
        {
            button1.setChecked(false);
            button2.setChecked(true);
            isConsignor=false;
            label2.setText("Pickup Location");
            label1.setText("Drop Location");        }

        contactPerson.setText(intent.getStringExtra("sender_name"));
        contactPerson2.setText(intent.getStringExtra("receiver_name"));

        contactPersonNo.setText(intent.getStringExtra("sender_no"));
        contactPersonNo2.setText(intent.getStringExtra("receiver_no"));

        weight.setText(intent.getStringExtra("weight"));
        noPackages.setText(intent.getStringExtra("quantity"));
        value.setText(intent.getStringExtra("value"));
        specialRemark.setText(intent.getStringExtra("remark"));

        pickUpDate.setText(intent.getStringExtra("pickUpDateTime"));


        invoiceNo.setText(intent.getStringExtra("innoviceNo"));
        saidContains.setText(intent.getStringExtra("saidContains"));
        setLoadType(intent.getStringExtra("loadType"));

    }

    private void setProduct(String productName){
        try {
        for(int i=0;i<productDnames.size();i++)
        {
            if(productName.equals(productDnames.get(i).getProductName()))
            {
                finalProductCode=productDnames.get(i).getProductCode();
                product.setText(productDnames.get(i).getProductName());
            }
        }
        }catch (Exception e) { handleError(e,"setProduct()"); }
    }

    private void setLoadType(String loadType) throws Exception{

        if(loadType.equals("(F/S)"))
        {
            loadSpinner.setSelection(2);
        }
        else
        {
            loadSpinner.setSelection(1);
        }
    }


    private void fetch_Products() {
        final JSONObject object = new JSONObject();
        try {
            //input your API parameters
            object.put("Tokenno",userDetails.getString("TokenNo",""));
        } catch (JSONException e) { handleError(e,"fetch_Products()");
            e.printStackTrace();
        }

        AndroidNetworking.post(APIConstants.ProductList)
                .addJSONObjectBody(object) // posting json
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            JSONArray   data = new JSONArray(response.toString());

                            list_product = new ArrayList<>();
                            productDnames=new ArrayList<>();

                            for(int i=0;i<data.length();i++) {

                                JSONObject obj =data.getJSONObject(i);
                                DataProuduct prouduct=new DataProuduct(obj.getString("ProductCode"),obj.getString("ProductName"));
                                productDnames.add(prouduct);
                                //  Log.e("pro",obj.getString("ProductName"));
                            }
                            //fetch profucts and assign to spinner
                            //if update activity
                            if(previousActivity.equalsIgnoreCase("pickup_details"))
                            {
                                setProduct(intent.getStringExtra("productName"));
                            }
                            product.setClickable(true);


                        } catch (JSONException e) {handleError(e,"fetch_Products()");
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        new ErrorManager(Activity_pickup_request.this,Activity_pickup_request.class.getName(),
                                anError.getClass().toString(),anError.getMessage(),"Network ERROR");
                        Log.e("Error : ",anError.getMessage());
                        //Toast.makeText(Activity_pickup_request.this, "Connection Error", Toast.LENGTH_SHORT).show();

                    }
                });

    }

    private void fetch_WHA_CMA() {

        final JSONObject object = new JSONObject();
        try {
            //input your API parameters
            object.put("CustomerCode",userDetails.getString("CustomerCode",""));
            object.put("TokenNo",userDetails.getString("TokenNo",""));
            object.put("UserId",userDetails.getString("UserID",""));
        } catch (JSONException e) {handleError(e,"fetch_WHA_CMA()");
            e.printStackTrace();
        }

        AndroidNetworking.post(APIConstants.AddressList)
                .addJSONObjectBody(object) // posting json
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            JSONArray   data = new JSONArray(response.toString());

                            list_cma = new ArrayList<DataMaster>();
                            list_wha = new ArrayList<DataMaster>();
                            Dnames1=new ArrayList<>();
                            Dnames2=new ArrayList<>();

                            JSONObject  WHA1=data.getJSONObject(0);
                            for(int i=0;i<WHA1.getJSONArray("WHA").length();i++)
                            {

                                JSONObject obj =WHA1.getJSONArray("WHA").getJSONObject(i);
                                Dnames1.add(obj.getString("Name"));

                                dataMaster = new DataMaster(obj.getString("Name"),obj.getString("Address"),obj.getString("Contact_Name"),obj.getString("MobileNo"),obj.getString("AddressID"),obj.toString());

                                list_wha.add(dataMaster);

                            }

                            JSONObject  CMA1=data.getJSONObject(0);
                            for(int i=0;i<CMA1.getJSONArray("CMA").length();i++)
                            {

                                JSONObject obj =CMA1.getJSONArray("CMA").getJSONObject(i);
                                Dnames2.add(obj.getString("Name"));

                                dataMaster = new DataMaster(obj.getString("Name"),obj.getString("Address"),obj.getString("Contact_Name"),obj.getString("MobileNo"),obj.getString("AddressID"),obj.toString());
                                list_cma.add(dataMaster);


                            }

                            if(previousActivity.equalsIgnoreCase("pickup_details"))
                            {
                                fetchAddressFrom_ID();
                            }
                            pickupTV.setEnabled(true);
                            deliveryTV.setEnabled(true);

                        } catch (JSONException e) {handleError(e,"fetch_WHA_CMA()");
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                       // Log.e("Error : ",anError.getMessage());
                        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_pickup_request.this);
                        builder.setTitle("Network Error");
                        builder.setMessage("Please Check Network Connection")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent intent = new Intent(getApplicationContext(), Activity_Navigation.class);
                                        startActivity(intent);                                            }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                });
    }

    private void fetchAddressFrom_ID() {
        try {
        String pick_id = intent.getStringExtra("pickup_adr_id");
        String del_id = intent.getStringExtra("del_adr_id");
        String req_by = intent.getStringExtra("requested_by");

        assert pick_id != null;
        assert del_id != null;

        Log.e("pick",pick_id);
        Log.e("del",del_id);

        assert req_by != null;

        if(req_by.equalsIgnoreCase("consignor"))
        {
            isConsignor = true;
            for(int i=0;i<list_wha.size();i++)
            {
                if(list_wha.get(i).getAddressID().equalsIgnoreCase(pick_id))
                {
                    pickupAddressID = pick_id;
                    pickupTV.setText("Location : "+list_wha.get(i).getName());
                    address.setText(list_wha.get(i).getAddress());
                }
                Log.e("wha_adr_code",list_wha.get(i).getAddressID());
            }
            for(int i=0;i<list_cma.size();i++)
            {
                if(list_cma.get(i).getAddressID().equalsIgnoreCase(del_id))
                {
                    deliveryAddressID = del_id;
                    deliveryTV.setText("Location : "+list_cma.get(i).getName());
                    address2.setText(list_cma.get(i).getAddress());
                }
                Log.e("cma_adr_code",list_cma.get(i).getAddressID());

            }
        }
        else
        {
            isConsignor = false;

            for(int i=0;i<list_wha.size();i++)
            {
                if(list_wha.get(i).getAddressID().equalsIgnoreCase(pick_id))
                {
                    deliveryAddressID = pick_id;
                    pickupTV.setText("Location : "+list_wha.get(i).getName());
                    address.setText(list_wha.get(i).getAddress());
                }
                Log.e("wha_adr_code",list_wha.get(i).getAddressID());
            }
            for(int i=0;i<list_cma.size();i++)
            {
                if(list_cma.get(i).getAddressID().equalsIgnoreCase(del_id))
                {
                    pickupAddressID = del_id;
                    deliveryTV.setText("Location : "+list_cma.get(i).getName());
                    address2.setText(list_cma.get(i).getAddress());
                }
                Log.e("cma_adr_code",list_cma.get(i).getAddressID());

            }
        }
        }catch (Exception e) { handleError(e,"fetchAddressFrom_ID()"); }
    }

    private void submitPickupData() throws Exception
    {
        String reqId = "";
        String url = APIConstants.PickupRequest;
        if(previousActivity.equalsIgnoreCase("pickup_details"))
        {
            reqId = intent.getStringExtra("requestID");
            url = APIConstants.PickupRequestChange;


        }

        String req_by;

        if(isConsignor)
            req_by = "Consignor";
        else
            req_by = "Consignee";



        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ID",reqId );
            jsonObject.put("DeclareValue",value.getText().toString() );
            jsonObject.put("InvoiceNo",invoiceNo.getText().toString());
            jsonObject.put("LoadType",loadSpinner.getSelectedItem().toString());



            jsonObject.put("Pickup_Date",pickUpDate.getText().toString() );

            jsonObject.put("ProductCode",finalProductCode );
            if(isConsignor)
            {
                jsonObject.put("Receiver_ContactMobileNo",contactPersonNo2.getText().toString());
                jsonObject.put("Receiver_ContactName",contactPerson2.getText().toString() );
                jsonObject.put("Sender_ContactMobileNo",contactPersonNo.getText().toString() );
                jsonObject.put("Sender_ContactName",contactPerson.getText().toString() );
                jsonObject.put("Receiver_AddressID",deliveryAddressID );
                jsonObject.put("Pickup_AddressID", pickupAddressID);
            }
            else
            {
                jsonObject.put("Receiver_ContactMobileNo",contactPersonNo.getText().toString());
                jsonObject.put("Receiver_ContactName",contactPerson.getText().toString() );
                jsonObject.put("Sender_ContactMobileNo", contactPersonNo2.getText().toString());
                jsonObject.put("Sender_ContactName",contactPerson2.getText().toString() );
                jsonObject.put("Receiver_AddressID",pickupAddressID );
                jsonObject.put("Pickup_AddressID",deliveryAddressID );
            }


            jsonObject.put("Remark", specialRemark.getText().toString());
            jsonObject.put("Requested_By",req_by);
            jsonObject.put("SaidContains",saidContains.getText().toString() );

            jsonObject.put("TokenNo",userDetails.getString("TokenNo","") );
            jsonObject.put("UserID", userDetails.getString("UserID",""));
            jsonObject.put("Weight", weight.getText().toString());
            jsonObject.put("Quantity",noPackages.getText().toString() );

        } catch (JSONException e) { handleError(e,"submitPickupData()");
            e.printStackTrace();
        }

        AndroidNetworking.post(url)
                .addJSONObjectBody(jsonObject) // posting json
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject data1 = null;

                        data1=response;
                        String msg;

                        try {
                            if(data1.getBoolean("IsSuccess")) {
                               // Log.e("MSg",data1.getString("Msg"));
                              //  Toast.makeText(getApplicationContext(),data1.getString("Msg"),Toast.LENGTH_LONG).show();

                                msg = data1.getString("Msg");
                                //notification and sqlite insertion
                                if(!previousActivity.equalsIgnoreCase("pickup_details"))
                                {

                                    msg = data1.getString("Msg")+" Generated Successfully. Please note request id.";

                                    Calendar c = Calendar.getInstance();
                                    java.text.SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
                                    String datetime = dateformat.format(c.getTime());

                                    mydb.insertNotification("Pickup Request",datetime,data1.getString("Msg")+" Generated Succesfully!");
                                    createNotificationChannel();
                                    NotificationCompat.Builder builder = new NotificationCompat.Builder(Activity_pickup_request.this, "123")
                                            .setSmallIcon(R.drawable.inland_logo_app)
                                            .setContentTitle("Inland World Logistics")
                                            .setContentText("Pickup Request Generated Successfully")
                                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                                    builder.setAutoCancel(true);
                                    Intent intent = new Intent(getApplicationContext(), Activity_Splash.class);
                                    intent.putExtra("fromActivity","NotificationPickUp");
                                    PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0,intent
                                            , 0);


                                    builder.setContentIntent(contentIntent);
                                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(Activity_pickup_request.this);
                                    // notificationId is a unique int for each notification that you must define
                                    notificationManager.notify(12, builder.build());

                                }


                                //TODO : goto dashnoard from here
                                AlertDialog.Builder builder = new AlertDialog.Builder(Activity_pickup_request.this);
                                builder.setMessage(msg)
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                Intent intent = new Intent(getApplicationContext(), Activity_Navigation.class);
                                                startActivity(intent);                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();

                            }
                        } catch (JSONException e) {handleError(e,"submitPickupData() AndroidNetworking..");
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        new ErrorManager(Activity_pickup_request.this,Activity_pickup_request.class.getName(),
                                anError.getClass().toString(),anError.getMessage(),"Network ERROR");
                        Toast.makeText(getApplicationContext(),"Network Error",Toast.LENGTH_LONG).show();
                        Log.e("ERROR",anError.getErrorDetail());
                    }
                });


    }


    private void createNotificationChannel() { try {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Inland";
            String description = "Logistics";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("123", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }catch (Exception e) { handleError(e,"createNotificationChannel()"); }
    }


    private void createDialog(Dialog dialog, ArrayList<String> List, String headingLabel, TextView tv, int i) throws Exception{
        //dialog creation
        dialog = new Dialog(Activity_pickup_request.this);
        dialog.setContentView(R.layout.locations_dialog_design);
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

        TextView heading = dialog.findViewById(R.id.heading);
        heading.setText(headingLabel);
        RecyclerView rv = dialog.findViewById(R.id.rv_location);
        LinearLayout addBtn = dialog.findViewById(R.id.add_btn);

        if (i == 1) {
            final Dialog finalDialog = dialog;
            addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), Activity_CMA_WHA.class);
                    intent.putExtra("previous", "warehouse_add");
                    startActivityForResult(intent,warehouseCode);
                    finalDialog.dismiss();
                }
            });
        } else if (i == 2) {
            final Dialog finalDialog = dialog;
            addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), Activity_CMA_WHA.class);
                    intent.putExtra("previous", "customer_add");
                    startActivityForResult(intent,customerCode);
                    finalDialog.dismiss();
                }
            });
        }

        activeDialog = dialog;
        activeTextView = tv;
        activeList = List;
        label = headingLabel;
        locationTypeIndex = i;

        //dialog components

        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(Activity_pickup_request.this));
        AdapterLocation adapterLocation = new AdapterLocation(Activity_pickup_request.this, List, Activity_pickup_request.this);

        rv.setAdapter(adapterLocation);
    }

    public boolean validateShipmentDetails() throws Exception{
        if (pickUpDate.getText().toString().equals("")) {
            Toast.makeText(Activity_pickup_request.this, "Enter Pick Up Date!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (weight.getText().toString().equals("")) {
            Toast.makeText(Activity_pickup_request.this, "Enter Weight !", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (noPackages.getText().toString().equals("")) {
            Toast.makeText(Activity_pickup_request.this, "Enter Quantity!", Toast.LENGTH_SHORT).show();
            return false;
        }

         else if (value.getText().toString().equals("")) {
            Toast.makeText(Activity_pickup_request.this, "Enter Declared Value!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (product.getText().toString().equals("")) {
            Toast.makeText(Activity_pickup_request.this, "Select Product!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (loadSpinner.getSelectedItemPosition()==0) {
            Toast.makeText(Activity_pickup_request.this, "Select Load Type!", Toast.LENGTH_SHORT).show();
            return false;
        }else if (saidContains.getText().toString().equals("")) {
            Toast.makeText(Activity_pickup_request.this, "Enter SaidContains!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;

    }

    public boolean validatePickup() throws Exception{
        if (address.getText().toString().equals("")) {
            Toast.makeText(Activity_pickup_request.this, "Enter Address!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (contactPerson.getText().toString().equals("")) {
            Toast.makeText(Activity_pickup_request.this, "Enter Contact Person !", Toast.LENGTH_SHORT).show();
            return false;
        } else if (contactPersonNo.getText().toString().equals("")) {
            Toast.makeText(Activity_pickup_request.this, "Enter Contact No!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (contactPersonNo.getText().toString().length()!=10) {
            Toast.makeText(Activity_pickup_request.this, "Invalid Contact No!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (address2.getText().toString().equals("")) {
            Toast.makeText(Activity_pickup_request.this, "Enter Address!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (contactPerson2.getText().toString().equals("")) {
            Toast.makeText(Activity_pickup_request.this, "Enter Contact Person !", Toast.LENGTH_SHORT).show();
            return false;
        } else if (contactPersonNo2.getText().toString().equals("")) {
            Toast.makeText(Activity_pickup_request.this, "Enter Contact Person No!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (contactPersonNo2.getText().toString().length()!=10) {
            Toast.makeText(Activity_pickup_request.this, "Invalid Contact No!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (pincode1.equalsIgnoreCase(pincode2))
        {
            if(address.getText().toString().equalsIgnoreCase(address2.getText().toString()))
            {
                Toast.makeText(Activity_pickup_request.this, "Address should be different!", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        return true;
    }
    @Override
    public void selectedProduct(int position) {
        product.setText(productDnames.get(position).getProductName());
        finalProductCode=productDnames.get(position).getProductCode();
        dialog.dismiss();
    }

    @Override
    public void selectedLocation(int position) throws JSONException {
       // locationPref = getApplicationContext().getSharedPreferences("LocationManager", MODE_PRIVATE);
//        locationPref.edit().putString("selectedLocation", activeList.get(position).toString().toLowerCase()).commit();
//        locationPref.edit().putInt("locationTypeIndex", locationTypeIndex).commit();

        if(label.equalsIgnoreCase("warehouse location"))
        {
            if(isConsignor)
            {
                activeTextView.setText(activeList.get(position));
                address.setText(list_wha.get(position).getAddress());
                contactPerson.setText(list_wha.get(position).getContactPerson());
                contactPersonNo.setText(list_wha.get(position).getContactNumber());
                pickupAddressID = list_wha.get(position).getAddressID();
            }
            else
            {
                activeTextView.setText(activeList.get(position));
                address.setText(list_wha.get(position).getAddress());
                contactPerson.setText(list_wha.get(position).getContactPerson());
                contactPersonNo.setText(list_wha.get(position).getContactNumber());
                deliveryAddressID = list_wha.get(position).getAddressID();

            }
            JSONObject obj=new JSONObject(list_wha.get(position).getObj());
            pincode1= obj.getString("Pincode");
            wha_position = position;
        }
        else if(label.equalsIgnoreCase("customer location"))
        {
            if(isConsignor) {
                activeTextView.setText(activeList.get(position));
                address2.setText(list_cma.get(position).getAddress());
                contactPerson2.setText(list_cma.get(position).getContactPerson());
                contactPersonNo2.setText(list_cma.get(position).getContactNumber());
                deliveryAddressID = list_cma.get(position).getAddressID();
            }
            else
            {
                activeTextView.setText(activeList.get(position));
                address2.setText(list_cma.get(position).getAddress());
                contactPerson2.setText(list_cma.get(position).getContactPerson());
                contactPersonNo2.setText(list_cma.get(position).getContactNumber());
                pickupAddressID = list_cma.get(position).getAddressID();

            }

            cma_position = position;
            JSONObject obj=new JSONObject(list_cma.get(position).getObj());
            pincode2= obj.getString("Pincode");
        }

        activeDialog.dismiss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {

            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
            else
            {
                fetch_WHA_CMA();
                clearFields();

                   pickupTV.setText("Select Location");
                    deliveryTV.setText("Select Location");
            }
        }catch (Exception e) { handleError(e,"onActivityResult()"); }
    }

    private void closeKeyboard() {

        View view = this.getCurrentFocus();
        if(view!=null)
        {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }

    }

    private void clearFields() throws Exception
    {
        address.setText("");
        address2.setText("");
        contactPersonNo.setText("");
        contactPersonNo2.setText("");
        contactPerson.setText("");
        contactPerson2.setText("");

    }

    private String dateConversion(int year, int month, int day) throws Exception{
        String m=String.valueOf(month);
        String d=String.valueOf(day);
        String y=String.valueOf(year);

        if (m.length()==1)
        {
            m="0"+m;
        }
        if (d.length()==1)
        {
            d="0"+d;
        }

        return d+"/"+m+"/"+y;

    }
    void handleError(Exception e,String loc){
        new ErrorManager(Activity_pickup_request.this,Activity_pickup_request.class.getName(),
                e.getClass().toString(),e.getMessage(),loc);
    }
}
