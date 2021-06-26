package com.elf.dea;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.elf.dea.MeetingData.Meeting;
import com.elf.dea.UserData.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

public class MeetingActivity extends AppCompatActivity {
    int position;
    ArrayList<String> usernameFromDB;
    ArrayList<String> meetingNameFromDB;
    ArrayList<String> meetingImageFromDB;
    ArrayList<String> meetingDateTimeFromDB;
    ArrayList<String> meetingDistrictFromDB;
    ArrayList<String> meetingRestaurantNameFromDB;
    ArrayList<String> meetingCreators;

    TextView name, datetime, location, restName , eatType, meetpr, interestText;
    ImageView imageView;
    TextView creator;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    User user = new User();
    Meeting meeting = new Meeting();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting);

        firebaseAuth =FirebaseAuth.getInstance();
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

        System.out.println("name: " + meetingNameFromDB.get(position));


        name = findViewById(R.id.meetNameText);
        datetime = findViewById(R.id.datetimeText2);
        location = findViewById(R.id.locText);
        restName = findViewById(R.id.restText);
        eatType = findViewById(R.id.eatText);
        meetpr = findViewById(R.id.meetText);
        interestText = findViewById(R.id.intText);
        imageView = findViewById(R.id.imageView9);
        creator = findViewById(R.id.creatorText);

        String name1 = meetingNameFromDB.get(position);

        name.setText(name1);
        datetime.setText(meetingDateTimeFromDB.get(position));
        location.setText(meetingDistrictFromDB.get(position));
        restName.setText(meetingRestaurantNameFromDB.get(position));
        Picasso.get().load(meetingImageFromDB.get(position)).into(imageView);
        creator.setText(meetingCreators.get(position));

        getUserInterestFromDB(meetingCreators.get(position));
    }

    public void getUserInterestFromDB(String creator){

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

                        getMeetingRestaurantFromDB(user, creator, meeting);

                        System.out.println("__get INTEREST: " + user.getEatingPreferences().isCoffee());


                    }
                }
            }
        });
    }

    public void getMeetingRestaurantFromDB(User user, String mail, Meeting meeting){

        CollectionReference meetingCollectionReference = firebaseFirestore.collection("Meetings");
        meetingCollectionReference.document(mail).collection("Restaurant").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    System.out.println("DB ERROR");
                }

                if (value != null) {
                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                        Map<String, Object> data = snapshot.getData();

                        String resname = (String) data.get("name");

                        boolean hasAnimal = (boolean) data.get("hasAnimal");
                        boolean hasOuterSpace = (boolean) data.get("hasOuterSpace");
                        boolean hasInnerSpace = (boolean) data.get("hasInnerSpace");
                        boolean hasSmokingArea = (boolean) data.get("hasSmokingArea");
                        boolean hasWifi = (boolean) data.get("hasWifi");

                        boolean isCafe = (boolean) data.get("isCafe");
                        boolean isBar = (boolean) data.get("isBar");
                        boolean isFastFood = (boolean) data.get("isFastFood");
                        boolean isFish = (boolean) data.get("isFish");
                        boolean isMeat = (boolean) data.get("isMeat");
                        boolean isTraditional = (boolean) data.get("isTraditional");

                        String expenses = (String) data.get("expenses");

                        meeting.getRestaurant().getPlaceFeature().setAvailableForAnimals(hasAnimal); //dbye yanlış basmışız creatingmeeting detailde
                        meeting.getRestaurant().getPlaceFeature().setSmokingArea(hasSmokingArea);
                        meeting.getRestaurant().getPlaceFeature().setWifi(hasWifi);
                        meeting.getRestaurant().getPlaceFeature().setOuterSpace(hasOuterSpace);
                        meeting.getRestaurant().getPlaceFeature().setInnerSpace(hasInnerSpace);


                        meeting.getRestaurant().getEatType().setFish(isFish);
                        meeting.getRestaurant().getEatType().setMeat(isMeat);
                        meeting.getRestaurant().getEatType().setBar(isBar);
                        meeting.getRestaurant().getEatType().setCafe(isCafe);
                        meeting.getRestaurant().getEatType().setFastfood(isFastFood);
                        meeting.getRestaurant().getEatType().setTraditional(isTraditional);

                        meeting.getRestaurant().setExpenses(expenses);
                        meeting.getRestaurant().setName(resname);

                        showMeeting(user, meeting);
                    }
                }

            }
        });
    }

    public  void showMeeting(User user, Meeting meeting){
        String eatPref = "";
        String meetPref = "";
        String availablity = "";
        String interest = "";
        String datetime = "";

        if(meeting.getRestaurant().getEatType().isFish()){
            eatPref += " Fish /";
        }
        if(meeting.getRestaurant().getEatType().isMeat()){
            eatPref += " Meat /";
        }
        if(meeting.getRestaurant().getEatType().isTraditional()){
            eatPref += " Traditional /";
        }
        if(meeting.getRestaurant().getEatType().isCafe()){
            eatPref += " Cafe /";
        }
        if(meeting.getRestaurant().getEatType().isFastfood()){
            eatPref += " Fast-food /";
        }
        if(meeting.getRestaurant().getEatType().isBar()){
            eatPref += " Bar /";
        }
        eatType.setText(eatPref);
        //////////meetPref
        if(meeting.getRestaurant().getPlaceFeature().isWifi()){
            meetPref += " WiFi /";
        }
        if(meeting.getRestaurant().getPlaceFeature().isSmokingArea()){
            meetPref += " Smoking Area /";
        }
        if(meeting.getRestaurant().getPlaceFeature().isOuterSpace()){
            meetPref += " Outer Space /";
        }
        if(meeting.getRestaurant().getPlaceFeature().isInnerSpace()){
            meetPref += " Inner Space /";
        }
        if(meeting.getRestaurant().getPlaceFeature().isAvailableForAnimals()){
            meetPref += " Animal Friendly /";
        }
        meetpr.setText(meetPref);

        ///interest
        if(user.getInterest().isTravel()){
            interest += " Travel /";
        }
        if (user.getInterest().isTechnology()){
            interest += " Technology /";
        }
        if (user.getInterest().isSports()){
            interest += " Sports /";
        }
        if (user.getInterest().isArt()){
            interest += " Art /";
        }
        if (user.getInterest().isMusic()){
            interest += " Music /";
        }
        interestText.setText(interest);
    }
}