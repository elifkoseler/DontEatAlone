package com.elf.dea;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elf.dea.UserData.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;
//şimdilik duruyor ama edit butonu vs özelleştirilerek başka bir recycle adapter yazılmalı!!

public class MyMeetingsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    ArrayList<String> usernameFromDB;
    ArrayList<String> meetingNameFromDB;
    ArrayList<String> meetingImageFromDB;
    ArrayList<String> meetingDateTimeFromDB;
    ArrayList<String> meetingDistrictFromDB;
    ArrayList<String> meetingRestaurantNameFromDB;

    FeedRecyclerAdapter feedRecyclerAdapter;


    User user; //bu user refi burda işlevsiz tam burada dbden tüm user çekilmeli

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //menuyu bağlamak için

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.options_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) { //itemler ne yapacak yazmak için

        if(item.getItemId() == R.id.CreateMeeting){

            Intent intentToUpload = new Intent(MyMeetingsActivity.this, CreateMeetingActivity.class);
            startActivity(intentToUpload);

        }
        else if(item.getItemId() == R.id.SignOut){

            firebaseAuth.signOut();

            Intent intentToSignUp = new Intent(MyMeetingsActivity.this, SignUpActivity.class);
            startActivity(intentToSignUp);
        }
        else if(item.getItemId() == R.id.MyProfile){
            Intent intent = new Intent(MyMeetingsActivity.this, MyProfileActivity.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.MyMeetings){
            Intent intent = new Intent(MyMeetingsActivity.this, MyMeetingsActivity.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.ShowAll) {
            Intent intent = new Intent(MyMeetingsActivity.this, FeedActivity.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.ShowMatches) {
            Intent intent = new Intent(MyMeetingsActivity.this, MatchingActivity.class);
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_meetings);
        recyclerView = findViewById(R.id.rec);

        usernameFromDB = new ArrayList<>();
        meetingNameFromDB = new ArrayList<>();
        meetingImageFromDB = new ArrayList<>();
        meetingDateTimeFromDB = new ArrayList<>();
        meetingDistrictFromDB = new ArrayList<>();
        meetingRestaurantNameFromDB = new ArrayList<>();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = firebaseFirestore.getInstance();

        user = (User) getIntent().getSerializableExtra("user"); // get ref of user from other activities
        getDataFromFirestore();

        feedRecyclerAdapter = new FeedRecyclerAdapter(meetingNameFromDB, meetingRestaurantNameFromDB, meetingDateTimeFromDB, meetingDistrictFromDB, meetingImageFromDB);

        recyclerView.setAdapter(feedRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void getDataFromFirestore( ){
        System.out.println("getDataFromFB methoduna girdi");
        //System.out.println("allMails" + allMails.get(0));
        CollectionReference userCollectionReference = firebaseFirestore.collection("Meetings");

        userCollectionReference.document(firebaseAuth.getCurrentUser().getEmail()).collection("Meeting Info").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(MyMeetingsActivity.this, error.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                }

                if (value != null) {

                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                        Map<String, Object> data = snapshot.getData();

                        //String username = (String) data.get("username");
                        String meetingname = (String) data.get("name");
                        String district = (String) data.get("district");
                        Number year = (Number) data.get("year");
                        Number month = (Number) data.get("month");
                        Number day = (Number) data.get("day");
                        Number hour = (Number) data.get("hour");
                        Number second = (Number) data.get("second");

                        //String datetime = day + " / " + month + " / " + year + " " + hour +" : " + second;
                        String datetime = String.valueOf(day) + "/" + String.valueOf(month) + "/" + String.valueOf(year) +
                                " " + String.valueOf(hour) + ":" + String.valueOf(second);

                        String restaurantname = (String) data.get("restaurant name");
                        String imageurl = (String) data.get("imageurl");

                        System.out.println("Databaseden gelen meetingname: " + meetingname);
                        System.out.println("Databaseden gelen datetime: " + datetime);
                        System.out.println("Databaseden gelen district: " + district);
                        System.out.println("Databaseden gelen restaurantname: " + restaurantname);
                        System.out.println("Databaseden gelen imageurl: " + imageurl);

                        //meetingText.setText(meetingname);

                        meetingNameFromDB.add(meetingname);
                        meetingDateTimeFromDB.add(datetime);
                        meetingDistrictFromDB.add(district);
                        meetingImageFromDB.add(imageurl);
                        meetingRestaurantNameFromDB.add(restaurantname);
                        //System.out.println("Liste:" +  meetingDistrictFromDB.get(0));
                        feedRecyclerAdapter.notifyDataSetChanged();

                    }
                }
            }
        });
    }
}