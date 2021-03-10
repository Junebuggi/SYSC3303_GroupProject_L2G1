

/**
 * Floor.java
 * 
 * The Floor thread will read in events from an input file and will try to put a request 
 * in the scheduler while the previous request has been acknowledged.
 *
 * @author rutvikshah
 * 
 * SYSC 3303 L2 Group 1
 * @version 1.0
 */

package ElevatorProject;


import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class Floor1 extends Network implements Runnable{
	private static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
	private Random rand = new Random();
	private int floorLevel;
	private Information.directionLamp dirLamp = Information.directionLamp.NOT_PRESSED;
	private int maxFloor = 7;
	private int schedulerPort;
	private int timeout = 1000;
	
	/**
	 * Constructor class used to initialize the object of the Floor class.
	 * 
	 * @param scheduler	the schedule where the actions of the floor are passed to
	 */
	public Floor1 (int floorLevel, int schedulerPort) {
		this.floorLevel = floorLevel;
		this.schedulerPort = schedulerPort;
	}
	
	public void setDirLamp(Information.directionLamp dirLamp) {
		this.dirLamp = dirLamp;
	}
	
	
	public byte[] createFloorRequest() {
		
		
		Date now = new Date();
	    String strDate = sdf.format(now);
		int flr = rand.nextInt(this.floorLevel + 1 - 1) + 1;
		String floor = "" + flr;
		String floorButton;

		//Logic to ensure first level only has Up button and top level only down button
		if(flr == 1)
			floorButton = "UP";
		else if (flr == maxFloor)
			floorButton = "DOWN";
		else
			floorButton = (new String[] { "UP", "DOWN" })[rand.nextInt(2)];
		String car;
		
		if(floorButton.equals("UP")) {
			car = String.valueOf(rand.nextInt(maxFloor-flr) + (flr+1));
		}
		else
			car = String.valueOf(rand.nextInt(flr) + 1);
		
		String[] message = new String[] {strDate, floor, floorButton, car };
		
		try {
			return String.join(" ", message).getBytes(pac.getEncoding());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Overrides the run method of the Runnable interface.
	 */	
	@Override
	public synchronized void run() {
		while(dirLamp.equals(Information.directionLamp.NOT_PRESSED)) {
			
			byte[] floorRequest = createFloorRequest();
			String msg = "";
			for(int i = 0; i < pac.parseData(floorRequest).length; i++) {
				msg += pac.parseData(floorRequest)[i] + " ";
			}
			System.out.println(msg);
			
			
			try {
				Thread.sleep(rand.nextInt(5000) + 10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}



public static void main(String[] args) {
	Floor flr = new Floor(7, 23);
	Random rand = new Random();
	while(true) {	
		byte[] floorRequest = flr.createFloorRequest();
		String msg = "";
		for(int i = 0; i < pac.parseData(floorRequest).length; i++) {
			msg += pac.parseData(floorRequest)[i] + " ";
		}
		System.out.println(msg);
		
		
		try {
			Thread.sleep(rand.nextInt(5000) + 10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
		
	}
}



