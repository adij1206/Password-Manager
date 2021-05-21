package com.aditya.passwordmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText userEmail,userPassword;
    private Button userLogin;
    private TextView newUserAccount;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference();

        userEmail = (EditText) findViewById(R.id.login_email);
        userPassword = (EditText) findViewById(R.id.login_password);
        userLogin = (Button) findViewById(R.id.login_button);
        loadingBar = new ProgressDialog(this);
        newUserAccount = (TextView) findViewById(R.id.need_new_acount_link);

        userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginToMain();
            }
        });

        newUserAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loginToMain() {
        String email = userEmail.getText().toString();
        String password = userPassword.getText().toString();

        loadingBar.setTitle("Creating New Account");
        loadingBar.setMessage("Please wait while we are creating your account...");
        loadingBar.setCanceledOnTouchOutside(true);
        loadingBar.show();

        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){

            mAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                //sendToMainActivity();
                                Toast.makeText(LoginActivity.this, "Successfully Logged In...", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                if (!isActivePass()) {
                                    if(isPin()){
                                        Intent intent = new Intent(LoginActivity.this, PinActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else{
                                        getPassword();
                                    }

                                }

                            }
                            else{
                                String e= task.getException().toString();
                                Toast.makeText(LoginActivity.this, "Eroor: "+e, Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                        }
                    });
        }
        else{
            Toast.makeText(this, "Please fill the blank column..", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isActivePass() {
        SharedPreferences prefs = getSharedPreferences("password_manager", MODE_PRIVATE);
        return prefs.getBoolean("isActivePin", false);
    }

    private boolean isPin(){
        SharedPreferences prefs = getSharedPreferences("password_manager", MODE_PRIVATE);
        String p = prefs.getString("pin", "");
        Log.d("Adi", "isPin: "+p);

        if(p.equals("")){
//            String pwd = getPassword();
//            SharedPreferences pref = getSharedPreferences("password_manager",MODE_PRIVATE);
//            SharedPreferences.Editor editor = pref.edit();
//            editor.putString("pin",pwd);
//            editor.commit();
            return false;
        }

        return true;

    }

    private void getPassword(){

        String id = mAuth.getCurrentUser().getUid();

        final String[] pin = new String[1];
        mRef.child("Passcode").child(id).child("PIN").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild("pin")){
                    pin[0] =  snapshot.child("pin").getValue().toString();
                    Log.d("Adi", "onDataChange124: "+pin[0]);
                    SharedPreferences pref = getSharedPreferences("password_manager",MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("pin",pin[0]);
                    editor.commit();

                    Intent intent = new Intent(LoginActivity.this, PinActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

}