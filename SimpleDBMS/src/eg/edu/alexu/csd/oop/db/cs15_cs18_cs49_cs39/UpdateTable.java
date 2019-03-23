package eg.edu.alexu.csd.oop.db.cs15_cs18_cs49_cs39;

import java.io.IOException;
import java.sql.SQLException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import eg.edu.alexu.csd.oop.db.Order;
import eg.edu.alexu.csd.oop.db.Query;
import eg.edu.alexu.csd.oop.db.QueryInfo;

public class UpdateTable implements Order {
	private Query queryOrders;
	private QueryInfo queryInfo;

	UpdateTable(Query query, QueryInfo queryInfo) {
		this.queryOrders = query;
		this.queryInfo = queryInfo;
	}

	@Override
	public boolean excuteBoolean() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String[][] excuteSelect() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int excuteUpdated() throws SQLException {
		try {
			return queryOrders.updateTable(queryInfo.getDataBaseName(), queryInfo.getTableName(),
					queryInfo.getColumnName(), queryInfo.getValue(), queryInfo.getCondition());
		} catch (ParserConfigurationException | SAXException | TransformerException e) {
			throw new SQLException();
		} catch (IOException e) {
			throw new SQLException();
		}
	}

}
