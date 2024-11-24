package be.flas.model;

import java.sql.Connection;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import be.flas.connection.DatabaseConnection;
import be.flas.dao.DAOInstructor;


public class Instructor extends Person {
	
	private List<Accreditation> accreditations;
	private List<Lesson> lessons;
	private List<Booking> bookings;
	

	public Instructor(String name,String firstName,int personId,String pseudo) {
		super(name,firstName,personId,pseudo);
		this.accreditations=new ArrayList<>();
		this.bookings=new ArrayList<>();
		this.lessons=new ArrayList<>();
		
		
	}
	
	public Instructor(String name,String firstName,int personId,LocalDate dateOfBirth,String pseudo,Accreditation acc) {
		super(name,firstName,personId,dateOfBirth,pseudo);
		this.accreditations=new ArrayList<>();
		this.bookings=new ArrayList<>();
		this.lessons=new ArrayList<>();
		AddAccreditation(acc);
		
	}
	//dans formins
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
		this.accreditations=new ArrayList<>();
		this.bookings=new ArrayList<>();
		this.lessons=new ArrayList<>();
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
	
	
	public boolean save() {
	    try {
	        
	        Connection connection = DatabaseConnection.getInstance().getConnection();
	        
	        DAOInstructor daoInstructor  = new DAOInstructor (connection);
	        
	        return daoInstructor.create(this);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	public boolean saveAccIns(int instructorId, int accreditationId) {
	    try {
	        
	        Connection connection = DatabaseConnection.getInstance().getConnection();
	        
	        DAOInstructor daoInstructor  = new DAOInstructor (connection);
	        
	        return daoInstructor.insertAcc_Instructor(instructorId,accreditationId);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	public static List<Instructor> getAllInstructors(){
		try {
	        
	        Connection connection = DatabaseConnection.getInstance().getConnection();
	        
	        DAOInstructor daoInstructor  = new DAOInstructor (connection);
	        
	        return daoInstructor.getAllInstructor();
	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ArrayList<>();
	    }
	}
	public static List<Instructor> getAllInstructorsWithAAndLT(){
		try {
	        
	        Connection connection = DatabaseConnection.getInstance().getConnection();
	        
	        DAOInstructor daoInstructor  = new DAOInstructor (connection);
	      
	        return daoInstructor.getAllInstructorsWithAccreditationsAndLessonTypes();
	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ArrayList<>();
	    }
	}
	public static List<Instructor> getAllInstructorsWithAAndLTWithId(int id){
		try {
	        
	        Connection connection = DatabaseConnection.getInstance().getConnection();
	        
	        DAOInstructor daoInstructor  = new DAOInstructor (connection);
	      
	        return daoInstructor.getInstructorsWithAccreditationsAndLessonTypesByLessonTypeId(id);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ArrayList<>();
	    }
	}
	public boolean isAvailable(int periodId, String timeSlot){
		try {
	        
	        Connection connection = DatabaseConnection.getInstance().getConnection();
	     
	        DAOInstructor daoInstructor  = new DAOInstructor (connection);
	        
	        return daoInstructor.isInstructorAvailable(this,periodId,timeSlot);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	public boolean delete() {
	    try {
	        
	        Connection connection = DatabaseConnection.getInstance().getConnection();
	        
	        DAOInstructor daoInstructor = new DAOInstructor(connection);
	        
	        return daoInstructor.delete(this);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	public static Instructor getInstructorByPseudo(String pseudo) {
	    try {
	        
	        Connection connection = DatabaseConnection.getInstance().getConnection();
	        
	        DAOInstructor daoInstructor = new DAOInstructor(connection);
	       
	        return daoInstructor.getInstructorByPseudo(pseudo);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	public boolean update() {
	    try {
	        
	        Connection connection = DatabaseConnection.getInstance().getConnection();
	        
	        DAOInstructor daoInstructor  = new DAOInstructor (connection);
	       
	        return daoInstructor.update(this);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	public static Instructor find(int id) {
	    try {
	        
	        Connection connection = DatabaseConnection.getInstance().getConnection();
	        
	        DAOInstructor daoInstructor = new DAOInstructor(connection);
	        
	        return daoInstructor.find(id);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	public boolean isAvailableForDate(LocalDate date, String timeSlot) {
	   
	    for (Booking booking : this.getBookings()) {
	        
	        Lesson lesson = booking.getLesson();
	        if (lesson != null) {
	           
	            if (lesson.getDate() != null && lesson.getDate().equals(date) 
	                ) {
	             
	                System.out.println("Réservation trouvée pour la date : " + lesson.getDate() + " et le créneau : " + timeSlot);
	                return true; 
	            }
	        }
	    }

	   
	    return false;
	}



	
	
	public boolean isAvailables(int periodId, String timeSlot) {
	    
          for (Booking booking : bookings) {
	        
	        
	        if (booking.getPeriod() != null && booking.getPeriod().getId() == periodId) {
	            
	        	
	            
	            if (booking.getLesson() != null && booking.getLesson().getDayPart() != null && booking.getLesson().getDayPart().equals(timeSlot)) {
	                
	               
	                
	                    return true; 
	                
	            }
	        }
	    }
	    return false;
	}

	



	
	
	public Integer getLessonIdForDate(int selectedLessonTypeId, String timeSlot, String courseType, int minBookings, int maxBookings, LocalDate selectedDate) {
	   
	    for (Lesson lesson : this.getLessons()) {
	        
	        if (lesson.getLessonType().getId() == selectedLessonTypeId &&
	            lesson.getDayPart().equals(timeSlot) &&
	            lesson.getCourseType().equals(courseType) &&
	            lesson.getMinBookings() == minBookings &&
	            lesson.getMaxBookings() == maxBookings &&
	            lesson.getDate().equals(selectedDate)) {
	        	return lesson.getId();

	            
	        }
	    }

	    
	    return -1;
	}

	public Integer getLessonId(int selectedLessonTypeId, String timeSlot, String courseType, int minBookings, int maxBookings, int selectedPeriodId) {
	   
	    for (Lesson lesson : this.getLessons()) {
	        
	        if (lesson.getLessonType().getId() == selectedLessonTypeId &&
	            lesson.getDayPart().equals(timeSlot) &&
	            lesson.getCourseType().equals(courseType) &&
	            lesson.getMinBookings() == minBookings &&
	            lesson.getMaxBookings() == maxBookings) {

	            
	            for (Booking booking : lesson.getBookings()) {
	                if (booking.getPeriod().getId() == selectedPeriodId) {
	            
	                    return lesson.getId();
	                }
	            }
	        }
	    }

	 
	    return -1;
	}

	
	public boolean isSkierInLesson(int skierId, int lessonId) {
	    for (Lesson lesson : this.getLessons()) {
	        if (lesson.getId() == lessonId) { 
	            List<Booking> bookings = lesson.getBookings();
	            if (bookings != null) {
	                for (Booking booking : bookings) {
	                    if (booking.getSkier() != null && booking.getSkier().getPersonId() == skierId) {
	                        return true; 
	                    }
	                }
	            }
	        }
	    }
	    return false; 
	}
	


	
	
	public boolean isLessonComplete(int lessonId) {
	    for (Lesson lesson : this.getLessons()) {
	    	
	        if (lesson.getId() == lessonId) {  
	            
	            if (lesson.getNumberSkier() >= lesson.getMaxBookings()) {
	            	
	                return true;  
	            }
	        }
	    }
	    return false; 
	}






	
	
}
