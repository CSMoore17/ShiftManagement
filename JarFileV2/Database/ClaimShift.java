package Database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class ClaimShift {

	public Connection c = null;
	
	// used to make sure a Employee isn't trying to accept their own shift give away
	boolean valid = false;
	
	ArrayList<String> SGA_ID = new ArrayList<String>();
	ArrayList<String> date = new ArrayList<String>();
	ArrayList<String> start_time = new ArrayList<String>();
	ArrayList<String> end_time = new ArrayList<String>();
	
	public ClaimShift(Connection conn)
	{
		c = conn;
	}
	
	//Need to pull up Shift Give Away (Offer Shift)
	//Ask for the id of what shift they want - SGAID
	
	public void Run(int EID)
	{
		try
		{
			
			//clear all array lists
			SGA_ID.clear();
			date.clear();
			start_time.clear();
			end_time.clear();
			
			Statement s = c.createStatement();
			
			//adding all SGA listings to the array lists to show
			String sql_query = String.format("Select * From Shift_Give_Away Where Status='A' and Employee_ID<>%s",EID);
			
			//testing
			System.out.println(sql_query);
			
			ResultSet RS = s.executeQuery(sql_query);
			
			if(RS.next() == true)
			{
				SGA_ID.add(String.valueOf(RS.getInt("SGA_ID")));
				date.add(String.valueOf(RS.getDate("Date")));
				start_time.add(String.valueOf(RS.getTime("Start_Time")));
				end_time.add(String.valueOf(RS.getTime("End_Time")));
				
				while(RS.next() == true)
				{
					SGA_ID.add(String.valueOf(RS.getInt("SGA_ID")));
					date.add(String.valueOf(RS.getDate("Date")));
					start_time.add(String.valueOf(RS.getTime("Start_Time")));
					end_time.add(String.valueOf(RS.getTime("End_Time")));
				}
			}
			
			//show all offered shifts (Shift Give Away)
			System.out.println(SGA_ID);
			System.out.println(date);
			System.out.println(start_time);
			System.out.println(end_time);
			
			
			//Select a shift ID
			String SGAID = readEntry("Please pick a Shift to add: ");
			
			//check SGA_ID picked is valid
			if(SGA_ID.contains(SGAID))
			{
				valid = true;
				
				//add to claim shift table
				//change status to 'P' (Pending) so other Employees can't try to claim it
				//Once added to claim shift table it can be approved by Admin user
				// -- If admin accepts claim the shift will be added to work_times with the EID to tie it to Employee 
				// -- -- claim deleted from claim shift and offer shift with that ID (deletes from claim shift and offer shift)
				// -- If admin denies claim the it will be deleted 
				// -- -- claim deleted from claim shift. Offer shift will stay up and status will turn to 'A' (available)
				
				//"Insert into Reports(A_Report,Date) values('%s','%s')", report,time
				//"Update Employee Set Name = %s Where Employee_ID = %s",Name,EID
				
				String sql_insert = String.format("Insert into Shift_Claim(Employee_ID,SGA_ID,Status) values(%s,%s,'%s')",EID,SGAID,'P');
				String sql_update = String.format("Update Shift_Give_Away Set Status ='%s' Where SGA_ID = %s",'P',SGAID);
				
				//execute sql strings
				s.executeUpdate(sql_insert);
				s.executeUpdate(sql_update);
				
				System.out.println("Claim accepted -- Wait for approval");
			}
			else
			{
				valid = false;
				System.out.println("Not a Valid SGA_ID");
			}
			
			//close statement
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
