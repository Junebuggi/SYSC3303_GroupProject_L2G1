/**
 * ElevatorTest.java
 * 
 * A jUNIT test class to test the methods of the ElevatorSubsystem class.
 *
 * @author Hasan
 * @author Alden
 * 
 * SYSC 3303 L2 Group 1
 * @version 1.0
 */

package ElevatorProject.ElevatorSubsytem;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import ElevatorProject.Information;
import ElevatorProject.Network;
import ElevatorProject.SchedulerSubsystem.SchedulerStateMachine;

public class ElevatorTest extends Network{
	
	private int nFloors, nShafts, schedulerPort;
	private ElevatorSubsystem elevSys;
	private Thread elevSubThread;
	private SchedulerStateMachine scheduler;
	private Thread schedulerElevatorThread;
	
	/**
	 * This method is used by the tests to set up the Elevator and Scheduler threads.
	 * 
	 * @param port
	 */
	public void setUpThreads(int port) {
		
		schedulerPort = port;
		
		nFloors = Information.NUM_FLOORS;
		nShafts = Information.NUM_ELEVATORS;
		
		// Create Scheduler Subsystem
		scheduler = new SchedulerStateMachine("Scheduler", port, 2000);
		schedulerElevatorThread = new Thread(scheduler, "activeThread");
		schedulerElevatorThread.start();
		
		// Create Elevator Subsystem
		elevSys = new ElevatorSubsystem(nShafts, schedulerPort, nFloors, true);
		elevSubThread = new Thread(elevSys, "Elevator Subsystem");
		elevSubThread.start();
	}
	
	/**
	 * After each test interrupt the elevator subsystem thread 
	 */
	@AfterEach
	public void tearDownThreads() {
		schedulerElevatorThread.interrupt();
		elevSubThread.interrupt();
	}
	
	/**
	 * This test method starts up the threads and checks that 
	 * the Elevator Subsystem and the Scheduler are connected.
	 * 
	 * @throws Throwable
	 */
	@Test
	public void testConnection() throws Throwable{
		System.out.println("Test 1 - Connection");
		//Test that all threads startup as expected
		int receiverPort = 30;
		setUpThreads(receiverPort);
		
		byte[] initMsg = ("elevatorInit 1 1").getBytes(pac.getEncoding());
		sendFromNewSocket(receiverPort, initMsg);
		System.out.println("The threads are connected.");
	}
	
	/**
	 * This test method starts up the threads and checks that 
	 * all elevators are initialized to IDLE state.
	 * 
	 * @throws Throwable
	 */
	@Test
	public void testStartUp() throws Throwable{
		System.out.println("Test 2 - IDLE state");
		//Test that all threads startup as expected
		int receiverPort = 31;
		setUpThreads(receiverPort);
		
		// For each elevator in the Elevator Subsystem, check if they are all IDLE when initialized 
		Elevator[] elevators = elevSys.getElevators();
		for (Elevator carts : elevators) {
			assertEquals(carts.getState().equals(carts.getIdleState()), true);
		}

		System.out.println("All elevator are in IDLE state.");
	}
	

}
