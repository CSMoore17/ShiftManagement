package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class ViewReports {

	public Connection c = null;
	
	public boolean hasReports = false;
	
	public Statement s;
	
	String deleteReport = "";
	//int currentRID = 50000000;
	
	//store all reports into arraylist
	ArrayList<String> R_ID = new ArrayList<String>();
	ArrayList<String> A_Report = new ArrayList<String>();
	ArrayList<String> Date = new ArrayList<String>();
	
	public ViewReports(Connection conn)
	{
		c = conn;
		
		try {
			
			s = c.createStatement();
			
			String sql_querey = "Select * From Reports";
			ResultSet RS = s.executeQuery(sql_querey);
			
			while(RS.next() == true)
			{
				R_ID.add(String.valueOf(RS.getInt("R_ID")));
				A_Report.add(RS.getString("A_Report"));
				Date.add(String.valueOf(RS.getDate("Date")));
				hasReports = true;
			}
			if(!hasReports)
			{
				System.out.println("-- No Reports --");
			}
			else
			{
				//testing
				System.out.println(R_ID);
				System.out.println(A_Report);
				System.out.println(Date);
			}
			
			s.close();
			
		} catch(Exception e) {
			System.out.println(e);
		}
	}
	
	//Variables that use readEntry- deleteReport
	//It's not a big issue, but i've run into a problem that could happen. Since the reports are deleted by inputing the RID number. How can I check
	//to make sure the user isn't deleting a report that isn't being show on the screen rn
	public void Run(String id)
	{
		try 
		{
			Statement s = c.createStatement();
			
			//what report to delete - can be checked with the next or previous button in the if satement down belows
			//deleteReport = readEntry("Delete Report (R_ID): ");
			
			try 
			{
				deleteReport = id;
				int test = Integer.parseInt(deleteReport); //checking to make sure input was a number
				if(!deleteReport.equals(""))
				{
					if(R_ID.contains(deleteReport))
					{
						String sql_delete = String.format("Delete from Reports where R_ID=%s", deleteReport);
						s.executeUpdate(sql_delete);
						clear();
					}
					else
					{
						System.out.println("R_ID doesn't exist");
					}
				}
				
				//update arrayLists
				clear();
				
				String sql_querey = "Select * From Reports";
				ResultSet RS = s.executeQuery(sql_querey);
				
				while(RS.next() == true)
				{
					R_ID.add(String.valueOf(RS.getInt("R_ID")));
					A_Report.add(RS.getString("A_Report"));
					Date.add(String.valueOf(RS.getDate("Date")));
					hasReports = true;
				}
				if(!hasReports)
				{
					System.out.println("-- No Reports --");
				}
				else
				{
					//testing
					System.out.println(R_ID);
					System.out.println(A_Report);
					System.out.println(Date);
				}
				
				//clear();
				
			}
			catch(NumberFormatException e)
			{
				//System.out.println(e);
				System.out.println("--No Report Deleted--");
				clear();
			}
			
			s.close();
			
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	//call once done with call -- going to try this up in run method idk if it will work or not
	public void clear()
	{
		R_ID.clear();
		A_Report.clear();
		Date.clear();
	}
	
	//button click for delete current report
	public void deleteReport(String num)
	{
		
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
