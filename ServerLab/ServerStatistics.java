import java.net.*;
import java.io.*;
import java.util.*;
import java.text.*;
/*
Kameren Lymon 
CISC 230 Jarvis
12/18/14

Class:  ServerStatistics 
     Gets statistics of the server class
Constructor:
    public ServerStatistics()
    public ServerStatistics(ServerStatistics other)
State:
    private boolean serverIsRunning;
    private int[] count;
    private static final long serialVersionUID
Methods:
    public Object clone()
        creates a shallow clone of the object
    
    public boolean serverIsRunning()
        tells if server is running

    public boolean setServerIsRunning()
        sets the server

    public int getCount(StatisticsType type)
        returns count
    
    public int incrementCount(ServerStatistics type)
        returns the increment count

Enum: StatisticsType
    Constructor:
        StatisticsType(int arrayIndex)
    State:
        private int arrayIndex
    Methods:
    	private int getIndex()
        public int getCount(ServerStatistics serverStatistics)
	public int incrementCount(ServerStatistics serverStatistics)

*/
public class ServerStatistics implements Cloneable, Serializable
{
	private boolean serverIsRunning;
	private int[] count;
	private static final long serialVersionUID = 1;

	public ServerStatistics()
	{
		this.serverIsRunning = false;
		this.count = new int[StatisticsType.values().length];

	}



	public ServerStatistics(ServerStatistics other)
	{
		this.serverIsRunning = other.serverIsRunning;
		this.count = new int[other.count.length];
		for(int i = 0; i < this.count.length; i++)
		{
			this.count[i] = other.count[i];
		}

	}


	public Object clone()
	{
		try
		{
			return super.clone();
		}
		catch(CloneNotSupportedException e){throw new RuntimeException(e.getMessage());}

	}

	public boolean serverIsRunning(){ return this.serverIsRunning;}

	public boolean setServerIsRunning(){return this.serverIsRunning = serverIsRunning;}
	public int getCount(StatisticsType type){return this.count[type.getIndex()];}

	public int incrementCount(ServerStatistics type){return type.incrementCount(this);}

	public enum StatisticsType
	{
		CommandMessageCount(0),
		ExceptionCount(3),
		InvalidMessageCount(2),
		ValidMessageCount(1);

		private int arrayIndex;
		private int getIndex() { return this.arrayIndex; }

		StatisticsType(int arrayIndex){this.arrayIndex = arrayIndex;}

		public int getCount(ServerStatistics serverStatistics)
		{
			return serverStatistics.getCount(this);

		}

		public int incrementCount(ServerStatistics serverStatistics)
		{
			int count;
			int serverStatisticsCount;

			count = serverStatistics.getCount(this);
			serverStatisticsCount = serverStatistics.count[this.getIndex()] = count + 1;

		 	 return serverStatisticsCount;

		}

	}





}