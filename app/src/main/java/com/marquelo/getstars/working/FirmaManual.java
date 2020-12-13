package com.marquelo.getstars.working;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class FirmaManual implements Parcelable {

    //--------------ATRIBUTOS----------------//
    private String id;
    private String nombre;
    private Uri url;


    //-----------CONSTRUCTORES--------------//

    public FirmaManual(){}

    public FirmaManual(String nombre, Uri url){
        this.nombre = nombre;
        this.url = url;
    }


    //----------GETTERS & SETTERS----------------//

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }

    public void setNombre(String nombre) { this.nombre = nombre; }

    public Uri getUrl() { return url; }

    public void setUrl(Uri url) { this.url = url; }



    protected FirmaManual(Parcel in) {
        id = in.readString();
        nombre = in.readString();
        url = (Uri) in.readValue(Uri.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(nombre);
        dest.writeValue(url);
    }

    public static final Creator<FirmaManual> CREATOR = new Creator<FirmaManual>() {
        @Override
        public FirmaManual createFromParcel(Parcel in) {
            return new FirmaManual(in);
        }

        @Override
        public FirmaManual[] newArray(int size) {
            return new FirmaManual[size];
        }
    };
}
