package com.example.nhom4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    TextInputEditText editEmail, editPass;
    Button btnsignin;
    TextView twSignUp;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editEmail = findViewById(R.id.txtEmail);
        editPass = findViewById(R.id.txtPassword);
        btnsignin = findViewById(R.id.btnSignIn);
        twSignUp = findViewById(R.id.signup);

        twSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterPage.class);
                startActivity(intent);
                finish();
            }
        });

        btnsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email, Password;
                email = String.valueOf(editEmail.getText());
                Password = String.valueOf(editPass.getText());

                if(TextUtils.isEmpty(email) && TextUtils.isEmpty(Password)) {
                    editEmail.setError("vui lòng nhập email");
                    editPass.setError("vui lòng nhập Pass");
                    return;
                }

                firebaseAuth.signInWithEmailAndPassword(email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(MainActivity.this,"login success", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, HomePage.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(MainActivity.this,"faile", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }
}