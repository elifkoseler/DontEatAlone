package com.elf.dea;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import com.elf.dea.UserData.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserPreferenceActivity extends AppCompatActivity {
    CheckBox meatCheckBox, fishCheckBox, fastfoodCheckBox,
            traditionalCheckBox, coffeeCheckBox, drinkCheckBox;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_preference);

        meatCheckBox = findViewById(R.id.MeatCheckBox);
        fishCheckBox = findViewById(R.id.FishCheckBox);
        fastfoodCheckBox = findViewById(R.id.FastFoodCheckBox);
        traditionalCheckBox = findViewById(R.id.TraditionalCheckBox);
        coffeeCheckBox = findViewById(R.id.CoffeeCheckBox);
        drinkCheckBox = findViewById(R.id.DrinkCheckBox);

    }


    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case meatCheckBox:
                if (checked)
            else
                // Remove the meat
                break;
            case fishCheckBox:
                if (checked)
                // Cheese me
            else
                // I'm lactose intolerant
                break;
        }
    }

    public void Next(View view){

    }

    public void Back(View view) {

    }

    public void EditLate(){

    }

}