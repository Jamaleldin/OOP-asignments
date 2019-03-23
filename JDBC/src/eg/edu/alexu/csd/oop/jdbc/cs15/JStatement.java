package eg.edu.alexu.csd.oop.jdbc.cs15;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.LinkedList;

import org.apache.log4j.Logger;

import eg.edu.alexu.csd.oop.db.Database;

public class JStatement implements Statement {
	private JConnect connection;
	private boolean isClosed;
	private LinkedList<String> batches;
	private Database db;
	private String tableName, databaseName, path;
	private Logger log;
	private int sec;

	public JStatement(Connection connection, Database db, String path) {
		this.connection = (JConnect) connection;
		this.db = db;
		isClosed = false;
		this.path = path;
		log = JLogger.getLog();
		batches = new LinkedList<String>();
	}

	@Override
	public void close() throws SQLException {
		isClosed = true;
	}

	@Override
	public void addBatch(String sql) throws SQLException {
		if (!isClosed)
			batches.add(sql);
		throw new SQLException();
	}

	@Override
	public void clearBatch() throws SQLException {
		if (!isClosed)
			batches.clear();
		log.warn("you closed the steatment");
		throw new SQLException();
	}

	@Override
	public boolean execute(String sql) throws SQLException {
		if (!isClosed) {
			String[] s = sql.toLowerCase().split("\\s+");
			if (s[0].equals("create") || s[0].equals("drop")) {
				if (s[0].equals("create") && s[1].equals("database")) {
					databaseName = path + System.getProperty("file.separator") + db.getDatabaseName(sql);
					log.info("creat database");
					return db.executeStructureQuery("create database " + databaseName);
				} else if (s[0].equals("create") && s[1].equals("table")) {
					tableName = db.getTableName(sql);
					log.info("creat Table");
					return db.executeStructureQuery(sql);
				} else if (s[0].equals("drop") && s[1].equals("database")) {
					databaseName = path + System.getProperty("file.separator") + db.getDatabaseName(sql);
					return db.executeStructureQuery("drop database " + databaseName);
				} else if (s[0].equals("drop") && s[1].equals("table")) {
					tableName = db.getTableName(sql);
					log.info("drop Table");
					return db.executeStructureQuery(sql);
				}

				throw new SQLException();
			} else if (s[0].equals("update") || s[0].equals("insert") || s[0].equals("delete")) {
				try {
					executeUpdate(sql);
					tableName = db.getTableName(sql);
					return true;
				} catch (Exception e) {
					log.error("Error in executeUpdate");
				}
			} else if (s[0].equals("select")) {
				try {
					ResultSet result = executeQuery(sql);
					tableName = db.getTableName(sql);
					return result != null;
				} catch (Exception e) {
					log.error("Error in executeSelect");
				}
			}
		}
		throw new SQLException();
	}

	@Override
	public int[] executeBatch() throws SQLException {
		if (!isClosed) {
			int[] arr = new int[batches.size()];
			for (int i = 0; i < batches.size(); i++) {
				String[] s = batches.get(i).toLowerCase().split("\\s+");
				if (s[0].equals("create") || s[0].equals("drop")) {
					execute(batches.get(i));
					arr[i] = 0;
				} else if (s[0].equals("update") || s[0].equals("insert") || s[0].equals("delete")) {
					arr[i] = executeUpdate(batches.get(i));
				} else if (s[0].equals("select")) {
					execute(batches.get(i));
					arr[i] = 0;
				}
			}
			return arr;
		}
		throw new SQLException();
	}

	@Override
	public ResultSet executeQuery(String sql) throws SQLException {
		if (!isClosed) {
			Object[][] selected = db.executeQuery(sql);
			if (selected == null) {
				return null;
			}
			tableName = db.getTableName(sql);
			databaseName = db.getCurrentDatabaseName();
			return new JResultset(selected, this, databaseName, tableName, db, db.getSelectedColumns(sql));
		}
		throw new SQLException();
	}

	@Override
	public int executeUpdate(String sql) throws SQLException {
		if (!isClosed) {
			tableName = db.getTableName(sql);
			return db.executeUpdateQuery(sql);
		}
		log.warn("you closed the steatment");
		throw new SQLException();
	}

	@Override
	public Connection getConnection() throws SQLException {
		if (!isClosed)
			return connection;
		log.warn("you closed the steatment");
		throw new SQLException();
	}

	@Override
	public int getQueryTimeout() throws SQLException {
		if (!isClosed)
			return sec;
		throw new SQLException();
	}

	@Override
	public void setQueryTimeout(int seconds) throws SQLException {
		if (!isClosed)
			sec = seconds;
		throw new SQLException();
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void cancel() throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void clearWarnings() throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void closeOnCompletion() throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();

	}

	@Override
	public boolean execute(String arg0, int arg1) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean execute(String arg0, int[] arg1) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean execute(String arg0, String[] arg1) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public int executeUpdate(String arg0, int arg1) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public int executeUpdate(String arg0, int[] arg1) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public int executeUpdate(String arg0, String[] arg1) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public int getFetchDirection() throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public int getFetchSize() throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public ResultSet getGeneratedKeys() throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public int getMaxFieldSize() throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public int getMaxRows() throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean getMoreResults() throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean getMoreResults(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public ResultSet getResultSet() throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public int getResultSetConcurrency() throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public int getResultSetHoldability() throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public int getResultSetType() throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public int getUpdateCount() throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isCloseOnCompletion() throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isClosed() throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isPoolable() throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void setCursorName(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void setEscapeProcessing(boolean arg0) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void setFetchDirection(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void setFetchSize(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void setMaxFieldSize(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void setMaxRows(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void setPoolable(boolean arg0) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

}
