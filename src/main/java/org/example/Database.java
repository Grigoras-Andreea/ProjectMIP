package org.example;

import java.sql.*;

public class Database {
    final String url = "jdbc:postgresql://localhost:5432/NoteCatalog" ;
    final String user = "postgres";
    final String password = "1q2w3e";

    /**
     * Connect to the PostgreSQL database
     *
     * @return a Connection object
     */
    public Connection connect() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
    public void insertIntoStudent(int id, String nume, String Prenume, int an, String username, String parola){
        String sql = "INSERT INTO \"Student\"(\"ID_Student\", \"Nume\", \"Prenume\", \"An_Studii\", \"Username\", \"Parola\") VALUES(?,?,?,?,?,?)";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, nume);
            pstmt.setString(3, Prenume);
            pstmt.setInt(4, an);
            pstmt.setString(5, username);
            pstmt.setString(6, parola);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insertIntoProfesor(int id, String nume, String Prenume, String username, String parola){
            String sql = "INSERT INTO \"Profesor\"(\"ID_Profesor\", \"Nume\", \"Prenume\", \"Username\", \"Parola\") VALUES(?,?,?,?,?)";
            try (Connection conn = this.connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                pstmt.setString(2, nume);
                pstmt.setString(3, Prenume);
                pstmt.setString(4, username);
                pstmt.setString(5, parola);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

    public int studentLogIn(String username, String parola) {
        String sql = "SELECT * FROM \"Student\" WHERE \"Username\" = ? AND \"Parola\" = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, parola);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Login successful, return the student ID
                    return rs.getInt("ID_Student");
                } else {
                    // Login failed
                    return -1;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return -1;
        }
    }

    public int profesorLogIn(String username, String parola) {
        String sql = "SELECT * FROM \"Profesor\" WHERE \"Username\" = ? AND \"Parola\" = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, parola);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Login successful, return the profesor ID
                    return rs.getInt("ID_Profesor");
                } else {
                    // Login failed
                    return -1;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return -1;
        }
    }

}
