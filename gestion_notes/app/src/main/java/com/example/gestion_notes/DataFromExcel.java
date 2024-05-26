package com.example.gestion_notes;

import java.util.HashMap;
import java.util.Map;

public class DataFromExcel {
    private String apogee;
    private String nom;
    private String prenom;
    private String session;
    private String matiere;
    private double note;
    private String validation;

    public DataFromExcel() {
        // Required empty constructor for Firebase
    }

    public DataFromExcel(String apogee, String nom, String prenom, String session, String matiere, double note, String validation) {
        this.apogee = apogee;
        this.nom = nom;
        this.prenom = prenom;
        this.session = session;
        this.matiere = matiere;
        this.note = note;
        this.validation = validation;
    }
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("apogee", apogee);
        result.put("nom", nom);
        result.put("prenom", prenom);
        result.put("session", session);
        result.put("matiere", matiere);
        result.put("note", note);
        result.put("validation", validation);
        return result;
    }

    // Getters and setters
    public String getApogee() {
        return apogee;
    }

    public void setApogee(String apogee) {
        this.apogee = apogee;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getMatiere() {
        return matiere;
    }

    public void setMatiere(String matiere) {
        this.matiere = matiere;
    }

    public double getNote() {
        return note;
    }

    public void setNote(double note) {
        this.note = note;
    }

    public String getValidation() {
        return validation;
    }

    public void setValidation(String validation) {
        this.validation = validation;
    }
}
