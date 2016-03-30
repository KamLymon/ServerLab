import java.net.*;
import java.io.*;
/*
Kameren Lymon 
CISC 230 Jarvis
12/18/14

Class: GenericRequest
    Sends a generic request

Constructor:
    public ResponseMessage runRequest(Socket socket)

State:
    private static final long serialVersionUID
*/
public class GenericRequest implements RequestMessage, Serializable
{

	public ResponseMessage runRequest(Socket socket)
	{

		System.out.println("Hello");
		return new GenericResponse();
	}
	private static final long serialVersionUID = 1;




}