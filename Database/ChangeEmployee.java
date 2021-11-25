package Database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ChangeEmployee {

	public Connection c = null;
	
	boolean validEmp = false;
	boolean prChange = false;
	boolean nameChange = false;
	
	public ChangeEmployee(Connection conn)
	{
		c = conn;
	}
	
	//variables that use ReadEntry - EID, Name, Payrate
	public void Run()
	{
		try
		{
			Statement s = c.createStatement();
			
			//Pick EID
			String EID = readEntry("EID: ");
			String sql_find = String.format("Select * From Employee Where Employee_ID=%s", EID);
			String PR = "";
			String Name = "";
			
			ResultSet RS = s.executeQuery(sql_find);
			
			if(RS.next() == true)
			{
				PR = readEntry("New Payrate: ");
				Name = readEntry("New Name: ");
				validEmp = true;
				
				if(!PR.equals(""))
					prChange = true;
				if(!Name.equals(""))
					nameChange = true;
			}
			else
			{
				//No employee exists
				System.out.println("No Employee Exists");
			}
			
			//Update Employee Set Hours = %s Where Employee_ID = %s
			
			if(validEmp)
			{
				String sql_update = "";
				String sql_update2 = "";
				
				if(prChange & !nameChange)
				{
					System.out.println("P");
					sql_update = String.format("Update Employee Set Payrate = %s Where Employee_ID = %s",PR,EID);
					s.executeUpdate(sql_update);
				}
				
				else if(nameChange & !prChange)
				{
					System.out.println("N");
					sql_update = String.format("Update Employee Set Name = %s Where Employee_ID = %s",Name,EID);
					s.executeUpdate(sql_update);
				}
				
				if(prChange && nameChange)
				{
					System.out.println("P and N");
					sql_update = String.format("Update Employee Set Payrate = %s Where Employee_ID = %s",PR,EID);
					sql_update2 = String.format("Update Employee Set Name = '%s' Where Employee_ID = %s",Name,EID);
					s.executeUpdate(sql_update);
					s.executeUpdate(sql_update2);
				}
				
				System.out.println(sql_update);
				System.out.println(sql_update2);
				
				System.out.println("Update Complete");
			}
			
			
			s.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	//Read Entry
	static String readEntry(String prompt)
	{
		try
		{
			StringBuffer buffer = new StringBuffer();
			System.out.print(prompt);
			System.out.flush();
			int c = System.in.read();
			while (c != '\n' && c != -1)
			{
				buffer.append((char)c);
				c = System.in.read();
			}
			return buffer.toString().trim();
		}
			catch(IOException e)
			{
				return "";
			}
	}
	
}
