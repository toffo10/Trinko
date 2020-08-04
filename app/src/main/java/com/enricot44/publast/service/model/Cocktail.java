package com.enricot44.publast.service.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "cocktail")
public class Cocktail implements Serializable, Parcelable {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "name")
    private String name = "";

    @ColumnInfo(name = "category")
    private String category = "";

    @ColumnInfo(name = "instruction")
    private String instruction = "";

    @ColumnInfo(name = "ingredients")
    private String ingredients = "";

    @ColumnInfo(name = "thumbnailUrl")
    private String thumbnailUrl = "";

    @ColumnInfo(name = "thumbnail")
    private Bitmap thumbnail;

    public Cocktail() {
    }

    protected Cocktail(Parcel in) {
        name = in.readString();
        category = in.readString();
        instruction = in.readString();
        ingredients = in.readString();
        thumbnailUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(category);
        dest.writeString(instruction);
        dest.writeString(ingredients);
        dest.writeString(thumbnailUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Cocktail> CREATOR = new Creator<Cocktail>() {
        @Override
        public Cocktail createFromParcel(Parcel in) {
            return new Cocktail(in);
        }

        @Override
        public Cocktail[] newArray(int size) {
            return new Cocktail[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }
}
