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
import be.flas.interfaces.IDaoClasse;
import be.flas.model.Instructor;
import be.flas.model.Skier;

public class DAOInstructor extends DaoGeneric<Instructor> {

    

	public DAOInstructor(Connection conn){
    	super(conn);
    }
    @Override
	public boolean delete(Instructor obj){
	    return false;
	}
    @Override
	public boolean update(Instructor obj){
	    return false;
	}
    @Override
	public Instructor find(int id){
    	Instructor s = new Instructor();
		return s;
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

    @Override
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
    
    public boolean isInstructorAvailable(int instructorId, int periodId, String timeSlot) {
        String sql = "SELECT Lesson.LessonId " +
                     "FROM Lesson " +
                     "JOIN Booking ON Lesson.LessonId = Booking.LessonId " +
                     "JOIN Period ON Booking.PeriodId = Period.PeriodId " +
                     "WHERE Lesson.InstructorId = ? " +
                     "AND Period.PeriodId = ? " +
                     "AND Lesson.DayPart = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, instructorId);
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
    }

    

}
