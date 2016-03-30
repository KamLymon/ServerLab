import java.net.*;
import java.io.*;
/*
Kameren Lymon 
CISC 230 Jarvis
12/18/14

Interface:
    public interface ResponseMessage
        sends a response message
Methods:
    public int getErrorCode()
    public String getMessage()
    public boolean isError()
    public void runResponse()


*/

public interface ResponseMessage extends Message
{
	public int getErrorCode();
	public String getMessage();
	public boolean isError();
	public void runResponse();

}
