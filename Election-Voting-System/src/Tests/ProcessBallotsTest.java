/*
ProcessBallots.java

Unit tests for the processBallotData() method in both OPLElection and CPLElection.

*/

import AES.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ProcessBallotsTest {
    /**
     * Tests for CPL ballot processing.
     */
    @Test
    @DisplayName("CPL basic")
    void CPLbasic() {
        FileInput fin = new FileInput("Project1/testing/testfiles/CPLexample.csv");
        assertFalse(fin.isOPL());

        Election election = new CPLElection();
        election.processBallotData(fin);
        Party[] parties = election.getParties();
        ArrayList<ArrayList<Candidate>> candidates = election.getCandidates();

        // basic info
        assertEquals(3, election.getSeatCount());
        assertEquals(9, election.getBallotCount());
        assertEquals(6, election.getPartyCount());
        assertEquals(11, election.getCandidateCount());

        // party votes
        assertEquals(3, parties[0].getVoteCount());
        assertEquals(2, parties[1].getVoteCount());
        assertEquals(0, parties[2].getVoteCount());
        assertEquals(2, parties[3].getVoteCount());
        assertEquals(1, parties[4].getVoteCount());
        assertEquals(1, parties[5].getVoteCount());

        // party names
        assertEquals("Democratic", parties[0].getName());
        assertEquals("Republican", parties[1].getName());
        assertEquals("New Wave", parties[2].getName());
        assertEquals("Reform", parties[3].getName());
        assertEquals("Green", parties[4].getName());
        assertEquals("Independent", parties[5].getName());

        // candidate names
        assertEquals("Joe", candidates.get(0).get(0).getName());
        assertEquals("Sally", candidates.get(0).get(1).getName());
        assertEquals("Ahmed", candidates.get(0).get(2).getName());
        assertEquals("Allen", candidates.get(1).get(0).getName());
        assertEquals("Nikki", candidates.get(1).get(1).getName());
        assertEquals("Taihui", candidates.get(1).get(2).getName());
        assertEquals("Sarah", candidates.get(2).get(0).getName());
        assertEquals("Xinyue", candidates.get(3).get(0).getName());
        assertEquals("Nikita", candidates.get(3).get(1).getName());
        assertEquals("Bethany", candidates.get(4).get(0).getName());
        assertEquals("Mike", candidates.get(5).get(0).getName());
    }

    /**
     * Tests for OPL ballot processing.
     */
    @Test
    @DisplayName("OPL basic")
    void OPLbasic() {
        FileInput fin = new FileInput("Project1/testing/testfiles/OPLexample.csv");
        assertTrue(fin.isOPL());

        Election election = new OPLElection();
        election.processBallotData(fin);
        Party[] parties = election.getParties();
        ArrayList<ArrayList<Candidate>> candidates = election.getCandidates();

        // basic info
        assertEquals(2, election.getSeatCount());
        assertEquals(9, election.getBallotCount());
        assertEquals(3, election.getPartyCount());
        assertEquals(6, election.getCandidateCount());

        // candidate votes
        assertEquals(2, candidates.get(0).get(0).getVoteCount());
        assertEquals(1, candidates.get(0).get(1).getVoteCount());
        assertEquals(0, candidates.get(0).get(2).getVoteCount());
        assertEquals(2, candidates.get(1).get(0).getVoteCount());
        assertEquals(2, candidates.get(1).get(1).getVoteCount());
        assertEquals(2, candidates.get(2).get(0).getVoteCount());

        // party votes
        assertEquals(3, parties[0].getVoteCount());
        assertEquals(4, parties[1].getVoteCount());
        assertEquals(2, parties[2].getVoteCount());

        // candidate names
        assertEquals("Pike", candidates.get(0).get(0).getName());
        assertEquals("Lucy", candidates.get(0).get(1).getName());
        assertEquals("Beiye", candidates.get(0).get(2).getName());
        assertEquals("Etta", candidates.get(1).get(0).getName());
        assertEquals("Alawa", candidates.get(1).get(1).getName());
        assertEquals("Sasha", candidates.get(2).get(0).getName());

        // party names
        assertEquals("Democrat", parties[0].getName());
        assertEquals("Republican", parties[1].getName());
        assertEquals("Independent1", parties[2].getName());
    }

    /**
     * Tests for OPL ballot data processing if the candidates aren't grouped by party when listed
     */
    @Test
    @DisplayName("OPL shuffled order")
    void OPLshuffle() {
        FileInput fin = new FileInput("Project1/testing/testfiles/OPLexampleShuffled.csv");
        assertTrue(fin.isOPL());

        Election election = new OPLElection();
        election.processBallotData(fin);
        Party[] parties = election.getParties();
        ArrayList<ArrayList<Candidate>> candidates = election.getCandidates();

        // basic info
        assertEquals(3, election.getSeatCount());
        assertEquals(18, election.getBallotCount());
        assertEquals(4, election.getPartyCount());
        assertEquals(6, election.getCandidateCount());

        // candidate votes
        assertEquals(2, candidates.get(0).get(0).getVoteCount());
        assertEquals(6, candidates.get(1).get(0).getVoteCount());
        assertEquals(2, candidates.get(2).get(0).getVoteCount());
        assertEquals(0, candidates.get(1).get(1).getVoteCount());
        assertEquals(6, candidates.get(0).get(1).getVoteCount());
        assertEquals(2, candidates.get(3).get(0).getVoteCount());

        // party votes
        assertEquals(8, parties[0].getVoteCount());
        assertEquals(6, parties[1].getVoteCount());
        assertEquals(2, parties[2].getVoteCount());
        assertEquals(2, parties[3].getVoteCount());

        // candidate names
        assertEquals("Alice", candidates.get(0).get(0).getName());
        assertEquals("Bob", candidates.get(1).get(0).getName());
        assertEquals("Charles", candidates.get(2).get(0).getName());
        assertEquals("Dave", candidates.get(1).get(1).getName());
        assertEquals("Evelyn", candidates.get(0).get(1).getName());
        assertEquals("Fran", candidates.get(3).get(0).getName());

        // party names
        assertEquals("Democrat", parties[0].getName());
        assertEquals("Republican", parties[1].getName());
        assertEquals("Independent1", parties[2].getName());
        assertEquals("Independent2", parties[3].getName());

    }
}
