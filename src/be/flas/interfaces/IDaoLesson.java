package be.flas.interfaces;

import java.time.LocalDate;

public interface IDaoLesson {

	//public boolean createLesson(int instructorId, int skierId, LocalDate startDate, LocalDate endDate, int lessonTypeId);
	//public boolean checkAndUpdateOrCreateLesson(int instructorId, int skierId, int lessonTypeId, LocalDate startDate, LocalDate endDate);
	public int createLesson(int instructorId, int lessonTypeId, String lessonType,String DayPart) ;
	public int getLessonId(int instructorId, int lessonTypeId, LocalDate startDate, LocalDate endDate);
	//public boolean createLesson(int instructorId, int lessonTypeId, int minBookings, int maxBookings);
}
