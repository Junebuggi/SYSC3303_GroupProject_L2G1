package ElevatorProject;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SchedulerStateMachineTest {
	private Thread underTest;
	private String[] threadOutput;
	private Scheduler scheduler;
	
	
    @BeforeEach
    public void getCommunicationData()throws Exception{
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        setUpThreads();

        //stop execution of threads
        Thread.sleep(1000);
        underTest.interrupt();

        threadOutput = (outContent.toString().split("\\r?\\n"));
    }
	public void setUpThreads() {
		scheduler = new Scheduler();
		underTest = new Thread(new SchedulerStateMachine(scheduler), "StateMachine");
	}
	/**
	 * This tests if after a arrivalSensor message is received by the scheduler that
	 * it stays in WAIR_FOR_REQUEST state.
	 * 
	 * @throws Throwable
	 */
	@Test
	public void testUpdateArrivals() throws Throwable{
		
		Scheduler scheduler = new Scheduler();
		SchedulerStateMachine underTest = new SchedulerStateMachine(scheduler);
		
		assertEquals(underTest.getState().equals(SchedulerStateMachine.State.WAIT_FOR_REQUEST),true);
		byte[] arrivalSensorData = "arrivalSensor hh:mm:ss.mmm 5 1".getBytes();
		
		synchronized (scheduler) {
			scheduler.putRequest(arrivalSensorData);
			ArrayList<Integer> actual = scheduler.getArrivals(1);
		}
		assertEquals(underTest.getState().equals(SchedulerStateMachine.State.WAIT_FOR_REQUEST),true);
		
		
	}
	
	/**
	 * At start up the scheduler state machine is expected to be in the 
	 * WAIT_FOR_REQUEST state
	 * @throws Throwable
	 */
	@Test
	public void testStartUp() throws Throwable{
		Scheduler scheduler = new Scheduler();
		SchedulerStateMachine underTest = new SchedulerStateMachine(scheduler);
		
		assertEquals(underTest.getState().equals(SchedulerStateMachine.State.WAIT_FOR_REQUEST),true);
	}
	
	/**
	 * This test checks the state after a floorRequest is placed in the scheduler
	 * @throws Throwable
	 */
	@Test
	public void testPutWorkRequest() throws Throwable{
		
		byte[] floorRequest = "floorRequest hh:mm:ss.mmm 5 UP 7".getBytes();
		scheduler.putRequest(floorRequest);
		assertEquals(underTest.getState().equals(SchedulerStateMachine.State.SEND_ELEVATOR_TO_FLOOR),false);		
	}
	
}
