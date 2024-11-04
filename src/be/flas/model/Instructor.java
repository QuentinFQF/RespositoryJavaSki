package be.flas.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Instructor extends Person {
	
	private List<Accreditation> accreditations;
	private List<Lesson> lessons;
	private List<Booking> bookings;
	

	public Instructor(String name,String firstName,int personId,LocalDate dateOfBirth,String pseudo,Accreditation acc) {
		super(name,firstName,personId,dateOfBirth,pseudo);
		this.accreditations=new ArrayList<>();
		this.bookings=new ArrayList<>();
		this.lessons=new ArrayList<>();
		AddAccreditation(acc);
		
	}
	public Instructor(String name,String firstName,LocalDate dateOfBirth,Accreditation acc,String pseudo) {
		super(name,firstName,dateOfBirth,pseudo);
		this.accreditations=new ArrayList<>();
		this.bookings=new ArrayList<>();
		this.lessons=new ArrayList<>();
		AddAccreditation(acc);
		//AddLesson(ls);
	}
	public Instructor(String name,String firstName,LocalDate dateOfBirth,String pseudo) {
		super(name,firstName,dateOfBirth,pseudo);
		this.accreditations=new ArrayList<>();
		this.bookings=new ArrayList<>();
		this.lessons=new ArrayList<>();
		//AddAccreditation(acc);
		//AddLesson(ls);
	}
	public Instructor(int id) {
		super(id);
		
	}
	public Instructor() {
		super();
	}
	public boolean IsAccreditate() {
		return false;
	}
	
	public List<Accreditation> getAccreditations() {
		return accreditations;
	}
	public void setAccreditations(List<Accreditation> accreditations) {
		this.accreditations = accreditations;
	}
	public List<Lesson> getLessons() {
		return lessons;
	}
	public void setLessons(List<Lesson> lessons) {
		this.lessons = lessons;
	}
	public List<Booking> getBookings() {
		return bookings;
	}
	public void setBookings(List<Booking> bookings) {
		this.bookings = bookings;
	}
	
	public void AddLesson(Lesson l) {
		if(l != null && !lessons.contains(l)) {
			lessons.add(l);
		}else{
			throw new IllegalArgumentException("Invalid LessonType: cannot be null or already present.");
		}
	}
	public void AddAccreditation(Accreditation a) {
		if(a != null && !accreditations.contains(a)) {
			accreditations.add(a);
		}else{
			throw new IllegalArgumentException("Invalid LessonType: cannot be null or already present.");
		}
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
		return "Instructor []"+super.toString();
	}
	
	
}
