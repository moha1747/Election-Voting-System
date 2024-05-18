/*
OPLAllocateSeatsTest.java

Unit tests for the allocatePartySeats() & assignCandidateSeats() methods for the OPLElection class.

*/

import AES.Candidate;
import AES.Election;
import AES.FileInput;
import AES.OPLElection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class for testing OPLAllocateCandidates
 * Cases to consider are:
 * 1. seatNo=0 vs seatNo>0
 * 2. TieStart=seatNo-1  vs TieStart<seatNo-1
 * 3. TieEnd=seatNo-1  vs TieEnd>seatNo-1
 */
public class OPLAllocateSeatsTest {
    @Test
    @DisplayName("OPLexample.csv")
        // In this case,
    void testAllocateCandidates1() {
        // Note that the allocatePartySeats method is shared between both CPL and OPL, so it only needs to be tested
        // for one type of election
        Election election = new OPLElection();
        FileInput fin = new FileInput("Project1/testing/testfiles/OPLexample.csv");
        assertTrue(fin.isOPL());
        election.processBallotData(fin);
        election.allocatePartySeats();

        int[][] allocationData = election.getAllocationData();
        // This spread makes it so that the second party has a tie for the seat, the first party does not have a tie for
        // the seat, and the last party is assigned 0 seats, so for party 1, TieStart=TieEnd=seatNo-1, for party 2
        // TieStart=SeatsNo-1, TieEnd>SeatsNo-1 and for party 3, seatNo=0.
        boolean test1 = Arrays.deepEquals(allocationData, new int[][]{{3, 4, 2}, {0, 0, 0}, {3, 4, 2}, {1, 1, 0}, {1, 1, 0}});
        assertTrue(test1);

        election.assignCandidateSeats();
        ArrayList<ArrayList<Candidate>> candidates = election.getCandidates();

        assertTrue(candidates.get(0).get(0).isSat());
        assertFalse(candidates.get(0).get(1).isSat());
        assertFalse(candidates.get(0).get(2).isSat());

        assertTrue(candidates.get(1).get(0).isSat() ^ candidates.get(1).get(1).isSat());

        assertFalse(candidates.get(2).get(0).isSat());
    }

    @Test
    @DisplayName("OPLexample.csv")
    void testAllocateCandidates2() {
        // Note that the allocatePartySeats method is shared between both CPL and OPL so it only needs to be tested
        // for one type of election
        Election election = new OPLElection();
        FileInput fin = new FileInput("Project1/testing/testfiles/OPLexampleTies.csv");
        assertTrue(fin.isOPL());
        election.processBallotData(fin);
        election.allocatePartySeats();

        int[][] allocationData = election.getAllocationData();
        // This spread makes it so that the second party has a tie for the seat, the first party does not have a tie for
        // the seat, and the last party is assigned 0 seats, so for party 1, TieStart=TieEnd=seatNo-1, for party 2
        // TieStart<SeatsNo-1, TieEnd>SeatsNo-1 and for party 3, seatNo=0.
        boolean test1 = Arrays.deepEquals(allocationData, new int[][]{{3, 6, 1}, {0, 1, 0}, {3, 2, 1}, {1, 1, 0}, {1, 2, 0}});
        assertTrue(test1);

        election.assignCandidateSeats();
        ArrayList<ArrayList<Candidate>> candidates = election.getCandidates();

        assertTrue(candidates.get(0).get(0).isSat());
        assertFalse(candidates.get(0).get(1).isSat());
        assertFalse(candidates.get(0).get(2).isSat());

        // Tests if exactly 2 are true
        int sum = 0;
        for (int i = 0; i < 3; i++) {
            if (candidates.get(1).get(i).isSat()) {
                sum++;
            }
        }
        assertEquals(sum, 2);

        assertFalse(candidates.get(2).get(0).isSat());
    }
}
