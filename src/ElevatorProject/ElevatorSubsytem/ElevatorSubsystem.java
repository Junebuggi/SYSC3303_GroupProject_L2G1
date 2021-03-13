package ElevatorProject.ElevatorSubsytem;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Arrays;

import ElevatorProject.Information;
import ElevatorProject.Network;

public class ElevatorSubsystem extends Network implements Runnable{
	
	private Elevator[] elevators;
	private int schedulerPort;
	
	public ElevatorSubsystem(int nCars, int schedulerPort, int nFloors) {
		this.schedulerPort = schedulerPort;
		sockets = new DatagramSocket[1];
		createCars(nCars, schedulerPort, nFloors);
	}
	
	public void createCars(int nCars, int schedulerPort, int nFloors) {
		elevators = new Elevator[nCars];
		
		for(int i = 0; i < nCars; i++) {
			elevators[i] = new Elevator(i+1, schedulerPort, nFloors);
			(new Thread(elevators[i], "Elevator " +(1+i))).start();

		}
	}

	
	public void setUp() {
		
		try {
			sockets[0] = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		System.out.println("ElevatorSubsystem is initializing to port: " + sockets[0].getLocalPort());
		System.out.println("Connecteing to Scheduler");
		String initMsg = ("elevatorInit " + elevators.length +  " " + sockets[0].getLocalPort());

		System.out.println(initMsg);
		rpc_send(schedulerPort, pac.toBytes(initMsg), 2000);
		
	}
	
	
	@Override
	public void run() {
		boolean running = true;
		
		setUp();
		System.out.println("Finished Setup. Now entering listening loop");
		
		while(running) {
			ReturnData returnData = receive(getSocket(0));
			send(returnData.getPort(), Network.createACK(), getSocket(0));
			String[] data = Network.pac.parseData(returnData.getData());
			//Validate packet received and add the floor to the specified elevators queue
			if(data[0].equals("floorRequest")) {
				int floor = Integer.valueOf(data[2]);
				int floorButton = Integer.valueOf(data[4]);
				int elevatorNum = Integer.valueOf(data[1]) - 1;
				
				//Add request to floorsToVisit and connect the pickup floor with the destination floor
				synchronized(elevators[elevatorNum]) {
					elevators[elevatorNum].addFloorToVisit(floor);
					elevators[elevatorNum].addDestination(floor, floorButton);
					elevators[elevatorNum].notifyAll();
				}
				

			}
		}
		
	}
	
	public static void main(String[] args) {
		
		int nFloors = Information.NUM_FLOORS;
		int nShafts = Information.NUM_FLOORS;
		int schedulerPort = Information.SCHEDULER_PORT;
		
		ElevatorSubsystem elevSys = new ElevatorSubsystem(nShafts, schedulerPort, nFloors);
		Thread elevSubThread = new Thread(elevSys, "Elevator Subsystem");
		elevSubThread.start();
	}

}
