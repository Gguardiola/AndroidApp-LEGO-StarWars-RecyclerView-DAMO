package com.example.recyclerviewproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.Serializable;

public class characterDAMO implements Serializable {
    private int id;
    private String name;
    private String type, planet, affiliations;
    private byte[] image;

    public characterDAMO (int id, String name, String type, String planet, String affiliations, byte[] image) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.planet = planet;
        this.affiliations = affiliations;
        this.image = image;
    }
    public String getPlanet() {
        return planet;
    }

    public String getAffiliations() {
        return affiliations;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Bitmap getImage() {

        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        return bitmap;
    }

   // public void setImage(int image) {
   //     this.image = image;
   // }
}
