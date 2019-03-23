package eg.edu.alexu.csd.oop.jdbc.cs15;

import java.io.File;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import org.apache.log4j.Logger;

public class JDriver implements Driver {
	ConnectionManger connectionManger;
    private Logger log ;
	public JDriver() {
		connectionManger = new ConnectionManger();
		log = JLogger.getLog();
	}

	@Override
	public boolean acceptsURL(String arg0) throws SQLException {
		log.info("acceptsURL");
		return true;
	}

	@Override
	public Connection connect(String url, Properties info) throws SQLException {
		File dir = (File) info.get("path");
		String path = dir.getAbsolutePath();
		log.info("Making a Connection");
		 
		 //JLogger.Supporting();
		return connectionManger.getConnection(path);
	}

	@Override
	public DriverPropertyInfo[] getPropertyInfo(String url, Properties properties) throws SQLException {
		// TODO Auto-generated method stub
		DriverPropertyInfo[] info = new DriverPropertyInfo[1];
		info[0] = new DriverPropertyInfo(url, properties.toString());
	    log.info("DriverPropertyInfo");
		return info;
	}

	@Override
	public int getMajorVersion() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public int getMinorVersion() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean jdbcCompliant() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

}
