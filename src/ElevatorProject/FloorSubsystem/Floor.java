package ElevatorProject.FloorSubsystem;

import java.io.UnsupportedEncodingException;
import java.net.DatagramSocket;
import java.net.SocketException;

import ElevatorProject.DirectionLamp;
import ElevatorProject.Information;
import ElevatorProject.Network;
import ElevatorProject.FloorSubsystem.FloorButton.Direction;
import ElevatorProject.GUI.ElevatorGridGUI;
import ElevatorProject.GUI.Components.FloorButtonsComponent;

/**
 * The floor class models a floor that has nShaft elevator shafts with an up or
 * down button to request and elevator and a lamp above each shaft to signal an
 * elevator arrival.
 * 
 * @author rutvikshah
 * @author Emma Boulay [Iteration 3]
 * 
 *         SYSC 3303 L2 Group 1
 * @version 1.0
 *
 */
public class Floor extends Network implements Runnable {

	private int floorLevel;
	private FloorButton[] floorButtons; // The UP or DOWN button
	private DirectionLamp[][] dirLamps; // Each shaft has a direction lamp signaling the arrival of an elevator
	private int schedulerPort;
	private int maxFloor;
	protected FloorButtonsComponent floorButtonsGUI;
	/**
	 * The constructor method creates a Floor at floorLevel that communicates with
	 * the scheduler
	 * 
	 * @param floorLevel    The level of the floor
	 * @param maxFloor      The total number of floors in the system
	 * @param nShafts       The number of elevator shafts at the floor
	 * @param schedulerPort The port used to communicate with the scheduler
	 */
	public Floor(int floorLevel, int maxFloor, int nShafts, int schedulerPort) {
		this.floorLevel = floorLevel;
		DirectionLamp[] dirLamp;
		this.schedulerPort = schedulerPort;
		this.maxFloor = maxFloor;
		
		if(Information.gui)
			this.floorButtonsGUI = ElevatorGridGUI.floorButtons[floorLevel-1];
		
		// Create new socket to listen from scheduler. Don't worry about a port. It will
		// let coordinate with scheduler in setUp()
		try {
			sockets = new DatagramSocket[] { new DatagramSocket() };
		} catch (SocketException e) {
			e.printStackTrace();
		}

		// If this is the top floor, only have down buttons and lamps
		if (floorLevel == maxFloor) {
			floorButtons = new FloorButton[] { new FloorButton("DOWN") };
			dirLamps = new DirectionLamp[nShafts][1];
			dirLamp = new DirectionLamp[] { new DirectionLamp("DOWN") };
			// If this is the bottom floor, only have up buttons and lamps
		} else if (floorLevel == 1) {
			floorButtons = new FloorButton[] { new FloorButton("UP") };
			dirLamps = new DirectionLamp[nShafts][1];
			dirLamp = new DirectionLamp[] { new DirectionLamp("UP") };
		} else {

			floorButtons = new FloorButton[] { new FloorButton("UP"), new FloorButton("DOWN") };
			dirLamps = new DirectionLamp[nShafts][2];
			dirLamp = new DirectionLamp[] { new DirectionLamp("UP"), new DirectionLamp("DOWN") };
		}
		// Give every shaft on the floor a set of direction lamps
		for (int i = 0; i < nShafts; i++) {
			dirLamps[i] = dirLamp;
		}
	}

	/**
	 * This method returns the floor
	 * 
	 * @return The floor of the Floor instance
	 */
	public int getFloor() {
		return this.floorLevel;
	}

	/**
	 * This method returns the floor button at a specified index.
	 * 
	 * @param index If there is two buttons, UP is always index 0.
	 * @return The FloorButton at the index
	 */
	public FloorButton getFloorButton(int index) {
		return floorButtons[index];
	}

	/**
	 * This method will turn on/off a given direction lamp.
	 * 
	 * @param dir   The direction of the lamp to be turned on/off
	 * @param onOff true if turnOn and false to turnOff
	 */
	public void turnOnOffLamp(String dir, boolean onOff) {
		for (FloorButton btn : floorButtons) {
			if (btn.getDirection().equals(Direction.valueOf(dir))) {
				btn.turnOnOfButton(onOff);
			}
		}
	}

	/**
	 * This method returns the direction lamp of a given shaft on the floor
	 * 
	 * @param nCar  The elevator shaft's direction lamp we wish to return
	 * @param index If this is a middle floor, index 0 is for UP lamp and index 1 is
	 *              for DOWN lamp
	 * @return The direction lamp of the shaft at nCar
	 */
	public DirectionLamp getDirectionLamp(int nCar, int index) {
		return dirLamps[nCar][index];
	}

	/**
	 * This method will turn on/off the direction lamp of the given shaft on the
	 * floor
	 * 
	 * @param nCar  The elevator shaft's direction lamp we wish to return
	 * @param index If this is a middle floor, index 0 is for UP lamp and index 1 is
	 *              for DOWN lamp
	 * @param onOff true if turning on the lamp and false if turning off the lamp
	 */
	public void setDirectionLamp(int nCar, int index, String dir) {
		dirLamps[nCar-1][index].setLamp(dir);
	}

	/**
	 * This method prints out all the information of the floor. Including the state
	 * of its floor buttons and direction lamps.
	 * 
	 * @return a string representation of the floor
	 */
	@Override
	public String toString() {
		String str = "Floor Level: " + floorLevel + "\n";
		str += "FloorButton: \n";
		for (FloorButton btn : floorButtons) {
			str += btn.getDirection() + " : " + btn.isPressed() + "\n";
		}
		str += "Elevator Shafts:";
		for (int i = 0; i < dirLamps.length; i++) {
			str += "\nShaft " + (i + 1) + " : ";
			for (DirectionLamp lamp : dirLamps[i]) {
				str += lamp.getDirection() + " : " + lamp.getLampState() + "\t";
			}

		}
		return str;
	}

	/**
	 * This method will initialize the floor's listening port with the scheduler.
	 */
	public void setUp() {
		System.out.println("Floor " + floorLevel + " is initializing to port: " + sockets[0].getLocalPort());
		System.out.println("Connecteing to Scheduler");
		byte[] initMsg = null;
		try {
			initMsg = ("floorInit " + floorLevel + " " + sockets[0].getLocalPort()).getBytes(pac.getEncoding());
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		// Keep trying to connect with the scheduler with a 2000ms timeout
		rpc_send(schedulerPort, initMsg, 2000);

	}
	
	private int getIndex(int floor, String direction) {
		if(floor == 1 || floor == this.maxFloor) {
			return 0;
		}
		else
			return ((direction.equals("UP") ? 0 : 1));
	}

	/**
	 * Overrides the run method of the Runnable interface.
	 */
	@Override
	public synchronized void run() {
		boolean running = true;

		while (running) {
			// Wait to receive instructions from the scheduler (no timeout)
			ReturnData returnData = receive(getSocket(0));
			// Send an ACK back once received
			send(returnData.getPort(), createACK(), getSocket(0));

			String request[] = pac.parseData(returnData.getData());
			// Turn off designated directionLamp
			if (request[0].equals("floorArrival") && !request[4].equals("IDLE")) {
				if(getFloorButton(getIndex(Integer.valueOf(request[2]), request[4])).isPressed()) {
					System.out.println("Floor " + floorLevel + ": Elevator " + request[3] + " arriving at floor " + request[2] + ", " + request[4] + " button lamp turning off");
					if(Information.gui)
						floorButtonsGUI.turnOffButton(request[4]);
					turnOnOffLamp(request[4], false);
				}	
				
				//setDirectionLamp(Integer.valueOf(request[3]), getIndex(Integer.valueOf(request[2]), request[4]), "OFF");
			}
		}
	}

}