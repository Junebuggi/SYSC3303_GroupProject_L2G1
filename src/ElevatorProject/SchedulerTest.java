package ElevatorProject;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

class SchedulerTest {
	
	@Test
	public void testPutRequest() throws Throwable{
		
		Scheduler underTest = new Scheduler();
		
		byte[] request = {0, 0, 0, 0};
		
		ArrayList<byte[]> expected = new ArrayList<>();
		expected.add(request);
		ArrayList<byte[]> actual;
		
		synchronized (underTest) {
			underTest.putRequest(request);
			actual = underTest.getAllRequest();
		}
		
		for(int i = 0; i < expected.size(); i++) {
			assertEquals(actual.get(i).equals(expected.get(i)),true);
		}
		
	}

}
