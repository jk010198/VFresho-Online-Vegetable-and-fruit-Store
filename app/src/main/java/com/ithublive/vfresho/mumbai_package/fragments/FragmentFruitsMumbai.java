package com.ithublive.vfresho.mumbai_package.fragments;


import android.app.ProgressDialog;
import android.app.Service;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.ithublive.vfresho.Adaptors.ProductArrayAdaptor;
import com.ithublive.vfresho.Models.AddProducts;
import com.ithublive.vfresho.Models.CartModel;
import com.ithublive.vfresho.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentFruitsMumbai extends Fragment {

    JsonArrayRequest request;
    RequestQueue requestQueue;
    List<AddProducts> productlist;
    ListView list_product;
    ProductArrayAdaptor productArrayAdaptor;
    static List<CartModel> viewcart1;
    ProgressDialog progressDialog;

    public FragmentFruitsMumbai() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_fruits_mumbai, container, false);

        progressDialog = new ProgressDialog(getActivity());
        list_product = view.findViewById(R.id.listview_fruits_mumbai);

        productlist = new ArrayList<>();
        viewcart1 = new ArrayList<>();

       // CartInsertion.cartAddedProducts =new HashMap<String,CartModel>();
        abc abc = new abc();
        abc.execute();

        return view;
    }

    public void mainMethod() {
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Please wait");
        progressDialog.setIcon(R.drawable.vfresho_logo);
        progressDialog.setCancelable(false);
        progressDialog.show();

        productlist.clear();

        request = new JsonArrayRequest("http://www.vfresho.in/viewproduct_fruits_mumbai.php", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                JSONObject jsonObject = null;

                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        AddProducts addProducts = new AddProducts();
                        addProducts.setId(jsonObject.getString("id"));
                        addProducts.setName(jsonObject.getString("name"));
                        addProducts.setPrice(jsonObject.getString("price"));
                        addProducts.setOld_price(jsonObject.getString("old_price"));
                        addProducts.setPercent_discount(jsonObject.getString("order_in_list"));
                        addProducts.setFood_category(jsonObject.getString("category"));
                        addProducts.setImg_url(jsonObject.getString("img_url"));
                        productlist.add(addProducts);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                productArrayAdaptor = new ProductArrayAdaptor(getActivity(),
                        R.layout.list_layout_product, (ArrayList<AddProducts>) productlist);
                list_product.setAdapter(productArrayAdaptor);
                progressDialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        });

        requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);

    }

    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Service.CONNECTIVITY_SERVICE);

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
                    mainMethod();
                    //Toast.makeText(getActivity(), "  internet available ", Toast.LENGTH_SHORT).show();
                }

                if (result == 0) {
                    Toast.makeText(getActivity(), " No internet available ", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), " No internet connection available ", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(result);
        }
    }
}