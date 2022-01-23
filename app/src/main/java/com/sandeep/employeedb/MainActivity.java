package com.sandeep.employeedb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class MainActivity extends AppCompatActivity {

    private EmployeeAdapter employeeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);

        RecyclerView mRecyclerView = findViewById(R.id.recyclerview);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(null);

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("employees");

        FirebaseRecyclerOptions<EmployeeModel> options =
                new FirebaseRecyclerOptions.Builder<EmployeeModel>()
                        .setQuery(query, EmployeeModel.class)
                        .build();

        employeeAdapter = new EmployeeAdapter(options);
        mRecyclerView.setAdapter(employeeAdapter);

        floatingActionButton.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), AddActivity.class)));
    }

    @Override
    protected void onStart() {
        super.onStart();

        employeeAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();

        employeeAdapter.stopListening();
    }
}