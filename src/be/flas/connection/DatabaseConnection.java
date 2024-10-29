package be.flas.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;

    // Remplacez le chemin par le chemin de votre base de données Access
    private static final String DATABASE_URL = "jdbc:ucanaccess://./DB_Flas.accdb";

    // Constructeur privé
    private DatabaseConnection() {
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            connection = DriverManager.getConnection(DATABASE_URL);
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Driver introuvable: " + e.getMessage());
            System.exit(0);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erreur de connexion: " + e.getMessage());
        }
    }

    // Méthode pour obtenir l'instance de la connexion
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    // Méthode pour obtenir la connexion
    public Connection getConnection() {
        return connection;
    }

    // Méthode pour exécuter une requête
    public ResultSet executeQuery(String query) throws SQLException {
        Statement stmt = connection.createStatement();
        return stmt.executeQuery(query);
    }

    // Méthode pour fermer la connexion
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
