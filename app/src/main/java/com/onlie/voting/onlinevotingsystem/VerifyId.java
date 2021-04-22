package com.onlie.voting.onlinevotingsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class VerifyId extends AppCompatActivity {

    String Phone;
    EditText Id;
    Button IdButton;
    private DatabaseReference firebaseRef;

    String myId, myAge, year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_age);

        Intent i = getIntent();
        Phone = i.getStringExtra("phone");

        Id = (EditText) findViewById(R.id.idproof);
        IdButton = (Button) findViewById(R.id.verifyagebutton);
        firebaseRef = FirebaseDatabase.getInstance().getReference();

        IdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(Id.getText().toString())) {
                    Toast.makeText(VerifyId.this, "Please Enter you id..", Toast.LENGTH_LONG).show();
                } else {
                    final String idStr = Id.getText().toString();
                    firebaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if ((!(snapshot.child("Users").child(Phone).child("ID").exists())) || snapshot.child("Users").child(Phone).child("Vote").getValue().toString().equals("0")) {
                                Toast.makeText(VerifyId.this, "AddIdToAccount", Toast.LENGTH_SHORT).show();
                                AddIdToAccount(Phone, idStr);
                            } else {
                                Toast.makeText(VerifyId.this, "Invalid ID", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
        });

    }

    private void AddIdToAccount(final String phone, String ID) {

        firebaseRef.child("Users").child(phone).child("ID").setValue(ID).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent intent = new Intent(VerifyId.this, CameraActivity.class);
                intent.putExtra("phone", phone);
                startActivity(intent);
            }
        });

    }
}