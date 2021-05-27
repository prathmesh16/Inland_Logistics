package com.iwlpl.connectme.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.iwlpl.connectme.API.APIConstants;
import com.iwlpl.connectme.R;
import com.iwlpl.connectme.errorHandler.ErrorManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class Activity_CMA_WHA extends AppCompatActivity {

    private EditText name,address,gst,pan,contactPerson,contactNo,email,landmark,pincode;
    private TextView state,district,city,latLongMap,header;
    private Button submit,update,delete;
    private String previousActivity;
    private Intent intent;
    private SharedPreferences userDetails;
    private LinearLayout UpdateDelete;
    private ImageButton backBtn;


    private LatLng pos;
    Dialog dialog;

    String stateCode;
    String AddressID;
    String obj;
    String strLatitude,strLongitude;
    JSONObject details;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {

        setContentView(R.layout.activity__cma_wha);
        AndroidNetworking.initialize(getApplicationContext());

        intent = getIntent();
        userDetails=getSharedPreferences("userDetails",MODE_PRIVATE);

        header = findViewById(R.id.header_cma_wha);
        backBtn=findViewById(R.id.backBtnActivity);
        state = findViewById(R.id.state);
        district = findViewById(R.id.district);
        city = findViewById(R.id.city);
        latLongMap= findViewById(R.id.latLongMap);
        strLatitude = "";
        strLongitude = "";

        name = findViewById(R.id.Name);
        address = findViewById(R.id.address);
        gst = findViewById(R.id.GSTNo);
        pan = findViewById(R.id.PANno);
        contactPerson = findViewById(R.id.contactPersonName);
        contactNo = findViewById(R.id.contactNo);
        email = findViewById(R.id.email);
        landmark = findViewById(R.id.landmark);
        pincode = findViewById(R.id.pincode);

        submit = findViewById(R.id.btnAdd);
        update = findViewById(R.id.btnUpdate);
        delete = findViewById(R.id.btnDelete);
        UpdateDelete=findViewById(R.id.update_delete_btns);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { try {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED,returnIntent);
                finish();
            }catch (Exception e) { handleError(e,"backBtn.onClick()"); }
            }
        });

        Intent intent = getIntent();
        previousActivity = intent.getStringExtra("previous");

        pincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (pincode.getText().toString().length()==6)
                {
                    fetchPincodeData(pincode.getText().toString());
                }
                else
                {
                    state.setText("");
                    district.setText("");
                    city.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (pincode.getText().toString().length()==6)
                {
                    fetchPincodeData(pincode.getText().toString());
                }
                else
                {
                    state.setText("");
                    district.setText("");
                    city.setText("");
                }
            }
        });
        if(previousActivity.equalsIgnoreCase("customer_details")||previousActivity.equalsIgnoreCase("warehouse_details"))
        {
            obj=intent.getStringExtra("details");
            try {
                details=new JSONObject(obj);

                //fetch details from warehouse name APIConstants NEEDED
                name.setText(details.getString("Name"));
                gst.setText(details.getString("Gstno"));
                pan.setText(details.getString("Panno"));
                contactPerson.setText(details.getString("Contact_Name"));
                contactNo.setText(details.getString("MobileNo"));
                email.setText(details.getString("EmailID"));
                address.setText(details.getString("Address"));
                landmark.setText(details.getString("Landmark"));
                pincode.setText(details.getString("Pincode"));
                strLatitude = details.getString("Latitude");
                strLongitude = details.getString("Longitude");
                latLongMap.setText(strLatitude +""+strLongitude);
                AddressID = details.getString("AddressID");
                Log.d("abc",details.toString());
                Log.e("Previous","show details");
                submit.setVisibility(View.INVISIBLE);
                UpdateDelete.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                handleError(e,"forgotMPIN.onClick()");
                e.printStackTrace();
            }


        }
        else if(previousActivity.equalsIgnoreCase("customer_add")||previousActivity.equalsIgnoreCase("warehouse_add"))
        {
            Log.e("Previous","add new");
            UpdateDelete.setVisibility(View.GONE);
            submit.setVisibility(View.VISIBLE);

        }

        checkPreviousActivity();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { try {
                closeKeyboard();
                if(validateData())
                {
                    showCustomDialog("ADD DETAILS","Do you want to submit the details ?",getResources().getDrawable(R.drawable.info_icon),"submit");
                }
            }catch (Exception e) { handleError(e,"submit.onClick()"); }
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { try {
                closeKeyboard();
                if(validateData())
                {
                        showCustomDialog("UPDATE DETAILS","Do you want to update the details ?",getResources().getDrawable(R.drawable.change_icon2),"update");

                }
            }catch (Exception e) { handleError(e,"update.onClick()"); }
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { try{
                closeKeyboard();
                showCustomDialog("DELETE RECORD","Do you want to delete record ?",getResources().getDrawable(R.drawable.delete_icon),"delete");
            }catch (Exception e) { handleError(e,"delete.onClick()"); }
            }
        });
        final Dialog dialog = new Dialog(Activity_CMA_WHA.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        /////make map clear
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        dialog.setContentView(R.layout.dialogmap);////your custom content

        MapView mMapView = dialog.findViewById(R.id.mapView);
        MapsInitializer.initialize(Activity_CMA_WHA.this);

        mMapView.onCreate(dialog.onSaveInstanceState());
        mMapView.onResume();


        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap googleMap) { try {
                double lat=18.52;
                double lng=73.85;
                if(!latLongMap.getText().toString().isEmpty())
                {
                    //String[] separated = latLongMap.getText().toString().split(",");
                    lat=Double.parseDouble(strLatitude);
                    lng=Double.parseDouble(strLongitude);
                }

                LatLng posisiabsen = new LatLng(lat,lng);
                pos=posisiabsen;////your lat lng
                googleMap.addMarker(new MarkerOptions().position(posisiabsen).title("Location"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(posisiabsen));
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener(){
                    public void onMapClick(LatLng point){

                        pos=point;
                        googleMap.clear();
                        googleMap.addMarker(new MarkerOptions().position(new LatLng(point.latitude,point.longitude)).title("Location"));
                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(point.latitude,point.longitude)));
                    }
                });

                dialog.findViewById(R.id.setLoc2).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        latLongMap.setText(pos.latitude + "," + pos.longitude);
                        strLatitude = String.valueOf(pos.latitude);
                        strLongitude = String.valueOf(pos.longitude);
//                                        Toast.makeText(Activity_CMA_WHA.this,
//                                                pos.latitude + ", " + pos.longitude,
//                                                Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });

                String apiKey = APIConstants.PlacesAPIKey;

                /**
                 * Initialize Places. For simplicity, the API key is hard-coded. In a production
                 * environment we recommend using a secure mechanism to manage API keys.
                 */
                if (!Places.isInitialized()) {
                    Places.initialize(getApplicationContext(), apiKey);
                }

// Create a new Places client instance.
                // PlacesClient placesClient = Places.createClient(getApplicationContext());
                AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                        getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

                autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

                autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                    @Override
                    public void onPlaceSelected(Place place) {
                        // TODO: Get info about the selected place.
                        Log.i( "a","Place: " + place.getName() + ", " + place.getId()+","+place.getLatLng());

                        //Toast.makeText(Activity_CMA_WHA.this,""+queriedLocation.longitude+" "+queriedLocation.latitude,Toast.LENGTH_SHORT).show();
                        searchLocation(place,googleMap);

                    }

                    @Override
                    public void onError(Status status) {
                        // TODO: Handle the error.
                        Log.i( "a","An error occurred: " + status);
                    }
                });

            }catch (Exception e) { handleError(e,"mMapView.onMapReady()"); }
            }
        });

        latLongMap.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                dialog.show();
                return true;

            }
        });

        contactNo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) { try{
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;
                if(contactNo.getText().toString().isEmpty())
                    return false;
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (contactNo.getRight() - contactNo.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        if (ContextCompat.checkSelfPermission(Activity_CMA_WHA.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(Activity_CMA_WHA.this, new String[]{Manifest.permission.CALL_PHONE},1);
                        }
                        else
                        {
                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:"+contactNo.getText().toString()));
                            startActivity(callIntent);
                            return true;
                        }

                    }
                }
            }catch (Exception e) { handleError(e,"contactNo.onTouch()"); }
                return false;

            }
        });

    }catch (Exception e) { handleError(e,"onCreate()"); }
    }
    public void searchLocation(Place place, GoogleMap mMap) { try {

        LatLng latLng = new LatLng(place.getLatLng().latitude,place.getLatLng().longitude);
        pos=latLng;
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(latLng).title("loc"));
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

    }catch (Exception e) { handleError(e,"searchLocation(...)"); }
    }
    private void showCustomDialog(String heading, String m, Drawable drawable, final String type) throws Exception{

        dialog = new Dialog(Activity_CMA_WHA.this);
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

        //icon
        icon.setImageDrawable(drawable);
        //title
        title.setText(heading);
        //msg
        msg.setText(m);
        //postive btn action
        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { try {
                if(type.equalsIgnoreCase("update")){
                    //updateCustomer();
                    add_update_Customer();
                }
                else if(type.equalsIgnoreCase("submit")){
                    //submitCustomer();
                    add_update_Customer();
                }
                else if(type.equalsIgnoreCase("delete")){
                    deleteCustomer();
                }
                dialog.dismiss();
                closeKeyboard();
            }catch (Exception e) { handleError(e,"positive.onClick()"); }
            }
        });
        //negative btn action
        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { try {
                dialog.dismiss();
                closeKeyboard();
            }catch (Exception e) { handleError(e,"negative.onClick()"); }
            }
        });

        closeKeyboard();

    }
    private void deleteCustomer() throws Exception{
        JSONObject newCustomer=new JSONObject();
        try {
            newCustomer.put("AddressID",AddressID);
            newCustomer.put("TokenNo",userDetails.getString("TokenNo",""));
            newCustomer.put("UserId",userDetails.getString("UserID",""));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(APIConstants.DeleteCustomer)
                .addJSONObjectBody(newCustomer) // posting json
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
                                Toast.makeText(Activity_CMA_WHA.this,data1.getString("Msg"),Toast.LENGTH_SHORT).show();
                                Intent returnIntent = new Intent();
                                setResult(Activity.RESULT_OK,returnIntent);
                                finish();
                            }
                            else
                            {
                                Toast.makeText(Activity_CMA_WHA.this,"Something went wrong !", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            new ErrorManager(Activity_CMA_WHA.this,Activity_Navigation.class.getName(),
                                    e.getClass().toString(),e.getMessage(),"deleteCustomer(){  AndroidNetworking.post(..");
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        new ErrorManager(Activity_CMA_WHA.this,Activity_CMA_WHA.class.getName(),
                                anError.getClass().toString(),anError.getMessage(),"Network ERROR");
                        Toast.makeText(Activity_CMA_WHA.this,"Network ERROR",Toast.LENGTH_LONG).show();

                    }
                });

    }

    private void add_update_Customer()  {

        String url = APIConstants.AddCustomer;
        String AddressId = "";
        JSONObject newCustomer=new JSONObject();

        try {
            previousActivity = intent.getStringExtra("previous");

            assert previousActivity != null;
            if(previousActivity.equalsIgnoreCase("customer_add")||previousActivity.equalsIgnoreCase("customer_details"))
            {
                newCustomer.put("Addresstype","CMA");
            }
            else
            {
                newCustomer.put("Addresstype","WHA");
//
            }

            if(previousActivity.equalsIgnoreCase("customer_details") || previousActivity.equalsIgnoreCase("warehouse_details") )
            {
                url = APIConstants.UpdateCustomer;
                AddressId = details.getString("AddressID");
            }

            newCustomer.put("Address",address.getText().toString());
            newCustomer.put("AddressID",AddressId);
            newCustomer.put("Citycode",city.getText().toString());
            newCustomer.put("Contact_Name",contactPerson.getText().toString());
            newCustomer.put("CustomerCode",userDetails.getString("CustomerCode",""));
            newCustomer.put("EmailID",email.getText().toString());
            newCustomer.put("Gstno",gst.getText().toString());
            newCustomer.put("Landmark",landmark.getText().toString());
            newCustomer.put("MobileNo",contactNo.getText().toString());
            newCustomer.put("Name",name.getText().toString());
            newCustomer.put("Panno",pan.getText().toString());
            newCustomer.put("Pincode",pincode.getText().toString());
            newCustomer.put("Latitude",strLatitude);
            newCustomer.put("Longitude",strLongitude);
            newCustomer.put("Statecode",stateCode);
            newCustomer.put("TokenNo",userDetails.getString("TokenNo",""));
            newCustomer.put("UserId",userDetails.getString("UserID",""));

        } catch (JSONException e) {
            handleError(e,"add_update_Customer()");
            e.printStackTrace();
        }

        AndroidNetworking.post(url)
                .addJSONObjectBody(newCustomer) // posting json
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
                                //Toast.makeText(Activity_CMA_WHA.this,data1.getString("Msg"),Toast.LENGTH_SHORT).show();
                                if(previousActivity.equalsIgnoreCase("customer_add") || previousActivity.equalsIgnoreCase("warehouse_add"))
                                {
                                    Toast.makeText(Activity_CMA_WHA.this,"Data Added Successfully.",Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Toast.makeText(Activity_CMA_WHA.this,"Data Updated Successfully.",Toast.LENGTH_SHORT).show();

                                }
                                Intent returnIntent = new Intent();
                                setResult(Activity.RESULT_OK,returnIntent);
                                finish();
                            }
                            else
                            {
                                Toast.makeText(Activity_CMA_WHA.this,"Something went wrong !", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            handleError(e,"add_update_Customer() AndroidNetworking.post(...");
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(Activity_CMA_WHA.this,"Network ERROR",Toast.LENGTH_LONG).show();
                        new ErrorManager(Activity_CMA_WHA.this,Activity_CMA_WHA.class.getName(),
                                anError.getClass().toString(),anError.getMessage(),"Network ERROR");
                    }
                });


    }

    public void fetchPincodeData(final String pincode)
    {
       final String Token = userDetails.getString("TokenNo", "");

        final JSONObject newPincode=new JSONObject();
        try {
            newPincode.put("PinCode",pincode);
            newPincode.put("TokenNo",Token);
        } catch (JSONException e) { handleError(e,"fetchPincodeData()");
            e.printStackTrace();
        }

        AndroidNetworking.post(APIConstants.Pincode)
                .addJSONObjectBody(newPincode) // posting json
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        JSONObject obj = null;
                        try {
                            obj = response.getJSONObject(0);
                            obj.getString("statename");
                            Log.e("state",obj.getString("statename"));

                            state.setText(obj.getString("statename"));
                            district.setText(obj.getString("districtname"));
                            city.setText(obj.getString("Cityname"));
                            stateCode = obj.getString("statecode");
                        } catch (JSONException e) {
                            handleError(e,"fetchPincodeData() AndroidNetworking.post(..");
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(Activity_CMA_WHA.this, "Pincode Fetch Error", Toast.LENGTH_SHORT).show();
                        new ErrorManager(Activity_CMA_WHA.this,Activity_CMA_WHA.class.getName(),
                                anError.getClass().toString(),anError.getMessage(),"Network ERROR");
                    }
                });

    }

    public void checkPreviousActivity() throws Exception
    {
        previousActivity = intent.getStringExtra("previous");

        assert previousActivity != null;

        if(previousActivity.equalsIgnoreCase("customer_add"))
        {
            header.setText("Add Customer");
        }
        else if(previousActivity.equalsIgnoreCase("customer_details"))
        {
            header.setText("Customer Details");
        }
        else if(previousActivity.equalsIgnoreCase("warehouse_add"))
        {
            header.setText("Add Warehouse");
        }
        else if(previousActivity.equalsIgnoreCase("warehouse_details"))
        {
            header.setText("Warehouse Details");

        }
    }

    public boolean validateData() throws Exception{
        if(name.getText().toString().equals(""))
        {
            Toast.makeText(Activity_CMA_WHA.this,"Enter Name!",Toast.LENGTH_LONG).show();
            return false;
        }
        else if(gst.getText().toString().equals("") && pan.getText().toString().equals(""))
        {
            Toast.makeText(Activity_CMA_WHA.this,"Enter GST No or Pan No",Toast.LENGTH_LONG).show();
            return false;
        }
//        else if(gst.getText().toString().equals(""))
//        {
//            Toast.makeText(Activity_CMA_WHA.this,"Enter GST No",Toast.LENGTH_LONG).show();
//            return false;
//        }
        else if(!(gst.getText().toString().equals("")) && gst.getText().toString().length()!=15)
        {
            Toast.makeText(Activity_CMA_WHA.this,"GST No Must be 15 Digits",Toast.LENGTH_LONG).show();
            return false;
        }
//        else if(pan.getText().toString().equals(""))
//        {
//            Toast.makeText(Activity_CMA_WHA.this,"Enter PAN No",Toast.LENGTH_LONG).show();
//            return false;
//        }
        else if(!(pan.getText().toString().equals("")) &&pan.getText().toString().length()!=10)
        {
            Toast.makeText(Activity_CMA_WHA.this,"PAN No Must be 10 Digits",Toast.LENGTH_LONG).show();
            return false;
        }
        else if(contactPerson.getText().toString().equals(""))
        {
            Toast.makeText(Activity_CMA_WHA.this,"Enter Contact Person Name!",Toast.LENGTH_LONG).show();
            return false;
        }
        else if(contactNo.getText().toString().equals(""))
        {
            Toast.makeText(Activity_CMA_WHA.this,"Enter Contact No",Toast.LENGTH_LONG).show();
            return false;
        }
        else if(contactNo.getText().toString().length()!=10)
        {
            Toast.makeText(Activity_CMA_WHA.this,"Invalid Contact No !",Toast.LENGTH_LONG).show();
            return false;
        }
        else if(email.getText().toString().equals(""))
        {
            Toast.makeText(Activity_CMA_WHA.this,"Enter Email",Toast.LENGTH_LONG).show();
            return false;
        }
        else if(address.getText().toString().equals(""))
        {
            Toast.makeText(Activity_CMA_WHA.this,"Enter Address",Toast.LENGTH_LONG).show();
            return false;
        }
        else if(landmark.getText().toString().equals(""))
        {
            Toast.makeText(Activity_CMA_WHA.this,"Enter Landmark",Toast.LENGTH_LONG).show();
            return false;
        }
        else if(pincode.getText().toString().equals(""))
        {
            Toast.makeText(Activity_CMA_WHA.this,"Enter Pincode",Toast.LENGTH_LONG).show();
            return false;
        }
        else if(pincode.getText().toString().length()!=6)
        {
            Toast.makeText(Activity_CMA_WHA.this,"Enter Correct Pincode",Toast.LENGTH_LONG).show();
            return false;
        }
        else if(state.getText().toString().equals(""))
        {
            Toast.makeText(Activity_CMA_WHA.this,"Invalid Pincode",Toast.LENGTH_LONG).show();
            return false;
        }
        else if(district.getText().toString().equals(""))
        {
            Toast.makeText(Activity_CMA_WHA.this,"Invalid Pincode",Toast.LENGTH_LONG).show();
            return false;
        }
        else if(city.getText().toString().equals(""))
        {
            Toast.makeText(Activity_CMA_WHA.this,"Invalid Pincode",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void closeKeyboard() {

        View view = this.getCurrentFocus();
        if(view!=null)
        {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED,returnIntent);
        finish();

    }
    void handleError(Exception e,String loc){
        new ErrorManager(Activity_CMA_WHA.this,Activity_CMA_WHA.class.getName(),
                e.getClass().toString(),e.getMessage(),loc);

    }
}
