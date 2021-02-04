import java.util.Random;

public class iteration1 {
	
	private static Random rand = new Random();
	
	public static void main(String[] args) {
		Thread elevator, floor, scheduler;
		
		elevator = new Thread(new Elevator(), "Elevator");
		floor = new Thread(new Floor(), "floor");
		scheduler = new Thead(new Scheduler(), "scheduler");
		
		elevator.start();
		floor.start();
		scheduler.start();
		
		for(int i = 0; i < 100; i++) {
			System.out.println(makeElevatorRequest());
		}
		
		return;
		
	}
	
	private static String makeElevatorRequest() {
		
		String hh = String.format("%02d", rand.nextInt(24));
		String mm = String.format("%02d",rand.nextInt(60));
		String ss = String.format("%02d",rand.nextInt(60));
		String mmm = String.format("%02d",rand.nextInt(1000));
		String floor = String.valueOf(rand.nextInt(7) + 1);
		String floorButton = (new String[] {"Up", "Down"})[rand.nextInt(2)];
		String car = String.valueOf(rand.nextInt(7) + 1);
		String[] message = new String[] {hh+":"+mm+":"+ss+"."+mmm, floor, floorButton, car};
		
		//a string separated by white spaces
		return String.join(" ", message);
	}

}
