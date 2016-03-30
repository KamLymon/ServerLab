import java.io.*;
import java.net.*;
/*
Kameren Lymon 
CISC 230 Jarvis
12/18/14

Class: AddressBookUpdaterMessage 
    Sends a message out to the user telling us the status of the enum UpdateAddressBook
Constructor:
    public AddressBookUpdaterMessage()
        sets values for serversAddressBook,errorCode,errorMessage,isError
State:
    private AddressBook serversAddressBook
    private int errorCode
    private String errorMessage  
    private boolean isError
        
Methods:
    public ResponseMessage runRequest(Socket socket)
       when ran it creates a new instance of AddressBook
    
    public String getMessage()
        accessor for errorMessage
    
    public boolean isError()
        accessor for isError

    public int getErrorCode()
        accessor for errorCode
    
*/

public class AddressBookUpdaterMessage implements RequestMessage, ResponseMessage, Serializable
{
	private AddressBook serversAddressBook;
	private int errorCode;
	private String errorMessage;
	private boolean isError;

	public AddressBookUpdaterMessage()
	{
		this.errorCode = 0;
		this.errorMessage = "";
		this.isError = false;
		this.serversAddressBook = null;
	}


	public ResponseMessage runRequest(Socket socket)
	{
		System.out.println("Received command: UpdateAddressBook");
		this.serversAddressBook = AddressBook.getInstance();
		return this;
	}


	public void runResponse()
	{
		String[] key;
		AddressBook book;

		book = AddressBook.getInstance();
		key = this.serversAddressBook.getKeys();

		for(int i = 0; i < key.length; i++)
                {
			localBook.update(key[i], this.serversAddressBook.get(keys[i]));
		}
        }


	public String getMessage(){return "Address Book Updated.";}
	public boolean isError(){return this.isError;}
	public int getErrorCode(){return this.errorCode;}

}