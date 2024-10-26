package be.flas.model;

import java.util.ArrayList;
import java.util.List;

public class LessonType {

	private String level;
	private String sport;
	private double price;
	private List<Accreditation> accreditations;
	private List<Lesson> lessons;
	public LessonType(String level,String sport,double price) {
		this.level=level;
		this.price=price;
		this.sport=sport;
		this.accreditations=new ArrayList<>();
		this.lessons=new ArrayList<>();
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
	
}
