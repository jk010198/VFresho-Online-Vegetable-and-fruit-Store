package com.ithublive.vfresho;

import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Map;

public class SplashScreen extends AppCompatActivity {
    AlertDialog.Builder adbLockDown;
    int myAppVersion = 16; // 14_8_2020(15-16)(No delivery on sat in mumbai)// 29-jul (13 to14,14 to 15) //15-jul (11 to 12)(12 to 13) removed login // 27_jun_2020 (8 to 9 then 9 to 10, 10 to 11) Mumbai reagion added.
    //13_apr(7 to 8)Opening app and AutoClosing and Notification from server
    // 9_apr_2020 (6 to 7) (closing service for lockdown of COvid-19)//04_Apr_2020(5 to 6) to match with build.gradle // 04_Apr_2020(4 to 5) //28-marc h2020 (3 to 4)
    ProgressBar progressBar;
    String appClose, appCloseMsg, appCloseMsgBtn1, appCloseMsgBtn2, notifUser, notifTitle, notifMsg, placesses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        progressBar = findViewById(R.id.progressBarsplashscreen);
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Intent in = getIntent();
        boolean exitcode = in.getBooleanExtra("exit_code", false);
        if (exitcode) {
            finish();
            System.exit(0);
        }
        NetCheckTask task = new NetCheckTask();
        task.execute();
    }

    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Service.CONNECTIVITY_SERVICE);

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

    class NetCheckTask extends AsyncTask<String, Void, Integer> {

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
                    /////////////////////Checking For Latest Version///////
                    StringRequest requestToCheckVersion = new StringRequest(StringRequest.Method.POST, "http://www.vfresho.in/softwareversion_new.php",

                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    //////////////////////
                                    // Toast.makeText(SplashScreen.this, "NotifMsg: " +response.indexOf("#7#"), Toast.LENGTH_SHORT).show();
                                    int serverAppVersion = Integer.parseInt(response.substring(response.indexOf("*0*") + 3, response.indexOf("*1*")));
                                    appClose = response.substring(response.indexOf("*1*") + 3, response.indexOf("*2*"));
                                    appCloseMsg = response.substring(response.indexOf("*2*") + 3, response.indexOf("*3*"));
                                    appCloseMsgBtn1 = response.substring(response.indexOf("*3*") + 3, response.indexOf("*4*"));
                                    appCloseMsgBtn2 = response.substring(response.indexOf("*4*") + 3, response.indexOf("*5*"));
                                    notifUser = response.substring(response.indexOf("*5*") + 3, response.indexOf("*6*"));
                                    notifTitle = response.substring(response.indexOf("*6*") + 3, response.indexOf("#7#"));
                                    notifMsg = response.substring(response.indexOf("#7#") + 3, response.indexOf("#8#"));
                                    placesses = response.substring(response.indexOf("#8#") + 3, response.indexOf("#9#"));
                                    ////////////////////
                                    // Toast.makeText(SplashScreen.this, "Response: " + response, Toast.LENGTH_SHORT).show();
                                    // Toast.makeText(SplashScreen.this, "NotifMsg: " + notifMsg, Toast.LENGTH_SHORT).show();

                                    // serverAppVersion = Integer.parseInt(response.trim());
                                    if (myAppVersion >= serverAppVersion) {
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (appClose.equals("no")) {
                                                    Intent intent = new Intent(getApplicationContext(), LocationChooserActivity.class);
                                                    //Intent intent = new Intent(getApplicationContext(), UserHomeActivity.class); // ye tha...
                                                    //Intent intent= new Intent(getApplicationContext(),LoginPage.class);
                                                    intent.putExtra("notifUser", notifUser);
                                                    intent.putExtra("notifTitle", notifTitle);
                                                    intent.putExtra("notifMsg", notifMsg);
                                                    intent.putExtra("placesses", "--Select location--,"+placesses);
                                                    SharedPreferences sharedPreferences = getSharedPreferences("vfresho_spsp", MODE_PRIVATE);
                                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                                    editor.putString("allplacesses", "--Select location--,"+placesses);
                                                    editor.commit();
                                                    startActivity(intent);
                                                } else if (appClose.equals("yes")) {
                                                    adbLockDown = new AlertDialog.Builder(SplashScreen.this);
                                                    adbLockDown.setTitle("VFresho.");
                                                    // adbLockDown.setMessage("WE HAVE CLOSED OUR SERVICES TILL LOCK-DOWN. \n\nSORRY FOR THE INCONVENIENCE.");
                                                    adbLockDown.setMessage(appCloseMsg);
                                                    adbLockDown.setCancelable(false);
                                                    adbLockDown.setPositiveButton(appCloseMsgBtn1, new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            onBackPressed();
                                                        }
                                                    });
                                                    adbLockDown.show();

                                                }
                                            }
                                        }, 800);
                                    } else {
                                        AlertDialog.Builder adb = new AlertDialog.Builder(SplashScreen.this);
                                        adb.setTitle("Update VFresho.");
                                        adb.setMessage("The new version of VFresho App is available now. To use VFresho please update the app from PlayStore. \n\nThank you.");
                                        adb.setPositiveButton("Update App", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID));
                                                startActivity(intent);
                                            }
                                        });
                                        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                finish();
                                            }
                                        });
                                        adb.show();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(SplashScreen.this, "Server Response Error.", Toast.LENGTH_SHORT).show();
                            // call self activity again.
                            //
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            return null;//super.getParams();
                        }
                    };
                    Volley.newRequestQueue(SplashScreen.this).add(requestToCheckVersion);

                    //////////////////////////////////////////////////////
                }

                if (result == 0) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "No Internet Connection...", Snackbar.LENGTH_INDEFINITE);
                    snackbar.setAction("Reload.", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                            startActivity(getIntent());
                        }
                    });
                    snackbar.show();
                }
            } else {
                progressBar.setVisibility(View.INVISIBLE);
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "No Internet Connection available...", Snackbar.LENGTH_INDEFINITE);

                snackbar.setAction("Reload.", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                        startActivity(getIntent());
                    }
                });
                snackbar.show();
            }
            super.onPostExecute(result);
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        finish();
        System.exit(0);
    }
}
