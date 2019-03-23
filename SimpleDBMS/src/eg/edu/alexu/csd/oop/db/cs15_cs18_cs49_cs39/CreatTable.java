package eg.edu.alexu.csd.oop.db.cs15_cs18_cs49_cs39;

import java.sql.SQLException;

import eg.edu.alexu.csd.oop.db.Order;
import eg.edu.alexu.csd.oop.db.Query;
import eg.edu.alexu.csd.oop.db.QueryInfo;

public class CreatTable implements Order {
	private QueryInfo queryInfo;
	private Query queryOrders;

	CreatTable(QueryInfo queryInfo,Query queryOrders) {
		this.queryInfo = queryInfo;
		this.queryOrders = queryOrders;
	}
	@Override
	public boolean excuteBoolean() throws SQLException {
		return queryOrders.createTable(queryInfo.getDataBaseName(), queryInfo.getTableName(), queryInfo.getColumnName(),
				queryInfo.getDataTypes());
	}

	@Override
	public String[][] excuteSelect() {
		return null;
	}

	@Override
	public int excuteUpdated() {
		return 0;
	}

}
