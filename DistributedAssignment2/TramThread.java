
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class TramThread implements Runnable {

	private Tram tram;
	private int prevStop;
	private int currStop;

	// Instance of the tram created
	public TramThread(Tram tram, int prevStop, int currStop) {
		super();
		this.tram = tram;
		this.prevStop = prevStop;
		this.currStop = currStop;
	}

	public void run() {
		for (int i = 0; i < 100; i++) {
			try {
				// Wait for random amount of time between 10, 20 seconds
				double time = 0;	
				time = 20000 - (Math.random()*10000);			
				Thread.sleep((long) time);
				// Runs tram tasks
				int nextStop = tram.retrieveNextStop(prevStop, currStop);
				System.out.println("[CLIENT] Tram: " + tram.getTramID() + " | Next top: " + nextStop);
				tram.updateLocation(nextStop);
				prevStop = currStop;
				currStop = nextStop;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {

		try {
			// Initialise server and creates trams
			FrontEndServer serverStub = getServerStub(FrontEndServer.SERVER_NAME, FrontEndServer.HOST_NAME,
					FrontEndServer.PORT);
			Runnable tramThread1 = new TramThread(new Tram(89, 1, serverStub), 2, 1);
			Runnable tramThread2 = new TramThread(new Tram(3, 101, serverStub), 11, 123);
			new Thread(tramThread1).start();
			new Thread(tramThread2).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static FrontEndServer getServerStub(String serverName, String hostName, int port) {

		Registry registry = getRegistry(hostName, port);
		if (registry == null) {
			return null;
		}

		FrontEndServer stub = null;
		// Attempts to access FE server
		try {
			stub = (FrontEndServer) registry.lookup(serverName);
		} catch (RemoteException e) {
			System.out.println("FE Server Exception: failed to communicate with registry at port " + port
					+ " when retrieving server " + serverName);
			e.printStackTrace();
		} catch (NotBoundException e) {
			System.out.println("FE Server Exception: " + serverName + " is not bound");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("FE Server Exception: " + serverName + " (general exception)");
			e.printStackTrace();
		}
		return stub;
	}

	// Grabs registry of FE server
	public static Registry getRegistry(String hostName, int port) {
		Registry registry = null;
		try {
			registry = LocateRegistry.getRegistry(hostName, port);
		} catch (RemoteException e) {
			System.out.println("FE Server Exception: cannot create a reference to the registry");
			e.printStackTrace();
		}
		return registry;
	}
}
