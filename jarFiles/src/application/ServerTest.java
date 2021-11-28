package application;
import java.io.*;
import java.sql.*;
import java.util.*;

public class ServerTest {
	
	public static void main(String[] args) {
		
		String url = "jdbc:sqlserver://csci540.database.windows.net:1433;database=employeetracker;user=PinkPumpkin540@csci540;password=PPCSCI540#;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
		String sql = "Select * From Login_Info";
		
		try {
			
			Connection conn = DriverManager.getConnection(url);
			Statement st = conn.createStatement();
			System.out.println("connected");
			
			ResultSet RS = st.executeQuery(sql);
			
			while (RS.next() == true) {
				System.out.println(RS.getInt("Login_ID"));
				System.out.println(RS.getString("username"));
				System.out.println(RS.getString("password"));
			}
			
			
		} catch (Exception e) {
			System.out.print(e);
		}
	}
}