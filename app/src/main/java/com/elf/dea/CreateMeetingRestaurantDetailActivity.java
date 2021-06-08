package com.elf.dea;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.elf.dea.MeetingData.Meeting;
import com.elf.dea.UserData.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;



import java.util.HashMap;

public class CreateMeetingRestaurantDetailActivity extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    User user;
    Meeting meeting;

    EditText expenseText;
    EditText addressText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_meeting_restaurant_detail);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        user = (User) getIntent().getSerializableExtra("user"); // get ref of user from other activities
        meeting = (Meeting) getIntent().getSerializableExtra("meeting");

        expenseText = findViewById(R.id.editTextExpense);
        expenseText.setInputType(InputType.TYPE_NULL);
        addressText = findViewById(R.id.editTextAddress);

        String[] expenses = new String[]{
                "₺",
                "₺₺",
                "₺₺₺"
        };

        expenseText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(CreateMeetingRestaurantDetailActivity.this);
                mBuilder.setTitle("Please choose the average expense of the restaurant");
                mBuilder.setSingleChoiceItems(expenses, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        expenseText.setText(expenses[i]);
                        System.out.println("Average expense: " + expenses[i]);
                       // location = expenses[i];
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });

    }
    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();
        //System.out.println(user.getName());
        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.meatCheckBox2:
                if (checked)
                    meeting.getRestaurant().getEatType().setMeat(true);
                else
                    meeting.getRestaurant().getEatType().setMeat(false);
                break;
            case R.id.fishCheckBox2:
                if (checked)
                    meeting.getRestaurant().getEatType().setFish(true);
                else
                    meeting.getRestaurant().getEatType().setFish(false);
                break;
            case R.id.fastFoodCheckBox2:
                if (checked)
                    meeting.getRestaurant().getEatType().setFastfood(true);
                else
                    meeting.getRestaurant().getEatType().setFastfood(false);
                break;
            case R.id.traditionalCheckBox2:
                if (checked)
                    meeting.getRestaurant().getEatType().setTraditional(true);
                else
                    meeting.getRestaurant().getEatType().setTraditional(false);
                break;
            case R.id.cafeCheckBox:
                if (checked)
                    meeting.getRestaurant().getEatType().setCafe(true);
                else
                    meeting.getRestaurant().getEatType().setCafe(false);
                break;
            case R.id.barCheckBox:
                if (checked)
                    meeting.getRestaurant().getEatType().setBar(true);
                else
                    meeting.getRestaurant().getEatType().setBar(false);
                break;
            //////////////////////////////////////////////////////////////////////////////
            case R.id.animalCheckBox2:
                if (checked)
                    meeting.getRestaurant().getPlaceFeature().setAvailableForAnimals(true);
                else
                    meeting.getRestaurant().getPlaceFeature().setAvailableForAnimals(false);
                break;
            case R.id.innerCheckBox2:
                if (checked)
                    meeting.getRestaurant().getPlaceFeature().setInnerSpace(true);
                else
                    meeting.getRestaurant().getPlaceFeature().setInnerSpace(false);
                break;
            case R.id.outerCheckBox2:
                if (checked)
                    meeting.getRestaurant().getPlaceFeature().setOuterSpace(true);
                else
                    meeting.getRestaurant().getPlaceFeature().setOuterSpace(false);
                break;
            case R.id.smokingCheckBox2:
                if (checked)
                    meeting.getRestaurant().getPlaceFeature().setSmokingArea(true);
                else
                    meeting.getRestaurant().getPlaceFeature().setSmokingArea(false);
                break;
            case R.id.wifiCheckBox2:
                if (checked)
                    meeting.getRestaurant().getPlaceFeature().setWifi(true);
                else
                    meeting.getRestaurant().getPlaceFeature().setWifi(false);
                break;
        }
    }

    public void Skip(View view){
        Intent intent = new Intent(CreateMeetingRestaurantDetailActivity.this, FeedActivity.class);
        intent.putExtra("user", user);
        intent.putExtra("meeting", meeting);
        startActivity(intent);
    }

    public void Create(View view){
        System.out.println("Create butonuna basıldı !!");
        System.out.println(meeting.getRestaurant().getName());
        System.out.println(meeting.getImageUrl());

        HashMap<String, Object> data = new HashMap<>();
        //user.setEmail(firebaseAuth.getCurrentUser().getEmail());
        System.out.println(firebaseAuth.getCurrentUser().getEmail());
        System.out.println(user.getEmail());
        System.out.println(user.getName());

        data.put("name", meeting.getName());
        data.put("year", meeting.getYear());
        data.put("month", meeting.getMonth());
        data.put("day", meeting.getDay());
        data.put("hour", meeting.getHour());
        data.put("second", meeting.getSecond());
        data.put("district", meeting.getDistrict());
        data.put("address", meeting.getAddress());

        /*LocalDate localDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        System.out.println(localDate.format(formatter));
        //String docname = meeting.getName() + localDate.format(formatter);*/
        String docname =  meeting.getName();

        firebaseFirestore.collection("Meetings").document(docname).collection("Meeting Info")
                .add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                System.out.println("Meeting Info Dbye eklendi!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CreateMeetingRestaurantDetailActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();

            }
        });
/*
        HashMap<String, Object> restaurantData = new HashMap<>();

        restaurantData.put("name", meeting.getRestaurant().getName());
        restaurantData.put("expenses", meeting.getRestaurant().getAverageExpenses());

        restaurantData.put("hasInnerSpace", meeting.getRestaurant().getPlaceFeature().isInnerSpace());
        restaurantData.put("hasOuterSpace", meeting.getRestaurant().getPlaceFeature().isOuterSpace());
        restaurantData.put("hasSmokingArea", meeting.getRestaurant().getPlaceFeature().isSmokingArea());
        restaurantData.put("hasInnerSpace", meeting.getRestaurant().getPlaceFeature().isAvailableForAnimals());
        restaurantData.put("hasWifi", meeting.getRestaurant().getPlaceFeature().isWifi());

        restaurantData.put("isMeat", meeting.getRestaurant().getEatType().isMeat());
        restaurantData.put("isFish", meeting.getRestaurant().getEatType().isFish());
        restaurantData.put("isBar", meeting.getRestaurant().getEatType().isBar());
        restaurantData.put("isCafe", meeting.getRestaurant().getEatType().isCafe());
        restaurantData.put("isFastFood", meeting.getRestaurant().getEatType().isFastfood());
        restaurantData.put("isTraditional", meeting.getRestaurant().getEatType().isTraditional());


        firebaseFirestore.collection("Meetings").document(docname).collection("Restaurant")
                .add(restaurantData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                System.out.println("Meeting > Restaurant DBye eklendi!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @org.jetbrains.annotations.NotNull Exception e) {
                Toast.makeText(CreateMeetingRestaurantDetailActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();

            }
        });


        //Restaurant databaseine destek olma amaçlı
        firebaseFirestore.collection("Restaurants").document(meeting.getRestaurant().getName()).collection("Restaurant Info")
                .add(restaurantData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                System.out.println("Restaurant > Restaurant DBye eklendi!");

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                System.out.println(e.getLocalizedMessage().toString());
            }
        });
*/
        System.out.println("DB işlemleri DONE");

    }
}