# SYSC3303_Group_Project

Group 1 Members
  - Abeer Rafiq
  - Alden Wan Yeung Ng
  - Emma Boulay
  - Hasan Baig
  - Rutvik Shah

Project Iteration 1 - Java Threads

- Purpose
The purpose of this project iteration is to model the communication between the floor subsystem and the 
elevator subsystem with the scheduler. The floor subsystem reads in events from an input file and will 
send them to the Scheduler and will wait until the scheduler communicates that the elevator has acknowledge 
their request. The elevator will make calls to the Scheduler while it is stationary to check if any requests 
are pending. If requests are pending the elevator subsystem will handle the request and send an acknowledgment 
for the Scheduler to pass back to the floor subsystem. The program will terminate when the floor subsystem 
has handled all events in the input file and has received the last acknowledgment.

- Breakdown of Responsibilities
  ALL
    -> Class Diagram
    -> Sequence Diagram
    -> ElevatorSystem.java (main program)
  Abeer + Emma
    -> Scheduler and related classes
  Alden + Hasan
    -> elevatorSubsystem and related classes
    -> Information.java
  Rutvik
    -> floorSubsystem and related classes
    
-File Names

Elevator.java
	-> The Elevator thread will try to get requests from the scheduler while it is stationary. 
It will then handle those requests and pass an acknowledgment to the Scheduler to be passed back to the floorSubsystem.
  
ElevatorRequest.java
 	-> The ElevatorRequest class models an event request

ElevatorSystem.java
	-> The main class which initializes the floorSubsystem(client), elevatorSubsystem(client) and the 
Scheduler(server) threads and then starts them.

ElevatorTest.java
	-> A jUNIT test class to test the methods of the Elevator class. Currently empty

Floor.java
-> The Floor thread will read in events from an input file and will try to put a request in the scheduler 
while the previous request has been acknowledged.
	
FloorTest.java
	-> A jUNIT test class to test the methods of the Floor class. Currently empty
	
Information.java
	-> A class that stores the constants of Iteration1 and the enumerators for the lamp state, motor direction, 
door state and direction lamp.

Scheduler.java
	-> The scheduler thread is a shared resource between the floor and elevator subsystems. The floorSubsystem 
will place requests into the Scheduler and the elevatorSubsystem will get and handle those requests.

SchedulerTest.java
	-> A jUNIT test class to test the methods of the Scheduler class.

floorRequests.txt
  -> A text file containing 100 event requests in the form of 
          "TIME FLOOR FLOORBUTTON CARBUTTON"

-Set-up Instructions
	1. Unzip project folder and import into Eclipse
	2. Run ElevatorSystem.java file (main program)
 