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

			while(!close){
				pacSize = 128;
				pacData = new byte[pacSize];
				pac = new DatagramPacket(pacData,pacSize);
				recvSoc.setSoTimeout(30000);
				recvSoc.receive(pac);
				String data = new String(pac.getData(),0,pac.getLength());
				processMessage(data);
			}
		}catch(SocketTimeoutException e){
			System.out.println("Timeout: "+e);
			//identify message loss on Timeout
			for(int i = 0; i < totalMessages; i++){
				if(receivedMessages[i] != 1){
					System.out.println(i+"th message is missing");
				}
			}
		}catch(SocketException e){
			System.out.println("Socket: "+e);
		}catch(IOException e){
			System.out.println("IO: "+e);
		}catch(Exception e){
			System.out.println("Exception: "+e);
		}

	}

	public void processMessage(String data) {

		MessageInfo msg = null;

		// TO-DO: Use the data to construct a new MessageInfo object
		try{
			  msg = new MessageInfo(cleanTextContent(data));
		}catch(Exception e){
			System.out.println("IO:" +e);
		}


		// TO-DO: On receipt of first message, initialise the receive buffer
		totalMessages = msg.totalMessages;
		if(msg.messageNum == 0){
			receivedMessages = new int[totalMessages];
		}

		// TO-DO: Log receipt of the message
		System.out.println(data);
		receivedMessages[msg.messageNum]=1;

		// TO-DO: If this is the last expected message, then identify
		//        any missing messages
		if(msg.messageNum == msg.totalMessages-1){
			close = true;
			boolean missing = false;
			for(int i = 0; i < msg.totalMessages; i++){
				if(receivedMessages[i] != 1){
					System.out.println(i+"th message is missing");
					missing = true;
				}
			}
			if(missing == false){
					System.out.println("all messages passed");
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

	private static String cleanTextContent(String text)
{
		// strips off all non-ASCII characters
		text = text.replaceAll("[^\\x00-\\x7F]", "");

		// erases all the ASCII control characters
		text = text.replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "");

		// removes non-printable characters from Unicode
		text = text.replaceAll("\\p{C}", "");

		return text.trim();
}

}
