package com.bsecure.vmp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bsecure.vmp.adapters.CustomersListAdapter;
import com.bsecure.vmp.adapters.ProductsListAdapter;
import com.bsecure.vmp.commons.AppPreferences;
import com.bsecure.vmp.interfaces.RequestHandler;
import com.bsecure.vmp.models.CustomerModel;
import com.bsecure.vmp.models.ProductModel;
import com.bsecure.vmp.volleyhttp.Constants;
import com.bsecure.vmp.volleyhttp.MethodResquest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Cash extends AppCompatActivity implements RequestHandler {



    private EditText leaks, paid, milk, curd, invamt, spotcom,monthlycom, netamount, olddue,payable, todaydue,totaldue ;

    private String oaid, cust_id, indent_collection_id, oad, session, head, rname, rno;

    double net, paidam, leak, old, tdue, todue;

    private TextView save;

    private static DecimalFormat df = new DecimalFormat("0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash);

        Intent in = getIntent();

        oaid = in.getStringExtra("oaid");

        cust_id = in.getStringExtra("cust_id");

        oad = in.getStringExtra("oad");

        session = in.getStringExtra("session");

        rname = in.getStringExtra("rname");

        rno = in.getStringExtra("r_no");

        head = in.getStringExtra("head");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle(head);//Organization Head

        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryDark));

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);


        save = findViewById(R.id.save);

        milk = findViewById(R.id.milk);

        curd = findViewById(R.id.curd);

        invamt = findViewById(R.id.invamt);

        spotcom = findViewById(R.id.spotcom);

        monthlycom = findViewById(R.id.monthlycom);

        netamount = findViewById(R.id.netamt);

        olddue = findViewById(R.id.olddue);

        payable = findViewById(R.id.payable);

        todaydue = findViewById(R.id.todaydue);

        totaldue = findViewById(R.id.totaldue);

        leaks = findViewById(R.id.leaks);

        paid = findViewById(R.id.paid);

        leaks.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!s.toString().trim().equals("")) {

                    net = Double.parseDouble(netamount.getText().toString());

                    old = Double.parseDouble(olddue.getText().toString());

                    if(paid.getText().toString().equals(""))
                    {
                        paidam = 0;
                    }
                    else {
                        paidam = Double.parseDouble(paid.getText().toString());
                    }

                    leak = Double.parseDouble(s.toString());

                    tdue = net-(paidam+leak);

                    if(tdue < 0)
                    {
                        todaydue.setText("0");
                    }
                    else
                    {
                        todaydue.setText(df.format(tdue));
                    }

                    //todaydue.setText(String.valueOf(df.format( net - paidam+ leak)));

                    if(leak == 0)
                    {
                        leaks.setText("");
                    }


                    //tdue = net - paidam+ leak;
                    todue = old + tdue;
                    totaldue.setText(df.format(todue));

                }else
                {
                    todaydue.setText("");
                    totaldue.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {



            }
        });

        /*leaks.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    if(leaks.getText().toString().length() == 0 || leaks.getText().toString().equals("0"))
                    {
                        leaks.setText("");
                    }

                }else
                {
                    if(leaks.getText().toString().length() == 0)
                    {
                        leaks.setText("0");
                    }
                }
            }
        });*/



        /*paid.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(hasFocus) {
                    if(paid.getText().toString().length() == 0 || paid.getText().toString().equals("0"))
                    {
                        paid.setText("");
                    }
                }else
                {
                    if(paid.getText().toString().length() == 0)
                    {
                        paid.setText("");
                    }
                }

            }
        });*/

        /*leaks.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });*/

        paid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(!charSequence.toString().trim().equals("")) {

                    net = Double.parseDouble(netamount.getText().toString());

                    old = Double.parseDouble(olddue.getText().toString());

                    paidam = Double.parseDouble(charSequence.toString());

                    if(leaks.getText().toString().equals(""))
                    {
                        leak = 0;
                    }
                    else {
                        leak = Double.parseDouble(leaks.getText().toString().trim());
                    }

                    tdue = net-(paidam+leak);

                    if(tdue < 0)
                    {
                        todaydue.setText("0");
                    }
                    else
                    {
                        todaydue.setText(df.format(tdue));
                    }

                    //todaydue.setText(String.valueOf(df.format( net - paidam+ leak)));

                    if(paidam == 0)
                    {
                        todaydue.setText("");
                    }


                    //tdue = net - paidam+ leak;
                    todue = old + tdue;
                    totaldue.setText(df.format(todue));

                }else
                {
                    todaydue.setText("");
                    totaldue.setText("");
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        getCollection(oaid, cust_id);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editCollection();

            }
        });

        /*if(netamount.getText().toString().equals(""))
        {
            net = 0;
        }else
        {
            net = Double.parseDouble(netamount.getText().toString().trim());
        }*/

        /*if(paid.getText().toString().equals(""))
        {
            paidam = 0;
        }
        else
        {
            paidam = Double.parseDouble(paid.getText().toString().trim());
        }
*/
        if(leaks.getText().toString().equals(""))
        {
            leak = 0;
        }
        else
        {
            leak = Double.parseDouble(leaks.getText().toString());
        }

       /* if(olddue.getText().toString().equals(""))
        {
            old = 0;
        }
        else {
            old = Double.parseDouble(olddue.getText().toString());
        }*/

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

    private void editCollection() {

        try {

            JSONObject object=new JSONObject();

            object.put("indent_collection_id", indent_collection_id);
            object.put("leaks", leak);
            object.put("paid", paidam);
            /*if(todaydue.getText().toString().equals("0"))
            {
                object.put("today_due", df.format( net - paidam+ leak));
            }
            else
            {
                object.put("today_due", todaydue.getText().toString());
            }*/
            //object.put("today_due", todaydue.getText().toString());

            object.put("today_due",tdue);

            /*if(totaldue.getText().toString().equals("0"))
            {
                object.put("total_due", old+net-paidam+leak);
            }
            else
            {
                object.put("total_due", totaldue.getText().toString());
            }*/
            //object.put("total_due", totaldue.getText().toString());

            object.put("total_due",todue);
            object.put("paid_date", System.currentTimeMillis());
            object.put("session", session);
            object.put("customer_id", cust_id);
            object.put("route_name", rname);
            object.put("route_no", rno);

            new MethodResquest(this,  this, Constants.edit_indent_collection, object.toString(),200);

        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    private void getCollection(String oaid, String cust_id) {

        try {

            JSONObject object=new JSONObject();

            object.put("order_allocation_id", oaid);

            object.put("customer_id", cust_id);

            object.put("order_allocation_date", oad);

            object.put("session", session);

            object.put("employee_id", AppPreferences.getInstance(this).getFromStore("eid"));

            new MethodResquest(this,  this, Constants.get_indent_collection, object.toString(),100);

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
                        JSONArray array = result.getJSONArray("indent_collection_details");
                        if(array.length() > 0)
                        {
                            JSONObject obj = array.getJSONObject(0);

                            net = Double.parseDouble(obj.optString("net_amount"));

                            old = Double.parseDouble(obj.optString("old_due"));

                            ////////

                            milk.setText(obj.optString("milk_qty"));

                            curd.setText(obj.optString("curd_qty"));

                            invamt.setText(obj.optString("inv_amout"));

                            spotcom.setText(obj.optString("spot_commision"));

                            monthlycom.setText(obj.optString("month_commission"));

                            netamount.setText(obj.optString("net_amount"));

                            olddue.setText(obj.optString("old_due"));

                            payable.setText(obj.optString("payable"));

                            if(obj.optString("today_due").equals("0") || obj.optString("today_due").equals(""))
                            {
                                todaydue.setText("0");
                                tdue = 0;
                            }
                            else
                            {
                                todaydue.setText(obj.optString("today_due"));
                                tdue = Double.parseDouble(obj.optString("today_due"));
                            }

                            if(obj.optString("total_due").equals("0") || obj.optString("total_due").equals(""))
                            {
                                totaldue.setText("0");
                                todue = 0;
                            }
                            else
                            {
                                totaldue.setText(obj.optString("total_due"));
                                todue = Double.parseDouble(obj.optString("total_due"));
                            }

                            indent_collection_id = obj.optString("indent_collection_id");

                            if(obj.optString("leaks").equals("0") || obj.optString("leaks").equals(""))
                            {
                                leaks.setText("");
                                leak = 0;
                            }
                            else
                            {
                                leaks.setText(obj.optString("leaks"));
                                leak = Double.parseDouble(obj.optString("leaks"));
                            }

                            if(obj.optString("paid").equals("0") || obj.optString("paid").equals(""))
                            {
                                paid.setText("");
                                paidam = 0;
                            }
                            else
                            {
                                paid.setText(obj.optString("paid"));
                                paidam = Double.parseDouble(obj.optString("paid"));
                            }

                            tdue = net-(paidam+leak);
                            todue = tdue+old;
                            int status = obj.optInt("status");
                            if(status == 1)
                            {
                                leaks.setEnabled(false);
                                paid.setEnabled(false);
                                save.setEnabled(false);
                            }

                        }
                        else
                        {
                            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
                        }

                    }
                    else
                    {
                        Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show();
                    }

                    break;

                case 200:

                    try {
                        JSONObject result1 = new JSONObject(response.toString());
                        if(result1.optString("statuscode").equalsIgnoreCase("200")) {

                            Toast.makeText(this, result1.optString("statusdescription"), Toast.LENGTH_SHORT).show();
                            Cash.this.finish();
                        }
                        else
                        {
                            Toast.makeText(this, result1.optString("statusdescription"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
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
