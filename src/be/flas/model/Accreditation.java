package be.flas.model;

import java.util.ArrayList;
import java.util.List;

public class Accreditation {

	private String name;
	private List<Instructor> instructors;
	private List<LessonType> lessonTypes;
	
	public Accreditation(String name,LessonType lt) {
		this.name=name;
		this.instructors=new ArrayList<>();
		this.lessonTypes=new ArrayList<>();
		AddLessonType(lt);
		
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
	
}
