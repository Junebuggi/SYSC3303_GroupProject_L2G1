package ElevatorProject.SchedulerSubsystem;

import java.util.Timer;
import java.util.TimerTask;
import java.awt.Toolkit;

/**
 * This class is responsible for
 * 
 * @author Emma Boulay
 *
 */
public class ElevatorFaultMonitor {
	private int waitTime;
	private Scheduler scheduler;
	private int elevator;
	private Timer timer;

	/**
	 * 
	 * @param scheduler
	 * @param waitTime
	 * @param elevator
	 */
	public ElevatorFaultMonitor(Scheduler scheduler, int waitTime, int elevator) {
		this.waitTime = waitTime;
		this.scheduler = scheduler;
		this.elevator = elevator;
		

	}
	/**
	 * 
	 */
	public void cancelTimer() {
		System.out.println("Arrival notification received. Stoping fault timer for elevator " + elevator);
		timer.cancel();
	}
	
	/**
	 * 
	 */
	public void startTimer() {
		timer = new Timer();

		System.out.println("Starting fault timer for elevator " + elevator);
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				
					System.out.println("Arrival sensor timed out. Elevator " + elevator
							+ " out of order.\nMaintenance has been notified and is on its way!");
					int elevatorFloor = scheduler.elevators.get(elevator).getFloor();
					//scheduler.elevators.remove(elevator);
					scheduler.elevators.put(elevator, scheduler.createNewElevatorReference(elevatorFloor, "Out_of_Order"));
					Toolkit.getDefaultToolkit().beep();
					timer.cancel();
			}
		};

		timer.schedule(task, (int) 1.15 * waitTime);
	}

}




