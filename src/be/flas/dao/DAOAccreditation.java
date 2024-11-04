package be.flas.dao;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import be.flas.connection.DatabaseConnection;
import be.flas.interfaces.DaoGeneric;
import be.flas.interfaces.IDaoAccreditation;
import be.flas.interfaces.IDaoClasse;
import be.flas.model.Accreditation;
import be.flas.model.Skier;


public class DAOAccreditation extends DaoGeneric<Accreditation> {

	public DAOAccreditation(Connection conn){
    	super(conn);
    }
    @Override
	public boolean delete(Accreditation obj){
	    return false;
	}
    @Override
	public boolean update(Accreditation obj){
	    return false;
	}
    @Override
	public Accreditation find(int id){
    	Accreditation s = new Accreditation();
		return s;
	}
    @Override
    public boolean create(Accreditation obj){
    	return false;
    }

    // Méthodes utilisant 'connection' ici, sans la fermer
    
    public List<String> selectNames() {
        List<String> names = new ArrayList<>();
        String query = "SELECT Names FROM Accreditation";

        try (PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet res = pstmt.executeQuery()) {

            while (res.next()) {
                names.add(res.getString("Names"));
                
            }
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la récupération des noms : " + ex.getMessage());
        }
        return names;
    }
	/*@Override
	public List<String> selectNames() {
        List<String> names = new ArrayList<>();
        String query = "SELECT Names FROM Accreditation"; // Requête SQL

        // Connexion à la base de données via le Singleton
        try (Connection connec = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = connec.prepareStatement(query);
             ResultSet res = pstmt.executeQuery()) {

            // Parcours des résultats
            while (res.next()) {
                names.add(res.getString("Names")); // Récupération du champ Names
            }
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la récupération des noms : " + ex.getMessage());
        }

        return names; // Retourne la liste des noms
    }*/
	    /*
	public List<String> selectNames() {
	    List<String> names = new ArrayList<>();
	    String query = "SELECT Names FROM Accreditation";

	    try (Connection connec = DatabaseConnection.getInstance().getConnection();
	         PreparedStatement pstmt = connec.prepareStatement(query);
	         ResultSet res = pstmt.executeQuery()) {

	        // Récupérer les noms
	        while (res.next()) {
	            names.add(res.getString("Names"));
	        }
	        System.out.println("Les noms des accréditations ont été récupérés avec succès.");

	    } catch (SQLException ex) {
	        System.err.println("Erreur lors de la récupération des noms d'accréditation : " + ex.getMessage());
	    }
	    return names;
	}
*/
	
	/*@Override
	public int selectId(String names) {
	    String query = "SELECT AccreditationId FROM Accreditation WHERE Names=?"; // Requête SQL

	    // Connexion à la base de données via le Singleton
	    try (Connection connec = DatabaseConnection.getInstance().getConnection();
	         PreparedStatement pstmt = connec.prepareStatement(query)) {
	        
	        pstmt.setString(1, names); // Utilise le nom fourni dans l'argument

	        try (ResultSet rs = pstmt.executeQuery()) { // Exécute et gère ResultSet
	            // Vérifie s'il y a un résultat
	            if (rs.next()) {
	                return rs.getInt("AccreditationId"); // Retourne l'ID si trouvé
	            } else {
	                return -1; // Aucune correspondance trouvée
	            }
	        }
	    } catch (SQLException ex) {
	        System.err.println("Erreur lors de la récupération de l'ID : " + ex.getMessage());
	        return -1; // En cas d'exception SQL, retourne -1
	    }
	}*/
    
    public int selectId(String names) {
        String query = "SELECT AccreditationId FROM Accreditation WHERE Names=?"; // Requête SQL

        // Utilise la connexion partagée
        try (PreparedStatement pstmt = connection.prepareStatement(query)) { // Utilise la connexion déjà existante
            pstmt.setString(1, names); // Utilise le nom fourni dans l'argument

            try (ResultSet rs = pstmt.executeQuery()) { // Exécute et gère ResultSet
                // Vérifie s'il y a un résultat
                if (rs.next()) {
                    return rs.getInt("AccreditationId"); // Retourne l'ID si trouvé
                } else {
                    return -1; // Aucune correspondance trouvée
                }
            }
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la récupération de l'ID : " + ex.getMessage());
            return -1; // En cas d'exception SQL, retourne -1
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





}
