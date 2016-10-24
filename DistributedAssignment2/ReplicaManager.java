import java.rmi.RemoteException; 
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class ReplicaManager implements ReplicaManagerServer {
	//List database of stops and trams
	private Map<Integer, List<Integer>> routeDB;
	private Map<Integer, List<Integer>> tramList;
    
    private String serverName;
    private int port;
    
	public static void main(String[] args) {
		//Because we create 3 instances, make ports
		//Forces specific ports in case of any clashes in testing
		if(args.length != 1)
		{
			System.out.println("remember a 4 digit port");
			System.exit(0);
		}
		if(args[0].length() != 4 | !args[0].equals("1099") & !args[0].equals("1098") & !args[0].equals("1097"))
		{
			System.out.println("Port must be either 1099, 1098, 1097");
			System.exit(0);
		}
		//Creates and binds server
		try {
			ReplicaManager server1 = new ReplicaManager("server", Integer.parseInt(args[0]));
			ReplicaManagerServer stub1 = (ReplicaManagerServer) UnicastRemoteObject.exportObject(server1, 0);
			Registry registry1 = LocateRegistry.createRegistry(Integer.parseInt(args[0]));
			registry1.bind("server", stub1);
			
			
			System.out.println("server1".toUpperCase() + " ready on Port: " + args[0]);
			
		} catch (Exception e) {
			System.out.println("server Startup".toUpperCase() + " exception: " + e.getMessage());
			e.printStackTrace();
			System.exit(0);
		}
	}
	//Initialises list and database
	private ReplicaManager(String serverName, int port) {
		super();
		this.serverName = serverName;
		this.port = port;
		routeDB = new HashMap<>();
		routeDB.put(1, Arrays.asList(1, 2, 3, 4, 5));
		routeDB.put(96, Arrays.asList(23,24,2,34,22));
		routeDB.put(101, Arrays.asList(123,11,22,34,5,4,7));
		routeDB.put(109, Arrays.asList(88,87,85,80,9,7,2,1));
		routeDB.put(112, Arrays.asList(110,123,11,22,34,33,29,4));
		tramList = new HashMap<>();
	}
	
	public Message retrieveNextStop(Message msg) throws RemoteException {
		//Method for retrieving next stop
		//Unmarshals and splits the data into appropriate object format
		RPCMessage rpcMsg = msg.unMarshal();
		String[] info = rpcMsg.getCsv_data().split(",");
		
		int routeID = Integer.parseInt(info[0]);
		int prevStop = Integer.parseInt(info[1]);
		int currStop = Integer.parseInt(info[2]);
		//Grabs next stop and prints
		int nextStop = getNextStop(routeID, prevStop, currStop, routeDB);
		System.out.println("[SERVER] Retrieve next stop on route " + routeID + " previous stop " 
				+ prevStop + " current stop: " + currStop);
		
		rpcMsg.setCsv_data(Integer.toString(nextStop));
		//Sets message type as reply
		rpcMsg.setMessageType(RPCMessage.MessageType.REPLY);
		Message msgReturn = new Message();
		msgReturn.marshal(rpcMsg);
		return msgReturn;
	}
	
	public Message updateTramLocation(Message msg) throws RemoteException {
		//Method for updating location
		RPCMessage rpcMsg = msg.unMarshal();
		//Splits data and assigns into object format
		String[] info = rpcMsg.getCsv_data().split(",");
		
		int tramID = Integer.parseInt(info[0]);
		int routeID = Integer.parseInt(info[1]);
		int stopID = Integer.parseInt(info[2]);
		//Updates location on the RM server
		int result = updateTramLoc(tramID, routeID, stopID, tramList);		
		//Sets to reply and retuns messsage
		rpcMsg.setCsv_data(Integer.toString(result));
		rpcMsg.setMessageType(RPCMessage.MessageType.REPLY);
		Message msgReturn = new Message();
		msgReturn.marshal(rpcMsg);
		return msgReturn;
	}
	
	public int getNextStop(int routeID, int prevStop, 
			//Determines next stop of tram
			int currStop, Map<Integer, List<Integer>> routeDB) {
		List<Integer> route = routeDB.get(routeID);
		int prevIndex = route.indexOf(prevStop);
		int currIndex = route.indexOf(currStop);
		//train moving to the right
		if(prevIndex < currIndex) {
			//if the current stop is the last stop
			if(currIndex == route.size() - 1) {
				return route.get(currIndex - 1);
			} else { // the current stop is not the last stop
				return route.get(currIndex + 1);
			}
		} else { //train moving to the left
			//if the current stop is the first stop
			if(currIndex == 0) {
				return route.get(currIndex + 1);
			} else {
				return route.get(currIndex - 1);
			}
		}
	}
	
	public int updateTramLoc(int tramID, int routeID, int stopID, Map<Integer, List<Integer>> tramList) {
		//updates list on the RM server
		tramList.put(tramID, Arrays.asList(routeID, stopID));
		List<Integer> theTram = tramList.get(tramID);
		System.out.printf("[" + serverName.toUpperCase() + "]: Tram %d is on route %d at station %d\n", tramID, theTram.get(0), theTram.get(1));
		return 1;
	}

	public Map<Integer, List<Integer>> getRouteDB() {
		return routeDB;
	}

	public Map<Integer, List<Integer>> getTramList() {
		return tramList;
	}
	
	
}