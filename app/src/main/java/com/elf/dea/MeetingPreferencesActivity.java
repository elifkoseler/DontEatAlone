package com.elf.dea;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.elf.dea.MeetingData.Meeting;
import com.elf.dea.UserData.Interest;
import com.elf.dea.UserData.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class MeetingPreferencesActivity extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_preferences);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }
    public void onCheckboxClicked(View view){
        boolean checked = ((CheckBox) view).isChecked();
        System.out.println("Checked: "+ checked);

        switch(view.getId()){
            case R.id.innerCheckBox:
                if(checked)
                    user.meetingPreferences.getRestaurant().getPlaceFeature().setInnerSpace(true);
                else
                    user.meetingPreferences.getRestaurant().getPlaceFeature().setInnerSpace(false);
                break;
            case R.id.outerCheckBox:
                if(checked)
                    user.meetingPreferences.getRestaurant().getPlaceFeature().setOuterSpace(true);
                else
                    user.meetingPreferences.getRestaurant().getPlaceFeature().setOuterSpace(false);
                break;
            case R.id.smokingCheckBox:
                if(checked)
                    user.meetingPreferences.getRestaurant().getPlaceFeature().setSmokingArea(true);
                else
                    user.meetingPreferences.getRestaurant().getPlaceFeature().setSmokingArea(false);
                break;
            case R.id.animalCheckBox:
                if(checked)
                    user.meetingPreferences.getRestaurant().getPlaceFeature().setAvailableForAnimals(true);
                else
                    user.meetingPreferences.getRestaurant().getPlaceFeature().setAvailableForAnimals(false);
                break;
            case R.id.wifiCheckBox:
                if(checked)
                    user.meetingPreferences.getRestaurant().getPlaceFeature().setWifi(true);
                else
                    user.meetingPreferences.getRestaurant().getPlaceFeature().setWifi(false);
                break;
        }
    }

    public void Save(View view){
        HashMap<String, Object> data = new HashMap<>();
        user.email = firebaseAuth.getCurrentUser().getEmail();

        data.put("hasInnerSpace", user.meetingPreferences.getRestaurant().getPlaceFeature().isInnerSpace());
        data.put("hasOuterSpace", user.meetingPreferences.getRestaurant().getPlaceFeature().isOuterSpace());
        data.put("hasSmokingArea", user.meetingPreferences.getRestaurant().getPlaceFeature().isSmokingArea());
        data.put("hasWifi", user.meetingPreferences.getRestaurant().getPlaceFeature().isWifi());
        data.put("hasAnimal", user.meetingPreferences.getRestaurant().getPlaceFeature().isAvailableForAnimals());

        firebaseFirestore.collection("Users").document(user.email).collection("Meeting Preferences").document("Restaurant").collection("Place Features")
                .add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                System.out.println("Meeting pref dbye eklendi");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MeetingPreferencesActivity.this, e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });

    }


    public void EditLater(View view){
        System.out.println("EditLater butonun basıldı!");
        Intent intent = new Intent(MeetingPreferencesActivity.this, FeedActivity.class);
        startActivity(intent);

    }

    public void Back(View view){
        System.out.println("Back butonuna basıldı!");
        Intent intent = new Intent(MeetingPreferencesActivity.this, InterestPreferencesActivity.class);
        startActivity(intent);
    }
}