package com.example.recyclerviewproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class GetCharactersData extends AppCompatActivity {

    private characterDAMO currentCharacter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_characters_data);
        Intent intent = getIntent();
     //   currentCharacter = (characterDAMO) intent.getSerializableExtra("currentCharacter");
     //   TextView t = (TextView) findViewById(R.id.salutacioTxt);
     //   t.setText(String.valueOf(currentCharacter.getName()));
    }
}