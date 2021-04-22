package com.onlie.voting.onlinevotingsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onlie.voting.onlinevotingsystem.Model.Users;
import com.onlie.voting.onlinevotingsystem.Prevalent.Prevalent;

public class LoginActivity extends AppCompatActivity {

    private EditText Phone, Password;
    private DatabaseReference firebaseRef;
    private ProgressDialog LoadingBar;
    String myPhone, myPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        Phone = findViewById(R.id.loginphone);
        Password = findViewById(R.id.loginpassword);
        Button login = findViewById(R.id.loginbutton);
        TextView newUser = findViewById(R.id.Newuser);
        TextView forgotPasswordTV = findViewById(R.id.forgotPasswordTV);

        LoadingBar = new ProgressDialog(this);
        firebaseRef = FirebaseDatabase.getInstance().getReference();
        myPassword = Password.getText().toString();
        myPhone = "+91" + Phone.getText().toString();

        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });

        forgotPasswordTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, UserUpdatePassword.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(Phone.getText().toString())) {
                    Toast.makeText(LoginActivity.this, "Please enter your phone number..", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(Password.getText().toString())) {
                    Toast.makeText(LoginActivity.this, "Please enter your password...", Toast.LENGTH_SHORT).show();
                } else if (Phone.getText().toString().length() < 10) {
                    Toast.makeText(LoginActivity.this, "Please enter correct phone number..", Toast.LENGTH_SHORT).show();
                } else {
                    LoginUser();
                }
            }
        });
    }

    private void LoginUser() {


        LoadingBar.setTitle("Login Account");
        LoadingBar.setMessage("Please wait while we are checking our credentials..");
        LoadingBar.setCanceledOnTouchOutside(false);
        LoadingBar.show();

        AllowAccessToUser(myPhone, myPassword);
    }

    private void AllowAccessToUser(final String myphone, final String mypassword) {

        firebaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child("Users").child("+91" + Phone.getText().toString()).exists()) {
                    final Users userdata = dataSnapshot.child("Users").child("+91" + Phone.getText().toString()).getValue(Users.class);
                    if (userdata.getPhone().equals("+91" + Phone.getText().toString())) {

                        if (userdata.getPassword().equals(Password.getText().toString())) {

                            LoadingBar.dismiss();
                            Toast.makeText(LoginActivity.this, "Logged in Successfully..", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                            Prevalent.currentOnlineUser = userdata;
                            i.putExtra("phone", "+91" + Phone.getText().toString());
                            startActivity(i);
                        } else {
                            LoadingBar.dismiss();
                            Toast.makeText(LoginActivity.this, "please enter correct password..", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    LoadingBar.dismiss();
                    Toast.makeText(LoginActivity.this, "please create your account first with this number ..", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
