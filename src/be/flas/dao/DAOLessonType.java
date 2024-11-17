package be.flas.dao;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import be.flas.interfaces.DaoGeneric;
import be.flas.interfaces.IDaoAccreditation;
import be.flas.interfaces.IDaoLessonType;
import be.flas.model.Accreditation;
import be.flas.model.Instructor;
import be.flas.model.LessonType;

public class DAOLessonType extends DaoGeneric<LessonType>{

	public DAOLessonType(Connection conn){
    	super(conn);
    }
    @Override
	public boolean delete(LessonType obj){
	    return false;
	}
    @Override
	public boolean update(LessonType obj){
	    return false;
	}
    
    /*@Override
    public LessonType find(int id) {
        LessonType lessonType = null;

        // Requête SQL pour récupérer un type de leçon
        String sql = "SELECT LessonTypeId, Levels, Price, Sport, AgeCategory " +
                     "FROM LessonType " +
                     "WHERE LessonTypeId = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id); // Définir le paramètre ID

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Création de l'objet `LessonType`
                    lessonType = new LessonType(
                        rs.getInt("LessonTypeId"),
                        rs.getString("Levels"),
                        rs.getString("Sport"),
                        rs.getDouble("Price"),
                        rs.getString("AgeCategory")
                    );
                }
            }
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la recherche du type de leçon avec ID " + id + " : " + ex.getMessage());
        }

        return lessonType;
    }*/
    @Override
    public LessonType find(int id) {
        LessonType lessonType = null;

        // Requête SQL pour récupérer un type de leçon avec les informations d'accréditation
        String sql = """
            SELECT lt.LessonTypeId, lt.Levels, lt.Price,
                   a.AccreditationId, a.Names AS AccreditationName, a.SportType, a.AgeCategory
            FROM LessonType lt
            LEFT JOIN Accreditation a ON lt.AccreditationId = a.AccreditationId
            WHERE lt.LessonTypeId = ?
        """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id); // Définir le paramètre ID

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Création de l'objet `Accreditation`
                    Accreditation accreditation = new Accreditation(
                        rs.getInt("AccreditationId"),
                        rs.getString("AccreditationName"),
                        rs.getString("SportType"),
                        rs.getString("AgeCategory")
                    );

                    // Création de l'objet `LessonType`
                    lessonType = new LessonType(
                        rs.getInt("LessonTypeId"),
                        rs.getString("Levels"),
                        rs.getDouble("Price"),
                   
                        accreditation // Liaison de l'accréditation avec le LessonType
                    );
                    
                    // Ajouter le LessonType à la liste de LessonTypes de l'accréditation
                    accreditation.AddLessonType(lessonType);
                }
            }
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la recherche du type de leçon avec ID " + id + " : " + ex.getMessage());
        }

        return lessonType;
    }


    @Override
    public boolean create(LessonType obj){
    	return false;
    }
    /*
    public List<LessonType> selectLessonTypes() {
        List<LessonType> lessonTypes = new ArrayList<>();
        String query = "SELECT LessonTypeId, Levels, Price, Sport, AgeCategory FROM LessonType";

        try (PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet res = pstmt.executeQuery()) {

            while (res.next()) {
                // Récupérer les valeurs des colonnes
                int lessonTypeId = res.getInt("LessonTypeId");
                String levels = res.getString("Levels");
                double price = res.getDouble("Price");
                String sport = res.getString("Sport");
                String ageCategory = res.getString("AgeCategory");

                // Créer une instance de LessonType
                LessonType lessonType = new LessonType(lessonTypeId, levels,sport, price,ageCategory);
                lessonType.setSport(sport);
                lessonType.setAgeCategory(ageCategory);

                // Ajouter à la liste
                lessonTypes.add(lessonType);
            }

        } catch (SQLException ex) {
            System.err.println("Erreur lors de la récupération des LessonTypes : " + ex.getMessage());
        }

        return lessonTypes;
    }*/


    // Méthodes utilisant 'connection' ici, sans la fermer
    
    public List<String> selectLessonType() {
        List<String> names = new ArrayList<>();
        String query = "SELECT Levels, Price, Sport, AgeCategory FROM LessonType";

        try (PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet res = pstmt.executeQuery()) {

            while (res.next()) {
                // Construire une description avec plusieurs colonnes
                String description = String.format("%s - %s - %s - %s",
                        res.getString("Levels"),
                        res.getString("Price"),
                        res.getString("Sport"),
                        res.getString("AgeCategory"));

                names.add(description);
            }
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la récupération des noms : " + ex.getMessage());
        }
        return names;
    }
    public List<LessonType> selectLessonTypes() {
        List<LessonType> lessonTypes = new ArrayList<>();
        Map<Integer, Accreditation> accreditationMap = new HashMap<>();

        // Requête SQL avec jointure pour obtenir les informations d'accréditation et de LessonType
        String query = """
            SELECT lt.LessonTypeId, lt.Levels, lt.Price,
                   a.AccreditationId, a.Names AS AccreditationName, a.SportType, a.AgeCategory
            FROM LessonType lt
            LEFT JOIN Accreditation a ON lt.AccreditationId = a.AccreditationId
        """;

        try (PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet res = pstmt.executeQuery()) {

            while (res.next()) {
                // Récupération des données de la table Accreditation
                int accreditationId = res.getInt("AccreditationId");
                String accreditationName = res.getString("AccreditationName");
                String sportType = res.getString("SportType");
                String ageCategory = res.getString("AgeCategory");

                // Recherche ou création d'un objet Accreditation dans le Map
                Accreditation accreditation = accreditationMap.get(accreditationId);
                if (accreditation == null) {
                    accreditation = new Accreditation(accreditationId, accreditationName, sportType, ageCategory);
                    accreditationMap.put(accreditationId, accreditation);
                }

                // Récupération des données de la table LessonType
                int lessonTypeId = res.getInt("LessonTypeId");
                String levels = res.getString("Levels");
                double price = res.getDouble("Price");

                // Création de l'objet LessonType
                LessonType lessonType = new LessonType(lessonTypeId, levels, price ,accreditation);

                // Ajout du LessonType à la liste des leçons et à l'accréditation correspondante
                lessonTypes.add(lessonType);
                accreditation.AddLessonType(lessonType); // Ajoute le LessonType à la liste interne de l'accréditation
            }

        } catch (SQLException ex) {
            System.err.println("Erreur lors de la récupération des types de leçon : " + ex.getMessage());
        }

        // Retourne la liste de LessonType
        return lessonTypes;
    }


    /*public List<String> selectLessonType() {
        List<String> lessonTypes = new ArrayList<>();
        String query = "SELECT Levels, Price, Sport, AgeCategory FROM LessonType";

        try (PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet res = pstmt.executeQuery()) {

            while (res.next()) {
                // Construire le texte formaté pour chaque élément du combo
                String title = res.getString("Sport") + " - " + res.getString("Levels");
                String description = "Catégorie d'âge : " + res.getString("AgeCategory") +
                                     ", Sport : " + res.getString("Sport") +
                                     ", Prix : " + res.getInt("Price") + "€";

                // Ajouter le format titre + description
                lessonTypes.add(title + ": " + description);
            }
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la récupération des noms : " + ex.getMessage());
        }
        return lessonTypes;
    }*/
    /*public List<Instructor> getInstructorsByLessonType(String lessonTypeName) {
        List<Instructor> instructors = new ArrayList<>();
        String sql = "SELECT i.Names, i.FirstName, i.DateOfBirth, i.Pseudo " +
                     "FROM Instructor i " +
                     "JOIN Acc_LessonType alt ON i.InstructorId = alt.InstructorId " +
                     "JOIN LessonType lt ON alt.LessonTypeId = lt.LessonTypeId " +
                     "WHERE lt.name = ?"; // Adaptez le nom du champ "name" selon votre colonne

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, lessonTypeName);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Instructor instructor = new Instructor(
                        resultSet.getString("Names"),
                        resultSet.getString("FirstName"),
                        resultSet.getDate("DateOfBirth").toLocalDate(),
                        resultSet.getString("Pseudo")
                    );
                    instructors.add(instructor);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des instructeurs : " + e.getMessage());
        }
        return instructors;
    }*/
    /*
    @Override
    public void create(LessonType lessonType) throws SQLException {
        String sql = "INSERT INTO LessonType (name, description, level, category, target_audience) VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, lessonType.getName());
            preparedStatement.setString(2, lessonType.getDescription());
            preparedStatement.setString(3, lessonType.getLevel());
            preparedStatement.setString(4, lessonType.getCategory());
            preparedStatement.setString(5, lessonType.getTargetAudience());
            
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout d'un type de leçon : " + e.getMessage());
            throw e; // Relancer l'exception après journalisation
        }
    }*/
    
   
    public int getLessonTypeIdByName(String level,double price,String sport,String age) {
        String sql = "SELECT LessonTypeid FROM LessonType WHERE Levels = ? and Price = ? and Sport = ? and AgeCategory = ?"; // Ajustez la requête selon votre schéma de table
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, level);
            preparedStatement.setDouble(2, price);
            preparedStatement.setString(3, sport);
            preparedStatement.setString(4, age);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("LessonTypeId"); // Récupère l'ID
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'ID du type de leçon : " + e.getMessage());
             // Relancer l'exception après journalisation
        }
        return -1; // Retourne -1 si aucun type de leçon n'est trouvé
    }







}
