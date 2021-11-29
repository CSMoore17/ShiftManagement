package Database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class CalculatePay {

	public Connection c = null;
	
	boolean valid = false;
	
	public CalculatePay(Connection conn)
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
			float pr = 0.0f;
			
			String sql_querey = String.format("Select Hours , Payrate From Employee Where Login_ID=%s", lid);
			
			ResultSet RS = s.executeQuery(sql_querey);
			
			if(RS.next() == true)
			{
				hours = RS.getFloat("Hours");
				pr = RS.getFloat("Payrate");
				valid = true;
				
				//for testing
				System.out.println(hours);
				System.out.println(pr);
			}
			else
			{
				System.out.println("Invalid");			
			}
			
			//calc pay
			float pay = hours * pr;
			System.out.println(pay);
			
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	
	
}
