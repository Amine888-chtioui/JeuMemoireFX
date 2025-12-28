package controller;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Carte;
import model.Theme;
import model.User;
import dao.UserDAO;
import util.SessionManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameController {

    @FXML private GridPane grille;
    @FXML private Label labelChrono, labelLevel, labelScore;
    @FXML private VBox finBox;
    @FXML private Label finMessage;
    @FXML private Button btnRejouer;
    @FXML private HBox barreSousNiveaux;
    @FXML
    private Button btnRetourHome;

    private List<Carte> cartes = new ArrayList<>();
    private Carte premiere = null;
    private Carte deuxieme = null;

    private int niveau = 1;
    private int score = 0;
    private int scoreTotal = 0;
    private int tailleCarte = 100;

    private Timeline chrono;
    private boolean modeIA = false;
    private boolean tourIA = false;
    private boolean blocage = false;

    private UserDAO userDAO = new UserDAO();
   
    @FXML
    private void retourHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/home.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) btnRetourHome.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // ---------------- Sous-niveaux ----------------
    private static class SousNiveau {
        int tempsMemoire;
        int tempsJeu;
        SousNiveau(int tm, int tj) { this.tempsMemoire = tm; this.tempsJeu = tj; }
    }

    private List<SousNiveau> sousNiveaux;
    private int sousNiveauActuel = 0;

    private List<Circle> cerclesSousNiveaux = new ArrayList<>();

    // ---------------- Initialisation ----------------
    @FXML
    public void initialize() {
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser != null) {
            niveau = currentUser.getNiveau();
            scoreTotal = currentUser.getScore();
        }
        demarrerNiveau();
    }

    public void setModeIA(boolean modeIA) {
        this.modeIA = modeIA;
    }

    // ---------------- Gestion des niveaux et sous-niveaux ----------------
    private void initialiserSousNiveaux() {
        sousNiveaux = new ArrayList<>();
        switch (niveau) {
            case 1:
                sousNiveaux.add(new SousNiveau(4, 20));
                sousNiveaux.add(new SousNiveau(3, 15));
                sousNiveaux.add(new SousNiveau(2, 11));
                break;
            case 2:
                sousNiveaux.add(new SousNiveau(4, 20));
                sousNiveaux.add(new SousNiveau(3, 15));
                sousNiveaux.add(new SousNiveau(2, 11));
                break;
            case 3:
                sousNiveaux.add(new SousNiveau(4, 20));
                sousNiveaux.add(new SousNiveau(3, 15));
                sousNiveaux.add(new SousNiveau(2, 11));
                break;
            default:
                sousNiveaux.add(new SousNiveau(3, 20));
                sousNiveaux.add(new SousNiveau(2, 15));
                sousNiveaux.add(new SousNiveau(1, 11));
                break;
        }
        sousNiveauActuel = 0;
    }
// ---------------------cela pour la barre des sous niveux------------
    private void initialiserBarreSousNiveaux() {
        barreSousNiveaux.getChildren().clear();
        cerclesSousNiveaux.clear();

        int nbSousNiveaux = sousNiveaux.size();

        for (int i = 0; i < nbSousNiveaux; i++) {
            Circle cercle = new Circle(15); 
            cercle.setStyle("-fx-fill: lightgray; -fx-stroke: black; -fx-stroke-width: 2;");
            cerclesSousNiveaux.add(cercle);

            barreSousNiveaux.getChildren().add(cercle);

            if (i < nbSousNiveaux - 1) {
                Region espace = new Region();
                HBox.setHgrow(espace, Priority.ALWAYS);
                barreSousNiveaux.getChildren().add(espace);
            }
        }
    }


    private void demarrerNiveau() {
        initialiserSousNiveaux();
        initialiserBarreSousNiveaux();
        demarrerSousNiveau();
    }

    private void demarrerSousNiveau() {
        score = 0;
        premiere = null;
        deuxieme = null;
        blocage = false;
        tourIA = modeIA;

        labelLevel.setText("Niveau " + niveau + " - Sous-niveau " + (sousNiveauActuel + 1));
        labelScore.setText("Score : " + score + " | Total : " + scoreTotal);

        finBox.setVisible(false);

        creerCartes();
        afficherCartesDebut();
    }

    // ---------------- Création des cartes ----------------
    private void creerCartes() {

        cartes.clear();
        grille.getChildren().clear();

        // récupérer le thème choisi
        Theme theme = SessionManager.getInstance().getThemeChoisi();

        // sécurité si jamais aucun thème choisi
        String dossierImages = "Cards"; // par défaut
        if (theme != null) {
            dossierImages = theme.getDossierImages(); // "anim" ou "Cards"
        }

        int totalImages = niveau + 2; // Level 1 → 3 paires
        int rows = (int) Math.ceil(Math.sqrt(totalImages * 2));
        int cols = rows;

        tailleCarte = Math.max(80, 120  - niveau * 10);

        for (int i = 1; i <= totalImages; i++) {
            String nomImage = i + ".png"; // juste le nom
            cartes.add(new Carte(nomImage, dossierImages, tailleCarte));
            cartes.add(new Carte(nomImage, dossierImages, tailleCarte));
        }

        Collections.shuffle(cartes);

        int index = 0;
        for (int i = 0; i < rows && index < cartes.size(); i++) {
            for (int j = 0; j < cols && index < cartes.size(); j++) {

                Carte c = cartes.get(index++);
                grille.add(c.getBouton(), j, i);

                c.getBouton().setOnAction(e -> {
                    if (!modeIA || !tourIA) {
                        clicCarte(c);
                    }
                });
            }
        }
    }


    // ---------------- Affichage initial ----------------
    private void afficherCartesDebut() {
        SousNiveau sn = sousNiveaux.get(sousNiveauActuel);
        int tempsMemoire = sn.tempsMemoire;

        blocage = true; // 🔒 BLOQUE LES CLICS PENDANT MÉMORISATION
        labelChrono.setText("Mémorisez : " + tempsMemoire + "s");

        PauseTransition pause = new PauseTransition(Duration.seconds(tempsMemoire));
        pause.setOnFinished(e -> {
            cacherCartes();
            blocage = false; //  AUTORISE LES CLICS APRÈS MÉMORISATION
            demarrerChronoJeu();

            if (modeIA && tourIA) jouerIA();
        });
        pause.play();
    }


    private void cacherCartes() {
        for (Carte c : cartes) {
            if (!c.estTrouvee()) c.cacher(tailleCarte);
        }
    }

    private void demarrerChronoJeu() {
        SousNiveau sn = sousNiveaux.get(sousNiveauActuel);
        final int[] tempsRestant = {sn.tempsJeu};
        labelChrono.setText("Temps : " + tempsRestant[0] + "s"); // Affiche le temps exact

        if (chrono != null) chrono.stop();

        chrono = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            tempsRestant[0]--;
            labelChrono.setText("Temps : " + tempsRestant[0] + "s"); // Met à jour chaque seconde
            if (tempsRestant[0] <= 0) finSousNiveau(false, "Temps écoulé ⏱️");
        }));
        chrono.setCycleCount(sn.tempsJeu); // Nombre de secondes exactes pour ce sous-niveau
        chrono.play();
    }


    // ---------------- Gestion des clics ----------------
    private void clicCarte(Carte c) {
        if (blocage || c.estTrouvee() || c == premiere) return;
        c.montrer(tailleCarte);

        if (premiere == null) {
            premiere = c;
        } else {
            deuxieme = c;
            verifierCartes();
        }
    }

    private void verifierCartes() {
        if (premiere == null || deuxieme == null) return;

        if (premiere.getValeur().equals(deuxieme.getValeur())) {
            premiere.setTrouvee(true);
            deuxieme.setTrouvee(true);

            score++;
            scoreTotal++;
            labelScore.setText("Score : " + score + " | Total : " + scoreTotal);

            premiere = null;
            deuxieme = null;

            if (score == cartes.size() / 2) {
                finSousNiveau(true, "Bravooo 🎉");
                return;
            }

            if (modeIA && tourIA) jouerIA();

        } else {
            blocage = true;
            PauseTransition pause = new PauseTransition(Duration.seconds(1));
            pause.setOnFinished(e -> {
                if (premiere != null) premiere.cacher(tailleCarte);
                if (deuxieme != null) deuxieme.cacher(tailleCarte);
                premiere = null;
                deuxieme = null;
                blocage = false;

                if (modeIA) tourIA = !tourIA;
                if (modeIA && tourIA) jouerIA();
            });
            pause.play();
        }
    }

    private void jouerIA() {
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(e -> {
            List<Carte> nonTrouvees = new ArrayList<>();
            for (Carte c : cartes) if (!c.estTrouvee()) nonTrouvees.add(c);

            if (nonTrouvees.size() < 2) return;

            Carte c1 = nonTrouvees.get((int)(Math.random() * nonTrouvees.size()));
            Carte c2;
            do { c2 = nonTrouvees.get((int)(Math.random() * nonTrouvees.size())); } 
            while (c1 == c2);

            clicCarte(c1);
            clicCarte(c2);
        });
        pause.play();
    }

    // ---------------- Fin du sous-niveau ----------------
    private void finSousNiveau(boolean reussi, String msg) {
        if (chrono != null) chrono.stop();

        for (Carte c : cartes) {
            c.montrer(tailleCarte);
            c.getBouton().setDisable(true);
        }

        finBox.setVisible(true);
        int totalPaires = cartes.size() / 2;

        // Met à jour la barre
        if (reussi) {
            cerclesSousNiveaux.get(sousNiveauActuel).setStyle("-fx-fill: green; -fx-stroke: black; -fx-stroke-width: 2;");
        } else {
            cerclesSousNiveaux.get(sousNiveauActuel).setStyle("-fx-fill: red; -fx-stroke: black; -fx-stroke-width: 2;");
        }

        if (reussi) {
            finMessage.setText(msg + "\nScore : " + score + "/" + totalPaires + "\n✅ Sous-niveau réussi !");
            btnRejouer.setText("Suivant");
            btnRejouer.setOnAction(e -> {
                sousNiveauActuel++;
                if (sousNiveauActuel >= sousNiveaux.size()) {
                    sauvegarderProgressionNiveau();
                    niveau++;
                    demarrerNiveau();
                } else {
                    demarrerSousNiveau();
                }
            });
        } else {
            finMessage.setText(msg + "\nScore : " + score + "/" + totalPaires + "\n❌ Rejouez !");
            btnRejouer.setText("Rejouer");
            btnRejouer.setOnAction(e -> demarrerSousNiveau());
        }

        sauvegarderProgressionScore();
    }

    // ---------------- Sauvegarde ----------------
    private void sauvegarderProgressionScore() {
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser != null) {
            currentUser.setScore(scoreTotal);
            userDAO.updateUser(currentUser);
        }
    }

    private void sauvegarderProgressionNiveau() {
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser != null) {
            currentUser.setScore(scoreTotal);
            if (niveau >= currentUser.getNiveau()) {
                currentUser.setNiveau(niveau + 1);
            }
            userDAO.updateUser(currentUser);
            System.out.println(" Niveau et score sauvegardés : Niveau " + currentUser.getNiveau() + 
                               ", Score total : " + scoreTotal);
        }
    }
}
