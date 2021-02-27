package ElevatorProject;

import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

class SchedulerStateMachineTest {
	
	
	@Test
	public void testUpdateArrivals() throws Throwable{
		
		SchedulerStateMachine underTest = new SchedulerStateMachine(String "-v");
		
		byte[] request = {0, 0, 0, 0};
		
		ArrayList<byte[]> expected = new ArrayList<>();
		expected.add(request);
		ArrayList<byte[]> actual;
		
		synchronized (underTest) {
			underTest.putRequest(request, "floorRequest".getBytes());
			actual = underTest.getAllRequest();
		}
		
		for(int i = 0; i < expected.size(); i++) {
			assertEquals(actual.get(i).equals(expected.get(i)),true);
		}
		
	}
	
}
