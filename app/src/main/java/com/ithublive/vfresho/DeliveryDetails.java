package com.ithublive.vfresho;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;

import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.ithublive.vfresho.Models.CartInsertion;
import com.ithublive.vfresho.Models.CartModel;
import com.ithublive.vfresho.mumbai_package.MumbaiHomeActivity;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeliveryDetails extends AppCompatActivity {
    int flagIntSubmittedOrder = 0;
    EditText edittext_name, edittext_mobilno, edittext_emailid, edittext_address;
    //    Spinner spinner_payment;
    String payment_method[] = {"---Select Payment method---", "Cash On Delivery"};
    String spinner_payment_method, no_select_payment_method;
    ProgressDialog progressDialog;
    String name, mobileno, emailid, address;
    String cart = "", emailCart = "";
    CartModel cartModel;
    String order_id;
    Calendar calendar;
    String current_date;
    String area;
    //TextView tv;
    SharedPreferences sharedPreferences;
    Button button_submit;
    RadioButton rb_9_to_1, rb_4_to_8, rb_cashondelivery;
    RadioButton rb_1p_to_7p_mumbai;
    RadioButton rb_location;
    double cartPrizeLocal;
    String delivery_date;

    @Override
    protected void onResume() {
        super.onResume();
        if (flagIntSubmittedOrder == 1) {
            if (area.equals("Vasai-Virar")) {
                startActivity(new Intent(DeliveryDetails.this, UserHomeActivity.class));
            } else {
                startActivity(new Intent(DeliveryDetails.this, MumbaiHomeActivity.class));
            }
        }
        //  Toast.makeText(this, "Resumed....", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_details);
        // Toast.makeText(this, "Delivery on create called...", Toast.LENGTH_SHORT).show();
        cartPrizeLocal = CartInsertion.cartprice;
        edittext_name = findViewById(R.id.edittext_name);
        edittext_mobilno = findViewById(R.id.edittext_mobileno);
        edittext_emailid = findViewById(R.id.edittext_emailid);
        edittext_address = findViewById(R.id.edittext_address);
//        spinner_payment = findViewById(R.id.spinner_paymentcategory);
        rb_9_to_1 = findViewById(R.id.radio_delivery_9_to_1);
        rb_4_to_8 = findViewById(R.id.radio_delivery_4_to_8);
        rb_1p_to_7p_mumbai = findViewById(R.id.radio_delivery_1pm_to_7pm_mumbai);
        rb_cashondelivery = findViewById(R.id.radio_cash_on_delivery);
        rb_location = findViewById(R.id.radioButton_Location);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);

        SimpleDateFormat format = new SimpleDateFormat("E - dd/MM/YYYY");
        // SimpleDateFormat format_Day = new SimpleDateFormat("E");
        //String nextDayDate= format.format(calendar.getTime());
        rb_9_to_1.setText(" 9 AM to 1 PM");
        rb_4_to_8.setText(" 4 PM to 8 PM");
        rb_1p_to_7p_mumbai.setText(" 1 PM to 7 PM");
        TextView tvNextDayDate = findViewById(R.id.textViewNextDate);

        sharedPreferences = getSharedPreferences("vfresho_spsp", Context.MODE_PRIVATE);
        area = sharedPreferences.getString("location", "");
        rb_location.setText(area);
        if (area.equals("Vasai-Virar")) {
            rb_9_to_1.setVisibility(View.VISIBLE);
            rb_4_to_8.setVisibility(View.VISIBLE);

            rb_1p_to_7p_mumbai.setVisibility(View.GONE);
        } else if (!area.equals("Vasai-Virar")) {
            rb_9_to_1.setVisibility(View.GONE);
            rb_4_to_8.setVisibility(View.GONE);

            rb_1p_to_7p_mumbai.setVisibility(View.VISIBLE);
        }
        if (!area.equals("Vasai-Virar") && calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            format = new SimpleDateFormat(" dd/MM/YYYY");
            calendar.add(Calendar.DAY_OF_YEAR, 2);
            tvNextDayDate.setText(Html.fromHtml("Monday, <font color='red'><u>" + format.format(calendar.getTime()) + " (No delivery on Saturday/Sunday)</u></font>"));//, HtmlCompat.FROM_HTML_MODE_COMPACT));
        } else if (!area.equals("Vasai-Virar") && calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            format = new SimpleDateFormat(" dd/MM/YYYY");
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            tvNextDayDate.setText(Html.fromHtml("Monday, <font color='red'><u>" + format.format(calendar.getTime()) + " (No delivery on Saturday/Sunday)</u></font>"));//, HtmlCompat.FROM_HTML_MODE_COMPACT));
        } else if (area.equals("Vasai-Virar") && calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            format = new SimpleDateFormat(" dd/MM/YYYY");
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            tvNextDayDate.setText(Html.fromHtml("Monday, <font color='red'><u>" + format.format(calendar.getTime()) + " (No delivery on Sunday)</u></font>"));//, HtmlCompat.FROM_HTML_MODE_COMPACT));
        } else {
            tvNextDayDate.setText(Html.fromHtml("Tomorrow, <font color='red'><u>" + format.format(calendar.getTime()) + "</u></font>"));//, HtmlCompat.FROM_HTML_MODE_COMPACT));
        }
        delivery_date = format.format(calendar.getTime());
        button_submit = findViewById(R.id.button_submitProductData);

        progressDialog = new ProgressDialog(this);

        sharedPreferences = getSharedPreferences("vfresho_spsp", MODE_PRIVATE);

        edittext_mobilno.setText(sharedPreferences.getString("mobile", ""));
        edittext_emailid.setText(sharedPreferences.getString("emailid", ""));
//        edittext_mobilno.setEnabled(false);
//        edittext_emailid.setEnabled(false);
        if (sharedPreferences.contains("name") && sharedPreferences.contains("address")) {
            edittext_name.setText(sharedPreferences.getString("name", "defaultValue"));
            edittext_address.setText(sharedPreferences.getString("address", ""));
        }


        ArrayAdapter aa = new ArrayAdapter(DeliveryDetails.this, android.R.layout.simple_spinner_dropdown_item, payment_method);
//        spinner_payment.setAdapter(aa);
//        spinner_payment.setDropDownVerticalOffset(110);
//        spinner_payment.getGravity();

//        spinner_payment.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                ((TextView) spinner_payment.getSelectedView()).setTextColor(Color.BLACK);
//                ((TextView) spinner_payment.getSelectedView()).setTextSize(17);
//            }
//        });

//        spinner_payment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
//                spinner_payment_method = (String) adapterView.getItemAtPosition(position);
//                no_select_payment_method = (String) adapterView.getItemAtPosition(position);
//            }

//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//            }
//        });
        int i = 1;
        for (CartModel cartModel : CartInsertion.cartAddedProducts.values()) {
            cart += cartModel.name + "$$$" + cartModel.quantity + "###" + cartModel.price + "***" + cartModel.img_url + ",!";
            emailCart += (i) + "  " + cartModel.name + "  Price: " + cartModel.price + " Quantity: " + cartModel.quantity + "<br>";
            i++;
        }
        //Log.d("VFreshoEmail", emailCart);
    }

    public void orderPlaced(View view) {
        NetChecker netChecker = new NetChecker();
        netChecker.execute();
    }

    public void mainMethod() {
        button_submit.setEnabled(false);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
        calendar = Calendar.getInstance();
        // Calendar today = calendar;
        current_date = sdf.format(calendar.getTime());

        progressDialog = new ProgressDialog(DeliveryDetails.this);
        progressDialog.setTitle("Placing Order");
        progressDialog.setMessage("Please Wait");
        progressDialog.setIcon(R.drawable.vfresho_logo);
        progressDialog.setCancelable(false);
        progressDialog.show();

        name = edittext_name.getText().toString();
        mobileno = edittext_mobilno.getText().toString();
        emailid = edittext_emailid.getText().toString();
        address = edittext_address.getText().toString();

        String Validemail = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+";

        Matcher matcher = Pattern.compile(Validemail).matcher(emailid);

        if (name.isEmpty()) {
            Toast.makeText(DeliveryDetails.this, "Enter Name", Toast.LENGTH_SHORT).show();
            button_submit.setEnabled(true);
            progressDialog.dismiss();
        } else if (!(matcher.matches())) {
            Toast.makeText(this, "please enter valid data emailid", Toast.LENGTH_SHORT).show();
            button_submit.setEnabled(true);
            progressDialog.dismiss();
        } else if (mobileno.isEmpty()) {
            Toast.makeText(DeliveryDetails.this, "Enter mobile Number", Toast.LENGTH_SHORT).show();
            button_submit.setEnabled(true);
            progressDialog.dismiss();
        } else if (mobileno.length() != 10 || mobileno == null) {
            Toast.makeText(this, "Enter valid mobile number.", Toast.LENGTH_SHORT).show();
            button_submit.setEnabled(true);
            progressDialog.dismiss();
        } else if (address.isEmpty()) {
            Toast.makeText(this, "Enter address", Toast.LENGTH_SHORT).show();
            button_submit.setEnabled(true);
            progressDialog.dismiss();
        } else if (!rb_location.isChecked()) {
            Toast.makeText(this, "Check Location.", Toast.LENGTH_SHORT).show();
            rb_location.setError("choose this location.");
            rb_location.requestFocus();
            button_submit.setEnabled(true);
            progressDialog.dismiss();
        } else if ((!rb_9_to_1.isChecked()) && (!rb_4_to_8.isChecked()) && (!rb_1p_to_7p_mumbai.isChecked())) {
            Toast.makeText(this, "Choose the Delivery slot", Toast.LENGTH_SHORT).show();
            button_submit.setEnabled(true);
            progressDialog.dismiss();
        } else if (!rb_cashondelivery.isChecked()) {
            Toast.makeText(this, "Select payment option", Toast.LENGTH_SHORT).show();
            button_submit.setEnabled(true);
            progressDialog.dismiss();
        }
//        else if (no_select_payment_method.equals("---Select Payment method---")) {
//            Toast.makeText(DeliveryDetails.this, "please select payment method", Toast.LENGTH_SHORT).show();
//            button_submit.setEnabled(true);
//            progressDialog.dismiss();
//        }
        else {
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putString("name", name);
            edit.putString("mobile", mobileno);
            edit.putString("emailid", emailid);
            edit.putString("address", address);
            edit.commit();


            //sharedPreferences=getSharedPreferences("vfresho_spsp", Context.MODE_PRIVATE);
            //area=sharedPreferences.getString("location","");
            //Log.d("vfadmin","Area:"+area);
            String url = "";
            if (area.equals("Vasai-Virar")) {
                url = "http://www.vfresho.in/order_new.php";
            } else if (!area.equals("Vasai-Virar")) {
                url = "http://www.vfresho.in/order_new_mumbai.php";
            }
//            StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://www.vfresho.in/order.php", new Response.Listener<String>() {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("VFresho", "THE RESPONSE: " + response);
                    if (response.contains("DB_connect_Fail.")) {
                        Toast.makeText(DeliveryDetails.this, "Database error.", Toast.LENGTH_SHORT).show();
                        button_submit.setEnabled(true);
                        progressDialog.dismiss();
                    }

                    if (response.contains("data_inserted")) {

                        AlertDialog.Builder adb = new AlertDialog.Builder(DeliveryDetails.this);
                        adb.setTitle("Order placed");
                        adb.setMessage("Your order has been placed successfully and order summary sent on your email id.");
                        adb.setIcon(R.drawable.vfresho_logo);
                        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                CartInsertion.cartprice = 0;
                                CartInsertion.cartAddedProducts.clear();

                                //Log.d("VFresho", "ORDER ID: " + order_id);
                                ///////////// Show App Share Dialogue /////////////////

                                AlertDialog.Builder adbShareApp = new AlertDialog.Builder(DeliveryDetails.this);
                                adbShareApp.setTitle("Share VFresho...");
                                adbShareApp.setMessage("Share VFresho with friends and family.");
                                adbShareApp.setIcon(R.drawable.vfresho_logo);
                                adbShareApp.setPositiveButton("Share", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {


                                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                                        shareIntent.setType("text/plain");
                                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "VFresho");
                                        String shareMessage = "Let me recommend you this application as I use to buy daily Vegetables & Fruits from VFresho.\n\n ";
                                        shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "  \n\n"
                                                + "Shop on Web:\n http://www.vfresho.com";
                                        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                                        startActivity(Intent.createChooser(shareIntent, "choose one"));

                                    }
                                });
                                adbShareApp.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (area.equals("Vasai-Virar")) {
                                            startActivity(new Intent(DeliveryDetails.this, UserHomeActivity.class));
                                        } else {
                                            startActivity(new Intent(DeliveryDetails.this, MumbaiHomeActivity.class));
                                        }
                                    }
                                });
                                adbShareApp.show();


                            }
                        });
                        adb.setCancelable(false);
                        adb.show();
                        progressDialog.dismiss();
                        if (response.contains("Oid")) {
                            sendEmail();
                            order_id = response.substring(response.indexOf("Oid") + 3);
                        }
                        Toast.makeText(DeliveryDetails.this, "Order placed Successfully and order summary sent on your email id.", Toast.LENGTH_SHORT).show();
                        //cartPrizeLocal=CartInsertion.cartprice;
                        CartInsertion.cartprice = 0;
                        CartInsertion.cartAddedProducts.clear();
                        flagIntSubmittedOrder = 1;
                        // button_submit.setEnabled(true);


                    }

                    if (response.contains("order_already_exists")) {
                        Toast.makeText(DeliveryDetails.this, "exists", Toast.LENGTH_SHORT).show();
                        button_submit.setEnabled(true);
                        progressDialog.dismiss();
                    }

                    if (response.contains("Oid")) {
                        // sendEmail();
                        // order_id = response.substring(response.indexOf("Oid") + 3);

                    }

                    if (response.contains("data_not_inserted")) {
                        button_submit.setEnabled(true);
                        progressDialog.dismiss();

                        AlertDialog.Builder adb = new AlertDialog.Builder(DeliveryDetails.this);
                        adb.setTitle("Order not placed");
                        adb.setMessage("Somthing went wrong");
                        adb.setIcon(R.drawable.vfresho_logo);
                        adb.setPositiveButton("Ok", null);
                        adb.setCancelable(false);
                        adb.show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    button_submit.setEnabled(true);
                    progressDialog.dismiss();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();
                    map.put("name", name);
                    map.put("mobile_no", mobileno);
                    map.put("email_id", emailid);
                    map.put("order_date", current_date);
                    map.put("address", address);
                    map.put("orderlist", cart);
                    map.put("grandtotal", cartPrizeLocal + "");
                    map.put("remark", "pending");
                    map.put("payment_method", "Cash On Delivery");
                    if (rb_9_to_1.isChecked()) {
                        map.put("delivery_slot", "9 AM to 1 PM");
                    }
                    if (rb_4_to_8.isChecked()) {
                        map.put("delivery_slot", "4 PM to 8 PM");
                    }
                    if (rb_1p_to_7p_mumbai.isChecked()) {
                        map.put("delivery_slot", "1 PM to 7 PM");
                    }
                    return map;
                }
            };

            Volley.newRequestQueue(DeliveryDetails.this).add(stringRequest);

            //////////////////////
        }
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

    class NetChecker extends AsyncTask<String, Void, Integer> {

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
                    //Toast.makeText(DeliveryDetails.this, "  internet available ", Toast.LENGTH_SHORT).show();
                }

                if (result == 0) {
                    Toast.makeText(DeliveryDetails.this, " No internet available ", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(DeliveryDetails.this, " No internet connection available ", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(result);
        }
    }

    public void sendEmail() {

        ///////////// send mail ///////////////
        StringRequest emailSend = new StringRequest(Request.Method.POST, "http://www.vfresho.in/sendemail.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("VFreshoEmail", response);
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("VFreshoEmail", "Error Aala" + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("name", name);
                map.put("mobile_no", mobileno);
                map.put("email_id", emailid);
                map.put("order_id", order_id);
                map.put("address", address);
                map.put("orderlist", emailCart);
                map.put("delivery_date", delivery_date);
                map.put("grandtotal", cartPrizeLocal + "");
                if (rb_9_to_1.isChecked()) {
                    map.put("delivery_slot", "9 AM to 1 PM");
                }
                if (rb_4_to_8.isChecked()) {
                    map.put("delivery_slot", "4 PM to 8 PM");
                }
                if (rb_1p_to_7p_mumbai.isChecked()) {
                    map.put("delivery_slot", "1 PM to 7 PM");
                }
                return map;
            }

        };
        Volley.newRequestQueue(DeliveryDetails.this).add(emailSend);
    }
}