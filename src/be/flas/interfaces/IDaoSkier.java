package be.flas.interfaces;

import java.util.List;

import be.flas.model.Skier;

public interface IDaoSkier <T>{

	public abstract boolean create(T obj);
	public List<Skier> getAllSkiers();
	public int getSkierIdByName(String name);
}
