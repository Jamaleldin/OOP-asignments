package eg.edu.alexu.csd.oop.db.cs15_cs18_cs49_cs39;

import java.io.File;
import java.sql.SQLException;

import eg.edu.alexu.csd.oop.db.Database;
import eg.edu.alexu.csd.oop.db.Order;
import eg.edu.alexu.csd.oop.db.Query;
import eg.edu.alexu.csd.oop.db.QueryInfo;

public class CDataBase implements Database {
	private Query cq = new CQuery();
	private QueryInfo qi = new CQueryInfo(null);
	private CheckSyntax check = new CheckSyntax();
	private Order order;

	@Override
	public String createDatabase(String databaseName, boolean dropIfExists) {
		boolean created = false;
		String q;
		File folder;
		if (dropIfExists == true) {
			q = "drop database " + databaseName;
			try {
				executeStructureQuery(q);
				q = "create database " + databaseName;
				created = executeStructureQuery(q);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			q = "create database " + databaseName;
			try {
				created = executeStructureQuery(q);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (created) {
			if (databaseName.matches("^((\\w[:(\\\\|\\/)(\\w+)]([\\\\|\\/]\\w+)*?)|(\\w+([\\\\|\\/]\\w+)+))$")) {
				folder = new File(databaseName);
			} else {
				folder = new File("DB/" + databaseName);
			}
			return folder.getAbsolutePath();
		}
		return null;
	}

	@Override
	public boolean executeStructureQuery(String query) throws SQLException {
		int checkNumber = check.checkSyntax(query);
		if (checkNumber != -1) {
			if (checkNumber == 0) {
				qi.setQuery(query);
				qi.setDataBaseName();
				order = new CreateDataBase(qi, cq);
				return order.excuteBoolean();
			} else if (checkNumber == 1) {
				qi.setQuery(query);
				qi.setTableName();
				qi.setColumnName();
				qi.setDataTypes();
				order = new CreatTable(qi, cq);
				return order.excuteBoolean();
			} else if (checkNumber == 4) {
				qi.setQuery(query);
				qi.setDataBaseName();
				return new DropDataBase(qi, cq).excuteBoolean();
			} else if (checkNumber == 7) {
				qi.setQuery(query);
				qi.setTableName();
				order = new DropTable(qi, cq);
				return order.excuteBoolean();
			}
		} else {
			throw new SQLException();
		}
		return false;
	}

	@Override
	public Object[][] executeQuery(String query) throws SQLException {
		String[][] selectedItems;
		if (check.checkSyntax(query) != -1) {
			qi.setQuery(query);
			qi.setTableName();
			qi.setColumnName();
			qi.setCondition();
			order = new SelectFromTable(cq, qi);
			selectedItems = order.excuteSelect();
		} else {
			throw new SQLException();
		}
		Object[][] selected = new Object[selectedItems.length][selectedItems[0].length];
		for (int i = 0; i < selectedItems.length; i++) {
			for (int j = 0; j < selectedItems[0].length; j++) {
				if (selectedItems[i][j].contains("'")) {
					selected[i][j] = selectedItems[i][j];
				} else {
					selected[i][j] = Integer.parseInt(selectedItems[i][j]);
				}
			}
		}
		return selected;
	}

	@Override
	public int executeUpdateQuery(String query) throws SQLException {
		int checkNumber = check.checkSyntax(query);
		if (checkNumber != -1) {
			if (checkNumber == 2) {
				qi.setQuery(query);
				qi.setColumnName();
				qi.setValue();
				order = new InsertIntoTable(qi, cq);
				return order.excuteUpdated();
			} else if (checkNumber == 3) {
				qi.setQuery(query);
				qi.setTableName();
				qi.setCondition();
				order = new DeleteFromTable(qi, cq);
				return order.excuteUpdated();
			} else if (checkNumber == 5) {
				qi.setQuery(query);
				qi.setTableName();
				qi.setColumnName();
				qi.setValue();
				qi.setCondition();
				order = new UpdateTable(cq, qi);
				return order.excuteUpdated();
			}
		} else {
			throw new SQLException();
		}
		return 0;
	}
}
