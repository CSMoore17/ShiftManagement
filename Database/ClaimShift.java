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
	//ArrayList<String> EMPID = new ArrayList<String>();
	ArrayList<String> date = new ArrayList<String>();
	ArrayList<String> start_time = new ArrayList<String>();
	ArrayList<String> end_time = new ArrayList<String>();
	//ArrayList<String> status = new ArrayList<String>();
	
	public ClaimShift(Connection conn)
	{
		c = conn;
	}
	
	//Need to pull up Shift Give Away (Offer Shift)
	//Ask for the id of what shift they want
	
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
			
			//Select a shift ID
			String SGA_ID = readEntry("Please pick a Shift to add: ");
			
			//check SGA_ID picked is valid
			
			
			//close statement
			
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
