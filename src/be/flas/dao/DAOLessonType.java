package be.flas.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import be.flas.interfaces.IDaoAccreditation;
import be.flas.interfaces.IDaoLessonType;
import be.flas.model.Instructor;

public class DAOLessonType implements IDaoLessonType{

	private Connection connection;

    public DAOLessonType(Connection connection) {
        this.connection = connection;
    }

    // Méthodes utilisant 'connection' ici, sans la fermer
    @Override
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
    







}
