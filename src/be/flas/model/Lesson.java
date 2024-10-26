package be.flas.model;

import java.util.ArrayList;
import java.util.List;

public class Lesson {

	private int minBookings;
	private int maxBookings;
	private Instructor instructor;
	private List<Booking> bookings;
	private LessonType lessonType;
	
	public Lesson(int min,int max,Instructor ins,LessonType lt) {
		this.minBookings=min;
		this.maxBookings=max;
		this.bookings=new ArrayList<>();
		this.instructor=ins;
		this.lessonType=lt;
		
	}
	
	public int getMinBookings() {
		return minBookings;
	}

	public void setMinBookings(int minBookings) {
		this.minBookings = minBookings;
	}

	public int getMaxBookings() {
		return maxBookings;
	}

	public void setMaxBookings(int maxBookings) {
		this.maxBookings = maxBookings;
	}
	

	public Instructor getInstructor() {
		return instructor;
	}

	public void setInstructor(Instructor instructor) {
		this.instructor = instructor;
	}

	public List<Booking> getBookings() {
		return bookings;
	}

	public void setBookings(List<Booking> bookings) {
		this.bookings = bookings;
	}

	public LessonType getLessonType() {
		return lessonType;
	}

	public void setLessonType(LessonType lessonType) {
		this.lessonType = lessonType;
	}

	public double getLessonPrice() {
		return 1;
	}
	public void AddBooking(Booking b) {
		if(b != null && !bookings.contains(b)) {
			bookings.add(b);
		}else{
			throw new IllegalArgumentException("Invalid LessonType: cannot be null or already present.");
		}
	}

	@Override
	public String toString() {
		return "Lesson [minBookings=" + minBookings + ", maxBookings=" + maxBookings + "]";
	}
	
}
