/*
PartyCandidateListTest.java

Unit tests for the generatePartyCandidateList() method in both OPLElection and CPLElection.

*/

import AES.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PartyCandidateListTest {
    @Test
    @DisplayName("OPLPartyCandidateList()")
    void testOPLPartyCandidateList() {
        // Setting data
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

        FakeElectionOPL election = new FakeElectionOPL("OPL", allocationData, 2, 2, 300,
                1, parties, candidates);
        // Compares the output of the function with what is expected
        String compare = election.generatePartyCandidateList();
        assertEquals("Parties & Candidates:\n" +
                " - Party: p1, Votes: 100 (33.33% of total)\n" +
                "   - Candidate: Alice, Votes: 100 (33.33% of total)\n" +
                "\n" +
                " - Party: p2, Votes: 200 (66.67% of total)\n" +
                "   - Candidate: Bob, Votes: 200 (66.67% of total)\n\n", compare);
    }

    @Test
    @DisplayName("CPLPartyCandidateList()")
    void testCPLPartyCandidateList() {
        // Setting data
        Party[] parties = {new Party("p1"), new Party("p2")};
        parties[0].setVoteCount(100);
        parties[1].setVoteCount(200);

        Candidate alice = new Candidate("Alice");
        Candidate bob = new Candidate("Bob");
        bob.giveSeat();

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

        // Generates the election
        FakeElectionCPL election = new FakeElectionCPL("OPL", allocationData, 2, 2, 300,
                1, parties, candidates);
        // runs the command in question and compares its output
        String compare = election.generatePartyCandidateList();
        assertEquals("Parties & Candidates:\n" +
                " - Party: p1, Votes: 100 (33.33% of total)\n" +
                "   - Candidate: Alice\n" +
                "\n" +
                " - Party: p2, Votes: 200 (66.67% of total)\n" +
                "   - Candidate: Bob\n\n", compare);
    }

    // Version of ElectionOPL that has a constructor that makes it easy to perform unit testing
    private static class FakeElectionOPL extends OPLElection {
        public FakeElectionOPL(String electionType, int[][] allocationData, int partyCount, int candidateCount,
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
        public String generatePartyCandidateList() {
            return super.generatePartyCandidateList();
        }

        @Override
        public void processBallotData(FileInput fin) {

        }

        @Override
        public void assignCandidateSeats() {

        }
    }

    // Version of ElectionCPL that has a constructor that makes it easy to perform unit testing
    private static class FakeElectionCPL extends CPLElection {
        public FakeElectionCPL(String electionType, int[][] allocationData, int partyCount, int candidateCount,
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
        public String generatePartyCandidateList() {
            return super.generatePartyCandidateList();
        }

        @Override
        public void processBallotData(FileInput fin) {

        }

        @Override
        public void assignCandidateSeats() {

        }
    }
}


