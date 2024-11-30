package be.flas.model;

import java.sql.Connection;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import be.flas.connection.DatabaseConnection;

import be.flas.dao.DAOLesson;


public class Lesson {

	private int minBookings;
	private int maxBookings;
	private Instructor instructor;
	private List<Booking> bookings;
	private LessonType lessonType;
	private int id;
	private String courseType;
	private String dayPart;
	private int tarifId;
	private int start;
	private int end;
	private int numberSkier;
	private LocalDate date;
	
	
	public int getTarifId() {
		return tarifId;
	}
	public void setTarifId(int tarifId) {
		this.tarifId = tarifId;
	}
	
	public Lesson(int min, int max, Instructor ins, LessonType lt, String day, String course, int tId, int id, int s, int e, LocalDate d) {
	    this.minBookings = min;
	    this.maxBookings = max;
	    this.bookings = new ArrayList<>();
	    this.instructor = ins;
	    this.lessonType = lt;
	    this.dayPart = day;
	    this.courseType = course;
	    this.tarifId = tId;
	    this.id = id;
	    this.start = s;
	    this.end = e;
	    this.date = d;
	}

	
	public Lesson(int min, int max, Instructor ins, LessonType lt, String day, String course, int tId, int id, int s, int e) {
	    this(min, max, ins, lt, day, course, tId, id, s, e, LocalDate.now()); 
	}

	public Lesson(int min,int max,Instructor ins,LessonType lt,String day,String course,int tId,int s,int e) {
		this.minBookings=min;
		this.maxBookings=max;
		this.bookings=new ArrayList<>();
		this.instructor=ins;
		this.lessonType=lt;
		this.dayPart=day;
		this.courseType=course;
		this.tarifId=tId;
		this.start=s;
		this.end=e;
		
	}
	public Lesson(int min,int max,Instructor ins,LessonType lt,String day,String course,int tId) {
		this.minBookings=min;
		this.maxBookings=max;
		this.bookings=new ArrayList<>();
		this.instructor=ins;
		this.lessonType=lt;
		this.dayPart=day;
		this.courseType=course;
		this.tarifId=tId;
		
	}
    public Lesson(int id) {
		this.id=id;
	}
	public Lesson() {
		this.bookings=new ArrayList<>();
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
	

	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
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
	public int getLessonId() {
		return minBookings;
	}
	public int getId() {
		return id;
	}

	

	public String getCourseType() {
		return courseType;
	}
	public void setCourseType(String courseType) {
		this.courseType = courseType;
	}
	public String getDayPart() {
		return dayPart;
	}
	public void setDayPart(String dayPart) {
		this.dayPart = dayPart;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setLessonId(int lessonId) {
		this.id=lessonId;
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
	
	

	    
    public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getEnd() {
		return end;
	}
	public void setEnd(int end) {
		this.end = end;
	}
	public int getNumberSkier() {
		return numberSkier;
	}
	public void setNumberSkier(int numberSkier) {
		this.numberSkier = numberSkier;
	}
	public int[] getMinAndMaxBooking(String lessonType, String timeSlot) {
        int minBooking;
        int maxBooking;

        if ("1 heure".equalsIgnoreCase(timeSlot) || "2 heures".equalsIgnoreCase(timeSlot)) {
            minBooking = 1;
            maxBooking = 4;
        } else if ("Enfant".equalsIgnoreCase(lessonType)) {
            minBooking = 5;
            maxBooking = 8;
        } else if ("Adulte".equalsIgnoreCase(lessonType)) {
            minBooking = 6;
            maxBooking = 10;
        } else {
            throw new IllegalArgumentException("Type de leçon ou créneau horaire non reconnu : " + lessonType + ", " + timeSlot);
        }

        return new int[]{minBooking, maxBooking};
    }
	public int[] getStartAndEndTime(String timeSlot) {
	    int startHour;
	    int endHour;

	   
	    if ("Matin".equalsIgnoreCase(timeSlot)) {
	        startHour = 9;
	        endHour = 12;
	    } else if ("Après-midi".equalsIgnoreCase(timeSlot) || "Apres-midi".equalsIgnoreCase(timeSlot)) {
	        startHour = 14;
	        endHour = 17;
	    } else {
	        throw new IllegalArgumentException("Créneau horaire non reconnu : " + timeSlot);
	    }

	   
	    return new int[]{startHour, endHour};
	}
	public int getDurationInHours(String timeSlot) {
	    if ("1 heure".equalsIgnoreCase(timeSlot)) {
	        return 1;
	    } else if ("2 heures".equalsIgnoreCase(timeSlot)) {
	        return 2;
	    } else {
	        throw new IllegalArgumentException("Créneau horaire non reconnu : " + timeSlot);
	    }
	}

	
	public boolean update() {
	    try {
	       
	        Connection connection = DatabaseConnection.getInstance().getConnection();
	        
	        DAOLesson daoLesson = new DAOLesson(connection);
	       
	        return daoLesson.update(this);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	
	
	
	
	

	
}
