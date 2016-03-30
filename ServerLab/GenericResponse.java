import java.net.*;
import java.io.*;
/*
Kameren Lymon 
CISC 230 Jarvis
12/18/14

Class: GenericResponse
    Sends a generic response
Constructor:
    public GenericResponse()
State: 
    private int getErrorCode
    private String getMessage
    private boolean isError
    private Socket client

Methods:
    public void runResponse()
    public ResponseMessage runRequest(Socket client)
    public int getErrorCode()
    public String getMessage()
    public boolean isError() 
    
*/

public class GenericResponse implements ResponseMessage, Serializable
{

	private int getErrorCode;
	private String getMessage;
	private boolean isError;
	private Socket client;

	private static final long serialVersionUID = 1;

	public GenericResponse()
	{
		this.getErrorCode = 1;
		this.getMessage = "it worked";
		this.isError = true;
	}
	public void runResponse(){}
	public ResponseMessage runRequest(Socket client)
	{
		this.client = client;
		return (ResponseMessage)client;
	}

	public int getErrorCode(){return this.getErrorCode;}
	public String getMessage(){return this.getMessage;}
	public boolean isError(){return this.isError;}


}