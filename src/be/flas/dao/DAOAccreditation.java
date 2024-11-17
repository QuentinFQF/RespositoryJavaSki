package be.flas.dao;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import be.flas.connection.DatabaseConnection;
import be.flas.interfaces.DaoGeneric;
import be.flas.interfaces.IDaoAccreditation;
import be.flas.interfaces.IDaoClasse;
import be.flas.model.Accreditation;
import be.flas.model.LessonType;
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
    
    /*public List<Accreditation> selectNames() {
        List<Accreditation> names = new ArrayList<>();
        String query = "SELECT AccreditationId,Names FROM Accreditation";

        try (PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet res = pstmt.executeQuery()) {

            while (res.next()) {
                names.add(res.getString("Names"));
                
            }
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la récupération des noms : " + ex.getMessage());
        }
        return names;
    }*/
    /*public List<Accreditation> selectNames() {
        List<Accreditation> accreditations = new ArrayList<>();
        String query = "SELECT AccreditationId, Names FROM Accreditation";

        try (PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet res = pstmt.executeQuery()) {

            while (res.next()) {
                int id = res.getInt("AccreditationId");
                String name = res.getString("Names");

                // Création d'un objet Accreditation
                Accreditation accreditation = new Accreditation(id, name);
                accreditations.add(accreditation);
            }
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la récupération des accréditations : " + ex.getMessage());
        }
        return accreditations;
    }*/
    public List<Accreditation> getAll() {
        List<Accreditation> accreditations = new ArrayList<>();
        // Requête SQL pour récupérer les accréditations avec les données associées de LessonType
        String query = "SELECT a.AccreditationId, a.Names, l.LessonTypeId, l.Levels, l.Price, l.Sport, l.AgeCategory " +
                       "FROM Accreditation a " +
                       "LEFT JOIN LessonType l ON a.AccreditationId = l.AccreditationId";

        try (PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet res = pstmt.executeQuery()) {

            // Map pour éviter de dupliquer les accréditations
            Map<Integer, Accreditation> accreditationMap = new HashMap<>();

            while (res.next()) {
                int accreditationId = res.getInt("AccreditationId");
                String accreditationName = res.getString("Names");

                // Récupérer l'accréditation existante dans le map ou la créer
                Accreditation accreditation = accreditationMap.get(accreditationId);
                if (accreditation == null) {
                    accreditation = new Accreditation(accreditationId, accreditationName);
                    accreditationMap.put(accreditationId, accreditation);
                }

                // Vérifier s'il y a des données sur le LessonType (s'il existe)
                int lessonTypeId = res.getInt("LessonTypeId");
                if (lessonTypeId != 0) {
                    String levels = res.getString("Levels");
                    double price = res.getDouble("Price");
                    String sport = res.getString("Sport");
                    String ageCategory = res.getString("AgeCategory");

                    // Créer un objet LessonType avec les informations
                    LessonType lessonType = new LessonType(lessonTypeId, levels, sport, price, ageCategory, accreditation);

                    // Ajouter le LessonType à l'accréditation correspondante
                    accreditation.AddLessonType(lessonType);
                }
            }

            // Ajouter toutes les accréditations au résultat final
            accreditations.addAll(accreditationMap.values());

        } catch (SQLException ex) {
            System.err.println("Erreur lors de la récupération des accréditations : " + ex.getMessage());
        }

        return accreditations;
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
    
    
    
    public List<Accreditation> selectAccreditationsNotIns(int instructorId) {
        List<Accreditation> accreditations = new ArrayList<>();
        
        // Requête SQL pour récupérer les accréditations non associées à l'instructeur donné
        String query = """
            SELECT 
                a.AccreditationId, a.Names, a.AgeCategory, a.SportType,
                lt.LessonTypeId, lt.Levels, lt.Price
            FROM Accreditation a
            LEFT JOIN Acc_LessonType alt ON a.AccreditationId = alt.AccreditationId
            LEFT JOIN LessonType lt ON alt.LessonTypeId = lt.LessonTypeId
            WHERE a.AccreditationId NOT IN (
                SELECT ia.AccreditationId
                FROM Ins_Accreditation ia
                WHERE ia.InstructorId = ?
            )
            ORDER BY a.AccreditationId;
        """;

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            // Affectation du paramètre de l'ID de l'instructeur
            pstmt.setInt(1, instructorId);

            try (ResultSet res = pstmt.executeQuery()) {
                Map<Integer, Accreditation> accreditationMap = new HashMap<>();

                while (res.next()) {
                    int accreditationId = res.getInt("AccreditationId");
                    String accreditationName = res.getString("Names");
                    String ageCategory = res.getString("AgeCategory");
                    String sportType = res.getString("SportType");
                    
                    int lessonTypeId = res.getInt("LessonTypeId");
                    String levels = res.getString("Levels");
                    double price = res.getDouble("Price");

                    // Vérifier si l'accréditation existe déjà dans la map
                    Accreditation accreditation = accreditationMap.getOrDefault(
                            accreditationId, 
                            new Accreditation(accreditationId, accreditationName,null, sportType, ageCategory)
                    );

                    // Ajouter le type de leçon à l'accréditation
                    if (lessonTypeId != 0) {
                        LessonType lessonType = new LessonType(lessonTypeId, levels, price);
                        accreditation.AddLessonType(lessonType);
                    }

                    // Mettre à jour la map
                    accreditationMap.put(accreditationId, accreditation);
                }

                // Ajouter les accréditations collectées à la liste finale
                accreditations.addAll(accreditationMap.values());
            }

        } catch (SQLException ex) {
            System.err.println("Erreur lors de la récupération des accréditations : " + ex.getMessage());
        }

        return accreditations;
    }
   









}
