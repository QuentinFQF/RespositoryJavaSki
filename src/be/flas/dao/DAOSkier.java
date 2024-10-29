package be.flas.dao;

import be.flas.connection.DatabaseConnection;

import be.flas.interfaces.IDaoClasse;
import be.flas.interfaces.IDaoSkier;
import be.flas.model.Skier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DAOSkier implements IDaoSkier<Skier> {

    @Override
    public boolean create(Skier skier) {
        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            // Obtenez l'instance de la connexion à la base de données
            connection = DatabaseConnection.getInstance().getConnection();
            
            // Préparez la requête SQL d'insertion
            String sql = "INSERT INTO Skier (Names, FirstName, Pseudo, DateOfBirth, Assurance) VALUES (?, ?, ?, ?, ?)";
            pstmt = connection.prepareStatement(sql);
            
            // Remplissez les paramètres de la requête
            pstmt.setString(1, skier.getName()); // Supposons que la classe Skier a un getter pour Names
            pstmt.setString(2, skier.getFirstName()); // Supposons que la classe Skier a un getter pour FirstNames
            pstmt.setString(3, skier.getPseudo()); // Supposons que la classe Skier a un getter pour Pseudo
            pstmt.setDate(4, java.sql.Date.valueOf(skier.getDateOfBirth())); // Supposons que DateOfBirth est un LocalDate
            pstmt.setBoolean(5, skier.isAssurance()); // Supposons que la classe Skier a un getter pour Assurance
            
            // Exécutez la requête
            int rowsInserted = pstmt.executeUpdate();
            return rowsInserted > 0; // Retourne true si au moins une ligne a été insérée

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Retourne false en cas d'erreur
        } /*finally {
            // Fermez les ressources
            try {
                if (pstmt != null) pstmt.close();
                if (connection != null) connection.close(); // Vous pourriez également vouloir laisser la connexion ouverte si vous utilisez le singleton
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }*/
    }
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
    }
    
}
