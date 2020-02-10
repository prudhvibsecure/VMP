package com.bsecure.vmp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.bsecure.vmp.commons.AppPreferences;
import com.bsecure.vmp.commons.CircularImageView;
import com.bsecure.vmp.interfaces.RequestHandler;
import com.bsecure.vmp.volleyhttp.Constants;
import com.bsecure.vmp.volleyhttp.MethodResquest;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

public class ExecutiveProfile extends AppCompatActivity implements RequestHandler {

  CircularImageView image;

  TextView name, mobile, address;

  Toolbar toolbar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_executive_profile);

    image =findViewById(R.id.image);

    name = findViewById(R.id.name);

    mobile = findViewById(R.id.mobile);

    address =  findViewById(R.id.address);

    toolbar = findViewById(R.id.toolset);
    toolbar.setTitle("Profile");
    toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryDark));
    setSupportActionBar(toolbar);
    ActionBar actionBar = getSupportActionBar();
    actionBar.setDisplayHomeAsUpEnabled(true);

    getProfile(AppPreferences.getInstance(this).getFromStore("eid"));

  }

  private void getProfile(String eid) {

    try {

      JSONObject object = new JSONObject();

      object.put("employee_id", eid);

      new MethodResquest(this,  this, Constants.get_profile, object.toString(),100);

    } catch (Exception e) {
      e.printStackTrace();
    }
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
            JSONArray array = result.getJSONArray("employee_details");

            JSONObject obj = array.getJSONObject(0);

            name.setText(obj.optString("employee_name"));

            mobile.setText(obj.optString("mobile_number"));

            address.setText(obj.optString("address"));

            //Glide.with(this).load(Uri.parse(obj.optString("profilepic"))).into(image);
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
