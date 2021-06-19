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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.elf.dea.UserData.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;

public class AvailabilityActivity extends AppCompatActivity {
    User user;

    DatePickerDialog picker;
    EditText dateText;
    EditText timeText;
    EditText locationText;

    private FirebaseFirestore firebaseFirestore; //database
    private FirebaseAuth firebaseAuth; //kullanıcı auth

    int d, m, y;
    int h, min;
    String district;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_availability);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        dateText = findViewById(R.id.dateText);
        dateText.setInputType(InputType.TYPE_NULL);
        timeText = findViewById(R.id.timeText);
        timeText.setInputType(InputType.TYPE_NULL);
        locationText = findViewById(R.id.locationText2);
        locationText.setInputType(InputType.TYPE_NULL);

        user = (User)getIntent().getSerializableExtra("user");

        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(AvailabilityActivity.this,
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

                mTimePicker = new TimePickerDialog(AvailabilityActivity.this, new TimePickerDialog.OnTimeSetListener() {
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
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(AvailabilityActivity.this);
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
        //System.out.println("From Feed to create"+user.getEmail());
        System.out.println(firebaseAuth.getCurrentUser().getEmail());
    }



    public void Save(View view){
        HashMap<String, Object> data = new HashMap<>();

        data.put("day", d);
        data.put("month", m);
        data.put("year", y);
        data.put("hour", h);
        data.put("second", min);
        data.put("district", district);

        firebaseFirestore.collection("Users").document(user.getEmail()).collection("Availability")
                .add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(AvailabilityActivity.this," USER INFO AVAILABLE Dbye eklendi!!",Toast.LENGTH_LONG).show();

                Intent intent = new Intent(AvailabilityActivity.this, StartActivity.class);
                intent.putExtra("user",user);
                startActivity(intent);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AvailabilityActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();

            }
        });



        Intent intent = new Intent(AvailabilityActivity.this, StartActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);

    }
    public void EditLater(View view){
        System.out.println("EditLater butonun basıldı!");
        Intent intent = new Intent(AvailabilityActivity.this, StartActivity.class);
        startActivity(intent);

    }

    public void Back(View view){
        System.out.println("Back butonuna basıldı!");
        Intent intent = new Intent(AvailabilityActivity.this, MeetingPreferencesActivity.class);
        startActivity(intent);
    }
}