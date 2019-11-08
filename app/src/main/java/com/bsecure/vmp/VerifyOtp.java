package com.bsecure.vmp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.JsonObjectRequest;
import com.bsecure.vmp.commons.AppPreferences;
import com.bsecure.vmp.interfaces.RequestHandler;
import com.bsecure.vmp.volleyhttp.Constants;
import com.bsecure.vmp.volleyhttp.MethodResquest;
import com.chaos.view.PinView;

import org.json.JSONArray;
import org.json.JSONObject;

public class VerifyOtp extends AppCompatActivity implements RequestHandler {

    private PinView pin_et;
    private Button btn;
    private String otp, phone;
    private TextView resend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        Intent in = getIntent();

        Bundle bd = in.getExtras();

        phone = bd.getString("phone");

        resend = findViewById(R.id.resend);

        pin_et = (PinView) findViewById(R.id.pinView);

        pin_et.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager keyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(pin_et, 0);
            }
        }, 50);


        btn = findViewById(R.id.submit);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                otp = pin_et.getText().toString();

                if (TextUtils.isEmpty(otp)) {
                    Toast.makeText(VerifyOtp.this, "OTP Must be 4 Digits", Toast.LENGTH_SHORT).show();
                    return;
                }
                verifyOtp(otp);
            }
        });

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resendOtp(phone);
            }
        });

    }


    private void verifyOtp(String otp) {

        try {

            JSONObject object=new JSONObject();

            object.put("otp", otp);

            object.put("mobile_number", phone);

            object.put("regidand", "");

            new MethodResquest(this,this, Constants.verify_otp, object.toString(),100);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void resendOtp(String phone) {

        try {

            JSONObject object=new JSONObject();

            object.put("phone_number", phone);

            new MethodResquest(this,this, Constants.send_otp, object.toString(),200);

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
                        String eid = result.optString("employee_id");

                        AppPreferences.getInstance(this).addToStore("eid", eid, false);
                        AppPreferences.getInstance(this).addToStore("phone", phone, false);
                         startActivity(new Intent(VerifyOtp.this, Dashboard.class));

                            return;

                    }
                    else
                    {
                        Toast.makeText(this, result.optString("statusdescription"), Toast.LENGTH_SHORT).show();
                    }

                    break;

                case 200:

                    JSONObject result2 = new JSONObject(response.toString());

                    if(result2.optString("statuscode").equalsIgnoreCase("200"))
                    {

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
