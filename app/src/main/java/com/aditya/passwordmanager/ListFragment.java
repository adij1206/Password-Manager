package com.aditya.passwordmanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dorianmusaj.cryptolight.CryptoLight;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

public class ListFragment extends Fragment {

    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef,childUser;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private String currentUserId;

    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_list, container, false);
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();

        userRef = FirebaseDatabase.getInstance().getReference().child("Holder").child(currentUserId);

        CryptoLight.init(getContext());

        recyclerView = view.findViewById(R.id.recyclerViewID);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        floatingActionButton = view.findViewById(R.id.fabBtn);

        FirebaseRecyclerOptions<Saver> options = new FirebaseRecyclerOptions.Builder<Saver>()
                .setQuery(userRef,Saver.class)
                .build();
        FirebaseRecyclerAdapter<Saver,ListViewHolder> adapter =
                new FirebaseRecyclerAdapter<Saver, ListViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ListViewHolder holder, final int position, @NonNull Saver model) {
                       holder.title.setText(model.getName());
                       holder.userId.setText("UserId: " + model.getUserId());
                       holder.password.setText("Password: " + CryptoLight.decrypt(getContext(),model.getPassword()));
                       holder.date.setText("Date: " + model.getDate());

                       holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View view) {
                               userRef.child(model.getName()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                   @Override
                                   public void onComplete(@NonNull Task<Void> task) {
                                       if(task.isSuccessful()){
                                           Toast.makeText(getContext(), "SuccessFully Deleted", Toast.LENGTH_SHORT).show();
                                       }
                                       else{
                                           Toast.makeText(getContext(), "Error:"+task.getException().toString(), Toast.LENGTH_SHORT).show();
                                       }
                                   }
                               });
                           }
                       });

                       holder.editBtn.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View view) {
                               createUpdatePopup(model.getName());
                           }
                       });
                    }

                    @NonNull
                    @Override
                    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row,parent,false);
                        ListViewHolder viewHolder = new ListViewHolder(view);
                        return viewHolder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPopup();
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        SharedPreferences.Editor editor = getActivity().getSharedPreferences("password_manager", MODE_PRIVATE).edit();
        editor.putBoolean("isActivePin", false);
        editor.apply();
    }

    private void createPopup() {

        builder = new AlertDialog.Builder(getContext());
        View view = getLayoutInflater().inflate(R.layout.popup,null);

        EditText name = (EditText) view.findViewById(R.id.popup_add_item_name);
        EditText userId = (EditText) view.findViewById(R.id.popup_add_item_userid);
        EditText password = (EditText) view.findViewById(R.id.popup_add_item_password);
        Button saveBtn = (Button) view.findViewById(R.id.popup_add_save_btn);

        builder.setView(view);
        dialog = builder.create();
        dialog.show();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"save Pressed",Toast.LENGTH_SHORT).show();
                String n = name.getText().toString();
                String i = userId.getText().toString();
                String p = password.getText().toString();
                saveToDb(n,i,p);
                dialog.dismiss();
            }
        });
    }

    private void createUpdatePopup(String title){
        builder = new AlertDialog.Builder(getContext());
        View view = getLayoutInflater().inflate(R.layout.update_popup,null);

        TextView name = (TextView) view.findViewById(R.id.update_popup_add_item_name);
        EditText userId = (EditText) view.findViewById(R.id.update_popup_add_item_userid);
        EditText password = (EditText) view.findViewById(R.id.update_popup_add_item_password);
        Button saveBtn = (Button) view.findViewById(R.id.update_popup_add_save_btn);
        name.setText(title);
        builder.setView(view);
        dialog = builder.create();
        dialog.show();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"save Pressed",Toast.LENGTH_SHORT).show();
                //String n = name.getText().toString();
                String i = userId.getText().toString();
                String p = password.getText().toString();
                updateToDb(i,p,title);
                dialog.dismiss();
            }
        });
    }

    private void updateToDb( String userId, String password,String title) {
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDateFormat = new SimpleDateFormat("MMM dd, yyyy");
        String currentDate = currentDateFormat.format(calForDate.getTime());
        String encryptPassword = CryptoLight.encrypt(getContext(),password);
        childUser = userRef.child(title);

        HashMap<String, Object> messageInfoMap = new HashMap<>();
        messageInfoMap.put("name",title);
        messageInfoMap.put("userId",userId);
        messageInfoMap.put("password",encryptPassword);
        messageInfoMap.put("date",currentDate);

        childUser.updateChildren(messageInfoMap);
    }

    private void saveToDb(String name, String userId, String password) {
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDateFormat = new SimpleDateFormat("MMM dd, yyyy");
        String currentDate = currentDateFormat.format(calForDate.getTime());
        String encryptPassword = CryptoLight.encrypt(getContext(),password);
        childUser = userRef.child(name);

        HashMap<String, Object> messageInfoMap = new HashMap<>();
        messageInfoMap.put("name",name);
        messageInfoMap.put("userId",userId);
        messageInfoMap.put("password",encryptPassword);
        messageInfoMap.put("date",currentDate);

        childUser.updateChildren(messageInfoMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //Toast.makeText(getContext(), "Added SuccessFully", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    public class ListViewHolder extends RecyclerView.ViewHolder {
        TextView title,userId,password,date;
        Button editBtn,deleteBtn;
        public ListViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.list_title);
            userId = itemView.findViewById(R.id.list_user_id);
            password = itemView.findViewById(R.id.list_password);
            date = itemView.findViewById(R.id.list_dateAdd);
            editBtn = itemView.findViewById(R.id.editButton);
            deleteBtn = itemView.findViewById(R.id.deleteButton);

        }
    }
}