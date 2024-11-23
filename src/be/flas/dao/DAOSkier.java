package be.flas.dao;

import be.flas.connection.DatabaseConnection;
import be.flas.interfaces.DaoGeneric;
import be.flas.interfaces.IDaoClasse;
import be.flas.interfaces.IDaoSkier;
import be.flas.model.Booking;
import be.flas.model.Lesson;
import be.flas.model.LessonType;
import be.flas.model.Period;
import be.flas.model.Skier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DAOSkier extends DaoGeneric<Skier> {


    
    public DAOSkier(Connection conn){
    	super(conn);
    }
    @Override
    public boolean delete(Skier obj) {
        String deleteBookingsSql = "DELETE FROM Booking WHERE SkierId = ?";
        String deleteSkierSql = "DELETE FROM Skier WHERE SkierId = ?";
        
        try {
            
            connection.setAutoCommit(false);

            
            try (PreparedStatement pstmtBookings = connection.prepareStatement(deleteBookingsSql)) {
                pstmtBookings.setInt(1, obj.getPersonId());
                pstmtBookings.executeUpdate();
            }

           
            try (PreparedStatement pstmtSkier = connection.prepareStatement(deleteSkierSql)) {
                pstmtSkier.setInt(1, obj.getPersonId());
                int affectedRows = pstmtSkier.executeUpdate();

               
                connection.commit();
                
                return affectedRows > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du skieur et de ses réservations : " + e.getMessage());
            try {
                connection.rollback(); 
            } catch (SQLException rollbackEx) {
                System.err.println("Erreur lors du rollback : " + rollbackEx.getMessage());
            }
        } finally {
            try {
                connection.setAutoCommit(true); 
            } catch (SQLException ex) {
                System.err.println("Erreur lors de la réactivation de l'auto-commit : " + ex.getMessage());
            }
        }

        return false;
    }

    @Override
    public boolean update(Skier s) {
        String sql = "UPDATE Skier SET Pseudo=?, FirstName=?, Names=?, DateOfBirth=? WHERE SkierId=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, s.getPseudo());
            stmt.setString(2, s.getFirstName());
            stmt.setString(3, s.getName());
            stmt.setInt(5, s.getPersonId());
            stmt.setDate(4, java.sql.Date.valueOf(s.getDateOfBirth()));
            
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public Skier find(int id) {
        Skier skier = null;

        
        Map<Integer, Booking> bookingMap = new HashMap<>();
        Map<Integer, Lesson> lessonMap = new HashMap<>();
        Map<Integer, Period> periodMap = new HashMap<>();

        try {
            
            String query = "SELECT " +
                    "sk.SkierId AS SkierId, " +
                    "sk.Names AS SkierName, " +
                    "sk.FirstName AS SkierFirstName, " +
                    "sk.Pseudo AS SkierPseudo, " +
                    "sk.DateOfBirth AS SkierDateOfBirth, " +
                    "b.BookingId AS BookingId, " +
                    "b.DateBooking AS BookingDateBooking, " +
                    "b.DateParticulier AS BookingDateParticulier, " +
                    "l.LessonId AS LessonId, " +
                    "l.NumberSkier AS LessonNumberSkier, " +
                    "l.MinBookings AS LessonMinBookings, " +
                    "l.MaxBookings AS LessonMaxBookings, " +
                    "l.DayPart AS LessonDayPart, " +
                    "l.CourseType AS LessonCourseType, " +
                    "lt.LessonTypeId AS LessonTypeId, " +
                    "lt.Levels AS LessonTypeLevel, " +
                    "lt.Price AS LessonTypePrice, " +
                    "lt.Sport AS LessonTypeSport, " +
                    "lt.AgeCategory AS LessonTypeAgeCategory, " +
                    "p.PeriodId AS PeriodId, " +
                    "p.StartDate AS PeriodStartDate, " +
                    "p.EndDate AS PeriodEndDate, " +
                    "p.IsVacation AS PeriodIsVacation " +
                    "FROM Skier sk " +
                    "LEFT JOIN Booking b ON b.SkierId = sk.SkierId " +
                    "LEFT JOIN Lesson l ON b.LessonId = l.LessonId " +
                    "LEFT JOIN LessonType lt ON l.LessonTypeId = lt.LessonTypeId " +
                    "LEFT JOIN Period p ON b.PeriodId = p.PeriodId " +
                    "WHERE sk.SkierId = ?";

            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                
                if (skier == null) {
                    skier = new Skier();
                    skier.setPersonId(rs.getInt("SkierId"));
                    skier.setName(rs.getString("SkierName"));
                    skier.setFirstName(rs.getString("SkierFirstName"));
                    skier.setPseudo(rs.getString("SkierPseudo"));

                    
                    java.sql.Date dob = rs.getDate("SkierDateOfBirth");
                    if (dob != null) {
                        skier.setDateOfBirth(dob.toLocalDate());
                    }
                }

               
                int bookingId = rs.getInt("BookingId");
                if (bookingId != 0 && !bookingMap.containsKey(bookingId)) {
                    Booking booking = new Booking();
                    booking.setId(bookingId);

                 
                    java.sql.Date bookingDate = rs.getDate("BookingDateBooking");
                    if (bookingDate != null) {
                        booking.setDateBooking(bookingDate.toLocalDate());
                    }

                 
                    java.sql.Date bookingDateParticulier = rs.getDate("BookingDateParticulier");
                    if (bookingDateParticulier != null) {
                        
                        int lessonId = rs.getInt("LessonId");
                        Lesson lesson = lessonMap.get(lessonId);
                        if (lesson == null && lessonId != 0) {
                            lesson = new Lesson();
                            lesson.setId(lessonId);
                            lesson.setNumberSkier(rs.getInt("LessonNumberSkier"));
                            lesson.setMinBookings(rs.getInt("LessonMinBookings"));
                            lesson.setMaxBookings(rs.getInt("LessonMaxBookings"));
                            lesson.setDayPart(rs.getString("LessonDayPart"));
                            lesson.setCourseType(rs.getString("LessonCourseType"));

                          
                            LessonType lessonType = new LessonType();
                            lessonType.setId(rs.getInt("LessonTypeId"));
                            lessonType.setLevel(rs.getString("LessonTypeLevel"));
                            lessonType.setPrice(rs.getDouble("LessonTypePrice"));
                            lessonType.setSport(rs.getString("LessonTypeSport"));
                            lessonType.setAgeCategory(rs.getString("LessonTypeAgeCategory"));
                            lesson.setLessonType(lessonType);

                            lessonMap.put(lessonId, lesson);
                        }

                        
                        lesson.setDate(bookingDateParticulier.toLocalDate());
                        booking.setLesson(lesson);
                    } else {
                       
                        int lessonId = rs.getInt("LessonId");
                        Lesson lesson = lessonMap.get(lessonId);
                        if (lesson == null && lessonId != 0) {
                            lesson = new Lesson();
                            lesson.setId(lessonId);
                            lesson.setNumberSkier(rs.getInt("LessonNumberSkier"));
                            lesson.setMinBookings(rs.getInt("LessonMinBookings"));
                            lesson.setMaxBookings(rs.getInt("LessonMaxBookings"));
                            lesson.setDayPart(rs.getString("LessonDayPart"));
                            lesson.setCourseType(rs.getString("LessonCourseType"));

                            
                            LessonType lessonType = new LessonType();
                            lessonType.setId(rs.getInt("LessonTypeId"));
                            lessonType.setLevel(rs.getString("LessonTypeLevel"));
                            lessonType.setPrice(rs.getDouble("LessonTypePrice"));
                            lessonType.setSport(rs.getString("LessonTypeSport"));
                            lessonType.setAgeCategory(rs.getString("LessonTypeAgeCategory"));
                            lesson.setLessonType(lessonType);

                            lessonMap.put(lessonId, lesson);
                        }

                    
                        booking.setLesson(lesson);
                    }

                  
                    Period period = periodMap.get(rs.getInt("PeriodId"));
                    if (period == null && rs.getInt("PeriodId") != 0) {
                        period = new Period();
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
                        periodMap.put(period.getId(), period);
                    }
                    booking.setPeriod(period);

                    skier.AddBooking(booking);
                    bookingMap.put(bookingId, booking);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return skier;
    }

    









 
    @Override
    public boolean create(Skier skier) {
        String sql = "INSERT INTO Skier (Names, FirstName, Pseudo, DateOfBirth) VALUES (?, ?, ?, ?)";

        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            
            
            pstmt.setString(1, skier.getName());
            pstmt.setString(2, skier.getFirstName()); 
            pstmt.setString(3, skier.getPseudo()); 
            pstmt.setDate(4, java.sql.Date.valueOf(skier.getDateOfBirth())); 
            

            int rowsInserted = pstmt.executeUpdate();
            return rowsInserted > 0; 

        } catch (SQLException e) {
            e.printStackTrace();
            return false; 
        }
    }
    public boolean testConnection() {
        try (Connection connection = DatabaseConnection.getInstance().getConnection()) {
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


    
   
    public List<Skier> getAllSkiers() {
        List<Skier> skiers = new ArrayList<>();
        String sql = "SELECT SkierId,Names, FirstName, Pseudo FROM Skier"; // Adaptation nécessaire selon la structure de votre base de données

        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
            	int id = rs.getInt("SkierId");
                String nom = rs.getString("Names");
                String prenom = rs.getString("FirstName");
                String pseudo = rs.getString("Pseudo");

                Skier skier = new Skier(id,nom, prenom, pseudo);
                skiers.add(skier);
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des skieurs : " + e.getMessage());
        }

        return skiers;
    }
    
    public int getSkierIdByName(String name){
        String sql = "SELECT Skierid FROM Skier WHERE Pseudo = ?"; 
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("SkierId"); 
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'ID de l'instructeur : " + e.getMessage());
            
        }
        return -1; 
    }
    public Skier getSkierByPseudo(String pseudo) {
        String sql = "SELECT SkierId, Names, FirstName, Pseudo, DateOfBirth FROM Skier WHERE Pseudo = ?"; 
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, pseudo);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Skier skier = new Skier();
                skier.setPersonId(resultSet.getInt("SkierId"));
                skier.setName(resultSet.getString("Names"));
                skier.setFirstName(resultSet.getString("FirstName"));
                skier.setPseudo(resultSet.getString("Pseudo"));
                skier.setDateOfBirth(resultSet.getDate("DateOfBirth").toLocalDate());
                
                return skier;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du skieur : " + e.getMessage());
        }
        return null; 
    }

    
}
