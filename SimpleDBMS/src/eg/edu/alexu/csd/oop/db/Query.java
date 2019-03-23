package eg.edu.alexu.csd.oop.db;

import java.io.IOException;
import java.sql.SQLException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

public interface Query {
	public boolean createDataBase(String databaseName);

	public boolean createTable(String databaseName, String tableName, String[] columns, String[] dataTypes)
			throws SQLException;

	public void insertIntoTable(String databaseName, String tableName, String[] columns, String[] vlaues)
			throws ParserConfigurationException, SAXException, IOException, TransformerException, SQLException;

	public int deleteFromTable(String databaseName, String tableName, String condition) throws SQLException;

	public boolean dropDataBase(String databaseName);

	public boolean dropTable(String databaseName, String tableName);

	public String[][] selectFromTable(String dataBaseName, String tableName, String column, String condition)
			throws SQLException;

	public int updateTable(String dataBaseName, String tableName, String[] columns, String[] vlaues, String condition)
			throws ParserConfigurationException, SAXException, IOException, TransformerException, SQLException;

}
