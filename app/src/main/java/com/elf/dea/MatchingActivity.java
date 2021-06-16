package com.elf.dea;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elf.dea.MeetingData.Meeting;
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

public class MatchingActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    FeedRecyclerAdapter feedRecyclerAdapter;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    ArrayList<String> usernameFromDB;
    ArrayList<String> meetingNameFromDB;
    ArrayList<String> meetingImageFromDB;
    ArrayList<String> meetingDateTimeFromDB;
    ArrayList<String> meetingDistrictFromDB;
    ArrayList<String> meetingRestaurantNameFromDB;
    ArrayList<String> allMails;

    ArrayList<Meeting> meetingList;
    ArrayList<Meeting> tempMeetingList;
    ArrayList<User> userList;
    boolean coffee;
    User user;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //menuyu bağlamak için

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.options_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) { //itemler ne yapacak yazmak için

        if(item.getItemId() == R.id.CreateMeeting){

            Intent intentToUpload = new Intent(MatchingActivity.this, CreateMeetingActivity.class);
            startActivity(intentToUpload);

        }
        else if(item.getItemId() == R.id.SignOut){

            firebaseAuth.signOut();

            Intent intentToSignUp = new Intent(MatchingActivity.this, SignUpActivity.class);
            startActivity(intentToSignUp);
        }
        else if(item.getItemId() == R.id.EditProfile){
            //yazılacak

        }
        else if(item.getItemId() == R.id.MyMeetings){
            Intent intent = new Intent(MatchingActivity.this, MyMeetingsActivity.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.ShowAll) {
            Intent intent = new Intent(MatchingActivity.this, FeedActivity.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.ShowMatches) {
            Intent intent = new Intent(MatchingActivity.this, MatchingActivity.class);
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching);

        recyclerView = findViewById(R.id.matchRecycle);

        usernameFromDB = new ArrayList<>();
        meetingNameFromDB = new ArrayList<>();
        meetingImageFromDB = new ArrayList<>();
        meetingDateTimeFromDB = new ArrayList<>();
        meetingDistrictFromDB = new ArrayList<>();
        meetingRestaurantNameFromDB = new ArrayList<>();
        allMails = new ArrayList<>();

        userList = new ArrayList<>();
        meetingList = new ArrayList<>();
        tempMeetingList = new ArrayList<>();

        user = new User();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = firebaseFirestore.getInstance();

        //user = (User) getIntent().getSerializableExtra("user"); // get ref of user from other activities //şurda diğerlerinden almak daha kolay olabilir uğraşmamak adına? bi yerde mutlaka çekmek gerekecek ama?
        getMailDataFromFirestore();
        getUserEatingPreferenceFromDB();

        feedRecyclerAdapter = new FeedRecyclerAdapter(meetingNameFromDB, meetingRestaurantNameFromDB,
                meetingDateTimeFromDB, meetingDistrictFromDB, meetingImageFromDB);

        recyclerView.setAdapter(feedRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    public void getUserEatingPreferenceFromDB(){

        CollectionReference userCollectionReference = firebaseFirestore.collection("Users");
        userCollectionReference.document(firebaseAuth.getCurrentUser().getEmail()).collection("Eating Preferences").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                if (value != null)
                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                        Map<String, Object> data = snapshot.getData();

                        boolean isCoffee = (boolean) data.get("isCoffee");
                        boolean isDrink = (boolean) data.get("isDrink");
                        boolean isFastFood = (boolean) data.get("isFastFood");
                        boolean isFish = (boolean) data.get("isFish");
                        boolean isMeat = (boolean) data.get("isMeat");
                        boolean isTraditional = (boolean) data.get("isTraditional");

                        user.getEatingPreferences().setCoffee(isCoffee);
                        user.getEatingPreferences().setDrink(isDrink);
                        user.getEatingPreferences().setFastfood(isFastFood);
                        user.getEatingPreferences().setFish(isFish);
                        user.getEatingPreferences().setMeat(isMeat);
                        user.getEatingPreferences().setTraditional(isTraditional);

                        getUserMeetingPreferenceFromDB(user);
                        System.out.println("__get EATİNG: " + user.getEatingPreferences().isCoffee());


                    }
            }

        });
    }

    public void getUserMeetingPreferenceFromDB(User user){
        CollectionReference userCollectionReference = firebaseFirestore.collection("Users");
        userCollectionReference.document(firebaseAuth.getCurrentUser().getEmail()).collection("Meeting Preferences")
                .document("Restaurant").collection("Place Features").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                if(value != null ){
                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                        Map<String, Object> data = snapshot.getData();

                        boolean hasAnimal = (boolean) data.get("hasAnimal");
                        boolean hasOuterSpace = (boolean) data.get("hasOuterSpace");
                        boolean hasInnerSpace = (boolean) data.get("hasInnerSpace");
                        boolean hasSmokingArea = (boolean) data.get("hasSmokingArea");
                        boolean hasWifi = (boolean) data.get("hasWifi");

                        user.getMeetingPreferences().getRestaurant().getPlaceFeature().setAvailableForAnimals(hasAnimal);
                        user.getMeetingPreferences().getRestaurant().getPlaceFeature().setInnerSpace(hasInnerSpace);
                        user.getMeetingPreferences().getRestaurant().getPlaceFeature().setOuterSpace(hasOuterSpace);
                        user.getMeetingPreferences().getRestaurant().getPlaceFeature().setSmokingArea(hasSmokingArea);
                        user.getMeetingPreferences().getRestaurant().getPlaceFeature().setWifi(hasWifi);

                        getUserInterestFromDB(user);
                        System.out.println("__get MEETİNG: " + user.getEatingPreferences().isCoffee());


                    }
                }
            }
        });
    }

    public void getUserInterestFromDB(User user){
        User tempUser;
        CollectionReference userCollectionReference = firebaseFirestore.collection("Users");
        userCollectionReference.document(firebaseAuth.getCurrentUser().getEmail()).collection("Interests").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                if(value != null ){
                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                        Map<String, Object> data = snapshot.getData();

                        boolean art = (boolean) data.get("isArt");
                        boolean music = (boolean) data.get("isMusic");
                        boolean sport = (boolean) data.get("isSports");
                        boolean tech = (boolean) data.get("isTech");
                        boolean travel = (boolean) data.get("isTravel");

                        user.getInterest().setArt(art);
                        user.getInterest().setMusic(music);
                        user.getInterest().setSports(sport);
                        user.getInterest().setTechnology(tech);
                        user.getInterest().setTravel(travel);


                        System.out.println("__get INTEREST: " + user.getEatingPreferences().isCoffee());


                    }
                }
            }
        });
    }

    public User getUser(User user, Meeting meeting){
        return user;
    }

    public void UserMatchingCalculator(User user){

    }

    public void getMailDataFromFirestore(){
        System.out.println("MATCHING >> getDataFromFB methoduna girdi > mail getirme kısmı");

        CollectionReference usermailCollectionReference = firebaseFirestore.collection("UserMails");
        usermailCollectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                if (value != null)
                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                        Map<String, Object> mailData = snapshot.getData();

                        String mail = (String) mailData.get("mail");
                        //System.out.println("Tüm mailler: " + mail);
                        getAllMeetingDataFromFirestore(mail);

                        allMails.add(mail);
                        //System.out.println("allMails" + allMails.get(0));

                    }
            }

        });

    }
    // meetinge yolla useri zaten user tek

    public void getAllMeetingDataFromFirestore(String mail) {
        System.out.println("MATCHING >> getDataFromFB methoduna girdi");
        Meeting meeting = new Meeting();
        CollectionReference meetingCollectionReference = firebaseFirestore.collection("Meetings");
        meetingCollectionReference.document(mail).collection("Meeting Info").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    System.out.println("DB ERROR");
                }

                if (value != null) {
                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                        Map<String, Object> data = snapshot.getData();

                        String meetingname = (String) data.get("name");
                        String district = (String) data.get("district");
                        Number year = (Number) data.get("year");
                        Number month = (Number) data.get("month");
                        Number day = (Number) data.get("day");
                        Number hour = (Number) data.get("hour");
                        Number second = (Number) data.get("second");

                        String datetime = String.valueOf(day) + "/" + String.valueOf(month) + "/" + String.valueOf(year) +
                                " " + String.valueOf(hour) + ":" + String.valueOf(second);

                        String restaurantname = (String) data.get("restaurant name");
                        String imageurl = (String) data.get("imageurl");

                        meetingNameFromDB.add(meetingname);
                        meetingDateTimeFromDB.add(datetime);
                        meetingDistrictFromDB.add(district);
                        meetingImageFromDB.add(imageurl);
                        meetingRestaurantNameFromDB.add(restaurantname);

                        meeting.setName(meetingname);
                        meeting.setDistrict(district);
                        meeting.setDay(Integer.parseInt(String.valueOf(day)));
                        meeting.setMonth(Integer.parseInt(String.valueOf(month)));
                        meeting.setYear(Integer.parseInt(String.valueOf(year)));
                        meeting.setHour(Integer.parseInt(String.valueOf(hour)));
                        meeting.setSecond(Integer.parseInt(String.valueOf(second)));

                        meetingList.add(meeting);

                        System.out.println("-- MATCH DB: " + meeting.getDistrict());

                        feedRecyclerAdapter.notifyDataSetChanged();

                    }
                }

            }
        });

    }



}