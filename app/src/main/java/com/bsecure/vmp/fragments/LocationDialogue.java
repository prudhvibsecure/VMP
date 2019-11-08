package com.bsecure.vmp.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;


import com.bsecure.vmp.Login;
import com.bsecure.vmp.R;
import com.bsecure.vmp.VerifyOtp;
import com.bsecure.vmp.interfaces.RequestHandler;
import com.bsecure.vmp.volleyhttp.Constants;
import com.bsecure.vmp.volleyhttp.MethodResquest;

import org.json.JSONException;
import org.json.JSONObject;

public class LocationDialogue extends DialogFragment implements RequestHandler {

    private String TAG = LocationDialogue.class.getSimpleName();

    private EditText lat, lang;

    private Button send;

    private double lati;
    private double longi;
    private String customer_id;

    public static LocationDialogue newInstance() {
        LocationDialogue f = new LocationDialogue();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_location_fragment, container, false);

        lat = v.findViewById(R.id.lat);

        lang = v.findViewById(R.id.lang);

        send = v.findViewById(R.id.send);

        lati = getArguments().getDouble("lat");

        longi = getArguments().getDouble("lang");

        customer_id = getArguments().getString("cid");

        lat.setText(String.valueOf(lati));

        lang.setText(String.valueOf(longi));

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(lati == 0)
                {
                    Toast.makeText(getActivity(), "Latitude Is Empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(longi == 0)
                {
                    Toast.makeText(getActivity(), "Longitude Is Empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                saveLocation(lati, longi);
                //saveLocation(Double.parseDouble(lat.getText().toString()), Double.parseDouble(lang.getText().toString()));
            }
        });

        return v;
    }

    private void saveLocation(Double lati, Double longi) {


        try {
            JSONObject object = new JSONObject();

            object.put("customer_id", customer_id);

            object.put("lat", lati);

            object.put("lang", longi);

            new MethodResquest(getActivity(),  this, Constants.edit_customer_latlan, object.toString(),100);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }





    @Override
    public void onPause() {
        super.onPause();
        //images.clear();
    }

    @Override
    public void onResume() {
        super.onResume();
        //images = simages;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //images.clear();
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
                        Toast.makeText(getActivity(), result.optString("statusdescription"), Toast.LENGTH_SHORT).show();

                    }
                    else
                    {
                        Toast.makeText(getActivity(), result.optString("statusdescription"), Toast.LENGTH_SHORT).show();
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

    public interface actionListener
    {
        void onAction(int i);
    }
}
