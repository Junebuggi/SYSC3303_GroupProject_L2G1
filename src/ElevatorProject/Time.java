package ElevatorProject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/**
 * This class is used to model the passage of time. The system's start time is the timestamp
 * of the first request in floorRequest.txt. When the system timestamp's a print statement, it 
 * will invoke the getCurrentTime() method. This will measure the absolute passage of time that 
 * the program has been running for (and will account for the time multiplier) and will add this
 * to the start time from the text file. 
 * 
 *  For example: if the textfile's first request has a timestamp of "10:00:00.000" and the system has
 *  a time_multiplier of 0.1 and the getCurrentTime() method is invoked after 100 ms, then the current
 *  timestamp will be "10:00:01.000"
 * 
 * @author Emma Boulay
 *
 */
public class Time {
	//The text file with all the event requests
	private static File inputFile = new File(
			System.getProperty("user.dir") + "/src/ElevatorProject/FloorSubsystem/floorRequest.txt");
	static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
	
	//The absolute start time, the timestamp of the first input request
	public static String startTime = getStartTime();
	//The reference start time, when the program begins executing
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
	 * The start time of the system, based on the first request in the input file
	 * 
	 * @return timeStamp, in the format "HH:MM:SS.mmm"
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
	
	/**
	 * This method will return the current timestamp for the system based on the elapsed time
	 * from the first request in the input file. It will also account for the time multiplier
	 * 
	 * @return timeStamp, the current timestamp of the system in the format "HH:MM:SS.mmm"
	 */
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
