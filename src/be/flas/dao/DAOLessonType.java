package be.flas.dao;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
    @Override
	public LessonType find(int id){
    	LessonType s = new LessonType();
		return s;
	}
    @Override
    public boolean create(LessonType obj){
    	return false;
    }

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
