package eg.edu.alexu.csd.oop.jdbc.cs15;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class JLogger {
	private static Logger Log = Logger.getLogger(JLogger.class);

	public JLogger() {
		PropertyConfigurator.configure("log4j.properties");
	}

	public static Logger getLog() {
		return Log;
	}

}
