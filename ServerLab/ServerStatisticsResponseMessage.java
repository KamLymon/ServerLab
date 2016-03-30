/*
Kameren Lymon 
CISC 230 Jarvis
12/18/14

Class: ServerStatisticsResponseMessage
    A message that tells about the server statistics
Consructor:
    public ServerStatisticsResponseMessage(ServerStatistics statistics, int errorCode,String responseMessage, boolean isError)

State:
    private int errorCode;
    private boolean isErrorMessage;
    private String responseMessage;
    private final static long serialVersionUID
Method:
    public void runResponse()
        
    public boolean isError()
        returns error message
    public String getMessage()
        returns response message
    public int getErrorCode()
        returns error code


*/

public class ServerStatisticsResponseMessage extends ServerStatistics implements ResponseMessage
{

	private int errorCode;
	private boolean isErrorMessage;
	private String responseMessage;
	private final static long serialVersionUID = 1;

	public ServerStatisticsResponseMessage(ServerStatistics statistics, int errorCode,String responseMessage, boolean isError)
	{       super(statistics);
		this.errorCode = errorCode;
		this.isErrorMessage = isErrorMessage;
		this.responseMessage = responseMessage;
	}
	public void runResponse(){}
	public boolean isError(){return this.isErrorMessage;}
	public String getMessage(){return this.responseMessage;}
	public int getErrorCode(){return errorCode;}

}