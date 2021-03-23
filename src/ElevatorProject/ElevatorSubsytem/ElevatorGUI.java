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
			ta = new JTextArea[Information.NUM_ELEVATORS+1];
		    
		    Box box = Box.createVerticalBox();
		    
		    ta[0] = new JTextArea(1,40);
		    ta[0].setEditable(false);
	    	
		    JScrollPane pane =
		        new JScrollPane(ta[0], JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
		                        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		    pane.setBorder(BorderFactory.createTitledBorder("State Colour Legend"));
		    box.add(pane);
		    for(int i = 0; i < Information.NUM_ELEVATORS; i++) {
		    	ta[i+1] = new JTextArea(5,40);
			    ta[i+1].setEditable(false);
		    	
			    JScrollPane pane1 =
			        new JScrollPane(ta[i+1], JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			                        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			    pane1.setBorder(BorderFactory.createTitledBorder("Elevator " + (i+1)));
			    box.add(pane1);
			   
		    }
		    getContentPane().add(box);
		    
		    ta[0].append("IDLE State: Green\n");
		    ta[0].append("MOVING State: Purple\n");
		    ta[0].append("ARRIVED State: Blue\n");

		    
		}
		
		public JTextArea getArea(int i) {
			return ta[i];
		}

}
