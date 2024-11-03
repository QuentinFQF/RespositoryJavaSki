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

import be.flas.interfaces.IDaoPeriod;
import be.flas.model.Instructor;
import be.flas.model.Period;

public class DAOPeriod implements IDaoPeriod{

	private Connection connection;

    public DAOPeriod(Connection connection) {
        this.connection = connection;
    }

    /*public List<Period> getAllPeriods() {
        List<Period> periods = new ArrayList<>();
        String sql = "SELECT PeriodId, StartDate, EndDate, IsVacation FROM Period";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                int periodId = resultSet.getInt("PeriodId");
                LocalDate startDate = resultSet.getDate("StartDate").toLocalDate();
                LocalDate endDate = resultSet.getDate("EndDate").toLocalDate();
                boolean isVacation = resultSet.getBoolean("IsVacation");

                periods.add(new Period(periodId, startDate, endDate, isVacation));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des périodes : " + e.getMessage());
        }

        return periods;
    }*/
    @Override
    public List<Period> getAllPeriods() {
        List<Period> periods = new ArrayList<>();
        String sql = "SELECT StartDate, EndDate, IsVacation FROM Period";

        // Format de la date à ajuster selon le format de votre base de données
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                LocalDate startDate = null;
                LocalDate endDate = null;
                boolean isVacation = resultSet.getBoolean("IsVacation");

                // Parsing des dates
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

                // Création de l'objet Period
                Period period = new Period(startDate, endDate, isVacation);
                periods.add(period);
            }
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la récupération des périodes : " + ex.getMessage());
        }
        return periods;
    }
    @Override
    public int getPeriodIdBy(LocalDate sd, LocalDate ed) {
        String sql = "SELECT PeriodId FROM Period WHERE StartDate = ? AND EndDate = ?"; // Assurez-vous que les noms des colonnes sont corrects

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            // Convertir LocalDate en java.sql.Date pour l'utilisation dans la base de données
            preparedStatement.setDate(1, java.sql.Date.valueOf(sd));
            preparedStatement.setDate(2, java.sql.Date.valueOf(ed));
            
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("PeriodId");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'ID de la période : " + e.getMessage());
        }

        // Retourne -1 si aucune période correspondante n'est trouvée ou en cas d'erreur
        return -1;
    }


}
