package be.flas.model;

import java.time.LocalDate;

public class Booking {

	private LocalDate dateBooking;
	private Skier skier;
	private Period period;
	private Lesson lesson;
	private Instructor instructor;
	
	public Booking(LocalDate dateBooking,Skier skier,Period period, Lesson lesson,Instructor instructor) {
		this.dateBooking=dateBooking;
		this.instructor=instructor;
		this.lesson=lesson;
		this.period=period;
		this.skier=skier;
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
	
}
