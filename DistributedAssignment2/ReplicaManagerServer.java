import java.rmi.Remote; 
import java.rmi.RemoteException;

public interface ReplicaManagerServer extends Remote {

	public static final String SERVER_NAME = "server";
    public static final String HOST_NAME = "localhost";
   // public static final int PORT = 1099;
    
	public Message retrieveNextStop(Message msg) throws RemoteException;
	public Message updateTramLocation(Message msg) throws RemoteException;
    

}
