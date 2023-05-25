package com.ithublive.vfresho;

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
import com.ithublive.vfresho.Fragments.FragmentFruits;
import com.ithublive.vfresho.Fragments.FragmentVegetables;
import com.ithublive.vfresho.Models.CartInsertion;
import com.ithublive.vfresho.Models.CartModel;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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

public class UserHomeActivity extends AppCompatActivity {

    private TabLayout mtabLayout;
    private ViewPager mviewPager;
    List<CartModel> viewcart1;
    final int PERMISSION_REQUEST_CODE = 1212;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        FirebaseApp.initializeApp(getApplicationContext());
        mtabLayout = findViewById(R.id.tablayout);
        mviewPager = findViewById(R.id.viewpager);

        setUpViewPager(mviewPager);
        mtabLayout.setupWithViewPager(mviewPager);
        viewcart1 = new ArrayList<>();
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setTitle(Html.fromHtml("VFresho <i><u> <font color='red' size='10'>(Vasai-Virar)</font></u></i>",HtmlCompat.FROM_HTML_MODE_COMPACT));
        displayNotice();
       // displayNarialPaniDialog();

    }

    private void displayNotice() {
        String notifUser = getIntent().getStringExtra("notifUser");
        String notifTitle = getIntent().getStringExtra("notifTitle");
        String notifMsg = getIntent().getStringExtra("notifMsg");
        if (notifUser!=null && notifUser.equals("yes")) {
            AlertDialog.Builder adbNotice = new AlertDialog.Builder(UserHomeActivity.this);
            adbNotice.setTitle(notifTitle);
            // adbNotice.setMessage("WE HAVE CLOSED OUR SERVICES TILL LOCK-DOWN. \n\nSORRY FOR THE INCONVENIENCE.");
            adbNotice.setMessage(notifMsg);
            adbNotice.setCancelable(false);
            adbNotice.setPositiveButton("Ok", null);
            adbNotice.show();
        }
    }

    private void setUpViewPager(ViewPager viewPager) {
        viewPagerAdapter adapter = new viewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new FragmentVegetables(), "Vegetables");
        adapter.addFragment(new FragmentFruits(), "Fruits");

        viewPager.setAdapter(adapter);
    }

    public void viewCart(View view) {
        // Toast.makeText(this, "" + CartInsertion.cartprice, Toast.LENGTH_SHORT).show();
        if (CartInsertion.cartAddedProducts.size() > 0) {

            if (CartInsertion.cartprice >= 150) {
                startActivity(new Intent(getApplicationContext(), ViewCart.class));
            } else {
                Toast.makeText(UserHomeActivity.this, "Minimum shopping RS.150/-.", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(UserHomeActivity.this, "Please select any products.", Toast.LENGTH_LONG).show();
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
        getMenuInflater().inflate(R.menu.users_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    ///// when clicking users_menu option //////////
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        switch (i) {
            case R.id.menu_aboutus: {
                // show popup
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(UserHomeActivity.this);

                LayoutInflater layoutInflater = LayoutInflater.from(UserHomeActivity.this);
                View popupInputDialogView = layoutInflater.inflate(R.layout.layout_about_us, null);

                alertDialogBuilder.setView(popupInputDialogView);
                alertDialogBuilder.show();
                break;
            }
            case R.id.menu_contactUs: {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(UserHomeActivity.this);

                LayoutInflater layoutInflater = LayoutInflater.from(UserHomeActivity.this);
                View popupInputDialogView = layoutInflater.inflate(R.layout.layout_contact_us, null);
                TextView tv_contactus = popupInputDialogView.findViewById(R.id.dialog_textview_contact_us);
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
//            case R.id.menu_subscribeNarialPani: {
//                displayNarialPaniDialog();
//                break;
//            }
            case R.id.cart_menu:
                if (CartInsertion.cartAddedProducts.size() > 0) {

                    if (CartInsertion.cartprice >= 150) {
                        startActivity(new Intent(getApplicationContext(), ViewCart.class));
                    } else {
                        Toast.makeText(UserHomeActivity.this, "Minimum shopping RS.150/-.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(UserHomeActivity.this, "Please select any products.", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.refresh:
                findViewById(R.id.refresh).setEnabled(false);
                setUpViewPager(mviewPager);
                mtabLayout.setupWithViewPager(mviewPager);

                Timer buttonTimer = new Timer();
                buttonTimer.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                findViewById(R.id.refresh).setEnabled(true);
                            }
                        });
                    }
                }, 10000);

                break;

            case R.id.menu_shareApp:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "VFresho");
                String shareMessage = "Let me recommend you this application as I use to buy daily Vegetables & Fruits from VFresho.\n\n ";
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"  \n\n"
                        +"Shop on Web:\n http://www.vfresho.com";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
                break;
            case R.id.menu_changeLocation:
                Intent intentLocation= new Intent(UserHomeActivity.this,LocationChooserActivity.class);
                intentLocation.putExtra("fromMenu","yesssss");
                startActivity(intentLocation);
                break;
//            case R.id.menu_logout:
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
//                        Intent intent = new Intent(UserHomeActivity.this, SplashScreen.class);
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

    private void displayNarialPaniDialog() {
        ///////////// Dialogue for Coconut water ///////////////////
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(UserHomeActivity.this);
        final AlertDialog alert = alertDialogBuilder.create();
        final LayoutInflater layoutInflater = LayoutInflater.from(UserHomeActivity.this);
        final View popupInputDialogView = layoutInflater.inflate(R.layout.layout_coconut_request, null);
        alert.setView(popupInputDialogView);
        alert.show();

        final EditText et_sub_name = popupInputDialogView.findViewById(R.id.edittext_dialog_coconut_name);
        final EditText et_sub_mobile = popupInputDialogView.findViewById(R.id.edittext_dialog_coconut_mobile);
        final EditText et_sub_address = popupInputDialogView.findViewById(R.id.edittext_dialog_coconut_address);
        final EditText et_sub_quantity = popupInputDialogView.findViewById(R.id.edittext_dialog_coconut_quantity);
        final RadioButton rb30 = popupInputDialogView.findViewById(R.id.radio_dialog_coconut_price30);
        final RadioButton rb40 = popupInputDialogView.findViewById(R.id.radio_dialog_coconut_price40);
        Button submitSubscription = popupInputDialogView.findViewById(R.id.buttonSubmitCoconut);


        submitSubscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String s1 = et_sub_name.getText().toString();
                final String s2 = et_sub_mobile.getText().toString();
                final String s3 = et_sub_address.getText().toString();
                final String s4 = et_sub_quantity.getText().toString();
                String pr = "";
                if (rb30.isChecked()) {
                    pr = "30";
                } else if (rb40.isChecked()) {
                    pr = "40";
                }

                final String finalPr = pr;

                if (s1.isEmpty() || s1.length() == 0) {
                    et_sub_name.setError("Enter name");
                    et_sub_name.requestFocus();
                    return;
                }
                if (s2.isEmpty() || s2.length() != 10) {
                    et_sub_mobile.setError("Enter valid mobile");
                    et_sub_mobile.requestFocus();
                    return;
                }
                if (s3.isEmpty() || s3.length() == 0) {
                    et_sub_address.setError("Enter address");
                    et_sub_address.requestFocus();
                    return;
                }
                if (s4.isEmpty() || s4.length() == 0) {
                    et_sub_quantity.setError("Enter quantity");
                    et_sub_quantity.requestFocus();
                    return;
                }
                if ((!rb30.isChecked()) && (!rb40.isChecked())) {
                    Toast.makeText(UserHomeActivity.this, "Select Coconut price", Toast.LENGTH_LONG).show();
                    rb30.setError("select price");
                    return;
                }
                sendEmail(s1, s2, s3, s4, finalPr);
                alert.dismiss();
            }
        });
        ImageButton btn_close_narialDialog = popupInputDialogView.findViewById(R.id.btn_dialog_coconut_close);

        btn_close_narialDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //alert.cancel();
                alert.dismiss();
            }
        });
        ///////////////////////////////////////////////////////////

    }

    private void sendEmail(final String sub_name, final String sub_mobile, final String sub_add, final String sub_qty, final String sub_price) {

        AsyncTask emailAsyncTask = new AsyncTask() {

            @Override
            protected void onPostExecute(Object o) {
                if (o.toString().equals("Positive")) {
                    Toast.makeText(UserHomeActivity.this, "Subscription Submitted.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(UserHomeActivity.this, "Error. Please try again", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected Object doInBackground(Object[] objects) {
                final String username = "vfreshovirar@gmail.com";
                final String password = "vfresho123";

                Properties prop = new Properties();
                prop.put("mail.smtp.host", "smtp.gmail.com");
                prop.put("mail.smtp.port", "465");
                prop.put("mail.smtp.auth", "true");
                prop.put("mail.smtp.socketFactory.port", "465");
                prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

                Session session = Session.getInstance(prop,
                        new javax.mail.Authenticator() {
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(username, password);
                            }
                        });

                try {

                    Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress("from@gmail.com"));
                    message.setRecipients(
                            Message.RecipientType.TO,
                            InternetAddress.parse("vfreshovirar@gmail.com, vfreshovirar@gmail.com")
                    );
                    message.setSubject("Narial Pani Subscription");
                    String emailBody = sub_name + " has subscribed for Narial Pani. Details are: \n" +
                            "Name: " + sub_name + " \nMobile: " + sub_mobile + " \nAddress: " + sub_add
                            + "\nQuantity: " + sub_qty + " \nPrice: " + sub_price;

                    message.setText(emailBody);

                    Transport.send(message);

                    return "Positive";

                } catch (MessagingException e) {
                    e.printStackTrace();
                    return "Negative";
                }

            }
        };

        emailAsyncTask.execute();


        // Log.i("GMail","toEmail: "+toEmail);


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