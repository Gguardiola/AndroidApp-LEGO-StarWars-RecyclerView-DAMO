package com.example.recyclerviewproject;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class EditSelectedCharacter extends AppCompatActivity {
    private Button testBtn, imgBtn, cancelBtn;
    private ImageView imagePreview;
    private EditText nameEdit,typeEdit,planetEdit,affEdit;
    private characterDAMO currentCharacter;
    private String currentName, currentType, currentPlanet, currentAff;
    private int currentId;
    private Bitmap currentImage;

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
        setContentView(R.layout.activity_edit);

        Intent intent = getIntent();
        currentId = intent.getIntExtra("id",0);
        currentName = intent.getStringExtra("name");
        currentType = intent.getStringExtra("type");
        currentPlanet = intent.getStringExtra("planet");
        currentAff = intent.getStringExtra("aff");
        currentImage = intent.getParcelableExtra("image");

        //Log.d("SELECTED",currentCharacter.getName());
        testBtn = (Button) findViewById(R.id.add_btn3);
        imgBtn = (Button) findViewById(R.id.img_btn);
        cancelBtn = (Button) findViewById(R.id.cancel_btn);

        nameEdit = (EditText) findViewById(R.id.name_edt);
        typeEdit = (EditText) findViewById(R.id.type_edt);
        planetEdit = (EditText) findViewById(R.id.planet_edt);
        affEdit = (EditText) findViewById(R.id.aff_edt);
        imagePreview = (ImageView) findViewById(R.id.imagePreview);

        nameEdit.setText(currentName);
        typeEdit.setText(currentType);
        planetEdit.setText(currentPlanet);
        affEdit.setText(currentAff);
        imagePreview.setImageBitmap(currentImage);

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
                if(nameEdit.getText().length() == 0 || typeEdit.getText().length() == 0 || planetEdit.getText().length() == 0 || affEdit.getText().length() == 0){
                    Toast.makeText(EditSelectedCharacter.this, "Completa todos los campos!", Toast.LENGTH_SHORT).show();

                }else {
                    Intent intent = new Intent(EditSelectedCharacter.this, MainActivity.class);
                    intent.putExtra("id",currentId);
                    intent.putExtra("name",nameEdit.getText().toString());
                    intent.putExtra("type",typeEdit.getText().toString());
                    intent.putExtra("planet",planetEdit.getText().toString());
                    intent.putExtra("affiliations",affEdit.getText().toString());
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
}