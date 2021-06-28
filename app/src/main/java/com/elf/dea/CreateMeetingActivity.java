package com.elf.dea;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.elf.dea.MeetingData.Meeting;
import com.elf.dea.UserData.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Calendar;
import java.util.UUID;

public class CreateMeetingActivity extends AppCompatActivity {

    Bitmap selectedImage;
    ImageView imageView;
    EditText nameText;

    private FirebaseStorage firebaseStorage;   //storage
    private StorageReference storageReference; //storage ref
    private FirebaseFirestore firebaseFirestore; //database
    private FirebaseAuth firebaseAuth; //kullanıcı auth
    Uri imageData, tempUri;

    DatePickerDialog picker;
    EditText dateText;
    EditText timeText;
    EditText locationText;
    EditText restaurantText;
    EditText numberText;

    User user;
    Meeting meeting = new Meeting();

    int d, m, y;
    int h, min;
    String district;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_meeting);

        imageView = findViewById(R.id.imageView3);
        nameText = findViewById(R.id.nameText);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        user = (User) getIntent().getSerializableExtra("user"); // get ref of user from other activities

        dateText = findViewById(R.id.editText1);
        dateText.setInputType(InputType.TYPE_NULL);
        timeText = findViewById(R.id.editText);
        timeText.setInputType(InputType.TYPE_NULL);
        locationText = findViewById(R.id.locationText);
        locationText.setInputType(InputType.TYPE_NULL);
        restaurantText = findViewById(R.id.editTextRestaurant);
        numberText = findViewById(R.id.editTextNumber);

        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(CreateMeetingActivity.this,
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

                mTimePicker = new TimePickerDialog(CreateMeetingActivity.this, new TimePickerDialog.OnTimeSetListener() {
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
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(CreateMeetingActivity.this);
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

    public void Next(View view){
        meeting.setName(nameText.getText().toString());
        meeting.setDay(d);
        meeting.setMonth(m);
        meeting.setYear(y);
        meeting.setHour(h);
        meeting.setSecond(min);
        meeting.setDistrict(district);
        meeting.getRestaurant().setName(restaurantText.getText().toString());
        meeting.setNumberOfParticipant(Integer.parseInt(numberText.getText().toString()));

        if(imageData != null) {
            UUID uuid = UUID.randomUUID();
            String imageName = "images/" + uuid + ".jpg";

            storageReference.child(imageName).putFile(imageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(CreateMeetingActivity.this,"Image is uploaded successfully!",Toast.LENGTH_LONG).show();

                    StorageReference newReference = FirebaseStorage.getInstance().getReference(imageName); //upload ettiğim imagein yerini bul
                    newReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            tempUri = uri;
                            meeting.setImageUrl(tempUri.toString());
                            Intent intent = new Intent(CreateMeetingActivity.this, CreateMeetingRestaurantDetailActivity.class);
                            intent.putExtra("user", user);
                            intent.putExtra("meeting", meeting);
                            startActivity(intent);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CreateMeetingActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });

        }
        //System.out.println(tempUri.toString());
       // meeting.setImageUrl(tempUri.toString());
        //System.out.println(meeting.getRestaurant().getName());

    }

   /* public void Upload(View view){
        System.out.println("Picked Date Exit " + d +  " " + m + " "+ y);


            //universal unique id kullanmalıyım, storageda üst üste yazmasın diye
            UUID uuid = UUID.randomUUID();
            String imageName = "images/" + uuid + ".jpg";
            storageReference.child(imageName).putFile(imageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(CreateMeetingActivity.this,"Uploaded successfully!",Toast.LENGTH_LONG).show();
                    //Download URL

                    StorageReference newReference = FirebaseStorage.getInstance().getReference(imageName); //upload ettiğim imagein yerini bul
                    newReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String downloadUrl = uri.toString();
                            /*FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            String userEmail = firebaseUser.getEmail();*/


/*
                            HashMap<String, Object> postData = new HashMap<>();
                            postData.put("useremail",user.getEmail());
                            postData.put("downloadurl",downloadUrl);
                            postData.put("name",meeting.getName());
                            postData.put("date", FieldValue.serverTimestamp());

                            firebaseFirestore.collection("Meetings").add(postData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {

                                    Intent intent = new Intent(CreateMeetingActivity.this, FeedActivity.class);
                                    //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //tüm aktiviteleri kapatıp uygulamadan çıkıyoruz.
                                    startActivity(intent);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(CreateMeetingActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(CreateMeetingActivity.this, e.getLocalizedMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }

*/
    public void selectImage(View view){
        //izin istendi, izin verildi ya da verilmedi.
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},1);
        } else {
            Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intentToGallery,2);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == 1) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ) { // istediğimiz dizinin sonucu

                Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentToGallery,2);
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode == 2 && resultCode == RESULT_OK && data != null) {

            imageData = data.getData(); // URI veriyor, görsel nerede kayıtlı
            try {
                if (Build.VERSION.SDK_INT >= 28) {
                    ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), imageData);
                    selectedImage = ImageDecoder.decodeBitmap(source);
                    imageView.setImageBitmap(selectedImage);
                } else {
                    selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageData);
                    imageView.setImageBitmap(selectedImage);
                }

            }
            catch(IOException e){
                e.printStackTrace();
            }


        }


        super.onActivityResult(requestCode, resultCode, data);
    }


}