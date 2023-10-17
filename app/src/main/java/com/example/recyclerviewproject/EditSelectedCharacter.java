package com.example.recyclerviewproject;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class EditSelectedCharacter extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Button testBtn, imgBtn, cancelBtn;
    private ImageView imagePreview;
    private EditText nameEdit,typeEdit;
    private characterDAMO currentCharacter;
    private String currentName, currentType, currentPlanet, currentAff;

    private String planetSelected, affSelected;
    private int currentId, indexAff, indexPlanet;
    private Bitmap currentImage;

    private Boolean isFirstInteractionAff, isFirstInteractionPlanet;
    private byte[] selectedImage = null;

    public byte[] toBlob(Uri imageToConvert){
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageToConvert);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            int bytesRead;
            byte[] buffer = new byte[1024];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
            return byteArrayOutputStream.toByteArray();
        }
        catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    public int getSpinnerIndex(String selectedItem, String[] spinnerArray) {
        int index = 0;
        for (String item : spinnerArray) {
            Log.d("loop",String.valueOf(item) + " "+String.valueOf(selectedItem));
            if (item.contains(selectedItem)) {
                Log.d("FOUND!",String.valueOf(index));
                return index;
            }
            index++;
        }
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Intent intent = getIntent();
        currentId = intent.getIntExtra("id",0);
        currentName = intent.getStringExtra("name");
        currentType = intent.getStringExtra("type");
        currentPlanet = intent.getStringExtra("planet");
        currentAff = intent.getStringExtra("aff");
        currentImage = intent.getParcelableExtra("image");
        isFirstInteractionAff = isFirstInteractionPlanet = true;

        //Log.d("SELECTED",currentCharacter.getName());
        testBtn = (Button) findViewById(R.id.add_btn3);
        imgBtn = (Button) findViewById(R.id.img_btn);
        cancelBtn = (Button) findViewById(R.id.cancel_btn);

        Spinner planetSpn = (Spinner) findViewById(R.id.planet_dropdown);
        planetSpn.setOnItemSelectedListener(this);

        Spinner affSpn = (Spinner) findViewById(R.id.aff_dropdown);
        affSpn.setOnItemSelectedListener(this);

        nameEdit = (EditText) findViewById(R.id.name_edt);
        typeEdit = (EditText) findViewById(R.id.type_edt);
        imagePreview = (ImageView) findViewById(R.id.imagePreview);

        nameEdit.setText(currentName);
        typeEdit.setText(currentType);
        affSelected = currentAff;
        planetSelected = currentPlanet;

        Resources resources = getResources();
        String[] affSelectArray = resources.getStringArray(R.array.affSelect);
        String[] planetSelectArray = resources.getStringArray(R.array.planetSelect);
        indexAff = getSpinnerIndex(affSelected,affSelectArray);
        Log.d("index1",String.valueOf(indexAff));
        indexPlanet = getSpinnerIndex(planetSelected,planetSelectArray);
        Log.d("index2",String.valueOf(indexPlanet));
        affSpn.setSelection(indexAff);
        planetSpn.setSelection(indexPlanet);
        imagePreview.setImageBitmap(currentImage);

        ActivityResultLauncher<Intent> imageFromGalleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.getData() != null) {
                            Uri selectedImageUri = data.getData();
                            Bitmap selectedImageBitmap = null;
                            try {
                                selectedImageBitmap
                                        = MediaStore.Images.Media.getBitmap(
                                        this.getContentResolver(),
                                        selectedImageUri);
                            }
                            catch (IOException e) {
                                e.printStackTrace();
                            }
                            imagePreview.setImageBitmap(selectedImageBitmap);
                            selectedImage = toBlob(selectedImageUri);
                        }
                    }
                });

        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                imageFromGalleryLauncher.launch(i);

            }
        });

        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nameEdit.getText().length() == 0 || typeEdit.getText().length() == 0 || planetSelected == null || affSelected == null){
                    Toast.makeText(EditSelectedCharacter.this, "Completa todos los campos!", Toast.LENGTH_SHORT).show();

                }else {
                    Intent intent = new Intent(EditSelectedCharacter.this, MainActivity.class);
                    intent.putExtra("id",currentId);
                    intent.putExtra("name",nameEdit.getText().toString());
                    intent.putExtra("type",typeEdit.getText().toString());
                    intent.putExtra("planet",planetSelected);
                    intent.putExtra("affiliations",affSelected);
                    if(selectedImage != null) intent.putExtra("changedImage", selectedImage);

                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditSelectedCharacter.this, MainActivity.class);
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });


     //   TextView t = (TextView) findViewById(R.id.salutacioTxt);
     //   t.setText(String.valueOf(currentCharacter.getName()));
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            if (adapterView.getId() == R.id.aff_dropdown) {
                if(isFirstInteractionAff){
                    affSelected = adapterView.getItemAtPosition(indexAff).toString();
                    Log.d("AFF SELECTED FIRST", affSelected);
                    isFirstInteractionAff = false;
                }else {
                    affSelected = adapterView.getItemAtPosition(i).toString();
                    Log.d("AFF SELECTED", affSelected);
                }
            } else if (adapterView.getId() == R.id.planet_dropdown) {
                if(isFirstInteractionPlanet) {
                    planetSelected = adapterView.getItemAtPosition(indexPlanet).toString();
                    Log.d("PLANET SELECTED FIRST", planetSelected);
                    isFirstInteractionPlanet = false;
                }else{
                    planetSelected = adapterView.getItemAtPosition(i).toString();
                    Log.d("PLANET SELECTED", planetSelected);

                }
            }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        if(adapterView.getId() == R.id.aff_dropdown){
            affSelected = null;
        }
        else if(adapterView.getId() == R.id.planet_dropdown){
            planetSelected = null;
        }
    }
}