import java.net.*;
import java.io.*;
/*
Kameren Lymon 
CISC 230 Jarvis
12/18/14

Class: Server
      server class in the sever-client network
Constructor:
    public Server(int portNumber, Log message)
    
State:
    private int portNumber;
    private static Log log;
    private ServerStatistics status;
Methods: 
    private void writeLogMessage(String message)
        writes a log message
    
    public static void main(String[] args)
        main class
    public void run()
        run class for thread
    public ServerStatistics getServerStatistics()
        returns serverstatistics
    public boolean serverIsRunning()
        tells if server is running
    synchronized public ServerStatistics stopServer()
        stops the server
    public void exceptionCaught(String where,Exception exception)
        tells you where you caught an exception in runtime


Nested Classes: Connection Handler
        Recieves the message from client and handles it and checks what type of message it is and sends a response message back
    Constructor: 
        public ConnectionHandler ( Socket socket )
    State:
        private Socket socket
    Methods:
        public void run()

Enums: ServerCommand

    Methods:
        public ServerStatistics execute ( Server server )
            returns either the statistics by cloning or stops the server depending on command
 
*/

public class Server implements Runnable,Serializable,Cloneable
{
	private int portNumber;
	private static Log log;
	private ServerStatistics status;


	private void writeLogMessage(String message)
	{


		this.log.log(message);

	}



	public static void main(String[] args)
	{	Server me;

		System.out.println("I am the server");

		me = new Server(17416,log);
		me.run();


	}

	public Server(int portNumber, Log message)
	{
		this.log = message;
		this.portNumber = portNumber;


	}

	public void run()
	{
		ServerSocket theServer;
		Socket client;



	try{

		theServer = new ServerSocket(this.portNumber);
		this.log.log("server socket created");

			while(true)
			{

			try{

				client = theServer.accept();
				new Thread(new ConnectionHandler(client)).start();
				writeLogMessage("You got pinged!!");
				



			}
			catch(Exception e){writeLogMessage("Connection to server failed: " + e.getMessage());}; //creates a message thatthe server failed


			}//while

		}
		catch(Exception e){writeLogMessage("Connection to server failed: " + e.getMessage());};//creates a message thatthe server failed


	}
	public ServerStatistics getServerStatistics()
	{
		return (ServerStatistics)this.status.clone();
	}

    public boolean serverIsRunning()
    {
         return this.getServerStatistics().serverIsRunning();
    }

   synchronized public ServerStatistics stopServer()
  {
   	if(this.serverIsRunning())
   		{
   			try
   			{
   			Socket socket;

   			this.status.setServerIsRunning();

   			socket = new Socket( InetAddress.getLocalHost(), this.portNumber );
   			} 
                        catch(UnknownHostException uhe) {uhe.getMessage(); }
   			catch(IOException ioe) { ioe.getMessage(); }
   		}

   			return this.getServerStatistics();
   	}


        public void exceptionCaught(String where,Exception exception)
        {
           System.out.println("Exception" + exception + " was caught in" + where);
        }



 public class ConnectionHandler implements Runnable,Serializable
   {
    private Socket socket;

    public ConnectionHandler ( Socket socket ) { this.socket = socket; }
    public void run()
    {
     CommandMessage       commandMessage;
     ObjectInputStream    input;
     Message              message;
     ObjectOutputStream   output;
     ResponseMessage      response;
     Server.ServerCommand serverCommand;
     ServerStatistics	 stats;


     input    = null;  // required by compiler
     output   = null;  // required by compiler
     response = null;  // required by compiler and used as a flag
     System.out.println("outside first loop");
	try
     {
      input   = new ObjectInputStream ( this.socket.getInputStream() );
      message = ( Message ) input.readObject();
      System.out.println("in loop");

      // The object read from the stream has successfully been cast into a
      // Message type. We only process RequestMessages and CommandMessages.
      // Do some more casting to determine which it is.
      try
       {
        response = ((RequestMessage) message).runRequest(this.socket);
 		Server.this.updateServerStatistics(ServerStatistics.StatisticsType.ValidMessageCount);
       }
      catch ( ClassCastException cce ){System.out.println(cce.getMessage());}
      {
       try
       {
        // Well, it didn't cast into a RequestMessage, let's see if it is
        // a CommandMessage.
        commandMessage = (CommandMessage) message;
    Server.this.updateServerStatistics(ServerStatistics.StatisticsType.CommandMessageCount);

        // OK, it is a CommandMessage. For now, just print out the command
        // name.
        System.out.println("Received this command: " + commandMessage.getCommand());
        stats = commandMessage.getCommand().execute(Server.this);

        // CommandMessages always require a
 	//ServerStatisticsResponseMessage so
        // create that now.
        response = new ServerStatisticsResponseMessage(Server.this.getServerStatistics(), 0, "this is the command response message", false );
        // this is the
 //error code. we will have to work on this later
   // "this is the
// command response message",
                                                      //  false // this is
 //the isError message flag

       } // try
       catch ( ClassCastException cce1 ){System.out.println(cce1.getMessage() + "after sceond loop");}
       {
        // The object read was cast successfully as Message but is
 //neither a RequestMessage
        // nor a CommandMessage.
    Server.this.updateServerStatistics(ServerStatistics.StatisticsType.InvalidMessageCount);
       } // catch
      }  // catch

     }  // try
     catch ( ClassCastException cce ) { Server.this.exceptionCaught ("ConnectionHandler", cce); } // object read could not be cast as Message
     catch ( Exception e ) { Server.this.exceptionCaught ("ConnectionHandler", e); }   // something else happened. maybe they
// closed the socket
     finally
     {
      //  To complete our request - response protocol, we need to send back
      //  a ResponseMessage object. If the message was a RequestMessge, then
      //  the RequestMessage created the response object. If the message was
      //  a CommandMessage, then the ConnectionHandler created the response.
      //  If the response object has not been created it is either because
      //  the RequestMessage returned null or something (such as casting
 //problems)
      //  were encountered. We could send back null, but it wouldn't be very
      //  helpful to the client. We will need to talk about how to handle
      //  this situation by creating another ResponseMessage class. For now,
      //  we will just send back null. That will probably cause null pointer
      //  exceptions on the client.
      try
      {
       	if ( response == null )
       	{
       //  this is where we will create an object of the new class
      	 }
      	 output = new ObjectOutputStream(this.socket.getOutputStream());
       		output.writeObject(response);
      } // try
      catch ( Exception e ) {Server.this.exceptionCaught("ConnectionHandler finally method response processing", e); }
      try { input.close();                         } catch(Exception e){System.out.println(e.getMessage() + "in finally");}
      try { output.close();                        } catch(Exception e) {System.out.println(e.getMessage() + "in finally");}
      try { this.socket.getInputStream().close();  } catch(Exception e) {System.out.println(e.getMessage() + "in finally");}
      try { this.socket.getOutputStream().close(); } catch(Exception e){System.out.println(e.getMessage() + "in finally");}
      try { this.socket.close();                   } catch(Exception e) {System.out.println(e.getMessage() + "in finally");}

    }  // run
	}
   }  // class ConnectionHandler



public enum ServerCommand
{

  StopServer

  {

   @Override

   public ServerStatistics execute ( Server server ) { return server.stopServer(); }

  },

  Statistics

  {

   @Override

   public ServerStatistics execute ( Server server )
   {

	  	 return (ServerStatistics) server.status.clone();

	}

  };

  // no constructor required because the enum has no state variables

  abstract public ServerStatistics execute ( Server server );

}






}//class