package be.flas.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

import be.flas.interfaces.IDaoLesson;

public class DAOLesson implements IDaoLesson{

	private Connection connection;

	// Constructeur qui prend une connexion
	public DAOLesson(Connection connection) {
	    this.connection = connection;
	}

	// Méthode pour insérer une leçon
	@Override
	public boolean createLesson(int instructorId, int skierId, LocalDate startDate, LocalDate endDate, int lessonTypeId) {
	    String sql = "INSERT INTO Lesson (InstructorId, SkierId, StartDate, EndDate, LessonTypeId) VALUES (?, ?, ?, ?, ?)";

	    // Utilisation de try-with-resources pour garantir la fermeture des ressources
	    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
	        
	        // Remplissage des paramètres de la requête
	        pstmt.setInt(1, instructorId); // ID de l'instructeur
	        pstmt.setInt(2, skierId); // ID du skieur
	        pstmt.setDate(3, java.sql.Date.valueOf(startDate)); // Date de début (LocalDate)
	        pstmt.setDate(4, java.sql.Date.valueOf(endDate)); // Date de fin (LocalDate)
	        pstmt.setInt(5, lessonTypeId); // ID du type de leçon

	        // Exécution de la requête
	        int rowsInserted = pstmt.executeUpdate();
	        return rowsInserted > 0; // Retourne true si une ligne a été insérée

	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false; // Retourne false en cas d'erreur
	    }
	}

}
