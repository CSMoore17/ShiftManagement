package Database;



import java.io.*;
import java.sql.*;
import java.util.*;


public class Test_Connection {

	public static void main(String[] args)
	{
		String myDriver = "com.microsoft.jdbc.sqlserver.SQLServerDriver";
		String myUrl = "jdbc:sqlserver://localhost:55559";
		
		
		
		//Trying connection to driver
		try
		{
			//Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:55559","sa","CSCI540FALL");
			Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:55559;databaseName=EMPLOYEE TRACKER;user=sa;password=CSCI540FALL;");
			
			//Connection conn = DriverManager.getConnection(myUrl,"sa","CSCI540FALL");
			Statement st = conn.createStatement();
			
			
			//PK insert on -- recreate for a 
			st.executeUpdate("SET IDENTITY_INSERT Login_Info ON");
			//st.executeUpdate("SET IDENTITY_INSERT Employee ON");
			// -- //
			
			
			//input test data -- area
			//st.executeUpdate("Insert into Login_info(Login_ID,username,password)  values(1234,'Bob','Bob1234')");
			//st.executeUpdate("Insert into Employee(Employee_ID,Login_ID,name,hours,payrate,Max_Hrs_Wk)  values(1,1234,'Bob',12,8,40)");
			
			//st.executeUpdate("Insert into Login_info(Login_ID,username,password)  values(2345,'Dan','Dan2345')");
			//st.executeUpdate("Insert into Employee(Employee_ID,Login_ID,name,hours,payrate,Max_Hrs_Wk)  values(2,2345,'Dan',13,9,40)");
			// -- //
			
			
			
			/*ResultSet rs = st.executeQuery("SELECT * FROM Employee");
			
			
			while(rs.next())
			{
				System.out.println(rs.getString("Login_ID"));
				System.out.println(rs.getString("name"));
				System.out.println(rs.getString("hours"));
				System.out.println(rs.getString("payrate"));
				System.out.println(rs.getString("Max_Hrs_Wk"));
				System.out.println();
			}
			
			rs = st.executeQuery("SELECT * FROM Login_Info");
			
			while(rs.next())
			{
				System.out.println(rs.getString("Login_ID"));
				System.out.println(rs.getString("username"));
				System.out.println(rs.getString("password"));
			}*/
			
			
			//Testing text based interface
			Scanner s = new Scanner(System.in);
			
			//Main login
			boolean validLogin = false;
			System.out.println("--Login--");
			
			
			while(!validLogin)
			{
				String UN = readEntry("Username: ");
				String PW = readEntry("Password: ");
			
				String sqlDup_tst = String.format("Select * From Login_Info Where username='%s' and password='%s'", UN, PW);
				
				ResultSet RS = st.executeQuery(sqlDup_tst);
			
				if(RS.next() == true) {
					RS = st.executeQuery(String.format("Select * From Employee Where Login_ID='%s'",RS.getString("Login_ID")));
					
					System.out.println("valid");
				
					while(RS.next())
					{
						System.out.print("Welcome ");
						System.out.println(RS.getString("name"));
						System.out.println();
						validLogin = true;
						break;
					}
				}
				else
					System.out.println("INVALID LOGIN\n");
			}
			
			System.out.println(
					"1: Add Login\n"
					+"2: Create Employee\n"
					+"3: Create Higher Up\n"
					+"4: Create Admin\n"
								);
			
			
			String option = "";
			option = s.nextLine();
			
			while(!option.equals("q"))
			{
				//Add Login 
				if(option.equals("1"))
				{
					String loginID = readEntry("Adding a Login_Info entry\n==========\nLogin_ID: ");
					String username = readEntry("Username: ");
					String password = readEntry("Password: ");
					
					String sqlDup_test = "Select * From Login_info where Login_ID=" + loginID + "and username='" + username + "' and password= '" + password + "' ";
					
					ResultSet rs = st.executeQuery(sqlDup_test);
					
					if(rs.next() == true)
					{
						System.out.println("Login Already Exists");
					}
					else
					{
						String loginEntry = String.format("Insert into Login_info(Login_ID,username,password) values(%s,'%s','%s')",loginID,username,password);
						st.executeUpdate(loginEntry);
						System.out.println("Login Added\n");
					}
					
					option = readEntry("\nPick Option: ");
					System.out.println("------------------------------");
				}
				
				//st.executeUpdate("Insert into Login_info(Login_ID,username,password)  values(1234,'Bob','Bob1234')");
				//st.executeUpdate("Insert into Employee(Employee_ID,Login_ID,name,hours,payrate,Max_Hrs_Wk)  values(1,1234,'Bob',12,8,40)");
				
				
				//Create Employee 
				else if(option.equals("2"))
				{
					String employeeID = readEntry("Adding a Employee entry\n==========\nEmployee ID: ");
					String loginID    = readEntry("Login_ID: ");
					String name    	  = readEntry("Name: ");
					String hours      = readEntry("Hours: ");
					String payrate    = readEntry("Payrate: ");
					String maxhrs     = readEntry("Max Hours: ");
					
					String sqlDup_test = String.format("Select * From Employee where Employee_ID=%s and Login_ID=%s", employeeID,loginID);
					
					ResultSet rs = st.executeQuery(sqlDup_test);
					
					if(rs.next() == true)
					{
						System.out.println("Login Already Exists");
					}
					else
					{
						String employeeEntry = String.format("Insert into Employee(Employee_ID,Login_ID,name,hours,payrate,Max_Hrs_Wk) values(%s,%s,'%s',%s,%s,%s)",employeeID,loginID,name,hours,payrate,maxhrs);
						st.executeUpdate(employeeEntry);
						System.out.println("Employee Added\n");
					}
					
					option = readEntry("\nPick Option: ");
					System.out.println("------------------------------");
				}
				
				//Next option
				else if(option.equals("3"))
				{
					//String 
				}
				
				
				
				
				
				//options from home screen
				///---------------------------///
				
				//Schedule
				else if(option.equals("a"))
				{
					
				}
				
				//Hours Worked
				else if(option.equals("b"))
				{
					
				}
				
				//Calculate Pay
				else if(option.equals("c"))
				{
					
				}
				
				//Request Time Off
				else if(option.equals("d"))
				{
					
				}
				
				//Offer Shift
				else if(option.equals("e"))
				{
					
				}
				
				//Claim Shift
				else if(option.equals("f"))
				{
					
				}
				
				//Report
				else if(option.equals("g"))
				{
					
				}
			}
			
			
			
			//Closing connection
			st.close();
            conn.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	//For reading given entries
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
