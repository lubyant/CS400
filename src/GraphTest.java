// --== CS400 File Header Information ==--
// Name: <your full name>
// Email: <your @wisc.edu email address>
// Team: <your team name: two letters>
// TA: <name of your team's ta>
// Lecturer: <name of your lecturer>
// Notes to Grader: <optional extra notes>
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.NoSuchElementException;



/**
 * Tests the implementation of CS400Graph for the individual component of
 * Project Three: the implementation of Dijsktra's Shortest Path algorithm.
 */
public class GraphTest {

    private CS400Graph<Integer> graph;
    @BeforeEach
    /**
     * Instantiate graph from last week's shortest path activity.
     */
    public void createGraph() {
        graph = new CS400Graph<>();
        // insert verticies 0-9
        for(int i=0;i<10;i++)
            graph.insertVertex(i);
        // insert edges from Week 08. Dijkstra's Activity
        graph.insertEdge(0,2,1);
        graph.insertEdge(1,7,2);
        graph.insertEdge(1,8,4);
        graph.insertEdge(2,4,4);
        graph.insertEdge(2,6,3);
        graph.insertEdge(3,1,6);
        graph.insertEdge(3,7,2);
        graph.insertEdge(4,5,4);
        graph.insertEdge(5,0,2);
        graph.insertEdge(5,1,4);
        graph.insertEdge(5,9,1);
        graph.insertEdge(6,3,1);
        graph.insertEdge(7,0,3);
        graph.insertEdge(7,6,1);
        graph.insertEdge(8,9,3);
        graph.insertEdge(9,4,5);
    }

    /**
     * Checks the distance/total weight cost from the vertex labelled 0 to 8
     * (should be 15), and from the vertex labelled 9 to 8 (should be 17).
     */
    @Test
    public void providedTestToCheckPathCosts() {
        assertTrue(graph.getPathCost(0,8) == 15);    
        assertTrue(graph.getPathCost(9,8) == 17);
    }

    /**
     * Checks the ordered sequence of data within vertices from the vertex 
     * labeled 0 to 8, and from the vertex labeled 9 to 8.
     */
    @Test
    public void providedTestToCheckPathContents() {
        assertTrue(graph.shortestPath(0,8).toString().equals(
            "[0, 2, 6, 3, 1, 8]"
        ));
        assertTrue(graph.shortestPath(9,8).toString().equals(
            "[9, 4, 5, 1, 8]"
        ));
    }
    
    /**
     * Confirm the result of activity from dot 2 to dot 8, the estimated shortest path distance
     * labelled 2 to 8, the shortest distance should be 14.
     */
    @Test
    public void furthestPathTest() {
    	assertTrue(graph.getPathCost(2,8) == 14);
    }
    
    /**
     * Confirm the result of activity from dot 2 to dot 8, furthest vertex from the label 2
     * 
     */
    @Test
    public void furthestStringTest() {
    	assertTrue(graph.shortestPath(2,8).toString().equals(
                "[2, 6, 3, 1, 8]"
            ));
    }
    
    /**
     * testing the exception for the null input
     * start vertex is 20 which is not in table.
     * should throws NoSuchElement exception
     */
    @Test
    public void ExceptionOneTest() {
    	boolean thrown =false;
    	try {
    		graph.getPathCost(20,15);
    	}
    	catch (NoSuchElementException e) {
    		thrown = true;
    	}
    	assertTrue(thrown);
    }
     
    /**
     * testing the exception for the null input
     * start vertex is 20 which is not in table.
     * should throws NoSuchElement exception
     */
    @Test
    public void ExceptionTwoTest() {
    	boolean thrown =false;
    	try {
    		graph.shortestPath(20, 8).toString();
    	}
    	catch (NoSuchElementException e) {
    		thrown = true;
    	}
    	assertTrue(thrown);

    }
}