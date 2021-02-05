
package ElevatorProject;

/**
 * @author rutvikshah
 *
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Floor implements Runnable{
	private final Integer NUMBER_OF_FLOORS = 7;
	private Scheduler scheduler;
	private boolean addWork;
	private boolean elevatorArrived;
	private int floorLevel;
	
	public Floor (Scheduler scheduler, int floorLevel, boolean addWork) {
		this.scheduler = scheduler;
		this.floorLevel = floorLevel;
		this.addWork = addWork;
		this.elevatorArrived = false;
	}
	
	@Override
	public synchronized void run() {
		
	}
	
	public ArrayList<ElevatorRequestData> parseInputFile(File inputFile) {
		final ArrayList<List<String>> parsedLines = new ArrayList<>();
		final ArrayList<ElevatorRequestData> elevatorRequests = new ArrayList<>();
		
		try {
			Scanner scanner = new Scanner(inputFile);
			while(scanner.hasNextLine()) {
				final String line = scanner.nextLine();
				//split each word at whitespaces and add it to the list inside parsedLines arraylist.
				parsedLines.add(Arrays.asList(line.split(" ")));
			}
			scanner.close();
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		}
		
		if (parsedLines.size() > 0) {
			try {
				for (int i=0; i < parsedLines.size(); i++) {
					elevatorRequests.add(this.changeToObj(parsedLines.get(i)));
				}
			} catch (Exception ex) {
				System.out.println("file parsing error, please check input file.");
			}
		}
		return elevatorRequests;
		
	}
	
	public ElevatorRequestData changeToObj(List<String> requestLine) throws Exception {
		//0: timestamp; 1: floor; 2: floorButton; 3: carButton
		
		SimpleDateFormat timestamp = new SimpleDateFormat("HH:mm:ss");
		//generate a Time object from the given time string from input file
		String formatTime = (timestamp.format(timestamp.parse(requestLine.get(0))));
		final Time time = Time.valueOf(formatTime);
		
		final Integer currentFloor = Integer.parseInt(requestLine.get(1));
		final String floorButton = requestLine.get(2);
		final Integer carButton = Integer.parseInt(requestLine.get(3));
		
		return new ElevatorRequestData(time, currentFloor, floorButton, carButton);
		
	}

}