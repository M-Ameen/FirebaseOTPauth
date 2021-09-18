package com.example.firebaseotpauth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Login extends AppCompatActivity {

    EditText et_getotp;
    Button bt_verify;
    String otpid;
    String phonenumber;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        phonenumber=getIntent().getStringExtra("mobile").toString();
        et_getotp=findViewById(R.id.et_getotp);
        bt_verify=findViewById(R.id.bt_verify);

        mAuth=FirebaseAuth.getInstance();

        defaultinitiateotp();
        bt_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_getotp.getText().toString().isEmpty()){
                    Toast.makeText(Login.this, "Field is Empty", Toast.LENGTH_SHORT).show();
                }
                else{
                    PhoneAuthCredential credential=PhoneAuthProvider.getCredential(otpid,et_getotp.getText().toString());
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });


    }

    private void defaultinitiateotp(){
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phonenumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                otpid=s;
                            }

                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                signInWithPhoneAuthCredential(phoneAuthCredential);
                                finish();
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(Login.this,Dashboard.class));
                        } else {
                            Toast.makeText(getApplicationContext(),"Sign in code error",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}