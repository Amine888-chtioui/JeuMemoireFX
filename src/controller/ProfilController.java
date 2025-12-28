package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.User;
import util.SessionManager;
import dao.UserDAO;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;

import database.DatabaseConnection;

public class ProfilController {

    @FXML private Label labelUsername;
    @FXML private Label labelNiveau;
    @FXML private Label labelScore;
    @FXML private Label labelDate;
    @FXML private Label labelProgression;

    private UserDAO userDAO = new UserDAO();

    @FXML
    public void initialize() {
        chargerProfil();
    }

    private void chargerProfil() {
        User currentUser = SessionManager.getInstance().getCurrentUser();
        
        if (currentUser != null) {
            User updatedUser = userDAO.getUserById(currentUser.getId());
            
            if (updatedUser != null) {
                SessionManager.getInstance().setCurrentUser(updatedUser);
                currentUser = updatedUser;
            }
            
            labelUsername.setText(currentUser.getUsername());
            labelNiveau.setText("Niveau " + currentUser.getNiveau());
            labelScore.setText(currentUser.getScore() + " points");
            
            chargerDateInscription(currentUser.getId());
            
            afficherMessageProgression(currentUser.getNiveau(), currentUser.getScore());
        }
    }

    private void chargerDateInscription(int userId) {
        try (Connection c = DatabaseConnection.getConnection()) {
            String sql = "SELECT date_inscription FROM users WHERE id = ?";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                java.sql.Timestamp timestamp = rs.getTimestamp("date_inscription");
                if (timestamp != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    labelDate.setText(sdf.format(timestamp));
                }
            }
        } catch (Exception e) {
            labelDate.setText("--/--/----");
            e.printStackTrace();
        }
    }

    private void afficherMessageProgression(int niveau, int score) {
        String message;
        
        if (niveau >= 10) {
            message = "🏆 Expert légendaire ! Tu es incroyable !";
        } else if (niveau >= 7) {
            message = "⭐ Maître de la mémoire ! Continue comme ça !";
        } else if (niveau >= 5) {
            message = "💪 Très bon joueur ! Tu progresses bien !";
        } else if (niveau >= 3) {
            message = "🚀 Bon début ! Continue à t'entraîner !";
        } else {
            message = "🎯 Débutant prometteur ! À toi de jouer !";
        }
        
        labelProgression.setText(message);
    }

    @FXML
    public void retourMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/home.fxml"));
            Stage stage = (Stage) labelUsername.getScene().getWindow();
            Scene scene = new Scene(loader.load(), 400, 350);
            stage.setScene(scene);
            stage.setTitle("Menu Principal");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}