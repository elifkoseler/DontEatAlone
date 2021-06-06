package com.elf.dea;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.elf.dea.MeetingData.Meeting;
import com.elf.dea.UserData.User;

public class CreateMeetingRestaurantDetailActivity extends AppCompatActivity {
    User user;
    Meeting meeting;

    EditText expenseText;
    EditText addressText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_meeting_restaurant_detail);

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
    }
}