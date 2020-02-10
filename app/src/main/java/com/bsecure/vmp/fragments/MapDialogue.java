package com.bsecure.vmp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.bsecure.vmp.R;
import com.bsecure.vmp.interfaces.RequestHandler;
import com.bsecure.vmp.volleyhttp.Constants;
import com.bsecure.vmp.volleyhttp.MethodResquest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

public class MapDialogue extends DialogFragment {

  private String TAG = MapDialogue.class.getSimpleName();

  private ImageView close;

  private double lati;

  private double longi;

  private String customer_name;

  private GoogleMap googleMap;

  private MapView map;

  public static MapDialogue newInstance() {
    MapDialogue f = new MapDialogue();
    return f;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.map_fragment, container, false);

    close = v.findViewById(R.id.close);

    lati = getArguments().getDouble("lat");

    longi = getArguments().getDouble("lang");

    customer_name = getArguments().getString("cname");

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
        googleMap.setMyLocationEnabled(true);

        // For dropping a marker at a point on the Map
        LatLng sydney = new LatLng(lati, longi);
        googleMap.addMarker(new MarkerOptions().position(sydney).title(customer_name).snippet("Customer"));

        // For zooming automatically to the location of the marker
        CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(20).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
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

}
