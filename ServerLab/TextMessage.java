import java.net.*;
import java.io.*;
/*
Kameren Lymon 
CISC 230 Jarvis
12/18/14

Class: TextMessage
     Type of message
Constructor:
    public TextMessage(InetSocketAddress recipientChatServer, String senderName, InetSocketAddress senderChatServer,String text)

State:
    private String text;
    private static final long serialVersionUID;
Methods:
    public String getText()
        returns the text 

    public ResponseMessage runRequest(Socket socket)
        returns an ok response

*/
public class TextMessage extends ChatMessage
{
	private String text;
	private static final long serialVersionUID = 1;

	public TextMessage(InetSocketAddress recipientChatServer, String senderName, InetSocketAddress senderChatServer,String text)
	{
		super(recipientChatServer,senderName,senderChatServer);
		this.text = text;

	}

	public String getText() {return text;}

	public ResponseMessage runRequest(Socket socket) 
	{
		AddressBook addressbook;
		addressbook = AddressBook.getInstance();
		addressbook.update(this.getSenderName(), new InetSocketAddress(socket.getInetAddress(), 17416 ));

		System.out.println(super.getSenderName() + " " + this.text);
		return super.getOKResponse();


	}








}