package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class CreateLogin {
	String username = "";
	String password = "";
	String user_type = "";
	
	String name = "";
	boolean valid_login = false;
	boolean usernameExists = false; //becomes an issue if true
	boolean passwordExists = false; //becomes an issue if true
	
	int L_ID = 0;
	
	Connection c = null;
	
	public CreateLogin(Connection conn)
	{
		c = conn;
	}
	
	public void Run(String username, String password, String user_type, String name)
	{	//st.executeUpdate("Insert into Login_info(username,password,type) values('E1','E5555','E')");
		try
		{
			Statement s = c.createStatement();
			
			//System.out.println("--Create Login--");
			this.username = username;
			this.password = password;
			this.user_type = user_type;
			
			String sqlDup_tst = String.format("Select * From Login_info Where username='%s' and password='%s' and type='%s'", username, password, user_type);
			
			try
			{
				//while(!valid_login)
				//{
					ResultSet RS = s.executeQuery(sqlDup_tst);
				
					if(RS.next() == true)
					{
						
						usernameExists = true;
						passwordExists = true;
//						System.out.println("Login Already Exists");
//						System.out.println("--Create Login");
//						username = readEntry("Username: ");
//						password = readEntry("Password: ");
//						user_type = readEntry("User Type: ");
						
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
						this.name = name;
						
						if(RS.next() == true) 
						{
							L_ID = RS.getInt("Login_ID");
							
							//Insert into Admin
							if(user_type.equals("A"))
							{
								sql_insert = String.format("Insert into Admin(Login_ID, name) values(%s,'%s')", L_ID, name);
								s.executeUpdate(sql_insert);
								//System.out.println("Login Created and Inserted - Admin\n=======================\n");
							}
							
							//Insert into Employee
							if(user_type.equals("E"))
							{
								//System.out.println("Intial values created -- Not final");
								
								sql_insert = String.format("Insert into Employee(Login_ID, name, Hours, Payrate, Max_Hrs_Wk) values('%s','%s',%s,%s,%s)", L_ID, name, 0, 0, 0);
								s.executeUpdate(sql_insert);
								//System.out.println("Login Created and Inserted - Employee\n=======================\n");
							}
						}
						else
						{
							//System.out.println("Setting L_ID for user failed");
						}
						
						valid_login = true;
						RS.close();
						s.close();
						//break;
					}
				//}
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
	
}