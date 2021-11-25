package Database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;

public class CreateReport {

	public Connection c = null;
	
	boolean reportLength = false;
	boolean reportEntered = false;
	
	public CreateReport(Connection conn)
	{
		c = conn;
	}
	
	public void Run()
	{
		//going to ask for a text input to make A_report field to put into Reports table
		//will also take current time and date
		//"Insert into Login_info(username,password,type) values('%s','%s','%s')"
		
		try 
		{
			Statement s = c.createStatement();
			
			//Report
			String report = readEntry("Type a Report: ");
			
			//Current time
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			LocalDateTime now = LocalDateTime.now(); 
			String time = dtf.format(now);
	
			String sql_report = "";
			
			//Checker for report length (<500 characters)
			if(report.length() < 500)
			{
				sql_report = String.format("Insert into Reports(A_Report,Date) values('%s','%s')", report,time);
				s.executeUpdate(sql_report);
				System.out.println("Report has been entered");
				
				reportLength = false;
				reportEntered = true;
			}
			else
			{
				System.out.println("Report is too long -- Keep under 500 characters");
				
				reportLength = true;
				reportEntered = false;
			}
			
			//Reset booleans
			reportEntered = false;
			
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
