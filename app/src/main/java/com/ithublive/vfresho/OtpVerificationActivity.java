package com.ithublive.vfresho;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ithublive.vfresho.mumbai_package.MumbaiHomeActivity;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class OtpVerificationActivity extends AppCompatActivity {

    ProgressBar progressBar;
    EditText editTextOtp;
    FirebaseAuth mAuth;
    private String verificationID;
    SharedPreferences sharedPreferences;
    String mobile, email, location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        FirebaseApp.initializeApp(getApplicationContext());
        progressBar = findViewById(R.id.progressbar);
        editTextOtp = findViewById(R.id.edittext_otp);

        sharedPreferences = getSharedPreferences("vfresho_spsp", MODE_PRIVATE);
        mobile = getIntent().getStringExtra("mobile");
        email = getIntent().getStringExtra("emailid");
        location = getIntent().getStringExtra("location");

        mAuth = FirebaseAuth.getInstance();
        sendVerificationCode("+91" + mobile);
        findViewById(R.id.buttonOTPVerify).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = editTextOtp.getText().toString().trim();
                if (code.isEmpty() || code.length() < 6) {
                    editTextOtp.setError("Enter Code");
                    editTextOtp.requestFocus();
                    return;
                }
                verifyCodeByButton(code);
            }
        });

    }

    private void verifyCodeByButton(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID, code);
        signInWithCreditiancial(credential);
    }

    private void signInWithCreditiancial(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                             editor.putString("mobile", mobile);
                             editor.putString("emailid", email);
                             editor.putString("location", location);
                               // editor.apply();
                            editor.commit();
                            Toast.makeText(OtpVerificationActivity.this, "Welcome  to VFresho", Toast.LENGTH_SHORT).show();
                            // FirebaseMessaging.getInstance().subscribeToTopic("donor_orphanage");
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference("loggedUser");
                            String key=    myRef.push().getKey();
                            Map user= new HashMap();
                            user.put("mobile",mobile);
                            user.put("email",email);
                            user.put("location",location);
                            user.put("time", Calendar.getInstance().getTimeInMillis());
                            myRef.child(key).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                }
                            });


                            if (location.equals("Vasai-Virar")) {
                                Intent intent = new Intent(OtpVerificationActivity.this, UserHomeActivity.class);
                                startActivity(intent);
                            } else if (!location.equals("Vasai-Virar")) { //location.equals("Andheri") || location.equals("Borivali") || location.equals("Kandivali")
                                Intent intent = new Intent(OtpVerificationActivity.this, MumbaiHomeActivity.class);
                                startActivity(intent);
                            }
                        } else {
                            Toast.makeText(OtpVerificationActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void sendVerificationCode(String number) {
        progressBar.setVisibility(View.VISIBLE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(number, 60, TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD

                , mCallBack);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationID = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                editTextOtp.setText(code);
                verifyCodeByButton(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(OtpVerificationActivity.this, "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };
}
