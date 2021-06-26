package com.elf.dea;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.elf.dea.UserData.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.Map;

public class MyProfileActivity extends AppCompatActivity {

    User user;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    ImageView profileImage;
    TextView emailText, usernameText, phoneText;
    TextView eatPrefText, meetPrefText, availableText, interestText;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //menuyu bağlamak için

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.options_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) { //itemler ne yapacak yazmak için

        if(item.getItemId() == R.id.CreateMeeting){

            Intent intentToUpload = new Intent(MyProfileActivity.this, CreateMeetingActivity.class);
            startActivity(intentToUpload);

        }
        else if(item.getItemId() == R.id.SignOut){

            firebaseAuth.signOut();

            Intent intentToSignUp = new Intent(MyProfileActivity.this, SignUpActivity.class);
            startActivity(intentToSignUp);
        }
        else if(item.getItemId() == R.id.MyProfile){
            Intent intentToUpload = new Intent(MyProfileActivity.this, MyProfileActivity.class);
            startActivity(intentToUpload);

        }
        else if(item.getItemId() == R.id.ShowAll) {
            Intent intent = new Intent(MyProfileActivity.this, FeedActivity.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.ShowMatches) {
            Intent intent = new Intent(MyProfileActivity.this, MatchingActivity.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.MyMeetings) {
            Intent intent = new Intent(MyProfileActivity.this, MyMeetingsActivity.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.JoinedMeetings){
            Intent intent = new Intent(MyProfileActivity.this, JoinedMeetingActivity.class);
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        user = new  User();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = firebaseFirestore.getInstance();

        profileImage = findViewById(R.id.profileImage);
        emailText = findViewById(R.id.emailText2);
        eatPrefText = findViewById(R.id.eatPrefText);
        meetPrefText = findViewById(R.id.meetPrefText);
        availableText = findViewById(R.id.availableText);
        interestText = findViewById(R.id.interestText);

        getUserEatingPreferenceFromDB();
    }

    public void Edit(View view) {
        Intent intent = new Intent(MyProfileActivity.this, EditProfileActivity.class);
        startActivity(intent);
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

                        getUserAvailabilityInfoFromDB(user);
                        System.out.println("__get INTEREST: " + user.getEatingPreferences().isCoffee());


                    }
                }
            }
        });
    }

    public void getUserAvailabilityInfoFromDB(User user) {
        CollectionReference userCollectionReference = firebaseFirestore.collection("Users");
        userCollectionReference.document(firebaseAuth.getCurrentUser().getEmail()).collection("Availability").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                if(value != null ){
                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                        Map<String, Object> data = snapshot.getData();

                        String district = (String) data.get("district");
                        Number year = (Number) data.get("year");
                        Number month = (Number) data.get("month");
                        Number day = (Number) data.get("day");
                        Number hour = (Number) data.get("hour");
                        Number second = (Number) data.get("second");

                        user.getMeetingPreferences().setDistrict(district);
                        user.getMeetingPreferences().setDay(Integer.parseInt(String.valueOf(day)));
                        user.getMeetingPreferences().setMonth(Integer.parseInt(String.valueOf(month)));
                        user.getMeetingPreferences().setYear(Integer.parseInt(String.valueOf(year)));
                        user.getMeetingPreferences().setHour(Integer.parseInt(String.valueOf(hour)));
                        user.getMeetingPreferences().setSecond(Integer.parseInt(String.valueOf(second)));

                        getUserInfoFromDB(user);

                        System.out.println("__get AVAİLABLE: " + user.getEatingPreferences().isCoffee());


                    }
                }
            }
        });
    }

    public void getUserInfoFromDB(User user){
        CollectionReference userCollectionReference = firebaseFirestore.collection("Users");
        userCollectionReference.document(firebaseAuth.getCurrentUser().getEmail()).collection("User Info").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                if(value != null ){
                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                        Map<String, Object> data = snapshot.getData();

                        String username = (String) data.get("username");
                        String phone = (String) data.get("phone");
                        String imageUrl = (String) data.get("profile image url");
                        String mail = firebaseAuth.getCurrentUser().getEmail();
                        //imagei alıp basmak lazım dbde olmadığı için almadım hata alıcam yoksa
                        user.setEmail(mail);
                        user.setUsername(username);
                        user.setPhone(phone);
                        user.setProfileImageUrl(imageUrl);
                        showProfile(user);
                        System.out.println("__get AVAİLABLE: " + user.getEatingPreferences().isCoffee());


                    }
                }
            }
        });
    }

    public void showProfile(User user){
        emailText.setText(user.getEmail());
        if(user.getProfileImageUrl() != null){
        Picasso.get().load(user.getProfileImageUrl()).into(profileImage);
        }

        String eatPref = "";
        String meetPref = "";
        String availablity = "";
        String interest = "";
        String datetime = "";

        if(user.getEatingPreferences().isFish()){
            eatPref += " Fish /";
        }
        if(user.getEatingPreferences().isMeat()){
            eatPref += " Meat /";
        }
        if(user.getEatingPreferences().isTraditional()){
            eatPref += " Traditional /";
        }
        if(user.getEatingPreferences().isCoffee()){
            eatPref += " Coffee /";
        }
        if(user.getEatingPreferences().isFastfood()){
            eatPref += " Fast-food /";
        }
        if(user.getEatingPreferences().isDrink()){
            eatPref += " Drink /";
        }
        eatPrefText.setText(eatPref);
        //////////meetPref
        if(user.getMeetingPreferences().getRestaurant().getPlaceFeature().isWifi()){
            meetPref += " WiFi /";
        }
        if(user.getMeetingPreferences().getRestaurant().getPlaceFeature().isSmokingArea()){
            meetPref += " Smoking Area /";
        }
        if(user.getMeetingPreferences().getRestaurant().getPlaceFeature().isOuterSpace()){
            meetPref += " Outer Space /";
        }
        if(user.getMeetingPreferences().getRestaurant().getPlaceFeature().isInnerSpace()){
            meetPref += " Inner Space /";
        }
        if(user.getMeetingPreferences().getRestaurant().getPlaceFeature().isAvailableForAnimals()){
            meetPref += " Animal Friendly /";
        }
        meetPrefText.setText(meetPref);
        //// availability
        datetime = String.valueOf(user.getMeetingPreferences().getDay()) + "/" + String.valueOf(user.getMeetingPreferences().getMonth()) + "/" + String.valueOf(user.getMeetingPreferences().getYear()) +
                " " + String.valueOf(user.getMeetingPreferences().getHour()) + ":" + String.valueOf(user.getMeetingPreferences().getSecond());
        availablity = datetime + " " + user.getMeetingPreferences().getDistrict();

        availableText.setText(availablity);
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