package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;

public class HoursWorked {

public Connection c = null;
	
	boolean valid = false;
	
	String pastWk[] = new String[1];
	String pastMn[] = new String[1];
	String pastYr[] = new String[1];
	
	String pastWKhrs[] = new String[1];
	String pastMnhrs[] = new String[1];
	String pastYrhrs[] = new String[1];
	
	String EID = "";
	
	int month_days[] = {31,28,31,30,31,30,31,31,30,31,30,31};
	                  
	                                           
	
	//total overall hours -- pulled from the Employee table Hours
	public float hours = 0.0f;
	
	public HoursWorked(Connection conn)
	{
		c = conn;
	}
	
	//will just pull from the login object's value of LID
	//Date			   Start Time				  End Time
	//YYYY-MM-DD   --  YYYY-MM-DD HH:MM:SS   --   YYYY-MM-DD HH:MM:SS
	
	
	//Total hours is pulled from the Employee table Hours
	//To Calc hours per Wk, Mn, Yr we must look at Work_Times table and compare to current time and see past week (past 7 days), month (past 30 days), year(entire current year)
	public void Run(int LID)
	{
		try
		{
			Statement s = c.createStatement();
			String lid = String.valueOf(LID);
			
			hours = 0.0f;
			
			String sql_querey = String.format("Select * From Employee Where Login_ID=%s", lid);
			//String sql_querey_EID = String.format("Select Hours From Employee Where Login_ID=%s", lid);
			
			ResultSet RS = s.executeQuery(sql_querey);
			
			if(RS.next() == true)
			{
				hours = RS.getFloat("Hours");
				EID =  String.valueOf(RS.getInt("Employee_ID"));
				valid = true;
				
				//for testing
				System.out.println(hours);
			}
			else
			{
				System.out.println("Invalid");			
			}
			
			
			
			//Return the total Hours worked by current employee
			
			//Calculate for Wk, Mn, Yr
			
			//current date and time
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("YYYY/MM/dd HH:mm:ss");  
			LocalDateTime now = LocalDateTime.now();  
			
			//just date
			DateTimeFormatter ndate = DateTimeFormatter.ofPattern("YYYY/MM/dd");
			
			//format for just the current day
			DateTimeFormatter dy = DateTimeFormatter.ofPattern("dd");
			
			//format for just the current month
			DateTimeFormatter mn = DateTimeFormatter.ofPattern("MM");
			
			//format for just the current year
			DateTimeFormatter yr = DateTimeFormatter.ofPattern("YYYY");
			
			//Strings to store current day, month, year
			String cd = ndate.format(now);
			String cdy = dy.format(now);
			String cmn = mn.format(now);
			String cyr = yr.format(now);
			
			//find hours by comparing to the string values above
			
			//for past 7 days
			int curday = Integer.parseInt(cdy);
			int startday = 0;
			int startmon = 0;
			
			//date 
			String startdate = "";
			
			//for past week
			//check if curday is less than 7 then you must roll back the month 
			if(curday <= 7)
			{
				startday = month_days[Integer.parseInt(cmn)-2] - (7-curday);
				startmon--;
				startdate = String.format("2021-%s-%s", (Integer.parseInt(cmn)+startmon),startday);
			}
			else
			{
				startday = curday - 7;
				startdate = String.format("2021-%s-%s",(Integer.parseInt(cmn)),startday);
			}
			
			//testing
			System.out.print("Past WK: ");
			System.out.println(startdate);
			pastWk[0] = String.valueOf(startdate);
			
			//////////----------------------------------------
			
			int currmonth = Integer.parseInt(cmn);
			int dayInMonth = 0;
			
			
			//for past month
			//check if curmonth is greater than 1 then you must roll back the year
			if(currmonth == 1)
			{
				currmonth = 12;
				if(curday > month_days[currmonth-1])
				{
					dayInMonth = month_days[currmonth-1];
					String year = String.valueOf( Integer.parseInt(cyr) - 1);
					startdate = String.format("%s-%s-%s", year, currmonth, dayInMonth);
				}
				else
				{
					dayInMonth = curday;
					String year = String.valueOf( Integer.parseInt(cyr) - 1);
					startdate = String.format("%s-%s-%s", year, currmonth, dayInMonth);
				}
			}
			else
			{
				//go to previous month (currMonth - 1)
				//Check previous month days work if not take previous month date and make its max the new date
				currmonth--;
				if(curday > month_days[currmonth-1])
				{
					dayInMonth = month_days[currmonth-1];
					startdate = String.format("2021-%s,%s", currmonth, dayInMonth);
				}
				else
				{
					dayInMonth = curday;
					startdate = String.format("2021-%s-%s", currmonth, dayInMonth);
				}
			}
			
			//testing
			System.out.print("Past Mn: ");
			System.out.println(startdate);
			pastMn[0] = String.valueOf(startdate);
			
			//////////----------------------------------------
			
			//for past year
			int pYear = Integer.parseInt(cyr) - 1;
			
			startdate = String.format("%s-%s-%s", pYear, cmn, cdy);
			
			//testing
			System.out.print("Past Yr: ");
			System.out.println(startdate);
			pastYr[0] = String.valueOf(startdate);
			
			//String sql_phrs_wk = String.format("Select Hours From Work_Times Where Login_ID=%s and ", lid);
			//////////----------------------------------------
			
			//SELECT * FROM #Course WHERE course_date >= '2015-10-07' and course_date < '2015-10-08' course_name course_date

			//adding up the hours per each increment
			
			//Hours for past week
			String sql_hrsWk = String.format("Select Hours From Work_Times Where Employee_ID=%s and Date >='%s' and Date <'%s'", EID,pastWk[0],cd);
			
			//Hours for past month
			String sql_hrsMn = String.format("Select Hours From Work_Times Where Employee_ID=%s and Date >='%s' and Date <'%s'", EID,pastMn[0],cd);
			
			//Hours for past year
			String sql_hrsYr = String.format("Select Hours From Work_Times Where Employee_ID=%s and Date >='%s' and Date <'%s'", EID,pastYr[0],cd);
			
			//used for totals
			double totalhrs = 0.0;
			
			
			//Week  - pastWKhrs[0]
			RS = s.executeQuery(sql_hrsWk);
			
			while(RS.next() == true)
			{
				totalhrs += (double)RS.getFloat("Hours");
			}
			
			//add wh hours to pastWkhrs[0]
			pastWKhrs[0] = String.valueOf(totalhrs);
			
			//---------
			
			//Month - pastMnhrs[0]
			RS = s.executeQuery(sql_hrsMn);
			
			totalhrs = 0.0;
			
			while(RS.next() == true)
			{
				totalhrs += (double)RS.getFloat("Hours");
			}
			
			//add mn hours to pastMnhrs[0]
			pastMnhrs[0] = String.valueOf(totalhrs);
			
			//---------
			
			//Year - paskYrhrs[0]
			RS = s.executeQuery(sql_hrsYr);
			
			totalhrs = 0.0;
			
			while(RS.next() == true)
			{
				totalhrs += (double)RS.getFloat("Hours");
			}
			
			//add yr hours to pastYrhrs[0]
			pastYrhrs[0] = String.valueOf(totalhrs);
			
			
			//testing
			System.out.println(pastWKhrs[0]);
			System.out.println(pastMnhrs[0]);
			System.out.println(pastYrhrs[0]);
			
			s.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
}
