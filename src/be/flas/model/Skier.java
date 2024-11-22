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
	        // Récupération de la connexion
	        Connection connection = DatabaseConnection.getInstance().getConnection();
	        // Création de l'instance DAO pour l'enregistrement
	        DAOSkier daoSkier = new DAOSkier(connection);
	        // Utilisation de 'this' pour passer l'objet courant à la méthode create
	        return daoSkier.create(this);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	public static List<Skier> getAll() {
	    try {
	        // Récupération de la connexion
	        Connection connection = DatabaseConnection.getInstance().getConnection();
	        // Création de l'instance DAO pour l'enregistrement
	        DAOSkier daoSkier = new DAOSkier(connection);
	        // Utilisation de 'this' pour passer l'objet courant à la méthode create
	        return daoSkier.getAllSkiers();
	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ArrayList<>();
	    }
	}
	public static Skier getSkierByPseudo(String pseudo) {
	    try {
	        // Récupération de la connexion
	        Connection connection = DatabaseConnection.getInstance().getConnection();
	        // Création de l'instance DAO pour l'enregistrement
	        DAOSkier daoSkier = new DAOSkier(connection);
	        // Utilisation de 'this' pour passer l'objet courant à la méthode create
	        return daoSkier.getSkierByPseudo(pseudo);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	public boolean delete() {
	    try {
	        // Récupération de la connexion
	        Connection connection = DatabaseConnection.getInstance().getConnection();
	        // Création de l'instance DAO pour l'enregistrement
	        DAOSkier daoSkier = new DAOSkier(connection);
	        // Utilisation de 'this' pour passer l'objet courant à la méthode create
	        return daoSkier.delete(this);
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
	        DAOSkier daoSkier = new DAOSkier(connection);
	        // Utilisation de 'this' pour passer l'objet courant à la méthode create
	        return daoSkier.update(this);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}

	public static Skier find(int id) {
	    try {
	        // Récupération de la connexion
	        Connection connection = DatabaseConnection.getInstance().getConnection();
	        // Création de l'instance DAO pour l'enregistrement
	        DAOSkier daoSkier = new DAOSkier(connection);
	        // Utilisation de 'this' pour passer l'objet courant à la méthode create
	        return daoSkier.find(id);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	
	/*public boolean isSkierInLesson(int skierId, int periodId, LocalDate dateParticulier) {
	    // Parcourir toutes les réservations
	    for (Booking booking : this.getBookings()) {
	        // Vérifier que la réservation a un skieur et une leçon
	        if (booking.getLesson() != null && booking.getSkier() != null) {
	            // Vérifier si le skieur correspond à l'ID recherché
	            if (booking.getSkier().getPersonId() == skierId) {
	                
	                // Si un paramètre dateParticulier est fourni
	                if (dateParticulier != null) {
	                    // Comparer la date particulière avec la date du cours particulier
	                    if (booking.getLesson().getDate() != null && 
	                        booking.getLesson().getDate().equals(dateParticulier)) {
	                        return true; // Le skieur est inscrit dans ce cours particulier à la date donnée
	                    }
	                } else {
	                    // Si aucun paramètre dateParticulier n'est fourni, vérifier la période associée
	                    if (booking.getPeriod() != null && booking.getPeriod().getId() == periodId) {
	                        return true; // Le skieur est inscrit dans un cours avec la période donnée
	                    }
	                }
	            }
	        }
	    }
	    return false; // Le skieur n'est pas inscrit dans cette période ou date particulière
	}*/
	public boolean isSkierInLesson(int skierId, int periodId, String timeSlot) {
	    // Parcourir les réservations
	    for (Booking booking : bookings) {
	        
	        // Vérifier si la période de la réservation correspond
	        if (booking.getPeriod() != null && booking.getPeriod().getId() == periodId) {
	            
	        	
	            // Vérifier si la réservation est associée à une leçon et que le timeSlot correspond
	            if (booking.getLesson() != null && booking.getLesson().getDayPart() != null && booking.getLesson().getDayPart().equals(timeSlot)) {
	                
	                // Vérifier si le skieur est inscrit
	                if (this.getPersonId() == skierId) {
	                    return true; // Le skieur est trouvé dans cette période et avec le bon créneau horaire
	                }
	            }
	        }
	    }
	    return false; // Le skieur n'est pas trouvé dans cette période et créneau horaire
	}
	
	public boolean isSkierInLesson(int skierId, LocalDate date) {
	    // Parcourir les réservations
	    for (Booking booking : bookings) {

	        // Vérifier si la réservation est associée à une leçon
	        if (booking.getLesson() != null) {

	            // Vérifier si c'est un cours particulier (en fonction de la présence de la date particulière)
	            if (booking.getLesson().getDate() != null && booking.getLesson().getDate().equals(date)) {

	                // Vérifier si le skieur est inscrit à cette réservation
	                if (this.getPersonId() == skierId) {
	                    return true; // Le skieur est inscrit à un cours particulier pour cette date
	                }
	            }
	        }
	    }
	    return false; // Le skieur n'est pas inscrit à un cours particulier pour cette date
	}
	
	public boolean hasMorningAndAfternoonBookings(Period period,String timeSlot) {
	    boolean hasMorning = false;
	    boolean hasAfternoon = false;

	    System.out.println("avant b : ");
	    for (Booking booking : this.getBookings()) {
	        // Vérifier si la réservation appartient à la période donnée
	    	System.out.println("apres b : ");
	        if (booking.getPeriod().getId() == period.getId()) {
	            
	            if (!booking.getLesson().getDayPart().equalsIgnoreCase(timeSlot)) {
	                return true;
	            }

	        }
	    }

	    return false;
	}







	
}
