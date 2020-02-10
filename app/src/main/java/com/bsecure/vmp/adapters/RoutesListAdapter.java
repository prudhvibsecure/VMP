package com.bsecure.vmp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bsecure.vmp.R;
import com.bsecure.vmp.Utils.Utils;
import com.bsecure.vmp.interfaces.ClickListener;
import com.bsecure.vmp.models.RoutesModel;

import java.util.ArrayList;

public class RoutesListAdapter extends RecyclerView.Adapter<RoutesListAdapter.ViewHolder> {

  private ArrayList<RoutesModel>routes;

  private LayoutInflater inflater;

  Context context;

  private ClickListener listener;

  String type;


  public RoutesListAdapter(Context context, ArrayList<RoutesModel> routes, ClickListener listener, String type)
  {
    this.context = context;

    this.routes  = routes;

    this.listener = listener;

    inflater = LayoutInflater.from(context);

    this.type = type;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

    View view = inflater.inflate(R.layout.route_item, parent,false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

    RoutesModel model = routes.get(position);

    holder.name.setText(model.getRoute_name());

    holder.date.setText(Utils.getDate(Long.parseLong(model.getOrder_allocation_date())*1000));

    if(type.equals("indent"))
    {
      if(model.getSession().equalsIgnoreCase("1"))
      {
        holder.session.setText("Morning");
      }
      else
      {
        holder.session.setText("Afternoon");
      }

    }

    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        listener.onClick(position, view);

      }
    });

  }

  @Override
  public int getItemCount() {

    return routes.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {

    public TextView name, date, session;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);

      name = itemView.findViewById(R.id.name);

      date = itemView.findViewById(R.id.date);

      session = itemView.findViewById(R.id.session);

    }
  }
}
