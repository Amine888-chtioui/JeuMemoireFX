package database;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {

    public static Connection getConnection() throws Exception {
        String url = "jdbc:mysql://localhost:3306/jeumemoire?useSSL=false&serverTimezone=UTC"; 
        String user = "root"; 
        String password = "";
        return DriverManager.getConnection(url, user, password);
    }

   
    public static void main(String[] args) {
        try {
            Connection c = DatabaseConnection.getConnection();
            System.out.println(" Connexion réussie à la base de données !");
            c.close();
        } catch (Exception e) {
            System.out.println("Erreur de connexion :");
            e.printStackTrace();
        }
    }
}
