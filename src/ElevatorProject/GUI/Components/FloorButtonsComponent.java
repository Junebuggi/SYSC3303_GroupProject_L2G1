package ElevatorProject.GUI.Components;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * This class models the the floor direction buttons that are on each floor. If
 * the floor is the top floor there is only a top bottom. And the bottom floor
 * only has an UP button. The FloorButton icons were created in AdobeXD.
 * 
 * @author Emma Boulay [Iteration 5]
 *
 */
public class FloorButtonsComponent {
	private JLabel[] buttons;
	private ImageIcon upPressed;
	private ImageIcon upNotPressed;
	private ImageIcon downPressed;
	private ImageIcon downNotPressed;
	private boolean topFloor;
	private boolean bottomFloor;

	/**
	 * This constructor creates the labels to model the floors floorbuttons and
	 * initializes them to notPressed.
	 * 
	 * @param panel
	 * @param topFloor
	 * @param bottomFloor
	 */
	public FloorButtonsComponent(JPanel panel, boolean topFloor, boolean bottomFloor) {
		this.topFloor = topFloor;
		this.bottomFloor = bottomFloor;

		upNotPressed = new ImageIcon(new ImageIcon("src/ElevatorProject/GUI/Components/FloorUpButtonNotPressed.png")
				.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
		upPressed = new ImageIcon(new ImageIcon("src/ElevatorProject/GUI/Components/FloorUpButtonPressed.png")
				.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));

		downNotPressed = new ImageIcon(new ImageIcon("src/ElevatorProject/GUI/Components/FloorDownButtonNotPressed.png")
				.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
		downPressed = new ImageIcon(new ImageIcon("src/ElevatorProject/GUI/Components/FloorDownButtonPressed.png")
				.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));

		buttons = new JLabel[2];
		// If the top floor, only a DOWN button
		if (topFloor) {
			buttons[0] = new JLabel();
			buttons[1] = new JLabel(downNotPressed);
		}
		// If the bottom floor, only a UP button
		else if (bottomFloor) {
			buttons[0] = new JLabel(upNotPressed);
			buttons[1] = new JLabel();
		} else {
			buttons[0] = new JLabel(upNotPressed);
			buttons[1] = new JLabel(downNotPressed);
		}
		panel.add(buttons[0]);
		panel.add(buttons[1]);

	}

	/**
	 * This method will turn the button on in the appropriate direction
	 * 
	 * @param direction "UP" or "DOWN"
	 */
	public void pressButton(String direction) {
		if (direction.equals("UP")) {
			buttons[0].setIcon(upPressed);
		} else if (direction.equals("DOWN")) {
			buttons[1].setIcon(downPressed);
		}
	}

	/**
	 * This method will turn the button off in the appropriate direction
	 * 
	 * @param direction "UP" or "DOWN"
	 */
	public void turnOffButton(String direction) {
		if (direction.equals("UP") && !topFloor) {
			buttons[0].setIcon(upNotPressed);
		} else if (direction.equals("DOWN") && !bottomFloor) {
			buttons[1].setIcon(downNotPressed);
		}

		if (direction.equals("DOWN") && bottomFloor) {
			buttons[0].setIcon(upNotPressed);
		} else if (direction.equals("UP") && topFloor) {
			buttons[1].setIcon(downNotPressed);
		}
	}

}
