package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Schedule {

public Connection c = null;
	
	boolean validSchedule = false;
	
	//used for showing work times
	ArrayList<String> W_ID = new ArrayList<String>();			//W_ID
	ArrayList<String> date = new ArrayList<String>();			//date
	ArrayList<String> start_time = new ArrayList<String>();		//start time
	ArrayList<String> end_time = new ArrayList<String>();		//end time
	ArrayList<String> available = new ArrayList<String>();		//shows if the shift has been offered or not already
	
	
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
			available.clear();
			
			Statement s = c.createStatement();
			
			//Current time
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("YYYY/MM/dd");
			LocalDateTime now = LocalDateTime.now();
			String currTime = String.valueOf(dtf.format(now));
			
			String sql_querey = String.format("Select * From Work_Times Where Employee_ID=%S and Date>='%s'ORDER BY Date ASC", eid, currTime);
			
			
			//testing
			//System.out.println(sql_querey);
			
			ResultSet RS = s.executeQuery(sql_querey);
			
			//testing
			//System.out.println("Just before Schedule adding elements to the array");
			
				
			//gets the rest of the RS
			while(RS.next() == true)
			{
				W_ID.add(String.valueOf(RS.getInt("WK_ID")));
				date.add(String.valueOf(RS.getDate("Date")));
				start_time.add(String.valueOf(RS.getTime("Start_Time")));
				end_time.add(String.valueOf(RS.getTime("End_Time")));
				available.add(RS.getString("Available"));
			}
			
			//nothing is returned
			if(W_ID.size() == 0)
			{
				validSchedule = false;
				//System.out.println("No Shifts Exist for this Employee");
			}
			
			//for testing
			//System.out.println(W_ID);
			//System.out.println(date);
			//System.out.println(start_time);
			//System.out.println(end_time);
			
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