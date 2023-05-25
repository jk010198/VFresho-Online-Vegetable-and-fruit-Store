package com.ithublive.vfresho;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.ithublive.vfresho.mumbai_package.MumbaiHomeActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LocationChooserActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    Spinner spinner_location;
    String locations[]; //= {"--Select location--", "Andheri", "Borivali", "Kandivali","Powai" ,"Virar"};
    String location = "--Select location--";
    Button buttonStartShop;
    //TextView tv_mobile, tv_email;
   // EditText editText_mobilelogin, editText_emaillogin;
    boolean freshUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_chooser);
      //  editText_mobilelogin = findViewById(R.id.editText_mobilelogin);
      //  editText_emaillogin = findViewById(R.id.editText_emaillogin);

        sharedPreferences = getSharedPreferences("vfresho_spsp", MODE_PRIVATE);


        String frmMenu = "naaaa";
        try {
            frmMenu = getIntent().getStringExtra("fromMenu");
            frmMenu = (frmMenu == null) ? ("null") : (frmMenu);
        } catch (NullPointerException ex) {
            frmMenu = "null";
        }
        if (!(frmMenu.equals("yesssss"))) {
            String loc = sharedPreferences.getString("location", "s_null");
            if (loc.equals("Vasai-Virar")) {
                Intent intent = new Intent(LocationChooserActivity.this, UserHomeActivity.class);
                intent.putExtra("location", loc);
                startActivity(intent);
            } else if ((!loc.equals("Vasai-Virar")) && (!loc.equals("s_null"))) { //location.equals("Andheri") || location.equals("Borivali") || location.equals("Kandivali")

                Intent intent = new Intent(LocationChooserActivity.this, MumbaiHomeActivity.class);
                intent.putExtra("location", loc);
                startActivity(intent);
            }
        }

//        tv_mobile = findViewById(R.id.tv_mobile4545);
//        tv_email = findViewById(R.id.tv_email4545);
       // final String mob = sharedPreferences.getString("mobile", "0");
       // final String email = sharedPreferences.getString("emailid", "nahi_hai");
//        if (mob.length() == 10 && !email.equals("nahi_hai")) {
//            //freshUser=false;
//            tv_mobile.setVisibility(View.INVISIBLE);
//            tv_email.setVisibility(View.INVISIBLE);
//            editText_mobilelogin.setVisibility(View.GONE);
//            editText_emaillogin.setVisibility(View.GONE);
//        }

        String placessesString = sharedPreferences.getString("allplacesses", "");
        //int xx = 0;
        locations = placessesString.split(",");
        spinner_location = findViewById(R.id.spinner_location);
        buttonStartShop = findViewById(R.id.buttonStartShop);
        final ArrayAdapter aa = new ArrayAdapter(LocationChooserActivity.this, android.R.layout.simple_spinner_dropdown_item, locations);
        spinner_location.setAdapter(aa);
        spinner_location.setDropDownVerticalOffset(110);
        spinner_location.getGravity();

        buttonStartShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  if (mob.equals("0") && email.equals("nahi_hai")) {
               //     String mobile_no = editText_mobilelogin.getText().toString();
                //    String email_id = editText_emaillogin.getText().toString();
//                    String Validemail = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\." +
                           // "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+";

                  //  Matcher matcher = Pattern.compile(Validemail).matcher(email_id);
                    location = spinner_location.getSelectedItem().toString();
                    if (location.equals("--Select location--")) {
                        AlertDialog.Builder adb = new AlertDialog.Builder(LocationChooserActivity.this);
                        adb.setTitle("Choose location.");
                        adb.setMessage("Please select your location...");
                        adb.setIcon(R.drawable.vfresho_logo);
                        adb.setPositiveButton("Ok", null);
                        adb.show();
                    }
//                    } else if (mobile_no.length() != 10) {
//                        AlertDialog.Builder adb = new AlertDialog.Builder(LocationChooserActivity.this);
//                        adb.setTitle("Mobile");
//                        adb.setMessage("Please enter proper mobile no...");
//                        adb.setIcon(R.drawable.vfresho_logo);
//                        adb.setPositiveButton("Ok", null);
//                        adb.show();
//                    } else if (!(matcher.matches())) {
//                        AlertDialog.Builder adb = new AlertDialog.Builder(LocationChooserActivity.this);
//                        adb.setTitle("Email id");
//                        adb.setMessage("Please enter proper email id...");
//                        adb.setIcon(R.drawable.vfresho_logo);
//                        adb.setPositiveButton("Ok", null);
//                        adb.show();
//                    }
//                    else {
//                        //  Log.d("VFresho", "All valid going to otp ");
//
//                        Intent intent = new Intent(LocationChooserActivity.this, OtpVerificationActivity.class);
//                        intent.putExtra("location", location);
//                        //   intent.putExtra("mobile", mobile_no);
//                        // intent.putExtra("emailid", email_id);
//
//                        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(intent);
//                        //}
//                    }
//                 else {
//                    location = spinner_location.getSelectedItem().toString();
//                    if (location.equals("--Select location--")) {
//                        AlertDialog.Builder adb = new AlertDialog.Builder(LocationChooserActivity.this);
//                        adb.setTitle("Choose location.");
//                        adb.setMessage("Please select your location...");
//                        adb.setIcon(R.drawable.vfresho_logo);
//                        adb.setPositiveButton("Ok", null);
//                        adb.show();
//                    }
                    else {
                        Log.d("VFresho", "All valid going after location change... ");
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("location", location);
                        editor.commit();
                        if (location.equals("Vasai-Virar")) {
                            Intent intent = new Intent(LocationChooserActivity.this, UserHomeActivity.class);
                            intent.putExtra("location", location);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(LocationChooserActivity.this, MumbaiHomeActivity.class);
                            intent.putExtra("location", location);
                            startActivity(intent);
                        }
                    }

            //    }


            }
        });

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle("Close VFresho.");
        adb.setIcon(R.drawable.vfresho_logo);
        adb.setMessage("Do you want to exit ?");
        adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getApplicationContext(), SplashScreen.class);
                intent.putExtra("exit_code", true);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        adb.setNegativeButton("No", null);
        adb.show();
    }
}
