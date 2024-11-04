package be.flas.interfaces;

import java.sql.Connection;

public abstract class DaoGeneric <T> {

	protected Connection connection = null;
	public DaoGeneric(Connection conn){
	this.connection = conn;
	}
	public abstract boolean create(T obj);
	public abstract boolean delete(T obj);
	public abstract boolean update(T obj);
	public abstract T find(int id);

}
