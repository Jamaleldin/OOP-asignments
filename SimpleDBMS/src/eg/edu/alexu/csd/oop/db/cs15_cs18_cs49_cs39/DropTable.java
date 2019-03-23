package eg.edu.alexu.csd.oop.db.cs15_cs18_cs49_cs39;

import eg.edu.alexu.csd.oop.db.Order;
import eg.edu.alexu.csd.oop.db.Query;
import eg.edu.alexu.csd.oop.db.QueryInfo;

public class DropTable implements Order {
	private QueryInfo queryInfo;
	private Query queryOrders;

	DropTable(QueryInfo queryInfo,Query queryOrders) {
		this.queryInfo = queryInfo;
		this.queryOrders = queryOrders;
	}

	@Override
	public boolean excuteBoolean() {
		return queryOrders.dropTable(queryInfo.getDataBaseName(), queryInfo.getTableName());
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
