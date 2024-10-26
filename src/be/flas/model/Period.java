package be.flas.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Period {
	private LocalDate startDate;
	private LocalDate endDate;
	private boolean isVacation;
	private List<Booking> bookings;
	
	public Period(LocalDate start,LocalDate end,boolean isVacation) {
		this.endDate=end;
		this.isVacation=isVacation;
		this.startDate=start;
		this.bookings=new ArrayList<>();
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

	@Override
	public String toString() {
		return "Period [startDate=" + startDate + ", endDate=" + endDate + ", isVacation=" + isVacation + "]";
	}
	
	

}
