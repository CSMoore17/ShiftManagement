package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class TimeOffRequest {

public Connection c = null;
	
	public String Start_date = "";
	public String End_date = "";
	
	public boolean valid = true;
	
	public TimeOffRequest(Connection conn)
	{
		c = conn;
	}
	
	//take in a start date and end date
	public void Run(String eid, String startDate, String endDate)
	{
		try 
		{
			Statement s = c.createStatement();
			
			//System.out.println("What day(s) would you like off.\n(If just one enter the same date for both fields)");
			
			Start_date = startDate;
			End_date = endDate;
			
			//"Insert into Work_Times(Employee_ID,Date,Start_Time,End_Time,Hours,Available) values('%s','%s','%s','%s',%s,'%s')", E_ID,date,start,end,total_hrs,'Y'
			
			String sql_insert = String.format("Insert into Time_Off_Requests(Employee_ID,Start_Date,End_Date) values(%s,'%s','%s')", eid, Start_date, End_date);
			String sql_test = String.format("Select * From Time_Off_Requests Where Employee_ID=%s and Start_Date='%s' and End_Date='%s'", eid, Start_date, End_date);
			
			ResultSet RS = s.executeQuery(sql_test);
			
			if(RS.next()==true)
			{
				valid = false;
				//System.out.println("Time Off Request Already Exists");
			}
			else
			{
				
				s.executeUpdate(sql_insert);
				//System.out.println("Time Off Request Was Accepted");
				
				//reset valid
				valid = true;
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