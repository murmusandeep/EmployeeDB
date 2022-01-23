package com.sandeep.employeedb;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EmployeeAdapter extends FirebaseRecyclerAdapter<EmployeeModel, EmployeeAdapter.EmployeeViewHolder> {

    public EmployeeAdapter(@NonNull FirebaseRecyclerOptions<EmployeeModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull EmployeeViewHolder holder, final int position, @NonNull EmployeeModel model) {

        holder.name.setText(model.getName());
        holder.email.setText(model.getEmail());
        holder.contact.setText(model.getContact());

        holder.edit.setOnClickListener(view -> {

            final DialogPlus dialogPlus = DialogPlus.newDialog(holder.contact.getContext())
                    .setContentHolder(new ViewHolder(R.layout.employee_update))
                    .setExpanded(true,1200)
                    .create();

            //dialogPlus.show();

            View view1 = dialogPlus.getHolderView();

            EditText name = view1.findViewById(R.id.name);
            EditText email = view1.findViewById(R.id.email);
            EditText contact = view1.findViewById(R.id.contact);

            Button update = view1.findViewById(R.id.update);

            name.setText(model.getName());
            email.setText(model.getEmail());
            contact.setText(model.getContact());


            dialogPlus.show();

            update.setOnClickListener(view2 -> {

                Map<String, Object> map = new HashMap<>();

                map.put("name", name.getText().toString());
                map.put("email", email.getText().toString());
                map.put("contact", contact.getText().toString());

                FirebaseDatabase.getInstance().getReference().child("employees")
                        .child(getRef(position).getKey()).updateChildren(map)
                        .addOnSuccessListener(unused -> {

                            Toast.makeText(holder.name.getContext(), "Data Updated Successfully", Toast.LENGTH_SHORT).show();
                            dialogPlus.dismiss();

                        })
                        .addOnFailureListener(e -> {


                            Toast.makeText(holder.name.getContext(), "Error While Updating", Toast.LENGTH_SHORT).show();
                            dialogPlus.dismiss();

                        });

            });

        });


        holder.delete.setOnClickListener(view -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(holder.name.getContext());

            builder.setTitle("Are You Sure?");
            builder.setMessage("Deleted Data cannot be UNDO.");

            builder.setPositiveButton("Delete", (dialogInterface, i) -> FirebaseDatabase.getInstance().getReference().child("employees")
                    .child(Objects.requireNonNull(getRef(position).getKey())).removeValue());

            builder.setNegativeButton("Cancel", (dialogInterface, i) -> Toast.makeText(holder.name.getContext(), "Cancel", Toast.LENGTH_SHORT).show());

            builder.show();

        });
    }

    @NonNull
    @Override
    public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_item, parent, false);

        return new EmployeeViewHolder(view);
    }

    static class EmployeeViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        TextView email;
        TextView contact;

        Button edit;
        Button delete;

        public EmployeeViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.employeeName);
            email = itemView.findViewById(R.id.employeeEmail);
            contact = itemView.findViewById(R.id.employeeContact);

            edit = itemView.findViewById(R.id.edit);
            delete = itemView.findViewById(R.id.delete);
        }
    }
}
