package be.flas.interfaces;

import java.util.List;

import be.flas.model.Instructor;
import be.flas.model.LessonType;

public interface IDaoLessonType {

	public abstract List<String> selectLessonType();
	//public abstract boolean create(LessonType lt);
	public abstract int getLessonTypeIdByName(String level,double price,String sport,String Age);
	//public List<Instructor> getInstructorsByLessonType(/*String level, */String category, String targetAudience);
}
