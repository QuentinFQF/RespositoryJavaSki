package be.flas.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

import be.flas.interfaces.IDaoBooking;

public class DAOBooking implements IDaoBooking{

	private Connection connection;

	// Constructeur qui prend une connexion
	public DAOBooking(Connection connection) {
	    this.connection = connection;
	}
	@Override
	public boolean createBooking(int lessonId, int skierId, int instructorId, int periodId) {
	    // Obtenir la date du jour
	    LocalDate dateBooking = LocalDate.now();
	    
	    String sqlInsertBooking = "INSERT INTO Booking (DateBooking, LessonId, SkierId, InstructorId, PeriodId) VALUES (?, ?, ?, ?, ?)";
	    
	    try (PreparedStatement pstmt = connection.prepareStatement(sqlInsertBooking)) {
	        pstmt.setDate(1, java.sql.Date.valueOf(dateBooking)); // Convertit LocalDate en java.sql.Date
	        pstmt.setInt(2, lessonId);
	        pstmt.setInt(3, skierId);
	        pstmt.setInt(4, instructorId);
	        pstmt.setInt(5, periodId);

	        int rowsInserted = pstmt.executeUpdate();
	        if (rowsInserted > 0) {
	            System.out.println("Réservation créée avec succès pour la leçon ID : " + lessonId);
	            return true; // Réservation créée avec succès
	        }
	    } catch (SQLException e) {
	        System.err.println("Erreur lors de la création de la réservation : " + e.getMessage());
	    }
	    return false; // Échec de la création de la réservation
	}

}
