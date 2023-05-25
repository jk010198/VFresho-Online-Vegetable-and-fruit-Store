package com.ithublive.vfresho.mumbai_package;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.ithublive.vfresho.BuildConfig;
import com.ithublive.vfresho.Fragments.FragmentFruits;
import com.ithublive.vfresho.Fragments.FragmentVegetables;
import com.ithublive.vfresho.LocationChooserActivity;
import com.ithublive.vfresho.Models.CartInsertion;
import com.ithublive.vfresho.Models.CartModel;
import com.ithublive.vfresho.R;
import com.ithublive.vfresho.SplashScreen;
import com.ithublive.vfresho.UserHomeActivity;
import com.ithublive.vfresho.ViewCart;
import com.ithublive.vfresho.mumbai_package.fragments.FragmentFruitsMumbai;
import com.ithublive.vfresho.mumbai_package.fragments.FragmentVegetablesMumbai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MumbaiHomeActivity extends AppCompatActivity {

private TabLayout mtabLayout;
    private ViewPager mviewPager;
    List<CartModel> viewcart1;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mumbai_home);

        FirebaseApp.initializeApp(getApplicationContext());
        mtabLayout = findViewById(R.id.tablayoutMumbai);
        mviewPager = findViewById(R.id.viewpagerMumbai);

        setUpViewPager(mviewPager);
        mtabLayout.setupWithViewPager(mviewPager);
        viewcart1 = new ArrayList<>();
       CartInsertion.cartAddedProducts =new HashMap<String,CartModel>();
        sharedPreferences = getSharedPreferences("vfresho_spsp", MODE_PRIVATE);
//      String  loc_ab = sharedPreferences.getString("location", "");
//        String ab_text= "VFresho <i><u> <font color='red' size='10'>("+loc_ab+")</font></u></i>";
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setTitle(Html.fromHtml(ab_text,HtmlCompat.FROM_HTML_MODE_COMPACT));

        // displayNarialPaniDialog();
    }

    private void setUpViewPager(ViewPager viewPager) {
    MumbaiHomeActivity.viewPagerAdapter adapter = new MumbaiHomeActivity.viewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new FragmentVegetablesMumbai(), "Vegetables");
        adapter.addFragment(new FragmentFruitsMumbai(), "Fruits");

        viewPager.setAdapter(adapter);
    }

    public void viewCart(View view) {
       // Toast.makeText(this, "" + CartInsertion.cartprice, Toast.LENGTH_SHORT).show();
        if (CartInsertion.cartAddedProducts.size() > 0) {

            if (CartInsertion.cartprice >= 399) {
                startActivity(new Intent(getApplicationContext(), ViewCart.class));
            } else {
                Toast.makeText(MumbaiHomeActivity.this, "Minimum shopping RS. 399/-.", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(MumbaiHomeActivity.this, "Please select any products.", Toast.LENGTH_SHORT).show();
        }
    }

    class viewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mfragmentList = new ArrayList<>();
        private final List<String> mframentTitleList = new ArrayList<>();

        public viewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return mfragmentList.get(i);
        }

        @Override
        public int getCount() {
            return mfragmentList.size();
        }

        public void addFragment(Fragment fragment, String string) {
            mfragmentList.add(fragment);
            mframentTitleList.add(string);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mframentTitleList.get(position);
        }
    }

    //// create users_menu //////////
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mumbai_users_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    ///// when clicking users_menu option //////////
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        switch (i) {
            case R.id.menu_mumbai_aboutus:
            {
                // show popup
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MumbaiHomeActivity.this);

                LayoutInflater layoutInflater = LayoutInflater.from(MumbaiHomeActivity.this);
                View popupInputDialogView = layoutInflater.inflate(R.layout.layout_about_us, null);

                alertDialogBuilder.setView(popupInputDialogView);
                alertDialogBuilder.show();
                break;
            }
            case R.id.menu_mumbai_contactUs:
            {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MumbaiHomeActivity.this);

                LayoutInflater layoutInflater = LayoutInflater.from(MumbaiHomeActivity.this);
                View popupInputDialogView = layoutInflater.inflate(R.layout.layout_contact_us, null);
                TextView tv_contactus= popupInputDialogView.findViewById(R.id.dialog_textview_contact_us);
                String htmlText = "<b>VFresho</b>" +
                        "         <br><br>our website:<font color='blue'><u> https://vfresho.com</u></font><br>" +
                        "         <br>mobile: <font color='blue'>7738678013</font><br>" +
                        "         <br>email: <font color='blue'>vfreshovirar@gmail.com</font><br>";
                if (Build.VERSION.SDK_INT >= 24) {
                    tv_contactus.setText(Html.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_COMPACT)); // for 24 api and more
                } else {
                    tv_contactus.setText(Html.fromHtml(htmlText)); // or for older api
                }
                tv_contactus.setTextIsSelectable(true);
                alertDialogBuilder.setView(popupInputDialogView);
                alertDialogBuilder.show();
                break;
            }

            case R.id.mumbai_cart_menu:
                if (CartInsertion.cartAddedProducts.size() > 0) {

                    if (CartInsertion.cartprice >= 399) {
                        startActivity(new Intent(getApplicationContext(), ViewCart.class));
                    } else {
                        Toast.makeText(MumbaiHomeActivity.this, "Minimum shopping RS.399/-.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MumbaiHomeActivity.this, "Please select any products.", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.mumbai_refresh:
                findViewById(R.id.mumbai_refresh).setEnabled(false);
                setUpViewPager(mviewPager);
                mtabLayout.setupWithViewPager(mviewPager);

                Timer buttonTimer = new Timer();
                buttonTimer.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                findViewById(R.id.mumbai_refresh).setEnabled(true);
                            }
                        });
                    }
                }, 10000);

                break;
            case R.id.menu_mumbai_shareApp:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "VFresho");
                String shareMessage = "Let me recommend you this application as I use to buy daily Vegetables & Fruits from VFresho.\n\n ";
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"  \n\n"
                        +"Shop on Web:\n http://www.vfresho.com";
                 shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
                break;
            case R.id.menu_mumbai_changeLocation:
                Intent intentLocation= new Intent(MumbaiHomeActivity.this,LocationChooserActivity.class);
                intentLocation.putExtra("fromMenu","yesssss");
                startActivity(intentLocation);
                break;
//            case R.id.menu_mumbai_logout:
//                AlertDialog.Builder adb = new AlertDialog.Builder(this);
//                adb.setTitle("Logout");
//                adb.setIcon(R.drawable.vfresho_logo);
//                adb.setMessage("Do you want to Logout ??");
//                adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        FirebaseAuth mAuth = FirebaseAuth.getInstance();
//                        mAuth.signOut();
//                        SharedPreferences sharedPreferences=getSharedPreferences("vfresho_spsp",MODE_PRIVATE);
//                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                        editor.clear();
//                        editor.commit();
//                        Intent intent = new Intent(MumbaiHomeActivity.this, SplashScreen.class);
//                        intent.putExtra("exit_code", true);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(intent);
//                    }
//                });
//                adb.setNegativeButton("No", null);
//                adb.show();
//                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle("Close VFresho.");
        //adb.setIcon(R.drawable.vfresho_logo);
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