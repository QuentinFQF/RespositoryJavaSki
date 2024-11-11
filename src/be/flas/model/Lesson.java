package be.flas.model;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import be.flas.connection.DatabaseConnection;
import be.flas.dao.DAOInstructor;
import be.flas.dao.DAOLesson;
import be.flas.dao.DAOSkier;

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
	
	
	public int getTarifId() {
		return tarifId;
	}
	public void setTarifId(int tarifId) {
		this.tarifId = tarifId;
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
	
	public static Lesson getLesson(int id){
		try {
	        // Récupération de la connexion
	        Connection connection = DatabaseConnection.getInstance().getConnection();
	        // Création de l'instance DAO pour l'enregistrement
	        DAOLesson daoLesson  = new DAOLesson (connection);
	        // Utilisation de 'this' pour passer l'objet courant à la méthode create
	        return daoLesson.find(id);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	public static int getLessonId(int instructorId, int lessonTypeId, String dayPart, String courseType, int minBookings, int maxBookings){
		try {
	        // Récupération de la connexion
	        Connection connection = DatabaseConnection.getInstance().getConnection();
	        // Création de l'instance DAO pour l'enregistrement
	        DAOLesson daoLesson  = new DAOLesson (connection);
	        // Utilisation de 'this' pour passer l'objet courant à la méthode create
	        return daoLesson.getLessonId(instructorId,lessonTypeId,dayPart,courseType,minBookings,maxBookings);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return -1;
	    }
	}
	public static boolean isComplete(int id){
		try {
	        // Récupération de la connexion
	        Connection connection = DatabaseConnection.getInstance().getConnection();
	        // Création de l'instance DAO pour l'enregistrement
	        DAOLesson daoLesson  = new DAOLesson (connection);
	        // Utilisation de 'this' pour passer l'objet courant à la méthode create
	        return daoLesson.isLessonComplete(id);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	public boolean save() {
	    try {
	        // Récupération de la connexion
	        Connection connection = DatabaseConnection.getInstance().getConnection();
	        // Création de l'instance DAO pour l'enregistrement
	        DAOLesson daoLesson = new DAOLesson(connection);
	        // Utilisation de 'this' pour passer l'objet courant à la méthode create
	        return daoLesson.create(this);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	public boolean update() {
	    try {
	        // Récupération de la connexion
	        Connection connection = DatabaseConnection.getInstance().getConnection();
	        // Création de l'instance DAO pour l'enregistrement
	        DAOLesson daoLesson = new DAOLesson(connection);
	        // Utilisation de 'this' pour passer l'objet courant à la méthode create
	        return daoLesson.update(this);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	

	
}
