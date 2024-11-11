package be.flas.model;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import be.flas.connection.DatabaseConnection;
import be.flas.dao.DAOPeriod;
import be.flas.dao.DAOSkier;

public class Period {
	private LocalDate startDate;
	private LocalDate endDate;
	private boolean isVacation;
	private List<Booking> bookings;
	private int id;
	
	public Period(int id,LocalDate start,LocalDate end,boolean isVacation) {
		this.endDate=end;
		this.isVacation=isVacation;
		this.startDate=start;
		this.bookings=new ArrayList<>();
		this.id=id;
	}
	public Period(LocalDate start,LocalDate end,boolean isVacation) {
		this.endDate=end;
		this.isVacation=isVacation;
		this.startDate=start;
		this.bookings=new ArrayList<>();
		
	}
    public Period(int id) {
		this.id=id;
	}
	public Period() {
		
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public boolean isVacation() {
		return isVacation;
	}

	public void setVacation(boolean isVacation) {
		this.isVacation = isVacation;
	}

	
	public List<Booking> getBookings() {
		return bookings;
	}

	public void setBookings(List<Booking> bookings) {
		this.bookings = bookings;
	}

	public void AddBooking(Booking b) {
		if(b != null && !bookings.contains(b)) {
			bookings.add(b);
		}else{
			throw new IllegalArgumentException("Invalid LessonType: cannot be null or already present.");
		}
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Override
	public String toString() {
		return "Period [startDate=" + startDate + ", endDate=" + endDate + ", isVacation=" + isVacation + "]";
	}
	
	
	public static List<Period> getAll() {
	    try {
	        // Récupération de la connexion
	        Connection connection = DatabaseConnection.getInstance().getConnection();
	        // Création de l'instance DAO pour l'enregistrement
	        DAOPeriod daoPeriod = new DAOPeriod(connection);
	        // Utilisation de 'this' pour passer l'objet courant à la méthode create
	        return daoPeriod.getAllPeriods();
	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ArrayList<>();
	    }
	}
	

}
