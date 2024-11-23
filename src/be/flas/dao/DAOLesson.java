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
import be.flas.model.Booking;
import be.flas.model.Instructor;
import be.flas.model.Lesson;
import be.flas.model.LessonType;
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
    public Lesson find(int id) {
        Lesson lesson = null;

       
        String sql = "SELECT l.LessonId, l.InstructorId, l.LessonTypeId, l.MinBookings, l.MaxBookings, " +
                     "l.NumberSkier, l.DayPart, l.CourseType, l.TariffId, l.StartTime, l.EndTime, " +
                     "i.Names AS InstructorNames, i.FirstName AS InstructorFirstName, i.DateOfBirth AS InstructorDateOfBirth, i.Pseudo AS InstructorPseudo, " +
                     "lt.Levels AS LessonTypeLevels, lt.Price AS LessonTypePrice, lt.AgeCategory, lt.Sport " +
                     "FROM Lesson l " +
                     "JOIN Instructor i ON l.InstructorId = i.InstructorId " +
                     "JOIN LessonType lt ON l.LessonTypeId = lt.LessonTypeId " +
                     "WHERE l.LessonId = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id); 

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    
                    Instructor instructor = new Instructor(
                        rs.getString("InstructorNames"),
                        rs.getString("InstructorFirstName"),
                        rs.getInt("InstructorId"),
                        rs.getDate("InstructorDateOfBirth").toLocalDate(),
                        rs.getString("InstructorPseudo"),
                        null
                    );

                    
                    LessonType lessonType = new LessonType(
                        rs.getInt("LessonTypeId"),
                        rs.getString("LessonTypeLevels"),
                        rs.getString("Sport"),
                        rs.getDouble("LessonTypePrice"),
                        rs.getString("AgeCategory")
                    );

                    
                    lesson = new Lesson(
                        rs.getInt("MinBookings"),
                        rs.getInt("MaxBookings"),
                        instructor,
                        lessonType,
                        rs.getString("DayPart"),
                        rs.getString("CourseType"),
                        rs.getInt("TariffId")
                    );

                    
                    lesson.setLessonId(rs.getInt("LessonId"));
                    lesson.setNumberSkier(rs.getInt("NumberSkier"));
                    lesson.setStart(rs.getInt("StartTime"));
                    lesson.setEnd(rs.getInt("EndTime"));
                }
            }
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la recherche de la leçon avec ID " + id + " : " + ex.getMessage());
        }

        return lesson;
    }



    @Override
    public boolean create(Lesson l) {
	    
	    
	   
	    String sqlInsertLesson = "INSERT INTO Lesson (LessonTypeId, MinBookings, MaxBookings, DayPart,CourseType,TariffId) VALUES (?, ?, ?, ?,?,?)";
	    String sqlUpdateNumberSkier = "UPDATE Lesson SET NumberSkier = NumberSkier + 1 WHERE LessonId = ?";

	    try (PreparedStatement pstmt = connection.prepareStatement(sqlInsertLesson, Statement.RETURN_GENERATED_KEYS)) {
	      
	        pstmt.setInt(1, l.getLessonType().getId());
	        pstmt.setInt(2, l.getMinBookings());
	        pstmt.setInt(3, l.getMaxBookings());
	        pstmt.setString(4, l.getDayPart()); 
	        pstmt.setString(5,l.getCourseType());
	        if (l.getCourseType().equals("Collectif")) {
                pstmt.setNull(6, java.sql.Types.INTEGER);  
            }else {
               
                 
            	System.out.println("ici c'est pâris ");
                pstmt.setInt(6, l.getTarifId());
            }

	        int rowsInserted = pstmt.executeUpdate();
	        if (rowsInserted > 0) {
	            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
	                if (generatedKeys.next()) {
	                    int lessonId = generatedKeys.getInt(1);
	                    System.out.println("Leçon créée avec succès avec l'ID : " + lessonId);

	                    
	                    try (PreparedStatement updatePstmt = connection.prepareStatement(sqlUpdateNumberSkier)) {
	                        updatePstmt.setInt(1, lessonId);
	                        updatePstmt.executeUpdate();
	                    }

	                    return true; 
	                }
	            }
	        }
	    } catch (SQLException e) {
	        System.err.println("Erreur lors de la création de la leçon : " + e.getMessage());
	    }
	    return false; 
    	
	}

    
    

	
	
    public int getLessonId(int instructorId, int lessonTypeId, String dayPart, String courseType, int minBookings, int maxBookings) {
        String sql = """
            SELECT l.LessonId 
            FROM Lesson l
            JOIN Booking b ON l.LessonId = b.LessonId
            WHERE b.InstructorId = ? 
            AND l.LessonTypeId = ? 
            AND l.DayPart = ? 
            AND l.CourseType = ? 
            AND l.MinBookings = ? 
            AND l.MaxBookings = ?
        """;

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
        return -1; 
    }


    
    @Override
    public boolean update(Lesson l) {
        String sql = "UPDATE Lesson SET NumberSkier = NumberSkier + 1 WHERE LessonId = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, l.getId());
            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0; 
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du champ NumberSkier : " + e.getMessage());
        }
        return false;
    }
    
    public boolean isLessonComplete(int id) {
        String sql = "SELECT NumberSkier, MaxBookings FROM Lesson WHERE LessonId = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1,id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int numberSkier = rs.getInt("NumberSkier");
                    int maxBookings = rs.getInt("MaxBookings");

                    return numberSkier >= maxBookings; 
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification de l'état de la leçon : " + e.getMessage());
        }
        return false; 
    }
    
    public boolean isLessonComplete2(int id) {
        String sql = "SELECT NumberSkier, MaxBookings FROM Lesson WHERE LessonId = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int numberSkier = rs.getInt("NumberSkier");
                    int maxBookings = rs.getInt("MaxBookings");

                    return numberSkier >= maxBookings; 
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification de l'état de la leçon : " + e.getMessage());
        }
        return false; 
    }








}
