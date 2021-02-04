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
public class Scheduler implements Runnable{

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
	 *                     subsytem
	 */
	public synchronized void putRequest(Object floorRequest) {
		while (!isWork) {
			try {
				wait();
			} catch (InterruptedException e) {
				System.err.println(e);
			}

			this.floorRequest = floorRequest;
			isWork = true;

			notifyAll();
			return;
		}
	}

	/**
	 * 
	 * @return
	 */
	public synchronized Object getRequest() {
		return 0;
	}

	/**
	 * 
	 * @return
	 */
	public synchronized boolean isWork() {
		return false;
	}
	
	@Override
	public void run() {
		while(true) {
			
		}
	}

}

}
