package com.ithublive.vfresho;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ithublive.vfresho.Adaptors.CartArrayAdaptor;
import com.ithublive.vfresho.Models.CartInsertion;
import com.ithublive.vfresho.Models.CartModel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

public class ViewCart extends AppCompatActivity {

    static ListView lv;
    List<CartModel> viewcart;
    CartArrayAdaptor cartArrayAdaptor;
    CartModel product;
    static public TextView textView;
    TextView tv_deliveryCharges,tv_total;
    String location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cart);

        textView = findViewById(R.id.textview_grandtotal);
        tv_deliveryCharges=findViewById(R.id.textview_deliveryCharges);
        tv_total=findViewById(R.id.textview_total);
        SharedPreferences sharedPreferences= getSharedPreferences("vfresho_spsp",MODE_PRIVATE);
         location=sharedPreferences.getString("location","");
        if(location.equals("Vasai-Virar"))
        {
            tv_deliveryCharges.setVisibility(View.GONE);
            tv_total.setVisibility(View.GONE);
            textView.setText("GrandTotal:- ₹ " + CartInsertion.cartprice + "");
        }
        else{
            tv_total.setVisibility(View.VISIBLE);
            tv_total.setText("Total ₹ "+CartInsertion.cartprice);
            tv_deliveryCharges.setVisibility(View.VISIBLE);
            textView.setText("Grand Total:- ₹ " + (CartInsertion.cartprice+50));
            CartInsertion.cartprice+=50;
        }


        lv = findViewById(R.id.listview_viewcart);

        viewcart = new ArrayList<>();
        for ( CartModel cm  : CartInsertion.cartAddedProducts.values())
        {
            product = cm;
            viewcart.add(product);
        }
//        for (int i = 0; i < CartInsertion.cartProducts.size(); i++) {
//            product = CartInsertion.cartProducts.get(i);
//            viewcart.add(product);
//        }

        cartArrayAdaptor = new CartArrayAdaptor(ViewCart.this, R.layout.list_layout_cart, viewcart);
        lv.setAdapter(cartArrayAdaptor);
    }

    public void nextToDetails(View view) {
        abc abc = new abc();
        abc.execute();
    }

    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Service.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            NetworkInfo info = connectivityManager.getActiveNetworkInfo();
            if (info != null) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    class abc extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {

            Integer result = 0;
            try {
                Socket socket = new Socket();
                SocketAddress socketAddress = new InetSocketAddress("8.8.8.8", 53);
                socket.connect(socketAddress, 1500);
                socket.close();
                result = 1;
            } catch (IOException e) {
                e.printStackTrace();
                result = 0;
            }
            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            if (isConnected()) {
                if (result == 1) {
                    Intent i = new Intent(ViewCart.this, DeliveryDetails.class);
                    i.putExtra("payment_amount", CartInsertion.cartprice);
                    startActivity(i);
                }

                if (result == 0) {
                    Toast.makeText(ViewCart.this, " No internet available ", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(ViewCart.this, " No internet connection available ", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(result);
        }


    }

    @Override
    public void onBackPressed() {
        if(location.equals("Vasai-Virar"))
        {

        }
        else{
             CartInsertion.cartprice-=50;
        }

        super.onBackPressed();

    }
}