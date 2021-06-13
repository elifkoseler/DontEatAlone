package com.elf.dea;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.elf.dea.UserData.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class InterestPreferencesActivity extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest_preferences);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        user = (User)getIntent().getSerializableExtra("user");
        System.out.println("InterestActivity: " + user.getName());

    }

    public void onCheckboxClicked(View view){
        boolean checked = ((CheckBox) view).isChecked();
        System.out.println("Checked: "+ checked);

        switch(view.getId()){
            case R.id.travelCheckBox:
                if(checked)
                    user.interest.setTravel(true);
                else
                    user.interest.setTravel(false);
                break;
            case R.id.musicCheckBox:
                if(checked)
                    user.interest.setMusic(true);
                else
                    user.interest.setMusic(false);
                break;
            case R.id.sportsCheckBox:
                if(checked)
                    user.interest.setSports(true);
                else
                    user.interest.setSports(false);
                break;
            case R.id.artCheckBox:
                if(checked)
                    user.interest.setArt(true);
                else
                    user.interest.setArt(false);
                break;
            case R.id.techCheckBox:
                if(checked)
                    user.interest.setTechnology(true);
                else
                    user.interest.setTechnology(false);
                break;
        }

        System.out.println(user.interest.isArt());
    }

    public void Save(View view){
        HashMap<String, Object> data = new HashMap<>();
        user.setEmail(firebaseAuth.getCurrentUser().getEmail());

        data.put("isTravel", user.interest.isTravel());
        data.put("isMusic", user.interest.isMusic());
        data.put("isSports", user.interest.isSports());
        data.put("isArt", user.interest.isArt());
        data.put("isTech", user.interest.isTechnology());

        firebaseFirestore.collection("Users").document(user.getEmail()).collection("Interests")
                .add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                System.out.println("Interests dbye eklendi");
                Intent intent = new Intent(InterestPreferencesActivity.this, MeetingPreferencesActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(InterestPreferencesActivity.this, e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });

    }

    public void EditLater(View view){
        System.out.println("EditLater butonun basıldı!");
        Intent intent = new Intent(InterestPreferencesActivity.this, StartActivity.class);
        startActivity(intent);

    }

    public void Back(View view){
        System.out.println("Back butonuna basıldı!");
        Intent intent = new Intent(InterestPreferencesActivity.this, EatingPreferenceActivity.class);
        startActivity(intent);
    }
}