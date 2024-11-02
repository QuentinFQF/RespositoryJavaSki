package be.flas.interfaces;


import java.util.List;


import be.flas.model.Instructor;

public interface IDaoClasse<T> {

	
	public abstract boolean create(T obj);
	public abstract int getInstructorIdByName(String name);
	public List<Instructor> getAllInstructor();
	public List<Instructor> getInstructorsByLessonType(/*String level, */String category, String targetAudience);
	//public abstract boolean insertAcc_Instructor(int instructorId, int accreditationId);
	
}
