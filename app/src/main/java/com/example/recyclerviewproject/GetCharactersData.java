package com.example.recyclerviewproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GetCharactersData extends AppCompatActivity {
    private Button testBtn;
    private characterDAMO currentCharacter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_characters_data);

        Intent intent = getIntent();
        String currentCharacter = intent.getStringExtra("currentCharacter");

        testBtn = (Button) findViewById(R.id.test_btn);

        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GetCharactersData.this, MainActivity.class);
                intent.putExtra("name", "pepe");
                setResult(RESULT_OK, intent);
                finish();
            }
        });


     //   TextView t = (TextView) findViewById(R.id.salutacioTxt);
     //   t.setText(String.valueOf(currentCharacter.getName()));
    }
}