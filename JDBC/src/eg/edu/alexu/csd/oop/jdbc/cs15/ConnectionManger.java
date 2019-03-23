package eg.edu.alexu.csd.oop.jdbc.cs15;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;

import eg.edu.alexu.csd.oop.db.cs15_cs18_cs49_cs39.CDataBase;

public class ConnectionManger {
	private long expirationTime;

	private Hashtable<Connection, Long> used, unused;
	public ConnectionManger() {
		expirationTime = 30000;
		used = new Hashtable<>();
		unused = new Hashtable<>();
	}

	protected Connection create() {
		return new JConnect(new CDataBase());
	}

	public void expire(Connection o) {
		try {
			o.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean validate(Connection o) {
		try {
			return (!o.isClosed());
		} catch (SQLException e) {
			e.printStackTrace();
			return (false);
		}
	}

	public synchronized Connection checkOut() {
		long now = System.currentTimeMillis();
		Connection t;
		if (used.size() > 0) {
			Enumeration<Connection> e = used.keys();
			while (e.hasMoreElements()) {
				t = e.nextElement();
				if ((now - used.get(t)) > expirationTime) {
					// object has expired
					used.remove(t);
					expire(t);
					t = null;
				} else {
					if (validate(t)) {
						used.remove(t);
						unused.put(t, now);
						return (t);
					} else {
						// object failed validation
						used.remove(t);
						expire(t);
						t = null;
					}
				}
			}
		}
		// no objects available, create a new one
		t = create();
		unused.put(t, now);
		return (t);
	}

	public synchronized void checkIn(Connection t) {
		used.remove(t);
		unused.put(t, System.currentTimeMillis());
	}

	public Connection getConnection(String path) {
		JConnect c = (JConnect) checkOut();
		c.setConnectionManger(this);
		c.setPath(path);
		return c;

	}

}
