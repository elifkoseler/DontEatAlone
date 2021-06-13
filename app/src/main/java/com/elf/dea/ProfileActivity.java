package com.elf.dea;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.elf.dea.UserData.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {

    EditText nameText, usernameText, phoneText, locationText, birthYearText;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    User user = new User();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        nameText = findViewById(R.id.NameText);
        usernameText = findViewById(R.id.UsernameText);
        phoneText = findViewById(R.id.PhoneText);
        locationText = findViewById(R.id.LocationText);
        birthYearText = findViewById(R.id.BirthYearText);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

    }

    public void EditLater(View view){
        Log.d("myTag","EditLater butonuna basıldı!");
        Intent intent = new Intent(ProfileActivity.this, StartActivity.class);
        startActivity(intent);
        //finish();
    }

    public void Save(View view){

        //UUID uuid = UUID.randomUUID();

        user.setName(nameText.getText().toString());
        user.setUsername(usernameText.getText().toString());
        user.setPhone(phoneText.getText().toString());
        user.setLocation(locationText.getText().toString());
        user.setBirthYear(Integer.parseInt(birthYearText.getText().toString()));
        //Toast.makeText(ProfileActivity.this,"Save'e bastın !!", Toast.LENGTH_LONG).show();
        user.setEmail(firebaseAuth.getCurrentUser().getEmail());

        HashMap<String, Object> emailData = new HashMap<>();
        emailData.put("mail", user.getEmail());

        HashMap<String, Object> postData = new HashMap<>();
        postData.put("name", user.getName());
        postData.put("username", user.getUsername());
        postData.put("email", user.getEmail());
        postData.put("phone", user.getPhone());
        postData.put("location", user.getLocation());
        postData.put("birth year", user.getBirthYear());
        postData.put("register time", FieldValue.serverTimestamp());

        firebaseFirestore.collection("UserMails").add(emailData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(ProfileActivity.this,"UserMail Dbye eklendi!!",Toast.LENGTH_LONG).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(ProfileActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();

            }
        });
        //User maili bi yerde komple tutmam gerekiyor

        firebaseFirestore.collection("Users").document(user.getEmail()).collection("User Info")
                .add(postData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(ProfileActivity.this,"Dbye eklendi!!",Toast.LENGTH_LONG).show();
                System.out.println("Profile OnSuccesstesin");
                Intent intent = new Intent(ProfileActivity.this, EatingPreferenceActivity.class);
                intent.putExtra("user",user);
                System.out.println("ekstra");
                startActivity(intent);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfileActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();

            }
        });


        /*
        //Toast.makeText(ProfileActivity.this,"girdi!",Toast.LENGTH_LONG).show();
        Log.d("myTag",name);
        Log.d("myTag",username);
        Log.d("myTag",phone);


        if(name == null || username == null || phone == null){
            Toast.makeText(ProfileActivity.this,"Please fill all information before click the SAVE button!",Toast.LENGTH_LONG).show();
            Log.d("myTag","Bir şey boş!!");
            Log.d("myTag",name);
            Log.d("myTag",username);
            Log.d("myTag",phone);

        }

        if(name != null && username != null  && phone != null){
           // System.out.println(firebaseAuth.getCurrentUser().getEmail());
            Log.d("myTag","Her şey dolu!!!");
            Log.d("myTag",name);
            Log.d("myTag",username);
            Log.d("myTag",phone);
// Dolu boş kontrolleri yapılmalı bir problem var her şeye dolu diyor :( null gelse dahil
*/
        }
    }
