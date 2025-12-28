package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import util.SceneLoader;
import util.SessionManager;
import model.User;

import java.io.IOException;
import java.util.Optional;

public class HomeController {

    @FXML private Label labelWelcome;
    @FXML private ImageView gifView;
    @FXML private VBox profileMenu;
    @FXML private ImageView avatarProfile;
    

    @FXML
    private void toggleProfileMenu() {
        profileMenu.setVisible(!profileMenu.isVisible());
    }
    @FXML
    public void initialize() {
    	 Rectangle clip = new Rectangle(
    		        avatarProfile.getFitWidth(), avatarProfile.getFitHeight()
    		    );
    		    clip.setArcWidth(avatarProfile.getFitWidth());
    		    clip.setArcHeight(avatarProfile.getFitHeight());
    		    avatarProfile.setClip(clip);
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser != null && labelWelcome != null) {
            labelWelcome.setText("Bienvenue " + currentUser.getUsername() + " ! ");
        }

     // Charger le GIF animé
        try {
            Image gif = new Image(getClass().getResource("/images/satart.gif").toExternalForm());
            gifView.setImage(gif);
        } catch (Exception e) {
            System.out.println("Erreur chargement GIF : " + e.getMessage());
        }
    }

    @FXML
    private void retourTheme() {
        SceneLoader.load("/ui/theme_selection.fxml");
    }

    @FXML
    public void commencerJeu() {
        lancerJeu(false);
    }

    @FXML
    public void jouerContreIA() {
        lancerJeu(true);
    }

    @FXML
    public void afficherProfil() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/profil.fxml"));
            Stage stage = (Stage) labelWelcome.getScene().getWindow();
            Scene scene = new Scene(loader.load(), 450, 500);
            stage.setScene(scene);
            stage.setTitle("Mon Profil");
        } catch (IOException e) {
            e.printStackTrace();
            afficherErreur("Impossible d'ouvrir le profil.");
        }
    }

    @FXML
    public void deconnexion() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Déconnexion");
        alert.setHeaderText("Voulez-vous vraiment vous déconnecter ?");
        alert.setContentText("Votre progression est sauvegardée.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            SessionManager.getInstance().logout();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/login.fxml"));
                Stage stage = (Stage) labelWelcome.getScene().getWindow();
                Scene scene = new Scene(loader.load(), 400, 350);
                stage.setScene(scene);
                stage.setTitle("Connexion");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void quitterJeu() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Quitter");
        alert.setHeaderText("Voulez-vous vraiment quitter le jeu ?");
        alert.setContentText("Votre progression est sauvegardée.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Platform.exit(); 
            System.exit(0);
        }
    }

    private void lancerJeu(boolean modeIA) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/game.fxml"));
            Stage stage = (Stage) labelWelcome.getScene().getWindow();
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.setTitle("Jeu de Mémoire");
            stage.setMaximized(true);

            // Contrôleur du jeu
            GameController controller = loader.getController();
            controller.setModeIA(modeIA);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            afficherErreur("Impossible de lancer le jeu.");
        }
    }

    private void afficherErreur(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText("Une erreur est survenue");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
