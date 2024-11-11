package be.flas.dao;

import be.flas.connection.DatabaseConnection;
import be.flas.interfaces.DaoGeneric;
import be.flas.interfaces.IDaoClasse;
import be.flas.interfaces.IDaoSkier;
import be.flas.model.Skier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAOSkier extends DaoGeneric<Skier> {


    
    public DAOSkier(Connection conn){
    	super(conn);
    }
    @Override
    public boolean delete(Skier obj) {
        String deleteBookingsSql = "DELETE FROM Booking WHERE SkierId = ?";
        String deleteSkierSql = "DELETE FROM Skier WHERE SkierId = ?";
        
        try {
            // Commencez une transaction
            connection.setAutoCommit(false);

            // Supprimer les réservations associées
            try (PreparedStatement pstmtBookings = connection.prepareStatement(deleteBookingsSql)) {
                pstmtBookings.setInt(1, obj.getPersonId());
                pstmtBookings.executeUpdate();
            }

            // Supprimer le skieur
            try (PreparedStatement pstmtSkier = connection.prepareStatement(deleteSkierSql)) {
                pstmtSkier.setInt(1, obj.getPersonId());
                int affectedRows = pstmtSkier.executeUpdate();

                // Valider la transaction si tout s'est bien passé
                connection.commit();
                
                return affectedRows > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du skieur et de ses réservations : " + e.getMessage());
            try {
                connection.rollback(); // Annule la transaction en cas d'erreur
            } catch (SQLException rollbackEx) {
                System.err.println("Erreur lors du rollback : " + rollbackEx.getMessage());
            }
        } finally {
            try {
                connection.setAutoCommit(true); // Réactiver l'auto-commit
            } catch (SQLException ex) {
                System.err.println("Erreur lors de la réactivation de l'auto-commit : " + ex.getMessage());
            }
        }

        return false;
    }

    @Override
    public boolean update(Skier s) {
        String sql = "UPDATE Skier SET Pseudo=?, FirstName=?, Names=?, DateOfBirth=? WHERE SkierId=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, s.getPseudo());
            stmt.setString(2, s.getFirstName());
            stmt.setString(3, s.getName());
            stmt.setInt(5, s.getPersonId());
            stmt.setDate(4, java.sql.Date.valueOf(s.getDateOfBirth()));
            
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public Skier find(int id) {
        Skier skier = null;

        // Requête SQL pour récupérer un skieur par son ID
        String sql = "SELECT SkierId, Names, FirstName, Pseudo, DateOfBirth, Assurance " +
                     "FROM Skier " +
                     "WHERE SkierId = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id); // Définir le paramètre ID

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Création de l'objet `Skier`
                    skier = new Skier(
                        
                        rs.getString("Names"),
                        rs.getString("FirstName"),
                        rs.getInt("SkierId"),
                        
                        rs.getDate("DateOfBirth").toLocalDate(), 
                        rs.getString("Pseudo"),
                        rs.getBoolean("Assurance")
                    );
                }
            }
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la recherche du skieur avec ID " + id + " : " + ex.getMessage());
        }

        return skier;
    }

 
    @Override
    public boolean create(Skier skier) {
        String sql = "INSERT INTO Skier (Names, FirstName, Pseudo, DateOfBirth, Assurance) VALUES (?, ?, ?, ?, ?)";

        // Utilisation de try-with-resources pour garantir la fermeture des ressources
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            
            // Remplissage des paramètres de la requête
            pstmt.setString(1, skier.getName()); // Getter pour le nom
            pstmt.setString(2, skier.getFirstName()); // Getter pour le prénom
            pstmt.setString(3, skier.getPseudo()); // Getter pour le pseudo
            pstmt.setDate(4, java.sql.Date.valueOf(skier.getDateOfBirth())); // Getter pour la date de naissance (LocalDate)
            pstmt.setBoolean(5, skier.isAssurance()); // Getter pour l'assurance

            // Exécution de la requête
            int rowsInserted = pstmt.executeUpdate();
            return rowsInserted > 0; // Retourne true si une ligne a été insérée

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Retourne false en cas d'erreur
        }
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
    
   
    public List<Skier> getAllSkiers() {
        List<Skier> skiers = new ArrayList<>();
        String sql = "SELECT SkierId,Names, FirstName, Pseudo FROM Skier"; // Adaptation nécessaire selon la structure de votre base de données

        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
            	int id = rs.getInt("SkierId");
                String nom = rs.getString("Names");
                String prenom = rs.getString("FirstName");
                String pseudo = rs.getString("Pseudo");

                Skier skier = new Skier(id,nom, prenom, pseudo);
                skiers.add(skier);
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des skieurs : " + e.getMessage());
        }

        return skiers;
    }
    
    public int getSkierIdByName(String name){
        String sql = "SELECT Skierid FROM Skier WHERE Pseudo = ?"; // Ajustez la requête selon votre schéma de table
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("SkierId"); 
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'ID de l'instructeur : " + e.getMessage());
            
        }
        return -1; 
    }
    public Skier getSkierByPseudo(String pseudo) {
        String sql = "SELECT SkierId, Names, FirstName, Pseudo, DateOfBirth FROM Skier WHERE Pseudo = ?"; 
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, pseudo);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Skier skier = new Skier();
                skier.setPersonId(resultSet.getInt("SkierId"));
                skier.setName(resultSet.getString("Names"));
                skier.setFirstName(resultSet.getString("FirstName"));
                skier.setPseudo(resultSet.getString("Pseudo"));
                skier.setDateOfBirth(resultSet.getDate("DateOfBirth").toLocalDate());
                
                return skier;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du skieur : " + e.getMessage());
        }
        return null; // Retourne null si aucun résultat n'est trouvé ou en cas d'erreur
    }

    
}
