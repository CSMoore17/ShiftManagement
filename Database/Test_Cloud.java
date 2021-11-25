package Database;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

//import com.google.appengine.api.utils.SystemProperty;

//Connection name: 


public class Test_Cloud {

	public static void main(String[] args)
	{
		//location
		//jdbc:sqlserver://localhost;databaseName=employee-tracker;socketFactoryClass=com.google.cloud.sql.sqlserver.SocketFactory;socketFactoryConstructorArg=youtube-292000:us-east1:employee-tracker;user=PinkPumpkin;password=PPCSCI540
		
		//retring with azure
		//String url = "jdbc:sqlserver://localhost;databaseName=employee-tracker;socketFactoryClass=com.google.cloud.sql.sqlserver.SocketFactory;socketFactoryConstructorArg=youtube-292000:us-east1:employee-tracker;user=PinkPumpkin;password=PPCSCI540";
		String url = "jdbc:sqlserver://csci540.database.windows.net:1433;database=employeetracker;user=PinkPumpkin540@csci540;password=PPCSCI540#;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
		String Quit = "";
		
		//used with schedule shower
		String EID = "";
		String WID = "";
		
		boolean admin_user = false;
		boolean employee_user = false;
		boolean logged_in = false;
 		
		
		try
		{
			//Connection
			Connection conn = DriverManager.getConnection(url);
			Statement st = conn.createStatement();
			
			System.out.println("Connected");
			
			Scanner s = new Scanner(System.in);
			
			//Option selection
			//-- Login -- (forced for start)
			
			//Create option/action objects
			Login L = new Login(conn);
			Create_Login CL = new Create_Login(conn);
			Create_Shift CS = new Create_Shift(conn);
			EmployeeLookup EL = new EmployeeLookup(conn);
			ViewReports VR = new ViewReports(conn);
			ChangeEmployee CE = new ChangeEmployee(conn);
			
			CreateReport CR = new CreateReport(conn);
			HoursWorked HW = new HoursWorked(conn);
			CalculatePay CP = new CalculatePay(conn);
			Schedule S = new Schedule(conn);
			OfferShift OS = new OfferShift(conn);
			ClaimShift ECS = new ClaimShift(conn);
			
			while(!Quit.equals("y")) {
			while(!L.valid_login)
			{
				L.Run();	

				if(L.User_Type.equals("A"))
				{
					
					String sql_stat = String.format("Select * From Admin Where Login_ID='%s'",L.Login_ID);
					ResultSet RS = st.executeQuery(sql_stat);

					if(RS.next() == true)
					{
						System.out.print("\nWelcome Admin: " + RS.getString("name"));
						admin_user = true;
						logged_in = true;
					}
				}
				else if(L.User_Type.equals("E"))
				{
					String sql_stat = String.format("Select * From Employee Where Login_ID='%s'",L.Login_ID);
					ResultSet RS = st.executeQuery(sql_stat);
					
					if(RS.next() == true)
					{
						System.out.print("\nWelcome Employee: " + RS.getString("name"));
						employee_user = true;
						logged_in = true;
						EID = RS.getString("Employee_ID");
					}
				}
			}
			
			//options based on user type --- 
			System.out.println("\n--- Options ---");
			String options = "";
			
			//admin options
			if(admin_user)
			{
				System.out.println("1: Logout\n"
								 + "2: Create Login\n"
								 + "3: Create Shift\n"
								 + "4: Employee Lookup\n"
								 + "5: View Reports\n"
								 + "6: Change Employee");
				
				options = ("1: Logout\n"
						 + "2: Create Login\n"
						 + "3: Create Shift\n"
						 + "4: Employee Lookup\n"
						 + "5: View Reports\n"
						 + "6: Change Employee");
						
			}
			
			//employee options
			if(employee_user)
			{
				System.out.println("1: Logout\n"
								 + "2: Schedule\n"
								 + "3: Calculate Pay\n"
								 + "4: Offer Shift\n"
								 + "5: Request Time Off\n"
								 + "6: Claim Shift\n"
								 + "7: Hours Worked\n"
								 + "8: Anonymous Report\n");
				
				options = ("1: Logout\n"
						 + "2: Schedule\n"
						 + "3: Calculate Pay\n"
						 + "4: Offer Shift\n"
						 + "5: Request Time Off\n"
						 + "6: Claim Shift\n"
						 + "7: Hours Worked\n"
						 + "8: Anonymous Report\n");
			}
			
			String opsel = readEntry("=======================\nSelect: ");
			
			//while not logged out
			
			while(logged_in)
			{
				if(admin_user)
				{
					//Logout
					if(opsel.equals("1"))
					{
						logged_in = false;
						L.Reset();
						admin_user = false;
						break;
					}
				
					//Create Login
					if(opsel.equals("2"))
					{
						CL.Run();
					}
					
					//Create Shift
					if(opsel.equals("3"))
					{
						CS.Run();
					}
					
					//Employee Lookup
					if(opsel.equals("4"))
					{
						EL.Run();
					}
					
					//View Reports
					if(opsel.equals("5"))
					{
						VR.Run();
					}
					
					//View Reports
					if(opsel.equals("6"))
					{
						CE.Run();
					}
					
					opsel = readEntry(options);
				}
			
				if(employee_user)
				{
					if(opsel.equals("1"))
					{
						logged_in = false;
						L.Reset();
						employee_user = false;
						break;
					}
					
					//Schedule - view
					if(opsel.equals("2"))
					{
						S.Run(Integer.valueOf(EID));
					}
					
					//Schedule - view
					if(opsel.equals("3"))
					{
						CP.Run(L.Login_ID);
					}
					
					//Offer Shift
					if(opsel.equals("4"))
					{
						OS.Run(Integer.valueOf(EID));
					}
					
					//Claim Shift
					if(opsel.equals("6"))
					{
						ECS.Run(Integer.valueOf(EID));
					}
					
					//Hours - vies
					if(opsel.equals("7"))
					{
						HW.Run(L.Login_ID);
					}
					
					//Create - Report
					if(opsel.equals("8"))
					{
						CR.Run();
					}
					
					opsel = readEntry(options);
				}
				
				if(opsel.equals("1"))
				{
					Quit = readEntry("Quit (y/n)");
					System.out.println("\n==========--===========");
				}
				
				//if(Quit.equals("y"))
				//{
					//L.Reset();
				//}
				
				//opsel = readEntry(options);
			} }
			
			//Close connection
			//st.close();
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
