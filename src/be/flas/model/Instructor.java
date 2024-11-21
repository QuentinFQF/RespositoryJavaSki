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
	
	public static Instructor find(int id) {
	    try {
	        // Récupération de la connexion
	        Connection connection = DatabaseConnection.getInstance().getConnection();
	        // Création de l'instance DAO pour l'enregistrement
	        DAOInstructor daoInstructor = new DAOInstructor(connection);
	        // Utilisation de 'this' pour passer l'objet courant à la méthode create
	        return daoInstructor.find(id);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	public boolean isAvailableForDate(LocalDate date, String timeSlot) {
	    // Vérifier les réservations associées à cet instructeur
	    for (Booking booking : this.getBookings()) {
	        // Vérifier si la réservation est associée à une leçon
	        Lesson lesson = booking.getLesson();
	        if (lesson != null) {
	            // Vérification de la date et du créneau horaire
	            if (lesson.getDate() != null && lesson.getDate().equals(date) 
	                ) {
	                // L'instructeur est déjà réservé pour cette date et ce créneau
	                System.out.println("Réservation trouvée pour la date : " + lesson.getDate() + " et le créneau : " + timeSlot);
	                return true; // L'instructeur n'est pas disponible
	            }
	        }
	    }

	    // Si aucune réservation n'est trouvée pour cette date et ce créneau, l'instructeur est disponible
	    return false;
	}



	
	
	public boolean isAvailables(int periodId, String timeSlot) {
	    // Vérifier les réservations générales de l'instructeur
          for (Booking booking : bookings) {
	        
	        // Vérifier si la période de la réservation correspond
	        if (booking.getPeriod() != null && booking.getPeriod().getId() == periodId) {
	            
	        	
	            // Vérifier si la réservation est associée à une leçon et que le timeSlot correspond
	            if (booking.getLesson() != null && booking.getLesson().getDayPart() != null && booking.getLesson().getDayPart().equals(timeSlot)) {
	                
	                // Vérifier si le skieur est inscrit
	                
	                    return true; // Le skieur est trouvé dans cette période et avec le bon créneau horaire
	                
	            }
	        }
	    }
	    return false;
	}

	



	
	/*public Integer getLessonId(int selectedLessonTypeId, String timeSlot, String courseType, int minBookings, int maxBookings) {
	    // Parcours de la liste des leçons de l'instructeur
	    for (Lesson lesson : this.getLessons()) {
	        // Vérifier si la leçon correspond aux critères donnés
	        if (lesson.getLessonType().getId() == selectedLessonTypeId &&
	            lesson.getDayPart().equals(timeSlot) &&
	            lesson.getCourseType().equals(courseType) &&
	            lesson.getMinBookings() == minBookings &&
	            lesson.getMaxBookings() == maxBookings) {
	            
	            // Retourner l'ID de la leçon correspondante
	            return lesson.getId();
	        }
	    }
	    
	    // Si aucune leçon ne correspond aux critères, retourner null ou une valeur indiquant que la leçon n'a pas été trouvée
	    return -1;  // Ou vous pouvez retourner -1 si vous préférez une valeur numérique
	}*/
	public Integer getLessonIdForDate(int selectedLessonTypeId, String timeSlot, String courseType, int minBookings, int maxBookings, LocalDate selectedDate) {
	    // Parcours de la liste des leçons de l'instructeur
	    for (Lesson lesson : this.getLessons()) {
	        // Vérifier si la leçon correspond aux critères généraux
	        if (lesson.getLessonType().getId() == selectedLessonTypeId &&
	            lesson.getDayPart().equals(timeSlot) &&
	            lesson.getCourseType().equals(courseType) &&
	            lesson.getMinBookings() == minBookings &&
	            lesson.getMaxBookings() == maxBookings &&
	            lesson.getDate().equals(selectedDate)) {
	        	return lesson.getId();

	            // Vérifier les réservations de cette leçon pour la date spécifique
	            /*for (Booking booking : lesson.getBookings()) {
	            	
	                if (booking.getDateBooking().equals(selectedDate)) {
	                    // Si une réservation correspond à la date donnée, retourner l'ID de la leçon
	                    return lesson.getId();
	                }
	            }*/
	        }
	    }

	    // Si aucune leçon ne correspond aux critères, retourner -1 pour indiquer qu'aucune leçon n'a été trouvée
	    return -1;
	}

	public Integer getLessonId(int selectedLessonTypeId, String timeSlot, String courseType, int minBookings, int maxBookings, int selectedPeriodId) {
	    // Parcours de la liste des leçons de l'instructeur
	    for (Lesson lesson : this.getLessons()) {
	        // Vérifier si la leçon correspond aux critères généraux
	        if (lesson.getLessonType().getId() == selectedLessonTypeId &&
	            lesson.getDayPart().equals(timeSlot) &&
	            lesson.getCourseType().equals(courseType) &&
	            lesson.getMinBookings() == minBookings &&
	            lesson.getMaxBookings() == maxBookings) {

	            // Vérifier les réservations de cette leçon pour le PeriodId
	            for (Booking booking : lesson.getBookings()) {
	                if (booking.getPeriod().getId() == selectedPeriodId) {
	                    // Si une réservation correspond à la période donnée, retourner l'ID de la leçon
	                    return lesson.getId();
	                }
	            }
	        }
	    }

	    // Si aucune leçon ne correspond aux critères, retourner -1 pour indiquer qu'aucune leçon n'a été trouvée
	    return -1;
	}

	
	public boolean isSkierInLesson(int skierId, int lessonId) {
	    for (Lesson lesson : this.getLessons()) {
	        if (lesson.getId() == lessonId) { // Vérifier si c'est la bonne leçon
	            List<Booking> bookings = lesson.getBookings();
	            if (bookings != null) {
	                for (Booking booking : bookings) {
	                    if (booking.getSkier() != null && booking.getSkier().getPersonId() == skierId) {
	                        return true; // Le skieur est trouvé dans cette leçon
	                    }
	                }
	            }
	        }
	    }
	    return false; // Le skieur n'est pas trouvé dans cette leçon
	}
	/*public boolean isSkierInLesson(int skierId, int periodId) {
	    for (Lesson lesson : this.getLessons()) {
	        List<Booking> bookings = lesson.getBookings();
	        if (bookings != null) {
	            for (Booking booking : bookings) {
	                // Vérifier si la période de la réservation correspond
	                if (booking.getPeriod() != null && booking.getPeriod().getId() == periodId) {
	                    // Vérifier si le skieur est inscrit
	                    if (booking.getSkier() != null && booking.getSkier().getPersonId() == skierId) {
	                        return true; // Le skieur est trouvé dans cette période
	                    }
	                }
	            }
	        }
	    }
	    return false; // Le skieur n'est pas trouvé dans cette période
	}*/


	
	
	public boolean isLessonComplete(int lessonId) {
	    for (Lesson lesson : this.getLessons()) {
	    	
	        if (lesson.getId() == lessonId) {  // Si l'ID de la leçon correspond à celui donné
	            // Vérifiez si la leçon est complète
	            if (lesson.getNumberSkier() >= lesson.getMaxBookings()) {
	            	
	                return true;  // Si la leçon est complète
	            }
	        }
	    }
	    return false;  // Si aucune leçon avec l'ID donné n'est trouvée ou la leçon n'est pas complète
	}






	
	
}
