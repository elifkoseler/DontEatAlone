package com.elf.dea;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class GiveScoreActivity extends AppCompatActivity {
    RatingBar userRatingBar, meetingRatingBar;
    float userRate, meetingRate;

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;

    int position;
    ArrayList<String> usernameFromDB;
    ArrayList<String> meetingNameFromDB;
    ArrayList<String> meetingImageFromDB;
    ArrayList<String> meetingDateTimeFromDB;
    ArrayList<String> meetingDistrictFromDB;
    ArrayList<String> meetingRestaurantNameFromDB;
    ArrayList<String> meetingCreators;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_score);

        userRatingBar = findViewById(R.id.ratingBar2); // initiate a rating bar
        meetingRatingBar = findViewById(R.id.ratingBar);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        usernameFromDB = new ArrayList<>();
        meetingNameFromDB = new ArrayList<>();
        meetingImageFromDB = new ArrayList<>();
        meetingDateTimeFromDB = new ArrayList<>();
        meetingDistrictFromDB = new ArrayList<>();
        meetingRestaurantNameFromDB = new ArrayList<>();

        meetingCreators = new ArrayList<>();

        Intent intent = getIntent();
        position = intent.getIntExtra("position", 0);

        meetingCreators = intent.getStringArrayListExtra("meetingCreators");
        meetingNameFromDB = intent.getStringArrayListExtra("meetingNameFromDB");
        meetingDateTimeFromDB = intent.getStringArrayListExtra("meetingDateTimeFromDB");
        meetingRestaurantNameFromDB = intent.getStringArrayListExtra("meetingRestaurantNameFromDB");
        meetingDistrictFromDB = intent.getStringArrayListExtra("meetingDistrictFromDB");
        meetingCreators = intent.getStringArrayListExtra("meetingCreators");
        meetingImageFromDB = intent.getStringArrayListExtra("meetingImageFromDB");


    }

    public void submit(View view){
        userRate = userRatingBar.getRating();
        meetingRate = meetingRatingBar.getRating();

        System.out.println("UserRate: " + userRate);
        System.out.println("MeetingRate: " + meetingRate);

        System.out.println("name: " + meetingNameFromDB.get(position));

        HashMap<String, Object> postData = new HashMap<>();
        postData.put("Score", userRate);
        postData.put("Meeting", meetingNameFromDB.get(position));

        firebaseFirestore.collection("Users").document(meetingCreators.get(position)).collection("Score")
                .add(postData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(GiveScoreActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();

            }
        });

        HashMap<String, Object> data = new HashMap<>();
        data.put("Score", meetingRate);

        firebaseFirestore.collection("Meetings").document(meetingCreators.get(position)).collection("Score")
                .add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(GiveScoreActivity.this,"Score is submitted successfully!",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(GiveScoreActivity.this, JoinedMeetingActivity.class);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(GiveScoreActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();

            }
        });


    }
}