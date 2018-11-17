package com.akashpate.whereami;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class MainActivity extends AppCompatActivity {
    AlertDialog.Builder alertDialog;
    Button mLocationButton;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLocationButton = findViewById(R.id.locateBtn);

        preferences = getSharedPreferences("UserPref",MODE_PRIVATE);
        if (preferences.getString("name",null)==null){
            alertDialog = new AlertDialog.Builder(MainActivity.this);
            alertDialog.setTitle("User Name");
            alertDialog.setMessage("Enter Full Name :");
            final EditText nameEditText = new EditText(MainActivity.this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            nameEditText.setLayoutParams(layoutParams);
            alertDialog.setView(nameEditText);
            alertDialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(nameEditText.getText().toString().equals("")){
                        Toast.makeText(MainActivity.this,"Please enter name!",Toast.LENGTH_LONG).show();
                    }else {
                        SharedPreferences.Editor editor = getSharedPreferences("UserPref",MODE_PRIVATE).edit();
                        editor.putString("name",nameEditText.getText().toString());
                        editor.commit();
                    }
                }
            });
            alertDialog.show();
        }



        mLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,MapActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }
}
