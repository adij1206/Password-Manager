package com.aditya.passwordmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dorianmusaj.cryptolight.CryptoLight;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference mUser,childUser;

    private String currentUserId;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseDatabase.getInstance().getReference().child("Holder");

        if(mAuth.getCurrentUser()!=null) {
            currentUserId = mAuth.getCurrentUser().getUid();
        }

        CryptoLight.init(this);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPopup();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.sign_out) {
            mAuth.signOut();
            sendToLogin();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        //String id = currentUserId;
        if(currentUser==null){
            sendToLogin();
        }
        else{
            mUser.child(currentUserId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        fab.setVisibility(View.GONE);
                        if (!isActivePass()) {
                            Intent intent = new Intent(MainActivity.this, PinActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            sendToList();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void sendToLogin() {
        Intent loginIntent = new Intent(MainActivity.this,LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }
    private void createPopup() {
        builder = new AlertDialog.Builder(this);
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
                Toast.makeText(MainActivity.this,"save Pressed",Toast.LENGTH_SHORT).show();
                String n = name.getText().toString();
                String i = userId.getText().toString();
                String p = password.getText().toString();
                saveToDb(n,i,p);
                dialog.dismiss();
                fab.setVisibility(View.GONE);
                sendToList();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences.Editor editor = getSharedPreferences("password_manager", MODE_PRIVATE).edit();
        editor.putBoolean("isActivePin", false);
        editor.apply();

    }

    private boolean isPass() {
        SharedPreferences prefs = getSharedPreferences("password_manager", MODE_PRIVATE);
        return prefs.getBoolean("isPin", false);
    }

    private boolean isActivePass() {
        SharedPreferences prefs = getSharedPreferences("password_manager", MODE_PRIVATE);
        return prefs.getBoolean("isActivePin", false);
    }


    private void sendToList() {
        Fragment fragment = null;
        fragment = new ListFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frameLayout,fragment).commitAllowingStateLoss();
    }

    private void saveToDb(String name,String userId,String password) {

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDateFormat = new SimpleDateFormat("MMM dd, yyyy");
        String currentDate = currentDateFormat.format(calForDate.getTime());
        String encyptPassword = CryptoLight.encrypt(this,password);
        childUser = mUser.child(currentUserId).child(name);

        HashMap<String, Object> messageInfoMap = new HashMap<>();
        messageInfoMap.put("name",name);
        messageInfoMap.put("userId",userId);
        messageInfoMap.put("password",encyptPassword);
        messageInfoMap.put("date",currentDate);

        childUser.updateChildren(messageInfoMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Added SuccessFully", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}