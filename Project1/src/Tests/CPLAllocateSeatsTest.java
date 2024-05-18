/*
CPLAllocateSeatsTest.java

Unit tests for the allocatePartySeats() & assignCandidateSeats() methods for the CPLElection class.

*/

import AES.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class for testing AllocatePartySeats and CPLallocateCandidates
 * For AllocateParty seats the cases to consider are:
 * 1. PartyNumber=0 vs PartyNumber>0, however because the file is assumed to be formatted correctly, PartyNumber will
 * never be 0, so no test case needs to be made
 * 2. votesPerSeat=0 vs votesPerSeats>0, votes per seat is only 0 iff ballot count is 0, which is not an election
 * so no test cases need to be made
 * 3. FirstAllocation < candidates vs FirstAllocation >= candidates
 * a. hasCandidatesLeft[i] = false vs true, changes with 3 so no test case needs to be made
 * 4. remainingSeats < partyCount vs remainingSeats >= partyCount
 * 5. FirstAllocation < candidates from each party vs FirstAllocation >= candidates from each party
 * 6. TieStart=SeatsLeft-1  vs TieStart<SeatsLeft-1
 * 7. TieEnd=SeatsLeft-1  vs TieEnd>SeatsLeft-1
 * <p>
 * For CPLAllocateCandidates, the cases to consider are:
 * 1.  PartyNumber=0 vs PartyNumber>0, however because the file is assumed to be formatted correctly, PartyNumber will
 * never be 0, so no test case needs to be made
 * 2. TotalAllocation of Seats=0 vs >0
 */
public class CPLAllocateSeatsTest {
    @Test
    @DisplayName("CPLexample.csv")
        // Tests case where PartyNumber>0, votesPerSeat>0, First Allocation < candidates from each party,
        // remainingSeats < Party Count, First Allocation+ Second Allocation < candidates from each party,
        // tieStart=tieEnd=Seatsleft-1
    void testAllocatePartySeats1() {
        // Note that the allocatePartySeats method is shared between both CPL and OPL, so it only needs to be tested
        // for one type of election
        Election election = new CPLElection();
        FileInput fin = new FileInput("Project1/testing/testfiles/CPLexample.csv");
        assertFalse(fin.isOPL());
        // Because all the variables above are protected, the only way to change them is to run processBallotData
        // However, the values of the variables are guaranteed to be what we want them to be because of the assertEquals
        election.processBallotData(fin);
        Party[] parties = election.getParties();

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

        election.allocatePartySeats();
        int[][] allocationData = election.getAllocationData();

        assertArrayEquals(new int[][]{{3, 2, 0, 2, 1, 1}, {1, 0, 0, 0, 0, 0}, {0, 2, 0, 2, 1, 1}, {0, 1, 0, 1, 0, 0}, {1, 1, 0, 1, 0, 0}}, allocationData);
    }

    @Test
    @DisplayName("CPLexample.csv")
        // Tests case where PartyNumber>0, votesPerSeat=0, First Allocation < candidates from each party,
        // remainingSeats >= Party Count, First Allocation+ Second Allocation >= candidates from each party,
        // tieStart=tieEnd=Seatsleft-1
    void testAllocatePartySeats2() {
        // Note that the allocatePartySeats method is shared between both CPL and OPL, so it only needs to be tested
        // for one type of election
        Election election = new CPLElection();
        FileInput fin = new FileInput("Project1/testing/testfiles/CPLexample2.csv");
        assertFalse(fin.isOPL());

        election.processBallotData(fin);
        Party[] parties = election.getParties();


        assertEquals(10, election.getSeatCount());
        assertEquals(11, election.getBallotCount());
        assertEquals(4, election.getPartyCount());
        assertEquals(24, election.getCandidateCount());

        // In this case, there are not enough candidates in the independent party on first allocation
        // This also makes it so the number of remaining seats exceeds the party count and the first allocation + second
        // allocation of seats exceed the candidates from the second party, testing the alternate versions of cases
        // 3, 4 and 5.
        assertEquals(0, parties[0].getVoteCount());
        assertEquals(1, parties[1].getVoteCount());
        assertEquals(2, parties[2].getVoteCount());
        assertEquals(8, parties[3].getVoteCount());

        election.allocatePartySeats();
        int[][] allocationData = election.getAllocationData();
        boolean test1 = Arrays.deepEquals(allocationData, new int[][]{{0, 1, 2, 8}, {0, 0, 1, 1}, {0, 1, 0, 6}, {3, 3, 2, 0}, {3, 3, 3, 1}});
        assertTrue(test1);
    }

    @Test
    @DisplayName("CPLexampleTies.txt")
        // Tests case where PartyNumber>0, votesPerSeat>0, First Allocation < candidates from each party,
        // remainingSeats < Party Count, First Allocation+ Second Allocation < candidates from each party,
        // tieStart<Seatsleft-1<tieEnd
    void testAllocatePartySeats3() {
        Election election = new CPLElection();
        FileInput fin = new FileInput("Project1/testing/testfiles/CPLexampleTies.csv");
        assertFalse(fin.isOPL());

        election.processBallotData(fin);
        Party[] parties = election.getParties();


        assertEquals(3, election.getSeatCount());
        assertEquals(18, election.getBallotCount());
        assertEquals(6, election.getPartyCount());
        assertEquals(11, election.getCandidateCount());

        // In this case, after the first seats for parties 2 and 5 are taken, parties 1, 3 and 6 tie for the last seat
        assertEquals(2, parties[0].getVoteCount());
        assertEquals(6, parties[1].getVoteCount());
        assertEquals(2, parties[2].getVoteCount());
        assertEquals(0, parties[3].getVoteCount());
        assertEquals(6, parties[4].getVoteCount());
        assertEquals(2, parties[5].getVoteCount());

        election.allocatePartySeats();
        int[][] allocationData = election.getAllocationData();
        boolean test1 = Arrays.deepEquals(allocationData, new int[][]{{2, 6, 2, 0, 6, 2}, {0, 1, 0, 0, 1, 0}, {2, 0, 2, 0, 0, 2}, {1, 0, 0, 0, 0, 0}, {1, 1, 0, 0, 1, 0}});
        boolean test2 = Arrays.deepEquals(allocationData, new int[][]{{2, 6, 2, 0, 6, 2}, {0, 1, 0, 0, 1, 0}, {2, 0, 2, 0, 0, 2}, {0, 0, 0, 0, 0, 1}, {0, 1, 0, 0, 1, 1}});
        boolean test3 = Arrays.deepEquals(allocationData, new int[][]{{2, 6, 2, 0, 6, 2}, {0, 1, 0, 0, 1, 0}, {2, 0, 2, 0, 0, 2}, {0, 0, 1, 0, 0, 0}, {0, 1, 1, 0, 1, 0}});
        assertTrue(test1 ^ test2 ^ test3);
    }

    @Test
    @DisplayName("CPLexample.csv")
        // Tests allocatedSeats=0 for some parties and tests allocatedSeats!=0 for other parties
    void testAllocateCandidates() {
        Election election = new CPLElection();
        FileInput fin = new FileInput("Project1/testing/testfiles/CPLexample.csv");
        assertFalse(fin.isOPL());
        election.processBallotData(fin);
        election.allocatePartySeats();

        int[][] allocationData = election.getAllocationData();
        // Tests allocatedSeats=0 for parties 3, 5, and 6 and tests allocatedSeats!=0 for parties 1 ,2 and 4
        assertArrayEquals(new int[][]{{3, 2, 0, 2, 1, 1}, {1, 0, 0, 0, 0, 0}, {0, 2, 0, 2, 1, 1}, {0, 1, 0, 1, 0, 0}, {1, 1, 0, 1, 0, 0}}, allocationData);

        election.assignCandidateSeats();
        ArrayList<ArrayList<Candidate>> candidates = election.getCandidates();

        assertTrue(candidates.get(0).get(0).isSat());
        assertFalse(candidates.get(0).get(1).isSat());
        assertFalse(candidates.get(0).get(2).isSat());

        assertTrue(candidates.get(1).get(0).isSat());
        assertFalse(candidates.get(1).get(1).isSat());
        assertFalse(candidates.get(1).get(2).isSat());

        assertFalse(candidates.get(2).get(0).isSat());

        assertTrue(candidates.get(3).get(0).isSat());
        assertFalse(candidates.get(3).get(1).isSat());

        assertFalse(candidates.get(4).get(0).isSat());

        assertFalse(candidates.get(5).get(0).isSat());
    }
}
