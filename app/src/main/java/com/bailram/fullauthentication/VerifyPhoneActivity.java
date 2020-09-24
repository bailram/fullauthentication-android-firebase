package com.bailram.fullauthentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerifyPhoneActivity extends AppCompatActivity {
    private EditText otpNumberOne, otpNumberTwo, otpNumberThree, otpNumberFour, otpNumberFive, otpNumberSix;
    private Button verifyPhone, resendOTP;
    private Boolean otpValid = true;
    private FirebaseAuth firebaseAuth;
    private PhoneAuthCredential phoneAuthCredential;
    private PhoneAuthProvider.ForceResendingToken token;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;
    private String verificationId;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);

        // get intent data
        Intent data = getIntent();
        phone = data.getStringExtra("phone");

        // instance firebase
        firebaseAuth = FirebaseAuth.getInstance();

        // initialized attribut
        otpNumberOne = findViewById(R.id.otpNumberOne);
        otpNumberTwo = findViewById(R.id.optNumberTwo);
        otpNumberThree = findViewById(R.id.otpNumberThree);
        otpNumberFour = findViewById(R.id.otpNumberFour);
        otpNumberFive = findViewById(R.id.otpNumberFive);
        otpNumberSix = findViewById(R.id.optNumberSix);

        verifyPhone = findViewById(R.id.verifyPhoneBTn);
        resendOTP = findViewById(R.id.resendOTP);

        verifyPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // validate Field
                validateField(otpNumberOne);
                validateField(otpNumberTwo);
                validateField(otpNumberThree);
                validateField(otpNumberFour);
                validateField(otpNumberFive);
                validateField(otpNumberSix);

                if(otpValid){
                    // send otp to the user
                    String otp = otpNumberOne.getText().toString()+otpNumberTwo.getText().toString()+otpNumberThree.getText().toString()+otpNumberFour.getText().toString()+
                            otpNumberFive.getText().toString()+otpNumberSix.getText().toString();
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId,otp);

                    verifyAuthentication(credential);
                }
            }
        });

        mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationId = s;
                token = forceResendingToken;
                resendOTP.setVisibility(View.GONE); // when code send resend, hide button resend
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(String s) {
                super.onCodeAutoRetrievalTimeOut(s);
                resendOTP.setVisibility(View.VISIBLE); // when code time out, show button resend
            }

            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                verifyAuthentication(phoneAuthCredential);
                resendOTP.setVisibility(View.GONE);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(getApplicationContext(), "OTP Verification Failed", Toast.LENGTH_SHORT).show();
            }
        };

        sendOTP(phone);

        resendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resendOTP(phone);
            }
        });
    }

    private void verifyAuthentication(PhoneAuthCredential credential) {
        firebaseAuth.getCurrentUser().linkWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(getApplicationContext(), "Account Created and Linked.", Toast.LENGTH_SHORT).show();
                // send to dashboard
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void sendOTP(String phoneNumber){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber,60, TimeUnit.SECONDS,this,mCallBacks);
    }

    private void resendOTP(String phoneNumber){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber,60, TimeUnit.SECONDS,this,mCallBacks,token);
    }

    private void validateField(EditText field) {
        if(field.getText().toString().isEmpty()){
            otpValid = false;
            field.setError("Required");
        }else{
            otpValid = true;
        }
    }
}