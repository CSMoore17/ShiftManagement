package Database;

import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime; 

public class CurrentTimeGetter_Test {

	public static void main(String[] args)
	{
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 
		DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyy-MM-dd");
		LocalDateTime now = LocalDateTime.now();  
		System.out.println(dtf.format(now));
		System.out.println(dtf2.format(now));
		
		String time = dtf.format(now);
		
		System.out.println(time);
	}
	
}
