package be.flas.model;

import java.sql.Connection;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import be.flas.connection.DatabaseConnection;

import be.flas.dao.DAOLessonType;


public class LessonType {

	private String level;
	private String sport;
	private double price;
	private List<Accreditation> accreditations;
	private Accreditation accreditation;
	private List<Lesson> lessons;
	private int id;
	private String ageCategory;
	
	public LessonType(int id,String level,String sport,double price,String age) {
		this.level=level;
		this.price=price;
		this.sport=sport;
		this.ageCategory=age;
		this.id=id;
		this.accreditations=new ArrayList<>();
		this.lessons=new ArrayList<>();
	}
	//utiliser dasn getAll
	public LessonType(int id,String level,double price,Accreditation a) {
		this.level=level;
		this.price=price;
		this.id=id;
		this.accreditation=a;
		this.lessons=new ArrayList<>();
	}
	public LessonType(int id,String level,String sport,double price,String age,Accreditation a) {
		this.level=level;
		this.price=price;
		this.sport=sport;
		this.ageCategory=age;
		this.id=id;
		this.accreditation=a;
		this.lessons=new ArrayList<>();
	}
	
	public LessonType(String level,String sport,double price) {
		this.level=level;
		this.price=price;
		this.sport=sport;
		this.accreditations=new ArrayList<>();
		this.lessons=new ArrayList<>();
	}
    public LessonType(int id,String level,double price) {
		this.id=id;
		this.level=level;
		this.price=price;
		this.accreditations=new ArrayList<>();
		this.lessons=new ArrayList<>();
	}
    public LessonType(int id) {
		this.id=id;
	}
	public LessonType() {
		
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
	
	public Accreditation getAccreditation() {
		return accreditation;
	}
	public void setAccreditation(Accreditation accreditation) {
		this.accreditation = accreditation;
	}
	public String getAgeCategory() {
		return ageCategory;
	}

	public void setAgeCategory(String ageCategori) {
		this.ageCategory = ageCategori;
	}

	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getSport() {
		return sport;
	}
	public void setSport(String sport) {
		this.sport = sport;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	@Override
	public String toString() {
		return "LessonType [level=" + level + ", sport=" + sport + ", price=" + price + "]";
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
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	
	
	public static List<LessonType> getAll() {
	    try {
	        
	        Connection connection = DatabaseConnection.getInstance().getConnection();
	        
	        DAOLessonType daoLessonType = new DAOLessonType(connection);
	      
	        return daoLessonType.selectLessonTypes();
	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ArrayList<>();
	    }
	}
	
	public static LessonType getLesson(int id){
		try {
	        
	        Connection connection = DatabaseConnection.getInstance().getConnection();
	       
	        DAOLessonType daoLessonType  = new DAOLessonType (connection);
	       
	        return daoLessonType.find(id);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	@Override
	public boolean equals(Object obj) {
	    if (this == obj) return true;
	    if (obj == null || getClass() != obj.getClass()) return false;
	    LessonType that = (LessonType) obj;
	    return id == that.id &&
	           Objects.equals(level, that.level) &&
	           Objects.equals(price, that.price);
	}

	@Override
	public int hashCode() {
	    return Objects.hash(id, level, price);
	}

	
}
