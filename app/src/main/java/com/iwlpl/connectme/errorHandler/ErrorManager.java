package com.iwlpl.connectme.errorHandler;

import android.content.Context;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.iwlpl.connectme.API.APIConstants;
import com.iwlpl.connectme.data_handler.ErrorDetails;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.content.Context.MODE_PRIVATE;

public class ErrorManager {
    public static DBHelper dbHelper;
    Context mContext;
    private SharedPreferences userDetails;
    ArrayList<ErrorDetails> data;

    public ErrorManager(Context mContext) {      try {
        this.mContext = mContext;
        dbHelper = new DBHelper(mContext);
        userDetails=mContext.getSharedPreferences("userDetails",MODE_PRIVATE);

            uploadOnline();

        } catch (Exception e) { e.printStackTrace(); }
    }

    public ErrorManager(Context context, String activity_name, String error_class, String error_message, String location) {
        try {
        dbHelper = new DBHelper(context);
        mContext = context;
            userDetails=context.getSharedPreferences("userDetails",MODE_PRIVATE);

            dbHelper.insertData(activity_name, error_class, error_message,userDetails.getString("CustomerCode",""), location);

            uploadOnline();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public boolean isConnected()  throws Exception{
        ConnectivityManager connector = (ConnectivityManager) mContext.getSystemService(CONNECTIVITY_SERVICE);
        assert connector != null;
        NetworkInfo info = connector.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }

    private void uploadOnline() throws Exception{
        if(isConnected()) {
            data = dbHelper.getAllData();
            if (!data.isEmpty()) {
                for (ErrorDetails er : data) {
                    submitdata(er.getActivity_name(), er.getError_class(), er.getError_message(), er.getCustomerCode(), er.getDate(), userDetails.getString("UserID", ""));
                    dbHelper.deleteData(er.getId());
                }

            }
        }
    }

    private void submitdata(String activity_name, String error_class, String error_message, String customerCode,String TimeStamp,String userId) throws JSONException {

        JSONObject newErrorlog=new JSONObject();

        newErrorlog.put("Activity_name",activity_name);
        newErrorlog.put("CustomerCode",customerCode);
        newErrorlog.put("Date",TimeStamp);
        newErrorlog.put("Error_class",error_class);
        newErrorlog.put("Error_message",error_message);
        newErrorlog.put("Pagename","");
        newErrorlog.put("Servicename","");
        newErrorlog.put("UserId",userId);



        AndroidNetworking.post(APIConstants.ErrorLog)
                .addJSONObjectBody(newErrorlog) // posting json
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

                            }
                            else
                            {
                                Toast.makeText(mContext,"Something went wrong !", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) { Toast.makeText(mContext, "handErr"+e, Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(mContext,"ErrorManager Class "+anError,Toast.LENGTH_LONG).show();
                    }
                });

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public  String getCurrentTimeStamp(){
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentDateTime = dateFormat.format(new Date()); // Find todays date

            return currentDateTime;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }
}
