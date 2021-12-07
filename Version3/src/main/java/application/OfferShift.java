package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class OfferShift {

public Connection c = null;
	
	boolean invalid = false;
	
	String Available = "";
	
	//aka Shift Give Away
	public OfferShift(Connection conn)
	{
		c = conn;
	}
	
	
	//This functionality is going to run together with the Schedule function - So employee will already be able to see the list of shifts made for them
	//The only input would be the Work_Time ID / EID and that will be used to make a insert for the Offer_Shift table that will be seen by admin to approve or decline
	
	//will need this
	//public void Runn(int WK_ID, int EID)
	public void Run(int EID, String WID)
	{
		try 
		{
			Statement s = c.createStatement();
			
			String WK_ID = WID;
			
			
			//Make sure the selected WK_ID actually exists
			//String sql_wkid = String.format("Select * From Work_Times Where Employee_ID=%s and WK_ID=%s and Available!='N'", EID, WK_ID);
			String sql_wkid = String.format("Select * From Work_Times Where Employee_ID=%s and WK_ID=%s", EID, WK_ID); //Includes all work times (Y or N)
			String sql_giver_name = String.format("Select Name From Employee Where Employee_ID=%s", EID);
			
			//testing
			//System.out.println("Testing\n" + sql_wkid);
			//System.out.println(sql_giver_name);
			
			ResultSet RS = s.executeQuery(sql_wkid);
			
			if(RS.next() == false)
			{
				//System.out.println("Shift Doesn't exist");
				invalid = true;
			}
			else
			{
				invalid = false;
				
				RS = s.executeQuery(sql_giver_name);
                RS.next();
                String name = RS.getString("Name");
				
				//Requestion sql
                String date = "";
				String ST = "";
				String ET = "";
				RS = s.executeQuery(sql_wkid);
				
				//need to store the values of the shift
				//already have EID ready
				//need Date, Start time, End time, set status to A (for Avaliable)
				//Should only be one row
				if(RS.next() == true)
				{
					date = String.valueOf(RS.getDate("Date"));
					ST = String.valueOf(RS.getTime("Start_Time"));
					ET = String.valueOf(RS.getTime("End_Time"));
					Available = String.valueOf(RS.getString("Available").substring(0,1));
				}
				
				if(Available.equals("Y"))
				{
					//add to the Shift Give Away table
					//"Insert into Login_info(username,password,type) values('%s','%s','%s')", username, password, user_type
					String sql_insert = String.format("Insert Into Shift_Give_Away(Employee_ID,Date,Start_Time,End_Time,Status,WK_ID,Giver_Name) values(%s,'%s','%s','%s','%s',%s,'%s')", EID, date, ST, ET, "A",WK_ID,name);
					String sql_update = String.format("Update Work_Times Set Available = 'N' Where Employee_ID=%s and WK_ID=%s", EID, WK_ID);
					
					try
					{
						s.executeUpdate(sql_insert);
						s.executeUpdate(sql_update);
						//System.out.println("Shift was put up for Offer - But you're currently still set for that time until it has been taken by another Employee");
					}
					catch(Exception e)
					{
						System.out.println(e);
					}
				}
				else
				{
					//System.out.println("Offer Shfit - Not able to do that");
					invalid = true;
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