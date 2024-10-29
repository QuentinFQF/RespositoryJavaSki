package be.flas.interfaces;

import java.util.List;

public interface IDaoAccreditation  {

	public abstract List<String> selectNames();
	public abstract int selectId(String name);
}
