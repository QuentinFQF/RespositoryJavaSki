package be.flas.model;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import be.flas.connection.DatabaseConnection;
import be.flas.dao.DAOSkier;

public class Skier extends Person{

	private boolean assurance;
	private List<Booking> bookings;

	//utiliser dans formSkier
	public Skier(String name,String firstName,LocalDate dateOfBirth,String pseudo) {
		super(name,firstName,pseudo,dateOfBirth);
		this.bookings=new ArrayList<>();
		
	}
	public Skier(String name,String firstName,int personId,LocalDate dateOfBirth,String pseudo,boolean assurance) {
		super(name,firstName,personId,dateOfBirth,pseudo);
		this.assurance=assurance;
	}
	public Skier(String name,String firstName,LocalDate dateOfBirth,String pseudo,boolean assurance) {
		super(name,firstName,dateOfBirth,pseudo);
		this.assurance=assurance;
		this.bookings=new ArrayList<>();
	}
	public Skier(String name,String firstName,String pseudo) {
		super(name,firstName,pseudo);
		this.bookings=new ArrayList<>();
		
	}
	public Skier(int id,String name,String firstName,String pseudo) {
		super(name,firstName,id,pseudo);
		this.bookings=new ArrayList<>();
		
	}
	public Skier(int id) {
		super(id);
		
	}
	public Skier() {
		super();
		this.bookings=new ArrayList<>();
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
	
	
	public boolean save() {
	    try {
	        
	        Connection connection = DatabaseConnection.getInstance().getConnection();
	       
	        DAOSkier daoSkier = new DAOSkier(connection);
	    
	        return daoSkier.create(this);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	public static List<Skier> getAll() {
	    try {
	        
	        Connection connection = DatabaseConnection.getInstance().getConnection();
	      
	        DAOSkier daoSkier = new DAOSkier(connection);
	     
	        return daoSkier.getAllSkiers();
	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ArrayList<>();
	    }
	}
	public static Skier getSkierByPseudo(String pseudo) {
	    try {
	      
	        Connection connection = DatabaseConnection.getInstance().getConnection();
	        
	        DAOSkier daoSkier = new DAOSkier(connection);
	  
	        return daoSkier.getSkierByPseudo(pseudo);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	public boolean delete() {
	    try {
	        
	        Connection connection = DatabaseConnection.getInstance().getConnection();
	      
	        DAOSkier daoSkier = new DAOSkier(connection);
	
	        return daoSkier.delete(this);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	public boolean update() {
	    try {
	        
	        Connection connection = DatabaseConnection.getInstance().getConnection();
	      
	        DAOSkier daoSkier = new DAOSkier(connection);
	  
	        return daoSkier.update(this);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}

	public static Skier find(int id) {
	    try {
	       
	        Connection connection = DatabaseConnection.getInstance().getConnection();
	       
	        DAOSkier daoSkier = new DAOSkier(connection);

	        return daoSkier.find(id);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	
	
	public boolean isSkierInLesson(int skierId, int periodId, String timeSlot) {
	   
	    for (Booking booking : bookings) {
	        
	        
	        if (booking.getPeriod() != null && booking.getPeriod().getId() == periodId) {
	            
	        	
	         
	            if (booking.getLesson() != null && booking.getLesson().getDayPart() != null && booking.getLesson().getDayPart().equals(timeSlot)) {
	                
	                
	                if (this.getPersonId() == skierId) {
	                    return true; 
	                }
	            }
	        }
	    }
	    return false; 
	}
	
	public boolean isSkierInLesson(int skierId, LocalDate date) {
	   
	    for (Booking booking : bookings) {

	     
	        if (booking.getLesson() != null) {

	       
	            if (booking.getLesson().getDate() != null && booking.getLesson().getDate().equals(date)) {

	                
	                if (this.getPersonId() == skierId) {
	                    return true;
	                }
	            }
	        }
	    }
	    return false;
	}
	
	public boolean hasMorningAndAfternoonBookings(Period period,String timeSlot) {
	    

	   
	    for (Booking booking : this.getBookings()) {
	    
	    	
	        if (booking.getPeriod().getId() == period.getId()) {
	            
	            if (!booking.getLesson().getDayPart().equalsIgnoreCase(timeSlot)) {
	                return true;
	            }

	        }
	    }

	    return false;
	}
	
	public static boolean isPseudoExists(String pseudo) {
	    try {
	        
	        Connection connection = DatabaseConnection.getInstance().getConnection();
	      
	        DAOSkier daoSkier = new DAOSkier(connection);
	
	        return daoSkier.isPseudoExists(pseudo);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}







	
}
