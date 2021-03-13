package ElevatorProject.SchedulerSubsystem;


import static org.junit.Assert.assertEquals;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ElevatorProject.Network;
/**
 * This is the JUnit test class for the SchedulerStateMachine class.
 * It exercises the setup process and how the scheduler handles receiving
 * arrivalSensor and floorRequest messages
 * 
 * @author Emma Boulay [Iteration 3]
 *
 */
class SchedulerStateMachineTest extends Network{
	private SchedulerStateMachine underTest;
	private Thread active, listening;
	private Scheduler scheduler;
	
	/**
	 * This method will setup the active and listening thread of the scheduler. It will then
	 * send mock floorInit and elevatorInit messages to the scheduler to exercise its setup 
	 * process
	 * 
	 * @param port The port the listening thread is listening on
	 */
	public void setUpThreads(int port) {
		int receiverPort = port;
		
		scheduler = new Scheduler("Scheduler", receiverPort, 2000);
		underTest = new SchedulerStateMachine(scheduler);
		active = new Thread(underTest, "activeThread");
		listening = new Thread(underTest, "listeningThread");
		
		active.start();
		listening.start();
		//Check that scheduler starts in WAIT_FOR_REQUEST state
		assertEquals(underTest.getState().equals(SchedulerStateMachine.State.WAIT_FOR_REQUEST),true);
		
		try {
			byte[] returnData;
			byte[] elevatorPort = "elevatorInit 1 1".getBytes(pac.getEncoding());
			byte[] floorPort1 = "floorInit 1 2".getBytes(pac.getEncoding());
			byte[] floorPort2 = "floorInit 2 2".getBytes(pac.getEncoding());
			byte[] floorInitEnd = "floorInitEnd".getBytes(pac.getEncoding());
			
			returnData = rpc_send(receiverPort, elevatorPort, 2000);
			assertEquals(Arrays.equals(returnData, "ACK".getBytes(pac.getEncoding())), true);
			
			returnData = rpc_send(receiverPort, floorPort1, 2000);
			assertEquals(Arrays.equals(returnData, "ACK".getBytes(pac.getEncoding())), true);
			
			returnData = rpc_send(receiverPort, floorPort2, 2000);
			assertEquals(Arrays.equals(returnData, "ACK".getBytes(pac.getEncoding())), true);
			
			returnData = rpc_send(receiverPort, floorInitEnd, 2000);
			assertEquals(Arrays.equals(returnData, "ACK".getBytes(pac.getEncoding())), true);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * After each test interrupt the active and listening threads in the scheduler
	 */
	@AfterEach
	public void tearDownThreads() {
		listening.interrupt();
		active.interrupt();
	}
	/**
	 * This tests if after a arrivalSensor message is received by the scheduler that
	 * it stays in WAIR_FOR_REQUEST state.
	 * 
	 * @throws Throwable byte encoding exception
	 */
	@Test
	public void testUpdateArrivals() throws Throwable{
		
		//Set up threads
		int receiverPort = 24;
		setUpThreads(receiverPort);
		//Create arrival sensor notification to send to scheduler
		byte[] arrivalSensorData = "arrivalSensor hh:mm:ss.mmm 5 1".getBytes();
		//Send arrival sensor notification to scheduler and wait for ACK
		byte[] returnData = rpc_send(receiverPort, arrivalSensorData, 2000);
		
		//Once an ACK is received, assert that it is as expected
		assertEquals(Arrays.equals(returnData, "ACK".getBytes(pac.getEncoding())), true);
		
	}
	
	/**
	 * At start up the scheduler state machine is expected to be in the 
	 * WAIT_FOR_REQUEST state
	 * @throws Throwable
	 */
	@Test
	public void testStartUp() throws Throwable{
		//Test that all threads startup as expected
		int receiverPort = 25;
		setUpThreads(receiverPort);
	}
	
	/**
	 * This test checks the state after a floorRequest is placed in the scheduler and
	 * ensures that the workRequests array is expected after the scheduler receives
	 * the request
	 * 
	 * @throws Throwable byte encoding exception
	 */
	@Test
	public void testPutWorkRequest() throws Throwable{
		//Set up threads
		int receiverPort = 26;
		setUpThreads(receiverPort);
		
		//Create floorRequest to send to scheduler
		String[] floorString = "floorRequest hh:mm:ss.mmm 5 UP 7".split(" ");
		
		//Send floorRequest to scheduler and wait for ACK
		byte[] floorRequest = "floorRequest hh:mm:ss.mmm 5 UP 7".getBytes();
		byte[] returnData = rpc_send(receiverPort, floorRequest, 2000);
		
		//floorRequest has been acknowledged. Check that its been added to the scheduler properly
		assertEquals(Arrays.equals(returnData, "ACK".getBytes(pac.getEncoding())), true);
		assertEquals(scheduler.getAllRequest().size() == 1, true);
		assertEquals(Arrays.equals(floorString, scheduler.getAllRequest().get(0)), true);
		

	}
	
}
