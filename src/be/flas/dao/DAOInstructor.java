package be.flas.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import be.flas.interfaces.IDaoClasse;
import be.flas.model.Instructor;

public class DAOInstructor implements IDaoClasse<Instructor> {

    private final Connection connection;

    // Constructeur pour initialiser avec la connexion partagée
    public DAOInstructor(Connection connection) {
        this.connection = connection;
    }

    public boolean testConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                System.out.println("Connexion établie avec succès !");
                return true;
            } else {
                System.out.println("Échec de la connexion.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la connexion : " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean create(Instructor ins) {
        PreparedStatement pstmt = null;
        try {
            // Préparez la requête SQL d'insertion
            String sql = "INSERT INTO Instructor (Names, FirstName, Pseudo, DateOfBirth) VALUES (?, ?, ?, ?)";
            pstmt = connection.prepareStatement(sql);

            // Remplissez les paramètres de la requête
            pstmt.setString(1, ins.getName());
            pstmt.setString(2, ins.getFirstName());
            pstmt.setString(3, ins.getPseudo());
            pstmt.setDate(4, java.sql.Date.valueOf(ins.getDateOfBirth()));

            // Exécutez la requête
            int rowsInserted = pstmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean insertAcc_Instructor(int instructorId, int accreditationId) {
        PreparedStatement pstmt = null;
        try {
            // Préparez la requête SQL d'insertion
            String sql = "INSERT INTO Ins_Accreditation (InstructorId, AccreditationId) VALUES (?, ?)";
            pstmt = connection.prepareStatement(sql);

            // Remplissez les paramètres de la requête
            pstmt.setInt(1, instructorId);
            pstmt.setInt(2, accreditationId);

            // Exécutez la requête
            int rowsInserted = pstmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public int insertInstructor(Instructor i) {
        String query = "INSERT INTO Instructor (Names, FirstName, Pseudo, DateOfBirth) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            // Remplissage des paramètres de la requête en utilisant l'objet Instructor
            pstmt.setString(1, i.getName());
            pstmt.setString(2, i.getFirstName());
            pstmt.setString(3, i.getPseudo());
            pstmt.setDate(4, java.sql.Date.valueOf(i.getDateOfBirth()));

            // Exécution de la requête
            int rowsInserted = pstmt.executeUpdate();

            // Vérifie si l'insertion a réussi et récupère l'ID généré
            if (rowsInserted > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1); // Retourne l'ID de l'instructeur inséré
                    }
                }
            }
            return -1;

        } catch (SQLException ex) {
            System.err.println("Erreur lors de l'insertion de l'instructeur : " + ex.getMessage());
            return -1;
        }
    }
}
