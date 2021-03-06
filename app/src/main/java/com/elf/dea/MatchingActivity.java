package com.elf.dea;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elf.dea.MeetingData.Meeting;
import com.elf.dea.UserData.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MatchingActivity extends AppCompatActivity implements FeedRecyclerAdapter.RecyclerViewClickListener {
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
    ArrayList<String> meetingCreators;
    ArrayList<Integer> resultList;

    ArrayList<Meeting> meetingList;
    ArrayList<Meeting> tempMeetingList;
    ArrayList<User> userList;
    ArrayList<Integer> numberOfParticipantList;

    User user;

    int result;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //menuyu ba??lamak i??in

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.options_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) { //itemler ne yapacak yazmak i??in

        if(item.getItemId() == R.id.CreateMeeting){

            Intent intentToUpload = new Intent(MatchingActivity.this, CreateMeetingActivity.class);
            startActivity(intentToUpload);

        }
        else if(item.getItemId() == R.id.SignOut){

            firebaseAuth.signOut();

            Intent intentToSignUp = new Intent(MatchingActivity.this, SignUpActivity.class);
            startActivity(intentToSignUp);
        }
        else if(item.getItemId() == R.id.MyProfile){
            Intent intent = new Intent(MatchingActivity.this, MyProfileActivity.class);
            startActivity(intent);
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
        else if(item.getItemId() == R.id.JoinedMeetings){
            Intent intent = new Intent(MatchingActivity.this, JoinedMeetingActivity.class);
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
        meetingCreators = new ArrayList<>();
        allMails = new ArrayList<>();

        userList = new ArrayList<>();
        meetingList = new ArrayList<>();
        tempMeetingList = new ArrayList<>();

        resultList = new ArrayList<>();
        numberOfParticipantList = new ArrayList<>();

        user = new User();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = firebaseFirestore.getInstance();

        //user = (User) getIntent().getSerializableExtra("user"); // get ref of user from other activities //??urda di??erlerinden almak daha kolay olabilir u??ra??mamak ad??na? bi yerde mutlaka ??ekmek gerekecek ama?

        getUserEatingPreferenceFromDB();

        feedRecyclerAdapter = new FeedRecyclerAdapter(meetingNameFromDB, meetingRestaurantNameFromDB,
                meetingDateTimeFromDB, meetingDistrictFromDB, meetingImageFromDB, this);

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
                    //    System.out.println("__get EAT??NG: " + user.getEatingPreferences().isCoffee());


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
                   //     System.out.println("__get MEET??NG: " + user.getEatingPreferences().isCoffee());


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
                      //  System.out.println("__get INTEREST: " + user.getEatingPreferences().isCoffee());


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

                      //  System.out.println("__get AVA??LABLE: " + user.getEatingPreferences().isCoffee());


                    }
                }
            }
        });
    }



    public void getMailDataFromFirestore(User user){
        System.out.println("MATCHING >> getDataFromFB methoduna girdi > mail getirme k??sm??");

        CollectionReference usermailCollectionReference = firebaseFirestore.collection("UserMails");
        usermailCollectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                if (value != null)
                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                        Map<String, Object> mailData = snapshot.getData();

                        String mail = (String) mailData.get("mail");
                        //System.out.println("T??m mailler: " + mail);
                        getAllMeetingDataFromFirestore(mail, user);

                        allMails.add(mail);
                        //System.out.println("allMails" + allMails.get(0));

                    }
            }

        });


    }
    // meetinge yolla useri zaten user tek

    public void getAllMeetingDataFromFirestore(String mail, User user) {
      //  System.out.println("MATCHING >> getDataFromFB methoduna girdi");
        System.out.println("MA??L: " + mail);

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

                        //System.out.println("__get INTERESTMEET: " + user.getEatingPreferences().isCoffee());


                    }
                }

            }
        });

    }


    public void getMeetingRestaurantFromDB(User user, Meeting meeting, String mail){
      //  System.out.println("+++ Second Meeting Func =>  " + meeting.getDistrict());

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

                        meeting.getRestaurant().getPlaceFeature().setAvailableForAnimals(hasAnimal); //dbye yanl???? basm??????z creatingmeeting detailde
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

                        //burada kald??n dbden t??m meet rest ??zelliklerini ??ekip a??a????da matchlemeyi dene
                        //sonras??nda bu ????kan puanlar??n meetleriyle creatora gidip userin interestlerini kar????la??t??r??caz.
                        //en son feedrecycle i??in bas??caz feed ka?? puan ??st?? olacak hen??z karar veremedim

                        //System.out.println("-- LAST MATCH DB: " + meeting.getDistrict());
                       // System.out.println("-- LAST : user:  " + user.getEatingPreferences().isCoffee());


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

        Popular.add("Be??ikta??"); Popular.add("Kad??k??y"); Popular.add("Bak??rk??y"); Popular.add("??i??li"); Popular.add("Fatih");

        Europe.add("Arnavutk??y"); Europe.add("Avc??lar"); Europe.add("Ba??c??lar");
        Europe.add("Bah??elievler"); Europe.add("Bak??rk??y"); Europe.add("Ba??ak??ehir");
        Europe.add("Bayrampa??a"); Europe.add("Be??ikta??"); Europe.add("Beylikd??z??");
        Europe.add("Beyo??lu"); Europe.add("B??y??k??ekmece"); Europe.add("??atalca");
        Europe.add("Esenler"); Europe.add("Esenyurt"); Europe.add("Ey??psultan");
        Europe.add("Fatih"); Europe.add("Gaziosmanpa??a"); Europe.add("G??ng??ren");
        Europe.add("Fatih"); Europe.add("Gaziosmanpa??a"); Europe.add("G??ng??ren");
        Europe.add("K??????thane"); Europe.add("K??????k??ekmece"); Europe.add("Sar??yer");
        Europe.add("Silivri"); Europe.add("Sultangazi"); Europe.add("??i??li");
        Europe.add("Zeytinburnu");

        Anatolia.add("Adalar"); Anatolia.add("Ata??ehir"); Anatolia.add("Beykoz");
        Anatolia.add("??ekmek??y"); Anatolia.add("Kad??k??y"); Anatolia.add("Kartal");
        Anatolia.add("Maltepe"); Anatolia.add("Pendik"); Anatolia.add("Sancaktepe");
        Anatolia.add("Sultanbeyli"); Anatolia.add("??ile"); Anatolia.add("Tuzla");
        Anatolia.add("??mraniye"); Anatolia.add("??sk??dar");

        //Date matching----------------------------------------------------------------------------
        if(user.getMeetingPreferences().getYear() == meeting.getYear()){ //y??llar e??itse
            //System.out.println("DatePoint = "+ datePoint);

            if(user.getMeetingPreferences().getMonth() == meeting.getMonth()){
                datePoint += 10;
               // System.out.println("DatePoint = "+ datePoint);

                if(user.getMeetingPreferences().getDay() == meeting.getDay()){ //perfect match
                    datePoint += 10;
                //    System.out.println("DatePoint = "+ datePoint);

                }
                else if(Math.abs(user.getMeetingPreferences().getDay() - meeting.getDay()) <= 7){
                    datePoint += 5;
                  //  System.out.println("DatePoint = "+ datePoint);

                }
                else if(Math.abs(user.getMeetingPreferences().getDay() - meeting.getDay()) > 7 && Math.abs(user.getMeetingPreferences().getDay() - meeting.getDay()) <= 14){
                    datePoint += 3;
                  //  System.out.println("DatePoint = "+ datePoint);

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
       //     System.out.println("LocPoint = " + locPoint);
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

//Buraya if ko??ulu gelecek ??u puan?? ge??en meetin interestine bak??lacak ??eklidne sonras??nda getUserInterestFormatch func??na yollanacak


        System.out.println("-- Meeting District: " + meeting.getDistrict());
        System.out.println("-- User District: " + user.getMeetingPreferences().getDistrict());
        System.out.println("DatePoint = "+  meeting.getName()+" ("+meeting.getDistrict() + ") :"+ datePoint);
        System.out.println("TimePoint = " +  meeting.getName()+" ("+meeting.getDistrict() + ") :"+  timePoint);
        System.out.println("LocPoint = " + meeting.getName()+" ("+meeting.getDistrict() + ") :"+  locPoint);
        System.out.println("EatPoint = " + meeting.getName()+" ("+meeting.getDistrict() + ") :"+ eatPoint);
        System.out.println("PlacePoint = " + meeting.getName()+" ("+meeting.getDistrict() + ") :"+  placePoint);

        res = datePoint + timePoint + eatPoint + intPoint + locPoint + placePoint;
        if( res >= 50 && meeting.getCreator() != firebaseAuth.getCurrentUser().getEmail()){
            getUserInterestFromDBforMatching(user, meeting, res);

        }
        else{
            System.out.println("First Result is smaller than 50!! =>"+ meeting.getName()+" ("+meeting.getDistrict() + ") :");
        }

        System.out.println("++ First Result => " + res);
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

                       // System.out.println("TEMP USER Interest: " + tempUser.getInterest().isArt());

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
            //System.out.println("Intpoint1 = " + intPoint);

        }
        if(user.getInterest().isMusic() == userToMatch.getInterest().isMusic()){
            intPoint += 4;
            //System.out.println("Intpoint2 = " + intPoint);

        }
        if(user.getInterest().isSports() == userToMatch.getInterest().isSports()){
            intPoint += 4;
            //System.out.println("Intpoint3 = " + intPoint);

        }
        if(user.getInterest().isTechnology() == userToMatch.getInterest().isTechnology()){
            intPoint += 4;
           // System.out.println("Intpoint4 = " + intPoint);

        }
        if(user.getInterest().isTravel() == userToMatch.getInterest().isTravel()){
            intPoint += 4;
            //System.out.println("Intpoint5 = " + intPoint);

        }

        res += intPoint;
        System.out.println("Interest Point = " + meeting.getName()+" ("+meeting.getDistrict() + ") :"+ intPoint);

        System.out.println("++ LAST Result => "+ meeting.getName()+" ("+meeting.getDistrict() + ") vs " + user.getMeetingPreferences().getDistrict() + ": "+ res);

        String datetime = String.valueOf(meeting.getDay()) + "/" + String.valueOf(meeting.getMonth()) + "/" + String.valueOf(meeting.getYear()) +
                " " + String.valueOf(meeting.getHour()) + ":" + String.valueOf(meeting.getSecond());


        result = res;
        resultList.add(res);
        numberOfParticipantList.add(meeting.getNumberOfParticipant());

        if(!meeting.getCreator().equals(firebaseAuth.getCurrentUser().getEmail())){
            meetingCreators.add(meeting.getCreator());
            meetingNameFromDB.add(meeting.getName());
            meetingDateTimeFromDB.add(datetime);
            meetingDistrictFromDB.add(meeting.getDistrict());
            meetingImageFromDB.add(meeting.getImageUrl());
            meetingRestaurantNameFromDB.add(meeting.getRestaurant().getName());

        }
        feedRecyclerAdapter.notifyDataSetChanged();
        //buraya da en son ????kan resulta g??re feedrecycle ????kar ge??
    }

    @Override
    public void recyclerViewListClicked(View v, int position) {
        if(v instanceof Button){
        System.out.println("Button " + meetingNameFromDB.get(position));
            join(meetingCreators.get(position),position);
        }

        else{
        System.out.println("View " + meetingNameFromDB.get(position));

        Intent intent = new Intent(MatchingActivity.this, MatchedMeetingActivity.class);
        intent.putExtra("position", position);
        intent.putExtra("meetingNameFromDB", meetingNameFromDB);
        intent.putExtra("meetingDateTimeFromDB", meetingDateTimeFromDB);
        intent.putExtra("meetingRestaurantNameFromDB",meetingRestaurantNameFromDB);
        intent.putExtra("meetingDistrictFromDB", meetingDistrictFromDB);
        intent.putExtra("meetingImageFromDB", meetingImageFromDB);
        intent.putExtra("meetingCreators", meetingCreators);
        intent.putExtra("result", resultList);

        startActivity(intent);
        }
    }

    public void join(String creator, int position) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("participant", firebaseAuth.getCurrentUser().getEmail());

        HashMap<String, Object> meetingData = new HashMap<>();
        meetingData.put("meeting name", meetingNameFromDB.get(position));
        meetingData.put("creator of meeting", creator);

        if (creator.equals(firebaseAuth.getCurrentUser().getEmail())) {
            AlertDialog alertDialog1 = new AlertDialog.Builder(MatchingActivity.this).create();
            alertDialog1.setTitle("YOU CAN'T JOIN THIS MEETING");
            alertDialog1.setMessage("It is already yours!");
            alertDialog1.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog1, int which) {
                    Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
                }
            });
            alertDialog1.show();

        } else if (!creator.equals(firebaseAuth.getCurrentUser().getEmail())){
            firebaseFirestore.collection("Meetings").document(creator).collection("Participants")
                    .add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    System.out.println("Participant: " + firebaseAuth.getCurrentUser().getEmail() + " added to: " + meetingNameFromDB.get(position));

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MatchingActivity.this, e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();

                }
            });

            firebaseFirestore.collection("Users").document(firebaseAuth.getCurrentUser().getEmail()).collection("Meetings I've joined")
                    .add(meetingData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {

                    System.out.println("Participant: " + firebaseAuth.getCurrentUser().getEmail() + " added to: " + meetingNameFromDB.get(position));
                    AlertDialog alertDialog = new AlertDialog.Builder(MatchingActivity.this).create();
                    alertDialog.setTitle("Successfully");
                    alertDialog.setMessage("You have joined the meeting!");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
                        }
                    });

                    alertDialog.show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MatchingActivity.this, e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();

                }
            });

        }
    }
}