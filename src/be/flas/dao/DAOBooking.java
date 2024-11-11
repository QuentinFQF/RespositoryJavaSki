package be.flas.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import be.flas.interfaces.DaoGeneric;
import be.flas.interfaces.IDaoBooking;
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
   
    @Override
	public boolean update(Booking obj){
	    return false;
	}
    @Override
	public Booking find(int id){
    	Booking s = new Booking();
		return s;
	}
    @Override
    public boolean create(Booking b) {
	    // Obtenir la date du jour
	    //LocalDate dateBooking = LocalDate.now();
	    
	    String sqlInsertBooking = "INSERT INTO Booking (DateBooking, LessonId, SkierId, InstructorId, PeriodId) VALUES (?, ?, ?, ?, ?)";
	    
	    try (PreparedStatement pstmt = connection.prepareStatement(sqlInsertBooking)) {
	        pstmt.setDate(1, java.sql.Date.valueOf(b.getDateBooking())); // Convertit LocalDate en java.sql.Date
	        pstmt.setInt(2, b.getLesson().getId());
	        pstmt.setInt(3, b.getSkier().getPersonId());
	        pstmt.setInt(4, b.getInstructor().getPersonId());
	        pstmt.setInt(5, b.getPeriod().getId());

	        int rowsInserted = pstmt.executeUpdate();
	        if (rowsInserted > 0) {
	            System.out.println("Réservation créée avec succès pour la leçon ID : " + b.getLesson().getLessonId());
	            return true; // Réservation créée avec succès
	        }
	    } catch (SQLException e) {
	        System.err.println("Erreur lors de la création de la réservation : " + e.getMessage());
	    }
	    return false; // Échec de la création de la réservation
	}
	
	/*public boolean createBooking(int lessonId, int skierId, int instructorId, int periodId) {
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
	}*/
    
    /*public List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        String sql = """
            SELECT 
                b.BookingId, b.DateBooking,
                s.SkierId, s.Names AS SkierName, s.FirstNames AS SkierFirstName,
                i.InstructorId, i.Names AS InstructorName, i.FirstNames AS InstructorFirstName,
                l.LessonId, l.LessonType,
                p.DayPart
            FROM 
                Booking b
            JOIN Skier s ON b.SkierId = s.SkierId
            JOIN Instructor i ON b.InstructorId = i.InstructorId
            JOIN Lesson l ON b.LessonId = l.LessonId
            JOIN Period p ON b.PeriodId = p.PeriodId
            """;

        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                LocalDate dateBooking = rs.getDate("DateBooking").toLocalDate();
                Skier skier = new Skier(rs.getInt("SkierId"), rs.getString("Names"), rs.getString("FirstName"),rs.getString("Pseudo"));
                Instructor instructor = new Instructor(rs.getString("Names"), rs.getString("FirstName"),rs.getInt("InstructorId"),rs.getString("Pseudo"));
                Lesson lesson = new Lesson(rs.getInt("LessonId"), rs.getString("LessonType"));
                Period period = new Period(rs.getString("DayPart"));

                Booking booking = new Booking(dateBooking, skier, period, lesson, instructor);
                bookings.add(booking);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des réservations : " + e.getMessage());
        }

        return bookings;
    }*/
    
    public List<Booking> getBookingsBySkierOrInstructorId(String skierP,String insP) {
        List<Booking> bookings = new ArrayList<>();
        
        if (skierP == null) {
            skierP = "";  // Remplacez par une chaîne vide si skierP est null
        }

        if (insP == null) {
            insP = "";  // Remplacez par une chaîne vide si insP est null
        }

        // Requête SQL avec filtres pour rechercher par ID du skieur ou de l'instructeur
        String sql = """
            SELECT 
                b.BookingId, b.DateBooking,
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
            // Définir les paramètres pour skierId et instructorId
            pstmt.setObject(1, skierP);
            pstmt.setObject(2, skierP);
            pstmt.setObject(3, insP);
            pstmt.setObject(4, insP);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                // Parcourir les résultats et construire les objets Booking
                while (rs.next()) {
                    // Récupération de la date de réservation
                    LocalDate dateBooking = rs.getDate("DateBooking").toLocalDate();
                    int id=rs.getInt("BookingId");
                    
                    // Création de l'objet Skier
                    Skier skier = new Skier(
                        rs.getInt("SkierId"), 
                        rs.getString("SkierName"), 
                        rs.getString("SkierFirstName"),
                        rs.getString("SkierPseudo")
                    );
                    
                    // Création de l'objet Instructor
                    Instructor instructor = new Instructor(
                        rs.getString("InstructorName"), 
                        rs.getString("InstructorFirstName"), 
                        rs.getInt("InstructorId"),
                        rs.getString("InstructorPseudo")
                    );
                    
                    // Création de l'objet LessonType
                    LessonType lessonType = new LessonType(
                        rs.getInt("LessonTypeId"), 
                        rs.getString("Levels"), 
                        rs.getString("Sport"), 
                        rs.getDouble("Price"), 
                        rs.getString("AgeCategory")
                    );
                    
                    // Création de l'objet Lesson
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

                    // Création de l'objet Period
                    Period period = new Period(
                        rs.getInt("PeriodId"),
                        rs.getDate("StartDate").toLocalDate(),
                        rs.getDate("EndDate").toLocalDate(),
                        rs.getBoolean("IsVacation")
                    );

                    // Création et ajout de l'objet Booking à la liste
                    Booking booking = new Booking(id,dateBooking, skier, period, lesson, instructor);
                    bookings.add(booking);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des réservations : " + e.getMessage());
        }

        return bookings;
    }
    
    @Override
    public boolean delete(Booking b) {
        // Définir la requête SQL pour supprimer un booking
        String deleteBookingSql = "DELETE FROM Booking WHERE BookingId = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(deleteBookingSql)) {
            // Paramétrer la requête avec l'ID du booking
            pstmt.setInt(1, b.getId());

            // Exécuter la requête de suppression
            int affectedRows = pstmt.executeUpdate();

            // Si l'ID du booking a été trouvé et supprimé, affectedRows sera > 0
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du booking : " + e.getMessage());
            return false;
        }
    }




}
