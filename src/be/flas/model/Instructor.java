package be.flas.model;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import be.flas.connection.DatabaseConnection;
import be.flas.dao.DAOInstructor;
import be.flas.dao.DAOSkier;

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
	        // Récupération de la connexion
	        Connection connection = DatabaseConnection.getInstance().getConnection();
	        // Création de l'instance DAO pour l'enregistrement
	        DAOInstructor daoInstructor  = new DAOInstructor (connection);
	        // Utilisation de 'this' pour passer l'objet courant à la méthode create
	        //return daoInstructor.insertInstructor(this);
	        return daoInstructor.create(this);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	public boolean saveAccIns(int instructorId, int accreditationId) {
	    try {
	        // Récupération de la connexion
	        Connection connection = DatabaseConnection.getInstance().getConnection();
	        // Création de l'instance DAO pour l'enregistrement
	        DAOInstructor daoInstructor  = new DAOInstructor (connection);
	        // Utilisation de 'this' pour passer l'objet courant à la méthode create
	        return daoInstructor.insertAcc_Instructor(instructorId,accreditationId);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	public static List<Instructor> getAllInstructors(){
		try {
	        // Récupération de la connexion
	        Connection connection = DatabaseConnection.getInstance().getConnection();
	        // Création de l'instance DAO pour l'enregistrement
	        DAOInstructor daoInstructor  = new DAOInstructor (connection);
	        // Utilisation de 'this' pour passer l'objet courant à la méthode create
	        return daoInstructor.getAllInstructor();
	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ArrayList<>();
	    }
	}
	public static List<Instructor> getAllInstructorsWithAAndLT(){
		try {
	        // Récupération de la connexion
	        Connection connection = DatabaseConnection.getInstance().getConnection();
	        // Création de l'instance DAO pour l'enregistrement
	        DAOInstructor daoInstructor  = new DAOInstructor (connection);
	        // Utilisation de 'this' pour passer l'objet courant à la méthode create
	        return daoInstructor.getAllInstructorsWithAccreditationsAndLessonTypes();
	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ArrayList<>();
	    }
	}
	public static List<Instructor> getAllInstructorsWithAAndLTWithId(int id){
		try {
	        // Récupération de la connexion
	        Connection connection = DatabaseConnection.getInstance().getConnection();
	        // Création de l'instance DAO pour l'enregistrement
	        DAOInstructor daoInstructor  = new DAOInstructor (connection);
	        // Utilisation de 'this' pour passer l'objet courant à la méthode create
	        return daoInstructor.getInstructorsWithAccreditationsAndLessonTypesByLessonTypeId(id);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ArrayList<>();
	    }
	}
	public boolean isAvailable(int periodId, String timeSlot){
		try {
	        // Récupération de la connexion
	        Connection connection = DatabaseConnection.getInstance().getConnection();
	        // Création de l'instance DAO pour l'enregistrement
	        DAOInstructor daoInstructor  = new DAOInstructor (connection);
	        // Utilisation de 'this' pour passer l'objet courant à la méthode create
	        return daoInstructor.isInstructorAvailable(this,periodId,timeSlot);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	public boolean delete() {
	    try {
	        // Récupération de la connexion
	        Connection connection = DatabaseConnection.getInstance().getConnection();
	        // Création de l'instance DAO pour l'enregistrement
	        DAOInstructor daoInstructor = new DAOInstructor(connection);
	        // Utilisation de 'this' pour passer l'objet courant à la méthode create
	        return daoInstructor.delete(this);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	public static Instructor getInstructorByPseudo(String pseudo) {
	    try {
	        // Récupération de la connexion
	        Connection connection = DatabaseConnection.getInstance().getConnection();
	        // Création de l'instance DAO pour l'enregistrement
	        DAOInstructor daoInstructor = new DAOInstructor(connection);
	        // Utilisation de 'this' pour passer l'objet courant à la méthode create
	        return daoInstructor.getInstructorByPseudo(pseudo);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	public boolean update() {
	    try {
	        // Récupération de la connexion
	        Connection connection = DatabaseConnection.getInstance().getConnection();
	        // Création de l'instance DAO pour l'enregistrement
	        DAOInstructor daoInstructor  = new DAOInstructor (connection);
	        // Utilisation de 'this' pour passer l'objet courant à la méthode create
	        return daoInstructor.update(this);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	
}
