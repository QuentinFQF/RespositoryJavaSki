package be.flas.interfaces;

import java.time.LocalDate;

public interface IDaoLesson {

	public boolean createLesson(int instructorId, int skierId, LocalDate startDate, LocalDate endDate, int lessonTypeId);
}
