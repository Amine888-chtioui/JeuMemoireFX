package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import database.DatabaseConnection;
import model.User;

public class UserDAO {

    // ================= REGISTER =================
    public boolean registerUser(User user) {
        try (Connection c = DatabaseConnection.getConnection()) {

            // Vérifier username
            String checkSql = "SELECT COUNT(*) FROM users WHERE username = ?";
            PreparedStatement checkPs = c.prepareStatement(checkSql);
            checkPs.setString(1, user.getUsername());
            ResultSet rs = checkPs.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                return false;
            }
           
            // INSERT user
            String sql = "INSERT INTO users(username, password, score, niveau, date_inscription) "
                       + "VALUES (?, ?, ?, ?, ?)";

            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setInt(3, user.getScore());
            ps.setInt(4, user.getNiveau());
            ps.setTimestamp(5, Timestamp.valueOf(user.getDateInscription())); 

            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ================= LOGIN =================
    public User loginUser(String username, String password) {
        try (Connection c = DatabaseConnection.getConnection()) {

            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setScore(rs.getInt("score"));
                user.setNiveau(rs.getInt("niveau"));
                user.setDateInscription(
                    rs.getTimestamp("date_inscription").toLocalDateTime()
                );
                return user;
            }

            return null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ================= UPDATE =================
    public void updateUser(User user) {
        try (Connection c = DatabaseConnection.getConnection()) {

            String sql = "UPDATE users SET score = ?, niveau = ? WHERE id = ?";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, user.getScore());
            ps.setInt(2, user.getNiveau());
            ps.setInt(3, user.getId());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= GET BY ID =================
    public User getUserById(int id) {
        try (Connection c = DatabaseConnection.getConnection()) {

            String sql = "SELECT * FROM users WHERE id = ?";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setScore(rs.getInt("score"));
                user.setNiveau(rs.getInt("niveau"));
                user.setDateInscription(
                    rs.getTimestamp("date_inscription").toLocalDateTime()
                );
                return user;
            }

            return null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
