package be.flas.model;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;

import be.flas.connection.DatabaseConnection;
import be.flas.dao.DAOBooking;
import be.flas.dao.DAOLesson;
import be.flas.dao.DAOSkier;

public class Booking {

	private LocalDate dateBooking;
	private Skier skier;
	private Period period;
	private Lesson lesson;
	private Instructor instructor;
	private int id;
	
	public Booking(LocalDate dateBooking,Skier skier,Period period, Lesson lesson,Instructor instructor) {
		this.dateBooking=dateBooking;
		this.instructor=instructor;
		this.lesson=lesson;
		this.period=period;
		this.skier=skier;

	}
	public Booking(int id,LocalDate dateBooking,Skier skier,Period period, Lesson lesson,Instructor instructor) {
		this.dateBooking=dateBooking;
		this.instructor=instructor;
		this.lesson=lesson;
		this.period=period;
		this.skier=skier;
		this.id=id;
	}
    public Booking(int id) {
		this.id=id;
	}
	public Booking() {
		
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public LocalDate getDateBooking() {
		return dateBooking;
	}
	public void setDateBooking(LocalDate dateBooking) {
		this.dateBooking = dateBooking;
	}
	public double calulatePrice() {
		return 1;
	}
	
	public Skier getSkier() {
		return skier;
	}
	public void setSkier(Skier skier) {
		this.skier = skier;
	}
	public Period getPeriod() {
		return period;
	}
	public void setPeriod(Period period) {
		this.period = period;
	}
	public Lesson getLesson() {
		return lesson;
	}
	public void setLesson(Lesson lesson) {
		this.lesson = lesson;
	}
	public Instructor getInstructor() {
		return instructor;
	}
	public void setInstructor(Instructor instructor) {
		this.instructor = instructor;
	}
	
	@Override
	public String toString() {
		return "Booking [dateBooking=" + dateBooking + "]";
	}
	
	public boolean save() {
	    try {
	        // Récupération de la connexion
	        Connection connection = DatabaseConnection.getInstance().getConnection();
	        // Création de l'instance DAO pour l'enregistrement
	        DAOBooking daoBooking = new DAOBooking(connection);
	        // Utilisation de 'this' pour passer l'objet courant à la méthode create
	        //return daoBooking.create(this);
	        return daoBooking.createWithLesson(this);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	public static List<Booking> getBookingsBySkierOrInstructorId(String skierP,String insP) {
        // Récupération de la connexion
        Connection connection = DatabaseConnection.getInstance().getConnection();
        DAOBooking daoBooking = new DAOBooking(connection);

        // Appel à la méthode DAO pour obtenir les réservations
        List<Booking> bookings = daoBooking.getBookingsBySkierOrInstructorId(skierP,insP);

        return bookings;
    }
	
	public boolean delete() {
	    try {
	        // Récupération de la connexion
	        Connection connection = DatabaseConnection.getInstance().getConnection();
	        // Création de l'instance DAO pour l'enregistrement
	        DAOBooking daoBooking = new DAOBooking(connection);
	        // Utilisation de 'this' pour passer l'objet courant à la méthode create
	        return daoBooking.delete(this);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
}
