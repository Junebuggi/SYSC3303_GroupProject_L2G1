package ElevatorProject;

import java.io.UnsupportedEncodingException;

/**
 * The Packet class contains helper functions to manipulate byte arrays. The
 * methods allow for printing byte[], combining byte arrays and a toString
 * method.
 * 
 * @author Emma Boulay
 * @version 1.00
 *
 */
public class Packet {
	private String encoding = "US-ASCII";

	/**
	 * This method returns a byte[] array in a printable format by iterating over
	 * the byte[] and adding each byte to a formatted string. For example: "[ 0x00
	 * 0x01 0x02 0x03 0x04 ]
	 * 
	 * @param bytes - The byte[] to be converted to string
	 * @return A string representation of the byte[]
	 */
	public String printByte(byte[] bytes) {
		String s = "[ ";
		for (byte b : bytes) {
			s += String.format("0x%02X ", b);
		}
		return (s + "]");
	}

	/**
	 * This method appends b2 to b1. This method is useful for combining the packet
	 * to be sent to the intermediate host from the client.
	 * 
	 * @param b1 The array to be first in the new array
	 * @param b2 The array to be second in the new array
	 * @return The resulting array: b1 + b2
	 */
	public byte[] appendArray(byte[] b1, byte[] b2) {
		int length = b1.length + b2.length;

		byte[] b3 = new byte[length];

		int i;
		for (i = 0; i < b1.length; i++) {
			b3[i] = b1[i];
		}
		for (int j = 0; i < length; i++, j++) {
			b3[i] = b2[j];
		}

		return b3;
	}

	/**
	 * This method returns a string containing the data in byte[] format and string
	 * format
	 * 
	 * @param name      The name of the object: Client, Intermediate Host or Server
	 * @param sendOrRec 1: sending, other: receiving
	 * @param data      The data to be represented
	 * @return
	 */
	public String packetToString(String name, int sendOrRec, byte[] data) {

		String type = (sendOrRec == 1) ? "sending" : "receiving";

		String msg = "";
		try {
			msg = name + ": " + type + " a packet containing:\n\tString Format:" + new String(data, encoding)
					+ "\n\tByte[] Format: " + printByte(data);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return msg;
	}

	/**
	 * Gets the encoding used in Packet
	 * 
	 * @return The encoding used. For example "US-ASCII"
	 */
	public String getEncoding() {
		return this.encoding;
	}

	public String[] parseData(byte[] data) {
		try {
			return (new String(data, encoding)).split(" ");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public byte[] toBytes(String str) {
		
		try {
			return str.getBytes(this.encoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
