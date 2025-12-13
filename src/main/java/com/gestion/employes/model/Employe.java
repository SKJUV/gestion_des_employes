package com.gestion.employes.model;

import java.time.LocalDate;

public class Employe {
    private int id;
    private String nom;
    private String prenom;
    private LocalDate dateEmbauche;
    private String poste;
    private double salaire;
    private String service;
    private Role role;

    public Employe() {
    }

    public Employe(int id, String nom, String prenom, LocalDate dateEmbauche, String poste, double salaire, String service, Role role) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.dateEmbauche = dateEmbauche;
        this.poste = poste;
        this.salaire = salaire;
        this.service = service;
        this.role = role;
    }

    public Employe(String nom, String prenom, LocalDate dateEmbauche, String poste, double salaire, String service, Role role) {
        this.nom = nom;
        this.prenom = prenom;
        this.dateEmbauche = dateEmbauche;
        this.poste = poste;
        this.salaire = salaire;
        this.service = service;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public LocalDate getDateEmbauche() {
        return dateEmbauche;
    }

    public void setDateEmbauche(LocalDate dateEmbauche) {
        this.dateEmbauche = dateEmbauche;
    }

    public String getPoste() {
        return poste;
    }

    public void setPoste(String poste) {
        this.poste = poste;
    }

    public double getSalaire() {
        return salaire;
    }

    public void setSalaire(double salaire) {
        this.salaire = salaire;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
