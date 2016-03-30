import java.util.*;
import java.text.*;
/*
Kameren Lymon 
CISC 230 Jarvis
12/18/14

Class: Log
    a print message that logs events happening in the client-server process

Constructor:
        public Log()
Method:
	public static void log(String message)
            prints a message
*/
public class Log
{

	public Log() {}

	public static void log(String message)
	{
		DateFormat dateFormat;
		dateFormat = new SimpleDateFormat("yyyy/MM/dd  HHmmss.mmm");
		Calendar cal = Calendar.getInstance();
		System.out.println(dateFormat.format(cal.getTime()) + " " + message);
	}



}