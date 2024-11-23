package be.flas.dao;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import be.flas.interfaces.DaoGeneric;
import be.flas.interfaces.IDaoAccreditation;
import be.flas.interfaces.IDaoLessonType;
import be.flas.model.Accreditation;
import be.flas.model.Instructor;
import be.flas.model.LessonType;

public class DAOLessonType extends DaoGeneric<LessonType>{

	public DAOLessonType(Connection conn){
    	super(conn);
    }
    @Override
	public boolean delete(LessonType obj){
	    return false;
	}
    @Override
	public boolean update(LessonType obj){
	    return false;
	}
    
    
    @Override
    public LessonType find(int id) {
        LessonType lessonType = null;

        
        String sql = """
            SELECT lt.LessonTypeId, lt.Levels, lt.Price,
                   a.AccreditationId, a.Names AS AccreditationName, a.SportType, a.AgeCategory
            FROM LessonType lt
            LEFT JOIN Accreditation a ON lt.AccreditationId = a.AccreditationId
            WHERE lt.LessonTypeId = ?
        """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id); 

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    
                    Accreditation accreditation = new Accreditation(
                        rs.getInt("AccreditationId"),
                        rs.getString("AccreditationName"),
                        rs.getString("SportType"),
                        rs.getString("AgeCategory")
                    );

                    
                    lessonType = new LessonType(
                        rs.getInt("LessonTypeId"),
                        rs.getString("Levels"),
                        rs.getDouble("Price"),
                   
                        accreditation 
                    );
                    
                  
                    accreditation.AddLessonType(lessonType);
                }
            }
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la recherche du type de leçon avec ID " + id + " : " + ex.getMessage());
        }

        return lessonType;
    }


    @Override
    public boolean create(LessonType obj){
    	return false;
    }
    
    
    public List<String> selectLessonType() {
        List<String> names = new ArrayList<>();
        String query = "SELECT Levels, Price, Sport, AgeCategory FROM LessonType";

        try (PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet res = pstmt.executeQuery()) {

            while (res.next()) {
                
                String description = String.format("%s - %s - %s - %s",
                        res.getString("Levels"),
                        res.getString("Price"),
                        res.getString("Sport"),
                        res.getString("AgeCategory"));

                names.add(description);
            }
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la récupération des noms : " + ex.getMessage());
        }
        return names;
    }
    public List<LessonType> selectLessonTypes() {
        List<LessonType> lessonTypes = new ArrayList<>();
        Map<Integer, Accreditation> accreditationMap = new HashMap<>();

      
        String query = """
            SELECT lt.LessonTypeId, lt.Levels, lt.Price,
                   a.AccreditationId, a.Names AS AccreditationName, a.SportType, a.AgeCategory
            FROM LessonType lt
            LEFT JOIN Accreditation a ON lt.AccreditationId = a.AccreditationId
        """;

        try (PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet res = pstmt.executeQuery()) {

            while (res.next()) {
                
                int accreditationId = res.getInt("AccreditationId");
                String accreditationName = res.getString("AccreditationName");
                String sportType = res.getString("SportType");
                String ageCategory = res.getString("AgeCategory");

                
                Accreditation accreditation = accreditationMap.get(accreditationId);
                if (accreditation == null) {
                    accreditation = new Accreditation(accreditationId, accreditationName, sportType, ageCategory);
                    accreditationMap.put(accreditationId, accreditation);
                }

                
                int lessonTypeId = res.getInt("LessonTypeId");
                String levels = res.getString("Levels");
                double price = res.getDouble("Price");

               
                LessonType lessonType = new LessonType(lessonTypeId, levels, price ,accreditation);

             
                lessonTypes.add(lessonType);
                accreditation.AddLessonType(lessonType); 
            }

        } catch (SQLException ex) {
            System.err.println("Erreur lors de la récupération des types de leçon : " + ex.getMessage());
        }

      
        return lessonTypes;
    }


    
    
   
    public int getLessonTypeIdByName(String level,double price,String sport,String age) {
        String sql = "SELECT LessonTypeid FROM LessonType WHERE Levels = ? and Price = ? and Sport = ? and AgeCategory = ?"; 
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, level);
            preparedStatement.setDouble(2, price);
            preparedStatement.setString(3, sport);
            preparedStatement.setString(4, age);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("LessonTypeId"); 
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'ID du type de leçon : " + e.getMessage());
             
        }
        return -1; 
    }







}
