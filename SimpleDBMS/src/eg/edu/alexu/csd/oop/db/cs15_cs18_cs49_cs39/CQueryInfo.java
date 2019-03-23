package eg.edu.alexu.csd.oop.db.cs15_cs18_cs49_cs39;

import eg.edu.alexu.csd.oop.db.QueryInfo;

public class CQueryInfo implements QueryInfo{
	private String query;
	private String dataBaseName;
	private String TableName;
	private String[] columnsName;
	private String[] values;
	private String[] dataTypes;
	private String condition;
	private CheckSyntax check;

	public CQueryInfo(String query) {
		this.query = query;
		check = new CheckSyntax();
	}

	@Override
	public void setQuery(String query) {
		this.query = query;
	}

	@Override
	public String getQuery() {
		return query;
	}

	@Override
	public void setDataBaseName() {
		dataBaseName = check.getDataBaseName(query);

	}

	@Override
	public String getDataBaseName() {
		return dataBaseName;
	}

	@Override
	public void setTableName() {
		TableName = check.getTableName(query);

	}

	@Override
	public String getTableName() {
		return TableName;
	}

	@Override
	public void setColumnName() {
		columnsName = check.getColumnsName(query);

	}

	@Override
	public String[] getColumnName() {
		return columnsName;
	}

	@Override
	public void setValue() {
		values = check.getValues(query);

	}

	@Override
	public String[] getValue() {
		return values;
	}

	@Override
	public void setDataTypes() {
		dataTypes = check.getDataType(query);
	}

	@Override
	public String[] getDataTypes() {
		return dataTypes;
	}

	@Override
	public void setCondition() {
		condition = check.getCondition(query);

	}

	@Override
	public String getCondition() {
		return condition;
	}
}
