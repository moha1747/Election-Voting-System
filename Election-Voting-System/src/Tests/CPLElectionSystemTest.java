/*
CPLElectionSystemTest.java

System tests for the CPLElection class.

*/

import AES.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests various CPL election inputs for validity of information and expected results.
 */
class CPLElectionSystemTest {

    @Test
    @DisplayName("CPLexample.csv")
    void test1() {
        Election election = new CPLElection();
        FileInput fin = new FileInput("Project1/testing/testfiles/CPLexample.csv");
        assertFalse(fin.isOPL());
        election.processBallotData(fin);
        election.allocatePartySeats();
        election.assignCandidateSeats();

        Party[] parties = election.getParties();
        ArrayList<ArrayList<Candidate>> candidates = election.getCandidates();
        int[][] allocationData = election.getAllocationData();

        assertEquals(3, election.getSeatCount());
        assertEquals(9, election.getBallotCount());
        assertEquals(6, election.getPartyCount());
        assertEquals(11, election.getCandidateCount());

        assertEquals(3, parties[0].getVoteCount());
        assertEquals(2, parties[1].getVoteCount());
        assertEquals(0, parties[2].getVoteCount());
        assertEquals(2, parties[3].getVoteCount());
        assertEquals(1, parties[4].getVoteCount());
        assertEquals(1, parties[5].getVoteCount());

        assertArrayEquals(new int[]{0, 1, 0, 1, 0, 0}, allocationData[3]);

        assertEquals(1, allocationData[4][0]);
        assertEquals(1, allocationData[4][1]);
        assertEquals(0, allocationData[4][2]);
        assertEquals(1, allocationData[4][3]);
        assertEquals(0, allocationData[4][4]);
        assertEquals(0, allocationData[4][5]);
    }

    @Test
    @DisplayName("CPLexample2.csv")
    void test2() {
        Election election = new CPLElection();
        FileInput fin = new FileInput("Project1/testing/testfiles/CPLexample2.csv");
        assertFalse(fin.isOPL());
        election.processBallotData(fin);
        election.allocatePartySeats();
        election.assignCandidateSeats();

        Party[] parties = election.getParties();
        ArrayList<ArrayList<Candidate>> candidates = election.getCandidates();
        int[][] allocationData = election.getAllocationData();

        assertEquals(10, election.getSeatCount());
        assertEquals(11, election.getBallotCount());
        assertEquals(4, election.getPartyCount());
        assertEquals(24, election.getCandidateCount());

        assertEquals(0, parties[0].getVoteCount());
        assertEquals(1, parties[1].getVoteCount());
        assertEquals(2, parties[2].getVoteCount());
        assertEquals(8, parties[3].getVoteCount());

        boolean test1 = Arrays.deepEquals(allocationData, new int[][]{{0, 1, 2, 8}, {0, 0, 1, 1}, {0, 1, 0, 6}, {3, 3, 2, 0}, {3, 3, 3, 1}});
        assertTrue(test1);

        assertTrue(candidates.get(0).get(0).isSat());
        assertTrue(candidates.get(0).get(1).isSat());
        assertTrue(candidates.get(0).get(2).isSat() ^ candidates.get(1).get(3).isSat());

        assertFalse(candidates.get(0).get(3).isSat());
        assertFalse(candidates.get(0).get(4).isSat());
        assertFalse(candidates.get(0).get(5).isSat());
        assertFalse(candidates.get(0).get(6).isSat());
        assertFalse(candidates.get(0).get(7).isSat());
        assertFalse(candidates.get(0).get(8).isSat());
        assertFalse(candidates.get(0).get(9).isSat());


        assertTrue(candidates.get(1).get(0).isSat());
        assertTrue(candidates.get(1).get(1).isSat());
        assertTrue(candidates.get(1).get(2).isSat());

        assertFalse(candidates.get(1).get(4).isSat());
        assertFalse(candidates.get(1).get(5).isSat());
        assertFalse(candidates.get(1).get(6).isSat());
        assertFalse(candidates.get(1).get(7).isSat());
        assertFalse(candidates.get(1).get(8).isSat());
        assertFalse(candidates.get(1).get(9).isSat());

        assertTrue(candidates.get(2).get(0).isSat());
        assertTrue(candidates.get(2).get(1).isSat());
        assertTrue(candidates.get(2).get(2).isSat());

        assertTrue(candidates.get(3).get(0).isSat());
    }

    @Test
    @DisplayName("CPLexampleLarge.csv")
    void testLarge() {
        Election election = new CPLElection();
        FileInput fin = new FileInput("Project1/testing/testfiles/CPLexampleLarge.csv");
        assertFalse(fin.isOPL());
        election.processBallotData(fin);
        election.allocatePartySeats();
        election.assignCandidateSeats();

        Party[] parties = election.getParties();
        ArrayList<ArrayList<Candidate>> candidates = election.getCandidates();
        int[][] allocationData = election.getAllocationData();

        // basic election info
        assertEquals(11, election.getSeatCount());
        assertEquals(934229, election.getBallotCount());
        assertEquals(20, election.getCandidateCount());
        assertEquals(6, election.getPartyCount());

        // vote counts
        assertEquals(455205, parties[0].getVoteCount());
        assertEquals(372397, parties[1].getVoteCount());
        assertEquals(65123, parties[2].getVoteCount());
        assertEquals(7057, parties[3].getVoteCount());
        assertEquals(33946, parties[4].getVoteCount());
        assertEquals(501, parties[5].getVoteCount());

        // party names
        assertEquals("Democrat", parties[0].getName());
        assertEquals("Republican", parties[1].getName());
        assertEquals("Independent1", parties[2].getName());
        assertEquals("Independent2", parties[3].getName());
        assertEquals("Independent3", parties[4].getName());
        assertEquals("Independent4", parties[5].getName());

        // candidate names
        // democrat
        assertEquals("Alice", candidates.get(0).get(0).getName());
        assertEquals("Bob", candidates.get(0).get(1).getName());
        assertEquals("Charlie", candidates.get(0).get(2).getName());
        assertEquals("David", candidates.get(0).get(3).getName());
        assertEquals("Eve", candidates.get(0).get(4).getName());
        assertEquals("Frank", candidates.get(0).get(5).getName());
        assertEquals("Grace", candidates.get(0).get(6).getName());
        assertEquals("Hank", candidates.get(0).get(7).getName());
        // republican
        assertEquals("Ivy", candidates.get(1).get(0).getName());
        assertEquals("Jack", candidates.get(1).get(1).getName());
        assertEquals("Kate", candidates.get(1).get(2).getName());
        assertEquals("Larry", candidates.get(1).get(3).getName());
        assertEquals("Mabel", candidates.get(1).get(4).getName());
        assertEquals("Nancy", candidates.get(1).get(5).getName());
        assertEquals("Oscar", candidates.get(1).get(6).getName());
        assertEquals("Peggy", candidates.get(1).get(7).getName());
        // independent
        assertEquals("Quincy", candidates.get(2).get(0).getName());
        assertEquals("Roger", candidates.get(3).get(0).getName());
        assertEquals("Sally", candidates.get(4).get(0).getName());
        assertEquals("Tom", candidates.get(5).get(0).getName());

        // allocation data
        boolean allocation = Arrays.deepEquals(allocationData, new int[][]{
                {455205, 372397, 65123, 7057, 33946, 501},
                {5, 4, 0, 0, 0, 0},
                {30555, 32677, 65123, 7057, 33946, 501},
                {0, 0, 1, 0, 1, 0},
                {5, 4, 1, 0, 1, 0}
        });
        assertTrue(allocation);

        // candidates are correctly sat
        // democrat
        assertTrue(candidates.get(0).get(0).isSat());
        assertTrue(candidates.get(0).get(1).isSat());
        assertTrue(candidates.get(0).get(2).isSat());
        assertTrue(candidates.get(0).get(3).isSat());
        assertTrue(candidates.get(0).get(4).isSat());
        assertFalse(candidates.get(0).get(5).isSat());
        assertFalse(candidates.get(0).get(6).isSat());
        assertFalse(candidates.get(0).get(7).isSat());
        // republican
        assertTrue(candidates.get(1).get(0).isSat());
        assertTrue(candidates.get(1).get(1).isSat());
        assertTrue(candidates.get(1).get(2).isSat());
        assertTrue(candidates.get(1).get(3).isSat());
        assertFalse(candidates.get(1).get(4).isSat());
        assertFalse(candidates.get(1).get(5).isSat());
        assertFalse(candidates.get(1).get(6).isSat());
        assertFalse(candidates.get(1).get(7).isSat());
        // independents
        assertTrue(candidates.get(2).get(0).isSat());
        assertFalse(candidates.get(3).get(0).isSat());
        assertTrue(candidates.get(4).get(0).isSat());
        assertFalse(candidates.get(5).get(0).isSat());
    }
}