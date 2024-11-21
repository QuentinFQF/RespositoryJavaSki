package be.flas.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import be.flas.interfaces.DaoGeneric;
import be.flas.interfaces.IDaoClasse;
import be.flas.model.Accreditation;
import be.flas.model.Booking;
import be.flas.model.Instructor;
import be.flas.model.Lesson;
import be.flas.model.LessonType;
import be.flas.model.Period;
import be.flas.model.Skier;

public class DAOInstructor extends DaoGeneric<Instructor> {

    

	public DAOInstructor(Connection conn){
    	super(conn);
    }
	@Override
	public boolean delete(Instructor obj) {
	    // Requêtes pour supprimer les enregistrements associés
	    String deleteBookingsSql = "DELETE FROM Booking WHERE InstructorId = ?";
	    //String deleteLessonsSql = "DELETE FROM Lesson WHERE InstructorId = ?";
	    String deleteInsAccSql = "DELETE FROM Ins_Accreditation WHERE InstructorId = ?";
	    String deleteInstructorSql = "DELETE FROM Instructor WHERE InstructorId = ?";

	    try {
	        // Commencez une transaction
	        connection.setAutoCommit(false);

	        // Supprimer les réservations associées
	        try (PreparedStatement pstmtBookings = connection.prepareStatement(deleteBookingsSql)) {
	            pstmtBookings.setInt(1, obj.getPersonId());
	            pstmtBookings.executeUpdate();
	        }

	        

	        // Supprimer les enregistrements dans Ins_Acc
	        try (PreparedStatement pstmtInsAcc = connection.prepareStatement(deleteInsAccSql)) {
	            pstmtInsAcc.setInt(1, obj.getPersonId());
	            pstmtInsAcc.executeUpdate();
	        }

	        // Supprimer l'instructeur lui-même
	        try (PreparedStatement pstmtInstructor = connection.prepareStatement(deleteInstructorSql)) {
	            pstmtInstructor.setInt(1, obj.getPersonId());
	            int affectedRows = pstmtInstructor.executeUpdate();

	            // Valider la transaction si tout s'est bien passé
	            connection.commit();
	            
	            return affectedRows > 0;  // Retourner true si l'instructeur a été supprimé avec succès
	        }
	        
	    } catch (SQLException e) {
	        System.err.println("Erreur lors de la suppression de l'instructeur et de ses données associées : " + e.getMessage());
	        try {
	            connection.rollback(); // Annule la transaction en cas d'erreur
	        } catch (SQLException rollbackEx) {
	            System.err.println("Erreur lors du rollback : " + rollbackEx.getMessage());
	        }
	    } finally {
	        try {
	            connection.setAutoCommit(true); // Réactiver l'auto-commit
	        } catch (SQLException ex) {
	            System.err.println("Erreur lors de la réactivation de l'auto-commit : " + ex.getMessage());
	        }
	    }

	    return false;  // Retourner false en cas d'échec
	}

    @Override
    public boolean update(Instructor i) {
        String sql = "UPDATE Instructor SET Pseudo=?, FirstName=?, Names=?, DateOfBirth=? WHERE InstructorId=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, i.getPseudo());
            stmt.setString(2, i.getFirstName());
            stmt.setString(3, i.getName());
            stmt.setInt(5, i.getPersonId());
            stmt.setDate(4, java.sql.Date.valueOf(i.getDateOfBirth()));
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
    /*@Override
    public Instructor find(int id) {
        Instructor instructor = null;

        Map<Integer, Accreditation> accreditationMap = new HashMap<>();
        Map<Integer, Lesson> lessonMap = new HashMap<>();
        Map<Integer, Booking> bookingMap = new HashMap<>();

        try {
            // Requête SQL mise à jour
            String query = "SELECT " +
                    "i.InstructorId AS InstructorId, " +
                    "i.Names AS InstructorName, " +
                    "i.FirstName AS InstructorFirstName, " +
                    "i.Pseudo AS InstructorPseudo, " +
                    "i.DateOfBirth AS InstructorDateOfBirth, " +
                    "a.AccreditationId AS AccreditationId, " +
                    "a.Names AS AccreditationName, " +
                    "a.SportType AS AccreditationSportType, " +
                    "a.AgeCategory AS AccreditationAgeCategory, " +
                    "l.LessonId AS LessonId, " +
                    "l.NumberSkier AS NumberSkier, " +
                    "l.MinBookings AS LessonMinBookings, " +
                    "l.MaxBookings AS LessonMaxBookings, " +
                    "l.DayPart AS LessonDayPart, " +
                    
                    "l.CourseType AS LessonCourseType, " +
                    "lt.LessonTypeId AS LessonTypeId, " +
                    "lt.Levels AS LessonTypeLevel, " +
                    "lt.Price AS LessonTypePrice, " +
                    "lt.Sport AS LessonTypeSport, " +
                    "lt.AgeCategory AS LessonTypeAgeCategory, " +
                    "b.BookingId AS BookingId, " +
                    "b.DateBooking AS BookingDateBooking, " +
                    "b.DateParticulier AS LessonDateParticulier, " +
                    "p.PeriodId AS PeriodId, " +
                    "p.StartDate AS PeriodStartDate, " +
                    "p.EndDate AS PeriodEndDate, " +
                    "p.IsVacation AS PeriodIsVacation, " +
                    "sk.SkierId AS SkierId, " +
                    "sk.Names AS SkierName, " +
                    "sk.FirstName AS SkierFirstName " +
                    "FROM " +
                    "Instructor i " +
                    "LEFT JOIN Ins_Accreditation ia ON i.InstructorId = ia.InstructorId " +
                    "LEFT JOIN Accreditation a ON ia.AccreditationId = a.AccreditationId " +
                    "LEFT JOIN Booking b ON b.InstructorId = i.InstructorId " +
                    "LEFT JOIN Skier sk ON b.SkierId = sk.SkierId " +
                    "LEFT JOIN Lesson l ON b.LessonId = l.LessonId " +
                    "LEFT JOIN LessonType lt ON l.LessonTypeId = lt.LessonTypeId " +
                    "LEFT JOIN Period p ON b.PeriodId = p.PeriodId " +
                    "WHERE " +
                    "i.InstructorId = ?";

            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                if (instructor == null) {
                    instructor = new Instructor();
                    instructor.setPersonId(rs.getInt("InstructorId"));
                    instructor.setName(rs.getString("InstructorName"));
                    instructor.setFirstName(rs.getString("InstructorFirstName"));
                    instructor.setPseudo(rs.getString("InstructorPseudo"));

                    java.sql.Date dob = rs.getDate("InstructorDateOfBirth");
                    if (dob != null) {
                        instructor.setDateOfBirth(dob.toLocalDate());
                    }
                }

                int accreditationId = rs.getInt("AccreditationId");
                if (!accreditationMap.containsKey(accreditationId) && accreditationId != 0) {
                    Accreditation accreditation = new Accreditation();
                    accreditation.setId(accreditationId);
                    accreditation.setName(rs.getString("AccreditationName"));
                    accreditation.setSport(rs.getString("AccreditationSportType"));
                    accreditation.setAgeCategory(rs.getString("AccreditationAgeCategory"));
                    accreditationMap.put(accreditationId, accreditation);
                }

                int lessonId = rs.getInt("LessonId");
                Lesson lesson = lessonMap.get(lessonId);
                if (lesson == null && lessonId != 0) {
                    lesson = new Lesson();
                    lesson.setId(lessonId);
                    lesson.setMinBookings(rs.getInt("LessonMinBookings"));
                    lesson.setMaxBookings(rs.getInt("LessonMaxBookings"));
                    lesson.setDayPart(rs.getString("LessonDayPart"));
                    lesson.setCourseType(rs.getString("LessonCourseType"));
                    lesson.setNumberSkier(rs.getInt("NumberSkier"));
                    java.sql.Date dob = rs.getDate("LessonDateParticulier");
                    if (dob != null) {
                        lesson.setDate(dob.toLocalDate());
                    }

                    LessonType lessonType = new LessonType();
                    lessonType.setId(rs.getInt("LessonTypeId"));
                    lessonType.setLevel(rs.getString("LessonTypeLevel"));
                    lessonType.setPrice(rs.getDouble("LessonTypePrice"));
                    lessonType.setSport(rs.getString("LessonTypeSport"));
                    lessonType.setAgeCategory(rs.getString("LessonTypeAgeCategory"));
                    lesson.setLessonType(lessonType);

                    // Initialiser la liste des réservations
                    lesson.setBookings(new ArrayList<>());

                    lessonMap.put(lessonId, lesson);
                }

                int bookingId = rs.getInt("BookingId");
                if (!bookingMap.containsKey(bookingId) && bookingId != 0) {
                    Booking booking = new Booking();
                    booking.setId(bookingId);

                    java.sql.Date bookingDate = rs.getDate("BookingDateBooking");
                    if (bookingDate != null) {
                        booking.setDateBooking(bookingDate.toLocalDate());
                    }

                    Period period = new Period();
                    period.setId(rs.getInt("PeriodId"));

                    java.sql.Date periodStart = rs.getDate("PeriodStartDate");
                    java.sql.Date periodEnd = rs.getDate("PeriodEndDate");
                    if (periodStart != null) {
                        period.setStartDate(periodStart.toLocalDate());
                    }
                    if (periodEnd != null) {
                        period.setEndDate(periodEnd.toLocalDate());
                    }

                    period.setVacation(rs.getBoolean("PeriodIsVacation"));
                    booking.setPeriod(period);

                    // Récupération du skieur
                    int skierId = rs.getInt("SkierId");
                    if (skierId != 0) {
                        Skier skier = new Skier();
                        skier.setPersonId(skierId);
                        skier.setName(rs.getString("SkierName"));
                        skier.setFirstName(rs.getString("SkierFirstName"));
                        booking.setSkier(skier);
                    }

                    // Associer cette réservation à la leçon correspondante
                    if (lesson != null) {
                        lesson.getBookings().add(booking);
                    }

                    bookingMap.put(bookingId, booking);
                }
            }

            if (instructor != null) {
                instructor.setAccreditations(new ArrayList<>(accreditationMap.values()));
                instructor.setLessons(new ArrayList<>(lessonMap.values()));
                instructor.setBookings(new ArrayList<>(bookingMap.values()));
            }


            if (instructor != null) {
                instructor.setAccreditations(new ArrayList<>(accreditationMap.values()));
                instructor.setLessons(new ArrayList<>(lessonMap.values()));
                instructor.setBookings(new ArrayList<>(bookingMap.values()));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return instructor;
    }*/
    
    /*@Override
    public Instructor find(int id) {
        Instructor instructor = null;

        // Maps temporaires pour éviter les doublons
        Map<Integer, Accreditation> accreditationMap = new HashMap<>();
        Map<Integer, Lesson> lessonMap = new HashMap<>();
        Map<Integer, Booking> bookingMap = new HashMap<>();

        try {
            // Requête SQL mise à jour
            String query = "SELECT " +
                    "i.InstructorId AS InstructorId, " +
                    "i.Names AS InstructorName, " +
                    "i.FirstName AS InstructorFirstName, " +
                    "i.Pseudo AS InstructorPseudo, " +
                    "i.DateOfBirth AS InstructorDateOfBirth, " +
                    "a.AccreditationId AS AccreditationId, " +
                    "a.Names AS AccreditationName, " +
                    "a.SportType AS AccreditationSportType, " +
                    "a.AgeCategory AS AccreditationAgeCategory, " +
                    "l.LessonId AS LessonId, " +
                    "l.NumberSkier AS NumberSkier, " +
                    "l.MinBookings AS LessonMinBookings, " +
                    "l.MaxBookings AS LessonMaxBookings, " +
                    "l.DayPart AS LessonDayPart, " +
                    "l.CourseType AS LessonCourseType, " +
                    "lt.LessonTypeId AS LessonTypeId, " +
                    "lt.Levels AS LessonTypeLevel, " +
                    "lt.Price AS LessonTypePrice, " +
                    "lt.Sport AS LessonTypeSport, " +
                    "lt.AgeCategory AS LessonTypeAgeCategory, " +
                    "b.BookingId AS BookingId, " +
                    "b.DateBooking AS BookingDateBooking, " +
                    "b.DateParticulier AS LessonDateParticulier, " +
                    "p.PeriodId AS PeriodId, " +
                    "p.StartDate AS PeriodStartDate, " +
                    "p.EndDate AS PeriodEndDate, " +
                    "p.IsVacation AS PeriodIsVacation, " +
                    "sk.SkierId AS SkierId, " +
                    "sk.Names AS SkierName, " +
                    "sk.FirstName AS SkierFirstName " +
                    "FROM " +
                    "Instructor i " +
                    "LEFT JOIN Ins_Accreditation ia ON i.InstructorId = ia.InstructorId " +
                    "LEFT JOIN Accreditation a ON ia.AccreditationId = a.AccreditationId " +
                    "LEFT JOIN Booking b ON b.InstructorId = i.InstructorId " +
                    "LEFT JOIN Skier sk ON b.SkierId = sk.SkierId " +
                    "LEFT JOIN Lesson l ON b.LessonId = l.LessonId " +
                    "LEFT JOIN LessonType lt ON l.LessonTypeId = lt.LessonTypeId " +
                    "LEFT JOIN Period p ON b.PeriodId = p.PeriodId " +
                    "WHERE " +
                    "i.InstructorId = ?";

            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                // Initialisation de l'instructeur
                if (instructor == null) {
                    instructor = new Instructor();
                    instructor.setPersonId(rs.getInt("InstructorId"));
                    instructor.setName(rs.getString("InstructorName"));
                    instructor.setFirstName(rs.getString("InstructorFirstName"));
                    instructor.setPseudo(rs.getString("InstructorPseudo"));

                    java.sql.Date dob = rs.getDate("InstructorDateOfBirth");
                    if (dob != null) {
                        instructor.setDateOfBirth(dob.toLocalDate());
                    }
                }

                // Récupération et ajout des accréditations
                int accreditationId = rs.getInt("AccreditationId");
                if (!accreditationMap.containsKey(accreditationId) && accreditationId != 0) {
                    Accreditation accreditation = new Accreditation();
                    accreditation.setId(accreditationId);
                    accreditation.setName(rs.getString("AccreditationName"));
                    accreditation.setSport(rs.getString("AccreditationSportType"));
                    accreditation.setAgeCategory(rs.getString("AccreditationAgeCategory"));
                    accreditationMap.put(accreditationId, accreditation);
                    instructor.AddAccreditation(accreditation); // Utilisation de la méthode du modèle
                }

                // Récupération et ajout des leçons
                int lessonId = rs.getInt("LessonId");
                Lesson lesson = lessonMap.get(lessonId);
                if (lesson == null && lessonId != 0) {
                    lesson = new Lesson();
                    lesson.setId(lessonId);
                    lesson.setMinBookings(rs.getInt("LessonMinBookings"));
                    lesson.setMaxBookings(rs.getInt("LessonMaxBookings"));
                    lesson.setDayPart(rs.getString("LessonDayPart"));
                    lesson.setCourseType(rs.getString("LessonCourseType"));
                    lesson.setNumberSkier(rs.getInt("NumberSkier"));

                    java.sql.Date lessonDate = rs.getDate("LessonDateParticulier");
                    if (lessonDate != null) {
                        lesson.setDate(lessonDate.toLocalDate());
                    }

                    LessonType lessonType = new LessonType();
                    lessonType.setId(rs.getInt("LessonTypeId"));
                    lessonType.setLevel(rs.getString("LessonTypeLevel"));
                    lessonType.setPrice(rs.getDouble("LessonTypePrice"));
                    lessonType.setSport(rs.getString("LessonTypeSport"));
                    lessonType.setAgeCategory(rs.getString("LessonTypeAgeCategory"));
                    lesson.setLessonType(lessonType);

                    lessonMap.put(lessonId, lesson);
                    instructor.AddLesson(lesson); // Utilisation de la méthode du modèle
                }

                // Récupération et ajout des réservations
                int bookingId = rs.getInt("BookingId");
                Booking booking = bookingMap.get(bookingId);
                if (booking == null && bookingId != 0) {
                    booking = new Booking();
                    booking.setId(bookingId);

                    java.sql.Date bookingDate = rs.getDate("BookingDateBooking");
                    if (bookingDate != null) {
                        booking.setDateBooking(bookingDate.toLocalDate());
                    }

                    Period period = new Period();
                    period.setId(rs.getInt("PeriodId"));

                    java.sql.Date periodStart = rs.getDate("PeriodStartDate");
                    java.sql.Date periodEnd = rs.getDate("PeriodEndDate");
                    if (periodStart != null) {
                        period.setStartDate(periodStart.toLocalDate());
                    }
                    if (periodEnd != null) {
                        period.setEndDate(periodEnd.toLocalDate());
                    }
                    period.setVacation(rs.getBoolean("PeriodIsVacation"));
                    booking.setPeriod(period);

                    int skierId = rs.getInt("SkierId");
                    if (skierId != 0) {
                        Skier skier = new Skier();
                        skier.setPersonId(skierId);
                        skier.setName(rs.getString("SkierName"));
                        skier.setFirstName(rs.getString("SkierFirstName"));
                        booking.setSkier(skier);
                    }

                    bookingMap.put(bookingId, booking);
                    instructor.AddBooking(booking); // Utilisation de la méthode du modèle
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return instructor;
    }*/
    /*@Override
    public Instructor find(int id) {
        Instructor instructor = null;

        // Maps temporaires pour éviter les doublons
        Map<Integer, Accreditation> accreditationMap = new HashMap<>();
        Map<Integer, Lesson> lessonMap = new HashMap<>();
        Map<Integer, Booking> bookingMap = new HashMap<>();

        try {
            // Requête SQL mise à jour
            String query = "SELECT " +
                    "i.InstructorId AS InstructorId, " +
                    "i.Names AS InstructorName, " +
                    "i.FirstName AS InstructorFirstName, " +
                    "i.Pseudo AS InstructorPseudo, " +
                    "i.DateOfBirth AS InstructorDateOfBirth, " +
                    "a.AccreditationId AS AccreditationId, " +
                    "a.Names AS AccreditationName, " +
                    "a.SportType AS AccreditationSportType, " +
                    "a.AgeCategory AS AccreditationAgeCategory, " +
                    "l.LessonId AS LessonId, " +
                    "l.NumberSkier AS NumberSkier, " +
                    "l.MinBookings AS LessonMinBookings, " +
                    "l.MaxBookings AS LessonMaxBookings, " +
                    "l.DayPart AS LessonDayPart, " +
                    "l.CourseType AS LessonCourseType, " +
                    "lt.LessonTypeId AS LessonTypeId, " +
                    "lt.Levels AS LessonTypeLevel, " +
                    "lt.Price AS LessonTypePrice, " +
                    "lt.Sport AS LessonTypeSport, " +
                    "lt.AgeCategory AS LessonTypeAgeCategory, " +
                    "b.BookingId AS BookingId, " +
                    "b.DateBooking AS BookingDateBooking, " +
                    "b.DateParticulier AS LessonDateParticulier, " +
                    "p.PeriodId AS PeriodId, " +
                    "p.StartDate AS PeriodStartDate, " +
                    "p.EndDate AS PeriodEndDate, " +
                    "p.IsVacation AS PeriodIsVacation, " +
                    "sk.SkierId AS SkierId, " +
                    "sk.Names AS SkierName, " +
                    "sk.FirstName AS SkierFirstName " +
                    "FROM " +
                    "Instructor i " +
                    "LEFT JOIN Ins_Accreditation ia ON i.InstructorId = ia.InstructorId " +
                    "LEFT JOIN Accreditation a ON ia.AccreditationId = a.AccreditationId " +
                    "LEFT JOIN Booking b ON b.InstructorId = i.InstructorId " +
                    "LEFT JOIN Skier sk ON b.SkierId = sk.SkierId " +
                    "LEFT JOIN Lesson l ON b.LessonId = l.LessonId " +
                    "LEFT JOIN LessonType lt ON l.LessonTypeId = lt.LessonTypeId " +
                    "LEFT JOIN Period p ON b.PeriodId = p.PeriodId " +
                    "WHERE " +
                    "i.InstructorId = ?";

            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                // Initialisation de l'instructeur
                if (instructor == null) {
                    instructor = new Instructor();
                    instructor.setPersonId(rs.getInt("InstructorId"));
                    instructor.setName(rs.getString("InstructorName"));
                    instructor.setFirstName(rs.getString("InstructorFirstName"));
                    instructor.setPseudo(rs.getString("InstructorPseudo"));

                    java.sql.Date dob = rs.getDate("InstructorDateOfBirth");
                    if (dob != null) {
                        instructor.setDateOfBirth(dob.toLocalDate());
                    }
                }

                // Récupération et ajout des accréditations
                int accreditationId = rs.getInt("AccreditationId");
                if (!accreditationMap.containsKey(accreditationId) && accreditationId != 0) {
                    Accreditation accreditation = new Accreditation();
                    accreditation.setId(accreditationId);
                    accreditation.setName(rs.getString("AccreditationName"));
                    accreditation.setSport(rs.getString("AccreditationSportType"));
                    accreditation.setAgeCategory(rs.getString("AccreditationAgeCategory"));
                    accreditationMap.put(accreditationId, accreditation);
                    instructor.AddAccreditation(accreditation);
                }

                // Récupération et ajout des leçons
                int lessonId = rs.getInt("LessonId");
                Lesson lesson = lessonMap.get(lessonId);
                if (lesson == null && lessonId != 0) {
                    lesson = new Lesson();
                    lesson.setId(lessonId);
                    lesson.setMinBookings(rs.getInt("LessonMinBookings"));
                    lesson.setMaxBookings(rs.getInt("LessonMaxBookings"));
                    lesson.setDayPart(rs.getString("LessonDayPart"));
                    lesson.setCourseType(rs.getString("LessonCourseType"));
                    lesson.setNumberSkier(rs.getInt("NumberSkier"));

                    java.sql.Date lessonDate = rs.getDate("LessonDateParticulier");
                    if (lessonDate != null) {
                        lesson.setDate(lessonDate.toLocalDate());
                    }

                    LessonType lessonType = new LessonType();
                    lessonType.setId(rs.getInt("LessonTypeId"));
                    lessonType.setLevel(rs.getString("LessonTypeLevel"));
                    lessonType.setPrice(rs.getDouble("LessonTypePrice"));
                    lessonType.setSport(rs.getString("LessonTypeSport"));
                    lessonType.setAgeCategory(rs.getString("LessonTypeAgeCategory"));
                    lesson.setLessonType(lessonType);

                    lessonMap.put(lessonId, lesson);
                    instructor.AddLesson(lesson);
                }

                // Récupération et ajout des réservations
                int bookingId = rs.getInt("BookingId");
                Booking booking = bookingMap.get(bookingId);
                if (booking == null && bookingId != 0) {
                    booking = new Booking();
                    booking.setId(bookingId);

                    java.sql.Date bookingDate = rs.getDate("BookingDateBooking");
                    if (bookingDate != null) {
                        booking.setDateBooking(bookingDate.toLocalDate());
                    }

                    Period period = new Period();
                    period.setId(rs.getInt("PeriodId"));

                    java.sql.Date periodStart = rs.getDate("PeriodStartDate");
                    java.sql.Date periodEnd = rs.getDate("PeriodEndDate");
                    if (periodStart != null) {
                        period.setStartDate(periodStart.toLocalDate());
                    }
                    if (periodEnd != null) {
                        period.setEndDate(periodEnd.toLocalDate());
                    }
                    period.setVacation(rs.getBoolean("PeriodIsVacation"));
                    booking.setPeriod(period);

                    int skierId = rs.getInt("SkierId");
                    if (skierId != 0) {
                        Skier skier = new Skier();
                        skier.setPersonId(skierId);
                        skier.setName(rs.getString("SkierName"));
                        skier.setFirstName(rs.getString("SkierFirstName"));
                        booking.setSkier(skier);
                    }

                    // Lier la réservation à une leçon
                    if (lesson != null) {
                        booking.setLesson(lesson);
                        lesson.AddBooking(booking); // Utilisation de la méthode du modèle Lesson
                    }

                    bookingMap.put(bookingId, booking);
                    instructor.AddBooking(booking);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return instructor;
    }*/
    @Override
    public Instructor find(int id) {
        Instructor instructor = null;

        // Maps temporaires pour éviter les doublons
        Map<Integer, Accreditation> accreditationMap = new HashMap<>();
        Map<Integer, Lesson> lessonMap = new HashMap<>();
        Map<Integer, Booking> bookingMap = new HashMap<>();

        try {
            // Requête SQL mise à jour
            String query = "SELECT " +
                    "i.InstructorId AS InstructorId, " +
                    "i.Names AS InstructorName, " +
                    "i.FirstName AS InstructorFirstName, " +
                    "i.Pseudo AS InstructorPseudo, " +
                    "i.DateOfBirth AS InstructorDateOfBirth, " +
                    "a.AccreditationId AS AccreditationId, " +
                    "a.Names AS AccreditationName, " +
                    "a.SportType AS AccreditationSportType, " +
                    "a.AgeCategory AS AccreditationAgeCategory, " +
                    "l.LessonId AS LessonId, " +
                    "l.NumberSkier AS NumberSkier, " +
                    "l.MinBookings AS LessonMinBookings, " +
                    "l.MaxBookings AS LessonMaxBookings, " +
                    "l.DayPart AS LessonDayPart, " +
                    "l.CourseType AS LessonCourseType, " +
                    "lt.LessonTypeId AS LessonTypeId, " +
                    "lt.Levels AS LessonTypeLevel, " +
                    "lt.Price AS LessonTypePrice, " +
                    "lt.Sport AS LessonTypeSport, " +
                    "lt.AgeCategory AS LessonTypeAgeCategory, " +
                    "b.BookingId AS BookingId, " +
                    "b.DateBooking AS BookingDateBooking, " +
                    "b.DateParticulier AS LessonDateParticulier, " +
                    "p.PeriodId AS PeriodId, " +
                    "p.StartDate AS PeriodStartDate, " +
                    "p.EndDate AS PeriodEndDate, " +
                    "p.IsVacation AS PeriodIsVacation, " +
                    "sk.SkierId AS SkierId, " +
                    "sk.Names AS SkierName, " +
                    "sk.FirstName AS SkierFirstName " +
                    "FROM " +
                    "Instructor i " +
                    "LEFT JOIN Ins_Accreditation ia ON i.InstructorId = ia.InstructorId " +
                    "LEFT JOIN Accreditation a ON ia.AccreditationId = a.AccreditationId " +
                    "LEFT JOIN Booking b ON b.InstructorId = i.InstructorId " +
                    "LEFT JOIN Skier sk ON b.SkierId = sk.SkierId " +
                    "LEFT JOIN Lesson l ON b.LessonId = l.LessonId " +
                    "LEFT JOIN LessonType lt ON l.LessonTypeId = lt.LessonTypeId " +
                    "LEFT JOIN Period p ON b.PeriodId = p.PeriodId " +
                    "WHERE " +
                    "i.InstructorId = ?";

            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                // Initialisation de l'instructeur
                if (instructor == null) {
                    instructor = new Instructor();
                    instructor.setPersonId(rs.getInt("InstructorId"));
                    instructor.setName(rs.getString("InstructorName"));
                    instructor.setFirstName(rs.getString("InstructorFirstName"));
                    instructor.setPseudo(rs.getString("InstructorPseudo"));

                    java.sql.Date dob = rs.getDate("InstructorDateOfBirth");
                    if (dob != null) {
                        instructor.setDateOfBirth(dob.toLocalDate());
                    }
                }

                // Récupération et ajout des accréditations
                int accreditationId = rs.getInt("AccreditationId");
                if (!accreditationMap.containsKey(accreditationId) && accreditationId != 0) {
                    Accreditation accreditation = new Accreditation();
                    accreditation.setId(accreditationId);
                    accreditation.setName(rs.getString("AccreditationName"));
                    accreditation.setSport(rs.getString("AccreditationSportType"));
                    accreditation.setAgeCategory(rs.getString("AccreditationAgeCategory"));
                    accreditationMap.put(accreditationId, accreditation);
                    instructor.AddAccreditation(accreditation);
                }

                // Récupération et ajout des leçons (particulières et collectives)
                int lessonId = rs.getInt("LessonId");
                Lesson lesson = lessonMap.get(lessonId);
                if (lesson == null && lessonId != 0) {
                    lesson = new Lesson();
                    lesson.setId(lessonId);
                    lesson.setMinBookings(rs.getInt("LessonMinBookings"));
                    lesson.setMaxBookings(rs.getInt("LessonMaxBookings"));
                    lesson.setDayPart(rs.getString("LessonDayPart"));
                    lesson.setCourseType(rs.getString("LessonCourseType"));
                    lesson.setNumberSkier(rs.getInt("NumberSkier"));

                    java.sql.Date lessonDate = rs.getDate("LessonDateParticulier");
                    if (lessonDate != null) {
                        lesson.setDate(lessonDate.toLocalDate());
                    }

                    LessonType lessonType = new LessonType();
                    lessonType.setId(rs.getInt("LessonTypeId"));
                    lessonType.setLevel(rs.getString("LessonTypeLevel"));
                    lessonType.setPrice(rs.getDouble("LessonTypePrice"));
                    lessonType.setSport(rs.getString("LessonTypeSport"));
                    lessonType.setAgeCategory(rs.getString("LessonTypeAgeCategory"));
                    lesson.setLessonType(lessonType);

                    lessonMap.put(lessonId, lesson);
                    instructor.AddLesson(lesson);
                }

                // Récupération et ajout des réservations
                int bookingId = rs.getInt("BookingId");
                Booking booking = bookingMap.get(bookingId);
                if (booking == null && bookingId != 0) {
                    booking = new Booking();
                    booking.setId(bookingId);

                    java.sql.Date bookingDate = rs.getDate("BookingDateBooking");
                    if (bookingDate != null) {
                        booking.setDateBooking(bookingDate.toLocalDate());
                    }

                    Period period = new Period();
                    period.setId(rs.getInt("PeriodId"));

                    java.sql.Date periodStart = rs.getDate("PeriodStartDate");
                    java.sql.Date periodEnd = rs.getDate("PeriodEndDate");
                    if (periodStart != null) {
                        period.setStartDate(periodStart.toLocalDate());
                    }
                    if (periodEnd != null) {
                        period.setEndDate(periodEnd.toLocalDate());
                    }
                    period.setVacation(rs.getBoolean("PeriodIsVacation"));
                    booking.setPeriod(period);

                    int skierId = rs.getInt("SkierId");
                    if (skierId != 0) {
                        Skier skier = new Skier();
                        skier.setPersonId(skierId);
                        skier.setName(rs.getString("SkierName"));
                        skier.setFirstName(rs.getString("SkierFirstName"));
                        booking.setSkier(skier);
                    }

                    // Lier la réservation à une leçon
                    if (lesson != null) {
                        booking.setLesson(lesson);
                        lesson.AddBooking(booking); // Utilisation de la méthode du modèle Lesson
                    }

                    bookingMap.put(bookingId, booking);
                    instructor.AddBooking(booking);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return instructor;
    }




    




    public boolean testConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                System.out.println("Connexion établie avec succès !");
                return true;
            } else {
                System.out.println("Échec de la connexion.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la connexion : " + e.getMessage());
            return false;
        }
    }

    /*@Override
    public boolean create(Instructor ins) {
        PreparedStatement pstmt = null;
        try {
            // Préparez la requête SQL d'insertion
            String sql = "INSERT INTO Instructor (Names, FirstName, Pseudo, DateOfBirth) VALUES (?, ?, ?, ?)";
            pstmt = connection.prepareStatement(sql);

            // Remplissez les paramètres de la requête
            pstmt.setString(1, ins.getName());
            pstmt.setString(2, ins.getFirstName());
            pstmt.setString(3, ins.getPseudo());
            pstmt.setDate(4, java.sql.Date.valueOf(ins.getDateOfBirth()));

            // Exécutez la requête
            int rowsInserted = pstmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }*/
    
    /*@Override
    public boolean create(Instructor instructor) {
        String sqlInstructor = "INSERT INTO Instructor ( Names, FirstName, Pseudo, DateOfBirth) VALUES ( ?, ?, ?, ?)";
        String sqlInsAccreditation = "INSERT INTO Ins_Accreditation (InstructorId, AccreditationId) VALUES (?, ?)";

        try (PreparedStatement pstmtInstructor = connection.prepareStatement(sqlInstructor, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement pstmtInsAccreditation = connection.prepareStatement(sqlInsAccreditation)) {

            // Insérer dans Instructor
            //pstmtInstructor.setInt(1, instructor.getInstructorId());
            pstmtInstructor.setString(1, instructor.getName());
            pstmtInstructor.setString(2, instructor.getFirstName());
            pstmtInstructor.setString(3, instructor.getPseudo());
            pstmtInstructor.setDate(4, java.sql.Date.valueOf(instructor.getDateOfBirth()));
            int rowsInsertedInstructor = pstmtInstructor.executeUpdate();

            // Obtenir l'InstructorId généré
            ResultSet rs = pstmtInstructor.getGeneratedKeys();
            if (rs.next() && rowsInsertedInstructor > 0) {
                int instructorId = rs.getInt(1);

                // Insérer dans Ins_Accreditation
                pstmtInsAccreditation.setInt(1, instructorId);
                pstmtInsAccreditation.setInt(2, instructor.getAccreditations().get(0));
                int rowsInsertedAccreditation = pstmtInsAccreditation.executeUpdate();
                return rowsInsertedAccreditation > 0;
            } else {
                return false; // Retourne false si l'InstructorId n'a pas été généré ou si l'insertion a échoué
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    	
    }*/
    @Override
    public boolean create(Instructor instructor) {
        String sqlInstructor = "INSERT INTO Instructor ( Names, FirstName, Pseudo, DateOfBirth) VALUES ( ?, ?, ?, ?)";
        String sqlInsAccreditation = "INSERT INTO Ins_Accreditation (InstructorId, AccreditationId) VALUES (?, ?)";

        try (PreparedStatement pstmtInstructor = connection.prepareStatement(sqlInstructor, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement pstmtInsAccreditation = connection.prepareStatement(sqlInsAccreditation)) {

            // Insérer dans Instructor
            pstmtInstructor.setString(1, instructor.getName());
            pstmtInstructor.setString(2, instructor.getFirstName());
            pstmtInstructor.setString(3, instructor.getPseudo());
            pstmtInstructor.setDate(4, java.sql.Date.valueOf(instructor.getDateOfBirth()));
            int rowsInsertedInstructor = pstmtInstructor.executeUpdate();

            // Obtenir l'InstructorId généré
            ResultSet rs = pstmtInstructor.getGeneratedKeys();
            if (rs.next() && rowsInsertedInstructor > 0) {
                int instructorId = rs.getInt(1);

                // Récupérer la première accréditation de la liste
                Accreditation accreditation = instructor.getAccreditations().get(0);
                int accreditationId = accreditation.getId();

                // Insérer dans Ins_Accreditation
                pstmtInsAccreditation.setInt(1, instructorId);
                pstmtInsAccreditation.setInt(2, accreditationId);
                int rowsInsertedAccreditation = pstmtInsAccreditation.executeUpdate();
                return rowsInsertedAccreditation > 0;
            } else {
                return false; // Retourne false si l'InstructorId n'a pas été généré ou si l'insertion a échoué
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



    public boolean insertAcc_Instructor(int instructorId, int accreditationId) {
        PreparedStatement pstmt = null;
        try {
            // Préparez la requête SQL d'insertion
            String sql = "INSERT INTO Ins_Accreditation (InstructorId, AccreditationId) VALUES (?, ?)";
            pstmt = connection.prepareStatement(sql);

            // Remplissez les paramètres de la requête
            pstmt.setInt(1, instructorId);
            pstmt.setInt(2, accreditationId);

            // Exécutez la requête
            int rowsInserted = pstmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public int insertInstructor(Instructor i) {
        String query = "INSERT INTO Instructor (Names, FirstName, Pseudo, DateOfBirth) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            // Remplissage des paramètres de la requête en utilisant l'objet Instructor
            pstmt.setString(1, i.getName());
            pstmt.setString(2, i.getFirstName());
            pstmt.setString(3, i.getPseudo());
            pstmt.setDate(4, java.sql.Date.valueOf(i.getDateOfBirth()));

            // Exécution de la requête
            int rowsInserted = pstmt.executeUpdate();

            // Vérifie si l'insertion a réussi et récupère l'ID généré
            if (rowsInserted > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1); // Retourne l'ID de l'instructeur inséré
                    }
                }
            }
            return -1;

        } catch (SQLException ex) {
            System.err.println("Erreur lors de l'insertion de l'instructeur : " + ex.getMessage());
            return -1;
        }
    }
    
    public List<Instructor> getAllInstructor() {
        List<Instructor> instructors = new ArrayList<>();
        String sql = "SELECT Names, FirstName,Pseudo,DateOfBirth FROM Instructor"; // Vérifiez les noms des colonnes

        // Format de la date abrégé (à ajuster selon votre format exact dans Access)
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy"); // Changer selon le format utilisé
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                LocalDate dateOfBirth = null;

                // Conversion de String à LocalDate
                String dateString = resultSet.getString("DateOfBirth");
                if (dateString != null && !dateString.isEmpty()) {
                    // Le parsing peut lever une exception si le format ne correspond pas
                    try {
                        dateOfBirth = LocalDate.parse(dateString, formatter); // Parsing en LocalDate
                    } catch (Exception e) {
                        System.err.println("Erreur de parsing de la date : " + dateString + " - " + e.getMessage());
                    }
                }

                // Création de l'instructeur
                Instructor instructor = new Instructor(
                    resultSet.getString("Names"),
                    resultSet.getString("FirstName"),
                    dateOfBirth,
                    resultSet.getString("Pseudo")
                );
                instructors.add(instructor);
            }
        } catch (SQLException ex) {
            System.err.println("Erreur lors de l'insertion de l'instructeur : " + ex.getMessage());
        }
        return instructors;
    }
    
    public List<Instructor> getInstructorsByLessonType(/*String level,*/ String category, String targetAudience) {
        List<Instructor> instructors = new ArrayList<>();
        String sql = "SELECT DISTINCT i.Names, i.FirstName, i.DateOfBirth, i.Pseudo " +
                     "FROM Instructor i " +
                     "JOIN Ins_Accreditation ins_acc ON i.InstructorId = ins_acc.InstructorId " +
                     "JOIN Accreditation acc ON ins_acc.AccreditationId = acc.AccreditationId " +
                     "JOIN Acc_LessonType alt ON acc.AccreditationId = alt.AccreditationId " +
                     "JOIN LessonType lt ON alt.LessonTypeId = lt.LessonTypeId " +
                     "WHERE lt.AgeCategory = ? AND lt.Sport = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            //statement.setString(1, level);
            statement.setString(1, targetAudience);
            statement.setString(2, category);
            
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
    }
    
   
    public int getInstructorIdByName(String name){
        String sql = "SELECT Instructorid FROM Instructor WHERE Pseudo = ?"; // Ajustez la requête selon votre schéma de table
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("InstructorId"); 
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'ID de l'instructeur : " + e.getMessage());
            
        }
        return -1; 
    }
    
    /*public boolean isInstructorAvailable(Instructor i, int periodId, String timeSlot) {
        String sql = "SELECT Lesson.LessonId " +
                     "FROM Lesson " +
                     "JOIN Booking ON Lesson.LessonId = Booking.LessonId " +
                     "JOIN Period ON Booking.PeriodId = Period.PeriodId " +
                     "WHERE Lesson.InstructorId = ? " +
                     "AND Period.PeriodId = ? " +
                     "AND Lesson.DayPart = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, i.getPersonId());
            pstmt.setInt(2, periodId);
            pstmt.setString(3, timeSlot);

            try (ResultSet rs = pstmt.executeQuery()) {
                // Si un résultat est trouvé, le moniteur est déjà assigné
                return !rs.next();
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification de la disponibilité du moniteur : " + e.getMessage());
        }
        return false; // Retourne false si l'instructeur n'est pas disponible ou si la requête échoue
    }*/
    public boolean isInstructorAvailable(Instructor instructor, int periodId, String timeSlot) {
        String sql = """
            SELECT b.BookingId 
            FROM Booking b
            JOIN Lesson l ON b.LessonId = l.LessonId
            JOIN Period p ON b.PeriodId = p.PeriodId
            WHERE b.InstructorId = ? 
            AND p.PeriodId = ? 
            AND l.DayPart = ?
        """;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            // Paramètres de la requête
            pstmt.setInt(1, instructor.getPersonId()); // Utilise l'ID de l'instructeur depuis l'objet Instructor
            pstmt.setInt(2, periodId);                // ID de la période
            pstmt.setString(3, timeSlot);             // Créneau horaire

            try (ResultSet rs = pstmt.executeQuery()) {
                // Si un résultat est trouvé, le moniteur est déjà assigné à ce créneau
                return !rs.next();
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification de la disponibilité du moniteur : " + e.getMessage());
        }

        return false; // Retourne false si l'instructeur n'est pas disponible ou si une erreur survient
    }

    
    
    
    /*public List<Instructor> getAllInstructorsWithAccreditationsAndLessonTypes() {
        List<Instructor> instructors = new ArrayList<>();
        
        // Requête SQL pour récupérer les instructeurs, accréditations et types de leçons associés
        String sql = "SELECT i.InstructorId, i.Names, i.FirstName, i.Pseudo, i.DateOfBirth, " +
                     "a.AccreditationId, a.Names AS AccreditationName, a.SportType, a.AgeCategory, " +
                     "lt.LessonTypeId, lt.Levels, lt.Price " +
                     "FROM Instructor i " +
                     "JOIN Ins_Accreditation ia ON i.InstructorId = ia.InstructorId " +
                     "JOIN Accreditation a ON ia.AccreditationId = a.AccreditationId " +
                     "JOIN Acc_LessonType alt ON a.AccreditationId = alt.AccreditationId " +
                     "JOIN LessonType lt ON alt.LessonTypeId = lt.LessonTypeId";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Map pour éviter de créer des instructeurs en double
            Map<Integer, Instructor> instructorMap = new HashMap<>();

            // Parcours des résultats de la requête SQL
            while (rs.next()) {
                int instructorId = rs.getInt("InstructorId");

                // Si l'instructeur n'est pas encore dans la map, on l'ajoute
                if (!instructorMap.containsKey(instructorId)) {
                    Instructor instructor = new Instructor(
                            rs.getString("Names"),
                            rs.getString("FirstName"),
                            instructorId,
                            rs.getDate("DateOfBirth").toLocalDate(),
                            rs.getString("Pseudo"),
                            null// On initialisera plus tard les accréditations
                    );
                    instructorMap.put(instructorId, instructor);
                }

                // Récupérer l'instructeur existant
                Instructor instructor = instructorMap.get(instructorId);

                // Créer l'accréditation pour cet instructeur
                Accreditation accreditation = new Accreditation(
                        rs.getInt("AccreditationId"),
                        
                        rs.getString("AccreditationName"),
                        null,
                        rs.getString("SportType"),
                        rs.getString("AgeCategory")
                     
                );

                // Créer le LessonType pour l'accréditation
                LessonType lessonType = new LessonType(
                        rs.getInt("LessonTypeId"),
                        rs.getString("Levels"),
                        rs.getDouble("Price")
                );

                // Associer le LessonType à l'accréditation
                accreditation.AddLessonType(lessonType);

                // Ajouter l'accréditation à l'instructeur
                instructor.AddAccreditation(accreditation);
            }

            // Retourner la liste des instructeurs avec leurs accréditations et leçons associées
            return new ArrayList<>(instructorMap.values());

        } catch (SQLException ex) {
            // Gérer les erreurs de la base de données
            System.err.println("Erreur lors de la récupération des instructeurs et de leurs accréditations : " + ex.getMessage());
            return Collections.emptyList();
        }
    }*/
    public List<Instructor> getAllInstructorsWithAccreditationsAndLessonTypes() {
        List<Instructor> instructors = new ArrayList<>();
        
        // Requête SQL pour récupérer les instructeurs, accréditations et types de leçons associés
        String sql = "SELECT i.InstructorId, i.Names, i.FirstName, i.Pseudo, i.DateOfBirth, " +
                     "a.AccreditationId, a.Names AS AccreditationName, a.SportType, a.AgeCategory, " +
                     "lt.LessonTypeId, lt.Levels, lt.Price " +
                     "FROM Instructor i " +
                     "JOIN Ins_Accreditation ia ON i.InstructorId = ia.InstructorId " +
                     "JOIN Accreditation a ON ia.AccreditationId = a.AccreditationId " +
                     "JOIN Acc_LessonType alt ON a.AccreditationId = alt.AccreditationId " +
                     "JOIN LessonType lt ON alt.LessonTypeId = lt.LessonTypeId";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Map pour éviter de créer des instructeurs et accréditations en double
            Map<Integer, Instructor> instructorMap = new HashMap<>();
            Map<Integer, Accreditation> accreditationMap = new HashMap<>();

            while (rs.next()) {
                int instructorId = rs.getInt("InstructorId");
                int accreditationId = rs.getInt("AccreditationId");
                int lessonTypeId = rs.getInt("LessonTypeId");

                // Récupérer ou créer l'instructeur
                Instructor instructor;
                if (!instructorMap.containsKey(instructorId)) {
                    // Créer la première accréditation
                    Accreditation firstAccreditation = new Accreditation(
                            accreditationId,
                            rs.getString("AccreditationName"),
                            null, // Le premier LessonType sera ajouté
                            rs.getString("SportType"),
                            rs.getString("AgeCategory")
                    );

                    // Créer le premier LessonType et l'ajouter à l'accréditation
                    LessonType firstLessonType = new LessonType(
                            lessonTypeId,
                            rs.getString("Levels"),
                            rs.getDouble("Price")
                    );
                    firstAccreditation.AddLessonType(firstLessonType);

                    // Initialiser l'instructeur avec la première accréditation
                    instructor = new Instructor(
                            rs.getString("Names"),
                            rs.getString("FirstName"),
                            instructorId,
                            rs.getDate("DateOfBirth").toLocalDate(),
                            rs.getString("Pseudo"),
                            firstAccreditation // On passe la première accréditation
                    );
                    instructorMap.put(instructorId, instructor);

                    // Ajouter la première accréditation dans la map
                    accreditationMap.put(accreditationId, firstAccreditation);
                } else {
                    instructor = instructorMap.get(instructorId);
                }

                // Vérifier si l'accréditation existe déjà
                Accreditation accreditation;
                if (accreditationMap.containsKey(accreditationId)) {
                    accreditation = accreditationMap.get(accreditationId);
                } else {
                    // Créer une nouvelle accréditation si elle n'existe pas encore
                    accreditation = new Accreditation(
                            accreditationId,
                            rs.getString("AccreditationName"),
                            null, // Le `LessonType` sera ajouté après
                            rs.getString("SportType"),
                            rs.getString("AgeCategory")
                    );
                    instructor.AddAccreditation(accreditation);
                    accreditationMap.put(accreditationId, accreditation);
                }

                // Ajouter le `LessonType` à l'accréditation
                LessonType lessonType = new LessonType(
                        lessonTypeId,
                        rs.getString("Levels"),
                        rs.getDouble("Price")
                );
                if (!accreditation.getLessonTypes().contains(lessonType)) {
                    accreditation.AddLessonType(lessonType);
                }
            }

            return new ArrayList<>(instructorMap.values());

        } catch (SQLException ex) {
            System.err.println("Erreur lors de la récupération des instructeurs et de leurs accréditations : " + ex.getMessage());
            return Collections.emptyList();
        }
    }
    
    
    /*public List<Instructor> getInstructorsWithAccreditationsAndLessonTypesByLessonTypeId(int lessonTypeId) {
        List<Instructor> instructors = new ArrayList<>();
        
        // Requête SQL pour récupérer les instructeurs, accréditations et types de leçons associés,
        // filtrée par le LessonTypeId
        String sql = "SELECT i.InstructorId, i.Names, i.FirstName, i.Pseudo, i.DateOfBirth, " +
                     "a.AccreditationId, a.Names AS AccreditationName, a.SportType, a.AgeCategory, " +
                     "lt.LessonTypeId, lt.Levels, lt.Price " +
                     "FROM Instructor i " +
                     "JOIN Ins_Accreditation ia ON i.InstructorId = ia.InstructorId " +
                     "JOIN Accreditation a ON ia.AccreditationId = a.AccreditationId " +
                     "JOIN Acc_LessonType alt ON a.AccreditationId = alt.AccreditationId " +
                     "JOIN LessonType lt ON alt.LessonTypeId = lt.LessonTypeId " +
                     "WHERE lt.LessonTypeId = ?"; // Utilisation du paramètre pour filtrer par LessonTypeId

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, lessonTypeId); // Paramètre pour filtrer par LessonTypeId
            try (ResultSet rs = stmt.executeQuery()) {

                // Map pour éviter de créer des instructeurs et accréditations en double
                Map<Integer, Instructor> instructorMap = new HashMap<>();
                Map<Integer, Accreditation> accreditationMap = new HashMap<>();

                while (rs.next()) {
                    int instructorId = rs.getInt("InstructorId");
                    int accreditationId = rs.getInt("AccreditationId");
                    int currentLessonTypeId = rs.getInt("LessonTypeId");

                    // Récupérer ou créer l'instructeur
                    Instructor instructor;
                    if (!instructorMap.containsKey(instructorId)) {
                        // Créer la première accréditation
                        Accreditation firstAccreditation = new Accreditation(
                                accreditationId,
                                rs.getString("AccreditationName"),
                                null, // Le premier LessonType sera ajouté
                                rs.getString("SportType"),
                                rs.getString("AgeCategory")
                        );

                        // Créer le premier LessonType et l'ajouter à l'accréditation
                        LessonType firstLessonType = new LessonType(
                                currentLessonTypeId,
                                rs.getString("Levels"),
                                rs.getDouble("Price")
                        );
                        firstAccreditation.AddLessonType(firstLessonType);

                        // Initialiser l'instructeur avec la première accréditation
                        instructor = new Instructor(
                                rs.getString("Names"),
                                rs.getString("FirstName"),
                                instructorId,
                                rs.getDate("DateOfBirth").toLocalDate(),
                                rs.getString("Pseudo"),
                                firstAccreditation // On passe la première accréditation
                        );
                        instructorMap.put(instructorId, instructor);

                        // Ajouter la première accréditation dans la map
                        accreditationMap.put(accreditationId, firstAccreditation);
                    } else {
                        instructor = instructorMap.get(instructorId);
                    }

                    // Vérifier si l'accréditation existe déjà
                    Accreditation accreditation;
                    if (accreditationMap.containsKey(accreditationId)) {
                        accreditation = accreditationMap.get(accreditationId);
                    } else {
                        // Créer une nouvelle accréditation si elle n'existe pas encore
                        accreditation = new Accreditation(
                                accreditationId,
                                rs.getString("AccreditationName"),
                                null, // Le `LessonType` sera ajouté après
                                rs.getString("SportType"),
                                rs.getString("AgeCategory")
                        );
                        instructor.AddAccreditation(accreditation);
                        accreditationMap.put(accreditationId, accreditation);
                    }

                    // Ajouter le `LessonType` à l'accréditation
                    LessonType lessonType = new LessonType(
                            currentLessonTypeId,
                            rs.getString("Levels"),
                            rs.getDouble("Price")
                    );
                    if (!accreditation.getLessonTypes().contains(lessonType)) {
                        accreditation.AddLessonType(lessonType);
                    }
                }

                return new ArrayList<>(instructorMap.values());

            } catch (SQLException ex) {
                System.err.println("Erreur lors de la récupération des instructeurs et de leurs accréditations : " + ex.getMessage());
                return Collections.emptyList();
            }
        } catch (SQLException ex) {
            System.err.println("Erreur de préparation de la requête SQL : " + ex.getMessage());
            return Collections.emptyList();
        }
    }*/
    public List<Instructor> getInstructorsWithAccreditationsAndLessonTypesByLessonTypeId(int lessonTypeId) {
        List<Instructor> instructors = new ArrayList<>();
        
        // Requête SQL pour récupérer les instructeurs, accréditations et types de leçons associés,
        // filtrée par le LessonTypeId
        String sql = "SELECT i.InstructorId, i.Names, i.FirstName, i.Pseudo, i.DateOfBirth, " +
                     "a.AccreditationId, a.Names AS AccreditationName, a.SportType, a.AgeCategory, " +
                     "lt.LessonTypeId, lt.Levels, lt.Price " +
                     "FROM (Instructor i " +
                     "INNER JOIN Ins_Accreditation ia ON i.InstructorId = ia.InstructorId) " +
                     "INNER JOIN Accreditation a ON ia.AccreditationId = a.AccreditationId " +
                     "INNER JOIN LessonType lt ON a.AccreditationId = lt.AccreditationId " +
                     "WHERE lt.LessonTypeId = ?"; // Utilisation du paramètre pour filtrer par LessonTypeId

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, lessonTypeId); // Paramètre pour filtrer par LessonTypeId
            try (ResultSet rs = stmt.executeQuery()) {

                // Map pour éviter de créer des instructeurs et accréditations en double
                Map<Integer, Instructor> instructorMap = new HashMap<>();
                Map<Integer, Accreditation> accreditationMap = new HashMap<>();

                while (rs.next()) {
                    int instructorId = rs.getInt("InstructorId");
                    int accreditationId = rs.getInt("AccreditationId");
                    int currentLessonTypeId = rs.getInt("LessonTypeId");

                    // Récupérer ou créer l'instructeur
                    Instructor instructor;
                    if (!instructorMap.containsKey(instructorId)) {
                        // Créer la première accréditation
                        Accreditation firstAccreditation = new Accreditation(
                                accreditationId,
                                rs.getString("AccreditationName"),
                                null, // Le premier LessonType sera ajouté plus tard
                                rs.getString("SportType"),
                                rs.getString("AgeCategory")
                        );

                        // Créer le premier LessonType et l'ajouter à l'accréditation
                        LessonType firstLessonType = new LessonType(
                                currentLessonTypeId,
                                rs.getString("Levels"),
                                rs.getDouble("Price")
                        );
                        firstAccreditation.AddLessonType(firstLessonType);

                        // Initialiser l'instructeur avec la première accréditation
                        instructor = new Instructor(
                                rs.getString("Names"),
                                rs.getString("FirstName"),
                                instructorId,
                                rs.getDate("DateOfBirth").toLocalDate(),
                                rs.getString("Pseudo"),
                                firstAccreditation // On passe la première accréditation
                        );
                        instructorMap.put(instructorId, instructor);

                        // Ajouter la première accréditation dans la map
                        accreditationMap.put(accreditationId, firstAccreditation);
                    } else {
                        instructor = instructorMap.get(instructorId);
                    }

                    // Vérifier si l'accréditation existe déjà
                    Accreditation accreditation;
                    if (accreditationMap.containsKey(accreditationId)) {
                        accreditation = accreditationMap.get(accreditationId);
                    } else {
                        // Créer une nouvelle accréditation si elle n'existe pas encore
                        accreditation = new Accreditation(
                                accreditationId,
                                rs.getString("AccreditationName"),
                                null, // Le `LessonType` sera ajouté après
                                rs.getString("SportType"),
                                rs.getString("AgeCategory")
                        );
                        instructor.AddAccreditation(accreditation);
                        accreditationMap.put(accreditationId, accreditation);
                    }

                    // Ajouter le `LessonType` à l'accréditation
                    LessonType lessonType = new LessonType(
                            currentLessonTypeId,
                            rs.getString("Levels"),
                            rs.getDouble("Price")
                    );
                    if (!accreditation.getLessonTypes().contains(lessonType)) {
                        accreditation.AddLessonType(lessonType);
                    }
                }

                return new ArrayList<>(instructorMap.values());

            } catch (SQLException ex) {
                System.err.println("Erreur lors de la récupération des instructeurs et de leurs accréditations : " + ex.getMessage());
                return Collections.emptyList();
            }
        } catch (SQLException ex) {
            System.err.println("Erreur de préparation de la requête SQL : " + ex.getMessage());
            return Collections.emptyList();
        }
    }



    
    /*public Instructor getInstructorByPseudo(String pseudo) {
        String sql = "SELECT InstructorId, Names, FirstName, Pseudo, DateOfBirth FROM Instructor WHERE Pseudo = ?"; 
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, pseudo);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
            	Instructor instructor = new Instructor();
            	instructor.setPersonId(resultSet.getInt("InstructorId"));
            	instructor.setName(resultSet.getString("Names"));
            	instructor.setFirstName(resultSet.getString("FirstName"));
            	instructor.setPseudo(resultSet.getString("Pseudo"));
            	instructor.setDateOfBirth(resultSet.getDate("DateOfBirth").toLocalDate());
                
                return instructor;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du skieur : " + e.getMessage());
        }
        return null; // Retourne null si aucun résultat n'est trouvé ou en cas d'erreur
    }*/
    
    /*public Instructor getInstructorByPseudo(String pseudo) {
        String sql = "SELECT i.InstructorId, i.Names, i.FirstName, i.Pseudo, i.DateOfBirth, " +
                     "a.AccreditationId, a.Names AS AccreditationName, " +
                     "l.LessonTypeId, l.Levels, l.Price, l.Sport, l.AgeCategory " +
                     "FROM Instructor i " +
                     "LEFT JOIN Ins_Accreditation ia ON i.InstructorId = ia.InstructorId " +
                     "LEFT JOIN Accreditation a ON ia.AccreditationId = a.AccreditationId " +
                     "LEFT JOIN LessonType l ON a.AccreditationId = l.AccreditationId " +
                     "WHERE i.Pseudo = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, pseudo);
            ResultSet resultSet = preparedStatement.executeQuery();

            Map<Integer, Accreditation> accreditationMap = new HashMap<>();
            Instructor instructor = null;

            while (resultSet.next()) {
                if (instructor == null) {
                    instructor = new Instructor();
                    instructor.setPersonId(resultSet.getInt("InstructorId"));
                    instructor.setName(resultSet.getString("Names"));
                    instructor.setFirstName(resultSet.getString("FirstName"));
                    instructor.setPseudo(resultSet.getString("Pseudo"));
                    instructor.setDateOfBirth(resultSet.getDate("DateOfBirth").toLocalDate());
                }

                int accreditationId = resultSet.getInt("AccreditationId");
                if (accreditationId != 0) { // Vérifie si l'AccreditationId n'est pas nul
                    String accreditationName = resultSet.getString("AccreditationName");

                    Accreditation accreditation = accreditationMap.get(accreditationId);
                    if (accreditation == null) {
                        accreditation = new Accreditation(accreditationId, accreditationName);
                        accreditationMap.put(accreditationId, accreditation);
                    }

                    int lessonTypeId = resultSet.getInt("LessonTypeId");
                    if (lessonTypeId != 0) { // Vérifie si le LessonTypeId n'est pas nul
                        String levels = resultSet.getString("Levels");
                        double price = resultSet.getDouble("Price");
                        String sport = resultSet.getString("Sport");
                        String ageCategory = resultSet.getString("AgeCategory");

                        LessonType lessonType = new LessonType(lessonTypeId, levels,sport,price, ageCategory, accreditation);
                        accreditation.AddLessonType(lessonType);
                    }

                    instructor.AddAccreditation(accreditation);
                }
            }

            return instructor;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'instructeur : " + e.getMessage());
        }
        return null; // Retourne null si aucun résultat n'est trouvé ou en cas d'erreur
    }*/
    public Instructor getInstructorByPseudo(String pseudo) {
        String sql = "SELECT i.InstructorId, i.Names, i.FirstName, i.Pseudo, i.DateOfBirth, " +
                     "a.AccreditationId, a.Names AS AccreditationName, " +
                     "l.LessonTypeId, l.Levels, l.Price, l.Sport, l.AgeCategory " +
                     "FROM Instructor i " +
                     "LEFT JOIN Ins_Accreditation ia ON i.InstructorId = ia.InstructorId " +
                     "LEFT JOIN Accreditation a ON ia.AccreditationId = a.AccreditationId " +
                     "LEFT JOIN LessonType l ON a.AccreditationId = l.AccreditationId " +
                     "WHERE i.Pseudo = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, pseudo);
            ResultSet resultSet = preparedStatement.executeQuery();

            Map<Integer, Accreditation> accreditationMap = new HashMap<>();
            Instructor instructor = null;

            while (resultSet.next()) {
                if (instructor == null) {
                    instructor = new Instructor();
                    instructor.setPersonId(resultSet.getInt("InstructorId"));
                    instructor.setName(resultSet.getString("Names"));
                    instructor.setFirstName(resultSet.getString("FirstName"));
                    instructor.setPseudo(resultSet.getString("Pseudo"));
                    instructor.setDateOfBirth(resultSet.getDate("DateOfBirth").toLocalDate());
                }

                int accreditationId = resultSet.getInt("AccreditationId");
                if (accreditationId != 0) { // Vérifie si l'AccreditationId n'est pas nul
                    String accreditationName = resultSet.getString("AccreditationName");

                    Accreditation accreditation = accreditationMap.get(accreditationId);
                    if (accreditation == null) {
                        accreditation = new Accreditation(accreditationId, accreditationName);
                        accreditationMap.put(accreditationId, accreditation);
                    }

                    int lessonTypeId = resultSet.getInt("LessonTypeId");
                    if (lessonTypeId != 0) { // Vérifie si le LessonTypeId n'est pas nul
                        String levels = resultSet.getString("Levels");
                        double price = resultSet.getDouble("Price");
                        String sport = resultSet.getString("Sport");
                        String ageCategory = resultSet.getString("AgeCategory");

                        LessonType lessonType = new LessonType(lessonTypeId, levels, sport,  price,ageCategory, accreditation);
                        if (!accreditation.getLessonTypes().contains(lessonType)) {
                            accreditation.AddLessonType(lessonType);
                        }
                    }

                    if (!instructor.getAccreditations().contains(accreditation)) {
                        instructor.AddAccreditation(accreditation);
                    }
                }
            }

            return instructor;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'instructeur : " + e.getMessage());
        }
        return null; // Retourne null si aucun résultat n'est trouvé ou en cas d'erreur
    }



    
    
    




    

}
