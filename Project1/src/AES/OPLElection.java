/*
OPLElection.java

The OPLElection class implements OPL election behaviors.

*/

package AES;

import java.util.*;


/**
 * Represents an Open Party List (OPL) election. This class is responsible for
 * managing and executing the specific operations related to OPL elections,
 * including processing ballot data, allocating seats, generating audit files,
 * and displaying winners.
 */
public class OPLElection extends Election {
    /**
     * Initializes an OPL election.
     */
    public OPLElection() {
        this.electionType = "OPL";
    }

    protected Map<Integer, Integer[]> ballotTo2DIndex;

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
        int candidateCount = Integer.parseInt(fin.getLine());

        // if we already set up our data structures, just consume the candidate lines, record the ballotCount and return
        if (candidates != null) {
            for (int i = 0; i < candidateCount; i++) {
                fin.getLine();
            }
            this.ballotCount += currentBallots;
            return currentBallots;
        }

        // initialize counts
        this.seatCount = seatCount;
        this.ballotCount = currentBallots;
        this.candidateCount = candidateCount;

        // use temporary ArrayList for parties, since partyCount is unknown
        ArrayList<String> partyList = new ArrayList<>();
        candidates = new ArrayList<>();

        // set/map to track unique parties
        Set<String> partySet = new HashSet<>();
        Map<String, Integer> partyToIndex = new HashMap<>();

        // map ballot index to party/candidate index
        ballotTo2DIndex = new HashMap<>();

        // capture the candidates
        for (int i = 0; i < candidateCount; i++) {
            // split the line by commas to get party/candidate names
            String[] tokens = fin.getLine().split(", ");
            String partyName = tokens[0];
            String candidateName = tokens[1];

            // initialize candidate
            Candidate candidate = new Candidate(candidateName);

            // if the party has not been seen before, add it
            if (!partySet.contains(partyName)) {
                partyToIndex.put(partyName, partyList.size());
                partyList.add(partyName);
                partySet.add(partyName);
                candidates.add(new ArrayList<>());
            }

            // add the candidate and record its party/candidate indices
            int partyIndex = partyToIndex.get(partyName);
            ArrayList<Candidate> partyCandidates = candidates.get(partyIndex);
            int candidateIndex = partyCandidates.size();
            partyCandidates.add(candidate);
            ballotTo2DIndex.put(i, new Integer[]{partyIndex, candidateIndex});
        }

        // capture the parties
        partyCount = partyList.size();
        parties = new Party[partyCount];
        for (int i = 0; i < partyCount; i++) {
            String partyName = partyList.get(i);
            boolean indep = partyName.contains("Independent");
            parties[i] = new Party(indep, partyName);
        }

        return currentBallots;
    }

    /**
     * Processes the ballot data from the specified FileInput object.
     *
     * @param fin the file input wrapper that contains the path to the election data file.
     */
    @Override
    public void processBallotData(FileInput fin) {
        int currentBallots = processHeaderData(fin);
        int[] voteCounts = fin.tallyVotes(candidateCount, currentBallots);
        fin.close();

        // record candidate votes
        for (int i = 0; i < candidateCount; i++) {
            Integer[] pcIndex = ballotTo2DIndex.get(i);
            int pIndex = pcIndex[0];
            int cIndex = pcIndex[1];
            candidates.get(pIndex).get(cIndex).setVoteCount(voteCounts[i]);
        }

        // calculate/record party votes
        for (int i = 0; i < partyCount; i++) {
            ArrayList<Candidate> partyCandidates = candidates.get(i);
            int partyVotes = 0;
            for (Candidate candidate : partyCandidates) {
                partyVotes += candidate.getVoteCount();
            }
            parties[i].setVoteCount(partyVotes);
        }
    }

    /**
     * Assigns seats to candidates based on the seats a party receives.
     */
    @Override
    public void assignCandidateSeats() {
        // for each party
        for (int partyNo = 0; partyNo < partyCount; partyNo++) {
            int seatNo = allocationData[4][partyNo];
            // if there are seats to allocate
            if (seatNo > 0) {
                // creates an array to store the relevant per candidates votes for each party
                int[][] sortedCandidates = new int[candidates.get(partyNo).size()][2];
                for (int i = 0; i < candidates.get(partyNo).size(); i++) {
                    sortedCandidates[i][1] = i;
                    sortedCandidates[i][0] = candidates.get(partyNo).get(i).voteCount;
                }
                Arrays.sort(sortedCandidates, Comparator.comparingInt(o -> Integer.MAX_VALUE - o[0]));
                // tieEnd represents the end of the tied region
                int tieEnd = seatNo - 1;
                while (tieEnd > -1 && tieEnd + 1 < sortedCandidates.length
                        && (sortedCandidates[tieEnd][0] == sortedCandidates[tieEnd + 1][0])) {
                    tieEnd++;
                }
                // tieStart represents the beginning of the tied region
                int tieStart = seatNo - 1;
                while (tieStart > 0 && (sortedCandidates[tieStart][0] == sortedCandidates[tieStart - 1][0])) {
                    tieStart--;
                }
                // gives the tiebreak indices.
                int[] indices = tiebreak(tieEnd - tieStart + 1, seatNo - tieStart);
                // gives seats to all candidates with more votes than the tied value
                for (int i = 0; i < tieStart; i++) {
                    candidates.get(partyNo).get(sortedCandidates[i][1]).giveSeat();
                }
                //  gives seats to all candidates that are tied based on the indices that were tiebroken
                for (int index : indices) {
                    candidates.get(partyNo).get(sortedCandidates[index + tieStart][1]).giveSeat();
                }
            }
        }
    }

    /**
     * Produces the list of parties and candidates for display, along with vote counts and proportions.
     *
     * @return the list as a displayable String.
     */
    @Override
    public String generatePartyCandidateList() {
        StringBuilder builder = new StringBuilder();
        builder.append("Parties & Candidates:\n");
        for (int i = 0; i < parties.length; i++) {
            int partyVotes = parties[i].getVoteCount();
            builder.append(" - Party: ").append(parties[i].getName())
                    .append(", Votes: ").append(partyVotes)
                    .append(" (").append(
                            String.format("%.2f%%", (double) partyVotes / ballotCount * 100)
                    ).append(" of total)").append("\n");
            for (Candidate candidate : candidates.get(i)) {
                int votes = candidate.getVoteCount();
                builder.append("   - Candidate: ").append(candidate.getName())
                        .append(", Votes: ").append(votes)
                        .append(" (").append(
                                String.format("%.2f%%", (double) votes / ballotCount * 100)
                        ).append(" of total)").append("\n");
            }
            builder.append("\n");
        }
        return builder.toString();
    }
}
