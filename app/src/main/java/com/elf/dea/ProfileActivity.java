package com.elf.dea;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.elf.dea.UserData.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.UUID;

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
        Intent intent = new Intent(ProfileActivity.this,FeedActivity.class);
        startActivity(intent);
        //finish();
    }

    public void Save(View view){

        //UUID uuid = UUID.randomUUID();

        user.name = nameText.getText().toString();
        user.username = usernameText.getText().toString();
        user.phone = phoneText.getText().toString();
        user.location = locationText.getText().toString();
        user.birthYear = Integer.parseInt(birthYearText.getText().toString());
        //Toast.makeText(ProfileActivity.this,"Save'e bastın !!", Toast.LENGTH_LONG).show();
        user.email = firebaseAuth.getCurrentUser().getEmail();

        HashMap<String, Object> postData = new HashMap<>();
        postData.put("name", user.name);
        postData.put("username", user.username);
        postData.put("email", user.email);
        postData.put("phone", user.phone);
        postData.put("location", user.location);
        postData.put("birth year", user.birthYear);


        firebaseFirestore.collection("Users").document(user.email).collection("User Info")
                .add(postData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(ProfileActivity.this,"Dbye eklendi!!",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ProfileActivity.this, UserPreferenceActivity.class);
                startActivity(intent);
                finish();
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
