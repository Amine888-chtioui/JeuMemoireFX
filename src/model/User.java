package model;

import java.time.LocalDateTime;

public class User {

    private int id;
    private String username;
    private String password;
    private int score;
    private int niveau;
    private LocalDateTime dateInscription; 

    public User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.score = 0;
        this.niveau = 1;
        this.dateInscription = LocalDateTime.now(); 
    }


    // ===== GETTERS & SETTERS =====

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getNiveau() {
        return niveau;
    }

    public void setNiveau(int niveau) {
        this.niveau = niveau;
    }
    
    public LocalDateTime getDateInscription() {
        return dateInscription;
    }

    public void setDateInscription(LocalDateTime dateInscription) {
        this.dateInscription = dateInscription;
    }
}
