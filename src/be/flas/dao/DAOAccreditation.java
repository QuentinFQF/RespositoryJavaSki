
package be.flas.dao;

import java.sql.Connection;



import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import be.flas.connection.DatabaseConnection;
import be.flas.interfaces.DaoGeneric;

import be.flas.model.Accreditation;
import be.flas.model.LessonType;



public class DAOAccreditation extends DaoGeneric<Accreditation> {

	public DAOAccreditation(Connection conn){
    	super(conn);
    }
    @Override
	public boolean delete(Accreditation obj){
	    return false;
	}
    @Override
	public boolean update(Accreditation obj){
	    return false;
	}
    @Override
	public Accreditation find(int id){
    	Accreditation s = new Accreditation();
		return s;
	}
    @Override
    public boolean create(Accreditation obj){
    	return false;
    }

    
    public List<Accreditation> getAll() {
        List<Accreditation> accreditations = new ArrayList<>();
        
        String query = "SELECT a.AccreditationId, a.Names, l.LessonTypeId, l.Levels, l.Price, l.Sport, l.AgeCategory " +
                       "FROM Accreditation a " +
                       "LEFT JOIN LessonType l ON a.AccreditationId = l.AccreditationId";

        try (PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet res = pstmt.executeQuery()) {

            
            Map<Integer, Accreditation> accreditationMap = new HashMap<>();

            while (res.next()) {
                int accreditationId = res.getInt("AccreditationId");
                String accreditationName = res.getString("Names");

                
                Accreditation accreditation = accreditationMap.get(accreditationId);
                if (accreditation == null) {
                    accreditation = new Accreditation(accreditationId, accreditationName);
                    accreditationMap.put(accreditationId, accreditation);
                }

                
                int lessonTypeId = res.getInt("LessonTypeId");
                if (lessonTypeId != 0) {
                    String levels = res.getString("Levels");
                    double price = res.getDouble("Price");
                    String sport = res.getString("Sport");
                    String ageCategory = res.getString("AgeCategory");

                    
                    LessonType lessonType = new LessonType(lessonTypeId, levels, sport, price, ageCategory, accreditation);

                    
                    accreditation.AddLessonType(lessonType);
                }
            }

            
            accreditations.addAll(accreditationMap.values());

        } catch (SQLException ex) {
            System.err.println("Erreur lors de la récupération des accréditations : " + ex.getMessage());
        }

        return accreditations;
    }



    //utiliser null aprt
    public int selectId(String names) {
        String query = "SELECT AccreditationId FROM Accreditation WHERE Names=?"; 

        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) { 
            pstmt.setString(1, names); 

            try (ResultSet rs = pstmt.executeQuery()) { 
                
                if (rs.next()) {
                    return rs.getInt("AccreditationId"); 
                } else {
                    return -1; 
                }
            }
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la récupération de l'ID : " + ex.getMessage());
            return -1; 
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
    
    
    //ici modifier
    /*public List<Accreditation> selectAccreditationsNotIns(int instructorId) {
        List<Accreditation> accreditations = new ArrayList<>();
        
       
        String query = """
            SELECT 
                a.AccreditationId, a.Names, a.AgeCategory, a.SportType,
                lt.LessonTypeId, lt.Levels, lt.Price
            FROM Accreditation a
            LEFT JOIN Acc_LessonType alt ON a.AccreditationId = alt.AccreditationId
            LEFT JOIN LessonType lt ON alt.LessonTypeId = lt.LessonTypeId
            WHERE a.AccreditationId NOT IN (
                SELECT ia.AccreditationId
                FROM Ins_Accreditation ia
                WHERE ia.InstructorId = ?
            )
            ORDER BY a.AccreditationId;
        """;

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            
            pstmt.setInt(1, instructorId);

            try (ResultSet res = pstmt.executeQuery()) {
                Map<Integer, Accreditation> accreditationMap = new HashMap<>();

                while (res.next()) {
                    int accreditationId = res.getInt("AccreditationId");
                    String accreditationName = res.getString("Names");
                    String ageCategory = res.getString("AgeCategory");
                    String sportType = res.getString("SportType");
                    
                    int lessonTypeId = res.getInt("LessonTypeId");
                    String levels = res.getString("Levels");
                    double price = res.getDouble("Price");

                    
                    Accreditation accreditation = accreditationMap.getOrDefault(
                            accreditationId, 
                            new Accreditation(accreditationId, accreditationName,null, sportType, ageCategory)
                    );

                    
                    if (lessonTypeId != 0) {
                        LessonType lessonType = new LessonType(lessonTypeId, levels, price);
                        accreditation.AddLessonType(lessonType);
                    }

                    
                    accreditationMap.put(accreditationId, accreditation);
                }

                
                accreditations.addAll(accreditationMap.values());
            }

        } catch (SQLException ex) {
            System.err.println("Erreur lors de la récupération des accréditations : " + ex.getMessage());
        }

        return accreditations;
    }*/
    
    public List<Accreditation> selectAccreditationsNotIns(int instructorId) {
        List<Accreditation> accreditations = new ArrayList<>();

        String query = """
            SELECT 
                a.AccreditationId, a.Names, a.AgeCategory, a.SportType,
                lt.LessonTypeId, lt.Levels, lt.Price
            FROM Accreditation a
            LEFT JOIN LessonType lt ON a.AccreditationId = lt.AccreditationId
            WHERE a.AccreditationId NOT IN (
                SELECT ia.AccreditationId
                FROM Ins_Accreditation ia
                WHERE ia.InstructorId = ?
            )
            ORDER BY a.AccreditationId;
        """;

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            // Bind the instructor ID parameter
            pstmt.setInt(1, instructorId);

            try (ResultSet res = pstmt.executeQuery()) {
                // Map to avoid duplicate accreditations
                Map<Integer, Accreditation> accreditationMap = new HashMap<>();

                while (res.next()) {
                    // Fetch accreditation details
                    int accreditationId = res.getInt("AccreditationId");
                    String accreditationName = res.getString("Names");
                    String ageCategory = res.getString("AgeCategory");
                    String sportType = res.getString("SportType");

                    // Fetch lesson type details (if present)
                    int lessonTypeId = res.getInt("LessonTypeId");
                    String levels = res.getString("Levels");
                    double price = res.getDouble("Price");

                    // Check if the accreditation already exists in the map
                    Accreditation accreditation = accreditationMap.getOrDefault(
                            accreditationId,
                            new Accreditation(accreditationId, accreditationName, null, sportType, ageCategory)
                    );

                    // Add lesson type to the accreditation if it exists
                    if (lessonTypeId != 0) {
                        LessonType lessonType = new LessonType(lessonTypeId, levels, price);
                        accreditation.AddLessonType(lessonType);
                    }

                    // Update the map
                    accreditationMap.put(accreditationId, accreditation);
                }

                // Collect all values from the map into the result list
                accreditations.addAll(accreditationMap.values());
            }

        } catch (SQLException ex) {
            System.err.println("Erreur lors de la récupération des accréditations : " + ex.getMessage());
        }

        return accreditations;
    }

   









}
