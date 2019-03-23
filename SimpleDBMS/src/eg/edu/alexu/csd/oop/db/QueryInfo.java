package eg.edu.alexu.csd.oop.db;

public interface QueryInfo {
	public void setQuery(String query);

	public String getQuery();

	public void setDataBaseName();

	public String getDataBaseName();

	public void setTableName();

	public String getTableName();

	public void setColumnName();

	public String[] getColumnName();

	public void setValue();

	public String[] getValue();

	public void setDataTypes();

	public String[] getDataTypes();

	public void setCondition();

	public String getCondition();
}
