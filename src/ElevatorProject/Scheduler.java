/**
 * Scheduler.java
 * 
 * The scheduler is used to pass floorRequests from the floor subsystem to the
 * elevator subsystem
 *
 * @author Emma Boulay
 * @author Abeer Rafiq
 * 
 *         SYSC 3303 L2
 * @version 1.0
 */
package ElevatorProject;

public class Scheduler implements Runnable {

	private Object floorRequest;
	private boolean isWork;

	/**
	 * The default constructor.
	 */
	public Scheduler() {
		this.floorRequest = null;
		this.isWork = false;
	}

	/**
	 * This method puts a floor request into the scheduler. This method will return
	 * when there is space for the floor request in the scheduler.
	 * 
	 * @param floorRequest An object representing the floor request from the floor
	 *                     subsystem
	 */
	public synchronized void putRequest(Object floorRequest) {
		while (!isWork) {
			try {
				wait();
			} catch (InterruptedException e) {
				System.err.println(e);
			}

			this.floorRequest = floorRequest;
			this.isWork = true;

			notifyAll();
			return;
		}
	}

	/**
	 * 
	 * @return floorRequest
	 */
	public synchronized Object getRequest() {
		while (isWork) {
			try {
				wait();
			} catch (InterruptedException e) {
				System.err.println(e);
			}
		}

		Object floorRequest = this.floorRequest;
		this.floorRequest = null;
		this.isWork = false;
		notifyAll();
		return floorRequest;
	}

	/**
	 * 
	 * @return isWork
	 */
	public synchronized boolean isWork() {
		return this.isWork;
	}

	@Override
	public void run() {
		while (true) {

		}
	}

}
