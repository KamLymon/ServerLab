import java.io.*;
import java.net.*;
/*
Kameren Lymon
CISC 230 Jarvis
12/18/14

Class: ServerCommandMessage
        Sends a server command message
Constructor:
    public ServerCommandMessage(Server.ServerCommand command,String securityCode)
State: 
    private static final long serialVersionUID = 1
    private Server.ServerCommand command
    private String securityCode
Methods:
    public Server.ServerCommand getCommand()
         returns the command
    public String getSecurityCode()
         returns the security code
    public ResponseMessage sendTo(InetSocketAddress destination)
         sends a message to client
    

*/
public class ServerCommandMessage implements Serializable, CommandMessage
{
    private static final long serialVersionUID = 1;
    private Server.ServerCommand command;
    private String securityCode;

    public ServerCommandMessage(Server.ServerCommand command,String securityCode)
    {
        this.command = command;
        this.securityCode = securityCode;
    }

    public Server.ServerCommand getCommand(){return this.command;}
    public String getSecurityCode(){return this.securityCode;}

    public ResponseMessage sendTo(InetSocketAddress destination)
    {
        Socket socket;
        ObjectOutputStream output;
        ObjectInputStream input;
        ResponseMessage response;


        response = null;

        try
        {

			socket = new Socket(destination.getAddress().getHostAddress(),destination.getPort());

			output = new ObjectOutputStream(socket.getOutputStream() );

			input = new ObjectInputStream(socket.getInputStream());

			response = (ResponseMessage)input.readObject();

			output.close();
			input.close();
			socket.close();




	}
	catch(IOException e){}
	catch(ClassNotFoundException e){}

	return response;


    }



    }





