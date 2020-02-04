/*
 * Created on 01-Mar-2016
 */
package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Arrays;

import common.MessageInfo;

public class UDPServer {

	private DatagramSocket recvSoc;
	private int totalMessages = -1;
	private int[] receivedMessages;
	private boolean close;

	private void run() {
		int				pacSize;
		byte[]			pacData;
		DatagramPacket 	pac;

		// TO-DO: Receive the messages and process them by calling processMessage(...).
		//        Use a timeout (e.g. 30 secs) to ensure the program doesn't block forever
		try{
		pacSize = 1000;
		while(!close){
			pacData = new byte[pacSize];
			pac = new DatagramPacket(pacData,pacSize);
			recvSoc.setSoTimeout(30000);
			recvSoc.receive(pac);
		}
		String data = new String(pacData);
		processMessage(data);
		}catch(SocketException e){
			System.out.println("Socket: "+e);
		}catch(SocketTimeoutException e){
			System.out.println("Timeout: "+e);
		}

	}

	public void processMessage(String data) {

		MessageInfo msg = null;

		// TO-DO: Use the data to construct a new MessageInfo object
		try{
			  msg = new MessageInfo(data);
		}catch(Exception e){
			System.out.println("IO:" +e);
		}


		// TO-DO: On receipt of first message, initialise the receive buffer

		if(receivedMessages == null){
			int[] receivedMessages = new int[msg.messageNum];//why int
		}

		// TO-DO: Log receipt of the message
		System.out.println(data);
		totalMessages++;
		receivedMessages[msg.messageNum]=1;

		// TO-DO: If this is the last expected message, then identify
		//        any missing messages
		if(msg.messageNum == msg.totalMessages-1){
			close = true;
			for(int i = 0; i < msg.totalMessages; i++){
				if(receivedMessages[i] != 1){
					System.out.println(i+"th message is missing");
				}
			}
		}

	}


	public UDPServer(int rp) {
		// TO-DO: Initialise UDP socket for receiving data
		try{
	  recvSoc = new DatagramSocket(rp);}
		catch(SocketException e){
			System.out.println("Socket:" + e.getMessage());
		}
		// Done Initialisation
		System.out.println("UDPServer ready");
	}

	public static void main(String args[]) {
		int	recvPort;

		// Get the parameters from command line
		if (args.length < 1) {
			System.err.println("Arguments required: recv port");
			System.exit(-1);
		}
		recvPort = Integer.parseInt(args[0]);

		// TO-DO: Construct Server object and start it by calling run().
		UDPServer udpserver = new UDPServer(recvPort);
		udpserver.run();
	}

}
