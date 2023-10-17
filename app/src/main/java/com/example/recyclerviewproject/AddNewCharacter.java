package com.example.recyclerviewproject;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
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
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import kotlinx.coroutines.flow.AbstractFlow;

public class AddNewCharacter extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Button sendBtn, imgBtn, cancelBtn;
    private ImageView imagePreview;
    private EditText nameEdit,typeEdit;
    private String planetSelected, affSelected;
    int SELECT_PICTURE = 200;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Intent intent = getIntent();

        ActivityResultLauncher<Intent> imageFromGalleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    // do your operation from here....
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

        sendBtn = (Button) findViewById(R.id.add_btn3);
        cancelBtn = (Button) findViewById(R.id.cancel_btn);

        Spinner planetSpn = (Spinner) findViewById(R.id.planet_dropdown);
        planetSpn.setOnItemSelectedListener(this);

        Spinner affSpn = (Spinner) findViewById(R.id.aff_dropdown);
        affSpn.setOnItemSelectedListener(this);

        imgBtn = (Button) findViewById(R.id.img_btn);
        nameEdit = (EditText) findViewById(R.id.name_edt);
        typeEdit = (EditText) findViewById(R.id.type_edt);
        imagePreview = (ImageView) findViewById(R.id.imagePreview);

        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                imageFromGalleryLauncher.launch(i);

            }
        });
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nameEdit.getText().length() == 0 || typeEdit.getText().length() == 0 || planetSelected == null || affSelected == null || selectedImage == null){
                    Toast.makeText(AddNewCharacter.this, "Completa todos los campos!", Toast.LENGTH_SHORT).show();

                }else {
                    Intent intent = new Intent(AddNewCharacter.this, MainActivity.class);
                    intent.putExtra("name",nameEdit.getText().toString());
                    intent.putExtra("type",typeEdit.getText().toString());
                    intent.putExtra("planet",planetSelected);
                    intent.putExtra("affiliations",affSelected);
                    intent.putExtra("image", selectedImage);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddNewCharacter.this, MainActivity.class);
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(adapterView.getId() == R.id.aff_dropdown){
            affSelected = adapterView.getItemAtPosition(i).toString();
            Log.d("AFF SELECTED",affSelected);
        }
        else if(adapterView.getId() == R.id.planet_dropdown){
            planetSelected = adapterView.getItemAtPosition(i).toString();
            Log.d("PLANET SELECTED",planetSelected);
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