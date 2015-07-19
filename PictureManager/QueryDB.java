import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.DefaultListModel;


public class QueryDB {

	Connection conn;
	Statement stmt;
	ResultSet rs;
	ArrayList<Picture> picModel;
	
	
	public QueryDB() throws SQLException{

		try {
			conn = DriverManager.getConnection(
			 "jdbc:mysql://localhost/yourDB",
			 "username",
			 "password" );
		} catch (SQLException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		try {
			try {
				stmt = conn.createStatement();
			} catch (SQLException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		try {
			try {
				rs = stmt.executeQuery( "SELECT * FROM pictures");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		    try {
		    	picModel = new ArrayList<Picture>();
		        while ( rs.next() ) {
		        	Picture p = new Picture();
		        	p.setId(rs.getInt(1));
		        	p.setName(rs.getString(2));
		        	p.setPath(rs.getString(3));
		        
		        	picModel.add(p);
		        }
		    } finally {
		        try { rs.close(); } catch (Throwable ignore) {}
		    }
		} finally {
		    try { stmt.close(); } catch (Throwable ignore) {}
		}
		} finally {
		    try { conn.close(); } catch (Throwable ignore) {}
		}
	}
	
	
}
