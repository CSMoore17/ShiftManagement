package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Login {
	String username;
	String password;
	
	int Login_ID = 0;
	String User_Type = "";
	
	Connection c = null;
	
	boolean valid_login = false;
	
	public Login(Connection cn)
	{
		//connection 
		c = cn;
	}
	
	public void Run(String username, String password)
	{
		//alone style
		try
		{
			Statement s = c.createStatement();
			
			this.username = username;
			this.password = password;
					
			String sqlDup_tst = String.format("Select * From Login_Info Where username='%s' and password='%s'", username, password);
			
			try 	
			{
				ResultSet RS = s.executeQuery(sqlDup_tst);
				
				if(RS.next() == true) 
				{
					Login_ID = RS.getInt("Login_ID");
					User_Type = RS.getString("type");
					valid_login = true;
				}
				else
				{
					System.out.println("INVALID LOGIN\n=======================\n");
				}

			}
			catch(Exception e)
			{
				System.out.println(e);
			}
			
			//close statement
			//RS.close();
			s.close();
			
		}
		catch(Exception e)
		{
			System.out.println(e);
		}	
		
	}
	
	public int Get_Login_ID()
	{
		return Login_ID;
	}
	
	public void Reset()
	{
		username = "";
		password = "";
		valid_login = false;
		User_Type = "";
	}
	
	//Read input
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
