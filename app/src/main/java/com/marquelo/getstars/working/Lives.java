package com.marquelo.getstars.working;

import android.os.Parcel;
import android.os.Parcelable;

public class Lives implements Parcelable {
    private String nombre;
    private String descripcion;
    private String creador; //nombre creador
    private String fechaFinal;
    private String key; //key del creador
    private String img;
    private double precio;
    private String capacidadActual;
    private String capacidadTotal;
    private String idLives;//key del live actual

    public Lives(){

    }

    public Lives(String nombre, String descripcion, String creador, String fechaFinal, String key) {
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



    public String getIdLives() {
        return idLives;
    }

    public void setIdLives(String idLives) {
        this.idLives = idLives;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getCapacidadTotal() {
        return capacidadTotal;
    }

    public void setCapacidadTotal(String capacidadTotal) {
        this.capacidadTotal = capacidadTotal;
    }

    public String getCapacidadActual() {
        return capacidadActual;
    }

    public void setCapacidadActual(String capacidadActual) {
        this.capacidadActual = capacidadActual;
    }

    protected Lives(Parcel in) {
        nombre = in.readString();
        descripcion = in.readString();
        creador = in.readString();
        fechaFinal = in.readString();
        key = in.readString();
        img = in.readString();
        precio = in.readDouble();
        capacidadActual = in.readString();
        capacidadTotal = in.readString();
        idLives = in.readString();
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
        dest.writeDouble(precio);
        dest.writeString(capacidadActual);
        dest.writeString(capacidadTotal);
        dest.writeString(idLives);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Lives> CREATOR = new Parcelable.Creator<Lives>() {
        @Override
        public Lives createFromParcel(Parcel in) {
            return new Lives(in);
        }

        @Override
        public Lives[] newArray(int size) {
            return new Lives[size];
        }
    };
}
