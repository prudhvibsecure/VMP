package com.bsecure.vmp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bsecure.vmp.Utils.GPSTracker;
import com.bsecure.vmp.adapters.CustomersListAdapter;
import com.bsecure.vmp.commons.AppPreferences;
import com.bsecure.vmp.fragments.LocationDialogue;
import com.bsecure.vmp.fragments.MapDialogue;
import com.bsecure.vmp.interfaces.ClickListener;
import com.bsecure.vmp.interfaces.RequestHandler;
import com.bsecure.vmp.models.CustomerModel;
import com.bsecure.vmp.volleyhttp.Constants;
import com.bsecure.vmp.volleyhttp.MethodResquest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Customers extends AppCompatActivity implements RequestHandler, ClickListener {

    private RecyclerView list;

    private ArrayList<CustomerModel> customers;

    private CustomersListAdapter adapter;

    private String route_no, oaid, session, type, oad, rname, customer_name, head;

    private ProgressBar progress;

    int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers);

        Intent in = getIntent();

        route_no = in.getStringExtra("rno");

        oaid = in.getStringExtra("oaid");

        session = in.getStringExtra("session");

        oad = in.getStringExtra("oad");

        type = in.getStringExtra("type");

        rname = in.getStringExtra("rname");

        head = in.getStringExtra("head");

        list = findViewById(R.id.list);

        progress = findViewById(R.id.progress);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle(head);//Organization Head

        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryDark));

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);

        getCustomers(route_no.trim());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getCustomers(String route_no) {

        try {

            JSONObject object=new JSONObject();

            object.put("route_no", route_no);

            new MethodResquest(this,  this, Constants.get_customers, object.toString(),100);

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
                        progress.setVisibility(View.GONE);

                        customers = new ArrayList<>();

                        JSONArray array = result.getJSONArray("customer_details");

                        for(int i = 0; i< array.length();i++) {

                            JSONObject obj = array.getJSONObject(i);

                            CustomerModel model = new CustomerModel();

                            model.setId(obj.optString("customer_id"));

                            model.setName(obj.optString("customer_name"));

                            model.setAddress(obj.optString("customer_address"));

                            customers.add(model);

                        }

                        LinearLayoutManager manager = new LinearLayoutManager(this);

                        list.setLayoutManager(manager);

                        adapter = new CustomersListAdapter(this, customers, this);

                        list.setAdapter(adapter);

                    }
                    else
                    {
                        progress.setVisibility(View.GONE);

                        Toast.makeText(this, result.optString("statusdescription"), Toast.LENGTH_SHORT).show();
                    }

                    break;

                case 200:

                    try {

                        JSONObject result2 = new JSONObject(response.toString());

                        if(result2.optString("statuscode").equalsIgnoreCase("200")) {

                            JSONArray array = result2.getJSONArray("details");

                            JSONObject obj = array.getJSONObject(0);

                            Double lat = Double.parseDouble(obj.optString("lat"));

                            Double lang = Double.parseDouble(obj.optString("lang"));


                            Bundle bundle = new Bundle();

                            bundle.putDouble("lat", lat);

                            bundle.putDouble("lang", lang);

                            bundle.putString("cname", customer_name);

                            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

                            MapDialogue newFragment = MapDialogue.newInstance();

                            newFragment.setArguments(bundle);

                            newFragment.show(ft, "Location");

                        }
                        else
                        {
                            Toast.makeText(this, "Failed To Get Location.Try Again", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                    break;

                case 1000:

                    JSONObject result3 = new JSONObject(response.toString());

                    if(result3.optString("statuscode").equalsIgnoreCase("200")) {

                        Intent in = new Intent(Customers.this, Cash.class);

                        in.putExtra("oaid", oaid);

                        in.putExtra("cust_id", customers.get(position).getId());

                        in.putExtra("oad", oad);

                        in.putExtra("session", session);

                        in.putExtra("rname", rname);

                        in.putExtra("r_no", route_no);

                        in.putExtra("head", customer_name);

                        startActivity(in);

                    }
                    else
                    {

                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                Customers.this);
                        builder.setTitle("Alert");
                        builder.setMessage(result3.optString("statusdescription"));
                        builder.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {

                                        Intent in = new Intent(Customers.this, Customers.class);
                                        in.putExtra("rno", route_no);

                                        in.putExtra("oaid", oaid);

                                        in.putExtra("session", session);

                                        in.putExtra("oad", oad);

                                        in.putExtra("type", type);

                                        in.putExtra("rname", rname);

                                        in.putExtra("head", head);
                                        startActivity(in);
                                        Customers.this.finish();
                                    }
                                });
                        builder.show();

                        // Toast.makeText(this, result3.optString("statusdescription"), Toast.LENGTH_SHORT).show();
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


    @Override
    public void onClick(final int pos, View view) {

        position = pos;

        customer_name = customers.get(position).getName();

        if(view.getId() == R.id.opt)
        {
            PopupMenu popup = new PopupMenu(this, view);
            //inflating menu from xml resource
            popup.inflate(R.menu.options_menu);
            //adding click listener
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.add:
                            GPSTracker gpsTracker = new GPSTracker(Customers.this);
                            if (gpsTracker.getIsGPSTrackingEnabled()) {

                                double lat = gpsTracker.getLatitude();
                                double lang = gpsTracker.getLongitude();
                                String cid = customers.get(position).getId();

                                Bundle bundle = new Bundle();
                                bundle.putDouble("lat", lat);
                                bundle.putDouble("lang", lang);
                                bundle.putString("cid", cid);

                                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                                LocationDialogue newFragment = LocationDialogue.newInstance();
                                newFragment.setArguments(bundle);
                                newFragment.show(ft, "Location");

                            } else {
                                // can't get location
                                // GPS or Network is not enabled
                                // Ask user to enable GPS/network in settings
                                gpsTracker.showSettingsAlert();
                            }
                            //handle menu1 click
                            break;
                        case R.id.view:

                            getLocation(customers.get(position).getId());

                            break;

                    }
                    return false;
                }
            });
            //displaying the popup
            popup.show();
        }
        else {

            if (type.equals("indent")) {

                Intent in = new Intent(Customers.this, Products.class);

                in.putExtra("oaid", oaid);

                in.putExtra("cust_id", customers.get(position).getId());

                in.putExtra("cust_name", customers.get(position).getName());

                in.putExtra("session", session);

                in.putExtra("oad", oad);

                in.putExtra("rname", rname);

                in.putExtra("r_no", route_no);

                in.putExtra("head", customer_name);

                startActivity(in);

                return;
            }

            if (type.equals("cash")) {

                getCollection(oaid, customers.get(position).getId());
            }

        }


    }

    private void getLocation(String id) {

        try {
            JSONObject object = new JSONObject();

            object.put("customer_id", id);

            new MethodResquest(Customers.this,  this, Constants.get_customer_latlan, object.toString(),200);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLongClick(int position, View view) {

    }

    private void getCollection(String oaid, String cust_id) {

        try {

            JSONObject object=new JSONObject();

            object.put("order_allocation_id", oaid);

            object.put("customer_id", cust_id);

            object.put("order_allocation_date", oad);

            object.put("session", session);

            object.put("employee_id", AppPreferences.getInstance(this).getFromStore("eid"));

            new MethodResquest(this,  this, Constants.get_indent_collection, object.toString(),1000);

        } catch (Exception e) {

            e.printStackTrace();

        }

    }
}
