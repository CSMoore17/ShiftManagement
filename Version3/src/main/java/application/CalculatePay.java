package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class CalculatePay {

public Connection c = null;
	
	boolean valid = false;
	
	String[] Whrs = new String[1];
	String[] Mhrs = new String[1];
	String[] Yhrs = new String[1];
	
	double Wpay = 0.0;
	double Mpay = 0.0;
	double Ypay = 0.0;
	
	public String totalWPay = "";
	public String totalMPay = "";
	public String totalYPay = "";
	
	public CalculatePay(Connection conn)
	{
		c = conn;
	}
	
	public void Run(int LID, String WK[], String MN[], String YR[])
	{
		//setting arrays 
		Whrs = WK;
		Mhrs = MN;
		Yhrs = YR;
		
		try
		{
			Statement s = c.createStatement();
			String lid = String.valueOf(LID);
			
			float payrate = 0.0f;
			
			String sql_querey = String.format("Select Hours , Payrate From Employee Where Login_ID=%s", lid);
			
			ResultSet RS = s.executeQuery(sql_querey);
			
			if(RS.next() == true)
			{
				payrate = RS.getFloat("Payrate");
			}
			else
			{
				//System.out.println("Invalid");			
			}
			
			//calc pay for each section
			Wpay = (double)payrate * Double.parseDouble(Whrs[0]);
			Mpay = (double)payrate * Double.parseDouble(Mhrs[0]);
			Ypay = (double)payrate * Double.parseDouble(Yhrs[0]);
			
			//convert to string
			totalWPay = String.valueOf(Wpay);
			totalMPay = String.valueOf(Mpay);
			totalYPay = String.valueOf(Ypay);
			
			//testing
			//System.out.println(Wpay);
			//System.out.println(Mpay);
			//System.out.println(Ypay);
			
			
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
}