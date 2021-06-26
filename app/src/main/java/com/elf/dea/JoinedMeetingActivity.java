package com.elf.dea;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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

public class JoinedMeetingActivity extends AppCompatActivity implements JoinedRecycleAdapter.JoinedRecyclerViewClickListener {
    private RecyclerView recyclerView;
    JoinedRecycleAdapter joinedRecycleAdapter;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    ArrayList<String> usernameFromDB;
    ArrayList<String> meetingNameFromDB;
    ArrayList<String> meetingImageFromDB;
    ArrayList<String> meetingDateTimeFromDB;
    ArrayList<String> meetingDistrictFromDB;
    ArrayList<String> meetingRestaurantNameFromDB;
    ArrayList<String> meetingCreators;
    ArrayList<String> allMails;

    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joined_meeting);
        recyclerView = findViewById(R.id.joinRecycleView);

        usernameFromDB = new ArrayList<>();
        meetingNameFromDB = new ArrayList<>();
        meetingImageFromDB = new ArrayList<>();
        meetingDateTimeFromDB = new ArrayList<>();
        meetingDistrictFromDB = new ArrayList<>();
        meetingRestaurantNameFromDB = new ArrayList<>();

        meetingCreators = new ArrayList<>();
        allMails = new ArrayList<>();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = firebaseFirestore.getInstance();

        user = (User) getIntent().getSerializableExtra("user");

        getJoinedMeetingDataFromDB();

        joinedRecycleAdapter = new JoinedRecycleAdapter(meetingNameFromDB, meetingRestaurantNameFromDB, meetingDateTimeFromDB, meetingDistrictFromDB, meetingImageFromDB,  this);

        recyclerView.setAdapter(joinedRecycleAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



    }
    public void JoinedRecyclerViewListClicked(View v, int position){

            System.out.println("Button " + meetingNameFromDB.get(position));

            Intent intent = new Intent(JoinedMeetingActivity.this, MeetingActivity.class);
            intent.putExtra("position", position);
            intent.putExtra("meetingNameFromDB", meetingNameFromDB);
            intent.putExtra("meetingDateTimeFromDB", meetingDateTimeFromDB);
            intent.putExtra("meetingRestaurantNameFromDB",meetingRestaurantNameFromDB);
            intent.putExtra("meetingDistrictFromDB", meetingDistrictFromDB);
            intent.putExtra("meetingImageFromDB", meetingImageFromDB);
            intent.putExtra("meetingCreators", meetingCreators);

            startActivity(intent);


    }

    public void getJoinedMeetingDataFromDB(){
        CollectionReference userCollectionReference = firebaseFirestore.collection("Users");

        userCollectionReference.document(firebaseAuth.getCurrentUser().getEmail()).collection("Meetings I've joined").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(JoinedMeetingActivity.this, error.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                }
                if (value != null) {

                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                        Map<String, Object> data = snapshot.getData();
                        String meetingname = (String) data.get("meeting name");
                        String creator = (String) data.get("creator of meeting");

                        getDataFromFirestore(creator,meetingname);
                    }
                }
            }
        });
    }
    public void getDataFromFirestore(String mail, String meetingName){
        CollectionReference meetingCollectionReference = firebaseFirestore.collection("Meetings");

        meetingCollectionReference.document(mail).collection("Meeting Info").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(JoinedMeetingActivity.this, error.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
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
                        String creator = (String) data.get("creator user");

                        //String datetime = day + " / " + month + " / " + year + " " + hour +" : " + second;
                        String datetime = String.valueOf(day) + "/" + String.valueOf(month) + "/" + String.valueOf(year) +
                                " " + String.valueOf(hour) + ":" + String.valueOf(second);

                        String restaurantname = (String) data.get("restaurant name");
                        String imageurl = (String) data.get("imageurl");

                        //System.out.println("Databaseden gelen meetingname: " + meetingname);
                        //System.out.println("Databaseden gelen datetime: " + datetime);
                        //System.out.println("Databaseden gelen district: " + district);
                        //System.out.println("Databaseden gelen restaurantname: " + restaurantname);
                        //System.out.println("Databaseden gelen imageurl: " + imageurl);

                        if(meetingName == meetingName){
                            meetingNameFromDB.add(meetingname);
                        }
                        meetingDateTimeFromDB.add(datetime);
                        meetingDistrictFromDB.add(district);
                        meetingImageFromDB.add(imageurl);
                        meetingRestaurantNameFromDB.add(restaurantname);
                        meetingCreators.add(creator);

                        joinedRecycleAdapter.notifyDataSetChanged();

                    }
                }
            }
        });
    }

    ///option menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //menuyu bağlamak için

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.options_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) { //itemler ne yapacak yazmak için

        if(item.getItemId() == R.id.CreateMeeting){

            Intent intentToUpload = new Intent(JoinedMeetingActivity.this, CreateMeetingActivity.class);
            startActivity(intentToUpload);

        }
        else if(item.getItemId() == R.id.SignOut){

            firebaseAuth.signOut();

            Intent intentToSignUp = new Intent(JoinedMeetingActivity.this, SignUpActivity.class);
            startActivity(intentToSignUp);
        }
        else if(item.getItemId() == R.id.MyProfile){
            Intent intentToUpload = new Intent(JoinedMeetingActivity.this, MyProfileActivity.class);
            startActivity(intentToUpload);

        }
        else if(item.getItemId() == R.id.ShowAll) {
            Intent intent = new Intent(JoinedMeetingActivity.this, FeedActivity.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.ShowMatches) {
            Intent intent = new Intent(JoinedMeetingActivity.this, MatchingActivity.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.MyMeetings) {
            Intent intent = new Intent(JoinedMeetingActivity.this, MyMeetingsActivity.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.JoinedMeetings){
            Intent intent = new Intent(JoinedMeetingActivity.this, JoinedMeetingActivity.class);
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}