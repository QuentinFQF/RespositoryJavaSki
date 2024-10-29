package be.flas.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import be.flas.connection.DatabaseConnection;
import be.flas.interfaces.IDaoClasse;
import be.flas.model.Instructor;
import be.flas.model.Skier;

public class DAOInstructor implements IDaoClasse<Instructor> {

	public boolean testConnection() {
        try (Connection connection = DatabaseConnection.getInstance().getConnection()) {
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
    }/*
	@Override
    public boolean create(Instructor ins) {
        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            // Obtenez l'instance de la connexion à la base de données
            connection = DatabaseConnection.getInstance().getConnection();
            
            // Préparez la requête SQL d'insertion
            String sql = "INSERT INTO Instructor (Names, FirstName, Pseudo, DateOfBirth) VALUES (?, ?, ?, ?)";
            pstmt = connection.prepareStatement(sql);
            
            // Remplissez les paramètres de la requête
            pstmt.setString(1, ins.getName()); // Supposons que la classe Skier a un getter pour Names
            pstmt.setString(2, ins.getFirstName()); // Supposons que la classe Skier a un getter pour FirstNames
            pstmt.setString(3, ins.getPseudo()); // Supposons que la classe Skier a un getter pour Pseudo
            pstmt.setDate(4, java.sql.Date.valueOf(ins.getDateOfBirth())); // Supposons que DateOfBirth est un LocalDate
          
            
            // Exécutez la requête
            int rowsInserted = pstmt.executeUpdate();
            return rowsInserted > 0; // Retourne true si au moins une ligne a été insérée

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Retourne false en cas d'erreur
        } finally {
            // Fermez les ressources
            try {
                if (pstmt != null) pstmt.close();
                if (connection != null) connection.close(); // Vous pourriez également vouloir laisser la connexion ouverte si vous utilisez le singleton
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override 
    public boolean insertAcc_Instructor(int instructorId, int accreditationId) {
        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = DatabaseConnection.getInstance().getConnection();
            
            // Préparez la requête SQL d'insertion
            String sql = "INSERT INTO Ins_Accreditation (InstructorId, AccreditationId) VALUES (?, ?)";
            pstmt = connection.prepareStatement(sql);
            
            // Remplissez les paramètres de la requête
            pstmt.setInt(1, instructorId);
            pstmt.setInt(2, accreditationId);
            
            // Exécutez la requête
            int rowsInserted = pstmt.executeUpdate();
            return rowsInserted > 0; // Retourne true si au moins une ligne a été insérée

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Retourne false en cas d'erreur
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (connection != null) connection.close(); // Vous pourriez également vouloir laisser la connexion ouverte si vous utilisez le singleton
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public int insertInstructor(Instructor i) {
        String query = "INSERT INTO Instructor (Names, FirstName, Pseudo, DateOfBirth) VALUES (?, ?, ?, ?)";

        try (Connection connec = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = connec.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            // Remplissage des paramètres de la requête en utilisant l'objet Instructor
            pstmt.setString(1, i.getName());
            pstmt.setString(2, i.getFirstName());
            pstmt.setString(3, i.getPseudo());
            pstmt.setDate(4, java.sql.Date.valueOf(i.getDateOfBirth())); // Conversion de LocalDate en java.sql.Date

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
            return -1; // Retourne -1 si aucune ligne n'a été insérée

        } catch (SQLException ex) {
            System.err.println("Erreur lors de l'insertion de l'instructeur : " + ex.getMessage());
            return -1; // Retourne -1 en cas d'erreur
        }
    }

	*/
}
