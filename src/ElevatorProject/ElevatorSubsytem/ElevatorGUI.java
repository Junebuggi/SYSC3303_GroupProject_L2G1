package ElevatorProject.ElevatorSubsytem;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import ElevatorProject.Information;

@SuppressWarnings("serial")
public class ElevatorGUI extends JFrame{
	
		private JTextArea ta[];

		public ElevatorGUI() {
			super("Elevators");
			System.out.println("Text Area Created!");
			ta = new JTextArea[Information.NUM_ELEVATORS];
		    
		    Box box = Box.createVerticalBox();
		    for(int i = 0; i < Information.NUM_ELEVATORS; i++) {
		    	ta[i] = new JTextArea(5,40);
			    ta[i].setEditable(false);
		    	System.out.println("Text Area Created!");
		    	
			    JScrollPane pane1 =
			        new JScrollPane(ta[i], JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			                        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			    pane1.setBorder(BorderFactory.createTitledBorder("Elevator " + (i+1)));
			    box.add(pane1);
			   
		    }
		    getContentPane().add(box);
		    
		}
		
		public JTextArea getArea(int i) {
			return ta[i];
		}

}
