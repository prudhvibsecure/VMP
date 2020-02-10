package com.bsecure.vmp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bsecure.vmp.adapters.CustomersListAdapter;
import com.bsecure.vmp.adapters.ProductsListAdapter;
import com.bsecure.vmp.commons.AppPreferences;
import com.bsecure.vmp.interfaces.ClickListener;
import com.bsecure.vmp.interfaces.RequestHandler;
import com.bsecure.vmp.models.CustomerModel;
import com.bsecure.vmp.models.ProductModel;
import com.bsecure.vmp.volleyhttp.Constants;
import com.bsecure.vmp.volleyhttp.MethodResquest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Products extends AppCompatActivity implements RequestHandler, ClickListener {

  private RecyclerView list;

  private ArrayList<ProductModel> products;

  private ProductsListAdapter adapter;

  private String oaid = "", cust_id, cust_name, eid, session, fquant ="", ocd="", oad, rname, rno, head;

  private int status;

  private TextView save;

  private String[] quant;

  private JSONArray odarray;

  private ProgressBar progress;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_products);

    Intent in = getIntent();

    oaid = in.getStringExtra("oaid");

    cust_id = in.getStringExtra("cust_id");

    cust_name = in.getStringExtra("cust_name");

    eid = AppPreferences.getInstance(Products.this).getFromStore("eid");

    session = in.getStringExtra("session");

    oad = in.getStringExtra("oad");

    rname = in.getStringExtra("rname");

    rno = in.getStringExtra("r_no");

    head = in.getStringExtra("head");


    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

    toolbar.setTitle(head);//Organization Head

    toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryDark));

    setSupportActionBar(toolbar);

    ActionBar actionBar = getSupportActionBar();

    actionBar.setDisplayHomeAsUpEnabled(true);

    list = findViewById(R.id.list);

    progress = findViewById(R.id.progress);

    getProducts(oaid);

    save = findViewById(R.id.save);

    save.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        saveProducts();

      }
    });
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


  private void getProducts(String oaid) {

    try {

      JSONObject object=new JSONObject();

      object.put("order_allocation_id", oaid);

      object.put("customer_id", cust_id);

      object.put("employee_id", eid);

      object.put("order_allocation_date", oad);

      object.put("session", session);

      new MethodResquest(this,  this, Constants.get_products, object.toString(),100);


    } catch (Exception e) {

      e.printStackTrace();

    }

  }


  private void saveProducts() {

    try {

      fquant = "";

      products = adapter.getProducts();

      for(int j = 0; j < products.size(); j++)
      {
        String quant = "";
        if(products.get(j).getQuantity().equals(""))
        {
          quant = ",0";
        }else {
          quant = "," + products.get(j).getQuantity();
        }
        fquant = fquant+quant;
      }

      fquant = fquant.substring(1);

      JSONObject object=new JSONObject();

      object.put("order_allocation_id", oaid);

      object.put("customer_id", cust_id);

      object.put("customer_name", cust_name);

      object.put("employee_id", eid);

      object.put("order_collection_date", System.currentTimeMillis());

      object.put("order_allocation_date", oad);

      object.put("session", session);

      object.put("qty", fquant);

      object.put("route_name", rname);

      object.put("route_no", rno);

      //object.put("route_name", rname);

      if(odarray.length() > 0)
      {
        object.put("order_collection_id", ocd );

        new MethodResquest(this, this, Constants.edit_order_collection, object.toString(), 200);

        progress.setVisibility(View.VISIBLE);

      }else {

        new MethodResquest(this, this, Constants.add_order_collection, object.toString(), 200);

        progress.setVisibility(View.VISIBLE);
      }

    } catch (Exception e) {

      e.printStackTrace();

    }

  }


  @Override
  public void onClick(int position, View view) {

  }

  @Override
  public void onLongClick(int position, View view) {

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

            products = new ArrayList<>();

            odarray = result.getJSONArray("order_details");

            String quantity = "";

            if(odarray.length() > 0) {

              JSONObject odobj = odarray.getJSONObject(0);

              ocd = odobj.optString("order_collection_id");

              quantity = odobj.optString("qty");

              quant = quantity.split(",");

              status = odobj.optInt("status");

            }

            JSONArray parray = result.getJSONArray("product_details");

            for(int i = 0; i< parray.length();i++) {

              JSONObject pobj = parray.getJSONObject(i);

              ProductModel model = new ProductModel();

              model.setId(pobj.optString("product_id"));

              model.setName(pobj.optString("product_name"));

              if(quantity.equals("")) {
                model.setQuantity("");
              }
              else
              {
                model.setQuantity(quant[i]);
              }

              products.add(model);

            }

            LinearLayoutManager manager = new LinearLayoutManager(this);

            list.setLayoutManager(manager);

            adapter = new ProductsListAdapter(this, products, this, status);

            list.setAdapter(adapter);

          }
          else
          {
            progress.setVisibility(View.GONE);

            Toast.makeText(this, result.optString("statusdescription"), Toast.LENGTH_SHORT).show();
          }

          break;

        case 200:

          JSONObject result2 = new JSONObject(response.toString());

          if(result2.optString("statuscode").equalsIgnoreCase("200"))
          {
            progress.setVisibility(View.GONE);

            Toast.makeText(this, result2.optString("statusdescription"), Toast.LENGTH_SHORT).show();
            Products.this.finish();
          }
          else
          {
            progress.setVisibility(View.GONE);

            Toast.makeText(this, result2.optString("statusdescription"), Toast.LENGTH_SHORT).show();
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
