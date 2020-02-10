package com.bsecure.vmp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bsecure.vmp.Utils.GPSTracker;
import com.bsecure.vmp.commons.AppPreferences;
import com.bsecure.vmp.fragments.LocationDialogue;
import com.bsecure.vmp.interfaces.Receiver;
import com.bsecure.vmp.interfaces.RequestHandler;
import com.bsecure.vmp.services.MyLocationReciever;
import com.bsecure.vmp.services.UserLocationService;
import com.bsecure.vmp.volleyhttp.Constants;
import com.bsecure.vmp.volleyhttp.MethodResquest;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Dashboard extends AppCompatActivity implements RequestHandler , NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;
    TextView text1,text2;
    RelativeLayout indent, cash;
    double lat,lang;
    public LocationReceiver rec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Dashboard");//Organization Head
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryDark));
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        //actionBar.setDisplayHomeAsUpEnabled(true);

        text1=findViewById(R.id.itext);

        indent = findViewById(R.id.indent);

        cash = findViewById(R.id.cash);

        cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               Intent in = new Intent(Dashboard.this, Routes.class);
               in.putExtra("type", "cash");
               in.putExtra("head", "Cash Collection");
               startActivity(in);

            }
        });

        text2=findViewById(R.id.ctext);

        indent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent in = new Intent(Dashboard.this, Routes.class);
                in.putExtra("type", "indent");
                in.putExtra("head", "Indent Collection");
                startActivity(in);

            }
        });

        toolbar=findViewById(R.id.toolbar);

        drawer = findViewById(R.id.drawer_layout);

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);

        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.logout) {

            AppPreferences.getInstance(this).addToStore("eid", "", false);
            startActivity(new Intent(this, Login.class));
            Dashboard.this.finish();

        }
        else if(id == R.id.map) {

            Intent in = new Intent(Dashboard.this, Routes.class);
            in.putExtra("type", "map");
            startActivity(in);

        }
        else if(id == R.id.routes) {

            Intent in = new Intent(Dashboard.this, Routes.class);
            in.putExtra("type", "routes");
            startActivity(in);

        }
        else if(id == R.id.agent)
        {
            Intent in = new Intent(Dashboard.this, ExecutiveProfile.class);
            startActivity(in);
        }
        else if(id == R.id.start)
        {
            rec  = new LocationReceiver(new Handler());
            Intent mIntent = new Intent(this, UserLocationService.class);
            mIntent.putExtra("rec",rec);
            startService(mIntent);
            // Construct an intent that will execute the AlarmReceiver
           /* Intent intent = new Intent(getApplicationContext(), MyLocationReciever.class);
            intent.putExtra("rec",rec);
            // Create a PendingIntent to be triggered when the alarm goes off
            final PendingIntent pIntent = PendingIntent.getBroadcast(this, MyLocationReciever.REQUEST_CODE,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT);
            // Setup periodic alarm every every half hour from this point onwards
            long firstMillis = System.currentTimeMillis(); // alarm is set right away
            AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
            // First parameter is the type: ELAPSED_REALTIME, ELAPSED_REALTIME_WAKEUP, RTC_WAKEUP
            // Interval can be INTERVAL_FIFTEEN_MINUTES, INTERVAL_HALF_HOUR, INTERVAL_HOUR, INTERVAL_DAY
            alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis,
                    1000, pIntent);*/
        }
        else if(id == R.id.stop)
        {
            stopService(new Intent(this,UserLocationService.class));

            /*Intent intent = new Intent(getApplicationContext(), MyLocationReciever.class);
            final PendingIntent pIntent = PendingIntent.getBroadcast(this, MyLocationReciever.REQUEST_CODE,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
            alarm.cancel(pIntent);
            Toast.makeText(this, "Stopped", Toast.LENGTH_SHORT).show();*/
        }

            /*else if (id == R.id.loc) {

            Intent in = new Intent(Dashboard.this, Routes.class);
            in.putExtra("type", "l");
            startActivity(in);

        }*/


        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) < 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            Log.d("CDA", "onKeyDown Called");
            onBackPressed();
        }

        return super.onKeyDown(keyCode, event);
    }

    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);

    }

    @Override
    public void requestStarted() {

    }

    @Override
    public void requestCompleted(JSONObject response, int requestType) {

        if(requestType == 10)
        {
            try {
                JSONObject result = new JSONObject(response.toString());
                if(result.optString("statuscode").equalsIgnoreCase("200")) {
                    Toast.makeText(this, "sent", Toast.LENGTH_SHORT).show();
                }else
                {
                    Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void requestEndedWithError(String error, int errorcode) {

    }

    private class LocationReceiver extends ResultReceiver {
        LocationReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if(resultCode == 2)
            {
                GPSTracker gpsTracker = new GPSTracker(Dashboard.this);
                if (gpsTracker.getIsGPSTrackingEnabled()) {

                    lat = gpsTracker.getLatitude();
                    lang = gpsTracker.getLongitude();
                    saveLocation();

                } else {
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gpsTracker.showSettingsAlert();
                }
            }
        }
    }

    private void saveLocation() {

        try {
            JSONObject object = new JSONObject();

            object.put("lattitude",lat);
            object.put("longitude",lang);
            object.put("employee_id", AppPreferences.getInstance(this).getFromStore("eid"));

            new MethodResquest(Dashboard.this,  Dashboard.this, Constants.update_emp_loc, object.toString(),10);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}
