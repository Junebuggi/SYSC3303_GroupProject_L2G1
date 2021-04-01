package ElevatorProject.GUI.Components;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ElevatorProject.Information;

public class ElevatorButtonComponent {
	
	private JLabel[] buttons;
	private ImageIcon pressed;
	private ImageIcon notPressed;
	
	
	public ElevatorButtonComponent(JPanel panel) {
		notPressed = new ImageIcon("src/ElevatorProject/GUI/Components/ElevatorButtonNotPressed.png");
		pressed = new ImageIcon("src/ElevatorProject/GUI/Components/ElevatorButtonPressed.png");
		
		buttons = new JLabel[Information.NUM_FLOORS];
		for(int i = 0; i < Information.NUM_FLOORS; i++) {
			buttons[i] = new JLabel(new ImageIcon(notPressed.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT)));
			buttons[i].setText(""+(i+1));
			buttons[i].setHorizontalTextPosition(JLabel.CENTER);
			panel.add(buttons[i]);
		}
		
		
	}
	
	public void pressButton(int floorNumber) {
		buttons[floorNumber-1].setIcon(new ImageIcon(pressed.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT)));
	}
	
	public void turnOffButton(int floorNumber) {
		buttons[floorNumber-1].setIcon(new ImageIcon(notPressed.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT)));		
	}

}
