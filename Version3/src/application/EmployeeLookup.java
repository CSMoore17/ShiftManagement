package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EmployeeLookup {

public Connection c = null;
	
	boolean EID = false;
	boolean name = false;
	boolean invalidLookup = false;
	boolean toTrue = false;
	
	//Used to store RS for sql return
	ArrayList<String> EmpList_ID = new ArrayList<String>();
	ArrayList<String> EmpList_Name = new ArrayList<String>();
	ArrayList<String> EmpList_Hours = new ArrayList<String>();
	ArrayList<String> EmpList_PayRate = new ArrayList<String>();
	
	ArrayList<String> TO_StartDate = new ArrayList<>();
	ArrayList<String> TO_EndDate = new ArrayList<>();
	
	public EmployeeLookup(Connection conn)
	{
		c = conn;
	}
	
	//text params go though run
	//Variables that use readEntry- input 
	public void Run(String name_or_id)
	{
		try
		{
			Statement s = c.createStatement();
			
			//System.out.println("-- Employee Lookup --\n==================");
			String input = name_or_id;
			
			int E_ID = 0;
			//Checking to see if name or E_ID was passed
			try
			{
				E_ID = Integer.parseInt(input);
				EID = true;
			}
			catch(NumberFormatException e)
			{
				//System.out.println("Not E_ID");
				name = true;
			}
			
			//Look for employee
			String sql_querey = "";
			String sql_count = "";
			if(EID) {
				sql_querey = String.format("Select * From Employee Where Employee_ID=%s",input);
				//sql_count = String.format("Select Count(*) From Employee Where Employee_ID=%s",input);
				}
			else {
				sql_querey = String.format("Select * From Employee Where name='%s'", input);
				//sql_count = String.format("Select Count(*) From Employee Where name='%s'",input);
			}
			
			//reset values
			EID = false;
			name = false;
			
			//Testing
			//System.out.println(sql_querey);
			
			ResultSet RS = s.executeQuery(sql_querey);
			
			int col = 0;
			
			//add all rows to the list of results
			boolean tracker = false;
			while(RS.next() == true)
			{
				//System.out.println("While loop");
				//add to arraylist
				EmpList_ID.add(String.valueOf(RS.getInt("Employee_ID")));
				EmpList_Name.add(RS.getString("Name"));
				EmpList_Hours.add(String.valueOf(RS.getFloat("Hours")));
				EmpList_PayRate.add(String.valueOf(RS.getFloat("Payrate")));
				
				tracker = true;
				
			}
			if(!tracker)
			{
				invalidLookup = true;
				//System.out.println("-- No Employee Exists -- ");
			}	
			
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("YYYY/MM/dd");
            LocalDateTime now = LocalDateTime.now();
            String currTime = String.valueOf(dtf.format(now));
			
			for(String eid : EmpList_ID)
			{
				//adding times from Time Off Request
				String sql_query = String.format("Select * From Time_Off_Requests Where Employee_ID=%s and Start_Date>'%s'", eid, currTime);
				RS = s.executeQuery(sql_query);
				//just the first line
				if(RS.next() == true)
				{
					toTrue = true;
					TO_StartDate.add(String.valueOf(RS.getDate("Start_Date")));
					TO_EndDate.add(String.valueOf(RS.getDate("End_Date")));
					//System.out.println("for loop");
				}
			}
			
			
			s.close();
			
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	public void clear() {
		//Clear lists after each run
		toTrue = false;
		EmpList_ID.clear();
		EmpList_Name.clear();
		EmpList_Hours.clear();
		EmpList_PayRate.clear();
		TO_StartDate.clear();
		TO_EndDate.clear();
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