package ElevatorProject;

import java.io.FileWriter;
import java.io.IOException;

/**
 * This class simulates a stopwatch to measure the total elapsed time between
 * start (Object Initialization) and stop (invoking elapsedTime method)
 * 
 * @author Emma Boulay
 *
 */
public class NanoMeasure {
	 private final long startTime; //The startTime in nanoSecond
	
	public NanoMeasure() {
		this.startTime = System.nanoTime();
	}
	
	/**
	 * This method saves the elapsed time observation to a text file
	 * @param file The textfile to save the observation to
	 * @param thread The intermediateHost thread executing currently, 0 for clientThread, 1 for serverThread
	 * @param type Message type being sent by intermediateHost, "data" or "ACK"
	 */
	public void elapsedTime(FileWriter file, int thread, String type) {
		
		long endTime = System.nanoTime(); 
		long timeElapsed = endTime - startTime;

		try {
			file.write(thread + " " + timeElapsed + " " + type + "\n");
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
