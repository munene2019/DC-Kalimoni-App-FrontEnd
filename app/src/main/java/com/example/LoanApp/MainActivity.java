package com.example.LoanApp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity {
    Button btnSign;
    TextView textView;
    EditText text;
    Dialog dialog;
    String mess;
    String statusCode;
    Button btnRegister;
    private static final String BASE_URL = "http://192.168.1.3:7001/Loan_App_API/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSign = findViewById(R.id.btnsignin);
        btnSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallRetrofit();
            }
        });
        btnRegister=findViewById(R.id.register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i;
                i= new Intent(MainActivity.this, Registration_page.class);
                startActivity(i);
            }
        });


        }
//        btnSign.setOnClickListener(new View.OnClickListener() {
//           @Override
//            public void onClick(View v) {
//             CallRetrofit();
//            }
//        });




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
        Button okey = dialog.findViewById(R.id.okay);
        Button cancel = dialog.findViewById(R.id.cancel);
        TextView text = dialog.findViewById(R.id.text);
        text.setText(mess);
        okey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i;
                i = new Intent(MainActivity.this, Home_page.class);
                if (statusCode.equalsIgnoreCase("1")) {
                    startActivity(i);
                    dialog.dismiss();
                    startActivity(i);
                } else {
                    dialog.dismiss();
                }

                Toast.makeText(MainActivity.this, "Yess", Toast.LENGTH_LONG);

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void CallRetrofit() {
        //String  phoneNumber=findViewById(R.id.accountnumber1).toString();
      // String pin=findViewById(R.id.accountnumber2).toString();
        EditText phoneNumber2 = findViewById(R.id.accountnumber1);
        String phoneNumber=phoneNumber2.getText().toString();
        EditText pin2 = findViewById(R.id.accountnumber2);
        String pin=pin2.getText().toString();
       // String pin = "12345";
       // final String phoneNumber = "0712622099";
        String uuid = "12244";
        String service = "login";

        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("service", service);
           // jsonRequest.put("phoneNumber", phoneNumber2);
            jsonRequest.put("phoneNumber", phoneNumber);
            jsonRequest.put("pin", pin);
            jsonRequest.put("uuid", uuid);

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
                        showCustomDialog(MainActivity.this);
                        Log.i("MESSAGE", dataObject.getString("message"));
                    } catch (Exception ex) {
                        Log.i("EXCEPT",ex.toString());
                        statusCode = "300";
                        mess =ex.toString();
                        showCustomDialog(MainActivity.this);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    statusCode = "300";
                    mess =t.toString();
                     showCustomDialog(MainActivity.this);
                }
            });
        } catch (Exception ex) {
            statusCode = "300";
            mess = ex.toString();
             showCustomDialog(MainActivity.this);
        }
    }
}
