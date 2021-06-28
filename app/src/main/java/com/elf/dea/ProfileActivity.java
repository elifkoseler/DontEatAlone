package com.elf.dea;

import android.Manifest;
import android.app.AlertDialog;
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
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.elf.dea.UserData.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class ProfileActivity extends AppCompatActivity {

    Bitmap selectedImage;
    EditText nameText, usernameText, phoneText, locationText, birthYearText;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference; //storage ref
    private FirebaseStorage firebaseStorage;   //storage


    User user = new User();
    Uri imageData, tempUri;
    ImageView imageView;
    String district;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        nameText = findViewById(R.id.NameText);
        usernameText = findViewById(R.id.UsernameText);
        phoneText = findViewById(R.id.PhoneText);
        locationText = findViewById(R.id.LocationText);
        birthYearText = findViewById(R.id.BirthYearText);
        imageView = findViewById(R.id.imageView);
        locationText.setInputType(InputType.TYPE_NULL);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        String[] listItems = getResources().getStringArray(R.array.istanbul_county);

        locationText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(ProfileActivity.this);
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
        Log.d("myTag","EditLater butonuna basıldı!");
        Intent intent = new Intent(ProfileActivity.this, StartActivity.class);
        startActivity(intent);
        //finish();
    }

    public void Save(View view){

        //UUID uuid = UUID.randomUUID();

        user.setName(nameText.getText().toString());
        user.setUsername(usernameText.getText().toString());
        user.setPhone(phoneText.getText().toString());
        user.setLocation(district);
        user.setBirthYear(Integer.parseInt(birthYearText.getText().toString()));
        //Toast.makeText(ProfileActivity.this,"Save'e bastın !!", Toast.LENGTH_LONG).show();
        user.setEmail(firebaseAuth.getCurrentUser().getEmail());

        HashMap<String, Object> emailData = new HashMap<>();
        emailData.put("mail", user.getEmail());

        HashMap<String, Object> postData = new HashMap<>();
        postData.put("name", user.getName());
        postData.put("username", user.getUsername());
        postData.put("email", user.getEmail());
        postData.put("phone", user.getPhone());
        postData.put("location", user.getLocation());
        postData.put("birth year", user.getBirthYear());
        postData.put("register time", FieldValue.serverTimestamp());

        firebaseFirestore.collection("UserMails").add(emailData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                //Toast.makeText(ProfileActivity.this,"UserMail Dbye eklendi!!",Toast.LENGTH_LONG).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(ProfileActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();

            }
        });
        //User maili bi yerde komple tutmam gerekiyor

        firebaseFirestore.collection("Users").document(user.getEmail()).collection("User Info")
                .add(postData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
               // Toast.makeText(ProfileActivity.this,"Dbye eklendi!!",Toast.LENGTH_LONG).show();
                System.out.println("Profile OnSuccesstesin");
                Intent intent = new Intent(ProfileActivity.this, EatingPreferenceActivity.class);
                intent.putExtra("user",user);
                System.out.println("ekstra");
                startActivity(intent);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfileActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();

            }
        });

        if (imageData != null) {
            UUID uuid = UUID.randomUUID();
            String imageName = "images/" + uuid + ".jpg";

            storageReference.child(imageName).putFile(imageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(ProfileActivity.this, "Image is uploaded successfully!", Toast.LENGTH_LONG).show();

                    StorageReference newReference = FirebaseStorage.getInstance().getReference(imageName); //upload ettiğim imagein yerini bul
                    newReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();
                            sendImageUrltoDB(url);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ProfileActivity.this, e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });

        }


        }


     //image urlini fbye yazman sonrasında myprofilde çekip ile basman lazım
    public void uploadProfileImage(View view) {

    }

    public void sendImageUrltoDB(String url){
        HashMap<String, Object> postData = new HashMap<>();
        postData.put("profile image url", url);

        firebaseFirestore.collection("Users").document(user.getEmail()).collection("User Info")
                .add(postData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfileActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();

            }
        });
    }


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
