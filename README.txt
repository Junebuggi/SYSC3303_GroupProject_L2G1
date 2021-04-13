# SYSC3303_Group_Project

Group 1 Members
  - Abeer Rafiq
  - Alden Wan Yeung Ng
  - Emma Boulay
  - Hasan Baig
  - Rutvik Shah

Project Iteration 5 - GUI and Measurements

- BREAKDOWN OF RESPONSIBILITIES

     Abeer
      -> Gathered measurements for Scheduler performance and calculated statistics
     Emma
	  -> Created layout for the GUI and implemented it using Java Swing
	  -> Gathered measurements for Scheduler performance and calculated statistics
	  
     Alden + Hasan + Rutvik
	 

- FILE NAMES

DirectionLamp.java
	-> This class models the direction lamps inside and outside the elevator. It
 	   indicates the direction of the elevator. It has a direction and status of on
  	   or off
Information.java
	-> This class contains all the static final variables used in the system
Network.java
	-> The Network superclass contains generic methods to make it easier for sending
 	   and receiving packets.
Packet.java
	-> The Packet class contains helper functions to manipulate byte arrays. The
 	   methods allow for printing byte[], combining byte arrays and a toString
 	   method.
	   
/***** ELEVATOR SUBSYSTEM *****/
ArrivedES.java
	-> This class implements the ElevatorState interface to set the State to Arrived and change 
	   characteristics of the Elevator.
Elevator.java
	-> The Elevator threads floorsToVisit arraylist is updated by requests received
 	   from the Elevator Subsystem. When there are requests to be serviced and the
 	   elevator is in the IDLE state, it will service requests. It will also inform
           the scheduler through the arrival sensor at each floor it approaches to check
 	   if it should stop. It also changes the states of the state machine.
ElevatorButton.java
	-> This class simulates a floor button inside an elevator
ElevatorGUI.java
	-> Creates a GUI that display the messages of each elevator thread in their respective states
ElevatorState.java
	-> This interface holds all the methods that the Elevator States use to implement the logic.
ElevatorSubsystem.java
	-> This class controls the elevators in the system. It will receive requests
 	   from the scheduler at an initialized port and will pass the request on to the
 	   appropriate elevator.	   
ElevatorTest.java
	-> A jUNIT test class to test the methods of the Elevator class.
ErrorES.java
	-> This class implements the ElevatorState interface to set the State to Error and change characteristics 	   
	   of the Elevator.	
IdleES.java
	-> This class implements the ElevatorState interface to set the State to Idle and change characteristics 	   
	   of the Elevator.	
MovingES.java
	-> This class implements the ElevatorState interface to set the State to Moving and change 			   
	   characteristics of the Elevator.
     
/***** FLOOR SUBSYSTEM *****/
Floor.java
  	-> The floor class models a floor that has nShaft elevator shafts with an up or
           down button to request and elevator and a lamp above each shaft to signal an
           elevator arrival.
FloorButton.java
	-> This class simulates a floor button on a floor	
FloorRequestCreator.java
	-> The FloorRequestCreator class will create and print nRequests with parameters from
 	   the Information class. These requests can then be copied and pasted in to the
 	   floorRequest.txt file.
FloorSubsystem.java
	-> The FloorSubsystem thread will read in events from an input file and will try to put a request 
 	   in the scheduler and then peek ahead to the next request and sleep until time elapsed to send 
 	   the next floor request to the scheduler	   
FloorTest.java
	-> A jUNIT test class to test the methods of the Floor class. Currently empty
floorRequests.txt
  -> A text file containing 100 event requests in the form of 
          "TIME FLOOR FLOORBUTTON CARBUTTON"
	  
/***** SCHEDULER SUBSYSTEM *****/
ElevatorFaultMonitor.java
	->  This class is responsible for starting a timer each time a elevator services a request.
		The timer will be triggered after a certain time since there was no arrival sensor
		message received that would have cancelled the timer. The corresponding elevator 
		will go out of service to ensure that the issue can be reviewed before operating again.
Scheduler.java
	-> The Scheduler is responsible for handling incoming requests from the floor Subsystem and passing 		  
	   requests to the elevator subsystem. This is done with the implementation of a state machine. 
SchedulerStateMachine.java
	-> The scheduler state machine implements runnable and is in charge of implementing the state machine for 	  
	   the scheduler
SchedulerStateMachineTest.java
	-> This test class tests the state transitions of the state machine with how it interprets test data.
ElevatorFaultMonitor.java
	-> This class implements a fault timer that ensures that if the scheduler does not receive a arrival
	sensor message on time for a elevator, then that elevator is out of order.
SchedulerTest.java
	-> A jUNIT test class to test the methods of the Scheduler class. Currently empty

- SET-UP INSTRUCTIONS

	1. Unzip project folder and import into Eclipse
	2. Run the following files (threads)
		a. SchedulerStateMachine.java (scheduler system)
		b. ElevatorSubsystem.java (elevator system) - A GUI WILL POP UP SHOWING ELEVATOR STATE INFO
		c. FloorSubsystem.java (floor system)
	3. Run SchedulerStateMachineTest.java JUnit file to run unit test
	4. Run ElevatorTest.java JUnit file to run unit test
 
- NOTE1
	To create randomized floorRequests data, the user can run "FloorRequestCreator.java" 
	to have floorRequsts printed into the console, in a chronological order in accordance with time.
	The console output can then be copy and pasted in the "floorRequest.txt" file. The NUM_ELEVATORS
	and NUM_FLOORS variables in Information.java can be modified to simulated desired system.

- NOTE2
	In the Information.java class there is a variable TIME_MULTIPLIER. It is used to change the speed of
	execution. It currently is 0.1 so the system will run at 10 times the speed.

