package model;

public class Theme {
    private String nom;
    private String dossierImages;

    public Theme(String nom, String dossierImages) {
        this.nom = nom;
        this.dossierImages = dossierImages;
    }

    public String getNom() {
        return nom;
    }

    public String getDossierImages() {
        return dossierImages;
    }
}
