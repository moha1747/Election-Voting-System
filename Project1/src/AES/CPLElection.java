/*
CPLElection.java

The CPLElection class implements CPL election behaviors.

*/

package AES;

import java.util.*;

/**
 * Represents a Closed Party List (CPL) election. This class handles the specific
 * operations related to CPL elections, including processing ballot data, allocating
 * seats, generating audit files, and displaying winners.
 */
public class CPLElection extends Election {
    /**
     * Initializes a CPL election.
     */
    public CPLElection() {
        this.electionType = "CPL";
    }

    /**
     * Processes the header data from a file.
     *
     * @param fin the file input wrapper, where the next line available is the 2nd line of the original file.
     * @return the ballot count in the current file.
     */
    protected int processHeaderData(FileInput fin) {
        // consume the first 3 lines
        int seatCount = Integer.parseInt(fin.getLine());
        int currentBallots = Integer.parseInt(fin.getLine());
        int partyCount = Integer.parseInt(fin.getLine());

        // if we already set up our data structures, just consume the parties lines, record the ballotCount and return
        if (parties != null) {
            for (int i = 0; i < candidateCount; i++) {
                fin.getLine();
            }
            this.ballotCount += currentBallots;
            return currentBallots;
        }

        // initialize counts
        this.seatCount = seatCount;
        this.ballotCount = currentBallots;
        this.partyCount = partyCount;
        candidateCount = 0;

        parties = new Party[partyCount];
        candidates = new ArrayList<>(partyCount);

        // capture the parties and candidates
        for (int i = 0; i < partyCount; i++) {
            // split the line by commas to get party/candidate names
            String[] tokens = fin.getLine().split(", ");
            String partyName = tokens[0];

            // count candidates
            int candCount = tokens.length - 1;
            candidateCount += candCount;

            // initialize party
            parties[i] = partyName.equals("Independent") ? new Party(true, partyName) : new Party(partyName);

            // initialize candidates
            ArrayList<Candidate> cands = new ArrayList<>(candCount);
            for (int j = 0; j < candCount; j++) {
                cands.add(j, new Candidate(tokens[j + 1]));
            }
            candidates.add(i, cands);
        }

        return currentBallots;
    }

    /**
     * Processes the ballot data from the specified file input.
     *
     * @param fin the file input wrapper that contains the path to the election data file.
     */
    @Override
    public void processBallotData(FileInput fin) {
        int currentBallots = processHeaderData(fin);
        int[] voteCounts = fin.tallyVotes(partyCount, currentBallots);
        fin.close();

        // record votes
        for (int i = 0; i < partyCount; i++) {
            parties[i].setVoteCount(voteCounts[i]);
        }
    }

    /**
     * Allocates seats to candidates based on the votes received.
     * Does not return anything. Instead, it calculates the number of seats allocated to each party and stores
     * information about that allocation to allocationData.
     */
    @Override
    public void assignCandidateSeats() {
        // for each party
        for (int partyNo = 0; partyNo < partyCount; partyNo++) {
            // give seats to the first correct number of candidates
            for (int i = 0; i < allocationData[4][partyNo]; i++) {
                candidates.get(partyNo).get(i).giveSeat();
            }
        }

    }

    /**
     * Produces the list of parties and candidates for display, along with vote counts and proportions
     *
     * @return the list as a displayable String.
     */
    @Override
    protected String generatePartyCandidateList() {
        StringBuilder builder = new StringBuilder();
        builder.append("Parties & Candidates:\n");
        for (int i = 0; i < parties.length; i++) {
            int votes = parties[i].getVoteCount();
            builder.append(" - Party: ").append(parties[i].name)
                    .append(", Votes: ").append(votes)
                    .append(" (").append(
                            String.format("%.2f%%", (double) votes / ballotCount * 100)
                    ).append(" of total)").append("\n");
            for (Candidate candidate : candidates.get(i)) {
                builder.append("   - Candidate: ").append(candidate.getName()).append("\n");
            }
            builder.append("\n");
        }
        return builder.toString();
    }
}
