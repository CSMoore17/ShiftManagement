package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class CreateShift {

	String name = "";
	String E_ID = "";
	boolean dateValid = true;
	boolean valid_EID;
	boolean validTime = true;
	boolean t_start = false;
	boolean t_end = false;
	
	
	
	Connection c = null;

	public CreateShift(Connection conn)
	{
		c = conn;
	}
	
	//To Do
	/*
	
	Ask for user name and make sure it is an employee
		- if not a valid employee bring error
	If there is more than one employee with the same name ask for the employee_id
	Use employee_id from employee table to set E_ID here
	Ask for date
	Ask for start time and end time
	Ask for how many hours (even though that is answered by above values. just do it)
	
	*/
	
	public void Run(String employeeID, String d, String startTime, String endTime)
	{
		try
		{
			Statement s = c.createStatement();
			
			//System.out.println("-- Create Shift --");
			
			//Pick from the list
			//fix make sure the admin is picking a valid e_id from the list given
			E_ID = employeeID;
			
			//Check if ID exists
			String sql_query = String.format("Select * From Employee Where Employee_ID = %s", E_ID);
			ResultSet RS = s.executeQuery(sql_query);
			
			valid_EID = false;
			
			//while(!valid_EID)
			//{
				if(RS.next() == true)
				{
					valid_EID = true;
				}
				else
				{
					valid_EID = false;
					
					sql_query = String.format("Select * From Employee Where Employee_ID = %s", E_ID);
					RS = s.executeQuery(sql_query);
				}
			//}
			
			
			//Create a shift
			//"2015-03-31" ex
			String date = d;
			if (d.length() != 10) {
				dateValid = false;
			}
			String date_year = date.substring(0,4);
			String date_month = date.substring(5,7);
			String date_day = date.substring(8,10);
			//String date = "";
			//System.out.println("========__-__========\n");
			
			//Checking input for date
			boolean validInput = false;
			//while(!validInput)
			//{
				if(date_year.length() != 4)
				{
					dateValid = false;
				}
				
				if(date_month.length() != 2)
				{
					dateValid = false;
				}
				
				if(date_day.length() != 2)
				{
					dateValid = false;
				}
				
				//Input valid move on
				if(date_year.length() == 4 && date_month.length() == 2 && date_day.length() == 2)
				{
					validInput = true;
					
					//date = String.format("'%s-%s-%s'", date_year, date_month, date_day);
					
					//System.out.println("========__-__========\n\n");
					//break;
				}
			//}
			
			//Checking input for time (Start and End)
			// -- The time checkers aren't a valid while loop - they only check one time then move on -- fix
			//System.out.println("Please enter a start and end time for the shift. Follow this format (21:15:00) (HH:MM:00)");
			String start = startTime;
			String end = endTime;
			
			if (start.length() != 8) {
				t_start = false;
			}
			if (end.length() != 8) {
				t_end = false;
			}
			
			int s_hr = 0;
			int s_mn = 0;
			int s_ss = 0;
			
			int e_hr = 0;
			int e_mn = 0;
			int e_ss = 0;
			
			validInput = false;
			//while(!validInput)
			//{
				//booleans for start and end
				t_start = false;
				t_end = false;
				
				//Checking start
				s_hr = Integer.parseInt(start.substring(0,2));
				s_mn = Integer.parseInt(start.substring(3,5));
				s_ss = Integer.parseInt(start.substring(6,8));
				

				if(s_hr > 24 || s_hr < 0 || s_mn > 60 || s_mn < 0 || s_ss > 60 || s_ss < 0)
				{
					validTime = false;
				}
				
				else if(start.length() != 8)
				{
					validTime = false;
				}
				
				//valid start time
				//if((s_hr < 24 && s_hr > 0 && s_mn < 60 && s_mn > 0 && s_ss < 60 && s_ss > 0) && (start.length() == 8))
				else
				{
					t_start = true;
				}
				
				//Checking end
				e_hr = Integer.parseInt(end.substring(0,2));
				e_mn = Integer.parseInt(end.substring(3,5));
				e_ss = Integer.parseInt(end.substring(6,8));
				
				if(e_hr > 24 || e_hr < 0 || e_mn > 60 || e_mn < 0 || e_ss > 60 || e_ss < 0)
				{
					validTime = false;
				}
				
				else if(end.length() != 8)
				{
					validTime = false;
				}
				
				//valid end time
				//if((e_hr < 24 && e_hr > 0 && e_mn < 60 && e_mn > 0 && e_ss < 60 && e_ss > 0) && (end.length() == 8))
				else
				{
					t_end = true;
				}
				
				//valid overall start and end
				if(t_start && t_end)
				{
					validInput = true;
					validTime = true;
					//break;
				}
			//}
			
			
			//Testing what was built
			//System.out.println("Testing Shift maker\n");
			//System.out.println("Date: " + date);
			//System.out.println("Start time: " + start);
			//System.out.println("End time: " + end);
			
			//Calc hours for Employee table
			//Cast to double (float) #.#
			float total_hrs = 0.0f;
			
			int hr = 0;
			int mn = 0;
			
			//try 2
			if(s_hr < e_hr)		//start hour less than end hour
			{
				if(s_mn < e_mn) //if start min is less than end min
				{
					hr = e_hr - s_hr;
					mn = e_mn - s_mn;
					total_hrs += (float)hr + (float)((double)mn/60.0);					//set total time
				}
				else // hour still less but mins not EX: start: 10:15:00 ,end: 11:10:00 Should be: 55mins
				{
					hr = e_hr - s_hr - 1;
					mn = 60 - (s_mn - e_mn);
					total_hrs += (float)hr + (float)((double)mn/60.0);					//set total time
				}
			}
			//23:30:00 - 02:40:00
			else if(s_hr > e_hr)
			{
				if(s_mn < e_mn)
				{
					hr = (24-s_hr) + e_hr;
					mn = e_mn - s_mn;
					total_hrs += (float)hr + (float)((double)mn/60.0);
				}
				else
				{
					hr = (24-s_hr) + e_hr - 1;
					mn = 60 - (s_mn - e_mn);
					total_hrs += (float)hr + (float)((double)mn/60.0);
				}
				
			}
			
			//same hour -- only mins involved
			else
			{
				hr = 0;
				mn = e_mn - s_mn;
				total_hrs += (float)((double)mn/60.0);
			}
			
			//System.out.println(total_hrs);
			
			//round the total_time to 1 decimal
			double st = Math.round(total_hrs*100.0)/100.0;
			//System.out.println(st);
			
			//Inserting the shift into Work_Times table
			String sql_insert_wt = String.format("Insert into Work_Times(Employee_ID,Date,Start_Time,End_Time,Hours,Available) values('%s','%s','%s','%s',%s,'%s')", E_ID,date,start,end,total_hrs,'Y');
			//System.out.println(sql_insert_wt);
			
			s.executeUpdate(sql_insert_wt);
			
			//update employee hours
			sql_query = String.format("Select Hours From Employee Where Employee_ID = %s", E_ID);
			RS = s.executeQuery(sql_query);
			
			if(RS.next() == true)
			{
				float curHour = RS.getFloat("Hours");
				//System.out.println(curHour);
				total_hrs += curHour;
			}

			String sql_insert_em = String.format("Update Employee Set Hours = %s Where Employee_ID = %s", total_hrs, E_ID);
			//System.out.println(sql_insert_em);
			
			s.executeUpdate(sql_insert_em);
			
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