package com.example.recyclerviewproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorBoundsInfo;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView charList;
    ArrayList<characterDAMO> lChars;
    SQLiteDatabase db;
    Button AddNewCharBtn, deleteBtn, editBtn;

    characterDAMO selectedItem;

    public void getDataBase(String dataBase) {
        db = openOrCreateDatabase(dataBase, MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS characters(" +
                "id INT NOT NULL PRIMARY KEY, " +
                "name TEXT NOT NULL, " +
                "description TEXT NOT NULL, " +
                "image INT NOT NULL)");
    }

    public void insertData(int id, String name, String description, int image) {
        //db.execSQL("INSERT INTO characters VALUES(" + id + ", '" + name + "', '" + description + "', "+ image + ")");

        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("name", name);
        values.put("description", description);
        values.put("image", image);
        db.insert("characters", null, values);
    }

    public void showData() {
        Cursor c = db.rawQuery("SELECT * FROM characters", null);
        c.moveToFirst();
        ArrayList<characterDAMO> lchars = new ArrayList<characterDAMO>();
        while (!c.isAfterLast())
        {
            //String s = "ID " + c.getInt(0) + " name: " + c.getString(1) + " description: " + c.getString(2);
            //Toast.makeText(this, s, Toast.LENGTH_SHORT);
            characterDAMO cDAMO = new characterDAMO(c.getInt(0), c.getString(1), c.getString(2), c.getInt(3));
            lchars.add(cDAMO);
            c.moveToNext();
        }
    }
    private ArrayList<characterDAMO> getCharactersData() {

        insertData(1, "Boba Fett", "Bountyhunter", R.drawable.boba);
        insertData(2, "Clone Trooper", "Republic soldier", R.drawable.clone);
        insertData(3, "Jawa", "Alien", R.drawable.jawa);
        insertData(4, "Krell", "Jedi",R.drawable.krell);
        insertData(5, "Obi-wan", "Jedi",R.drawable.obi);
        insertData(6, "Yoda", "Jedi",R.drawable.yoda);
        insertData(7, "Qui-gon", "Jedi",R.drawable.quigon);
        insertData(8, "Darth Maul", "Lord Sith",R.drawable.maul);
        insertData(9, "Akbar", "Moncalamari",R.drawable.akbar);
        insertData(10, "Darth Vader", "Lord Sith",R.drawable.vader);

        ArrayList<characterDAMO> lchars = new ArrayList<characterDAMO>();

        Cursor c = db.rawQuery("SELECT * FROM characters", null);
        c.moveToFirst();

        while (!c.isAfterLast())
        {
            characterDAMO cDAMO = new characterDAMO(c.getInt(0), c.getString(1), c.getString(2), c.getInt(3));
            lchars.add(cDAMO);
            c.moveToNext();
        }


        return lchars;
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        charList = (RecyclerView) findViewById(R.id.charList);

        getDataBase("legoStarWars.db");

        //CAMBIAR!
        lChars = getCharactersData();

        deleteBtn = (Button) findViewById(R.id.delete_btn);
        deleteBtn.setEnabled(false);
        AddNewCharBtn = (Button) findViewById(R.id.add_btn);
        editBtn = (Button) findViewById(R.id.edit_btn);
        editBtn.setEnabled(false);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete this character?")
                .setTitle("Delete")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // CONFIRM
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // CANCEL
                    }
                });
        // Create the AlertDialog object and return it
        AlertDialog deleteDialog = builder.create();

        CharacterRecyclerViewAdapter adapter;
        adapter = new CharacterRecyclerViewAdapter(lChars, deleteBtn, editBtn, getApplicationContext());
        RecyclerView.LayoutManager l = new LinearLayoutManager(getApplicationContext());
        charList.setLayoutManager(l);
        charList.setItemAnimator(new DefaultItemAnimator());
        charList.setAdapter(adapter);

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedItem = lChars.get(adapter.getSelectedItem());
                Log.d("ITEM TO DELETE", String.valueOf(selectedItem.getName()));
                deleteDialog.show();
            }
        });

        AddNewCharBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), GetCharactersData.class);
                intent.putExtra("salutacio", "Benvingut");

                startActivity(intent);
            }
        });




    }
}

