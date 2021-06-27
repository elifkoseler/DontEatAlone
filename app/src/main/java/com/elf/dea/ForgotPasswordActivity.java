package com.elf.dea;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {
    EditText mail;
    String usermail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        mail = findViewById(R.id.forgotmailText);
    }

    public void send(View view){
        usermail = mail.getText().toString();
        FirebaseAuth.getInstance().sendPasswordResetEmail(usermail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            AlertDialog alertDialog1 = new AlertDialog.Builder(ForgotPasswordActivity.this).create();
                            alertDialog1.setTitle("Password reset link is sent!");
                            alertDialog1.setMessage("Please check your mail");

                            alertDialog1.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog1, int which) {
                                    Intent intent = new Intent(ForgotPasswordActivity.this, SignUpActivity.class);
                                    startActivity(intent);
                                }
                            });
                            alertDialog1.show();
                        }
                    }
                });
    }
}