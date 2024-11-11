package be.flas.model;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import be.flas.connection.DatabaseConnection;
import be.flas.dao.DAOAccreditation;
import be.flas.dao.DAOInstructor;

public class Accreditation {

	private String name;
	private List<Instructor> instructors;
	private List<LessonType> lessonTypes;
	private String sport;
	private String ageCategory;
	private int id;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Accreditation(int id,String name,LessonType lt,String sport,String ageCategory) {
		this.name=name;
		this.ageCategory=ageCategory;
		this.sport=sport;
		this.id=id;
		this.instructors=new ArrayList<>();
		this.lessonTypes=new ArrayList<>();
		if (lt != null) {
	        this.AddLessonType(lt);
	    }
		
		
	}
	public Accreditation(String name,LessonType lt) {
		this.name=name;
		this.instructors=new ArrayList<>();
		this.lessonTypes=new ArrayList<>();
		AddLessonType(lt);
		
	}
	public Accreditation(String name) {
		this.name=name;
		this.instructors=new ArrayList<>();
		//this.lessonTypes=new ArrayList<>();
		//AddLessonType(lt);
		
	}
	public Accreditation() {
		
	}
	public List<Instructor> getInstructors() {
		return instructors;
	}
	public void setInstructors(List<Instructor> instructors) {
		this.instructors = instructors;
	}
	public List<LessonType> getLessonTypes() {
		return lessonTypes;
	}
	public void setLessonTypes(List<LessonType> lessonTypes) {
		this.lessonTypes = lessonTypes;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void AddLessonType(LessonType lt) {
		if(lt != null && !lessonTypes.contains(lt)) {
			lessonTypes.add(lt);
		}else{
			throw new IllegalArgumentException("Invalid LessonType: cannot be null or already present.");
		}
	}
	public void AddInstructor(Instructor i) {
		if(i != null && !lessonTypes.contains(i)) {
			instructors.add(i);
		}else{
			throw new IllegalArgumentException("Invalid LessonType: cannot be null or already present.");
		}
	}
	@Override
	public String toString() {
		return "Accreditation [name=" + name + "]";
	}
	
	
	
	public String getSport() {
		return sport;
	}
	public void setSport(String sport) {
		this.sport = sport;
	}
	public String getAgeCategory() {
		return ageCategory;
	}
	public void setAgeCategory(String ageCategory) {
		this.ageCategory = ageCategory;
	}
	public static int selectId(String names) {
	    try {
	        // Récupération de la connexion
	        Connection connection = DatabaseConnection.getInstance().getConnection();
	        // Création de l'instance DAO pour l'enregistrement
	        DAOAccreditation daoAccreditation = new DAOAccreditation(connection);
	        // Utilisation de 'this' pour passer l'objet courant à la méthode create
	        return daoAccreditation.selectId(names);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return -1;
	    }
	}
	
	public static List<Accreditation> selectAccDiffIns(int id) {
	    try {
	        // Récupération de la connexion
	        Connection connection = DatabaseConnection.getInstance().getConnection();
	        // Création de l'instance DAO pour l'enregistrement
	        DAOAccreditation daoAccreditation = new DAOAccreditation(connection);
	        // Utilisation de 'this' pour passer l'objet courant à la méthode create
	        return daoAccreditation.selectAccreditationsNotIns(id);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ArrayList<>();
	    }
	}
	
	
}
