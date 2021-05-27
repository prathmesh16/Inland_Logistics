package com.iwlpl.connectme.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.TypedValue;
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
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.qrcode.ByteArray;
import com.iwlpl.connectme.API.APIConstants;
import com.iwlpl.connectme.R;
import com.iwlpl.connectme.adapter.AdapterTrackPoints;
import com.iwlpl.connectme.data_handler.DataTrackPoints;
import com.iwlpl.connectme.errorHandler.ErrorManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

public class Activity_Track extends AppCompatActivity {

    private String docket_no;
    private TextView trackNo,sender,receiver,source,dest,docketDate,packages,weight,saidContains,value,currentLoc;
    private SharedPreferences userDetails;
    private RecyclerView recyclerView;
    private ArrayList<DataTrackPoints> listPoints;
    private DataTrackPoints dataTrackPoints;
    private AdapterTrackPoints adapterTrackPoints;
    private CardView cvDetails;
    private ProgressBar progressBar;
    private TextView lastStatus;
    private EditText etDocket;
    private ImageButton back;
    private Button liveTrackBtn, viewPod;

    private String podFront, podBack;

    static Image image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
        setContentView(R.layout.activity__track);
        AndroidNetworking.initialize(getApplicationContext());

        final Intent intent = getIntent();
        docket_no =  intent.getStringExtra("docket_no");
        userDetails = getSharedPreferences("userDetails",MODE_PRIVATE);

        podBack="";
        podBack="";

        etDocket = findViewById(R.id.etDocketTrack);
        back = findViewById(R.id.imageButton);

        trackNo = findViewById(R.id.tvTrackNo);
        sender = findViewById(R.id.tvTrackSenderName);
        source = findViewById(R.id.tvTrackFrom);
        dest = findViewById(R.id.tvTrackTo);
        docketDate = findViewById(R.id.tvTrackDate);
        receiver = findViewById(R.id.tvTrackReceiverName);
        packages = findViewById(R.id.tvTrackPackages);
        weight = findViewById(R.id.tvTrackWeight);
        saidContains = findViewById(R.id.tvTrackSaidcontain);
        value = findViewById(R.id.tvTrackValue);
        currentLoc = findViewById(R.id.tvTrackCurrentLoc);
        cvDetails = findViewById(R.id.CardViewTrack);
//        cvCurrent = findViewById(R.id.cardViewCurrentLoc);
        progressBar = findViewById(R.id.pbTrack);
        lastStatus = findViewById(R.id.tvTrackLastStatus);
        liveTrackBtn=findViewById(R.id.liveTrackBtn);
        viewPod = findViewById(R.id.viewPodBtn);

        recyclerView = findViewById(R.id.rvTrack);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fetchDocketData();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { try {
                //finish();
                Intent intent1  = new Intent(Activity_Track.this, Activity_Navigation.class);
                startActivity(intent1);
            }catch (Exception e) { handleError(e,"back.onClick()"); }
            }
        });

        etDocket.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) { try {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;


                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    if (event.getRawX() >= (etDocket.getRight() - etDocket.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

                        if (etDocket.getText().toString().isEmpty()) {
                            Toast.makeText(Activity_Track.this, "Please Enter Docket/Ref No. !", Toast.LENGTH_SHORT).show();
                            return false;

                        }
                        else
                        {
                            cvDetails.setVisibility(View.GONE);
                            progressBar.setVisibility(View.VISIBLE);
                            docket_no = etDocket.getText().toString();
                            fetchDocketData();
                            return true;
                        }

                    }

                }
            }catch (Exception e) { handleError(e,"etDocket.onTouch()"); }
                return false;
            }
        });

        viewPod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { try {

                if(podFront!="" )
                {
                    //TODO : SHOW POD IMAGES HERe

                    Log.e("front :",podFront);
                    Log.e("back :",podBack);

                    final Dialog dialog = new Dialog(Activity_Track.this);
                    dialog.setContentView(R.layout.dilog_pod);
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

                    ImageView backBtn,downloadBtn,shareBtn;
                    final com.jsibbold.zoomage.ZoomageView front,back;
                    backBtn=dialog.findViewById(R.id.back_btn);
                    downloadBtn=dialog.findViewById(R.id.download_btn);
                    //shareBtn=dialog.findViewById(R.id.share_btn);
                    front=dialog.findViewById(R.id.pod_front);
                    back=dialog.findViewById(R.id.pod_back);

                    backBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });

                    //TODO : Test tracking no : C278000054

                    //loading images---

                    final String frontUrl;
                    final String backUrl;

                    frontUrl=podFront.replace("http","https");
                   backUrl= podBack.replace("http","https");

                    Log.e("front :",frontUrl);
                    Log.e("back :",backUrl);

                    Glide.with(Activity_Track.this).load(frontUrl).into(front);
                    Glide.with(Activity_Track.this).load(backUrl).into(back);
                    downloadBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) { try {

                    Toast.makeText(Activity_Track.this,"Downloading POD , Please Wait...",Toast.LENGTH_LONG).show();


//                                URL url = new URL(podFront);
//                                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//                                URL url = new URL(frontUrl);
//                                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                                connection.setDoInput(true);
//                                connection.connect();
//                                InputStream input = connection.getInputStream();
//                                Bitmap bmp = BitmapFactory.decodeStream(input);
                                Glide.with(Activity_Track.this)
                                        .asBitmap()
                                        .load(frontUrl)
                                        .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(Bitmap bmp, Transition<? super Bitmap> transition) {
                                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                                byte[] byteArray = stream.toByteArray();
                                                bmp.recycle();
                                                Document document = new Document();
                                                String FILE = Environment.getExternalStorageDirectory()+"/"+trackNo.getText().toString()+"Front.pdf";
                                                try {
                                                    PdfWriter.getInstance(document, new FileOutputStream(FILE));
                                                } catch (DocumentException e) { handleError(e,"viewPod.onClick()");
                                                    e.printStackTrace();
                                                    Log.e("erro","1");
                                                } catch (FileNotFoundException e) {
                                                    Log.e("erro","2");
                                                    e.printStackTrace();
                                                }
                                                document.open();

                                                try
                                                {
                                                    image = Image.getInstance(byteArray);  ///Here i set byte array..you can do bitmap to byte array and set in image...
                                                }
                                                catch (BadElementException e)
                                                {
                                                    // TODO Auto-generated catch block
                                                    handleError(e,"viewPod.onClick()");
                                                    Log.e("erro","1");
                                                    e.printStackTrace();
                                                }
                                                catch (MalformedURLException e)
                                                { handleError(e,"viewPod.onClick()");
                                                    // TODO Auto-generated catch block
                                                    Log.e("erro","1");
                                                    e.printStackTrace();
                                                }
                                                catch (IOException e)
                                                { handleError(e,"viewPod.onClick()");
                                                    Log.e("erro","1");

                                                    // TODO Auto-generated catch block
                                                    e.printStackTrace();
                                                }
                                                // image.scaleAbsolute(150f, 150f);
                                                image.scalePercent(80);
                                                try
                                                {
                                                    document.add(image);
                                                    Log.e("pdf","done");
                                                } catch (DocumentException e) { handleError(e,"viewPod.onClick()");
                                                    // TODO Auto-generated catch block
                                                    Log.e("erro","1");
                                                    e.printStackTrace();
                                                }
                                                document.close();
                                            }


                                        });
                            Glide.with(Activity_Track.this)
                                    .asBitmap()
                                    .load(backUrl)
                                    .into(new SimpleTarget<Bitmap>() {
                                        @Override
                                        public void onResourceReady(Bitmap bmp, Transition<? super Bitmap> transition) {
                                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                            byte[] byteArray = stream.toByteArray();
                                            bmp.recycle();
                                            Document document = new Document();
                                            String FILE = Environment.getExternalStorageDirectory()+"/"+trackNo.getText().toString()+"Back.pdf";
                                            try {
                                                PdfWriter.getInstance(document, new FileOutputStream(FILE));
                                            } catch (DocumentException e) { handleError(e,"viewPod.onClick()");
                                                e.printStackTrace();
                                            } catch (FileNotFoundException e) { handleError(e,"viewPod.onClick()");
                                                e.printStackTrace();
                                            }
                                            document.open();

                                            try
                                            {
                                                image = Image.getInstance(byteArray);  ///Here i set byte array..you can do bitmap to byte array and set in image...
                                            }
                                            catch (BadElementException e)
                                            { handleError(e,"viewPod.onClick()");
                                                // TODO Auto-generated catch block
                                                e.printStackTrace();
                                            }
                                            catch (MalformedURLException e)
                                            { handleError(e,"viewPod.onClick()");
                                                // TODO Auto-generated catch block
                                                e.printStackTrace();
                                            }
                                            catch (IOException e)
                                            { handleError(e,"viewPod.onClick()");
                                                // TODO Auto-generated catch block
                                                e.printStackTrace();
                                            }
                                            // image.scaleAbsolute(150f, 150f);
                                            image.scalePercent(80);
                                            try
                                            {
                                                document.add(image);
                                                Log.e("pdf","done");
                                                dialog.dismiss();
                                                Toast.makeText(Activity_Track.this,"Downloaded.Files downloaded at path : "+FILE,Toast.LENGTH_LONG).show();
                                            } catch (DocumentException e) { handleError(e,"viewPod.onClick()");
                                                // TODO Auto-generated catch block
                                                e.printStackTrace();
                                            }
                                            document.close();
                                        }


                                    });
//                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                                byte[] byteArray = stream.toByteArray();
//                                bmp.recycle();
//                                Document document = new Document();
//                                PdfWriter.getInstance(document, new FileOutputStream(FILE));
//                                document.open();
//
//                                try
//                                {
//                                    image = Image.getInstance(byteArray);  ///Here i set byte array..you can do bitmap to byte array and set in image...
//                                }
//                                catch (BadElementException e)
//                                {
//                                    // TODO Auto-generated catch block
//                                    e.printStackTrace();
//                                }
//                                catch (MalformedURLException e)
//                                {
//                                    // TODO Auto-generated catch block
//                                    e.printStackTrace();
//                                }
//                                catch (IOException e)
//                                {
//                                    // TODO Auto-generated catch block
//                                    e.printStackTrace();
//                                }
//                                // image.scaleAbsolute(150f, 150f);
//                                try
//                                {
//                                    document.add((Element) front);
//                                    Log.e("pdf","done");
//                                } catch (DocumentException e) {
//                                    // TODO Auto-generated catch block
//                                    e.printStackTrace();
//                                }
//                                document.close();


                        }catch (Exception e) { handleError(e,"viewPod.onClick()"); }
                        }
                    });
                }
                else
                {
                    //POD IMAGE NOT AVALIBLE
                    Toast.makeText(Activity_Track.this, "POD Not Found.", Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e) { handleError(e,"viewPod.onClick()"); }
            }
        });

        liveTrackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { try {


               final Dialog dialog=new Dialog(Activity_Track.this);
                fetchLiveTrack(dialog);
                dialog.setContentView(R.layout.live_track_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCancelable(false);
                Window window = dialog.getWindow();
                window.setGravity(Gravity.CENTER);
                window.getAttributes().windowAnimations=R.style.DialogAnimation;
                final WindowManager.LayoutParams params = window.getAttributes();
                params.height = WindowManager.LayoutParams.WRAP_CONTENT;
                params.width = WindowManager.LayoutParams.MATCH_PARENT;
                window.setAttributes(params);
                dialog.show();
                Button CloseBtn=dialog.findViewById(R.id.dialog_button_positive);
                CloseBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       dialog.dismiss();
                    }
                });
            }catch (Exception e) { handleError(e,"liveTrackBtn.onClick()"); }
            }
        });
    }catch (Exception e) { handleError(e,"onCreate)"); }
    }
    private static void addImage(Document document, ByteArray byteArray)
    {


    }
    private void fetchLiveTrack(final Dialog dialog) {

        JSONObject newDocket=new JSONObject();
        try {
            newDocket.put("CustomerCode",userDetails.getString("CustomerCode",""));
            newDocket.put("DocketNo",docket_no);
            newDocket.put("TokenNo",userDetails.getString("TokenNo",""));

        } catch (JSONException e) { handleError(e,"fetchLiveTrack)");
            e.printStackTrace();
        }

        AndroidNetworking.post(APIConstants.LiveTrack)
                .addJSONObjectBody(newDocket) // posting json
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        TableLayout table = dialog.findViewById(R.id.tracking_tbl);
                        table.setStretchAllColumns(true);
                        Typeface typeface = ResourcesCompat.getFont(Activity_Track.this, R.font.nunito_semibold);
                        for(int i=0;i<response.length();i++)
                        {
                            try {
                                JSONObject obj =response.getJSONObject(i);

                                String dt = obj.getString("Date");
                                String adr = obj.getString("Address");

                                //TODO : Add date and address to table row here

                                TableRow tableRow = new TableRow(getApplicationContext());
                                tableRow.setGravity(Gravity.START);
                                if (i % 2 == 0)
                                    tableRow.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.white));
                                else
                                    tableRow.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.faint));

                                tableRow.setPadding(dptoPixel(0), dptoPixel(4), dptoPixel(0), dptoPixel(4));
                                TextView date = new TextView(getApplicationContext());
                                date.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                                date.setText(dt);
                                date.setPadding(dptoPixel(0), dptoPixel(0), dptoPixel(10), dptoPixel(0));
                                date.setGravity(Gravity.START);
                                date.setTextColor(getApplicationContext().getResources().getColor(R.color.heading));
                                date.setTypeface(typeface);
                                date.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
                                tableRow.addView(date);
                                
                                TextView address = new TextView(getApplicationContext());
                                address.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                                address.setText(adr);
                                address.setGravity(Gravity.START);
                                address.setTextColor(getApplicationContext().getResources().getColor(R.color.heading));
                                address.setTypeface(typeface);
                                address.setWidth(WindowManager.LayoutParams.MATCH_PARENT);

                               // address.setSingleLine(false);
                               // address.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);

                                tableRow.addView(address);

                                table.addView(tableRow);

                                Log.e("date - adr",dt+" "+adr);
                            } catch (JSONException e) { handleError(e,"fetchLiveTrack)");
                                e.printStackTrace();
                            }

                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        new ErrorManager(Activity_Track.this,Activity_Track.class.getName(),
                                anError.getClass().toString(),anError.getMessage(),"Network ERROR");
                        Toast.makeText(Activity_Track.this, "Network error", Toast.LENGTH_SHORT).show();
                    }
                });


    }
    private int dptoPixel(int p) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, p, this.getApplicationContext().getResources().getDisplayMetrics());
    }
    private void closeKeyboard() {

        View view = this.getCurrentFocus();
        if(view!=null)
        {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }
    private void fetchDocketData() {
        closeKeyboard();
        etDocket.clearFocus();
        lastStatus.setText("Delivery Status : ");


        JSONObject newDocket=new JSONObject();
        try {
            newDocket.put("CustomerCode",userDetails.getString("CustomerCode",""));
            newDocket.put("DocketNo",docket_no);
            newDocket.put("TokenNo",userDetails.getString("TokenNo",""));

        } catch (JSONException e) { handleError(e,"fetchDocketData()");
            e.printStackTrace();
        }

        AndroidNetworking.post(APIConstants.DocketTrack)
                .addJSONObjectBody(newDocket) // posting json
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {



                            JSONArray   data = null;
                            try {
                                data = new JSONArray(response.toString());

                                listPoints = new ArrayList<>();

                                JSONObject  dockets=data.getJSONObject(0);

                                if(dockets.getString("Docketno")!="null")
                                {
                                    //TODO : GET STATIC DATA HERE
                                    trackNo.setText(dockets.getString("Docketno"));
                                    sender.setText("Sender : "+dockets.getString("SenderName"));
                                    source.setText("From : "+dockets.getString("FromLocation"));
                                    dest.setText("To : "+dockets.getString("ToLocation"));
                                    docketDate.setText(dockets.getString("Docketdate"));
                                    receiver.setText("Receiver : "+dockets.getString("ReciverName"));
                                    packages.setText("Packages : "+dockets.getString("Packages"));
                                    weight.setText("Weight : "+dockets.getString("Weight"));
                                    saidContains.setText("Said Contains : "+dockets.getString("Saidtocontain"));
                                    value.setText("Declared Value : "+dockets.getString("Declarevalue"));

                                    podFront = dockets.getString("PODFImg");
                                    podBack = dockets.getString("PODBImg");

                                    Log.e("front :",podFront);
                                    Log.e("back :",podBack);

                                    if(podFront.isEmpty())
                                    {
                                        viewPod.setVisibility(View.GONE);
                                    }
                                    else
                                    {
                                        viewPod.setVisibility(View.VISIBLE);
                                    }

                                    if(dockets.getString("CurrentLocation").equalsIgnoreCase(""))
                                    {
                                        currentLoc.setVisibility(View.GONE);
                                    }
                                    else
                                    {
                                        currentLoc.setText("Current Location : "+dockets.getString("CurrentLocation"));
                                    }


                                    for(int i=0;i<dockets.getJSONArray("DocketList").length();i++)
                                    {

                                        // TODO : ADD TRACK POINTS LIST HERE DYNAMICALLY
                                        JSONObject obj =dockets.getJSONArray("DocketList").getJSONObject(i);
                                        dataTrackPoints = new DataTrackPoints(obj.getString("Status"),obj.getString("ORG"),obj.getString("Arrivaltime"));
                                        listPoints.add(dataTrackPoints);

                                        if(dataTrackPoints.getStatus().contains("Delivered"))
                                        {
                                            lastStatus.setText("Delivery Status : Delivered");
                                            Log.e("status : ",dataTrackPoints.getStatus());
                                        }

                                    }
                                   // lastStatus.append(dataTrackPoints.getStatus() +" "+ dataTrackPoints.getOrogin() + " (" + dataTrackPoints.getDate()+")");

                                    adapterTrackPoints = new AdapterTrackPoints(getApplicationContext(),listPoints);
                                    recyclerView.setAdapter(adapterTrackPoints);

                                    cvDetails.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                }
                                else
                                {
                                    Toast.makeText(Activity_Track.this, "data not found", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
//                                    Intent intent = new Intent(Activity_Track.this,Activity_Navigation.class);
//                                    startActivity(intent);

                                }


                            } catch (JSONException e) { handleError(e,"fetchDocketData()");
                                e.printStackTrace();
                            }

                    }

                    @Override
                    public void onError(ANError anError) {
                        progressBar.setVisibility(View.GONE);
                        new ErrorManager(Activity_Track.this,Activity_Track.class.getName(),
                                anError.getClass().toString(),anError.getMessage(),"Network ERROR");
                        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Track.this);
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
    void handleError(Exception e,String loc){
        new ErrorManager(Activity_Track.this,Activity_Track.class.getName(),
                e.getClass().toString(),e.getMessage(),loc);
    }
}
