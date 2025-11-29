package com.pfe.client;

public class ClientResponse {

    private Long id;
    private String nom;
    private String mail;
    private String numeroTelephone;
    private String adresse;

    public ClientResponse(Client client) {
        this.id = client.getId();
        this.nom = client.getNom();
        this.mail = client.getMail();
        this.numeroTelephone = client.getNumeroTelephone();
        this.adresse = client.getAdresse();
    }

    public ClientResponse(Long id, String nom, String mail, String numeroTelephone, String adresse) {
        this.id = id;
        this.nom = nom;
        this.mail = mail;
        this.numeroTelephone = numeroTelephone;
        this.adresse = adresse;
    }

    public static class Builder {
        private Long id;
        private String nom;
        private String mail;
        private String numeroTelephone;
        private String adresse;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder nom(String nom) {
            this.nom = nom;
            return this;
        }

        public Builder mail(String mail) {
            this.mail = mail;
            return this;
        }

        public Builder numeroTelephone(String numeroTelephone) {
            this.numeroTelephone = numeroTelephone;
            return this;
        }

        public Builder adresse(String adresse) {
            this.adresse = adresse;
            return this;
        }

        public ClientResponse build() {
            return new ClientResponse(id, nom, mail, numeroTelephone, adresse);
        }
    }

    public static ClientResponse createClient(Long id, String nom, String mail, String numeroTelephone, String adresse) {
        return new Builder()
                .id(id)
                .nom(nom)
                .mail(mail)
                .numeroTelephone(numeroTelephone)
                .adresse(adresse)
                .build();
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getNumeroTelephone() {
        return numeroTelephone;
    }

    public void setNumeroTelephone(String numeroTelephone) {
        this.numeroTelephone = numeroTelephone;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
}
