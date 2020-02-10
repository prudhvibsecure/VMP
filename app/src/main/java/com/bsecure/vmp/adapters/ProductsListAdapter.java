package com.bsecure.vmp.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bsecure.vmp.R;
import com.bsecure.vmp.interfaces.ClickListener;
import com.bsecure.vmp.models.CustomerModel;
import com.bsecure.vmp.models.ProductModel;

import java.util.ArrayList;

public class ProductsListAdapter extends RecyclerView.Adapter<ProductsListAdapter.ViewHolder> {

  private ArrayList<ProductModel> products;

  private LayoutInflater inflater;

  Context context;

  private ClickListener listener;

  private int status;


  public ProductsListAdapter(Context context, ArrayList<ProductModel>products, ClickListener listener, int status)
  {

    this.context = context;

    this.products = products;

    this.listener = listener;

    this.status = status;

    inflater = LayoutInflater.from(context);

  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

    View view = inflater.inflate(R.layout.product_item, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

    ProductModel model = products.get(position);

    holder.name.setText(model.getName());

    holder.quant.setText(model.getQuantity());

    holder.quant.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

      }

      @Override
      public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if(charSequence.toString().length() != 0) {
          products.get(position).setQuantity(charSequence.toString());
        }
      }

      @Override
      public void afterTextChanged(Editable editable) {

      }
    });

    holder.quant.setOnFocusChangeListener(new View.OnFocusChangeListener() {
      @Override
      public void onFocusChange(View v, boolean hasFocus) {

        if(hasFocus) {
          if(holder.quant.getText().toString().length() == 0 || holder.quant.getText().toString().equals("0"))
          {
            holder.quant.setText("");
          }
        }else
        {
          if(holder.quant.getText().toString().length() == 0)
          {
            holder.quant.setText("0");
          }
        }

      }
    });

    if(status == 1)
    {
      holder.quant.setEnabled(false);
    }
  }

  @Override
  public int getItemCount() {
    return products.size();
  }

  @Override
  public int getItemViewType(int position)
  {
    return position;
  }

  public class ViewHolder extends RecyclerView.ViewHolder {

    private TextView name;

    private EditText quant;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);

      name = itemView.findViewById(R.id.name);

      quant = itemView.findViewById(R.id.quant);
    }
  }

  public ArrayList<ProductModel> getProducts()
  {
    return this.products;
  }
}
