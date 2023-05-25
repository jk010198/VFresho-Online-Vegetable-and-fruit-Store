package com.ithublive.vfresho.Adaptors;

import android.app.Activity;
import android.net.Uri;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.ithublive.vfresho.Models.AddProducts;
import com.ithublive.vfresho.Models.CartModel;
import com.ithublive.vfresho.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public class CartArrayAdaptor extends ArrayAdapter {

    Activity context;
    List<CartModel> cm;
    HashMap<Integer, View> allRows = new HashMap<Integer, View>();
    CartModel cartModel;
    Uri uri;
    ArrayList<AddProducts> ap;
    int total = 0;

    public CartArrayAdaptor(@NonNull Activity context, int resource, @NonNull List<CartModel> product) {
        super(context, resource, product);
        this.context = context;
        cm = product;
        Collections.sort(cm);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable final View convertView, @NonNull final ViewGroup parent) {
        final LayoutInflater inflater = context.getLayoutInflater();
        final View rowView = inflater.inflate(R.layout.list_layout_cart, null, true);
        TextView txtName = (TextView) rowView.findViewById(R.id.tv_productname);
        TextView txtprice = (TextView) rowView.findViewById(R.id.tv_product_price);
        TextView txtquantity = (TextView) rowView.findViewById(R.id.tv_product_quantity);
        ImageView imageView = rowView.findViewById(R.id.cart_imageview_product);
        //ImageButton imageButton_delete = rowView.findViewById(R.id.imagebutton_delete);

        txtName.setText(cm.get(position).name);
        txtprice.setText("VFresho ₹:-" + cm.get(position).price);
        txtquantity.setText(cm.get(position).quantity + "");
        int price = Integer.parseInt(cm.get(position).price);
        int qty = cm.get(position).quantity;
        total = (price * qty);

        /*
        imageButton_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartModel model = cm.remove(position);
                CartInsertion.cartProducts.remove(model);
                CartInsertion.cartprice = CartInsertion.cartprice - ((Integer.parseInt(model.price)) * model.quantity);
                ViewCart.textView.setText("GrandTotal:-" + CartInsertion.cartprice + "   ");
                Toast.makeText(context, "deleted " + model.name, Toast.LENGTH_SHORT).show();
                total = total - ((Integer.parseInt(model.price)) * model.quantity);
                Toast.makeText(context, "" + total, Toast.LENGTH_SHORT).show();
                notifyDataSetChanged();
            }
        });*/

        uri = Uri.parse(cm.get(position).img_url);

        Glide
                .with(context)
                .load(uri)
                .into(imageView);

        return rowView;
    }
}