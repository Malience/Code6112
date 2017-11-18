Requirements:
- Java 9+
- Adding Java to your path is recommended
- Admin requires a machine capable of openGL 3.0+

~~~~~Instructions~~~~~
All jars can be ran with through commandline as so -
	java -jar JARNAME.jar "ARG0" "ARG1" ...

Client Instructions:
1. Open terminal in the programs directory (On windows Shift+right click in explorer -> Open command window here)
2. Input the command     java -jar Client.jar
2.1 *OPTIONAL* additional arguments may be provided as such - 
	java -jar Client.jar "Server IP" "Server Port"
3. The Client should now be running and will probably request information,
    After inputing the starting location commands can be inputed through the commandline to control the client
   For information on what kinds of commands are available type help or ? into the terminal

SimulatedClient Instructions:
NOTE: The simulated client is for testing purposes only!
1. Open terminal in the programs directory (On windows Shift+right click in explorer -> Open command window here)
2. Input the command     java -jar SimulatedClient.jar
2.1 *OPTIONAL* additional arguments may be provided as such - 
	java -jar SimulatedClient "Server IP" "Server Port"
3. The SimulatedClient should now be running

Admin Instructions:
NOTE: Admin file supports multiple operating systems, make sure to use the appropriate file
1. Open terminal in the programs directory (On windows Shift+right click in explorer -> Open command window here)
2. Input the command     java -jar AdminXXXX.jar 
	where XXXX is your OS
2.1 *OPTIONAL* additional arguments may be provided as such - 
	java -jar AdminXXXX.jar "Server IP" "Server Port"
3. The Admin should now be running and will probably request information,
    After inputing the starting location commands can be inputed through the commandline to control the client
   For information on what kinds of commands are available type help or ? into the terminal

Server Instructions:
1. Open terminal in the programs directory (On windows Shift+right click in explorer -> Open command window here)
2. Input the command     java -jar Server.jar
2.1 *OPTIONAL* additional arguments may be provided as such - 
	java -jar Server.jar "Server Port"
3. The Client should now be running

