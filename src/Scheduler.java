/**
 * Scheduler.java
 * 
 *
 * @author Emma Boulay
 * @author Abeer Rafiq
 * 
 * SYSC 3303 L2
 * @version 1.0
 */
public class Scheduler {
	
	private Object floorRequest;
	private boolean isWork;
	
	public Scheduler() {
		this.floorRequest = null;
		this.isWork = false;
	}
	
	/**
	 * 
	 * @param floorRequest 
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
	
	

}
