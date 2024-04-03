package ConnectMysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connectmysql {
    public static Connection getConnection() {
    	Connection c=null;
    	
    	try {
			//DriverManager.registerDriver(new com.mysql.jdbc.Driver());
			String url="jdbc:mysql://localhost:3306/student_mvc";
			String username="root";
			String password="binvaloi123";
			c=DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return c;
    }
    public static void close(Connection c) {
    	try {
    		if(c!=null) {
    			c.close();
    		}
    		
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    }
}
