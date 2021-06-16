package com.elf.dea;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.elf.dea.UserData.User;
import com.google.firebase.auth.FirebaseAuth;

public class StartActivity extends AppCompatActivity {
    User user = new User(); //içinde hiçbir şey yok
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //menuyu bağlamak için

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.options_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) { //itemler ne yapacak yazmak için

        if(item.getItemId() == R.id.CreateMeeting){

            Intent intentToUpload = new Intent(StartActivity.this, CreateMeetingActivity.class);
            startActivity(intentToUpload);

        }
        else if(item.getItemId() == R.id.SignOut){

            firebaseAuth.signOut();

            Intent intentToSignUp = new Intent(StartActivity.this, SignUpActivity.class);
            startActivity(intentToSignUp);
        }
        else if(item.getItemId() == R.id.EditProfile){
            //yazılacak
        }
        else if(item.getItemId() == R.id.ShowAll) {
            Intent intent = new Intent(StartActivity.this, FeedActivity.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.ShowMatches) {
            Intent intent = new Intent(StartActivity.this, MatchingActivity.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.MyMeetings) {
            Intent intent = new Intent(StartActivity.this, MyMeetingsActivity.class);
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
    }

    public void CreateActivity(View view){
        Intent intent = new Intent(StartActivity.this, CreateMeetingActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    public void ShowAll(View view){
        Intent intent = new Intent(StartActivity.this, MatchingActivity.class);
        startActivity(intent);
    }
}