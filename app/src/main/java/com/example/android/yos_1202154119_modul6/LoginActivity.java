package com.example.android.yos_1202154119_modul6;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    EditText mEmail;
    EditText mPass;

    Button mBtnRegister;
    Button mBtnLogin;

    FirebaseAuth mAuth;
    String TAG = "com.example.android.yos_1202154119_modul6";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        mEmail = (EditText) findViewById(R.id.et_email);
        mPass = (EditText) findViewById(R.id.et_password);
        mBtnRegister = (Button) findViewById(R.id.btn_daftar);
        mBtnLogin = (Button) findViewById(R.id.btn_masuk);

    }

    public void onRegister(View view) {
        String email = mEmail.getText().toString();
        String password = mPass.getText().toString();

        if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this, "The field is empty", Toast.LENGTH_SHORT).show();
        } else {
            if (password.length()>8){
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    //ketika sukses, UI akan menampilkan informasi
                                    Log.d(TAG, "createUserWithEmail:success");
                                    Toast.makeText(LoginActivity.this, "Register Sucess", Toast.LENGTH_SHORT).show();
                                    FirebaseUser user = mAuth.getCurrentUser();

                                } else {
                                    //jika gagal, maka menampilkan pesan dan toast
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(LoginActivity.this, "Register failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }else{

                Toast.makeText(LoginActivity.this, "Password is too short", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onLogin(View view) {
        String email = mEmail.getText().toString();
        String pass = mPass.getText().toString();

        if (TextUtils.isEmpty(email) && TextUtils.isEmpty(pass)){
            Toast.makeText(LoginActivity.this, "Field cannot be empty", Toast.LENGTH_SHORT).show();
        }else{
            mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        //jika sukses maka menampilkan informasi
                        Log.d(TAG, "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        startActivity(new Intent(LoginActivity.this, TimelineActivity.class));
                        Toast.makeText(LoginActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                        } else {
                        //jika gagal akan menampilkan pesan informasi
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                }
            });
        }
    }
}
