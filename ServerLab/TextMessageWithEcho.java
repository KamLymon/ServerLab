 import java.net.*;
import java.io.*;
 /*
 Kameren Lymon 
CISC 230 Jarvis
12/18/14
 
 Class:TextMessageWithEcho
       Creates a text message object
 Constructor:
        public TextMessageWithEcho(InetSocketAddress recipientChatServer, String senderName, InetSocketAddress senderChatServer,String text)
 Methods:
    public ResponseMessage send()
        sends a  text message   
 */

public class TextMessageWithEcho extends TextMessage
{
	public TextMessageWithEcho(InetSocketAddress recipientChatServer, String senderName, InetSocketAddress senderChatServer,String text)
	{
		super(recipientChatServer,senderName, senderChatServer,text);
	}



	public ResponseMessage send() throws Exception
	{
			TextMessage echo;

			echo = new TextMessage(this.getSenderChatServer(), this.getSenderName(), this.getSenderChatServer(), this.getText());
			echo.send();
			return super.send();
	}

}