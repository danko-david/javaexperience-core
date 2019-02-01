package eu.javaexperience.database.pojodb;

import java.sql.Connection;
import java.sql.SQLException;


public interface Database
{
	public Connection getConnection() throws SQLException;
	public <T extends Model> T getInstanceById(Class<T> cls, Object id) throws SQLException, InstantiationException, IllegalAccessException;
	public void insert(Model m) throws SQLException;
	public void updateById(Model m) throws SQLException;
	public void delete(Model m) throws SQLException;
	
	//queryBuilding L.eq.is()
	//bulk model loading
	
	
}
