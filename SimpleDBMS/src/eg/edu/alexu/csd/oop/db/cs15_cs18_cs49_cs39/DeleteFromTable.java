package eg.edu.alexu.csd.oop.db.cs15_cs18_cs49_cs39;

import java.sql.SQLException;

import eg.edu.alexu.csd.oop.db.Order;
import eg.edu.alexu.csd.oop.db.Query;
import eg.edu.alexu.csd.oop.db.QueryInfo;

public class DeleteFromTable implements Order {
	private QueryInfo queryInfo;
	private Query queryOrders;

	DeleteFromTable(QueryInfo queryInfo, Query queryOrders) {
		this.queryInfo = queryInfo;
		this.queryOrders = queryOrders;
	}

	@Override
	public boolean excuteBoolean() {
		return false;
	}

	@Override
	public String[][] excuteSelect() {
		return null;
	}

	@Override
	public int excuteUpdated() throws SQLException {
		return queryOrders.deleteFromTable(queryInfo.getDataBaseName(), queryInfo.getTableName(),
				queryInfo.getCondition());
	}

}
