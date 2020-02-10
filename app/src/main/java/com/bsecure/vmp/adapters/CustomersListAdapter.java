package com.bsecure.vmp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.bsecure.vmp.R;
import com.bsecure.vmp.interfaces.ClickListener;
import com.bsecure.vmp.models.CustomerModel;
import com.bsecure.vmp.models.RoutesModel;

import java.util.ArrayList;

public class CustomersListAdapter extends RecyclerView.Adapter<CustomersListAdapter.ViewHolder>{

  private ArrayList<CustomerModel> customers;

  private LayoutInflater inflater;

  Context context;

  private ClickListener listener;

  public CustomersListAdapter(Context context, ArrayList<CustomerModel>customers, ClickListener listener)
  {
    this.context = context;

    this.customers  = customers;

    this.listener = listener;

    inflater = LayoutInflater.from(context);
  }

  @NonNull
  @Override
  public CustomersListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

    View view = inflater.inflate(R.layout.customer_item, parent,false);

    return new CustomersListAdapter.ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull final  CustomersListAdapter.ViewHolder holder, final int position) {

    CustomerModel model = customers.get(position);

    holder.name.setText(model.getName());

    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        listener.onClick(position, view);

      }
    });

    holder.opt.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        listener.onClick(position, view);
               /* PopupMenu popup = new PopupMenu(context, holder.opt);
                //inflating menu from xml resource
                popup.inflate(R.menu.options_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.add:
                                listener.onClick(position, view);
                                break;
                            case R.id.view:
                                //handle menu2 click
                                break;

                        }
                        return false;
                    }
                });
                //displaying the popup
                popup.show();*/

      }
    });


  }

  @Override
  public int getItemCount() {
    return customers.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {

    private TextView name;

    private ImageView opt;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);

      name = itemView.findViewById(R.id.name);

      opt = itemView.findViewById(R.id.opt);
    }
  }
}
