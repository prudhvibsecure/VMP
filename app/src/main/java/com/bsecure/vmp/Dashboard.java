package com.bsecure.vmp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bsecure.vmp.commons.AppPreferences;
import com.bsecure.vmp.interfaces.RequestHandler;
import com.bsecure.vmp.volleyhttp.Constants;
import com.bsecure.vmp.volleyhttp.MethodResquest;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONObject;

public class Dashboard extends AppCompatActivity implements RequestHandler, NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;
    TextView text1,text2;
    RelativeLayout indent, cash;


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
               in.putExtra("type", "i");
               startActivity(in);

            }
        });

        text2=findViewById(R.id.ctext);

        indent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent in = new Intent(Dashboard.this, Routes.class);
                in.putExtra("type", "c");
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
    public void requestStarted() {

    }

    @Override
    public void requestCompleted(JSONObject response, int requestType) {



    }

    @Override
    public void requestEndedWithError(String error, int errorcode) {

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
            in.putExtra("type", "m");
            startActivity(in);

        }
        else if(id == R.id.routes) {

            Intent in = new Intent(Dashboard.this, Routes.class);
            in.putExtra("type", "r");
            startActivity(in);

        }
        else if(id == R.id.agent)
        {
            Intent in = new Intent(Dashboard.this, ExecutiveProfile.class);
            startActivity(in);
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

}
