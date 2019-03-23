package eg.edu.alexu.csd.oop.db.cs15_cs18_cs49_cs39;

import java.sql.SQLException;

import eg.edu.alexu.csd.oop.db.Order;
import eg.edu.alexu.csd.oop.db.Query;
import eg.edu.alexu.csd.oop.db.QueryInfo;

public class SelectFromTable implements Order {
	private Query queryOrders;
	private QueryInfo queryInfo;

	SelectFromTable(Query query,QueryInfo queryInfo) {
		this.queryOrders = query;
		this.queryInfo = queryInfo;
	}

	@Override
	public boolean excuteBoolean() {
		return false;
	}

	@Override
	public String[][] excuteSelect() throws SQLException {
		return queryOrders.selectFromTable(queryInfo.getDataBaseName(), queryInfo.getTableName(), queryInfo.getColumnName()[0],
				queryInfo.getCondition());
	}

	@Override
	public int excuteUpdated() {
		return 0;
	}

}
