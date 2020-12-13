package com.marquelo.getstars.working;

public class User {
    //Atributo
    private String nombre;
    private String email;
    private String sexo;
    private int dia;
    private int mes;
    private int year;
    private String password;
    private boolean isStar;
    private boolean isPro;


    public User(String nombre, String email, String sexo, int dia, int mes, int year, String password) {
        this.nombre = nombre;
        this.email = email;
        this.sexo = sexo;
        this.dia = dia;
        this.mes = mes;
        this.year = year;
        this.password = password;
        isStar = false;
        isPro = false;
    }

    private boolean validarFecha(int dia, int mes, int año){
        return false;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
