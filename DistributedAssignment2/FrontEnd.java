import java.net.DatagramSocket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class FrontEnd implements FrontEndServer {

	private static final FrontEnd instance = new FrontEnd("server");

	private String serverName;

	public static void main(String[] args) {
		int port = FrontEndServer.PORT;
		try {
			//Creates FE server.
			FrontEnd server = getInstance();
			FrontEndServer stub = (FrontEndServer) UnicastRemoteObject.exportObject(server, 0);
			Registry registry = LocateRegistry.createRegistry(port);
			registry.bind("server", stub);
			System.out.println("Front End".toUpperCase() + " Port " + FrontEndServer.PORT);
		} catch (Exception e) {
			System.out.println("server".toUpperCase() + " exception: " + e.getMessage());
			e.printStackTrace();
		}

	}

	public static FrontEnd getInstance() {
		return instance;
	}

	private FrontEnd(String serverName) {
		super();
		this.serverName = serverName;
	}

	public Message retrieveNextStop(Message msg) throws RemoteException {
		//checks what replicator services are running, 
		ReplicaManagerServer[] serverList = listTramService();
		Message msgReturn = null;
		for (int i = 0; i < serverList.length; i++) {
			//if service is running, request a stop.
			if (serverList[i] != null) {
				msgReturn = serverList[i].retrieveNextStop(msg);
			}
		}

		return msgReturn;
	}

	
	public Message updateTramLocation(Message msg) throws RemoteException {
		//checks what replicator services are running, 
		ReplicaManagerServer[] serverList = listTramService();
		Message msgReturn = null;
		for (int i = 0; i < serverList.length; i++) {
			if (serverList[i] != null) {
				//if service is running, update tram location on server
				msgReturn = serverList[i].updateTramLocation(msg);
			}
		}

		return msgReturn;
	}

	public static ReplicaManagerServer getServerStub(String serverName, String hostName, int port) {
		// Checks registry and stub of RM servers
		Registry registry = getRegistry(hostName, port);
		if (registry == null) {
			return null;
		}
		// Checks stub
		ReplicaManagerServer stub = null;
		try {
			stub = (ReplicaManagerServer) registry.lookup(serverName);
		} catch (RemoteException e) {
		} catch (NotBoundException e) {
		} catch (Exception e) {
		}
		return stub;
	}

	public static Registry getRegistry(String hostName, int port) {
		// Checks registry of RM server
		Registry registry = null;
		try {
			registry = LocateRegistry.getRegistry(hostName, port);
		} catch (RemoteException e) {
			System.out.println("FE Server Exception: cannot create a reference to the registry");
			e.printStackTrace();
		}
		return registry;
	}

	public static ReplicaManagerServer[] listTramService() {
		//array of RM seerver details
		ReplicaManagerServer[] serverStatus = { null, null, null };

		//Checks each RM server, prints results, will have an exception and return null if server is off.
		ReplicaManagerServer serverStub1 = getServerStub(FrontEndServer.SERVER_NAME, FrontEndServer.HOST_NAME, 1099);
		if (serverStub1 == null) {
			System.out.print("RM1 is OFF |");
		} else {
			System.out.print("RM1 is ON |");
		}
		serverStatus[0] = serverStub1;

		ReplicaManagerServer serverStub2 = getServerStub(FrontEndServer.SERVER_NAME, FrontEndServer.HOST_NAME, 1098);
		if (serverStub2 == null) {
			System.out.print("RM2 is OFF |");
		} else {
			System.out.print("RM2 is ON |");
		}
		serverStatus[1] = serverStub2;

		ReplicaManagerServer serverStub3 = getServerStub(FrontEndServer.SERVER_NAME, FrontEndServer.HOST_NAME, 1097);
		if (serverStub3 == null) {
			System.out.println("RM3 is OFF |");
		} else {
			System.out.println("RM3 is ON |");
		}

		serverStatus[2] = serverStub3;

		//Returns the list of servers
		return serverStatus;

	}

}