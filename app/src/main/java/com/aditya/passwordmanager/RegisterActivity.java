package com.aditya.passwordmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private EditText userEmail,userPassword;
    private Button userRegisterBtn,setPinBtn;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef,childUser;
    private ProgressDialog loadingBar;

    private static final int MAX_LENGHT = 4;
    private String codeString = "";
    private String confirm_codeString = "";
    ArrayList<ImageView> dots;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference();

        userEmail = (EditText) findViewById(R.id.register_mail);
        userPassword = (EditText) findViewById(R.id.register_password);
        userRegisterBtn = (Button) findViewById(R.id.register_btn);
        setPinBtn = (Button) findViewById(R.id.pin_btn);
        loadingBar = new ProgressDialog(this);

        userRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        if(!Pin()){
            setPinBtn.setVisibility(View.VISIBLE);
            setPinBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getPinLayout();
                }
            });
        }
    }

    private void registerUser() {
        String email = userEmail.getText().toString();
        String password = userPassword.getText().toString();

        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
            loadingBar.setTitle("Creating New Account");
            loadingBar.setMessage("Please wait while we are creating your account...");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();

            mAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                String currentUserid = mAuth.getCurrentUser().getUid();
                                //mRef.child("Holder").child(currentUserid).setValue(" ");

                                Toast.makeText(RegisterActivity.this, "Account is created successfully", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                getPinLayout();
                            }
                            else {
                                String error = task.getException().toString();
                                Toast.makeText(RegisterActivity.this, "Error :"+error, Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                        }
                    });
        }
        else{
            Toast.makeText(this, "Please fill the blank columns", Toast.LENGTH_LONG).show();
        }
    }

    private void sendToMainActivity() {
        Intent mainIntent = new Intent(RegisterActivity.this,MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
    private void getPinLayout() {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.pin_layout, null);
        codeString="";
        Button zero = mView.findViewById(R.id.btn0);
        Button one = mView.findViewById(R.id.btn1);
        Button two = mView.findViewById(R.id.btn2);
        Button three = mView.findViewById(R.id.btn3);
        Button four = mView.findViewById(R.id.btn4);
        Button five = mView.findViewById(R.id.btn5);
        Button six = mView.findViewById(R.id.btn6);
        Button seven = mView.findViewById(R.id.btn7);
        Button eight = mView.findViewById(R.id.btn8);
        Button nine = mView.findViewById(R.id.btn9);
        ImageView back = mView.findViewById(R.id.btn_clear);
        ImageView close = mView.findViewById(R.id.btn_close);
        TextView title = mView.findViewById(R.id.title);
        title.setText("ENTER YOUR NEW PIN");

        ImageView dot1 = mView.findViewById(R.id.dot_1);
        ImageView dot2 = mView.findViewById(R.id.dot_2);
        ImageView dot3 = mView.findViewById(R.id.dot_3);
        ImageView dot4 = mView.findViewById(R.id.dot_4);



        dots = new ArrayList<>();
        dots.add(dot1);
        dots.add(dot2);
        dots.add(dot3);
        dots.add(dot4);


        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (codeString.length() > 0) {
                    //remove last character of code
                    codeString = removeLastChar(codeString);

                    //update dots layout
                    setDotImagesState();
                }
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePrefsPin("not_done");
                codeString="";
                confirm_codeString="";
                savePrefsPinData(false);
                dialog.dismiss();
            }
        });
        zero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                codeString += "0";
                if (codeString.length() == MAX_LENGHT) {
                    getConfirmPinLayout();
                    dialog.dismiss();
                } else {
                    setDotImagesState();
                }
            }
        });
        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                codeString += "1";
                if (codeString.length() == MAX_LENGHT) {
                    setDotImagesState();
                    getConfirmPinLayout();
                    dialog.dismiss();
                } else {
                    setDotImagesState();
                }
            }
        });
        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                codeString += "2";
                if (codeString.length() == MAX_LENGHT) {
                    setDotImagesState();
                    getConfirmPinLayout();
                    dialog.dismiss();
                } else {
                    setDotImagesState();
                }
            }
        });
        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                codeString += "3";
                if (codeString.length() == MAX_LENGHT) {
                    setDotImagesState();
                    getConfirmPinLayout();
                    dialog.dismiss();
                } else {
                    setDotImagesState();
                }
            }
        });
        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                codeString += "4";
                if (codeString.length() == MAX_LENGHT) {
                    setDotImagesState();
                    getConfirmPinLayout();
                    dialog.dismiss();
                } else {
                    setDotImagesState();
                }
            }
        });
        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                codeString += "5";
                if (codeString.length() == MAX_LENGHT) {
                    setDotImagesState();
                    getConfirmPinLayout();
                    dialog.dismiss();
                } else {
                    setDotImagesState();
                }
            }
        });
        six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                codeString += "6";
                if (codeString.length() == MAX_LENGHT) {
                    setDotImagesState();
                    getConfirmPinLayout();
                    dialog.dismiss();
                } else {
                    setDotImagesState();
                }
            }
        });
        seven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                codeString += "7";
                if (codeString.length() == MAX_LENGHT) {
                    setDotImagesState();
                    getConfirmPinLayout();
                    dialog.dismiss();
                } else {
                    setDotImagesState();
                }
            }
        });
        eight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                codeString += "8";
                if (codeString.length() == MAX_LENGHT) {
                    setDotImagesState();
                    getConfirmPinLayout();
                    dialog.dismiss();
                } else {
                    setDotImagesState();
                }
            }
        });
        nine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                codeString += "9";
                if (codeString.length() == MAX_LENGHT) {
                    setDotImagesState();
                    getConfirmPinLayout();
                    dialog.dismiss();
                } else {
                    setDotImagesState();
                }
            }
        });
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

    private boolean Pin() {
        SharedPreferences pref = getSharedPreferences("password_manager",MODE_PRIVATE);
        Boolean isIntroActivityOpnendBefore = pref.getBoolean("isPin",false);
        return  isIntroActivityOpnendBefore;
    }


    private void getConfirmPinLayout() {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.pin_layout, null);

        Button zero = mView.findViewById(R.id.btn0);
        Button one = mView.findViewById(R.id.btn1);
        Button two = mView.findViewById(R.id.btn2);
        Button three = mView.findViewById(R.id.btn3);
        Button four = mView.findViewById(R.id.btn4);
        Button five = mView.findViewById(R.id.btn5);
        Button six = mView.findViewById(R.id.btn6);
        Button seven = mView.findViewById(R.id.btn7);
        Button eight = mView.findViewById(R.id.btn8);
        Button nine = mView.findViewById(R.id.btn9);
        ImageView back = mView.findViewById(R.id.btn_clear);
        ImageView close = mView.findViewById(R.id.btn_close);
        TextView title = mView.findViewById(R.id.title);
        title.setText("CONFIRM PIN");
        confirm_codeString="";

        ImageView dot1 = mView.findViewById(R.id.dot_1);
        ImageView dot2 = mView.findViewById(R.id.dot_2);
        ImageView dot3 = mView.findViewById(R.id.dot_3);
        ImageView dot4 = mView.findViewById(R.id.dot_4);

        dots = new ArrayList<>();
        dots.add(dot1);
        dots.add(dot2);
        dots.add(dot3);
        dots.add(dot4);

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (confirm_codeString.length() > 0) {
                    //remove last character of code
                    confirm_codeString = removeLastChar(confirm_codeString);

                    //update dots layout
                    setConfirmDotImagesState();
                }
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePrefsPin("not_done");
                codeString="";
                confirm_codeString="";
                savePrefsPinData(false);
                dialog.dismiss();
            }
        });
        zero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirm_codeString += "0";
                if (confirm_codeString.length() == MAX_LENGHT) {
                    if(codeString.equals(confirm_codeString)) {
                        savePrefsPin(codeString);
                        savePassword(codeString);
                        setConfirmDotImagesState();
                        sendToMainActivity();
                        putPin(true);
                        Toast.makeText(RegisterActivity.this, "Pin set Successfully!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }else{
                        codeString="";
                        savePrefsPinData(false);
                        dialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "Pin does not match", Toast.LENGTH_SHORT).show();
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
                if (confirm_codeString.length() == MAX_LENGHT) {
                    if (codeString.equals(confirm_codeString)) {
                        savePrefsPin(codeString);
                        savePassword(codeString);
                        sendToMainActivity();
                        putPin(true);
                        setConfirmDotImagesState();
                        Toast.makeText(RegisterActivity.this, "Pin set Successfully!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }else{
                        codeString="";
                        savePrefsPinData(false);
                        dialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "Pin does not match", Toast.LENGTH_SHORT).show();
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
                        savePrefsPin(codeString);
                        savePassword(codeString);
                        setConfirmDotImagesState();
                        sendToMainActivity();
                        putPin(true);
                        Toast.makeText(RegisterActivity.this, "Pin set Successfully!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }else{
                        codeString="";
                        savePrefsPinData(false);
                        dialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "Pin does not match", Toast.LENGTH_SHORT).show();
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
                        savePrefsPin(codeString);
                        savePassword(codeString);
                        setConfirmDotImagesState();
                        sendToMainActivity();
                        putPin(true);
                        Toast.makeText(RegisterActivity.this, "Pin set Successfully!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }else{
                        codeString="";
                        savePrefsPinData(false);
                        dialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "Pin does not match", Toast.LENGTH_SHORT).show();
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
                        savePrefsPin(codeString);
                        savePassword(codeString);
                        setConfirmDotImagesState();
                        sendToMainActivity();
                        putPin(true);
                        Toast.makeText(RegisterActivity.this, "Pin set Successfully!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }else{
                        codeString="";
                        savePrefsPinData(false);
                        dialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "Pin does not match", Toast.LENGTH_SHORT).show();
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
                        savePrefsPin(codeString);
                        savePassword(codeString);
                        setConfirmDotImagesState();
                        sendToMainActivity();
                        putPin(true);
                        Toast.makeText(RegisterActivity.this, "Pin set Successfully!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }else{
                        codeString="";
                        savePrefsPinData(false);
                        dialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "Pin does not match", Toast.LENGTH_SHORT).show();
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
                        savePrefsPin(codeString);
                        savePassword(codeString);
                        setConfirmDotImagesState();
                        sendToMainActivity();
                        putPin(true);
                        Toast.makeText(RegisterActivity.this, "Pin set Successfully!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }else{
                        codeString="";
                        savePrefsPinData(false);
                        dialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "Pin does not match", Toast.LENGTH_SHORT).show();
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
                        savePrefsPin(codeString);
                        savePassword(codeString);
                        setConfirmDotImagesState();
                        sendToMainActivity();
                        putPin(true);
                        Toast.makeText(RegisterActivity.this, "Pin set Successfully!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }else{
                        codeString="";
                        savePrefsPinData(false);
                        dialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "Pin does not match", Toast.LENGTH_SHORT).show();
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
                        savePrefsPin(codeString);
                        savePassword(codeString);
                        setConfirmDotImagesState();
                        sendToMainActivity();
                        putPin(true);
                        Toast.makeText(RegisterActivity.this, "Pin set Successfully!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }else{
                        codeString="";
                        savePrefsPinData(false);
                        dialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "Pin does not match", Toast.LENGTH_SHORT).show();
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
                        savePrefsPin(codeString);
                        savePassword(codeString);
                        setConfirmDotImagesState();
                        sendToMainActivity();
                        putPin(true);
                        Toast.makeText(RegisterActivity.this, "Pin set Successfully!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }else{
                        codeString="";
                        savePrefsPinData(false);
                        dialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "Pin does not match", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    setConfirmDotImagesState();
                }
            }
        });


    }

    private void putPin(boolean b) {
        SharedPreferences.Editor editor = getSharedPreferences("password_manager", MODE_PRIVATE).edit();
        editor.putBoolean("isActivePin", true);
        editor.apply();

    }


    private void setDotImagesState() {
        for (int i = 0; i < codeString.length(); i++) {
            dots.get(i).setImageResource(R.drawable.dot_enable);
        }
        if (codeString.length()<4) {
            for (int j = codeString.length(); j<4; j++) {
                dots.get(j).setImageResource(R.drawable.dot_disable);
            }
        }
    }

    private String removeLastChar(String s) {
        if (s == null || s.length() == 0) {
            return s;
        }
        return s.substring(0, s.length() - 1);
    }


    private void savePrefsPinData(boolean pin) {

        SharedPreferences pref = getSharedPreferences("password_manager",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isPin",pin);
        editor.commit();
    }

    private void savePrefsPin(String pin) {

        SharedPreferences pref = getSharedPreferences("password_manager",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("pin",pin);
        editor.commit();
    }

    private void savePassword(String pin){
        String currentUserId = mAuth.getCurrentUser().getUid();

        childUser = mRef.child("Passcode").child(currentUserId).child("PIN");

        HashMap<String, Object> messageInfoMap = new HashMap<>();
        messageInfoMap.put("pin",pin);

        childUser.updateChildren(messageInfoMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this, "Added SuccessFully", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


}