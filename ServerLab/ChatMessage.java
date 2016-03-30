import java.net.*;
import java.io.*;
import java.util.*;


public abstract class ChatMessage implements Serializable,RequestMessage
{
    /*
        Kameren Lymon
        CISC 230 Jarvis
        12/17/14
    
    Class: ChatMessage
        Type of message that allows us to send back and forth to clients
    
    Constructor:
        public ChatMessage(InetSocketAddress recipientChatServer,String senderName, InetSocketAddress senderChatServer)
    
    State: 
        private static final long serialVersionUID
        private InetSocketAddress recipientChatServer
        private InetSocketAddress  senderChatServer
        private String senderName
    
    Methods:
        
        public ResponseMessage getOKResponse()
            returns a OK response
        
        public InetSocketAddress getRecipientChatServer()
            accessor method for recipientChatServer
    
        public String getRecipientChatServerIP()
            returns the Recipients IP address
    
        public int getRecipientPortNumber()
            return recipients portnumber
        
        public String getSenderName()
             returns senderName
        public InetSocketAddress getSenderChatServer()
            returns senderChatServer
    
        public String getSenderIP()
            returns senders IP address
           
        public int getSenderPortNumber()
            returns senders port number
    
        public ResponseMessage send()
            sends a message to the client
         
        public ResponseMessage processResponse(ResponseMessage r)
            prints put a response letting us know that the response is processing
        
    Nested Classes:
        public class EverythingOKResponse
            
    
        Constructor:
               public EverythingOKResponse(String message, int errorCode)
        State:
               private int errorCode;
               private boolean isError;
               private String message;
               private static final long serialVersionUID
		
        Methods:
                public int getErrorCode()
		public String getMessage()
		public boolean isError()
                public void runResponse()  
    */
    
	private static final long serialVersionUID = 1; //serialization number 
	private InetSocketAddress recipientChatServer;
	private InetSocketAddress  senderChatServer;
	private String senderName;



	public ChatMessage(InetSocketAddress recipientChatServer,String senderName, InetSocketAddress senderChatServer)
	{
		this.recipientChatServer = recipientChatServer;
		this.senderChatServer = senderChatServer;
		this.senderName = senderName;



	}

	public ResponseMessage getOKResponse(){return new EverythingOKResponse("You're good", 0);}

	public InetSocketAddress getRecipientChatServer(){return this.recipientChatServer;}

	public String getRecipientChatServerIP(){return this.recipientChatServer.getAddress().getHostAddress();}

	public int getRecipientPortNumber(){return this.recipientChatServer.getPort();}

	public String getSenderName(){return this.senderName;}

	public InetSocketAddress getSenderChatServer(){	return this.senderChatServer;}

	public String getSenderIP(){return this.senderChatServer.getAddress().getHostAddress();}
	public int getSenderPortNumber(){return this.senderChatServer.getPort();}

	public ResponseMessage send() throws Exception
	{
		ObjectInputStream input;
		ObjectOutputStream output;
		Message message;

		Socket socket;




			socket = new Socket(this.getRecipientChatServerIP(),this.getRecipientPortNumber());


			output = new ObjectOutputStream(socket.getOutputStream());
			output.writeObject(this);
			input = new ObjectInputStream(socket.getInputStream());



			message = (ResponseMessage)input.readObject();

			processResponse((ResponseMessage)message);
                        
			output.close();
			input.close();
			socket.close();
			return (ResponseMessage)message;


	}
	public ResponseMessage processResponse(ResponseMessage r)
	{
		System.out.println("Response is processing");


			return r;
        }

	public class EverythingOKResponse implements ResponseMessage, Serializable
	{
		
                private int errorCode;
               private boolean isError;
               private String message;
                private static final long serialVersionUID = 1;


			public EverythingOKResponse(String message, int errorCode)
			{
                           if(message == null){throw new IllegalArgumentException("must have a valid message parameter");}
                           if(errorCode < 0){throw new IllegalArgumentException("error code must be a valid integer");}
                                this.errorCode = errorCode;
				this.message = message;
				this.isError = false;
			}

		public int getErrorCode()  { return this.errorCode; }
		public String getMessage() { return this.message; }
		public boolean isError()   { return this.errorCode != 0; }

                public void runResponse() { }


	}

}//class