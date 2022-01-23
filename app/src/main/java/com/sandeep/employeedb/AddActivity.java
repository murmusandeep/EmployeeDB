package com.sandeep.employeedb;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AddActivity extends AppCompatActivity {

    private EditText mName;
    private EditText mEmail;
    private EditText mContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        mName = findViewById(R.id.addName);
        mEmail = findViewById(R.id.addEmail);
        mContact = findViewById(R.id.addContact);

        Button mSave = findViewById(R.id.save);
        Button mBack = findViewById(R.id.back);

        mSave.setOnClickListener(view -> {

            insertData();
            clearAllData();
        });

        mBack.setOnClickListener(view -> finish());

    }

    private void insertData() {

        Map<String, Object> map = new HashMap<>();

        if(!mName.getText().toString().equals("") && !mEmail.getText().toString().equals("") && !mContact.getText().toString().equals("") ) {

            map.put("name", mName.getText().toString());
            map.put("email", mEmail.getText().toString());
            map.put("contact", mContact.getText().toString());

            FirebaseDatabase.getInstance().getReference().child("employees").push()
                    .setValue(map)
                    .addOnSuccessListener(unused -> Toast.makeText(AddActivity.this, "Data Inserted Successfully", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(AddActivity.this, "Error while inserting", Toast.LENGTH_SHORT).show());
        }
        else {
            Toast.makeText(AddActivity.this, "Fields Cannot be Empty", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearAllData() {

        mName.setText("");
        mEmail.setText("");
        mContact.setText("");
        finish();
    }
}