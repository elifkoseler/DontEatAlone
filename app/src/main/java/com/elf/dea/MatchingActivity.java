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
import com.google.firebase.firestore.Query;
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

                        getMailDataFromFirestore(user);

                        System.out.println("__get AVAİLABLE: " + user.getEatingPreferences().isCoffee());


                    }
                }
            }
        });
    }



    public void getMailDataFromFirestore(User user){
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
                        getAllMeetingDataFromFirestore(mail, user);

                        allMails.add(mail);
                        //System.out.println("allMails" + allMails.get(0));

                    }
            }

        });


    }
    // meetinge yolla useri zaten user tek

    public void getAllMeetingDataFromFirestore(String mail, User user) {
        System.out.println("MATCHING >> getDataFromFB methoduna girdi");
        System.out.println("MAİL: " + mail);

        Meeting meeting = new Meeting();
        CollectionReference meetingCollectionReference = firebaseFirestore.collection("Meetings");
        meetingCollectionReference.document(mail).collection("Meeting Info").orderBy("create date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
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

                        String creator = (String) data.get("creator user");

                        meeting.setName(meetingname);
                        meeting.setDistrict(district);
                        meeting.setDay(Integer.parseInt(String.valueOf(day)));
                        meeting.setMonth(Integer.parseInt(String.valueOf(month)));
                        meeting.setYear(Integer.parseInt(String.valueOf(year)));
                        meeting.setHour(Integer.parseInt(String.valueOf(hour)));
                        meeting.setSecond(Integer.parseInt(String.valueOf(second)));
                        meeting.getRestaurant().setName(restaurantname);
                        meeting.setImageUrl(imageurl);
                        meeting.setCreator(creator);

                        meetingList.add(meeting);
                        getMeetingRestaurantFromDB(user, meeting, mail);


                        //MatchingCalculator(user, meeting);

                        System.out.println("__get INTERESTMEET: " + user.getEatingPreferences().isCoffee());


                    }
                }

            }
        });

    }


    public void getMeetingRestaurantFromDB(User user, Meeting meeting, String mail){
        for (Meeting meet : meetingList){
            System.out.println("MEETLİST => " + meetingList.indexOf(meet)+ " => " +meet.getDistrict());
        }
        System.out.println("+++ Second Meeting Func =>  " + meeting.getDistrict());

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

                        //burada kaldın dbden tüm meet rest özelliklerini çekip aşağıda matchlemeyi dene
                        //sonrasında bu çıkan puanların meetleriyle creatora gidip userin interestlerini karşılaştırıcaz.
                        //en son feedrecycle için basıcaz feed kaç puan üstü olacak henüz karar veremedim

                        System.out.println("-- LAST MATCH DB: " + meeting.getDistrict());
                        System.out.println("-- LAST : user:  " + user.getEatingPreferences().isCoffee());


                        MeetingMatchingCalculator(user, meeting);


                    }
                }

            }
        });
    }

    public void MeetingMatchingCalculator(User user, Meeting meeting){

        int res = 0;
        int datePoint = 0;
        int timePoint = 0;
        int eatPoint = 0;
        int locPoint = 0;
        int placePoint = 0;
        int intPoint = 0;

        ArrayList<String> Europe = new ArrayList<>();
        ArrayList<String> Anatolia = new ArrayList<>();
        ArrayList<String> Popular = new ArrayList<>();

        Popular.add("Beşiktaş"); Popular.add("Kadıköy"); Popular.add("Bakırköy"); Popular.add("Şişli"); Popular.add("Fatih");

        Europe.add("Arnavutköy"); Europe.add("Avcılar"); Europe.add("Bağcılar");
        Europe.add("Bahçelievler"); Europe.add("Bakırköy"); Europe.add("Başakşehir");
        Europe.add("Bayrampaşa"); Europe.add("Beşiktaş"); Europe.add("Beylikdüzü");
        Europe.add("Beyoğlu"); Europe.add("Büyükçekmece"); Europe.add("Çatalca");
        Europe.add("Esenler"); Europe.add("Esenyurt"); Europe.add("Eyüpsultan");
        Europe.add("Fatih"); Europe.add("Gaziosmanpaşa"); Europe.add("Güngören");
        Europe.add("Fatih"); Europe.add("Gaziosmanpaşa"); Europe.add("Güngören");
        Europe.add("Kâğıthane"); Europe.add("Küçükçekmece"); Europe.add("Sarıyer");
        Europe.add("Silivri"); Europe.add("Sultangazi"); Europe.add("Şişli");
        Europe.add("Zeytinburnu");

        Anatolia.add("Adalar"); Anatolia.add("Ataşehir"); Anatolia.add("Beykoz");
        Anatolia.add("Çekmeköy"); Anatolia.add("Kadıköy"); Anatolia.add("Kartal");
        Anatolia.add("Maltepe"); Anatolia.add("Pendik"); Anatolia.add("Sancaktepe");
        Anatolia.add("Sultanbeyli"); Anatolia.add("Şile"); Anatolia.add("Tuzla");
        Anatolia.add("Ümraniye"); Anatolia.add("Üsküdar");

        //Date matching----------------------------------------------------------------------------
        if(user.getMeetingPreferences().getYear() == meeting.getYear()){ //yıllar eşitse
            System.out.println("DatePoint = "+ datePoint);

            if(user.getMeetingPreferences().getMonth() == meeting.getMonth()){
                datePoint += 10;
                System.out.println("DatePoint = "+ datePoint);

                if(user.getMeetingPreferences().getDay() == meeting.getDay()){ //perfect match
                    datePoint += 10;
                    System.out.println("DatePoint = "+ datePoint);

                }
                else if(Math.abs(user.getMeetingPreferences().getDay() - meeting.getDay()) <= 7){
                    datePoint += 5;
                    System.out.println("DatePoint = "+ datePoint);

                }
                else if(Math.abs(user.getMeetingPreferences().getDay() - meeting.getDay()) > 7 && Math.abs(user.getMeetingPreferences().getDay() - meeting.getDay()) <= 14){
                    datePoint += 3;
                    System.out.println("DatePoint = "+ datePoint);

                }
                else{
                    datePoint += 0;
                }
            }

            else if(Math.abs(user.getMeetingPreferences().getMonth() - meeting.getMonth()) == 1){
                datePoint += 2;
            }
        }
        else{
            datePoint = 0;
        }

        //Time matching---------------------------------------------------------------------------------------------------
        if(user.getMeetingPreferences().getHour() == meeting.getHour()){
            timePoint += 15;
            if(user.getMeetingPreferences().getSecond() == meeting.getSecond()){ //perfect match
                timePoint = 15;
            }
        }
        else if(Math.abs(user.getMeetingPreferences().getHour() - meeting.getHour()) <= 4){
            timePoint += 10;
        }
        else if(Math.abs(user.getMeetingPreferences().getHour() - meeting.getHour()) <= 8 && Math.abs(user.getMeetingPreferences().getHour() - meeting.getHour()) > 4){
            timePoint += 5;
        }

        else{
            timePoint += 0;
        }


        //Location matching
        String userLoc = user.getMeetingPreferences().getDistrict();
        String meetLoc = meeting.getDistrict();

        if(userLoc.equals(meetLoc)){
            locPoint += 15;
        }
        else if(Europe.contains(userLoc) && Europe.contains(meetLoc)){
            locPoint +=10;
        }
        else if(Anatolia.contains(userLoc) && Anatolia.contains(meetLoc)){
            locPoint +=10;
        }
        if(Anatolia.contains(userLoc) && Anatolia.contains(meetLoc) && Popular.contains(userLoc) && Popular.contains(meetLoc)){
            System.out.println("LocPoint = " + locPoint);
            locPoint += 2;
        }
        if(Europe.contains(userLoc) && Europe.contains(meetLoc) && Popular.contains(userLoc) && Popular.contains(meetLoc)){
            locPoint += 2;
        }


        //Eating Pref matching

        if(user.getEatingPreferences().isCoffee() == meeting.getRestaurant().getEatType().isCafe()){
            eatPoint += 3;
        }
        if(user.getEatingPreferences().isDrink() == meeting.getRestaurant().getEatType().isBar()){
            eatPoint += 3;
        }
        if(user.getEatingPreferences().isFastfood() == meeting.getRestaurant().getEatType().isFastfood()){
            eatPoint += 3;
        }
        if(user.getEatingPreferences().isTraditional() == meeting.getRestaurant().getEatType().isTraditional()){
            eatPoint += 3;
        }
        if(user.getEatingPreferences().isMeat() == meeting.getRestaurant().getEatType().isMeat()){
            eatPoint += 3;
        }
        if(user.getEatingPreferences().isFish() == meeting.getRestaurant().getEatType().isFish()){
            eatPoint += 3;
        }
        if (user.getEatingPreferences().isCoffee() == meeting.getRestaurant().getEatType().isCafe()
                && user.getEatingPreferences().isDrink() == meeting.getRestaurant().getEatType().isBar()
                && user.getEatingPreferences().isFastfood() == meeting.getRestaurant().getEatType().isFastfood()
                && user.getEatingPreferences().isTraditional() == meeting.getRestaurant().getEatType().isTraditional()
                && user.getEatingPreferences().isMeat() == meeting.getRestaurant().getEatType().isMeat()
                && user.getEatingPreferences().isFish() == meeting.getRestaurant().getEatType().isFish()) {
            eatPoint += 2;
        }


        //Meeting Pref matching

        if(user.getMeetingPreferences().getRestaurant().getPlaceFeature().isAvailableForAnimals()
                == meeting.getRestaurant().getPlaceFeature().isAvailableForAnimals()){
            placePoint += 2;
        }
        if(user.getMeetingPreferences().getRestaurant().getPlaceFeature().isInnerSpace()
                == meeting.getRestaurant().getPlaceFeature().isInnerSpace()){
            placePoint += 2 ;
        }
        if(user.getMeetingPreferences().getRestaurant().getPlaceFeature().isOuterSpace()
                == meeting.getRestaurant().getPlaceFeature().isOuterSpace()){
            placePoint += 2 ;
        }
        if(user.getMeetingPreferences().getRestaurant().getPlaceFeature().isSmokingArea()
                == meeting.getRestaurant().getPlaceFeature().isSmokingArea()){
            placePoint += 2 ;
        }
        if(user.getMeetingPreferences().getRestaurant().getPlaceFeature().isWifi()
                == meeting.getRestaurant().getPlaceFeature().isWifi()){
            placePoint += 2 ;
        }

        System.out.println("Creator => " + meeting.getCreator());

//Buraya if koşulu gelecek şu puanı geçen meetin interestine bakılacak şeklidne sonrasında getUserInterestFormatch funcına yollanacak


        System.out.println("-- MATCH MATCH: " + meeting.getDistrict());
        System.out.println("-- MATCH MATCH: " + user.getMeetingPreferences().getDistrict());
        System.out.println("DatePoint = "+ datePoint);
        System.out.println("TimePoint = " + timePoint);
        System.out.println("LocPoint = " + locPoint);
        System.out.println("EatPoint = " + eatPoint);
        System.out.println("PlacePoint = " + placePoint);

        res = datePoint + timePoint + eatPoint + intPoint + locPoint + placePoint;
        if( res >= 50){
            getUserInterestFromDBforMatching(user, meeting, res);

        }
        else{
            System.out.println("First RES is smaller than 50!!!!!!");
        }

        System.out.println("++ Result => " + res);
    }

    public void getUserInterestFromDBforMatching(User user, Meeting meeting, int res){
        String creatorMail = meeting.getCreator();
        User tempUser = new User();

        CollectionReference userCollectionReference = firebaseFirestore.collection("Users");
        userCollectionReference.document(creatorMail).collection("Interests").addSnapshotListener(new EventListener<QuerySnapshot>() {
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

                        tempUser.getInterest().setArt(art);
                        tempUser.getInterest().setMusic(music);
                        tempUser.getInterest().setSports(sport);
                        tempUser.getInterest().setTechnology(tech);
                        tempUser.getInterest().setTravel(travel);

                        System.out.println("TEMP USER Interest: " + tempUser.getInterest().isArt());

                        UserMatchingCalculator(user, tempUser, meeting, res);
                    }
                }
            }
        });
    }

    public void UserMatchingCalculator(User user, User userToMatch, Meeting meeting, int res){

        int intPoint = 0;
        if(user.getInterest().isArt() == userToMatch.getInterest().isArt()){

            intPoint += 4;
            System.out.println("Intpoint1 = " + intPoint);

        }
        if(user.getInterest().isMusic() == userToMatch.getInterest().isMusic()){
            intPoint += 4;
            System.out.println("Intpoint2 = " + intPoint);

        }
        if(user.getInterest().isSports() == userToMatch.getInterest().isSports()){
            intPoint += 4;
            System.out.println("Intpoint3 = " + intPoint);

        }
        if(user.getInterest().isTechnology() == userToMatch.getInterest().isTechnology()){
            intPoint += 4;
            System.out.println("Intpoint4 = " + intPoint);

        }
        if(user.getInterest().isTravel() == userToMatch.getInterest().isTravel()){
            intPoint += 4;
            System.out.println("Intpoint5 = " + intPoint);

        }

        res += intPoint;
        System.out.println("IntpointLAST = " + intPoint);

        System.out.println("++ LAST Result => " + res);

        String datetime = String.valueOf(meeting.getDay()) + "/" + String.valueOf(meeting.getMonth()) + "/" + String.valueOf(meeting.getYear()) +
                " " + String.valueOf(meeting.getHour()) + ":" + String.valueOf(meeting.getSecond());

        meetingNameFromDB.add(meeting.getName());
        meetingDateTimeFromDB.add(datetime);
        meetingDistrictFromDB.add(meeting.getDistrict());
        meetingImageFromDB.add(meeting.getImageUrl());
        meetingRestaurantNameFromDB.add(meeting.getRestaurant().getName());

        feedRecyclerAdapter.notifyDataSetChanged();
        //buraya da en son çıkan resulta göre feedrecycle çıkar geç
    }
}