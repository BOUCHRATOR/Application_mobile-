package com.example.gestion_notes;


public class HelperClass {

    String  email, password;



    public String getNom() {
        return email;
    }

    public void setNom(String nom) {
        this.email = nom;
    }





    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public HelperClass(String name, String nom, String username, String password) {

        this.email = nom;

        this.password = password;
    }

    public HelperClass() {
    }
}