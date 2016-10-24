
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Tram {

	private final int tramID;
	private int routeID;
	private final FrontEndServer serverStub;

	// Tram constructor
	public Tram(int id, int rId, FrontEndServer serverStub) {
		super();
		tramID = id;
		routeID = rId;
		this.serverStub = serverStub;
	}

	public int updateLocation(int stopID) throws RemoteException {
		System.out.println("[CLIENT] Tram " + tramID + ". Updating current location with server...");
		// Message to FE server, request.
		RPCMessage rm = new RPCMessage(RPCMessage.MessageType.REQUEST, 25L, 36L, 59L, (short) 69, (short) 72,
				String.format("%d,%d,%d", tramID, routeID, stopID));
		Message ms = new Message();
		ms.marshal(rm);

		Message msReturn = new Message();
		// Sends the message
		msReturn = serverStub.updateTramLocation(ms);
		RPCMessage rpcReturn = msReturn.unMarshal();
		return Integer.parseInt(rpcReturn.getCsv_data());
	}

	public int retrieveNextStop(int prevStop, int currStop) throws RemoteException {
		// Retrieving the stop.
		System.out.println("[CLIENT] Tram " + tramID + " | current route: " + routeID + " | current stop: " + currStop
				+ " | previous stop: " + prevStop + ". Retrieving next stop...");
		// Message construction
		RPCMessage rm = new RPCMessage(RPCMessage.MessageType.REQUEST, 25L, 36L, 59L, (short) 69, (short) 72,
				String.format("%d,%d,%d", routeID, prevStop, currStop));
		Message ms = new Message();
		ms.marshal(rm);

		Message msReturn = new Message();
		// Sends the message through
		msReturn = serverStub.retrieveNextStop(ms);
		RPCMessage rpcReturn = msReturn.unMarshal();
		// System.out.println(rpcReturn.toString());
		return Integer.parseInt(rpcReturn.getCsv_data());
	}

	public int getRouteID() {
		return routeID;
	}

	public void setRouteID(int routeID) {
		this.routeID = routeID;
	}

	public int getTramID() {
		return tramID;
	}

}
