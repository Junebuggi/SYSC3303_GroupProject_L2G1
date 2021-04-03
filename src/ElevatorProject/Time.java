package ElevatorProject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Time {
	private static File inputFile = new File(
			System.getProperty("user.dir") + "/src/ElevatorProject/FloorSubsystem/floorRequest.txt");
	static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
	
	public static String startTime = getStartTime();
	public static String startTimeReference = sdf.format(new Date());
	
	/**
	 * This method converts the given time in the format "HH:mm:ss.SSS" to
	 * milliseconds. It is used to determine the time required to wait between
	 * requests sent to scheduler.
	 * 
	 * @param time A string representation of time in the format "HH:mm:ss.SSS"
	 * @return milliseconds The given time in milliseconds
	 */
	public static long getMilli(String time) {
		
		String[] timeArray = time.replace(".", ":").split(":");
		
		long millis = Integer.parseInt(timeArray[3]);
		long second = Integer.parseInt(timeArray[2]) * 1000;
		long minute = Integer.parseInt(timeArray[1]) * 1000 * 60;
		long hour = Integer.parseInt(timeArray[0]) * 3600000;
		
		return (millis + second + minute + hour);
	}
	
	
	/**
	 * This method will return the 
	 * @return
	 */
	public static String getStartTime() {
		try {
			Scanner scanner = new Scanner(inputFile);
			String startTime = scanner.nextLine().split(" ")[0];	
			scanner.close();
			return startTime;

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;	
	}
	
	public static String getCurrentTime() {
		
		long elapsedTime = (int) ((getMilli(sdf.format(new Date())) - getMilli(startTimeReference)) / Information.TIME_MULTIPLIER);
		int newTime = (int) (elapsedTime + getMilli(startTime));
		
		long millis = newTime % 1000;
		long second = (newTime / 1000) % 60;
		long minute = (newTime / (1000 * 60)) % 60;
		long hour = (newTime / (1000 * 60 * 60)) % 24;

		String time = String.format("%02d:%02d:%02d.%03d", hour, minute, second, millis);
		return time;
	}
	

	
	

}
