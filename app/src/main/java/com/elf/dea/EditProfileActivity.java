package com.elf.dea;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;

import com.elf.dea.MeetingData.Meeting;
import com.elf.dea.UserData.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class EditProfileActivity extends AppCompatActivity {
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    User user;
    Meeting meeting;
    boolean meat, fish, drink, traditional, fastfood, coffee;
    boolean animal, inner, outer, smoke, wifi;

    String mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        user = new User();
        mail = firebaseAuth.getCurrentUser().getEmail();

    }
    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();
        //System.out.println(user.getName());
        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.meatCheckBox3:
                if (checked)
                    meat = true;
                else
                    meat = false;
                break;
            case R.id.fishCheckBox3:
                if (checked)
                    fish = true;
                else
                    fish = false;
                break;
            case R.id.fastFoodCheckBox3:
                if (checked)
                    fastfood = true;
                else
                    fastfood = false;
                break;
            case R.id.traditionalCheckBox3:
                if (checked)
                    traditional = true;
                else
                    traditional = false;
                break;
            case R.id.coffeeCheckBox3:
                if (checked)
                    coffee = true;
                else
                    coffee = false;
                break;
            case R.id.drinkCheckBox3:
                if (checked)
                    drink = true;
                else
                    drink = false;
                break;
            //////////////////////////////////////////////////////////////////////////////
            case R.id.animalCheckBox3:
                if (checked)
                    animal = true;
                else
                    animal = false;
                break;
            case R.id.innerCheckBox3:
                if (checked)
                    inner = true;
                else
                    inner = false;
                break;
            case R.id.outerCheckBox3:
                if (checked)
                    outer = true;
                else
                    outer = false;
                break;
            case R.id.smokingCheckBox3:
                if (checked)
                    smoke = true;
                else
                    smoke = false;
                break;
            case R.id.wifiCheckBox3:
                if (checked)
                    wifi = true;
                else
                    wifi = false;
                break;
        }
    }
    public void Submit(View view){
        CollectionReference colRefEat = firebaseFirestore.collection("Users").document(mail).collection("Eating Preferences");
        CollectionReference colRefMeet = firebaseFirestore.collection("Users").document(mail).collection("Meeting Preferences")
                .document("Restaurant").collection("Place Features");

        colRefEat.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot snap:
                     queryDocumentSnapshots) {
                    System.out.println(snap.getData() + " : " + snap.getId());
                    colRefEat.document(snap.getId()).update("isFish",fish).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                        }
                    });
                    colRefEat.document(snap.getId()).update("isMeat",meat).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                        }
                    });
                    colRefEat.document(snap.getId()).update("isTraditional",traditional).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                        }
                    });

                    colRefEat.document(snap.getId()).update("isFastFood",fastfood).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                        }
                    });
                    colRefEat.document(snap.getId()).update("isDrink",drink).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                        }
                    });

                    colRefEat.document(snap.getId()).update("isCoffee",coffee).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                        }
                    });

                }
            }
        });

        colRefMeet.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot snap :
                        queryDocumentSnapshots) {
                    System.out.println(snap.getData() + " : " + snap.getId());

                    colRefMeet.document(snap.getId()).update("hasAnimal",animal).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                        }
                    });

                    colRefMeet.document(snap.getId()).update("hasInnerSpace",inner).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                        }
                    });

                    colRefMeet.document(snap.getId()).update("hasOuterSpace",outer).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                        }
                    });

                    colRefMeet.document(snap.getId()).update("hasSmokingArea",smoke).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                        }
                    });
                    colRefMeet.document(snap.getId()).update("hasWifi",wifi).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                        }
                    });
                }
            }
        });

        AlertDialog alertDialog1 = new AlertDialog.Builder(EditProfileActivity.this).create();
        alertDialog1.setTitle("Your choices updated succesfully!");

        alertDialog1.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog1, int which) {
                Intent intent = new Intent(EditProfileActivity.this, MyProfileActivity.class);
                startActivity(intent);
            }
        });
        alertDialog1.show();


    }
    public void EditLater(View view){
        Intent intent = new Intent(EditProfileActivity.this, MyProfileActivity.class);
        startActivity(intent);
    }
}