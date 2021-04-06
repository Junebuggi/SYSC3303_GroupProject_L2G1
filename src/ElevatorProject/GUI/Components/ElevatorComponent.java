package ElevatorProject.GUI.Components;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import ElevatorProject.Information;
import ElevatorProject.GUI.ElevatorGridGUI;

/**
 * This class models all the floors in an elevator shaft as JPanels. The
 * elevator's position is represented by a colored panel.
 * 
 * @author Emma Boulay [Iteration 5]
 *
 */
public class ElevatorComponent {

	private Color errorColour = Color.decode("#e9afaf"); // pale red
	private Color idleColour = Color.decode("#d2e9af"); // pale green
	private Color movingColour = Color.decode("#e9d8f2"); // pale purple
	private Color arrivedColour = Color.decode("#c3e4e8"); // pale blue

	private Color noColour = Color.white;
	private JPanel floors[];

	/**
	 * This constructor will, create a JPanel to model each floor and add it to the
	 * parent elevatorShaftPanel. And then set the elevator to the bottom floor
	 * (Floor 1) and to IDLE
	 * 
	 * @param panel,      the parent panel
	 * @param elevatorID, the elevator's ID
	 */
	public ElevatorComponent(JPanel panel, int elevatorID) {

		floors = new JPanel[Information.NUM_FLOORS];
		for (int i = 0; i < Information.NUM_FLOORS; i++) {
			floors[i] = new JPanel();
			floors[i].setBackground(Color.WHITE);
			floors[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));

		}
		panel.add(new JLabel("Car " + elevatorID));
		panel.add(ElevatorGridGUI.dirLamp[elevatorID - 1]);
		for (int i = Information.NUM_FLOORS - 1; i >= 0; i--) {
			panel.add(floors[i]);
		}

		setColour("IDLE", 1);

	}

	/**
	 * This method is used to model the elevator's state at a specific floor
	 * 
	 * @param state, "IDLE", "MOVING", "ARRIVED" or "ERROR"
	 * @param floor, the floor the elevator is at
	 */
	public void setColour(String state, int floor) {
		// Clear all the panels to paint the new floor
		reset();

		if (state.equals("IDLE"))
			floors[floor - 1].setBackground(idleColour);
		else if (state.equals("MOVING"))
			floors[floor - 1].setBackground(movingColour);
		else if (state.equals("ARRIVED"))
			floors[floor - 1].setBackground(arrivedColour);
		else if (state.equals("ERROR")) {

			// Create a new thread to handle flashing the red error colour for the elevator
			new Thread() {
				public void run() {
					while (true) {
						try {
							floors[floor - 1].setBackground(errorColour);
							Thread.sleep(500);
							floors[floor - 1].setBackground(noColour);
							Thread.sleep(250);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}

			}.start();

		}

		else if (state.equals("NOELEVATOR"))
			floors[floor - 1].setBackground(noColour);

	}

	/**
	 * This method will set all the floors back to white
	 */
	public void reset() {
		for (JPanel floor : floors) {
			floor.setBackground(Color.WHITE);
		}
	}

}
