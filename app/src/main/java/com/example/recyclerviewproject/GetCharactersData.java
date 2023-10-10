package com.example.recyclerviewproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class GetCharactersData extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_characters_data);

        Intent intent = getIntent();
        String salutacio = intent.getStringExtra("salutacio");

        TextView t = (TextView) findViewById(R.id.salutacioTxt);
        t.setText(salutacio);
    }
}