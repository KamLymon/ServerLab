import java.net.*;
import java.io.*;
/* 
Kameren Lymon 
CISC 230 Jarvis
12/18/14

Interface: 
    public interface RequestMessage
        request a message
Methods: 
    public ResponseMessage runRequest(Socket socket)

*/
public interface RequestMessage extends Message
{

	public ResponseMessage runRequest(Socket socket);
}