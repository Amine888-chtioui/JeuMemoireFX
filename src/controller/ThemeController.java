package controller;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import util.SceneLoader;
import util.SessionManager;
import model.Theme;

public class ThemeController {

    @FXML private ImageView imgAnim;
    @FXML private ImageView imgCards;
    @FXML private ImageView imgEsthetique;

    @FXML
    public void initialize() {
        imgAnim.setImage(new Image(getClass().getResourceAsStream("/images/anim/cover.png")));
        imgCards.setImage(new Image(getClass().getResourceAsStream("/images/Cards/cover.png")));
        imgEsthetique.setImage(new Image(getClass().getResourceAsStream("/images/esthetique/cover.png")));
    }

    @FXML
    private void themeAnim() { choisirTheme("Animaux", "anim"); }

    @FXML
    private void themeCards() { choisirTheme("Cartes", "Cards"); }

    @FXML
    private void themeEsthetique() { choisirTheme("Esthétique", "esthetique"); }

    private void choisirTheme(String nom, String dossier) {
        SessionManager.getInstance().setThemeChoisi(new Theme(nom, dossier));
        SceneLoader.load("/ui/home.fxml");
    }
}
