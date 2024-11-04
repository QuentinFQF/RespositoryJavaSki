package be.flas.dao;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import be.flas.interfaces.IDaoLesson;
import be.flas.model.Lesson;

public class DAOLesson implements IDaoLesson{

	private Connection connection;

	// Constructeur qui prend une connexion
	public DAOLesson(Connection connection) {
	    this.connection = connection;
	}

	@Override
	public int createLesson(int instructorId, int lessonTypeId, String lessonType, String timeSlot,String courseType,int minBooking,int maxBooking) {
	    /*int minBooking;
	    int maxBooking;

	    if ("1 heure".equalsIgnoreCase(timeSlot) || "2 heures".equalsIgnoreCase(timeSlot)) {
	        minBooking = 1;
	        maxBooking = 4;
	    }else if ("Enfant".equalsIgnoreCase(lessonType)) {
	        minBooking = 5;
	        maxBooking = 8;
	    } else if ("Adulte".equalsIgnoreCase(lessonType)) {
	        minBooking = 6;
	        maxBooking = 10;
	    } else {
	        System.err.println("Type de leçon non reconnu : " + lessonType);
	        return -1; // Retourne -1 si le type de leçon est invalide
	    }*/
		/*int[] minMax;
		
	    try {
	        minMax = getMinAndMaxBooking(lessonType, timeSlot); // Appel à la méthode de vérification
	    } catch (IllegalArgumentException e) {
	        System.err.println(e.getMessage());
	        return -1; // Retourne -1 en cas d'erreur de validation
	    }

	    int minBooking = minMax[0];
	    int maxBooking = minMax[1];*/
	    
	    

	    String sqlInsertLesson = "INSERT INTO Lesson (InstructorId, LessonTypeId, MinBookings, MaxBookings, DayPart,CourseType) VALUES (?, ?, ?, ?, ?,?)";
	    String sqlUpdateNumberSkier = "UPDATE Lesson SET NumberSkier = NumberSkier + 1 WHERE LessonId = ?";

	    try (PreparedStatement pstmt = connection.prepareStatement(sqlInsertLesson, Statement.RETURN_GENERATED_KEYS)) {
	        pstmt.setInt(1, instructorId);
	        pstmt.setInt(2, lessonTypeId);
	        pstmt.setInt(3, minBooking);
	        pstmt.setInt(4, maxBooking);
	        pstmt.setString(5, timeSlot); // Ajoute le créneau horaire
	        pstmt.setString(6,courseType );

	        int rowsInserted = pstmt.executeUpdate();
	        if (rowsInserted > 0) {
	            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
	                if (generatedKeys.next()) {
	                    int lessonId = generatedKeys.getInt(1);
	                    System.out.println("Leçon créée avec succès avec l'ID : " + lessonId);

	                    // Incrémenter NumberSkier pour la leçon créée
	                    try (PreparedStatement updatePstmt = connection.prepareStatement(sqlUpdateNumberSkier)) {
	                        updatePstmt.setInt(1, lessonId);
	                        updatePstmt.executeUpdate();
	                    }

	                    return lessonId; // Retourne l'ID de la leçon créée
	                }
	            }
	        }
	    } catch (SQLException e) {
	        System.err.println("Erreur lors de la création de la leçon : " + e.getMessage());
	    }
	    return -1; // Retourne -1 en cas d'échec
	}

	@Override
	public int getLessonId(int instructorId, int lessonTypeId, LocalDate startDate, LocalDate endDate) {
	    String sql = "SELECT l.LessonId " +
	                 "FROM Booking b " +
	                 "JOIN Lesson l ON b.LessonId = l.LessonId " +
	                 "JOIN Period p ON b.PeriodId = p.PeriodId " +
	                 "WHERE l.InstructorId = ? AND l.LessonTypeId = ? " +
	                 "AND p.StartDate = ? AND p.EndDate = ?";

	    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
	        preparedStatement.setInt(1, instructorId);
	        preparedStatement.setInt(2, lessonTypeId);
	        preparedStatement.setDate(3, java.sql.Date.valueOf(startDate)); // Conversion de LocalDate à java.sql.Date
	        preparedStatement.setDate(4, java.sql.Date.valueOf(endDate)); // Conversion de LocalDate à java.sql.Date

	        ResultSet resultSet = preparedStatement.executeQuery();
	        
	        if (resultSet.next()) {
	            return resultSet.getInt("LessonId"); // Retourne l'ID de la leçon si trouvé
	        }
	    } catch (SQLException e) {
	        System.err.println("Erreur lors de la récupération de l'ID de la leçon : " + e.getMessage());
	    }
	    return -1; // Retourne -1 si aucune leçon n'est trouvée
	}





}
