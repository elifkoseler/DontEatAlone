package com.elf.dea;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.elf.dea.UserData.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class UserPreferenceActivity extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_preference);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }


    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.MeatCheckBox:
                if (checked)
                    user.eatingPreferences.setMeat(true);
                else
                    user.eatingPreferences.setMeat(false);
                break;
            case R.id.FishCheckBox:
                if (checked)
                    user.eatingPreferences.setFish(true);
                else
                    user.eatingPreferences.setFish(false);
                break;
            case R.id.FastFoodCheckBox:
                if (checked)
                    user.eatingPreferences.setFastfood(true);
                else
                    user.eatingPreferences.setFastfood(false);
                break;
            case R.id.TraditionalCheckBox:
                if (checked)
                    user.eatingPreferences.setTraditional(true);
                else
                    user.eatingPreferences.setTraditional(false);
                break;
            case R.id.CoffeeCheckBox:
                if (checked)
                    user.eatingPreferences.setCoffee(true);
                else
                    user.eatingPreferences.setCoffee(false);
                break;
            case R.id.DrinkCheckBox:
                if (checked)
                    user.eatingPreferences.setDrink(true);
                else
                    user.eatingPreferences.setDrink(false);
                break;

        }
    }

    public void Save(View view){

        HashMap<String, Object> data = new HashMap<>();
        data.put("isMeat", user.eatingPreferences.isMeat());
        data.put("isFish", user.eatingPreferences.isFish());
        data.put("isFastFood", user.eatingPreferences.isFastfood());
        data.put("isTraditional", user.eatingPreferences.isTraditional());
        data.put("isCoffee", user.eatingPreferences.isCoffee());
        data.put("isDrink", user.eatingPreferences.isDrink());

        user.email = firebaseAuth.getCurrentUser().getEmail();
        System.out.println(user.email);


        firebaseFirestore.collection("Users").document(user.email).collection("Eating Preferences")
                .add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                System.out.println("Eating Pref dbye eklendi.");
                Intent intent = new Intent(UserPreferenceActivity.this, InterestPreferencesActivity.class);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UserPreferenceActivity.this, e.getLocalizedMessage().toString()
                ,Toast.LENGTH_LONG).show();
            }
        });



        Toast.makeText(UserPreferenceActivity.this, "Next", Toast.LENGTH_LONG).show();
    }

    public void Back(View view) {
        System.out.println("Back butonuna basıldı!");
        Intent intent = new Intent(UserPreferenceActivity.this, ProfileActivity.class);
        startActivity(intent);
    }

    public void EditLater(View view){
        System.out.println("EditLater butonun basıldı!");
        Intent intent = new Intent(UserPreferenceActivity.this, FeedActivity.class);
        startActivity(intent);
    }

}