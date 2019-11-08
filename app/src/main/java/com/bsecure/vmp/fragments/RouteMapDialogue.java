package com.bsecure.vmp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.bsecure.vmp.R;
import com.bsecure.vmp.interfaces.RequestHandler;
import com.bsecure.vmp.volleyhttp.Constants;
import com.bsecure.vmp.volleyhttp.MethodResquest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

public class RouteMapDialogue extends DialogFragment implements RequestHandler {

    private String TAG = RouteMapDialogue.class.getSimpleName();

    private ImageView close;

    private double lati;

    private double longi;

    private String customer_name, route_no;

    private GoogleMap googleMap;

    private MapView map;

    public static RouteMapDialogue newInstance() {
        RouteMapDialogue f = new RouteMapDialogue();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.map_fragment, container, false);

        close = v.findViewById(R.id.close);

        route_no = getArguments().getString("route_no");

        map = v.findViewById(R.id.map);

        map.onCreate(savedInstanceState);

        map.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        map.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap Map) {

                googleMap = Map;

                // For showing a move to my location button
                //googleMap.setMyLocationEnabled(true);

                getRouteMap(route_no);
                // For dropping a marker at a point on the Map
               /* LatLng sydney = new LatLng(lati, longi);
                googleMap.addMarker(new MarkerOptions().position(sydney).title(customer_name).snippet("Customer"));*/

                // For zooming automatically to the location of the marker
               /* CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(20).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));*/
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
    }


    @Override
    public void onPause() {
        super.onPause();
        map.onPause();
        //images.clear();
    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
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

                        JSONArray array = result.getJSONArray("customer_route_details");
                        if(array.length() != 0)
                        {
                            for(int i=0;i<array.length();i++)
                            {
                                JSONObject obj = array.getJSONObject(i);

                                customer_name = obj.getString("customer_name");

                                lati = Double.parseDouble(obj.optString("lat"));

                                longi = Double.parseDouble(obj.optString("lang"));

                                LatLng latLng = new LatLng(lati, longi);

                                googleMap.addMarker(new MarkerOptions().position(latLng).title(customer_name).snippet("Customer"));

                                CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(15).build();
                                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                            }
                        }
                        else
                        {
                            Toast.makeText(getActivity(), "No Details Found", Toast.LENGTH_SHORT).show();
                        }

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

    private void getRouteMap(String route_no) {

        try {

            JSONObject object=new JSONObject();

            object.put("route_no", route_no);

            new MethodResquest(getActivity(),  this, Constants.get_route_latlan, object.toString(),100);

        } catch (Exception e) {

            e.printStackTrace();

        }

    }


}
