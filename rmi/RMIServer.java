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
		if(msg.messageNum == totalMessages){
			for(int i = 0; i < totalMessages; i++){
				if(receivedMessages[i] != 1){
					System.out.println(i+"th message is missing");
				}
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
		}catch (Exception e) {
			System.out.println("Trouble:"+e);
		}

		// TO-DO: Bind to RMI registry
		try{
			String name = new String("RMIServer");
		 rebindServer(name,rmis);
		}catch (Exception e) {
			System.out.println("Trouble:"+e);
		}

	}

	protected static void rebindServer(String serverURL, RMIServer server) {
		try{
		// TO-DO:
		// Start / find the registry (hint use LocateRegistry.createRegistry(...)
		// If we *know* the registry is running we could skip this (eg run rmiregistry in the start script)
		Registry r = LocateRegistry.getRegistry();
		// TO-DO:
		// Now rebind the server to the registry (rebind replaces any existing servers bound to the serverURL)
		// Note - Registry.rebind (as returned by createRegistry / getRegistry) does something similar but
		// expects different things from the URL field.
		r.rebind(serverURL,server);}
		catch(Exception e){
			System.out.println("Exception: "+e);
		}
	}
}
