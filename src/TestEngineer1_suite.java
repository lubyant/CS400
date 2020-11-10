import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

// --== CS400 File Header Information ==--
// Name: Boyuan Lu
// Email:blu38@wisc.edu
// Team: GG
// Role: TestEngineer
// TA: Dan
// Lecturer: Florian
// Notes to Grader: <optional extra notes>


/**
 * @author lby
 * This is the test suite for the graph application - Computer Connection
 */
public class TestEngineer1_suite {
	/*
	 * this test will include followling seccesion:
	 * Test1: 
	 */
	@Test
	void Test1_BEcreate() {
		try {
			NetworkBackend.create();
			if (NetworkBackend.graph.isEmpty())
				fail("Entities are missing in graphs!");
		} catch (Exception e) {
			fail("Method create doesn't work and throw the error!");
		}
		
	}
	
	
	/*
	 * this test will include followling seccesion:
	 * Test1: 
	 */
	@Test
	void Test2_BEadd() {
		NetworkBackend.create();
		int testName = 50;
		int testTo = 1;
		int testWeight = 10;
		try {
			NetworkBackend.add(testName, testTo, testWeight, testWeight);
			assertTrue(NetworkBackend.graph.vertices.containsKey(testName));
		} catch (Exception e) {
			fail("Method add doesn't work and throw the error");
		}
		
	}
	
	
	/*
	 * this test will include followling seccesion:
	 * Test1: 
	 */
	@Test
	void Test3_BEconnect() {
		NetworkBackend.create();
		int fromNode = 50;
		int toNode = 51;
		int testWeight = 50;
		NetworkBackend.add(fromNode, -1, testWeight, testWeight);
		NetworkBackend.add(toNode, -1, testWeight, testWeight);
		try {
			NetworkBackend.connect(fromNode, toNode, testWeight);
			assertTrue(NetworkBackend.graph.containsEdge(fromNode, toNode));
		}catch (Exception e) {
			fail("Method add doesn't work and throw the error");
		}
	}
	
	
	/*
	 * this test will include followling seccesion:
	 * Test1: 
	 */
	@Test
	void Test4_BElistInfo() {
		NetworkBackend.create();
		NetworkBackend.clear();
		int fromNode = 50;
		int toNode = 51;
		int testWeight = 50;
		NetworkBackend.add(fromNode, -1, testWeight, testWeight);
		NetworkBackend.add(toNode, -1, testWeight, testWeight);
		String TestStr = "Computer Name: 51\n"
				+ "	Outgoing Connections: NONE\n\n"
				+ "Computer Name: 50\n"
				+ "	Outgoing Connections: NONE\n\n";
		try {
			NetworkBackend.add(fromNode, -1, testWeight, testWeight);
			NetworkBackend.add(toNode, -1, testWeight, testWeight);
			assertTrue(TestStr.equals(NetworkBackend.listAllInfo()));
		}catch (Exception e) {
			fail("Method add doesn't work and throw the error");
		}
		
	}
	
	
	/*
	 * this test will include followling seccesion:
	 * Test1: 
	 */
	@Test
	void Test5_BEremove() {
		NetworkBackend.create();
		try {
			NetworkBackend.remove(1);
			assertTrue(!NetworkBackend.graph.vertices.containsKey(1));
		}catch (Exception e) {
			fail("Method add doesn't work and throw the error");
		}
		
	}
	
	/*
	 * this test will include followling seccesion:
	 * Test1: 
	 */
	@Test
	void Test6_BEclear() {
		NetworkBackend.create();
		try {
			NetworkBackend.clear();
			assertTrue(NetworkBackend.graph.vertices.isEmpty());
		}catch (Exception e) {
			fail("Method add doesn't work and throw the error");
		}
		
	}
	
	
	/*
	 * this test will include followling seccesion:
	 * Test1: 
	 */
	@Test
	void Test7_BEpathFinder1() {
		NetworkBackend.create();
		try {
			NetworkBackend.pathFinder(50, 1);
			fail("not throwing the exception!");
		}catch(NoSuchElementException e) {
			assertTrue(true);
		}catch(Exception e) {
			fail("inccorect exception were thrown!");
		}			
	}
	
	
	/*
	 * this test will include followling seccesion:
	 * Test1: 
	 */
	@Test
	void Test8_BEpathFinder2() {
		NetworkBackend.create();
		try {
			assertEquals(NetworkBackend.pingFinder(2, 3),153);
		}catch(Exception e) {
			fail("inccorect exception were thrown!");
		}
	}
}
