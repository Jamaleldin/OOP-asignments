package eg.edu.alexu.csd.oop.jdbc.cs15;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import eg.edu.alexu.csd.oop.db.Database;

public class JResultSetMetaData implements ResultSetMetaData {
	private String tableName;
	private Object[][] selected;
	private String[] types;
	private String selectedColumns;
	private String[] columns;
	private Logger log;

	public JResultSetMetaData(Database db, String databaseName, String tableName, Object[][] selected,
			String selectedColumns) {
		this.tableName = tableName;
		this.selected = selected;
		types = db.getColumnsTypes(databaseName, tableName);
		this.selectedColumns = selectedColumns;
		columns = db.getColumnsName(databaseName, tableName);
		log = JLogger.getLog();
	}

	@Override
	public int getColumnCount() throws SQLException {
		log.info("getColumnCount");
		return selected[0].length;
	}

	@Override
	public String getColumnLabel(int arg0) throws SQLException {
		System.out.println(arg0);
		if (selectedColumns.equals("all")) {
			if (arg0 > 0 && arg0 <= columns.length)
				return columns[arg0 - 1];
		} else {
			if (arg0 == 1)
				return selectedColumns;
		}
		throw new SQLException();
	}

	@Override
	public String getColumnName(int arg0) throws SQLException {
		return getColumnLabel(arg0);
	}

	@Override
	public int getColumnType(int arg0) throws SQLException {
		if (selectedColumns.equals("all")) {
			if (arg0 > 0 && arg0 <= types.length) {
				if (types[arg0 - 1].equals("int")) {
					return 0;
				} else {
					return 1;
				}
			}
		} else {
			if (arg0 == 1) {
				for (int i = 0; i < columns.length; i++) {
					if (columns[i].equals(selectedColumns)) {
						if (types[i].equals("int")) {
							return 0;
						} else {
							return 1;
						}
					}
				}
			}
		}
		throw new SQLException();
	}

	@Override
	public String getColumnTypeName(int arg0) throws SQLException {
		int type = getColumnType(arg0);
		if (type == 0)
			return "int";
		else
			return "varchar";
	}

	@Override
	public String getTableName(int arg0) throws SQLException {
		if (selectedColumns.equals("all")) {
			if (arg0 > 0 && arg0 <= columns.length)
				return tableName;
			else
				return "";
		} else if (arg0 == 1)
			return tableName;
		return "";
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
	public String getCatalogName(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public String getColumnClassName(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public int getColumnDisplaySize(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public int getPrecision(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public int getScale(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public String getSchemaName(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isAutoIncrement(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isCaseSensitive(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isCurrency(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isDefinitelyWritable(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public int isNullable(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isReadOnly(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isSearchable(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isSigned(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isWritable(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

}
