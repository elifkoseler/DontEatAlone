package com.elf.dea;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
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

public class FeedActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    ArrayList<String> usernameFromDB;
    ArrayList<String> meetingNameFromDB;
    ArrayList<String> meetingImageFromDB;
    ArrayList<String> meetingDateTimeFromDB;
    ArrayList<String> meetingDistrictFromDB;
    ArrayList<String> meetingRestaurantNameFromDB;

    ArrayList<String> allMails;
    FeedRecyclerAdapter feedRecyclerAdapter;
    TextView meetingText;


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

            Intent intentToUpload = new Intent(FeedActivity.this, CreateMeetingActivity.class);
            startActivity(intentToUpload);

        }
        else if(item.getItemId() == R.id.SignOut){

            firebaseAuth.signOut();

            Intent intentToSignUp = new Intent(FeedActivity.this,SignUpActivity.class);
            startActivity(intentToSignUp);
        }
        else if(item.getItemId() == R.id.MyMeetings){
            Intent intent = new Intent(FeedActivity.this,MyMeetingsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        recyclerView = findViewById(R.id.recyclerView);

        usernameFromDB = new ArrayList<>();
        meetingNameFromDB = new ArrayList<>();
        meetingImageFromDB = new ArrayList<>();
        meetingDateTimeFromDB = new ArrayList<>();
        meetingDistrictFromDB = new ArrayList<>();
        meetingRestaurantNameFromDB = new ArrayList<>();


        allMails = new ArrayList<>();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = firebaseFirestore.getInstance();

        user = (User) getIntent().getSerializableExtra("user"); // get ref of user from other activities
        getMailDataFromFirestore();

        feedRecyclerAdapter = new FeedRecyclerAdapter(meetingNameFromDB, meetingRestaurantNameFromDB, meetingDateTimeFromDB, meetingDistrictFromDB, meetingImageFromDB);

        recyclerView.setAdapter(feedRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    public void getMailDataFromFirestore(){ //datayı firestoredan alma
        //firebaseFirestore.collection().addSnapShotListener(same things here) //daha basit yöntem referencelamaktansa

        //CollectionReference userCollectionReference = firebaseFirestore.collection("User");
        //collectionReference.whereEqualTo("comment","mona lisa").addSnapshotListener(new EventListener<QuerySnapshot>() {
        // databaseden çekerken filtrelemek istersen equalTo kullanımı

        //zamana göre sıralı dbden çekmek
       /* collectionReference.orderBy("create date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {

                if(error != null){
                    Toast.makeText(FeedActivity.this, error.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                }

                if(queryDocumentSnapshots != null) {

                    for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){

                        Map<String, Object> data = snapshot.getData();
                        //casting
                        String comment = (String) data.get("comment");
                        String userEmail = (String) data.get("useremail");
                        String downloadUrl = (String) data.get("downloadurl");

                        //System.out.println(comment);

                        meetingNameFromDB.add(comment);
                        usernameFromDB.add(userEmail);
                        meetingImageFromDB.add(downloadUrl);


                    }
                }
            }
        });*/


        System.out.println("getDataFromFB methoduna girdi > mail getirme kısmı");

        CollectionReference usermailCollectionReference = firebaseFirestore.collection("UserMails");
        usermailCollectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                if (value != null)
                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                        Map <String, Object> mailData = snapshot.getData();

                        String mail = (String) mailData.get("mail");
                        //System.out.println("Tüm mailler: " + mail);
                        getDataFromFirestore(mail);

                        allMails.add(mail);
                        //System.out.println("allMails" + allMails.get(0));

                    }
            }

        });

    }

    public void getDataFromFirestore(String mail){
        System.out.println("getDataFromFB methoduna girdi");
        //System.out.println("allMails" + allMails.get(0));
        CollectionReference userCollectionReference = firebaseFirestore.collection("Meetings");

            userCollectionReference.document(mail).collection("Meeting Info").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                    if (error != null) {
                        Toast.makeText(FeedActivity.this, error.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
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