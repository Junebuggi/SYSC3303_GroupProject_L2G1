package ElevatorProject;

public class Elevator implements Runnable {

	private Object floorRequest;
	private boolean isWork;

	/**
	 * The default constructor.
	 */
	public Elevator() {
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
