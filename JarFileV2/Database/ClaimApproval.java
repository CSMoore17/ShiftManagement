package Database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class ClaimApproval {

	public Connection c = null;
	
	boolean valid = false;
	boolean validAD = false;
	
	ArrayList<String> SC_ID = new ArrayList<String>();
	ArrayList<String> EID = new ArrayList<String>();
	ArrayList<String> SGA_ID = new ArrayList<String>();
	
	String eid = "";
	String sga_id = "";
	
	public ClaimApproval(Connection conn)
	{
		c = conn;
	}
	
	//readEntry select / approve
	public void Run()
	{
		try
		{
			SC_ID.clear();
			EID.clear();
			SGA_ID.clear();
			
			Statement s = c.createStatement();
			
			//show all Shift claims waiting for approval ('P')
			String sql_query = "Select * From Shift_Claim";
			
			ResultSet RS = s.executeQuery(sql_query);
			
			while(RS.next() == true)
			{
				SC_ID.add(String.valueOf(RS.getInt("SC_ID")));
				EID.add(String.valueOf(RS.getInt("Employee_ID")));
				SGA_ID.add(String.valueOf(RS.getInt("SGA_ID")));
				valid = true;
			}
			
			
			//Once added to claim shift table it can be approved by Admin user
			// -- If admin accepts claim the shift will be added to work_times with the EID to tie it to Employee 
			// -- -- claim deleted from claim shift and offer shift with that ID (deletes from claim shift and offer shift)
			// -- If admin denies claim the it will be deleted 
			// -- -- claim deleted from claim shift. Offer shift will stay up and status will turn to 'A' (available)
			
			if(valid)
			{
				//show list of claims waiting for approval
				System.out.println(SC_ID);
				System.out.println(EID);
				System.out.println(SGA_ID);
				
				String select = readEntry("Select Claim (Enter SC_ID): ");
				
				if(SC_ID.contains(select))
				{
					//Get int location in array list so other values can be taken
					int index = SC_ID.indexOf(select);
					String sgaid = SGA_ID.get(index);
					
					//lists just used for this case
					String[] date = new String[1];
					String[] st = new String[1];
					String[] et = new String[1];
					
					//show that SGA
					sql_query = String.format("Select Date,Start_Time,End_Time,Employee_ID From Shift_Give_Away Where SGA_ID=%s",sgaid);
					
					RS = s.executeQuery(sql_query);
					
					RS.next();
					date[0] = String.valueOf(RS.getDate("Date"));
					st[0] = String.valueOf(RS.getTime("Start_Time"));
					et[0] = String.valueOf(RS.getTime("End_Time"));
					
					//taker eid - claim
					String teid = EID.get(index);
					
					//giver eid - give away
					String geid = String.valueOf(RS.getInt("Employee_ID"));
					
					
					//testing -- good to keep
					System.out.println(String.format("Date: %s, ST: %s, ET: %s", date[0],st[0],et[0]));
					
					//Who is offering and who is accepting
					String giver = "";
					String taker = "";
					
					String sql_query1 = String.format("Select Name From Employee Where Employee_ID = %s",geid);
					String sql_query2 = String.format("Select Name From Employee Where Employee_ID = %s",teid);
					
					//name 1
					RS = s.executeQuery(sql_query1);
					RS.next();
					giver = RS.getString("Name");
					
					RS = s.executeQuery(sql_query2);
					RS.next();
					taker = RS.getString("Name");
					
					System.out.println(String.format("Giver of Shift: %s - Shift Taker: %s",giver,taker));
					
					
					String approve = readEntry("Approve (A) / Deny (D): ");
					
					//Delete Claim and Delete Offer of SGA_ID (delete claim from taker and offer from giver)
					//Insert into Work_Times under EID of taker			-- Not done
					if(approve.toUpperCase().equals("A"))
					{
						validAD = true;
						
						//get information to create a work_times entry
						//
					}
					//Delete Claim and Set Offer of SGA_ID to 'A' -- Not done
					if(approve.toUpperCase().equals("D"))
					{
						validAD = true;
						
					}
					else
					{
						validAD = false;
					}
				}
				else
				{
					System.out.println("Not a Valid Claim");
				}
			}
			else
			{
				System.out.println("No Shift Claims");
			}
			
			valid = false;
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
