package com.elf.dea;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.elf.dea.MeetingData.Meeting;
import com.elf.dea.UserData.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;

public class EditProfileAvailabilityActivity extends AppCompatActivity {
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    User user;
    Meeting meeting;
    boolean art, music, travel, technology, sports;

    DatePickerDialog picker;
    EditText dateText;
    EditText timeText;
    EditText locationText;

    int d, m, y;
    int h, min;
    String district;


    String mail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_availability);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        user = new User();
        mail = firebaseAuth.getCurrentUser().getEmail();

        dateText = findViewById(R.id.editdatetext);
        dateText.setInputType(InputType.TYPE_NULL);
        timeText = findViewById(R.id.edittimetext);
        timeText.setInputType(InputType.TYPE_NULL);
        locationText = findViewById(R.id.editlocatext);
        locationText.setInputType(InputType.TYPE_NULL);

        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(EditProfileAvailabilityActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                dateText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                System.out.println("Picked Date " + dayOfMonth +  " " + monthOfYear + " "+ year);
                                d = dayOfMonth;
                                m = monthOfYear + 1 ;
                                y = year;
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        timeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;

                mTimePicker = new TimePickerDialog(EditProfileAvailabilityActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        timeText.setText( selectedHour + ":" + selectedMinute);
                        System.out.println("Picked time: " + selectedHour + ":" + selectedMinute);
                        h = selectedHour;
                        min = selectedMinute;
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        String[] listItems = getResources().getStringArray(R.array.istanbul_county);

        locationText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(EditProfileAvailabilityActivity.this);
                mBuilder.setTitle("Please choose the location");
                mBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        locationText.setText(listItems[i]);
                        System.out.println("Picked county: " + listItems[i]);
                        district = listItems[i];
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
            case R.id.musicCheckBox3:
                if (checked)
                    music = true;
                else
                    music = false;
                break;
            case R.id.artCheckBox3:
                if (checked)
                    art = true;
                else
                    art = false;
                break;
            case R.id.travelCheckBox3:
                if (checked)
                    travel = true;
                else
                    travel = false;
                break;
            case R.id.sportsCheckBox3:
                if (checked)
                    sports = true;
                else
                    sports = false;
                break;
            case R.id.techCheckBox3:
                if (checked)
                    technology = true;
                else
                    technology = false;
                break;

        }
    }
    public void Submit(View view){
        CollectionReference colRefInt = firebaseFirestore.collection("Users").document(mail).collection("Interests");
        CollectionReference colRefAva = firebaseFirestore.collection("Users").document(mail).collection("Availability");

        colRefAva.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot snap :
                        queryDocumentSnapshots) {
                    System.out.println(snap.getData() + " : " + snap.getId());
                    colRefAva.document(snap.getId()).update("day",d ).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                        }
                    });
                    colRefAva.document(snap.getId()).update("month",m ).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                        }
                    });
                    colRefAva.document(snap.getId()).update("year",y ).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                        }
                    });
                    colRefAva.document(snap.getId()).update("hour",h ).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                        }
                    });
                    colRefAva.document(snap.getId()).update("second",min ).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                        }
                    });

                    colRefAva.document(snap.getId()).update("district",district ).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                        }
                    });

                }
            }
        });

        colRefInt.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot snap :
                        queryDocumentSnapshots) {
                    System.out.println(snap.getData() + " : " + snap.getId());
                    colRefInt.document(snap.getId()).update("isArt",art ).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                        }
                    });
                    colRefInt.document(snap.getId()).update("isMusic",music ).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                        }
                    });
                    colRefInt.document(snap.getId()).update("isTech",technology ).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                        }
                    });
                    colRefInt.document(snap.getId()).update("isSports",sports ).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                        }
                    });
                    colRefInt.document(snap.getId()).update("isTravel",travel ).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                        }
                    });

                }
            }
        });

        AlertDialog alertDialog1 = new AlertDialog.Builder(EditProfileAvailabilityActivity.this).create();
        alertDialog1.setTitle("Your interest and availability are updated successfully!");

        alertDialog1.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog1, int which) {
                Intent intent = new Intent(EditProfileAvailabilityActivity.this, MyProfileActivity.class);
                startActivity(intent);
            }
        });
        alertDialog1.show();

    }
    public void EditLater(View view){
        Intent intent = new Intent(EditProfileAvailabilityActivity.this, MyProfileActivity.class);
        startActivity(intent);
    }

}