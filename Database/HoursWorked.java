package Database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class HoursWorked {

	public Connection c = null;
	
	boolean valid = false;
	
	public HoursWorked(Connection conn)
	{
		c = conn;
	}
	
	public void Run(int LID)
	{
		try
		{
			Statement s = c.createStatement();
			String lid = String.valueOf(LID);
			
			float hours = 0.0f;
			
			String sql_querey = String.format("Select Hours From Employee Where Login_ID=%s", lid);
			
			ResultSet RS = s.executeQuery(sql_querey);
			
			if(RS.next() == true)
			{
				hours = RS.getFloat("Hours");
				valid = true;
				
				//for testing
				System.out.println(hours);
			}
			else
			{
				System.out.println("Invalid");			
			}
			
			//Return the Hours worked by current employee
			
			s.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
}
