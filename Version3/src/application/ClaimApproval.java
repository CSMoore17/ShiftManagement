package application;

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
	ArrayList<String> Giver_Name = new ArrayList<String>();
	ArrayList<String> Taker_Name = new ArrayList<String>();
	
	ArrayList<String> Date = new ArrayList<String>();
	ArrayList<String> Start_Time = new ArrayList<String>();
	ArrayList<String> End_Time = new ArrayList<String>();
	
	String eid = "";
	String sga_id = "";
	
	public ClaimApproval(Connection conn)
	{
		c = conn;
		
		try
		{
			valid = false;
			SC_ID.clear();
			EID.clear();
			SGA_ID.clear();
			Giver_Name.clear();
			Taker_Name.clear();
			Date.clear();
			Start_Time.clear();
			End_Time.clear();
			
			Statement s = c.createStatement();
			
			String sql_query = "Select * From Shift_Claim";
			
			ResultSet RS = s.executeQuery(sql_query);
			
			while(RS.next() == true)
			{
				SC_ID.add(String.valueOf(RS.getInt("SC_ID")));
				EID.add(String.valueOf(RS.getInt("Employee_ID")));
				SGA_ID.add(String.valueOf(RS.getInt("SGA_ID")));
				valid = true;
			}
			
			String sql_giver_names_getter = "";
			for(String sgaid : SGA_ID)
			{
				sql_giver_names_getter = String.format("Select Giver_Name From Shift_Give_Away Where SGA_ID=%s", sgaid);
				RS = s.executeQuery(sql_giver_names_getter);
				if(RS.next()==true)
				{
					Giver_Name.add(RS.getString("Giver_Name"));
				}
			}
			
			String sql_taker_names_getter = "";
			for(String eid : EID)
			{
				sql_taker_names_getter = String.format("Select Name From Employee Where Employee_ID=%s", eid);
				RS = s.executeQuery(sql_taker_names_getter);
				if(RS.next()==true)
				{
					Taker_Name.add(RS.getString("Name"));
				}
			}
			
			String sql_work_info = "";
			for(String sgaid : SGA_ID)
			{
				sql_work_info = String.format("Select Date,Start_Time,End_Time,Employee_ID,WK_ID From Shift_Give_Away Where SGA_ID=%s",sgaid);
				//System.out.println(sql_work_info);
				RS = s.executeQuery(sql_work_info);
				RS.next();
				Date.add(String.valueOf(RS.getDate("Date")));
				Start_Time.add(String.valueOf(RS.getTime("Start_Time")));
				End_Time.add(String.valueOf(RS.getTime("End_Time")));
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	//readEntry  :  SC_ID  - and - select / Approve - Deny
	public void Run(String SCID, boolean a)
	{
		try
		{

			
			Statement s = c.createStatement();
			
			ResultSet RS = null;
			
			String sql_query = "";
			
			if(SC_ID.size() != 0)
			{
				//show list of claims waiting for approval
				//System.out.println(SC_ID);
				//System.out.println(EID);
				//System.out.println(SGA_ID);
				//System.out.println(Giver_Name);
				//System.out.println(Taker_Name);
				
				String select = SCID;
				
				
				//Get int location in array list so other values can be taken
				int index = SC_ID.indexOf(select);
				String sgaid = SGA_ID.get(index);  //pulls the same location as the sc_id
				
				//lists just used for this case
				String date = "";
				String st = "";
				String et = "";
				
				String wkid = "";
				
				//show that SGA
				//System.out.println("SGA information from ClaimApprovalv2 - Line 183");
				sql_query = String.format("Select Date,Start_Time,End_Time,Employee_ID,WK_ID From Shift_Give_Away Where SGA_ID=%s",sgaid);
				
				RS = s.executeQuery(sql_query);
				
				RS.next();                                       //Should only be one row
				date = String.valueOf(RS.getDate("Date"));
				st = String.valueOf(RS.getTime("Start_Time"));
				et = String.valueOf(RS.getTime("End_Time"));
				wkid = String.valueOf(RS.getInt("WK_ID"));
				
				//taker eid - claim
				String teid = EID.get(index);
				
				//giver eid - give away
				//System.out.println("Getting Employee ID for Give away - LIne 207");
				String geid = String.valueOf(RS.getInt("Employee_ID"));
				
				//Getting the hours for the shift
				sql_query = String.format("Select Hours From Work_Times Where WK_ID=%s",wkid);
				
				RS = s.executeQuery(sql_query);
				
				String hrs = "";
				RS.next();
				hrs = String.valueOf(RS.getFloat("Hours"));
				
				//testing -- good to keep
				//System.out.println(String.format("Date: %s, ST: %s, ET: %s", date,st,et));
				
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
				
				//System.out.println(String.format("Giver of Shift: %s - Shift Taker: %s",giver,taker));
				
				
				//String approve = readEntry("Approve (A) / Deny (D): ");
				String approve = "";
				//System.out.println(a);
				if(a)
					approve = "A";
				else
					approve = "D";
				
				//Delete Claim and Delete Offer of SGA_ID (delete claim from taker and offer from giver)
				//Insert into Work_Times under EID of taker			-- Not done
				if(approve.toUpperCase().equals("A"))
				{
					//System.out.println("before approval");
					validAD = true;
					
					//get information to create a work_times entry
					
					String sql_delete1 = String.format("Delete From Shift_Claim Where SC_ID=%s", select);     //make sure SC_ID is getting passed and not the index of the node
					String sql_delete2 = String.format("Delete From Shift_Give_Away Where SGA_ID=%s",sgaid);
					
					String sql_insert = String.format("Insert into Work_Times(Employee_ID,Date,Start_Time,End_Time,Hours,Available) values('%s','%s','%s','%s',%s,'%s')", teid,date,st,et,hrs,'Y');
					String sql_delete3 = String.format("Delete From Work_Times Where Employee_ID=%s and WK_ID=%s", geid,wkid);
					
					s.executeUpdate(sql_delete1);
					s.executeUpdate(sql_delete2);
					s.executeUpdate(sql_delete3);
					s.executeUpdate(sql_insert);
					
					//System.out.println("Shift change Approved -- Adding shift to Employee");
				}
				//Delete Claim and Set Offer of SGA_ID to 'A' -- Not done
				if(approve.toUpperCase().equals("D"))
				{
					//System.out.println("before denial");
					validAD = true;
					
					String sql_delete = String.format("Delete From Shift_Claim Where SC_ID=%s", select);	//make sure SC_ID is getting passed and not the index of the node
					//String sql_update = "Update Shift_Give_Away Set Status='A'";
					String sql_update = String.format("Update Shift_Give_Away Set Status='A' Where SGA_ID=%s", sgaid);
					
					s.executeUpdate(sql_delete);
					s.executeUpdate(sql_update);
					
					//System.out.println("Claim Deny -- Removing from Shift Claim and reset status on Shift Give Away");
					
				}
				else
				{
					validAD = false;
				}
			}
			else
			{
				//System.out.println("No Shift Claims");
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
	
	public void Update()
	{
		try
		{
			valid = false;
			SC_ID.clear();
			EID.clear();
			SGA_ID.clear();
			Giver_Name.clear();
			Taker_Name.clear();
			Date.clear();
			Start_Time.clear();
			End_Time.clear();
			
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
				//System.out.println("While Loop Reached");
			}
			
			String sql_giver_names_getter = "";
			for(String sgaid : SGA_ID)
			{
				sql_giver_names_getter = String.format("Select Giver_Name From Shift_Give_Away Where SGA_ID=%s", sgaid);
				RS = s.executeQuery(sql_giver_names_getter);
				if(RS.next()==true)
				{
					Giver_Name.add(RS.getString("Giver_Name"));
				}
			}
			//System.out.println("For Loop 1 Reached");
			
			String sql_taker_names_getter = "";
			for(String eid : EID)
			{
				sql_taker_names_getter = String.format("Select Name From Employee Where Employee_ID=%s", eid);
				RS = s.executeQuery(sql_taker_names_getter);
				if(RS.next()==true)
				{
					Taker_Name.add(RS.getString("Name"));
				}
			}
			//System.out.println("For Loop 2 Reached");
			
			String sql_work_info = "";
			for(String sgaid : SGA_ID)
			{
				sql_work_info = String.format("Select Date,Start_Time,End_Time,Employee_ID,WK_ID From Shift_Give_Away Where SGA_ID=%s",sgaid);
				RS = s.executeQuery(sql_work_info);
				RS.next();
				Date.add(String.valueOf(RS.getDate("Date")));
				Start_Time.add(String.valueOf(RS.getTime("Start_Time")));
				End_Time.add(String.valueOf(RS.getTime("End_Time")));
			}
			//System.out.println("For Loop 3 Reached");
			
			//System.out.println(Giver_Name);
			//System.out.println(Taker_Name);
			//System.out.println(Date);
			//System.out.println(Start_Time);
			//System.out.println(End_Time);
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
}