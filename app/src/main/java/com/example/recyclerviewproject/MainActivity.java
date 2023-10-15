package com.example.recyclerviewproject;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
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
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "type TEXT NOT NULL, " +
                "planet TEXT NOT NULL, " +
                "affiliations TEXT NOT NULL, " +
                "image BLOB NOT NULL)");
    }

    public void insertData(String name, String type, String planet, String affiliations, byte[] image) {
        //db.execSQL("INSERT INTO characters VALUES(" + id + ", '" + name + "', '" + description + "', "+ image + ")");

        Cursor c = db.rawQuery("SELECT id FROM characters where name='"+name+"'", null);
        if(c.getCount() == 0){
            ContentValues values = new ContentValues();
            values.put("name", name);
            values.put("type", type);
            values.put("planet", planet);
            values.put("affiliations", affiliations);
            values.put("image", image);
            db.insert("characters", null, values);
            c.close();
        }else {
            Log.e("SQL ERROR:","Este registro ya se encuentra introducido.");
            Toast.makeText(MainActivity.this, "Este registro ya se encuentra introducido!", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean deleteData(int idToDelete) {
        try{
            db.execSQL("DELETE FROM characters WHERE id =="+idToDelete);
            return true;
        }
        catch(SQLiteException e){
            Log.e("DELETE ERROR",String.valueOf(e));
            return false;
        }
        //TODO: context failure

    }
    private void updateData(int id, String name, String type, String planet, String aff, byte[] selectedImage) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("type", type);
        values.put("planet", planet);
        values.put("affiliations", aff);
        if(selectedImage != null) values.put("image", selectedImage);
        db.update("characters", values,"id = ?", new String[]{String.valueOf(id)});
    }

    public void showData() {
        Cursor c = db.rawQuery("SELECT * FROM characters", null);
        c.moveToFirst();
        ArrayList<characterDAMO> lchars = new ArrayList<characterDAMO>();
        while (!c.isAfterLast())
        {
            //String s = "ID " + c.getInt(0) + " name: " + c.getString(1) + " description: " + c.getString(2);
            //Toast.makeText(this, s, Toast.LENGTH_SHORT);
            characterDAMO cDAMO = new characterDAMO(c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getBlob(5));
            lchars.add(cDAMO);
            c.moveToNext();
        }
        c.close();
    }

    public byte[] drawableToBLOB(int currentDrawable){
        Drawable drawable = ContextCompat.getDrawable(MainActivity.this, currentDrawable);
        if (drawable instanceof BitmapDrawable){
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
            return stream.toByteArray();
        }
        return null;
    }
    private ArrayList<characterDAMO> getCharactersData() {
        Cursor c = db.rawQuery("SELECT * FROM characters", null);
        if(c.getCount() == 0) {
            insertData("Boba Fett", "Bountyhunter", "Kamino","Hutt Clan", drawableToBLOB(R.drawable.boba));
            insertData("Clone Trooper", "Republic soldier", "Coruscant","Grand Army of the Republic", drawableToBLOB(R.drawable.clone));
            insertData("Jawa", "Alien","Tatooine","Jawa's tribe", drawableToBLOB(R.drawable.jawa));
            insertData("Krell", "Jedi","Coruscant","Jedi Order",drawableToBLOB(R.drawable.krell));
            insertData("Obi-wan", "Jedi","Coruscant","Jedi Order",drawableToBLOB(R.drawable.obi));
            insertData("Yoda", "Jedi","Coruscant","Jedi Order",drawableToBLOB(R.drawable.yoda));
            insertData("Qui-gon", "Jedi","Coruscant","Jedi Order",drawableToBLOB(R.drawable.quigon));
            insertData("Darth Maul", "Lord Sith","Dathomir","Nightbrothers",drawableToBLOB(R.drawable.maul));
            insertData("Akbar", "Rebel Grand Admiral","Mon Cala","Moncalamari",drawableToBLOB(R.drawable.akbar));
            insertData( "Darth Vader", "Lord Sith","Mustafar","Empire",drawableToBLOB(R.drawable.vader));
        }
        ArrayList<characterDAMO> lchars = new ArrayList<characterDAMO>();

        c = db.rawQuery("SELECT * FROM characters", null);
        c.moveToFirst();

        while (!c.isAfterLast())
        {
            characterDAMO cDAMO = new characterDAMO(c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getBlob(5));
            lchars.add(cDAMO);
            c.moveToNext();
        }
        c.close();
        return lchars;
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        charList = (RecyclerView) findViewById(R.id.charList);

        getDataBase("legoStarWars_15.db");

        //TODO: CAMBIAR!
        lChars = getCharactersData();

        deleteBtn = (Button) findViewById(R.id.delete_btn);
        deleteBtn.setEnabled(false);
        AddNewCharBtn = (Button) findViewById(R.id.add_btn);
        editBtn = (Button) findViewById(R.id.edit_btn);
        editBtn.setEnabled(false);

        CharacterRecyclerViewAdapter adapter;
        adapter = new CharacterRecyclerViewAdapter(lChars, deleteBtn, editBtn, getApplicationContext());
        RecyclerView.LayoutManager l = new LinearLayoutManager(getApplicationContext());
        charList.setLayoutManager(l);
        charList.setItemAnimator(new DefaultItemAnimator());
        charList.setAdapter(adapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete this character?")
                .setTitle("Delete")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(deleteData(selectedItem.getId())){
                            Toast.makeText(MainActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                            lChars = getCharactersData();
                            adapter.setItems(lChars);
                            adapter.notifyDataSetChanged();
                        }else{
                            Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // CANCEL
                    }
                });
        // Create the AlertDialog object and return it
        AlertDialog deleteDialog = builder.create();


        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedItem = lChars.get(adapter.getSelectedItem());
                Log.d("ITEM TO DELETE", String.valueOf(selectedItem.getName()));
                deleteDialog.show();
            }
        });

        ActivityResultLauncher<Intent> addNewCharacterLauncher=registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result != null && result.getResultCode() == RESULT_OK){
                            if(result.getData() != null){
                                Log.d("ACTIVITY RES",String.valueOf(result.getData().getStringExtra("name")));
                                byte[] selectedImage = result.getData().getByteArrayExtra("image");
                                String name = result.getData().getStringExtra("name");
                                String type = result.getData().getStringExtra("type");
                                String planet = result.getData().getStringExtra("planet");
                                String aff = result.getData().getStringExtra("affiliations");
                                insertData(name, type,planet,aff, selectedImage);
                                lChars = getCharactersData();
                                adapter.setItems(lChars);
                                adapter.notifyDataSetChanged();

                            }
                        }
                    }
                }
        );

        ActivityResultLauncher<Intent> editCharacterLauncher=registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result != null && result.getResultCode() == RESULT_OK){
                            if(result.getData() != null){
                                Log.d("ACTIVITY RES",String.valueOf(result.getData().getStringExtra("name")));
                                byte[] selectedImage = null;
                                try {
                                    selectedImage = result.getData().getByteArrayExtra("changedImage");
                                }
                                catch (NullPointerException e){
                                    Log.d("INFO","IMAGE NOT CHANGED");
                                }
                                int id = result.getData().getIntExtra("id",0);
                                String name = result.getData().getStringExtra("name");
                                String type = result.getData().getStringExtra("type");
                                String planet = result.getData().getStringExtra("planet");
                                String aff = result.getData().getStringExtra("affiliations");
                                updateData(id, name, type, planet, aff, selectedImage);
                                lChars = getCharactersData();
                                adapter.setItems(lChars);
                                adapter.notifyDataSetChanged();
                                lChars = getCharactersData();
                                adapter.setItems(lChars);
                                adapter.notifyDataSetChanged();

                            }
                        }
                    }
                }
        );

        AddNewCharBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AddNewCharacter.class);
                addNewCharacterLauncher.launch(intent);
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), EditSelectedCharacter.class);
                Log.d("test",String.valueOf((lChars.get(adapter.getSelectedItem())).getName()));
                intent.putExtra("id", (lChars.get(adapter.getSelectedItem())).getId());
                intent.putExtra("name", (lChars.get(adapter.getSelectedItem())).getName());
                intent.putExtra("type", (lChars.get(adapter.getSelectedItem())).getType());
                intent.putExtra("planet", (lChars.get(adapter.getSelectedItem())).getPlanet());
                intent.putExtra("aff", (lChars.get(adapter.getSelectedItem())).getAffiliations());
                Bitmap resizedBitmap = Bitmap.createScaledBitmap((lChars.get(adapter.getSelectedItem())).getImage(), 100, 100, false);
                intent.putExtra("image", resizedBitmap);

                editCharacterLauncher.launch(intent);
            }
        });




    }


}

