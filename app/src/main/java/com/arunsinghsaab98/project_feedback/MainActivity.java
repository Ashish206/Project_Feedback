package com.arunsinghsaab98.project_feedback;

import android.content.DialogInterface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

//    String codeSent;
//
    EditText login_email,login_password;
    Switch switchAdminCustomer;

//    TextView reset_password;
    Button btn_login,btn_signUP;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login_email = findViewById(R.id.email);
        login_password = findViewById(R.id.login_password);

//        reset_password = findViewById(R.id.reset_pass);
//        btn_otp = findViewById(R.id.otp);
        switchAdminCustomer = findViewById(R.id.switchAdminCustomer);
        btn_login = findViewById(R.id.btn_login);
        btn_signUP = findViewById(R.id.btn_signup);

        mAuth = FirebaseAuth.getInstance();

        login_email.requestFocus();

//        btn_otp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                sendVerificationCode(); phone wale me use hua tha
//            }
//        });
        switchAdminCustomer.setChecked(false);

        switchAdminCustomer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    Intent intent = new Intent(getApplicationContext(),SignInAdmin.class);
                    startActivity(intent);
                }
            }
        });

        btn_signUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(),SignUp.class);
                startActivity(intent);

            }
        });


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = login_email.getText().toString();
                String password = login_password.getText().toString();
                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(MainActivity.this, "YOu are logedin", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(),Comp_Act.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
//                verifySignInCode(); phone wale me use hua tha

            }
        });



    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
//        pass user to next activity
    }



//    private void verifySignInCode()
//    {
//        String code = user_password.getText().toString();
//        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent,code);
//        signInWithPhoneAuthCredential(credential);
//    }
//
//    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//
//                            Toast.makeText(MainActivity.this, "Successful", Toast.LENGTH_SHORT).show();
////                            FirebaseUser user = task.getResult().getUser();
//                            // ...
//                        } else {
//                            // Sign in failed, display a message and update the UI
//
//                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
//                                // The verification code entered was invalid
//
//                                Toast.makeText(MainActivity.this, "Invalid code" , Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    }
//                });
//    }
//
//    private void sendVerificationCode() {
//
//        String phone =  "+91"+user_mobile.getText().toString();
//
//        if (phone.isEmpty())
//        {
//            user_mobile.setError("Phone number is required");
//            user_mobile.requestFocus();
//            return;
//        }
//        else if (phone.length() != 13)
//        {
//            user_mobile.setError("10 digit phone number is required");
//            user_mobile.requestFocus();
//        }
//
//        PhoneAuthProvider.getInstance().verifyPhoneNumber(
//                phone,        // Phone number to verify
//                60,                 // Timeout duration
//                TimeUnit.SECONDS,   // Unit of timeout
//                this,               // Activity (for callback binding)
//                mCallbacks);        // OnVerificationStateChangedCallbacksPhoneAuthActivity.java
//
//    }
//
//    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//        @Override
//        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
//
//        }
//
//        @Override
//        public void onVerificationFailed(@NonNull FirebaseException e) {
//
//        }
//
//        @Override
//        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//            super.onCodeSent(s, forceResendingToken);
//
//            codeSent = s;
//        }
//    };
}
