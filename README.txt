# SYSC3303_Group_Project

Group 1 Members
  - Abeer Rafiq
  - Alden Wan Yeung Ng
  - Emma Boulay
  - Hasan Baig
  - Rutvik Shah

Project Iteration 3 - Multiple Cars and System Distribution

- PURPOSE

The purpose of this iteration is to split the system up into three separate programs that can run on 
three separate computers and communicate with each other using UDP. The Scheduler is now be used to coordinate 
the movement of cars such that each car carries roughly the same number of passengers as all of the others and 
so that the waiting time for passengers at floors is minimized. The state machines for each car executes
independently of each other, but they share their position with the scheduler. The scheduler will choose which 
elevator is used to service a given request.

- BREAKDOWN OF RESPONSIBILITIES

  Abeer 
  -> Class Diagram
  Emma
    -> Creating UDP communication between subsystems, created the Network class to simplify send, receive and rpc_send
    -> Implemented the algorithm in the scheduler to determine which elevator will serivce which request
    -> Implemented the setup coordination between the ElevatorSubsystem, FloorSubsystem and Scheduler
    -> Modified SchedulerStateMachineTest JUnit test to send mock data over UDP to scheduler
    -> Created ElevatorSubsystem to control multiple elevators
    -> Created FloorSubsystem to control multiple floors and send request to scheduler with required delay
  Alden + Hasan + Rutvik
    -> elevatorSubsystem and related classes
    -> Information.java
    -> ElevatorState.java and related classes
    -> Sequence Diagram
    -> Overall State Diagram
    -> Elevator State Diagram
    -> floorSubsystem and related classes
    
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
ElevatorState.java
	-> This interface holds all the methods that the Elevator States use to implement the logic.
ElevatorSubsystem.java
	-> This class controls the elevators in the system. It will receive requests
 	   from the scheduler at an initialized port and will pass the request on to the
 	   appropriate elevator.	   
ElevatorTest.java
	-> A jUNIT test class to test the methods of the Elevator class.
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
Scheduler.java
	-> The Scheduler is responsible for handling incoming requests from the floor Subsystem and passing 		  
	   requests to the elevator subsystem. This is done with the implementation of a state machine. 
SchedulerStateMachine.java
	-> The scheduler statemachine implements runnable and is in charge of implementing the state machine for 	  
	   the scheduler
SchedulerStateMachineTest.java
	-> This test class tests the state transitions of the state machine with how it interprets test data.
SchedulerTest.java
	-> A jUNIT test class to test the methods of the Scheduler class. Currently empty

- SET-UP INSTRUCTIONS

	1. Unzip project folder and import into Eclipse
	2. Run the following files (threads)
		a. SchedulerStateMachineFloorSubsystem.java (scheduler system)
		b. ElevatorSubsystem.java (elevator system)
		c. FloorSubsystem.java (floor system)
	3. Run SchedulerStateMachineTest.java JUnit file to run unit test
	4. Run ElevatorTest.java JUnit file to run unit test
 
- NOTE

	To create randomized floorRequests data, the user can run "FloorRequestCreator.java" 
	to have floorRequsts printed into the console, in a chronological order in accordance with time.
	The console output can then be copy and pasted in the "floorRequest.txt" file. The NUM_ELEVATORS
	and NUM_FLOORS variables in Information.java can be modified to simulated desired system.