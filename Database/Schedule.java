package Database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class Schedule {

	public Connection c = null;
	
	boolean validSchedule = false;
	
	//used for showing work times
	ArrayList<String> W_ID = new ArrayList<String>();
	ArrayList<String> date = new ArrayList<String>();
	ArrayList<String> start_time = new ArrayList<String>();
	ArrayList<String> end_time = new ArrayList<String>();
	
	
	public Schedule(Connection conn)
	{
		c = conn;
	}
	
	
	//Doesn't need any typed input
	//Input will be taken from the driver EID
	public void Run(int eid)
	{
		try
		{
			//clear array lists
			W_ID.clear();
			date.clear();
			start_time.clear();
			end_time.clear();
			
			Statement s = c.createStatement();
			
			String sql_querey = String.format("Select * From Work_Times Where Employee_ID=%S", eid);
			
			//testing
			System.out.println(sql_querey);
			
			ResultSet RS = s.executeQuery(sql_querey);
			
			
			if(RS.next() == true)
			{
				validSchedule = true;
				W_ID.add(String.valueOf(RS.getInt("WK_ID")));
				date.add(String.valueOf(RS.getDate("Date")));
				start_time.add(String.valueOf(RS.getTime("Start_Time")));
				end_time.add(String.valueOf(RS.getTime("End_Time")));
				
				//gets the rest of the RS
				while(RS.next() == true)
				{
					W_ID.add(String.valueOf(RS.getInt("WK_ID")));
					date.add(String.valueOf(RS.getDate("Date")));
					start_time.add(String.valueOf(RS.getTime("Start_Time")));
					end_time.add(String.valueOf(RS.getTime("End_Time")));
				}
			}
			else
			{
				validSchedule = false;
				System.out.println("No Shifts Exist for this Employee");
			}
			
			//for testing
			System.out.println(W_ID);
			System.out.println(date);
			System.out.println(start_time);
			System.out.println(end_time);
			
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
