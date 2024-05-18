/*
OPLElectionSystemTest.java

System tests for the OPLElection class.

*/

import AES.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests various OPL election inputs for validity of information and expected results.
 */
class OPLElectionSystemTest {

    @Test
    @DisplayName("OPLexample.csv")
    void test1() {
        Election election = new OPLElection();
        FileInput fin = new FileInput("Project1/testing/testfiles/OPLexample.csv");
        assertTrue(fin.isOPL());
        election.processBallotData(fin);
        election.allocatePartySeats();
        election.assignCandidateSeats();

        Party[] parties = election.getParties();
        ArrayList<ArrayList<Candidate>> candidates = election.getCandidates();
        int[][] allocationData = election.getAllocationData();

        assertEquals(2, election.getSeatCount());
        assertEquals(9, election.getBallotCount());
        assertEquals(6, election.getCandidateCount());
        assertEquals(3, election.getPartyCount());

        assertEquals(2, candidates.get(0).get(0).getVoteCount());
        assertEquals(1, candidates.get(0).get(1).getVoteCount());
        assertEquals(0, candidates.get(0).get(2).getVoteCount());
        assertEquals(2, candidates.get(1).get(0).getVoteCount());
        assertEquals(2, candidates.get(1).get(1).getVoteCount());
        assertEquals(2, candidates.get(2).get(0).getVoteCount());

        assertEquals("Democrat", parties[0].getName());
        assertEquals("Republican", parties[1].getName());
        assertEquals("Independent1", parties[2].getName());

        assertEquals("Pike", candidates.get(0).get(0).getName());
        assertEquals("Lucy", candidates.get(0).get(1).getName());
        assertEquals("Beiye", candidates.get(0).get(2).getName());
        assertEquals("Etta", candidates.get(1).get(0).getName());
        assertEquals("Alawa", candidates.get(1).get(1).getName());
        assertEquals("Sasha", candidates.get(2).get(0).getName());

        boolean test1 = Arrays.deepEquals(allocationData, new int[][]{{3, 4, 2}, {0, 0, 0}, {3, 4, 2}, {1, 1, 0}, {1, 1, 0}});
        assertTrue(test1);

        assertTrue(candidates.get(0).get(0).isSat());
        assertFalse(candidates.get(0).get(1).isSat());
        assertFalse(candidates.get(0).get(2).isSat());

        assertTrue(candidates.get(1).get(0).isSat() ^ candidates.get(1).get(1).isSat());

        assertFalse(candidates.get(2).get(0).isSat());
    }

    @Test
    @DisplayName("OPLexample2.csv")
    void test2() {
        Election election = new OPLElection();
        FileInput fin = new FileInput("Project1/testing/testfiles/OPLexample2.csv");
        assertTrue(fin.isOPL());
        election.processBallotData(fin);
        election.allocatePartySeats();
        election.assignCandidateSeats();

        Party[] parties = election.getParties();
        ArrayList<ArrayList<Candidate>> candidates = election.getCandidates();
        int[][] allocationData = election.getAllocationData();

        // basic election info
        assertEquals(5, election.getSeatCount());
        assertEquals(58000, election.getBallotCount());
        assertEquals(10, election.getCandidateCount());
        assertEquals(4, election.getPartyCount());

        // vote counts
        assertEquals(15000, candidates.get(0).get(0).getVoteCount());
        assertEquals(5000, candidates.get(0).get(1).getVoteCount());
        assertEquals(5000, candidates.get(0).get(2).getVoteCount());
        assertEquals(1000, candidates.get(0).get(3).getVoteCount());
        assertEquals(15000, candidates.get(1).get(0).getVoteCount());
        assertEquals(5000, candidates.get(1).get(1).getVoteCount());
        assertEquals(5000, candidates.get(1).get(2).getVoteCount());
        assertEquals(1000, candidates.get(1).get(3).getVoteCount());
        assertEquals(5000, candidates.get(2).get(0).getVoteCount());
        assertEquals(1000, candidates.get(3).get(0).getVoteCount());

        // party names
        assertEquals("Democrat", parties[0].getName());
        assertEquals("Republican", parties[1].getName());
        assertEquals("Independent1", parties[2].getName());
        assertEquals("Independent2", parties[3].getName());

        // candidate names
        assertEquals("Alice", candidates.get(0).get(0).getName());
        assertEquals("Bob", candidates.get(0).get(1).getName());
        assertEquals("Charlie", candidates.get(0).get(2).getName());
        assertEquals("David", candidates.get(0).get(3).getName());
        assertEquals("Eve", candidates.get(1).get(0).getName());
        assertEquals("Frank", candidates.get(1).get(1).getName());
        assertEquals("Grace", candidates.get(1).get(2).getName());
        assertEquals("Hank", candidates.get(1).get(3).getName());

        // allocation data
        boolean allocation = Arrays.deepEquals(allocationData, new int[][]{
                {26000, 26000, 5000, 1000},
                {2, 2, 0, 0},
                {2800, 2800, 5000, 1000},
                {0, 0, 1, 0},
                {2, 2, 1, 0}
        });
        assertTrue(allocation);

        // candidates are correctly sat
        // democrat
        System.out.println(candidates);
        assertTrue(candidates.get(0).get(0).isSat());
        assertTrue(candidates.get(0).get(1).isSat() ^ candidates.get(0).get(2).isSat());
        assertFalse(candidates.get(0).get(3).isSat());
        // republican
        assertTrue(candidates.get(1).get(0).isSat());
        assertTrue(candidates.get(1).get(1).isSat() ^ candidates.get(1).get(2).isSat());
        assertFalse(candidates.get(1).get(3).isSat());
        // independents
        assertTrue(candidates.get(2).get(0).isSat());
        assertFalse(candidates.get(3).get(0).isSat());
    }

    @Test
    @DisplayName("OPLexampleLarge.csv")
    void testLarge() {
        Election election = new OPLElection();
        FileInput fin = new FileInput("Project1/testing/testfiles/OPLexampleLarge.csv");
        assertTrue(fin.isOPL());
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
        // democrat
        assertEquals(5676, candidates.get(0).get(0).getVoteCount());
        assertEquals(302, candidates.get(0).get(1).getVoteCount());
        assertEquals(231762, candidates.get(0).get(2).getVoteCount());
        assertEquals(9011, candidates.get(0).get(3).getVoteCount());
        assertEquals(76111, candidates.get(0).get(4).getVoteCount());
        assertEquals(120442, candidates.get(0).get(5).getVoteCount());
        assertEquals(670, candidates.get(0).get(6).getVoteCount());
        assertEquals(11231, candidates.get(0).get(7).getVoteCount());
        // republican
        assertEquals(227281, candidates.get(1).get(0).getVoteCount());
        assertEquals(6132, candidates.get(1).get(1).getVoteCount());
        assertEquals(25757, candidates.get(1).get(2).getVoteCount());
        assertEquals(91200, candidates.get(1).get(3).getVoteCount());
        assertEquals(320, candidates.get(1).get(4).getVoteCount());
        assertEquals(5, candidates.get(1).get(5).getVoteCount());
        assertEquals(7680, candidates.get(1).get(6).getVoteCount());
        assertEquals(14022, candidates.get(1).get(7).getVoteCount());
        // independent
        assertEquals(65123, candidates.get(2).get(0).getVoteCount());
        assertEquals(7057, candidates.get(3).get(0).getVoteCount());
        assertEquals(33946, candidates.get(4).get(0).getVoteCount());
        assertEquals(501, candidates.get(5).get(0).getVoteCount());

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
        assertFalse(candidates.get(0).get(0).isSat());
        assertFalse(candidates.get(0).get(1).isSat());
        assertTrue(candidates.get(0).get(2).isSat());
        assertTrue(candidates.get(0).get(3).isSat());
        assertTrue(candidates.get(0).get(4).isSat());
        assertTrue(candidates.get(0).get(5).isSat());
        assertFalse(candidates.get(0).get(6).isSat());
        assertTrue(candidates.get(0).get(7).isSat());
        // republican
        assertTrue(candidates.get(1).get(0).isSat());
        assertFalse(candidates.get(1).get(1).isSat());
        assertTrue(candidates.get(1).get(2).isSat());
        assertTrue(candidates.get(1).get(3).isSat());
        assertFalse(candidates.get(1).get(4).isSat());
        assertFalse(candidates.get(1).get(5).isSat());
        assertFalse(candidates.get(1).get(6).isSat());
        assertTrue(candidates.get(1).get(7).isSat());
        // independents
        assertTrue(candidates.get(2).get(0).isSat());
        assertFalse(candidates.get(3).get(0).isSat());
        assertTrue(candidates.get(4).get(0).isSat());
        assertFalse(candidates.get(5).get(0).isSat());
    }
}