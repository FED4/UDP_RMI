/*
 * Created on 01-Mar-2016
 */
package rmi;

import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.RMISecurityManager;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import common.MessageInfo;

public class RMIClient {

	public static void main(String[] args) {

		RMIServerI iRMIServer = null;

		// Check arguments for Server host and number of messages
		if (args.length < 2){
			System.out.println("Needs 2 arguments: ServerHostName/IPAddress, TotalMessageCount");
			System.exit(-1);
		}

		String urlServer = new String("rmi://" + args[0] + ":1099/RMIServer");
		int numMessages = Integer.parseInt(args[1]);


		try{
			// TO-DO: Initialise Security Manager
			if (System.getSecurityManager() == null){
				System.setSecurityManager(new RMISecurityManager());
			}

			// TO-DO: Bind to RMIServer
			iRMIServer = (RMIServerI) Naming.lookup(urlServer);
		}catch(RemoteException e){
			System.out.println("in lookup: Remote:" + e);
		}catch ( NotBoundException nbe ){
			 System.out.println("in lookup: NotBoundException" +nbe);
		}catch(NullPointerException e){
			System.out.println("in lookup: NullPointerException: "+e);
		}catch(Exception e){
			System.out.println("in lookup: Exception:" + e);
		}

		// TO-DO: Attempt to send messages the specified number of times
		try{
			for(int i = 0; i < numMessages; i++){
				MessageInfo msg = new MessageInfo(numMessages,i);
				iRMIServer.receiveMessage(msg);//test
			}
		}
		catch(java.rmi.ConnectException e){
			System.out.println("in receive:java.rmi.ConnectException: "+e);
		}
		catch(Exception e){
			System.out.println("in receive:Exception: "+e);
		}


	}
}
