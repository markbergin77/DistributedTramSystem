# DistributedTramSystem
Assignment 2 of distributed Systems.

A Distributed tram system that uses Remote Method Invocation to communicate between different applications.

# Tram Clients:
These clients are used to simulate actual trams. Tram clients continuously update the tracking service regarding
their locations. Between each update request, each tram client sleeps for a time interval that randomly varies
between 10 to 20 seconds. This simulates the time taken by a tram to travel from one stop to another.

**Updating the location of a tram – Consists of two operations.**
1. A tram client retrieves the next stop from the tracking service.
2. Then, it updates the next stop as the current location of the tram. A request is sent to the tracking service to
perform this. The next stop and the current time should be printed on the client console before sending an
update request to the tracking service.

# Server aka. Tracking Service:

**Consists of 2 components**
*A Front End server*  that hides replication management from the client, this communicates with one or more

*Replica Managers (RM)*, providing replication transparency. 

Each RM is a replication of one service to update and search for tram locations, 
this allows for the opportunity of multicasting and overall robustness.

To Run the application:

Compile all java programs within the src folder.

Run three instances of ReplicaManager (In three different command line interfaces),
Run a single instance of the Front End Server,
Run a single instance of the TramThread file to begin the application.

** _Note, ReplicaManager servers will require a port to be passed through the commandline, this is fixed to four specific ports, 1099 1098 and 1097 respectively. The system behaves this way due to the possibility of running this application through different machines in a network (With some very slight modification)._ **
