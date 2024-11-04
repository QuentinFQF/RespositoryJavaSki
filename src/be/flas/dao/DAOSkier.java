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
	public boolean delete(Skier obj){
	    return false;
	}
    @Override
	public boolean update(Skier obj){
	    return false;
	}
    @Override
	public Skier find(int id){
		Skier s = new Skier();
		return s;
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
        String sql = "SELECT Names, FirstName, Pseudo FROM Skier"; // Adaptation nécessaire selon la structure de votre base de données

        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String nom = rs.getString("Names");
                String prenom = rs.getString("FirstName");
                String pseudo = rs.getString("Pseudo");

                Skier skier = new Skier(nom, prenom, pseudo);
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
    
}
