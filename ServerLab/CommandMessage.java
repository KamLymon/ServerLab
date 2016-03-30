/*
Kameren Lymon 
CISC 230 Jarvis
11/18/14

Interface: CommandMessage
        interface for Commands that we send to server
Methods:
    public Server.ServerCommand getCommand()
    public String getSecurityCode()

*/

public interface CommandMessage extends Message
{
	public Server.ServerCommand getCommand();
	public String getSecurityCode();



}