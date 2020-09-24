package com.bailram.fullauthentication.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bailram.fullauthentication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignupFragment extends Fragment {
    private EditText fullname, email, password, retypePassword, countryCode, phone;
    private Button button;
    private boolean isDataValid = false;
    private FirebaseAuth fAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        // initialized attribut
        fullname = view.findViewById(R.id.editTextFullName);
        email = view.findViewById(R.id.editTextEmail);
        password = view.findViewById(R.id.editTextPassword);
        retypePassword = view.findViewById(R.id.editTextRetypePassword);
        countryCode = view.findViewById(R.id.countryCode);
        phone = view.findViewById(R.id.editTextPhone);
        button = view.findViewById(R.id.buttonRegister);

        // instance firebase
        fAuth = FirebaseAuth.getInstance();

        // validate data
        validateData(fullname);
        validateData(email);
        validateData(password);
        validateData(retypePassword);
        validateData(countryCode);
        validateData(phone);

        // validate password
        if(!password.getText().toString().equals(retypePassword.getText().toString())){
            isDataValid = false;
            retypePassword.setError("Password Do not Match");
        }else{
            isDataValid = true;
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isDataValid){
                    // proceed with the registration of the user
                    fAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Toast.makeText(getActivity(), "User Account is Created.", Toast.LENGTH_SHORT).show();
                            // send the user to verify the phone
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Error ! "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        return view;
    }

    private void validateData(EditText field) {
        if(field.getText().toString().isEmpty()){
            isDataValid = false;
            field.setError("Required Field.");
        }else{
            isDataValid = true;
        }
    }
}