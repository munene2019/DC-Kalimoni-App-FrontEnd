package com.example.LoanApp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PayLoan extends AppCompatActivity {
    Button btnApply;
    Dialog dialog;
    String mess;
    String statusCode;
    private static final String BASE_URL = "http://192.168.1.32:7001/Loan_App_API/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan);
        btnApply = findViewById(R.id.btnApply);
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallRetrofit();
            }
        });


    }

    private void CallRetrofit() {
        EditText LoanAmount= findViewById(R.id.LoanAmount);
        String LoanAmount2=LoanAmount.getText().toString();
        String service = "Pay loan";
        //String LoanAccount="";
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("service",service);
            // jsonRequest.put("phoneNumber", phoneNumber2);
            jsonRequest.put("LoanAmount", LoanAmount2);
            Log.i("Request",jsonRequest.toString());

            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());
            Retrofit retrofit = builder.build();


            LoginApi loginUrl= retrofit.create(LoginApi.class);

            HashMap<String, String> headMap = new HashMap<>();

            headMap.put("Content-Type", "application/json");

            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonRequest.toString());
            Call<ResponseBody> call = loginUrl.Login(body);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        Log.i("DIRECT RESPONSE", response.toString());
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        JSONObject dataObject = jsonObject.getJSONObject("data");
                        statusCode = jsonObject.getString("status");
                        Log.i("MESSAGECODE", statusCode);
                        Log.i("name",statusCode);
                        String message = dataObject.getString("message");
                        mess = message;
                        showCustomDialog(PayLoan.this);
                        Log.i("MESSAGE", dataObject.getString("message"));
                    } catch (Exception ex) {
                        Log.i("EXCEPT",ex.toString());
                        statusCode = "300";
                        mess =ex.toString();
                        showCustomDialog(PayLoan.this);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    statusCode = "300";
                    mess =t.toString();
                    showCustomDialog(PayLoan.this);
                }
            });
        } catch (Exception ex) {
            statusCode = "300";
            mess = ex.toString();
            showCustomDialog(PayLoan.this);
        }
    }
    public void showCustomDialog(final Context context) {
        final Dialog dialog = new Dialog(context);
        /// Toast.makeText(MainActivity.this, "Client will provide this ☺", Toast.LENGTH_SHORT).show();

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_dialog, null, false);
        dialog.setContentView(view);
        final Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        dialog.show();
        // Button applyloan=findViewById(R.id.btnApply);
        Button okey = dialog.findViewById(R.id.okay);
        Button cancel = dialog.findViewById(R.id.cancel);
        TextView text = dialog.findViewById(R.id.text);
        text.setText(mess);
        okey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i;
                i = new Intent(PayLoan.this, loanApplication_page.class);
                if (statusCode.equalsIgnoreCase("1")) {
                    startActivity(i);
                    dialog.dismiss();
                    startActivity(i);
                } else {
                    dialog.dismiss();
                }
                // Toast.makeText(loanApplication_page.this, "Yess", Toast.LENGTH_LONG);

            }
        });



        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}

