package controller;

import java.io.File;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import dao.UserDAO;
import model.User;

public class RegisterController {

    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private PasswordField txtConfirmPassword;
    @FXML private Label labelMessage;

    private UserDAO userDAO = new UserDAO();

    // ================= VIDEO =================
    @FXML private StackPane videoContainer;
    @FXML private MediaView mediaView;
    private MediaPlayer mediaPlayer;

    @FXML
    public void initialize() {
        chargerVideo();
    }
    private void chargerVideo() {
        File videoFile = new File("media/register.mp4");
        if (!videoFile.exists()) {
            System.out.println("Vidéo non trouvée : media/register.mp4");
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
    public void handleRegister() {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();
        String confirmPassword = txtConfirmPassword.getText().trim();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            labelMessage.setText("⚠️ Veuillez remplir tous les champs");
            return;
        }

        if (username.length() < 3) {
            labelMessage.setText("⚠️ Le username doit contenir au moins 3 caractères");
            return;
        }

        if (password.length() < 4) {
            labelMessage.setText("⚠️ Le mot de passe doit contenir au moins 4 caractères");
            return;
        }

        if (!password.equals(confirmPassword)) {
            labelMessage.setText("⚠️ Les mots de passe ne correspondent pas");
            return;
        }

        User newUser = new User(username, password);
        boolean success = userDAO.registerUser(newUser);

        if (success) {
            labelMessage.setText("✅ Inscription réussie! Vous pouvez vous connecter.");
            labelMessage.setStyle("-fx-text-fill: green;");

            new Thread(() -> {
                try {
                    Thread.sleep(2000);
                    javafx.application.Platform.runLater(() -> goToLogin());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();

        } else {
            labelMessage.setText("❌ Ce username existe déjà");
            labelMessage.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    public void goToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/login.fxml"));
            Stage stage = (Stage) txtUsername.getScene().getWindow();
            Scene scene = new Scene(loader.load(), 400, 350);
            stage.setScene(scene);
            stage.setTitle("Connexion");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
