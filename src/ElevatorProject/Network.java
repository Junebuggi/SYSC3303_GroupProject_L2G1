package ElevatorProject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * The Network superclass contains generic methods to make it easier for sending
 * and receiving packets. It is subclassed by the Client, IntermediateHost and
 * Server classes.
 * 
 * @author Emma Boulay
 * @version 1.00
 *
 */
public class Network {
	/**
	 * The ReturnData class is a simple class that is returned by the receive method
	 * so the method can return the port of the sender and the data received.
	 * 
	 * @author Emma Boulay
	 */
	public class ReturnData {
		private int port; // Port of the sender
		private byte[] data; // Data received

		ReturnData(int port, byte[] data) {
			this.port = port;
			this.data = data;
		}

		public int getPort() {
			return this.port;
		}

		public byte[] getData() {
			return this.data;
		}
	}

	protected static Packet pac = new Packet();
	protected String name;
	// An array of sockets was used to make the design more flexible for each
	// subclass
	protected DatagramSocket sockets[];
	protected static int sequenceNumber = 0;

	/**
	 * This method waits to receive a packet on the socket and when a packet
	 * arrives, it will print out information about the packet and store it in the
	 * data byte[].
	 * 
	 * @param socket The socket to wait on for an incoming packet
	 * @return returnData - The port and data received
	 * @throws IOException
	 */
	public ReturnData receive(DatagramSocket socket) {

		byte[] data = new byte[100];
		DatagramPacket receivePacket = new DatagramPacket(data, data.length);

		// Block until a datagram is received via sendReceiveSocket.
		try {
			socket.receive(receivePacket);
		} catch (IOException e) {
			if(e instanceof SocketTimeoutException) {
				return new ReturnData(-1, null);
				
			}
			e.printStackTrace();
		}

		int len = receivePacket.getLength();
		data = Arrays.copyOfRange(data, 0, len); // Remove unused 0s

		return new ReturnData(receivePacket.getPort(), data.clone());
	}
	

	/**
	 * This method prints the data to be sent and then sends the data from the
	 * socket to the receiverPort.
	 * 
	 * @param receiverPort The port address of the reciever's socket
	 * @param data         A byte[] array of the data to be sent
	 * @param socket       A DatagramSocket socket to use to send the data from
	 */
	public void send(int receiverPort, byte[] data, DatagramSocket socket) {

		// Create the packet to be sent
		DatagramPacket sendPacket = null;
		try {
			sendPacket = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), receiverPort);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.exit(1);
		}

		// Try to send the packet
		try {
			socket.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * This method uses the send method from the Network class but also creates a
	 * new socket to send from every time and then immediately closes the socket.
	 * 
	 * @param receiverPort The port to send the data to
	 * @param data         The byte[] array of the data to be sent
	 */
	public void sendFromNewSocket(int receiverPort, byte[] data) {
		DatagramSocket socket = null;
		try {
			socket = new DatagramSocket(); // IntermediateHost send to Socket
		} catch (SocketException se) { // Can't create the socket.
			se.printStackTrace();
		}

		this.send(receiverPort, data, socket);

		socket.close();

	}

	/**
	 * The classes that extend Network have an array of the various sockets they
	 * need. This method returns the indexed socket, this is needed for the send()
	 * and receive() methods.
	 * 
	 * @param i The index of the socket in the sockets array
	 * @return The indexed socket
	 */
	public DatagramSocket getSocket(int i) {
		return sockets[i];
	}

	/**
	 * This method closes all the sockets in the sockets array
	 */
	public void closeAllSockets() {
		for (DatagramSocket s : sockets) {
			s.close();
		}
	}
	public void setTimeoutAllSockets(int timeout) {
		for (DatagramSocket s: sockets) {
			try {
				s.setSoTimeout(timeout);
			} catch (SocketException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * This method is used to make Remote Procedure calls. The data is sent to
	 * the received and a response is received immediately.
	 * @param receiverPort The port of the receiver socket
	 * @param data The data to be sent
	 * @param senderSocket The socket for the data to be sent from
	 * @return the data received from the receiver
	 */
	public byte[] rpc_send(int receiverPort, byte[] data) {
		
		ReturnData returnData;
		DatagramSocket socket = null;
		
		try {
			socket = new DatagramSocket(); // IntermediateHost send to Socket
		} catch (SocketException se) { // Can't create the socket.
			se.printStackTrace();
		}
		
		send(receiverPort, data, socket);
		returnData = receive(socket);

		socket.close();
		return returnData.getData();
	}
	
	public byte[] rpc_send(int receiverPort, byte[] data, int timeout) {
		
		ReturnData returnData;
		DatagramSocket socket = null;
		
		try {
			socket = new DatagramSocket(); // IntermediateHost send to Socket
			socket.setSoTimeout(timeout);
		} catch (SocketException se) { // Can't create the socket.
			se.printStackTrace();
		}
		
		boolean timeOut = true;
		do {
			send(receiverPort, data, socket);
			returnData = receive(socket);
			
			if(returnData.getPort() != -1) {
				timeOut = false;
			}

		} while(timeOut);

		socket.close();
		return returnData.getData();
	}
	
	public byte[] rpc_send(int receiverPort, byte[] data, DatagramSocket socket) {
		
		ReturnData returnData;
		
		boolean timeOut = true;
		do {
			send(receiverPort, data, socket);
			returnData = receive(socket);
			
			if(returnData.getPort() != -1) {
				timeOut = false;
			}

		} while(timeOut);

		socket.close();
		return returnData.getData();
	}
	
	public static byte[] createACK() {
		try {
			return "ACK".getBytes(pac.getEncoding());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
}

