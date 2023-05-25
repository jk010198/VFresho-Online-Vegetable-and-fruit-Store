package com.ithublive.vfresho.Adaptors;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ithublive.vfresho.Models.AddProducts;
import com.ithublive.vfresho.Models.CartInsertion;
import com.ithublive.vfresho.Models.CartModel;
import com.ithublive.vfresho.R;

import java.util.List;

public class ProductArrayAdaptor extends BaseAdapter {
    private final Context context;
    private final int resource;
    private final List ap;
    private Uri uri;

    public ProductArrayAdaptor(Context context, int resource, List allProducts) {
        this.context = context;
        this.resource = resource;
        this.ap = allProducts;
    }

    @Override
    public int getCount() {
        return ap.size();
    }

    @Override
    public Object getItem(int i) {
        return ap.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(resource, viewGroup, false);
        final AddProducts pro = (AddProducts) ap.get(position);
        ImageView imageView = (ImageView) view.findViewById(R.id.product_imageview);
        TextView textview_name = (TextView) view.findViewById(R.id.tv_productname);
        TextView textview_oldprice = view.findViewById(R.id.tv_product_oldprice);
        TextView textview_discount_percent = view.findViewById(R.id.tv_product_discountpercent);

        TextView textview_price = (TextView) view.findViewById(R.id.tv_product_price);
        final TextView textViewCount = (TextView) view.findViewById(R.id.tv_count);
        final ImageView imageCheck = (ImageView) view.findViewById(R.id.ImageButtonProductChecked);
        imageCheck.setVisibility(View.INVISIBLE);
        textview_name.setText(pro.name);
       // notifyDataSetChanged();
        textview_price.setText("VFresho ₹" + pro.price);
        textview_oldprice.setText(Html.fromHtml("<del>   MRP:- " + pro.old_price + " </del>"));
        textview_discount_percent.setText(Html.fromHtml("you save ₹"
                + ((Integer.parseInt(pro.old_price)) - (Integer.parseInt(pro.price)))));

        uri = Uri.parse(pro.img_url);

        Glide
                .with(context)
                .load(uri)
                .into(imageView);
        if (CartInsertion.cartAddedProducts.containsKey(pro.name)) {
            CartModel cmx = CartInsertion.cartAddedProducts.get(pro.name);
            textViewCount.setText(cmx.quantity+"" );
            imageCheck.setVisibility(View.VISIBLE);
        }

        ImageButton ib_add, ib_remove;
        ib_add = view.findViewById(R.id.ib_add);
        ib_remove = view.findViewById(R.id.ib_remove);

        ib_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int count = Integer.parseInt((textViewCount.getText().toString()));
                count++;
                CartInsertion.cartprice=CartInsertion.cartprice+Double.parseDouble(pro.price);

                textViewCount.setText(count + "");
                imageCheck.setVisibility(View.VISIBLE);
                int qty = Integer.parseInt(pro.price);
              //  Toast.makeText(context, pro.name + " added  : " + count+"\nTotal Price"+CartInsertion.cartprice, Toast.LENGTH_SHORT).show();
                addProductsToCart(pro, count);
            }
        });
        ////////////////////////

        //////// remove qty /////////
        ib_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = Integer.parseInt(textViewCount.getText().toString());
                if (!(count == 0)) {
                    count--;
                    CartInsertion.cartprice=CartInsertion.cartprice - Double.parseDouble(pro.price);
                    if (count == 0) {
                        imageCheck.setVisibility(View.INVISIBLE);
                    }
                    textViewCount.setText(count + "");
                    int qty = Integer.parseInt(pro.price);
                   // Toast.makeText(context, pro.name + " removed  : " + count+"\nTotal Price"+CartInsertion.cartprice, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Emptyed cart", Toast.LENGTH_SHORT).show();
                }
                addProductsToCart(pro, count);
            }
        });

        return view;
    }

    private void addProductsToCart(AddProducts pro, int count) {

        CartInsertion.cartAddedProducts.remove(pro.name);
        if (count > 0) {
            CartModel cartModel = new CartModel(pro.id, pro.name, pro.price, count, ((Double.parseDouble(pro.price)) * count), pro.img_url);
            CartInsertion.cartAddedProducts.put(pro.name, cartModel);
            Log.d("TextList", cartModel.name + " added(qty:" + cartModel.quantity);
        }

    }
}