package ElevatorProject;

public class Elevator implements Runnable {
	private Scheduler scheduler;
	private byte[] currentRequest;
	
	public Elevator(Scheduler scheduler) {
		this.scheduler = scheduler;
		this.currentRequest = null;

	}

	public String toString() {
		String strRequest = new String(this.currentRequest);
		String[] parsedStr = strRequest.split(" ");
		return "Time: " + parsedStr[0] + "\nFloor: " + parsedStr[1] + "\nFloor Button: " + parsedStr[2] + "\nCar Button: " + parsedStr[3];
	}

	public void run() {

		int i = 0;
		while (true) {
			synchronized (scheduler) {
				if (scheduler.isWork()) {
					this.currentRequest = (byte[])scheduler.getRequest();
					System.out.println(this.toString() + "\n");
					scheduler.acknowledgeRequest();
					i++;
					if(i == 99) {
						System.exit(0);
					}
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
					}
				}
			}
		}

	}

}
