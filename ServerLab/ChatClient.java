import java.net.*;
import java.io.*;
import java.awt.*;
import java.util.Date;
/*

** Code written by Patrick Jarvis and Edited by to fit certain instances
Kameren Lymon
CISC 230 Jarvis
12/18/14

Class: Chat Client
           Client class that pairs with network over a socket to form two way connection
Constructor:
    public ChatClient ( String myName, int serverPort )

State:
     private String  myName
     private InetSocketAddress myServerAddress
     private Thread addressBookUpdaterThread;

Methods:
           public InetSocketAddress getMyServerAddress()
                returns local server address
	   public String getMyName
                returns myName
	   public Thread getAddressBookUpdaterThread()
                returns addressBookUpdaterThread
           public static void main (String[] args)

Nested Classes:
    public class AddressBookUpdater
        Updates address book with new users
            Constructor:
                public AddressBookUpdater(int updateIntervalInMilliSeconds) throws IOException
            State:
                private int updateIntervalInMilliSeconds;

            Methods:
                public void run()

                public static void start( ChatClient chatClient, char commandPrefix)



Enums:
    private enum Command
        +NoCommand
        +UnknownCommand
        +QUIT
        +STATISTICS
        +TEXT
        +UPDATE_ADDRESS_BOOK

        Methods:
            private static String readFromKeyboard ( String prompt )

            private static String[] parseForCommand ( String userData, char commandPrefix )

            public static String[] parseForMessage ( String userData )

            private ChatClient chatClient;
	    private String     messageData;

	    private Command ()

            abstract public void run ();

	    public  ChatClient      getChatClient()
	    private void            setChatClient  ( ChatClient chatClient )
	    public  String          getMessageData ()
	    private void            setMessageData ( String messageData )

	    public  ResponseMessage requestResponse(ChatMessage chatMessage)

             public  ResponseMessage requestResponse( Message commandMessage )
    Nested Class: ExceptionRaisedResponse
            Constructor:
                public ExceptionRaisedResponse ( Exception exception, String message)
            State:
             private Exception exception;
	     private String    message;


            Methods:
	     public int     getErrorCode()
	     public String  getMessage ()
	     public boolean isError     ()
	     public void    runResponse()









*/
public class ChatClient implements Serializable
{


	 private enum Command
	   {
	    NoCommand
	     {
	      @Override
	      public void run(){}  // user didn't enter anything.
	     },
	    UnknownCommand
	     {
	      @Override
	      public void run(){ System.out.println("Unrecognized command."); }
	     },
	    QUIT
	     {
	      @Override
	      public void run()
	      {
	 				ServerStatisticsResponseMessage response;
					 CommandMessage					 serverCommand;

					 serverCommand = new ServerCommandMessage(Server.ServerCommand.StopServer, "");

					  response = ( ServerStatisticsResponseMessage )this.requestResponse ( serverCommand );


					  //Code statistics without return object
					  System.out.println("Server is running: " + response.serverIsRunning());
					  for (ServerStatistics.StatisticsType stat : ServerStatistics.StatisticsType.values())
					  {
						System.out.println("   " + stat.name() + ": " + stat.getCount ( response ) );
		  }
	      }
	     },
	    STATISTICS
	     {
	      @Override
	      public void run()
	      {
	       ServerStatisticsResponseMessage response;
	       CommandMessage                  serverCommand;


	       serverCommand = new ServerCommandMessage(Server.ServerCommand.Statistics, "");
	       response      = ( ServerStatisticsResponseMessage ) this.requestResponse ( serverCommand );

	       System.out.println("Server is running: " + response.serverIsRunning());
	       for (ServerStatistics.StatisticsType stat : ServerStatistics.StatisticsType.values())
	       {
	        System.out.println("   " + stat.name() + ": " + stat.getCount (response ) );
	       }
	       //response.runResponse();
	      }
	     },
	    TEXT
	     {
	      @Override
	      public void run()
	      {
	       ChatClient        client;
	       TextMessage       message;
	       InetSocketAddress recipient;
	       ResponseMessage   response;
	       String[]          userData;

	       //  the user data is assumed to be in the form: to whitespace message
	       //  the start method ensures it won't be zero length after trimming.
	       userData = this.parseForMessage ( this.getMessageData() );

	       //  get the server address for this person
	       recipient = AddressBook.getInstance().get(userData[0]);
	       if ( recipient == null ) { System.out.println ( userData[0] + " is not in the address book." ); }

	       //  empty messages are not sent
	       if ( recipient != null && userData[1].length() > 0 )
	       {
	        client   = this.getChatClient();
	        if ( client.getMyServerAddress().equals(recipient) )
	          {
	            message  = new TextMessage ( client.getMyServerAddress(), client.getMyName(), recipient, userData[1]);
	          }
	          else
	          {
	           message  = new TextMessageWithEcho ( client.getMyServerAddress(), client.getMyName(), recipient, userData[1]);
	          }

	           response = this.requestResponse ( message );
	           //  eventually we will tell the response object to run itself
	 //but for now:
	           if ( response.isError() ) System.out.println(response.getMessage());
	          // response.runResponse();

	        } //if ( recipient != null && userData[1].length() > 0 )

	      	}  // run
	     },
	     UPDATE_ADDRESS_BOOK
	     {
			 @Override

			public void run()
			{
				AddressBookUpdaterMessage message;
				ResponseMessage response;

				message = new AddressBookUpdaterMessage();
				response = requestResponse(message);

				response.runResponse();



			}//run

		};//UPDATEADDRESSBOOK


	    //  a few static behaviors of the enum
	    private static String readFromKeyboard ( String prompt )
	    {
	     String userData;
	     userData = "";
	     try
	     {
	      System.out.print( prompt );
	      userData = new BufferedReader(new InputStreamReader(System.in)).readLine();
	     }
	     catch ( IOException io ) { throw new RuntimeException("Command.readFromKeyboard: " + io.getMessage()); }
	     return userData;
	    }

	    private static String[] parseForCommand ( String userData, char commandPrefix )
	    {
	     // Return a two element String array. Element zero is the text
	     // version of the command. Element one is the remainder of the
	     // String parameter. If the String parameter did not contain a
	     // command, use either of the three default commands:
	     //            NoCommand
	     //            UnknownCommand
	     //            Text

	     int loc;

	     //  force the String parameter into a proper command format
	     userData = userData.trim();
	     if ( userData.length()  == 0 )                 { userData = commandPrefix + Command.NoCommand.name(); }
	     if ( userData.charAt(0) != commandPrefix )     { userData = commandPrefix + Command.TEXT.name() + " " + userData;      }

	     userData = userData + " ";
	     loc      = userData.indexOf(" ");
	     return new String[]{ userData.substring(1, loc).trim(),userData.substring(loc+1).trim()};
	    }  // parseForCommand

	    public static String[] parseForMessage ( String userData )
	    {
	     //  return a two element String array. element zero contains the name of
	     //  the person to whom the message will be sent. element one contains
	     //  the message
	     int loc;

	     userData = userData + " ";
	     loc      = userData.indexOf(" ");
	     return new String[] { userData.substring(0, loc).trim(),userData.substring(loc+1).trim() };
	    }

	    public static void start( ChatClient chatClient, char commandPrefix)
	    {
	     String[]  userData;
	     Command   commandFromUser;


	     do
	     {
	      //  read from the user and convert their input into a command and
	// message
	      userData = Command.parseForCommand ( readFromKeyboard("> ").trim(), commandPrefix );

	      // enums are case sensitve. the user enums in Command are in upper
	 //case.
	      // change the user data to be upper case for comparison
	      try                                    { commandFromUser =Command.valueOf ( userData[0].toUpperCase() ); }
	      catch ( IllegalArgumentException iae ) { commandFromUser = Command.UnknownCommand;}

	      //  execute the command
	      commandFromUser.setChatClient  ( chatClient );
	      commandFromUser.setMessageData ( userData[1] );
	      commandFromUser.run();
	     }
	     while ( commandFromUser != Command.QUIT );
	    }

	    private ChatClient chatClient;
	    private String     messageData; // text entered by user

	    private Command () { this.chatClient = null; this.messageData = null; }

	    // some non-static behaviors
	    abstract public void run ();

	    public  ChatClient      getChatClient( )  { return this.chatClient; }
	    private void            setChatClient  ( ChatClient chatClient ) {this.chatClient = chatClient; }
	    public  String          getMessageData ( ) {return this.messageData; }
	    private void            setMessageData ( String messageData )    { this.messageData = messageData; }

	    public  ResponseMessage requestResponse( ChatMessage chatMessage )
	    {
	     //  implement the request-response protocol for the passed messgage
	     ResponseMessage response;
	     try   { response = chatMessage.send(); }
	     catch ( Exception e )  { response = new ExceptionRaisedResponse(e,"Exception raised while sending message"); }
	     return response;
	    }  // requestResponse



	    public  ResponseMessage requestResponse( Message commandMessage )
	    {
	     //  implement the request-response protocol for the passed
	 //Serializable object
	     InetSocketAddress  inetSocketAddress;
	     ObjectInputStream  input;
	     ObjectOutputStream output;
	     ResponseMessage    response;
	     Socket             socket;

	     input  = null;
	     output = null;
	     socket = null;

	     inetSocketAddress = this.getChatClient().getMyServerAddress();
	     try
	     {
	      socket  = new Socket ( inetSocketAddress.getHostString(),inetSocketAddress.getPort() );

	      output  = new ObjectOutputStream ( socket.getOutputStream() );
	   		output.writeObject(commandMessage);

	      input   = new ObjectInputStream  ( socket.getInputStream() );

	      response = (ResponseMessage) input.readObject();
	     }
	     catch ( Exception e )  { response = new ExceptionRaisedResponse(e, "Exception raised while sending command"); }
	     finally
	     {
	      try { input.close(); } catch (Exception e) {}
	      try { output.close(); } catch (Exception e) {}
	      try { socket.close(); } catch (Exception e) {}
	     }

	     return response;
	    }  // requestResponse


	    //  an inner class ( a non-static nested class ) inside the enum
	    private class ExceptionRaisedResponse implements ResponseMessage
	    {
	     private Exception exception;
	     private String    message;

	     public ExceptionRaisedResponse ( Exception exception, String message) { this.exception = exception; this.message = message; }

	     public int     getErrorCode() { return -1;   }
	     public String  getMessage ()  { return this.message + ": " +this.exception.getMessage(); }
	     public boolean isError     () { return true; }
	     public void    runResponse()  { System.out.println ( this.getMessage() ); }
	    }  // inner class

	   }  // enum



	   //  continuation of ChatClient class

	   private String            myName;
	   private InetSocketAddress myServerAddress;

	   public ChatClient ( String myName, int serverPort ) throws UnknownHostException,IOException
	   {
	    this.myName          = myName;
	    this.myServerAddress = new InetSocketAddress ( InetAddress.getLocalHost(), serverPort );
	    this.addressBookUpdaterThread = new Thread(new AddressBookUpdater(30000));
	   }

	   public InetSocketAddress getMyServerAddress() { return this.myServerAddress; }
	   public String            getMyName         () { return this.myName; }
	   public Thread getAddressBookUpdaterThread() {return this.addressBookUpdaterThread;}



	   public static void main (String[] args) throws Exception
	   {
	    ChatMessage          chatMessage;
	    ChatClient           client;
	    ServerCommandMessage command;
	    ResponseMessage      response;

	    client = new ChatClient ( "Kam", 17416 );

	    // put someone in the AddressBook
	    //AddressBook.getInstance().update ( "Me", new InetSocketAddress ( InetAddress.getLocalHost(), 17416 ) );

	    Command.start(client, '.');
   }

   private Thread addressBookUpdaterThread;

  public class AddressBookUpdater implements Runnable
   {
	   private int updateIntervalInMilliSeconds;

	   public AddressBookUpdater(int updateIntervalInMilliSeconds) throws IOException
	   {
		   this.updateIntervalInMilliSeconds = updateIntervalInMilliSeconds;
	   }

	   public void run()
	   {
		  try
		  {

			  Command.UPDATE_ADDRESS_BOOK.setChatClient(ChatClient.this);
			  while(!Thread.currentThread().isInterrupted())
			  {
				  Command.UPDATE_ADDRESS_BOOK.run();
				  Thread.currentThread().sleep(this.updateIntervalInMilliSeconds);


			  }


		  }
		  catch(Exception e){System.out.println(e.getMessage());}

	   }


   	}//ADDRESSBOOKUPDATER



}