package com.marquelo.getstars.working;

import android.os.Parcel;
import android.os.Parcelable;


public class ItemCarrito implements Parcelable {
    private String nombre;
    private String motivo;
    private String observaciones;
    private Double precio;
    private String imagen;
    private String creador;
    private String key;

    public ItemCarrito(){
    }

    public ItemCarrito(String nombre, String motivo, String observaciones, Double precio, String imagen) {
        this.nombre = nombre;
        this.motivo = motivo;
        this.observaciones = observaciones;
        this.precio = precio;
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getCreador() {
        return creador;
    }

    public void setCreador(String creador) {
        this.creador = creador;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }



    protected ItemCarrito(Parcel in) {
        nombre = in.readString();
        motivo = in.readString();
        observaciones = in.readString();
        precio = in.readByte() == 0x00 ? null : in.readDouble();
        imagen = in.readString();
        creador = in.readString();
        key = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nombre);
        dest.writeString(motivo);
        dest.writeString(observaciones);
        if (precio == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(precio);
        }
        dest.writeString(imagen);
        dest.writeString(creador);
        dest.writeString(key);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ItemCarrito> CREATOR = new Parcelable.Creator<ItemCarrito>() {
        @Override
        public ItemCarrito createFromParcel(Parcel in) {
            return new ItemCarrito(in);
        }

        @Override
        public ItemCarrito[] newArray(int size) {
            return new ItemCarrito[size];
        }
    };
}