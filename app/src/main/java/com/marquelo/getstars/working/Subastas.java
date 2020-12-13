package com.marquelo.getstars.working;


import android.os.Parcel;
import android.os.Parcelable;

public class Subastas implements Parcelable {
    //ATRIBUTOS
    private String nombre;
    private String descripcion;
    private Double precio;
    private String creador;
    private String fechaFinal;
    private String getImg;
    private String ultimoParticipante;
    private String key;
    private String idSubasta;
    public Subastas() {
    }

    public Subastas(String nombre, String info, Double precio, String getImg){
        this.nombre = nombre;
        this.descripcion = info;
        this.precio = precio;
        this.getImg = getImg;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getInfo() {
        return descripcion;
    }

    public void setInfo(String info) {
        this.descripcion = info;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public String getImg() {
        return getImg;
    }

    public void setImg(String getImg) {
        this.getImg = getImg;
    }

    public String getCreador() {
        return creador;
    }

    public void setCreador(String creador) {
        this.creador = creador;
    }

    public String getUltimoParticipante() {
        return ultimoParticipante;
    }

    public void setUltimoParticipante(String ultimoParticipante) {
        this.ultimoParticipante = ultimoParticipante;
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

    protected Subastas(Parcel in) {
        nombre = in.readString();
        descripcion = in.readString();
        precio = in.readByte() == 0x00 ? null : in.readDouble();
        creador = in.readString();
        fechaFinal = in.readString();
        getImg = in.readString();
        ultimoParticipante = in.readString();
        key = in.readString();
        idSubasta= in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nombre);
        dest.writeString(descripcion);
        if (precio == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(precio);
        }
        dest.writeString(creador);
        dest.writeString(fechaFinal);
        dest.writeString(getImg);
        dest.writeString(ultimoParticipante);
        dest.writeString(key);
        dest.writeString(idSubasta);

    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Subastas> CREATOR = new Parcelable.Creator<Subastas>() {
        @Override
        public Subastas createFromParcel(Parcel in) {
            return new Subastas(in);
        }

        @Override
        public Subastas[] newArray(int size) {
            return new Subastas[size];
        }
    };

    public String getIdSubasta() {
        return idSubasta;
    }

    public void setIdSubasta(String idSubasta) {
        this.idSubasta = idSubasta;
    }
}