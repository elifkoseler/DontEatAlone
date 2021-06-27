package com.elf.dea;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth; //tanımladım.
    EditText emailText, passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firebaseAuth = FirebaseAuth.getInstance(); //objeyi initialize ettim
        emailText = findViewById(R.id.emailText);
        passwordText = findViewById(R.id.passwordText);

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null){
            Intent intent = new Intent(SignUpActivity.this,FeedActivity.class);
            startActivity(intent);
            finish(); //aktiviteye geri dönmüyoruz destroy ediyoruz
        }

    }

    public void signInClicked(View view){

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Intent intent = new Intent(SignUpActivity.this,StartActivity.class);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignUpActivity.this, e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
            }
        });


    }

    public void signUpClicked(View view){

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        //if(email.matches("")) //boşsa hata ver alert falanla olabilir

        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() { //başarılı olursa döner
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(SignUpActivity.this,"User Created",Toast.LENGTH_LONG).show();

                Intent intent = new Intent(SignUpActivity.this,ProfileActivity.class);
                //Toast.makeText(SignUpActivity.this, "Please sign in with your new credentials!",Toast.LENGTH_LONG).show();
                //buraya hoşgeldiniz gibi bir şey eklenecek
                startActivity(intent);

                finish();
            }
        }).addOnFailureListener(new OnFailureListener() { //başarısız olursa hata mesajı alırım kullancıya nedeni ile fail
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignUpActivity.this, e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
            }
        });




    }

    public void ForgotPassword(View view){
        Intent intent = new Intent(SignUpActivity.this,ForgotPasswordActivity.class);
        startActivity(intent);
    }
}