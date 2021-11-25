package Database;

import java.io.IOException;
import java.text.*;
import java.util.*;

public class Testing {

	public static void main(String[] args)
	{
		double y = 60.0/(double)35;
		
		double x = Math.round((60.0/35.0*100.0)*10/100);
		
		x = x/10.0;
		
		//System.out.println(y);
		//System.out.println(x);
		
		/////
		
		/*String start = "10:15:00";
		String end = "15:16:00";    ///should be 
		
		int s_hr = 0;
		int s_mn = 0;
		int s_ss = 0;
		
		int e_hr = 0;
		int e_mn = 0;
		int e_ss = 0;
		
		boolean validInput = false;
		while(!validInput)
		{
			//booleans for start and end
			boolean t_start = false;
			boolean t_end = false;
			
			//Checking start
			s_hr = Integer.parseInt(start.substring(0,2));
			s_mn = Integer.parseInt(start.substring(3,5));
			s_ss = Integer.parseInt(start.substring(6,8));
			

			if(s_hr > 24 || s_hr < 0 || s_mn > 60 || s_mn < 0 || s_ss > 60 || s_ss < 0)
			{
				start = readEntry("Plese make sure the values are correct");
			}
			
			else if(start.length() != 8)
			{
				start = readEntry("Please use a valid start time");
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
				end = readEntry("Plese make sure the values are correct");
			}
			
			else if(end.length() != 8)
			{
				end = readEntry("Please use a valid start time");
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
				break;
			}
		}*/
		
		//testing math
		//float total_hrs = 0.0f;
		
		//ex Start: 10:15:00 
		//   End  : 15:45:00 or 01:15:00
		
		//Math for fraction of an hour
		//double x = Math.round((60.0/35.0*100.0)*10/100);
		//x = x/10.0;
		
		/*int hr = 0;
		int mn = 0;
		
		if(s_hr < e_hr)
		{
			hr = e_hr - s_hr;
			total_hrs += (float)hr;
			if(s_mn < e_mn)
			{
				mn = e_mn - s_mn;
				double frachour = (double)mn/60.0;
				total_hrs += (float)frachour;
			}
			else
			{
				int i = (60 - s_mn) + e_mn;
				double fh = 60.0 / (double)i;
				fh = Math.round((60.0/fh*100)*10/100);
				fh = fh/10.0;							//fractional hour
				total_hrs += (float)fh;
			}
			
		}
		else // s_hr > e_hr
		{
			hr = 24 - s_hr + e_hr;
			total_hrs += (float)hr;
			if(s_mn < e_mn)
			{
				mn = e_mn - s_mn;
				double frachour = (double)mn/60.0;
				total_hrs += (float)frachour; 
			}
			else
			{
				int i = (60 - s_mn) + e_mn;
				double fh = 60.0 / (double)i;
				fh = Math.round((60.0/fh*100)*10/100);
				fh = fh/10.0;							//fractional hour
				total_hrs += (float)fh;
			}
		}*/
		
		//Show total hrs
		
		//System.out.println(total_hrs);
		
		
		String start = "23:15:00";
		String end = "02:32:00";    ///should be 
		
		int s_hr = 0;
		int s_mn = 0;
		int s_ss = 0;
		
		int e_hr = 0;
		int e_mn = 0;
		int e_ss = 0;
		
		//Checking start
		s_hr = Integer.parseInt(start.substring(0,2));
		s_mn = Integer.parseInt(start.substring(3,5));
		s_ss = Integer.parseInt(start.substring(6,8));
		
		//Checking end
		e_hr = Integer.parseInt(end.substring(0,2));
		e_mn = Integer.parseInt(end.substring(3,5));
		e_ss = Integer.parseInt(end.substring(6,8));
		
		float total_time = 0.0f;
		
		int hr2 = 0;
		int mn2 = 0;
		
		//try 2
		if(s_hr < e_hr)		//start hour less than end hour
		{
			if(s_mn < e_mn) //if start min is less than end min
			{
				hr2 = e_hr - s_hr;
				mn2 = e_mn - s_mn;
				total_time += (float)hr2 + (float)((double)mn2/60.0);					//set total time
			}
			else // hour still less but mins not EX: start: 10:15:00 ,end: 11:10:00 Should be: 55mins
			{
				hr2 = e_hr - s_hr - 1;
				mn2 = 60 - (s_mn - e_mn);
				total_time += (float)hr2 + (float)((double)mn2/60.0);					//set total time
			}
		}
		//23:30:00 - 02:40:00
		else if(s_hr > e_hr)
		{
			if(s_mn < e_mn)
			{
				hr2 = (24-s_hr) + e_hr;
				mn2 = e_mn - s_mn;
				total_time += (float)hr2 + (float)((double)mn2/60.0);
			}
			else
			{
				hr2 = (24-s_hr) + e_hr - 1;
				mn2 = 60 - (s_mn - e_mn);
				total_time += (float)hr2 + (float)((double)mn2/60.0);
			}
			
		}
		
		//same hour -- only mins involved
		else
		{
			hr2 = 0;
			mn2 = e_mn - s_mn;
			total_time += (float)((double)mn2/60.0);
		}
		
		System.out.println(total_time);
		
		//round the total_time to 1 decimal
		double s = Math.round(total_time*10.0)/10.0;
		System.out.println(s);
		
		
		
		String test = "abc1";
		int i = Integer.parseInt(test);
		System.out.println(i);
		
		
	///////	
	}
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
