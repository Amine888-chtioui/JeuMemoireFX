package controller;

import java.io.File;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.layout.StackPane;

import dao.UserDAO;
import model.User;
import util.SceneLoader;
import util.SessionManager;

public class LoginController {

    // ================= LOGIN =================
    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private Label labelMessage;

    private UserDAO userDAO = new UserDAO();

    // ================= VIDEO =================
    @FXML private StackPane videoContainer;
    @FXML private MediaView mediaView;
    private MediaPlayer mediaPlayer;

    // ================= INITIALIZE =================
    @FXML
    public void initialize() {
        chargerVideo();
    }

    private void chargerVideo() {
        File videoFile = new File("media/login.mp4");
        if (!videoFile.exists()) {
            System.out.println("Vidéo non trouvée : media/login.mp4");
            return;
        }

        Media media = new Media(videoFile.toURI().toString());
        mediaPlayer = new MediaPlayer(media);

        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.setMute(true);
        mediaPlayer.setAutoPlay(true);

        mediaView.setMediaPlayer(mediaPlayer);
    }


    // ================= ACTIONS =================
    @FXML
    public void handleLogin() {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            labelMessage.setText(" Veuillez remplir tous les champs");
            labelMessage.setStyle("-fx-text-fill: orange;");
            return;
        }

        User user = userDAO.loginUser(username, password);

        if (user != null) {

            
            SessionManager.getInstance().setCurrentUser(user);

            System.out.println("📅 Date inscription : " + user.getDateInscription());

            labelMessage.setText(" Connexion réussie !");
            labelMessage.setStyle("-fx-text-fill: green;");

            ouvrirHome();
        } else {
            labelMessage.setText("❌ Username ou mot de passe incorrect");
            labelMessage.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    public void goToRegister() {
        SceneLoader.load("/ui/register.fxml");
    }

    private void ouvrirHome() {
        SceneLoader.load("/ui/theme_selection.fxml");
    }
}
