package be.flas.dao;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import be.flas.interfaces.DaoGeneric;

import be.flas.model.Booking;
import be.flas.model.Instructor;
import be.flas.model.Lesson;
import be.flas.model.LessonType;
import be.flas.model.Period;
import be.flas.model.Skier;

public class DAOBooking extends DaoGeneric<Booking>{

	public DAOBooking(Connection conn){
    	super(conn);
    }
	public boolean createBooking(Booking booking, int lessonId) {
	    LocalDate currentDate = LocalDate.now();

	    
	    boolean includeDateParticulier = !booking.getLesson().getDate().equals(currentDate);

	   
	    String sqlBase = "INSERT INTO Booking (DateBooking, LessonId, SkierId, PeriodId, InstructorId, Assurance";
	    String sqlValues = " VALUES (?, ?, ?, ?, ?, ?";

	   
	    if (includeDateParticulier) {
	        sqlBase += ", DateParticulier";
	        sqlValues += ", ?";
	    }

	    
	    sqlBase += ")";
	    sqlValues += ")";

	  
	    String sql = sqlBase + sqlValues;

	    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
	        
	        pstmt.setDate(1, java.sql.Date.valueOf(booking.getDateBooking()));
	        pstmt.setInt(2, lessonId);                                     
	        pstmt.setInt(3, booking.getSkier().getPersonId());                
	        pstmt.setInt(4, booking.getPeriod().getId());                      
	        pstmt.setInt(5, booking.getInstructor().getPersonId());            
	        pstmt.setBoolean(6, booking.isAssurance());                        

	      
	        if (includeDateParticulier) {
	            pstmt.setDate(7, java.sql.Date.valueOf(booking.getLesson().getDate())); 
	        }

	        
	        int rowsInserted = pstmt.executeUpdate();
	        if (rowsInserted > 0) {
	            System.out.println("Réservation créée avec succès pour la leçon ID : " + lessonId);
	            return true; 
	        }
	    } catch (SQLException e) {
	        System.err.println("Erreur lors de la création de la réservation : " + e.getMessage());
	    }

	    return false; 
	}


	public boolean createWithLesson(Booking booking) {
	    
	    Lesson lesson = booking.getLesson();

	    
	    int lessonId = createLesson(lesson);

	  
	    if (lessonId == -1) {
	        System.err.println("Erreur lors de la création de la leçon.");
	        return false;
	    }

	   
	    lesson.setLessonId(lessonId);

	    
	    return createBooking(booking);
	}

	
	private int createLesson(Lesson lesson) {
	    String sql = """
	        INSERT INTO Lesson (LessonTypeId, MinBookings, MaxBookings, DayPart, CourseType, TariffId, NumberSkier,StartTime,EndTime) 
	        VALUES (?, ?, ?, ?, ?, ?, ?,?,?)
	    """;

	    try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
	        pstmt.setInt(1, lesson.getLessonType().getId());
	        pstmt.setInt(2, lesson.getMinBookings());
	        pstmt.setInt(3, lesson.getMaxBookings());
	        pstmt.setString(4, lesson.getDayPart());
	        pstmt.setString(5, lesson.getCourseType());
	        pstmt.setInt(8, lesson.getStart());
	        pstmt.setInt(9, lesson.getEnd());

	        
	        if (lesson.getCourseType().equalsIgnoreCase("Collectif")) {
	            pstmt.setNull(6, java.sql.Types.INTEGER);
	        } else {
	        	System.out.println("ici c'est âris");
	            pstmt.setInt(6, lesson.getTarifId());
	        }

	    
	        pstmt.setInt(7, 1);

	        
	        int rowsInserted = pstmt.executeUpdate();
	        if (rowsInserted > 0) {
	            
	            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
	                if (generatedKeys.next()) {
	                    return generatedKeys.getInt(1); 
	                }
	            }
	        }
	    } catch (SQLException e) {
	        System.err.println("Erreur lors de la création de la leçon : " + e.getMessage());
	    }
	    return -1; 
	}


	
	private boolean createBooking(Booking booking) {
	    
	    LocalDate currentDate = LocalDate.now();

	    
	    boolean includeDateParticulier = !booking.getLesson().getDate().equals(currentDate);

	    
	    String sqlBase = "INSERT INTO Booking (DateBooking, LessonId, SkierId, PeriodId, InstructorId, Assurance";
	    String sqlValues = " VALUES (?, ?, ?, ?, ?, ?";

	    if (includeDateParticulier) {
	        sqlBase += ", DateParticulier"; 
	        sqlValues += ", ?";             
	    }

	    sqlBase += ")";
	    sqlValues += ")";

	    String sql = sqlBase + sqlValues;

	    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
	        
	        pstmt.setDate(1, java.sql.Date.valueOf(booking.getDateBooking()));
	        pstmt.setInt(2, booking.getLesson().getId());
	        pstmt.setInt(3, booking.getSkier().getPersonId());
	        pstmt.setInt(4, booking.getPeriod().getId());
	        pstmt.setInt(5, booking.getInstructor().getPersonId());
	        pstmt.setBoolean(6, booking.isAssurance());

	        
	        if (includeDateParticulier) {
	            pstmt.setDate(7, java.sql.Date.valueOf(booking.getLesson().getDate()));
	        }

	        
	        int rowsInserted = pstmt.executeUpdate();
	        if (rowsInserted > 0) {
	            System.out.println("Réservation créée avec succès pour la leçon ID : " + booking.getLesson().getId());
	            return true;
	        }
	    } catch (SQLException e) {
	        System.err.println("Erreur lors de la création de la réservation : " + e.getMessage());
	    }
	    return false;
	}


	@Override
	public boolean create(Booking obj){
	    return false;
	}

	@Override
	public boolean delete(Booking obj){
	    return false;
	}

   
    @Override
	public boolean update(Booking obj){
	    return false;
	}
    @Override
	public Booking find(int id){
    	Booking s = new Booking();
		return s;
	}
    




}
