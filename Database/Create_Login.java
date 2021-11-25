package Database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Create_Login {

	String username = "";
	String password = "";
	String user_type = "";
	
	String name = "";
	
	int L_ID = 0;
	
	Connection c = null;
	
	public Create_Login(Connection conn)
	{
		c = conn;
	}
	
	//For main use we can put params in the run method and  pass in instead of the 4 readEntries in the method
	// add username, password, user_type and name to the params of the Run method to pass into the code from the text field
	public void Run()
	{	//st.executeUpdate("Insert into Login_info(username,password,type) values('E1','E5555','E')");
		try
		{
			boolean valid_login = false;
			Statement s = c.createStatement();
			
			System.out.println("--Create Login--");
			username = readEntry("Username: ");
			password = readEntry("Password: ");
			user_type = readEntry("User Type: ");
			
			String sqlDup_tst = String.format("Select * From Login_info Where username='%s' and password='%s' and type='%s'", username, password, user_type);
			
			try
			{
				while(!valid_login)
				{
					ResultSet RS = s.executeQuery(sqlDup_tst);
				
					if(RS.next() == true)
					{
						System.out.println("Login Already Exists");
						System.out.println("--Create Login");
						username = readEntry("Username: ");
						password = readEntry("Password: ");
						user_type = readEntry("User Type: ");
						
						sqlDup_tst = String.format("Select * From Login_info Where username='%s' and password='%s' and type='%s'", username, password, user_type);
					}
					else
					{
						String sql_insert = String.format("Insert into Login_info(username,password,type) values('%s','%s','%s')", username, password, user_type);
						s.executeUpdate(sql_insert);
						
						//Getting Login_ID from Login_info table
						String sql_query = String.format("Select * From Login_info Where username='%s' and password='%s' and type='%s'", username, password, user_type);
						RS = s.executeQuery(sql_query);
						
						//Name for new user
						name = readEntry("Name: ");
						
						if(RS.next() == true) 
						{
							L_ID = RS.getInt("Login_ID");
							
							//Insert into Admin
							if(user_type.equals("A"))
							{
								sql_insert = String.format("Insert into Admin(Login_ID, name) values(%s,'%s')", L_ID, name);
								s.executeUpdate(sql_insert);
								System.out.println("Login Created and Inserted - Admin\n=======================\n");
							}
							
							//Insert into Employee
							if(user_type.equals("E"))
							{
								System.out.println("Intial values created -- Not final");
								
								sql_insert = String.format("Insert into Employee(Login_ID, name, Hours, Payrate, Max_Hrs_Wk) values('%s','%s',%s,%s,%s)", L_ID, name, 0, 0, 0);
								s.executeUpdate(sql_insert);
								System.out.println("Login Created and Inserted - Employee\n=======================\n");
							}
						}
						else
						{
							System.out.println("Setting L_ID for user failed");
						}
						
						valid_login = true;
						RS.close();
						s.close();
						break;
					}
				}
			}
			catch(Exception e)
			{
				System.out.println(e);
			}
			
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	
	
	//Read input -- can be deleted after passing on 
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
