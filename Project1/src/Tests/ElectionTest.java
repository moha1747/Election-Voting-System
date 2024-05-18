/*
ElectionTest.java

Unit tests for some Election class methods (the major methods are tested elsewhere).

*/

import AES.Candidate;
import AES.Election;
import AES.FileInput;
import AES.Party;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

public class ElectionTest {

    /**
     * Tests that all getters return correct data or deep copy of data.
     */
    @Test
    @DisplayName("test getters")
    void getters() {
        int[][] allocationData = new int[][]{{1, 2}, {3, 4}, {5, 6}, {7, 8}, {9, 0}};

        Party[] parties = new Party[]{new Party("p1"), new Party("p2")};
        parties[0].setVoteCount(5);
        parties[1].setVoteCount(6);
        parties[1].setIndep(true);

        Candidate alice = new Candidate("Alice");
        alice.setVoteCount(9);
        Candidate bob = new Candidate("Bob");
        bob.giveSeat();
        bob.setVoteCount(10);

        ArrayList<ArrayList<Candidate>> candidates = new ArrayList<>();
        candidates.add(new ArrayList<>());
        candidates.get(0).add(alice);
        candidates.add(new ArrayList<>());
        candidates.get(1).add(bob);

        Election election = new FakeElection("OPL", allocationData, 2, 2, 3,
                1, parties, candidates);

        // basic info is right
        assertEquals("OPL", election.getElectionType());
        assertEquals(2, election.getPartyCount());
        assertEquals(2, election.getCandidateCount());
        assertEquals(3, election.getBallotCount());
        assertEquals(1, election.getSeatCount());

        // run getters on objects
        // all returned objects should be different objects storing the same data.
        int[][] gotAllocationData = election.getAllocationData();
        Party[] gotParties = election.getParties();
        ArrayList<ArrayList<Candidate>> gotCandidates = election.getCandidates();

        // allocation data
        assertNotSame(allocationData, election.getAllocationData());
        assertEquals(allocationData.length, gotAllocationData.length);
        assertEquals(allocationData[0].length, gotAllocationData[0].length);
        for (int i = 0; i < allocationData.length; i++) {
            assertNotSame(allocationData[i], gotAllocationData[i]);
            for (int j = 0; j < allocationData[0].length; j++) {
                assertEquals(allocationData[i][j], gotAllocationData[i][j]);
            }
        }

        // parties
        assertNotSame(parties, election.getParties());
        assertEquals(parties.length, gotParties.length);
        for (int i = 0; i < parties.length; i++) {
            assertNotSame(parties[i], gotParties[i]);
            assertEquals(parties[i].getName(), gotParties[i].getName());
            assertEquals(parties[i].getVoteCount(), gotParties[i].getVoteCount());
            assertEquals(parties[i].isIndep(), gotParties[i].isIndep());
        }

        // candidates
        assertNotSame(candidates, election.getCandidates());
        assertEquals(candidates.size(), gotCandidates.size());
        for (int i = 0; i < candidates.size(); i++) {
            assertNotSame(candidates.get(i), gotCandidates.get(i));
            assertEquals(candidates.get(i).size(), gotCandidates.get(i).size());
            for (int j = 0; j < candidates.get(i).size(); j++) {
                Candidate candidate = candidates.get(i).get(j);
                Candidate gotCandidate = gotCandidates.get(i).get(j);
                assertNotSame(candidate, gotCandidate);
                assertEquals(candidate.getName(), gotCandidate.getName());
                assertEquals(candidate.getVoteCount(), gotCandidate.getVoteCount());
                assertEquals(candidate.isSat(), gotCandidate.isSat());
            }
        }
    }

    /**
     * Tests that generateElectionInfo() is correctly generating the String from class fields.
     */
    @Test
    @DisplayName("generateElectionInfo()")
    void generateElectionInfo() {
        FakeElection election = new FakeElection("OPL", null, 9, 8, 7,
                6, null, null);
        assertEquals(
                "OPL Election:\n - Party Count     : 9\n - Candidate Count : 8\n - Ballot Count    : 7\n - Seat Count      : 6\n\n",
                election.generateElectionInfo()
        );
    }

    /**
     * Tests that generateWinnerList() is correctly generating the String from class fields.
     */
    @Test
    @DisplayName("generateWinnerList()")
    void generateWinnerList() {
        Party[] parties = {new Party("p1"), new Party("p2")};

        Candidate alice = new Candidate("Alice");
        alice.setVoteCount(9);
        Candidate bob = new Candidate("Bob");
        bob.giveSeat();
        bob.setVoteCount(10);

        ArrayList<ArrayList<Candidate>> candidates = new ArrayList<>();
        candidates.add(new ArrayList<>());
        candidates.get(0).add(alice);
        candidates.add(new ArrayList<>());
        candidates.get(1).add(bob);

        FakeElection election = new FakeElection("OPL", null, 2, 2, 7,
                6, parties, candidates);
        String expected = "Seat Winners and their Party Affiliation:\n - Bob (p2)\n";
        assertEquals(expected, election.generateWinnerList());
    }

    /**
     * Tests that generateAllocationTable() is correctly generating the String from class fields.
     */
    @Test
    @DisplayName("generateAllocationTable()")
    void generateAllocationTable() {
        Party[] parties = {new Party("p1"), new Party("p2")};
        parties[0].setVoteCount(100);
        parties[1].setVoteCount(200);

        Candidate alice = new Candidate("Alice");
        alice.setVoteCount(100);
        Candidate bob = new Candidate("Bob");
        bob.giveSeat();
        bob.setVoteCount(200);

        ArrayList<ArrayList<Candidate>> candidates = new ArrayList<>();
        candidates.add(new ArrayList<>());
        candidates.get(0).add(alice);
        candidates.add(new ArrayList<>());
        candidates.get(1).add(bob);

        int[][] allocationData = new int[][]{
                {100, 200},
                {0, 0},
                {100, 200},
                {0, 1},
                {0, 1}
        };

        FakeElection election = new FakeElection("OPL", allocationData, 2, 2, 300,
                1, parties, candidates);
        String expected =
                "Seat Allocation Data:\n" +
                        " | Parties         |      Votes | First Allocation Of Seats | Remaining Votes | Second Allocation of Seats | Final Seat Total | % of Vote to % of Seats |\n" +
                        " --------------------------------------------------------------------------------------------------------------------------------------------------------\n" +
                        " | p1              |        100 |                         0 |             100 |                          0 |                0 |   33.33%   /    0.00%   |\n" +
                        " | p2              |        200 |                         0 |             200 |                          1 |                1 |   66.67%   /  100.00%   |\n\n";
        assertEquals(expected, election.generateAllocationTable());
    }

    /**
     * An implementation of Election that provides a constructor and public overrides to help test concrete methods.
     */
    private static class FakeElection extends Election {
        public FakeElection(String electionType, int[][] allocationData, int partyCount, int candidateCount,
                            int ballotCount, int seatCount, Party[] parties, ArrayList<ArrayList<Candidate>> candidates) {
            this.electionType = electionType;
            this.allocationData = allocationData;
            this.partyCount = partyCount;
            this.candidateCount = candidateCount;
            this.ballotCount = ballotCount;
            this.seatCount = seatCount;
            this.parties = parties;
            this.candidates = candidates;
        }

        public String generateElectionInfo() {
            return super.generateElectionInfo();
        }

        public String generateWinnerList() {
            return super.generateWinnerList();
        }

        public String generateAllocationTable() {
            return super.generateAllocationTable();
        }

        @Override
        public void processBallotData(FileInput fin) {

        }

        @Override
        public void assignCandidateSeats() {

        }

        @Override
        protected String generatePartyCandidateList() {
            return null;
        }
    }
}
