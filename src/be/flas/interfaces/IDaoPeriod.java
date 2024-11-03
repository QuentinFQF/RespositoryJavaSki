package be.flas.interfaces;

import java.time.LocalDate;
import java.util.List;

import be.flas.model.Instructor;
import be.flas.model.Period;

public interface IDaoPeriod {

	public List<Period> getAllPeriods();
	public int getPeriodIdBy(LocalDate sd,LocalDate ed);
}
