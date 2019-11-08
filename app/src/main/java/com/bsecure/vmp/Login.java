package com.bsecure.vmp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bsecure.vmp.interfaces.RequestHandler;
import com.bsecure.vmp.volleyhttp.Constants;
import com.bsecure.vmp.volleyhttp.MethodResquest;

import org.json.JSONObject;

public class Login extends AppCompatActivity implements RequestHandler{

    private EditText phoneno;

    private Button send;

    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        phoneno = findViewById(R.id.phoneno);

        send = findViewById(R.id.send);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                phone = phoneno.getText().toString().trim();

                if(TextUtils.isEmpty(phone))
                {
                    Toast.makeText(Login.this, "Please Enter Phone Number", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(phone.length() < 10)
                {
                    Toast.makeText(Login.this, "Phone Number Should be 10 Digits", Toast.LENGTH_SHORT).show();
                    return;
                }
                getLogin(phone);
                // startActivity(new Intent(Login.this,OtpScreen.class));
            }
        });
    }

    private void getLogin(String phone) {

        try {

            JSONObject object=new JSONObject();

            object.put("mobile_number", phone);

            new MethodResquest(this,  this, Constants.send_otp, object.toString(),100);

        } catch (Exception e) {

            e.printStackTrace();

        }

    }


    @Override
    public void requestStarted() {

    }

    @Override
    public void requestCompleted(JSONObject response, int requestType) {

        try {

            switch (requestType) {

                case 100:

                    JSONObject result = new JSONObject(response.toString());

                    if(result.optString("statuscode").equalsIgnoreCase("200"))
                    {
                        Toast.makeText(this, result.optString("statusdescription"), Toast.LENGTH_SHORT).show();

                        Intent in = new Intent(Login.this, VerifyOtp.class);

                        in.putExtra("phone", phone);

                        startActivity(in);
                    }
                    else
                    {
                        Toast.makeText(this, result.optString("statusdescription"), Toast.LENGTH_SHORT).show();
                    }

                    break;

                default:

                    break;
            }
        }catch (Exception e){

            e.printStackTrace();

        }

    }

    @Override
    public void requestEndedWithError(String error, int errorcode) {

    }
}
