package be.flas.model;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;

import be.flas.connection.DatabaseConnection;
import be.flas.dao.DAOSkier;

public class Skier extends Person{

	private boolean assurance;
	private List<Booking> bookings;

	
	public Skier(String name,String firstName,int personId,LocalDate dateOfBirth,String pseudo,boolean assurance) {
		super(name,firstName,personId,dateOfBirth,pseudo);
		this.assurance=assurance;
	}
	public Skier(String name,String firstName,LocalDate dateOfBirth,String pseudo,boolean assurance) {
		super(name,firstName,dateOfBirth,pseudo);
		this.assurance=assurance;
	}
	public Skier(String name,String firstName,String pseudo) {
		super(name,firstName,pseudo);
		
	}
	public Skier(int id) {
		super(id);
		
	}
	public Skier() {
		super();
	}
	public void AddBooking(Booking b) {
		if(b != null && !bookings.contains(b)) {
			bookings.add(b);
		}else{
			throw new IllegalArgumentException("Invalid LessonType: cannot be null or already present.");
		}
	}

	public boolean isAssurance() {
		return assurance;
	}

	public void setAssurance(boolean assurance) {
		this.assurance = assurance;
	}

	@Override
	public String toString() {
		return "Skier [assurance=" + assurance + "]"+super.toString();
	}

	public List<Booking> getBookings() {
		return bookings;
	}

	public void setBookings(List<Booking> bookings) {
		this.bookings = bookings;
	}
	
	
	// Nouvelle méthode statique pour sauvegarder un skieur
    public static boolean save(Skier skier) {
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            DAOSkier daoSkier = new DAOSkier(connection);
            return daoSkier.create(skier);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
	
}
