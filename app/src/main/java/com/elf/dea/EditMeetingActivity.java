package com.elf.dea;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
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

public class EditMeetingActivity extends AppCompatActivity {
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    User user;
    Meeting meeting;

    DatePickerDialog picker;
    EditText dateText;
    EditText timeText;
    EditText locationText;
    EditText restText;
    EditText meetnameText;

    int d, m, y;
    int h, min;
    String district;
    String restName, meetName;

    String mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_meeting);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        user = new User();
        mail = firebaseAuth.getCurrentUser().getEmail();

        dateText = findViewById(R.id.editmeetdate);
        dateText.setInputType(InputType.TYPE_NULL);
        timeText = findViewById(R.id.editmeettime);
        timeText.setInputType(InputType.TYPE_NULL);
        locationText = findViewById(R.id.editmeetlocation);
        locationText.setInputType(InputType.TYPE_NULL);
        restText = findViewById(R.id.meetresttext);
        meetnameText = findViewById(R.id.editmeetmeetame);


        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(EditMeetingActivity.this,
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

                mTimePicker = new TimePickerDialog(EditMeetingActivity.this, new TimePickerDialog.OnTimeSetListener() {
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
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(EditMeetingActivity.this);
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

    public void EditLater(View view){
        Intent intent = new Intent(EditMeetingActivity.this, MyMeetingsActivity.class);
        startActivity(intent);
    }
    public void Submit(View view){
        restName = restText.getText().toString();
        meetName = meetnameText.getText().toString();

        CollectionReference colRef = firebaseFirestore.collection("Meetings").document(mail).collection("Meeting Info");

        colRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot snap :
                        queryDocumentSnapshots) {
                    System.out.println(snap.getData() + " : " + snap.getId());
                    colRef.document(snap.getId()).update("day",d ).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                        }
                    });
                    colRef.document(snap.getId()).update("month",m ).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                        }
                    });
                    colRef.document(snap.getId()).update("year",y ).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                        }
                    });
                    colRef.document(snap.getId()).update("hour",h ).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                        }
                    });
                    colRef.document(snap.getId()).update("second",min ).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                        }
                    });

                    colRef.document(snap.getId()).update("district",district ).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                        }
                    });

                    colRef.document(snap.getId()).update("name",meetName).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                        }
                    });
                    colRef.document(snap.getId()).update("restaurant name",restName ).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                        }
                    });

                }
            }
        });

        AlertDialog alertDialog1 = new AlertDialog.Builder(EditMeetingActivity.this).create();
        alertDialog1.setTitle("The meeting is updated successfully!");

        alertDialog1.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog1, int which) {
                Intent intent = new Intent(EditMeetingActivity.this, MyMeetingsActivity.class);
                startActivity(intent);
            }
        });
        alertDialog1.show();

    }
}