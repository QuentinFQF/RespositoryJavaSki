package be.flas.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import be.flas.interfaces.DaoGeneric;
import be.flas.interfaces.IDaoPeriod;
import be.flas.model.Accreditation;
import be.flas.model.Instructor;
import be.flas.model.Period;

public class DAOPeriod extends DaoGeneric<Period>{

	public DAOPeriod(Connection conn){
    	super(conn);
    }
    @Override
	public boolean delete(Period obj){
	    return false;
	}
    @Override
	public boolean update(Period obj){
	    return false;
	}
    @Override
    public Period find(int id) {
        Period period = null;

        
        String sql = "SELECT PeriodId, StartDate, EndDate, IsVacation " +
                     "FROM Period " +
                     "WHERE PeriodId = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id); 

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    
                    period = new Period(
                        rs.getInt("PeriodId"),
                        rs.getDate("StartDate").toLocalDate(), 
                        rs.getDate("EndDate").toLocalDate(),   
                        rs.getBoolean("IsVacation")
                    );
                }
            }
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la recherche de la période avec ID " + id + " : " + ex.getMessage());
        }

        return period;
    }

    @Override
    public boolean create(Period obj){
    	return false;
    }
    
    
    public List<Period> getAllPeriods() {
        List<Period> periods = new ArrayList<>();
        String sql = "SELECT PeriodId,StartDate, EndDate, IsVacation FROM Period";

        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                LocalDate startDate = null;
                LocalDate endDate = null;
                boolean isVacation = resultSet.getBoolean("IsVacation");
                int id=resultSet.getInt("PeriodId");

                
                String startDateString = resultSet.getString("StartDate");
                String endDateString = resultSet.getString("EndDate");

                if (startDateString != null && !startDateString.isEmpty()) {
                    try {
                        startDate = LocalDate.parse(startDateString, formatter);
                    } catch (Exception e) {
                        System.err.println("Erreur de parsing de la date de début : " + startDateString + " - " + e.getMessage());
                    }
                }

                if (endDateString != null && !endDateString.isEmpty()) {
                    try {
                        endDate = LocalDate.parse(endDateString, formatter);
                    } catch (Exception e) {
                        System.err.println("Erreur de parsing de la date de fin : " + endDateString + " - " + e.getMessage());
                    }
                }

                
                Period period = new Period(id,startDate, endDate, isVacation);
                periods.add(period);
            }
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la récupération des périodes : " + ex.getMessage());
        }
        return periods;
    }
    
    public int getPeriodIdBy(LocalDate sd, LocalDate ed) {
        String sql = "SELECT PeriodId FROM Period WHERE StartDate = ? AND EndDate = ?"; 

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
           
            preparedStatement.setDate(1, java.sql.Date.valueOf(sd));
            preparedStatement.setDate(2, java.sql.Date.valueOf(ed));
            
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("PeriodId");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'ID de la période : " + e.getMessage());
        }

        
        return -1;
    }

    
    public boolean isDateInVacationPeriod(LocalDate dateToCheck) {
        String sql = "SELECT IsVacation FROM Period WHERE ? BETWEEN StartDate AND EndDate";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setDate(1, java.sql.Date.valueOf(dateToCheck)); 
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean("IsVacation");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        
        return false;
    }


}
