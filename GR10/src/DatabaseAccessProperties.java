/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author catherineberrut
 */
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * The BenchmarkProperties class describes the properties of the benchmark to
 * run.
 */
public class DatabaseAccessProperties {

	private Properties prop = new Properties();
	
	private String dbUrl;
	private String username, password;

	public DatabaseAccessProperties(String propertiesFile) {
		try {
			prop = new Properties();
			prop.load(new FileInputStream(propertiesFile));
		} catch (FileNotFoundException e) {
		      System.err.println( "FileNotFoundException: " + e.getMessage()) ;
		      e.printStackTrace();
		      return;
		} catch (IOException e) {
		      System.err.println( "IOException: " + e.getMessage()) ;
		      e.printStackTrace();
		      return;
		}
		dbUrl 	= prop.getProperty("database.url");
		username 	= prop.getProperty("database.username");
		password 	= prop.getProperty("database.password");
	}

	public String getDatabaseUrl() {
		return dbUrl;
	}
	public String getUsername() {
		return username;
	}
	public String getPassword() {
		return password;
	}
}
