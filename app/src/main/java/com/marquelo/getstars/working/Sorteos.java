package com.marquelo.getstars.working;

import android.os.Parcel;
import android.os.Parcelable;

public class Sorteos implements Parcelable {
    private String nombre;
    private String descripcion;
    private String creador;
    private String fechaFinal;
    private String key;
    private String img;
    private String partipantes;
    private String idSorteo;

    public Sorteos(){

    }

    public Sorteos(String nombre, String descripcion, String creador, String fechaFinal, String key) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.creador = creador;
        this.fechaFinal = fechaFinal;
        this.key = key;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCreador() {
        return creador;
    }

    public void setCreador(String creador) {
        this.creador = creador;
    }

    public String getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(String fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getPartipantes() {
        return partipantes;
    }

    public void setPartipantes(String partipantes) {
        this.partipantes = partipantes;
    }

    protected Sorteos(Parcel in) {
        nombre = in.readString();
        descripcion = in.readString();
        creador = in.readString();
        fechaFinal = in.readString();
        key = in.readString();
        img = in.readString();
        partipantes = in.readString();
        idSorteo = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nombre);
        dest.writeString(descripcion);
        dest.writeString(creador);
        dest.writeString(fechaFinal);
        dest.writeString(key);
        dest.writeString(img);
        dest.writeString(partipantes);
        dest.writeString(idSorteo);

    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Sorteos> CREATOR = new Parcelable.Creator<Sorteos>() {
        @Override
        public Sorteos createFromParcel(Parcel in) {
            return new Sorteos(in);
        }

        @Override
        public Sorteos[] newArray(int size) {
            return new Sorteos[size];
        }
    };

    public String getIdSorteo() {
        return idSorteo;
    }

    public void setIdSorteo(String idSorteo) {
        this.idSorteo = idSorteo;
    }
}