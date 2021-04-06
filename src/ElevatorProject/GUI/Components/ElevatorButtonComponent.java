package ElevatorProject.GUI.Components;

import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import ElevatorProject.Information;

/**
 * This class models the elevator buttons inside an elevator car. The icons were
 * developed in AdobeXD. It models a passenger pressing a button to request a
 * floor, and the button turning off when the elevator reaches the floor.
 * 
 * @author Emma Boulay [Iteration 5]
 *
 */
public class ElevatorButtonComponent {

	private JLabel[] buttons;
	private ImageIcon pressed;
	private ImageIcon notPressed;

	/**
	 * This method creates all the elevator buttons inside the elevator and sets
	 * them all to notPressed.
	 * 
	 * @param panel The grid panel where the icons are laid out on.
	 */
	public ElevatorButtonComponent(JPanel panel) {
		// The button icons, created in AdobeXD :)
		notPressed = new ImageIcon("src/ElevatorProject/GUI/Components/ElevatorButtonNotPressed.png");
		pressed = new ImageIcon("src/ElevatorProject/GUI/Components/ElevatorButtonPressed.png");

		buttons = new JLabel[Information.NUM_FLOORS];

		for (int i = 0; i < Information.NUM_FLOORS; i++) {
			buttons[i] = new JLabel(
					new ImageIcon(notPressed.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT)));
			buttons[i].setText("" + (i + 1)); // The floor number corresponding to the button
			buttons[i].setHorizontalTextPosition(JLabel.CENTER);
			panel.add(buttons[i]);
		}
	}

	/**
	 * This method will set the button in the pressed icon, simulating a passenger
	 * pressing a button
	 * 
	 * @param floorNumber, The button number to being pressed
	 */
	public void pressButton(int floorNumber) {
		buttons[floorNumber - 1]
				.setIcon(new ImageIcon(pressed.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT)));
	}

	/**
	 * This method will set the button to the notPressed icon, simulating the
	 * elevator arriving at the requested floor.
	 * 
	 * @param floorNumber, The button number to being pressed
	 */
	public void turnOffButton(int floorNumber) {
		buttons[floorNumber - 1]
				.setIcon(new ImageIcon(notPressed.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT)));
	}

}
