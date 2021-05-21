package com.aditya.passwordmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PinActivity extends AppCompatActivity {

    ArrayList<ImageView> dots;
    private static final int MAX_LENGHT = 4;
    private String codeString = "";
    private String confirm_codeString = "";

    private FirebaseAuth mAuth;
    private DatabaseReference mRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);

        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference().child("Passcode");


        Button zero = findViewById(R.id.btn0);
        Button one = findViewById(R.id.btn1);
        Button two = findViewById(R.id.btn2);
        Button three = findViewById(R.id.btn3);
        Button four = findViewById(R.id.btn4);
        Button five = findViewById(R.id.btn5);
        Button six = findViewById(R.id.btn6);
        Button seven = findViewById(R.id.btn7);
        Button eight = findViewById(R.id.btn8);
        Button nine = findViewById(R.id.btn9);
        ImageView back = findViewById(R.id.btn_clear);
        TextView title = findViewById(R.id.title);
        title.setText("ENTER PIN");

        ImageView dot1 = findViewById(R.id.dot_1);
        ImageView dot2 = findViewById(R.id.dot_2);
        ImageView dot3 = findViewById(R.id.dot_3);
        ImageView dot4 = findViewById(R.id.dot_4);

        SharedPreferences pref = getSharedPreferences("password_manager",MODE_PRIVATE);
        codeString = pref.getString("pin","");



        dots = new ArrayList<>();
        dots.add(dot1);
        dots.add(dot2);
        dots.add(dot3);
        dots.add(dot4);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (confirm_codeString.length() > 0) {
                    confirm_codeString = removeLastChar(confirm_codeString);
                    setConfirmDotImagesState();
                }
            }
        });


        zero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirm_codeString += "0";
                if (confirm_codeString.length() == MAX_LENGHT) {
                    if (codeString.equals(confirm_codeString)) {
                        setConfirmDotImagesState();
                        SharedPreferences.Editor editor = getSharedPreferences("password_manager", MODE_PRIVATE).edit();
                        editor.putBoolean("isActivePin", true);
                        editor.apply();
                        sendToList();
                        Toast.makeText(PinActivity.this, "Successfully!", Toast.LENGTH_SHORT).show();
                    }else{
                        confirm_codeString="";
                        Toast.makeText(PinActivity.this, "Pin does not match", Toast.LENGTH_SHORT).show();
                        setConfirmDotImagesState();
                    }
                } else {
                    setConfirmDotImagesState();
                }
            }
        });
        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirm_codeString += "1";
                Log.d("Adi", "onClick: "+codeString);
                if (confirm_codeString.length() == MAX_LENGHT) {
                    if (codeString.equals(confirm_codeString)) {
                        setConfirmDotImagesState();
                        SharedPreferences.Editor editor = getSharedPreferences("password_manager", MODE_PRIVATE).edit();
                        editor.putBoolean("isActivePin", true);
                        editor.apply();
                        sendToList();
                        Toast.makeText(PinActivity.this, "Successfully!", Toast.LENGTH_SHORT).show();
                    }else{
                        confirm_codeString="";
                        Toast.makeText(PinActivity.this, "Pin does not match", Toast.LENGTH_SHORT).show();
                        setConfirmDotImagesState();
                    }
                } else {
                    setConfirmDotImagesState();
                }
            }
        });
        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirm_codeString += "2";
                if (confirm_codeString.length() == MAX_LENGHT) {
                    if (codeString.equals(confirm_codeString)) {
                        setConfirmDotImagesState();
                        SharedPreferences.Editor editor = getSharedPreferences("password_manager", MODE_PRIVATE).edit();
                        editor.putBoolean("isActivePin", true);
                        editor.apply();
                        sendToList();
                        Toast.makeText(PinActivity.this, "Successfully!", Toast.LENGTH_SHORT).show();
                    }else{
                        confirm_codeString="";
                        Toast.makeText(PinActivity.this, "Pin does not match", Toast.LENGTH_SHORT).show();
                        setConfirmDotImagesState();
                    }
                } else {
                    setConfirmDotImagesState();
                }
            }
        });
        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirm_codeString += "3";
                if (confirm_codeString.length() == MAX_LENGHT) {
                    if (codeString.equals(confirm_codeString)) {
                        setConfirmDotImagesState();
                        SharedPreferences.Editor editor = getSharedPreferences("password_manager", MODE_PRIVATE).edit();
                        editor.putBoolean("isActivePin", true);
                        editor.apply();
                        sendToList();
                        Toast.makeText(PinActivity.this, "Successfully!", Toast.LENGTH_SHORT).show();
                    }else{
                        confirm_codeString="";
                        Toast.makeText(PinActivity.this, "Pin does not match", Toast.LENGTH_SHORT).show();
                        setConfirmDotImagesState();
                    }
                } else {
                    setConfirmDotImagesState();
                }
            }
        });
        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirm_codeString += "4";
                if (confirm_codeString.length() == MAX_LENGHT) {
                    if (codeString.equals(confirm_codeString)) {
                        setConfirmDotImagesState();
                        SharedPreferences.Editor editor = getSharedPreferences("password_manager", MODE_PRIVATE).edit();
                        editor.putBoolean("isActivePin", true);
                        editor.apply();
                        sendToList();
                        Toast.makeText(PinActivity.this, "Successfully!", Toast.LENGTH_SHORT).show();
                    }else{
                        confirm_codeString="";
                        Toast.makeText(PinActivity.this, "Pin does not match", Toast.LENGTH_SHORT).show();
                        setConfirmDotImagesState();
                    }
                } else {
                    setConfirmDotImagesState();
                }
            }
        });
        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirm_codeString += "5";
                if (confirm_codeString.length() == MAX_LENGHT) {
                    if (codeString.equals(confirm_codeString)) {
                        setConfirmDotImagesState();
                        SharedPreferences.Editor editor = getSharedPreferences("password_manager", MODE_PRIVATE).edit();
                        editor.putBoolean("isActivePin", true);
                        editor.apply();
                        sendToList();
                        Toast.makeText(PinActivity.this, "Successfully!", Toast.LENGTH_SHORT).show();
                    }else{
                        confirm_codeString="";
                        Toast.makeText(PinActivity.this, "Pin does not match", Toast.LENGTH_SHORT).show();
                        setConfirmDotImagesState();
                    }
                } else {
                    setConfirmDotImagesState();
                }
            }
        });
        six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirm_codeString += "6";
                if (confirm_codeString.length() == MAX_LENGHT) {
                    if (codeString.equals(confirm_codeString)) {
                        setConfirmDotImagesState();
                        SharedPreferences.Editor editor = getSharedPreferences("password_manager", MODE_PRIVATE).edit();
                        editor.putBoolean("isActivePin", true);
                        editor.apply();
                        sendToList();
                        Toast.makeText(PinActivity.this, "Successfully!", Toast.LENGTH_SHORT).show();
                    }else{
                        confirm_codeString="";
                        Toast.makeText(PinActivity.this, "Pin does not match", Toast.LENGTH_SHORT).show();
                        setConfirmDotImagesState();
                    }
                } else {
                    setConfirmDotImagesState();
                }
            }
        });
        seven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirm_codeString += "7";
                if (confirm_codeString.length() == MAX_LENGHT) {
                    if (codeString.equals(confirm_codeString)) {
                        setConfirmDotImagesState();
                        SharedPreferences.Editor editor = getSharedPreferences("password_manager", MODE_PRIVATE).edit();
                        editor.putBoolean("isActivePin", true);
                        editor.apply();
                        sendToList();
                        Toast.makeText(PinActivity.this, "Successfully!", Toast.LENGTH_SHORT).show();
                    }else{
                        confirm_codeString="";
                        Toast.makeText(PinActivity.this, "Pin does not match", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    setConfirmDotImagesState();
                }
            }
        });
        eight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirm_codeString += "8";
                if (confirm_codeString.length() == MAX_LENGHT) {
                    if (codeString.equals(confirm_codeString)) {
                        setConfirmDotImagesState();
                        SharedPreferences.Editor editor = getSharedPreferences("password_manager", MODE_PRIVATE).edit();
                        editor.putBoolean("isActivePin", true);
                        editor.apply();
                        sendToList();
                        Toast.makeText(PinActivity.this, "Successfully!", Toast.LENGTH_SHORT).show();
                    }else{
                        confirm_codeString="";
                        Toast.makeText(PinActivity.this, "Pin does not match", Toast.LENGTH_SHORT).show();
                        setConfirmDotImagesState();
                    }
                } else {
                    setConfirmDotImagesState();
                }
            }
        });
        nine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirm_codeString += "9";
                if (confirm_codeString.length() == MAX_LENGHT) {
                    if (codeString.equals(confirm_codeString)) {
                        setConfirmDotImagesState();
                        SharedPreferences.Editor editor = getSharedPreferences("password_manager", MODE_PRIVATE).edit();
                        editor.putBoolean("isActivePin", true);
                        editor.apply();
                        sendToList();
                        Toast.makeText(PinActivity.this, "Successfully!", Toast.LENGTH_SHORT).show();
                    }else{
                        confirm_codeString="";
                        Toast.makeText(PinActivity.this, "Pin does not match", Toast.LENGTH_SHORT).show();
                        setConfirmDotImagesState();
                    }
                } else {
                    setConfirmDotImagesState();
                }
            }
        });
    }

    private String removeLastChar(String s) {
        if (s == null || s.length() == 0) {
            return s;
        }
        return s.substring(0, s.length() - 1);
    }

    private void setConfirmDotImagesState() {
        for (int i = 0; i < confirm_codeString.length(); i++) {
            dots.get(i).setImageResource(R.drawable.dot_enable);
        }
        if (confirm_codeString.length()<4) {
            for (int j = confirm_codeString.length(); j<4; j++) {
                dots.get(j).setImageResource(R.drawable.dot_disable);
            }
        }
    }


    private void sendToList() {
        Intent mainIntent = new Intent(PinActivity.this,MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }


}