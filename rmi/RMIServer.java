/*
 * Created on 01-Mar-2016
 */
package rmi;

import java.net.*;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.rmi.RMISecurityManager;

import common.*;

public class RMIServer extends UnicastRemoteObject implements RMIServerI {

	private int totalMessages = -1;
	private int[] receivedMessages;

	public RMIServer() throws RemoteException {
	}

	public void receiveMessage(MessageInfo msg) throws RemoteException {

		// TO-DO: On receipt of first message, initialise the receive buffer
		if(msg.messageNum == 0){
			totalMessages = msg.totalMessages;
			receivedMessages = new int[totalMessages];
		}

		// TO-DO: Log receipt of the message
		receivedMessages[msg.messageNum] = 1;
		System.out.println(msg.messageNum);

		// TO-DO: If this is the last expected message, then identify
		//        any missing messages
		if(msg.messageNum == totalMessages - 1){
			for(int i = 0; i < totalMessages; i++){
				if(receivedMessages[i] != 1){
					System.out.println(i+"th message is missing");
				}
			}
			if(receivedMessages.length == totalMessages){
					System.out.println("all messages passed");
			}
		}
	}


	public static void main(String[] args) {

		RMIServer rmis = null;

		// TO-DO: Initialise Security Manager
		if (System.getSecurityManager() == null){
			System.setSecurityManager(new RMISecurityManager());
		}

		// TO-DO: Instantiate the server class
		try{
			rmis = new RMIServer();//get ref to stub
		}catch ( java.rmi.UnknownHostException uhe )
			{
         System.out.println("in rmis: UnknownHostException:" + uhe);
      }
      catch ( RemoteException re ){
         System.out.println("in rmis: RemoteException" + re );
      }catch (Exception e) {
				System.out.println("in rmis: Trouble:"+e);
			}



		// TO-DO: Bind to RMI registry
		try{
			String serverURL = new String("RMIServer");
		  rebindServer(serverURL,rmis);
		}
		catch (Exception e) {
			System.out.println("Trouble:"+e);
		}

		System.out.println("Server is Ready!");

	}

	protected static void rebindServer(String serverURL, RMIServer server) {
		try{
		// TO-DO:
		// Start / find the registry (hint use LocateRegistry.createRegistry(...)
		// If we *know* the registry is running we could skip this (eg run rmiregistry in the start script)
		Registry r = LocateRegistry.createRegistry(1099);
		// TO-DO:
		// Now rebind the server to the registry (rebind replaces any existing servers bound to the serverURL)
		// Note - Registry.rebind (as returned by createRegistry / getRegistry) does something similar but
		// expects different things from the URL field.
		r.rebind(serverURL,server);
		}
		catch ( RemoteException re ){
			 System.out.println("in rebind: RemoteException" + re );
		}catch(Exception e){
			System.out.println("in rebind: Exception: "+e);
		}
	}
}
