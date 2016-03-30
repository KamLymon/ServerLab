import java.io.*;
import java.util.*;
import java.net.*;
/*
Kameren Lymon
CISC 230 Jarvis
12/18/14

Class: AddressBook
Class is made to log IP Addresses and Names and create a Map of both

Constructor:
    private AddressBook()
        initializes addressBook variable, creating a new hashmap

State:
    private static final long serialVersionUID
        version number required to deserialize each class from sender to receiver

    private static AddressBook instance
        instance variable used for Singleton Pattern
     
    private Map<String, InetSocketAddress> addressBook
        hashmap used for logging IP addreses and Names

Methods:
    synchronized public static AddressBook getInstance()
        accessor method for the instance, also checks to see if instance is null
    
    public static AddressBook newInstance()
        creates a new instance

    private String convertToKey(String key)
        converts string to lower case

    synchronized public InetSocketAddress get(String name)
        gets the IP address of user

    public String[] getKeys()
        accessor method for the key

    private void put(String name, InetSocketAddress address)
        inserts a name and IP address intpo the hash map

    public void update(String name, InetSocketAddress address)
        updates the hash map 
    
    private Map<String, InetSocketAddress> getAddressBook()
        accessor method for the Map of the address book

    
    
    
    
    

*/

public class AddressBook implements Serializable
{       
    
        private Map<String, InetSocketAddress> addressBook;
	private static final long serialVersionUID = 1;
	private static AddressBook instance = null;
	
	private AddressBook() { this.addressBook = new HashMap<String, InetSocketAddress>(); }

	synchronized public static AddressBook getInstance()
        { 
            if(AddressBook.instance == null)
            {
                AddressBook.instance = new AddressBook();
            }
            
            return AddressBook.instance; 
        }

	public static AddressBook newInstance() { return AddressBook.getInstance(); }

	private String convertToKey(String key) { return key.toLowerCase(); }

	synchronized public InetSocketAddress get(String name)
	{
		return getAddressBook().get(convertToKey(name));
	}

	public String[] getKeys()
	{
		String[] result;
		result = getAddressBook().keySet().toArray(new String[0]);

		return result;
	}

	private void put(String name, InetSocketAddress address)
	{
		getAddressBook().put(name, address);
	}

	public void update(String name, InetSocketAddress address)
	{
		int    num;
		String key;

		num  = 1;
		key = convertToKey(name);

		if( getAddressBook().containsKey(key) )
		{
			while( getAddressBook().containsKey(key + num) )
			{
				num++;
			}
			put(key + num, address);
		}
		else { put(key, address); }
	}

	private Map<String, InetSocketAddress> getAddressBook()
	{
		return this.addressBook;
	}
}