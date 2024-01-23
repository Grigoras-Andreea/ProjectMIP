package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

public class Database {
    final String url = "jdbc:postgresql://localhost:5432/ProiectMIP" ;
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

    private boolean checkIfUsernameExistsForStudent(String username) {
        String sql = "SELECT * FROM \"Student\" WHERE \"Username\" = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Username already exists
                    return true;
                } else {
                    // Username does not exist
                    return false;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    private boolean checkIfUsernameExistsForProfesor(String username) {
        String sql = "SELECT * FROM \"Profesor\" WHERE \"Username\" = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Username already exists
                    return true;
                } else {
                    // Username does not exist
                    return false;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean checkIfUsernameExists(String username) {
        return checkIfUsernameExistsForStudent(username) || checkIfUsernameExistsForProfesor(username);
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

    public void insertNota(int idStudent, int ID_Disciplina, int nota) {
        String sql = "INSERT INTO \"Note\"(\"ID_Disciplina\",\"ID_Student\", \"Data\",\"Nota\") VALUES(?,?,?,?)";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, ID_Disciplina);
            pstmt.setInt(2, idStudent);
            pstmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            pstmt.setInt(4, nota);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insertIntoDisciplina(int ID_Disciplina, double medie, int idProfesor, String numeDisciplina){
        String sql = "INSERT INTO \"Disciplina\"(\"ID_Disciplina\",\"Medie\",\"ID_Profesor\",\"Nume_disciplina\") VALUES(?,?,?,?)";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, ID_Disciplina);
            pstmt.setDouble(2, medie);
            pstmt.setInt(3, idProfesor);
            pstmt.setString(4, numeDisciplina);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insertDataFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Assuming data in the file is comma-separated
                String[] data = line.split(",");
                // Assuming the data structure is: id, nume, prenume, an, username, parola
                int id = Integer.parseInt(data[0]);
                String nume = data[1];
                String prenume = data[2];
                int an = Integer.parseInt(data[3]);
                String username = data[4];
                String parola = data[5];

                // Call the existing insertIntoStudent method to insert data into the database
                insertIntoStudent(id, nume, prenume, an, username, parola);
            }
        } catch (IOException e) {
            e.printStackTrace();
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

    public int GetStudentLastID(){
        String sql = "SELECT * FROM \"Student\" ORDER BY \"ID_Student\" DESC LIMIT 1";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {

                    return rs.getInt("ID_Student");
                } else {

                    return -1;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return -1;
        }
    }

    public int GetProfesorLastID(){
        String sql = "SELECT * FROM \"Profesor\" ORDER BY \"ID_Profesor\" DESC LIMIT 1";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {

                    return rs.getInt("ID_Profesor");
                } else {

                    return -1;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return -1;
        }
    }
    public void showStudentGrades(int studentId) {
        String sql = "SELECT D.\"Nume_disciplina\" AS Subject, N.\"Nota\" AS Grade, N.\"Data\" AS Date " +
                "FROM \"Note\" N " +
                "JOIN \"Disciplina\" D ON N.\"ID_Disciplina\" = D.\"ID_Disciplina\" " +
                "WHERE N.\"ID_Student\" = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, studentId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String subject = rs.getString("Subject");
                    int grade = rs.getInt("Grade");
                    Timestamp date = rs.getTimestamp("Date");

                    System.out.println("Subject: " + subject + ", Grade: " + grade + ", Date: " + date);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
