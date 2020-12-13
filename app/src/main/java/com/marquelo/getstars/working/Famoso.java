package com.marquelo.getstars.working;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class Famoso implements Parcelable {
    //Atributo
    private String name;
    private String category;
    private String description;
    private Uri img;
    private int img2;
    private String key;
    private int viewType;
    private int position;

    public Famoso(){

    }

    public Famoso(String name, String category, String description, Uri img, int viewType) {
        this.name = name;
        this.category = category;
        this.description = description;
        this.img = img;
        this.viewType = viewType;
    }


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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Uri getImg() {
        return img;
    }

    public void setImg(Uri img) {
        this.img = img;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getImg2() {
        return img2;
    }

    public void setImg2(int img2) {
        this.img2 = img2;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    protected Famoso(Parcel in) {
        name = in.readString();
        category = in.readString();
        description = in.readString();
        img = (Uri) in.readValue(Uri.class.getClassLoader());
        img2 = in.readInt();
        key = in.readString();
        viewType = in.readInt();
        position = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(category);
        dest.writeString(description);
        dest.writeValue(img);
        dest.writeInt(img2);
        dest.writeString(key);
        dest.writeInt(viewType);
        dest.writeInt(position);

    }

    @SuppressWarnings("unused")
    public static final Creator<Famoso> CREATOR = new Creator<Famoso>() {
        @Override
        public Famoso createFromParcel(Parcel in) {
            return new Famoso(in);
        }

        @Override
        public Famoso[] newArray(int size) {
            return new Famoso[size];
        }
    };



}