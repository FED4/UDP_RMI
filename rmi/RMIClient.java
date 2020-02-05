/*
 * Created on 01-Mar-2016
 */
package rmi;

import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import common.MessageInfo;

public class RMIClient {

	public static void main(String[] args) {

		RMIServerI iRMIServer = null;

		// Check arguments for Server host and number of messages
		if (args.length < 2){
			System.out.println("Needs 2 arguments: ServerHostName/IPAddress, TotalMessageCount");
			System.exit(-1);
		}

		String urlServer = new String("rmi://" + args[0] + "/RMIServer");
		int numMessages = Integer.parseInt(args[1]);

		// TO-DO: Initialise Security Manager
		try{
			if (System.getSecurityManager() == null){
				System.setSecurityManager(new RMISecurityManager());
			}

			// TO-DO: Bind to RMIServer
			// Registry r = LocateRegistry.getRegistry(urlServer);
			// iRMIServer = (RMIServerI)r.lookup("rmi://remotehost/RMIServer");//get ref to stub
			// iRMIServer.invokemethod();//test


		}catch(RemoteException e){
			System.out.println(" Exception:" + e);
		}



		// TO-DO: Attempt to send messages the specified number of times

	}
}
