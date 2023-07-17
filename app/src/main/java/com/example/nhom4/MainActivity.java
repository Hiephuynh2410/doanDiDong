package com.example.nhom4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1;
    private static final String TAG = "GOOGLEAUTH ";
    GoogleSignInClient googleSignInClient ;
    private FirebaseAuth mauth;
    Dialog dialog;
    TextInputEditText editEmail, editPass;
    Button btnsignin;
    TextView twSignUp;
    ImageView twGoogle;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dialog = new Dialog(MainActivity.this);
        editEmail = findViewById(R.id.txtEmail);
        editPass = findViewById(R.id.txtPassword);
        btnsignin = findViewById(R.id.btnSignIn);
        twSignUp = findViewById(R.id.signup);

       GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
               .requestIdToken(getString(R.string.default_web_client_id))
               .requestEmail().build();

        googleSignInClient = GoogleSignIn.getClient(this,gso);

        twGoogle = findViewById(R.id.google_btn);
        twGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignIn();
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
    private  void SignIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            dialog.show();
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                //Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                dialog.dismiss();
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            Intent i = new Intent(MainActivity.this,HomePage.class);
                            startActivity(i);
                            finish();
                            dialog.dismiss();
                            //  updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            //  Log.w(TAG, "signInWithCredential:failure", task.getException());
                            //  Snackbar.make(mBinding.mainLayout, "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            // updateUI(null);
                            dialog.dismiss();
                            Toast.makeText(MainActivity.this,"Login failed",Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }
}