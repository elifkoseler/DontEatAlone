package com.elf.dea;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.elf.dea.UserData.User;

public class AvailabilityActivity extends AppCompatActivity {
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_availability);

        user = (User)getIntent().getSerializableExtra("user");

    }

    public void Save(View view){
        System.out.println("Takvim ayarlanacak");
        Intent intent = new Intent(AvailabilityActivity.this, FeedActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);

    }
    public void EditLater(View view){
        System.out.println("EditLater butonun basıldı!");
        Intent intent = new Intent(AvailabilityActivity.this, FeedActivity.class);
        startActivity(intent);

    }

    public void Back(View view){
        System.out.println("Back butonuna basıldı!");
        Intent intent = new Intent(AvailabilityActivity.this, MeetingPreferencesActivity.class);
        startActivity(intent);
    }
}