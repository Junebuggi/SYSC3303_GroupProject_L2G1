package ElevatorProject.SchedulerSubsystem;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.After;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SchedulerStateMachineTest {
	private SchedulerStateMachine underTest;
	private Scheduler scheduler;
	private Thread thread;

    @BeforeEach
    public void getCommunicationData()throws Exception{
		scheduler = new Scheduler();
		SchedulerStateMachine underTest = new SchedulerStateMachine(scheduler);
		thread = new Thread(new SchedulerStateMachine(scheduler), "StateMachine");

    }
    
	public void setUpThreads() {
		scheduler = new Scheduler();
		SchedulerStateMachine underTest = new SchedulerStateMachine(scheduler);
		Thread thread = new Thread(new SchedulerStateMachine(scheduler), "StateMachine");
		thread.start();
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
		thread = new Thread(new SchedulerStateMachine(scheduler), "StateMachine");
		thread.start();
		
		assertEquals(underTest.getState().equals(SchedulerStateMachine.State.WAIT_FOR_REQUEST),true);
		
		byte[] arrivalSensorData = "arrivalSensor hh:mm:ss.mmm 5 1".getBytes();
		
		synchronized (scheduler) {
			scheduler.putRequest(arrivalSensorData);
			ArrayList<Integer> actual = scheduler.getArrivals(1);
			ArrayList<Integer> expected = new ArrayList<>();
			expected.add(5);
			assertEquals(expected.equals(actual), true);
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
		scheduler = new Scheduler();
		SchedulerStateMachine underTest = new SchedulerStateMachine(scheduler);
		Thread thread = new Thread(new SchedulerStateMachine(scheduler), "StateMachine");
		thread.start();
		assertEquals(underTest.getState().equals(SchedulerStateMachine.State.WAIT_FOR_REQUEST),true);
	}
	
	/**
	 * This test checks the state after a floorRequest is placed in the scheduler
	 * @throws Throwable
	 */
	@Test
	public void testPutWorkRequest() throws Throwable{
		scheduler = new Scheduler();
		SchedulerStateMachine underTest = new SchedulerStateMachine(scheduler);
		Thread thread = new Thread(new SchedulerStateMachine(scheduler), "StateMachine");
		thread.start();
		
		byte[] floorRequest = "floorRequest hh:mm:ss.mmm 5 UP 7".getBytes();
		scheduler.putRequest(floorRequest);
		
		scheduler.acknowledgeRequest(floorRequest);
		assertEquals(Arrays.equals(scheduler.getRequest(), floorRequest), true);
		
		assertEquals(!underTest.stateHistory.equals(SchedulerStateMachine.State.SEND_ELEVATOR_TO_FLOOR),true);
		assertEquals(underTest.getState().equals(SchedulerStateMachine.State.WAIT_FOR_REQUEST),true);
		thread.interrupt();

	}
	
}
