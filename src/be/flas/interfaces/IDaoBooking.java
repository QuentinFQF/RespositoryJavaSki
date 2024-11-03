package be.flas.interfaces;

public interface IDaoBooking {

	public boolean createBooking(int lessonId, int skierId, int instructorId, int periodId);
}
