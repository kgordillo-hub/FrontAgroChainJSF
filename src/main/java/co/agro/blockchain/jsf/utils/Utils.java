package co.agro.blockchain.jsf.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public final class Utils {

	
	public static final String getProperty(final String propertyName) throws IOException {
		try (final InputStream input = Utils.class.getClassLoader()
				.getResourceAsStream("config.properties")) {

			final Properties prop = new Properties();

			if (input == null) {
				System.out.println("Sorry, unable to find config.properties");
				return null;
			}
			// load a properties file from class path, inside static method
			prop.load(input);
			return prop.getProperty(propertyName);
		} catch (final IOException ex) {
			throw ex;
		}
	}
}
