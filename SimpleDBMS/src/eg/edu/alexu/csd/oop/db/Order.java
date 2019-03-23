package eg.edu.alexu.csd.oop.db;

import java.sql.SQLException;

public interface Order {
	public boolean excuteBoolean() throws SQLException;
	public String[][] excuteSelect() throws SQLException;
	public int excuteUpdated() throws SQLException;
}
