package Database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class OfferShift {

	public Connection c = null;
	
	boolean invalid = false;
	
	//aka Shift Give Away
	public OfferShift(Connection conn)
	{
		c = conn;
	}
	
	
	//This functionality is going to run together with the Schedule function - So employee will already be able to see the list of shifts made for them
	//The only input would be the Work_Time ID / EID and that will be used to make a insert for the Offer_Shift table that will be seen by admin to approve or decline
	
	//will need this
	//public void Runn(int WK_ID, int EID)
	public void Run(int EID)
	{
		try 
		{
			Statement s = c.createStatement();
			
			String WK_ID = readEntry("Pick a Shift to Offer up (USE ITS ID): ");
			
			//Make sure the selected WK_ID actually exists
			String sql_wkid = String.format("Select * From Work_Times Where Employee_ID=%s and WK_ID=%s", EID, WK_ID);
			
			ResultSet RS = s.executeQuery(sql_wkid);
			
			if(RS.next() == false)
			{
				System.out.println("Shift Doesn't exist");
				invalid = true;
			}
			else
			{
				invalid = false;
				
				String date = "";
				String ST = "";
				String ET = "";
				
				//Requestion sql
				RS = s.executeQuery(sql_wkid);
				
				//need to store the values of the shift
				//already have EID ready
				//need Date, Start time, End time, set status to A (for Avaliable)
				if(RS.next() == true)
				{
					date = String.valueOf(RS.getDate("Date"));
					ST = String.valueOf(RS.getTime("Start_Time"));
					ET = String.valueOf(RS.getTime("End_Time"));
				}
				
				//add to the Shift Give Away table
				//"Insert into Login_info(username,password,type) values('%s','%s','%s')", username, password, user_type
				String sql_insert = String.format("Insert Into Shift_Give_Away(Employee_ID,Date,Start_Time,End_Time,Status) values(%s,'%s','%s','%s','%s')", EID, date, ST, ET, "A");
				
				
				try
				{
					s.executeUpdate(sql_insert);
					System.out.println("Shift was put up for Offer - But you're currently still set for that time until it has been taken by another Employee");
				}
				catch(Exception e)
				{
					System.out.println(e);
				}
				
				s.close();
			}
			
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
