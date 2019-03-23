package eg.edu.alexu.csd.oop.db.cs15_cs18_cs49_cs39;

import java.io.IOException;
import java.sql.SQLException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import eg.edu.alexu.csd.oop.db.Order;
import eg.edu.alexu.csd.oop.db.Query;
import eg.edu.alexu.csd.oop.db.QueryInfo;

public class InsertIntoTable implements Order {
	private QueryInfo queryInfo;
	private Query queryOrders;

	InsertIntoTable(QueryInfo queryInfo, Query queryOrders) {
		this.queryInfo = queryInfo;
		this.queryOrders = queryOrders;
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
			queryOrders.insertIntoTable(queryInfo.getDataBaseName(), queryInfo.getTableName(),
					queryInfo.getColumnName(), queryInfo.getValue());
			return 1;
		} catch (ParserConfigurationException | SAXException | IOException | TransformerException e) {
			throw new SQLException();
		} catch (SQLException e) {
			throw new SQLException();
		}

	}

}
