# SYSC3303_Group_Project

Group Members
  - Abeer Rafiq
  - Alden Wan Yeung Ng
  - Emma Boulay
  - Hasan Baig
  - Rutvik Shah

Project Iteration 1 - Java Threads

-Purpose
The purpose of this project iteration is to model the communication between the floor subsystem and the elevator subsystem with the scheduler. The floor subsystem reads in events from an input file and will send them to the Scheduler and will wait until the scheduler communicates that the elevator has acknowledge their request. The elevator will make calls to the Scheduler while it is stationary to check if any requests are pending. If requests are pending the elevator subsystem will handle the request and send an ackowledgment for the Scheduler to pass back to the floor subsystem. The program will terminate when the floor subsystem has handled all events in the input file and has recieved the last acknowledgment.

-Breakdown of Responsibilities
  ALL
    -> Class Diagram
    -> Sequence Diagram
    -> elevatorProjectTest.java (main program)
  Abeer + Emma
    -> Scheduler.java
  Alden + Hasan
    -> elevatorSubsystem.java
  Rutvik
    -> floorSubsystem.java
    
-File Names
elevatorProjectTest.java
	-> The main class which initializes the floorSubsystem(client), elevatorSubsystem(client) and the Scheduler(server) threads and starts them.

Scheduler.java
	-> The scheduler thread is a shared resource between the floor and elevator subsystems. The floorSubsystem will place requests into the Scheduler and the elevatorSubsystem will get and handle those requests.

floorSubsystem.java
	-> The floorSubsystem thread will read in events from an input file and will try to put a request in the scheduler while the previous request has been acknowledged.
  
elevatorSubsystem.java
	-> The elevatorSubystem thread will try to get requests from the scheduler while it is stationary. It will then handle those requests and pass an acknowledgment to the Scheduler to be passed back to the floorSubsystem.
  
floorRequests.txt
  -> A text file containing 100 event requests in the form of 
          "TIME FLOOR FLOORBUTTON CARBUTTON"

-Set-up Instructions
	1. Unzip project folder and import into Eclipse
	2. Run elevatorProjectTest.java file
