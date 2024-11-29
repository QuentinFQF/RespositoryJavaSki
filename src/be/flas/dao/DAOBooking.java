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
	public boolean update(Booking obj){
	    return false;
	}
    @Override
	public Booking find(int id){
    	Booking s = new Booking();
		return s;
	}
    // pas utiliser
    @Override
    public boolean create(Booking b) {
	    
	    
	    String sqlInsertBooking = "INSERT INTO Booking (DateBooking, LessonId, SkierId, PeriodId,DateParticulier) VALUES (?, ?, ?, ?,?)";
	    
	    try (PreparedStatement pstmt = connection.prepareStatement(sqlInsertBooking)) {
	        pstmt.setDate(1, java.sql.Date.valueOf(b.getDateBooking())); 
	        pstmt.setInt(2, b.getLesson().getId());
	        pstmt.setInt(3, b.getSkier().getPersonId());
	        
	        pstmt.setInt(4, b.getPeriod().getId());
	        pstmt.setDate(5, java.sql.Date.valueOf(b.getLesson().getDate()));

	        int rowsInserted = pstmt.executeUpdate();
	        if (rowsInserted > 0) {
	            System.out.println("Réservation créée avec succès pour la leçon ID : " + b.getLesson().getLessonId());
	            return true; 
	        }
	    } catch (SQLException e) {
	        System.err.println("Erreur lors de la création de la réservation : " + e.getMessage());
	    }
	    return false; 
	}
	
	
    //pt supprimer
    public List<Booking> getBookingsBySkierOrInstructorId(String skierP,String insP) {
        List<Booking> bookings = new ArrayList<>();
        
        if (skierP == null) {
            skierP = "";  
        }

        if (insP == null) {
            insP = "";  
        }

        
        String sql = """
            SELECT 
                b.BookingId, b.DateBooking,b.Assurance
                s.SkierId, s.Names AS SkierName, s.FirstName AS SkierFirstName, s.Pseudo AS SkierPseudo,
                i.InstructorId, i.Names AS InstructorName, i.FirstName AS InstructorFirstName, i.Pseudo AS InstructorPseudo,
                l.LessonId, l.MinBookings, l.MaxBookings, l.DayPart, l.CourseType, l.TariffId,l.StartTime,l.EndTime
                lt.LessonTypeId, lt.Levels, lt.Sport, lt.Price, lt.AgeCategory,
                p.PeriodId, p.StartDate, p.EndDate, p.IsVacation
            FROM 
                Booking b
            JOIN Skier s ON b.SkierId = s.SkierId
            JOIN Instructor i ON b.InstructorId = i.InstructorId
            JOIN Lesson l ON b.LessonId = l.LessonId
            JOIN LessonType lt ON l.LessonTypeId = lt.LessonTypeId
            JOIN Period p ON b.PeriodId = p.PeriodId
            WHERE 
                (s.Pseudo = ? OR ? = '') AND
                (i.Pseudo = ? OR ? = '')
            """;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            
            pstmt.setObject(1, skierP);
            pstmt.setObject(2, skierP);
            pstmt.setObject(3, insP);
            pstmt.setObject(4, insP);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                
                while (rs.next()) {
                    
                    LocalDate dateBooking = rs.getDate("DateBooking").toLocalDate();
                    int id=rs.getInt("BookingId");
                    boolean a=rs.getBoolean("Assurance");
                    
                    
                    Skier skier = new Skier(
                        rs.getInt("SkierId"), 
                        rs.getString("SkierName"), 
                        rs.getString("SkierFirstName"),
                        rs.getString("SkierPseudo")
                    );
                    
                   
                    Instructor instructor = new Instructor(
                        rs.getString("InstructorName"), 
                        rs.getString("InstructorFirstName"), 
                        rs.getInt("InstructorId"),
                        rs.getString("InstructorPseudo")
                    );
                    
                    
                    LessonType lessonType = new LessonType(
                        rs.getInt("LessonTypeId"), 
                        rs.getString("Levels"), 
                        rs.getString("Sport"), 
                        rs.getDouble("Price"), 
                        rs.getString("AgeCategory")
                    );
                    
                  
                    Lesson lesson = new Lesson(
                        rs.getInt("MinBookings"), 
                        rs.getInt("MaxBookings"),
                        instructor,
                        lessonType,
                        rs.getString("DayPart"),
                        rs.getString("CourseType"),
                        rs.getInt("TariffId"),
                        rs.getInt("StartTime"),
                        rs.getInt("EndTime")
                    );

                    
                    Period period = new Period(
                        rs.getInt("PeriodId"),
                        rs.getDate("StartDate").toLocalDate(),
                        rs.getDate("EndDate").toLocalDate(),
                        rs.getBoolean("IsVacation")
                    );

                    
                    Booking booking = new Booking(id,dateBooking, skier, period, lesson, instructor,a);
                    bookings.add(booking);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des réservations : " + e.getMessage());
        }

        return bookings;
    }
    //peut etre supprimer
    @Override
    public boolean delete(Booking b) {
        
        String deleteBookingSql = "DELETE FROM Booking WHERE BookingId = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(deleteBookingSql)) {
           
            pstmt.setInt(1, b.getId());

          
            int affectedRows = pstmt.executeUpdate();

           
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du booking : " + e.getMessage());
            return false;
        }
    }




}
