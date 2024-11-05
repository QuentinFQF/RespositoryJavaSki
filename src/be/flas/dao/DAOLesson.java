package be.flas.dao;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import be.flas.interfaces.DaoGeneric;
import be.flas.interfaces.IDaoLesson;
import be.flas.model.Accreditation;
import be.flas.model.Lesson;
import be.flas.model.Skier;

public class DAOLesson extends DaoGeneric<Lesson>{

	public DAOLesson(Connection conn){
    	super(conn);
    }
    @Override
	public boolean delete(Lesson obj){
	    return false;
	}
    
    @Override
	public Lesson find(int id){
    	Lesson s = new Lesson();
		return s;
	}
    @Override
    public boolean create(Lesson l) {
	    
	    
	   
	    String sqlInsertLesson = "INSERT INTO Lesson (InstructorId, LessonTypeId, MinBookings, MaxBookings, DayPart,CourseType,TariffId) VALUES (?, ?, ?, ?, ?,?,?)";
	    String sqlUpdateNumberSkier = "UPDATE Lesson SET NumberSkier = NumberSkier + 1 WHERE LessonId = ?";

	    try (PreparedStatement pstmt = connection.prepareStatement(sqlInsertLesson, Statement.RETURN_GENERATED_KEYS)) {
	        pstmt.setInt(1, l.getInstructor().getPersonId());
	        pstmt.setInt(2, l.getLessonType().getId());
	        pstmt.setInt(3, l.getMinBookings());
	        pstmt.setInt(4, l.getMaxBookings());
	        pstmt.setString(5, l.getDayPart()); // Ajoute le créneau horaire
	        pstmt.setString(6,l.getCourseType());
	        if (l.getCourseType().equals("Collectif")) {
                pstmt.setNull(7, java.sql.Types.INTEGER);  // Mettre NULL pour tarif_id si leçon collective
            }else {
                // Récupérez l'ID de tarif particulier ici si nécessaire pour les leçons particulières.
                 // Cette méthode doit être définie pour retourner l'ID correct.
                pstmt.setInt(7, l.getTarifId());
            }

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

	                    return true; // Retourne l'ID de la leçon créée
	                }
	            }
	        }
	    } catch (SQLException e) {
	        System.err.println("Erreur lors de la création de la leçon : " + e.getMessage());
	    }
	    return false; // Retourne -1 en cas d'échec
    	
	}
    
    

	
	//public int createLesson(int instructorId, int lessonTypeId/*, String lessonType*/, String timeSlot,String courseType,int minBooking,int maxBooking) {
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
	    
	    
/*
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
	}*/

	
	/*public int getLessonId(int instructorId, int lessonTypeId, LocalDate startDate, LocalDate endDate) {
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
	}*/
    public int getLessonId(int instructorId, int lessonTypeId, String dayPart, String courseType, int minBookings, int maxBookings) {
        String sql = "SELECT LessonId FROM Lesson WHERE InstructorId = ? AND LessonTypeId = ? AND DayPart = ? AND CourseType = ? AND MinBookings = ? AND MaxBookings = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, instructorId);
            pstmt.setInt(2, lessonTypeId);
            pstmt.setString(3, dayPart);
            pstmt.setString(4, courseType);
            pstmt.setInt(5, minBookings);
            pstmt.setInt(6, maxBookings);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("LessonId");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'ID de la leçon : " + e.getMessage());
        }
        return -1; // Retourne -1 si aucun résultat n'est trouvé ou en cas d'erreur
    }
    
    @Override
    public boolean update(Lesson l) {
        String sql = "UPDATE Lesson SET NumberSkier = NumberSkier + 1 WHERE LessonId = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, l.getId());
            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0; // Retourne true si la mise à jour a réussi
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du champ NumberSkier : " + e.getMessage());
        }
        return false; // Retourne false en cas d'échec
    }
    
    public boolean isLessonComplete(int lessonId) {
        String sql = "SELECT NumberSkier, MaxBookings FROM Lesson WHERE LessonId = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, lessonId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int numberSkier = rs.getInt("NumberSkier");
                    int maxBookings = rs.getInt("MaxBookings");

                    return numberSkier >= maxBookings; // Retourne true si la leçon est complète, sinon false
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification de l'état de la leçon : " + e.getMessage());
        }
        return false; // Retourne false en cas d'échec ou si la leçon n'existe pas
    }








}
