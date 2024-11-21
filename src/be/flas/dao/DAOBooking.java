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
	public boolean createWithLesson(Booking booking) {
	    // Récupérer les informations nécessaires depuis l'objet Booking
	    Lesson lesson = booking.getLesson();

	    // Étape 1 : Créer la leçon et récupérer l'ID généré
	    int lessonId = createLesson(lesson);

	    // Vérifier si l'insertion de la leçon a échoué
	    if (lessonId == -1) {
	        System.err.println("Erreur lors de la création de la leçon.");
	        return false;
	    }

	    // Associer l'ID de la leçon créée à l'objet Lesson
	    lesson.setLessonId(lessonId);

	    // Étape 2 : Créer le booking avec la leçon nouvellement créée
	    return createBooking(booking);
	}

	// Méthode pour créer la leçon dans la base de données
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

	        // Si la leçon est de type "Collectif", on ne met pas de tarif
	        if (lesson.getCourseType().equalsIgnoreCase("Collectif")) {
	            pstmt.setNull(6, java.sql.Types.INTEGER);
	        } else {
	        	System.out.println("ici c'est âris");
	            pstmt.setInt(6, lesson.getTarifId());
	        }

	        // Initialiser NumberSkier à 1
	        pstmt.setInt(7, 1);

	        // Exécution de la requête
	        int rowsInserted = pstmt.executeUpdate();
	        if (rowsInserted > 0) {
	            // Récupérer l'ID généré de la leçon
	            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
	                if (generatedKeys.next()) {
	                    return generatedKeys.getInt(1); // Retourne l'ID de la leçon
	                }
	            }
	        }
	    } catch (SQLException e) {
	        System.err.println("Erreur lors de la création de la leçon : " + e.getMessage());
	    }
	    return -1; // Retourne -1 en cas d'échec
	}


	// Méthode pour créer la réservation dans la base de données
	private boolean createBooking(Booking booking) {
	    String sql = """
	        INSERT INTO Booking (DateBooking, LessonId, SkierId, PeriodId,InstructorId,DateParticulier) 
	        VALUES (?, ?, ?, ?,?,?)
	    """;

	    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
	        pstmt.setDate(1, java.sql.Date.valueOf(booking.getDateBooking()));
	        pstmt.setInt(2, booking.getLesson().getId());
	        pstmt.setInt(3, booking.getSkier().getPersonId());
	        pstmt.setInt(4, booking.getPeriod().getId());
	        pstmt.setInt(5, booking.getInstructor().getPersonId());
	        pstmt.setDate(6, java.sql.Date.valueOf(booking.getLesson().getDate()));

	        int rowsInserted = pstmt.executeUpdate();
	        if (rowsInserted > 0) {
	            System.out.println("Réservation créée avec succès pour la leçon ID : " + booking.getLesson().getLessonId());
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
    @Override
    public boolean create(Booking b) {
	    // Obtenir la date du jour
	    //LocalDate dateBooking = LocalDate.now();
	    
	    String sqlInsertBooking = "INSERT INTO Booking (DateBooking, LessonId, SkierId, PeriodId,DateParticulier) VALUES (?, ?, ?, ?,?)";
	    
	    try (PreparedStatement pstmt = connection.prepareStatement(sqlInsertBooking)) {
	        pstmt.setDate(1, java.sql.Date.valueOf(b.getDateBooking())); // Convertit LocalDate en java.sql.Date
	        pstmt.setInt(2, b.getLesson().getId());
	        pstmt.setInt(3, b.getSkier().getPersonId());
	        //pstmt.setInt(4, b.getInstructor().getPersonId());
	        pstmt.setInt(4, b.getPeriod().getId());
	        pstmt.setDate(5, java.sql.Date.valueOf(b.getLesson().getDate()));

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
