package be.flas.interfaces;

import java.util.List;

import be.flas.model.Instructor;

public interface IDaoLessonType {

	public abstract List<String> selectLessonType();
	//public List<Instructor> getInstructorsByLessonType(/*String level, */String category, String targetAudience);
}
