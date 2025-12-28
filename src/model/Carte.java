package model;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Carte {

    private String valeur; 
    private boolean trouvee;
    private Button bouton;

    private Image imageFace;
    private Image imageDos;

    public Carte(String valeur, String dossierTheme, int taille) {
        this.valeur = valeur;
        this.trouvee = false;

        // Construire le chemin complet vers l'image
        String cheminFace = "/images/" + dossierTheme + "/" + valeur;
        String cheminDos  = "/images/" + dossierTheme + "/back.png";

        imageFace = new Image(getClass().getResourceAsStream(cheminFace));
        imageDos  = new Image(getClass().getResourceAsStream(cheminDos));

        // Créer bouton
        bouton = new Button();
        bouton.setPrefSize(taille + 10, taille + 10);

        // Afficher l'image face au début
        ImageView ivFace = new ImageView(imageFace);
        ivFace.setFitWidth(taille);
        ivFace.setFitHeight(taille);
        bouton.setGraphic(ivFace);
    }

    public void montrer(int taille) {
        ImageView iv = new ImageView(imageFace);
        iv.setFitWidth(taille);
        iv.setFitHeight(taille);
        bouton.setGraphic(iv);
    }

    public void cacher(int taille) {
        ImageView iv = new ImageView(imageDos);
        iv.setFitWidth(taille);
        iv.setFitHeight(taille);
        bouton.setGraphic(iv);
    }

    public String getValeur() { return valeur; }
    public Button getBouton() { return bouton; }
    public boolean estTrouvee() { return trouvee; }
    public void setTrouvee(boolean trouvee) { this.trouvee = trouvee; }
}
