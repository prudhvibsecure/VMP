package com.bsecure.vmp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bsecure.vmp.adapters.RoutesListAdapter;
import com.bsecure.vmp.commons.AppPreferences;
import com.bsecure.vmp.fragments.MapDialogue;
import com.bsecure.vmp.fragments.RouteMapDialogue;
import com.bsecure.vmp.interfaces.ClickListener;
import com.bsecure.vmp.interfaces.RequestHandler;
import com.bsecure.vmp.models.RoutesModel;
import com.bsecure.vmp.volleyhttp.Constants;
import com.bsecure.vmp.volleyhttp.MethodResquest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Routes extends AppCompatActivity implements RequestHandler, ClickListener {

  private RecyclerView list;

  private ArrayList<RoutesModel> routes;

  private RoutesListAdapter adapter;

  private String eid, type, head;

  private ProgressBar progress;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_routes);

    Intent in  = getIntent();

    head = in.getStringExtra("head");

    type = in.getStringExtra("type");

    progress = findViewById(R.id.progress);

    list = findViewById(R.id.list);

    eid = AppPreferences.getInstance(Routes.this).getFromStore("eid");

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

    toolbar.setTitle(head);//Organization Head

    toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryDark));

    setSupportActionBar(toolbar);

    ActionBar actionBar = getSupportActionBar();

    actionBar.setDisplayHomeAsUpEnabled(true);

    actionBar.setDisplayShowHomeEnabled(true);

    getRoutes(eid);
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

  private void getRoutes(String eid) {

    try {

      JSONObject object=new JSONObject();

      object.put("employee_id", eid);

      if(type.equals("indent")) {
        new MethodResquest(this, this, Constants.get_routes, object.toString(), 100);
      }
      else
      {
        new MethodResquest(this, this, Constants.get_routes_cash, object.toString(), 100);
      }

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

            routes = new ArrayList<>();

            JSONArray array = result.getJSONArray("route_details");

            for(int i = 0; i< array.length();i++) {

              JSONObject obj = array.getJSONObject(i);

              RoutesModel model = new RoutesModel();

              model.setRoute_name(obj.optString("route_name"));

              model.setRoute_no(obj.optString("route_no"));

              model.setOrder_allocation_date(obj.optString("order_allocation_date"));

              model.setOrder_allocation_id(obj.optString("order_allocation_id"));

              model.setSession(obj.optString("session"));

              routes.add(model);

            }

            LinearLayoutManager manager = new LinearLayoutManager(this);

            list.setLayoutManager(manager);

            adapter = new RoutesListAdapter(this, routes, this, type);

            list.setAdapter(adapter);

          }
          else
          {
            progress.setVisibility(View.GONE);

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

  @Override
  public void onClick(int position, View view) {

    if(type.equals("map"))
    {
      Bundle bundle = new Bundle();

      bundle.putString("route_no", routes.get(position).getRoute_no());

      FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

      RouteMapDialogue newFragment = RouteMapDialogue.newInstance();

      newFragment.setArguments(bundle);

      newFragment.show(ft, "Location");

    }
    else if(type.equals("routes"))
    {
      return;
    }
    else {

      Intent in = new Intent(Routes.this, Customers.class);

      in.putExtra("rno", routes.get(position).getRoute_no());

      in.putExtra("oaid", routes.get(position).getOrder_allocation_id());

      in.putExtra("session", routes.get(position).getSession());

      in.putExtra("oad", routes.get(position).getOrder_allocation_date());

      in.putExtra("type", type);

      in.putExtra("rname", routes.get(position).getRoute_name());

      in.putExtra("head", head);

      startActivity(in);
    }

  }

  @Override
  public void onLongClick(int position, View view) {

  }
}
