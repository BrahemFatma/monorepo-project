package com.pfe.client;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.Data;
import jakarta.persistence.Id; // CORRECT IMPORT

@Entity
@Table(name = "Clients")
@Data
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String mail;
    private String numeroTelephone;
    private String adresse;

    // Constructeur par défaut
    public Client() {
    }

    // Constructeur avec paramètres
    public Client(Long id, String nom, String mail, String numeroTelephone, String adresse) {
        this.id=id;
        this.nom = nom;
        this.mail = mail;
        this.numeroTelephone = numeroTelephone;
        this.adresse = adresse;
    }
    public Long getId() {
        return id;
    }
    // Getter et Setter pour nom
    public void setId(Long id) {
        this.id = id;
    }
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    // Getter et Setter pour mail
    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    // Getter et Setter pour numeroTelephone
    public String getNumeroTelephone() {
        return numeroTelephone;
    }

    public void setNumeroTelephone(String numeroTelephone) {
        this.numeroTelephone = numeroTelephone;
    }

    // Getter et Setter pour adresse
    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    // toString pour faciliter l'affichage
    @Override
    public String toString() {
        return "Client{" +
                "nom='" + nom + '\'' +
                ", mail='" + mail + '\'' +
                ", numeroTelephone='" + numeroTelephone + '\'' +
                ", adresse='" + adresse + '\'' +
                '}';
    }
}
