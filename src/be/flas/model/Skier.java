package be.flas.model;

import java.time.LocalDate;
import java.util.List;

public class Skier extends Person{

	private boolean assurance;
	private List<Booking> bookings;
	
	public Skier(String name,String firstName,int personId,LocalDate dateOfBirth,String pseudo,boolean assurance) {
		super(name,firstName,personId,dateOfBirth,pseudo);
		this.assurance=assurance;
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
	
}
