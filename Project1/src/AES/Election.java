/*
Election.java

The Election abstract class defines fields and behaviors shared by OPLElection and CPLElection.

*/

package AES;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.IntStream;

/**
 * Abstract class representing a general election. This class defines the structure and
 * necessary operations for managing an election, including processing ballot data,
 * allocating seats, generating audit files, and displaying election winners.
 */
public abstract class Election {
    /**
     * The type of the election, {@code "OPL"} or {@code "CPL"}.
     */
    protected String electionType;

    /**
     * A length 5 array, where {@code allocationData[j][i]} contains the data for Party {@code i}. The data stored
     * depends on {@code j}: 0) votes cast; 1) first allocation; 2) remaining votes; 3) second allocation; 4) seats won.
     */
    protected int[][] allocationData;

    /**
     * The number of parties.
     */
    protected int partyCount;

    /**
     * The number of candidates.
     */
    protected int candidateCount;

    /**
     * The number of ballots cast.
     */
    protected int ballotCount;

    /**
     * The number of seats available.
     */
    protected int seatCount;

    /**
     * Stores the parties in the election, where the Party at index {@code i} has corresponding Candidate list at
     * index {@code i} of {@code candidates}.
     */
    protected Party[] parties;

    /**
     * Stores the candidates in the election, where the Candidate list at index {@code i} corresponds to the Party at
     * index {@code i} of {@code parties}.
     */
    protected ArrayList<ArrayList<Candidate>> candidates;

    /**
     * Performs a fair tiebreak and returns an array of indices.
     *
     * @param n the number of options to choose from; {@code n >= r}.
     * @param r the number of allowed winners; must be positive.
     * @return a sorted integer array containing {@code r} unique integers on the range {@code [0, n)}.
     * @throws RuntimeException if {@code n < r} or if {@code r <= 0}.
     */
    public int[] tiebreak(int n, int r) { // n choose r
        if (n < r || r <= 0) {
            throw new RuntimeException("Cannot tiebreak n=" + n + ", r=" + r + ". Need n >= r > 0.");
        }

        Random rand = new Random();
        List<Integer> choices = new ArrayList<>(IntStream.range(0, n).boxed().toList()); // ArrayList of [0,n)
        int[] selected = new int[r];
        Arrays.fill(selected, -1);

        // select r options, removing already taken choices
        for (int i = 0; i < r; i++) {
            int next = rand.nextInt(choices.size());
            selected[i] = choices.get(next);
            choices.remove(next);
        }

        // return sorted array
        Arrays.sort(selected);
        return selected;
    }

    /**
     * Processes the ballot data from the specified file input.
     *
     * @param fin the file input wrapper that contains the path to the election data file.
     */
    public abstract void processBallotData(FileInput fin);

    /**
     * Allocates seats to candidates based on the votes received.
     */
    public void allocatePartySeats() {
        allocationData = new int[5][partyCount];
        // array representing the parties that have candidates that can be allocated
        boolean[] hasCandidatesLeft = new boolean[partyCount];
        // int representing the number of parties with candidates that can be allocated
        int numberCandidatesLeft = partyCount;
        // evaluates votesPerSeat to determine first allocation of seats
        int votesPerSeat = (int) Math.ceil((double) ballotCount / seatCount);
        // variable representing the seats have been taken so far
        int seatsTaken = 0;
        // Sets first row equal to vote count and initializes 4th row to all 0s
        for (int i = 0; i < partyCount; i++) {
            allocationData[0][i] = parties[i].getVoteCount();
            allocationData[3][i] = 0;
            allocationData[1][i] = 0;
            hasCandidatesLeft[i] = true;
        }
        // if there is more than one ballot per seat
        if (votesPerSeat != 0) {
            for (int i = 0; i < partyCount; i++) {
                // Tests if the number of seats we allocate to a party is less than the number of candidates they have
                if (allocationData[0][i] / votesPerSeat < candidates.get(i).size()) {
                    allocationData[1][i] = allocationData[0][i] / votesPerSeat;
                } else {
                    // if it is not, first allocation is equal to the number of candidates that party has
                    allocationData[1][i] = candidates.get(i).size();
                    // then set variables to represent that there are no seats left
                    hasCandidatesLeft[i] = false;
                    numberCandidatesLeft--;
                }
                // The votes remaining can be found using this formula:
                allocationData[2][i] = allocationData[0][i] - allocationData[1][i] * votesPerSeat;
                // seats taken incremented based on the seats each party takes
                seatsTaken += allocationData[1][i];
            }
        } else {
            if (partyCount >= 0) System.arraycopy(allocationData[0], 0, allocationData[2], 0, partyCount);
        }

        // remaining seats is the amount of seats still left ot be allocated
        int remainingSeats = seatCount - seatsTaken;
        // System.out.println(numberCandidatesLeft);
        // if there are more seats left than parties that can have seats allocated to then
        while (remainingSeats >= numberCandidatesLeft) {
            for (int i = 0; i < partyCount; i++) {
                // give all of those parties a seat
                if (hasCandidatesLeft[i]) {
                    remainingSeats--;
                    allocationData[3][i]++;
                    // if adding seats this way makes a party use all of their candidates, changes variables
                    // to represent that there are no candidates left for that party to allocate
                    if (allocationData[1][i] + allocationData[3][i] >= candidates.get(i).size()) {
                        hasCandidatesLeft[i] = false;
                        // System.out.println("Removed " + i);
                        numberCandidatesLeft--;
                    }
                }
            }
        }

        // creates array temp which is a copy of the remaining seats for each party in column 0 and the party number
        // in column 1 containing only parties with candidates left, sorted by column 0
        int[][] temp = new int[numberCandidatesLeft][2];
        int nonEmptyParty = 0;
        if (remainingSeats != 0) {
            for (int i = 0; i < partyCount; i++) {
                if (hasCandidatesLeft[i]) {
                    temp[nonEmptyParty][1] = i;
                    temp[nonEmptyParty][0] = allocationData[2][i];
                    nonEmptyParty++;
                }
            }
            Arrays.sort(temp, Comparator.comparingInt(o -> Integer.MAX_VALUE - o[0]));
            // tieEnd represents the end of the tied region
            int tieEnd = remainingSeats - 1;
            while (tieEnd > -1 && tieEnd + 1 < temp.length && (temp[tieEnd][0] == temp[tieEnd + 1][0])) {
                tieEnd++;
            }
            // tieStart represents the beginning of the tied region
            int tieStart = remainingSeats - 1;
            while (tieStart > 0 && (temp[tieStart][0] == temp[tieStart - 1][0])) {
                tieStart--;
            }
            // gives the tiebreak indices.
            int[] indices = tiebreak(tieEnd - tieStart + 1, remainingSeats - tieStart);
            // gives seats to all parties with more votes than the tied value
            for (int iCount = 0; iCount < tieStart; iCount++) {
                allocationData[3][temp[iCount][1]]++;
                remainingSeats--;
            }
            //  gives seats to all parties that are tied based on the indices that were tiebroken
            for (int index : indices) {
                allocationData[3][temp[index + tieStart][1]]++;
                remainingSeats--;
            }
        }
        // finds total allocation of seats by adding first and second allocations
        for (int i = 0; i < partyCount; i++) {
            allocationData[4][i] = allocationData[3][i] + allocationData[1][i];
        }
    }

    protected abstract int processHeaderData(FileInput fin);

    /**
     * Assigns seats to candidates based on the seats a party receives.
     */
    public abstract void assignCandidateSeats();

    /**
     * Creates an audit file for the Election, detailing party, candidate,
     * and seat allocation information.
     *
     * @throws RuntimeException if the audit file could not be created.
     */
    public void generateAuditFile() throws RuntimeException {
        StringBuilder builder = new StringBuilder();

        // add basic info
        builder.append(generateElectionInfo()).append("\n");

        // add party/candidate list
        builder.append(generatePartyCandidateList()).append("\n");

        // LRA calculation
        builder.append("Largest Remainder Approach Calculation:\n");
        int votesPerSeat = (int) Math.ceil((double) ballotCount / seatCount);
        builder.append(" - Ceiling of (Total Votes / Number of Seats) = ceil(").append(ballotCount).append(" / ").append(seatCount)
                .append(") = ").append(votesPerSeat).append("\n\n");

        // allocation table
        builder.append(generateAllocationTable()).append("\n");

        // seat winners and their party affiliation
        builder.append(generateWinnerList());

        // create the audit file
        SimpleDateFormat timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SSS");
        Date date = new Date();
        String filename = electionType + "_AuditFile_" + timestamp.format(date) + ".txt";

        // write to file
        try (PrintWriter printWriter = new PrintWriter(filename)) {
            printWriter.write(builder.toString());
            printWriter.flush();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Error: Unable to create or write to the audit file");
        }

        System.out.println("\nAudit File Generated: " + filename);
    }

    /**
     * Produces the basic election information for display.
     *
     * @return the election information as a displayable String.
     */
    protected String generateElectionInfo() {
        return electionType + " Election:\n" +
                " - Party Count     : " + partyCount + "\n" +
                " - Candidate Count : " + candidateCount + "\n" +
                " - Ballot Count    : " + ballotCount + "\n" +
                " - Seat Count      : " + seatCount + "\n\n";
    }

    /**
     * Produces the list of parties and candidates for display, along with vote counts and proportions.
     *
     * @return the list as a displayable String.
     */
    protected abstract String generatePartyCandidateList();

    /**
     * Produces a readable table containing the party allocation data.
     *
     * @return the table as a displayable String.
     */
    protected String generateAllocationTable() {
        StringBuilder builder = new StringBuilder();
        builder.append("Seat Allocation Data:\n");

        final String headerFormat = " | %-15s | %10s | %25s | %15s | %26s | %16s | %23s |\n " + "-".repeat(152) + "\n";
        final String rowFormat = " | %-15s | %,10d | %,25d | %,15d | %,26d | %,16d |  %6.2f%%   /  %6.2f%%   |\n";
        String header = String.format(headerFormat, "Parties", "Votes", "First Allocation Of Seats", "Remaining Votes", "Second Allocation of Seats", "Final Seat Total", "% of Vote to % of Seats");
        builder.append(header);

        for (int i = 0; i < parties.length; i++) {
            String row = String.format(rowFormat,
                    parties[i].getName(), // Party name
                    allocationData[0][i], // Votes
                    allocationData[1][i], // First Allocation
                    allocationData[2][i], // Remaining Votes
                    allocationData[3][i], // Second Allocation
                    allocationData[4][i], // Final Total
                    (double) allocationData[0][i] / ballotCount * 100, // % of Votes
                    (double) allocationData[4][i] / seatCount * 100    // % of Seats
            );
            builder.append(row);
        }
        builder.append("\n");
        return builder.toString();
    }

    /**
     * Produces the list of winners and their party affiliation.
     *
     * @return the list as a displayable String.
     */
    protected String generateWinnerList() {
        StringBuilder builder = new StringBuilder();
        builder.append("Seat Winners and their Party Affiliation:\n");
        for (int partyIndex = 0; partyIndex < parties.length; partyIndex++) {
            ArrayList<Candidate> partyCandidates = candidates.get(partyIndex);
            String partyName = parties[partyIndex].getName();

            // Add all candidates who got a seat
            for (Candidate candidate : partyCandidates) {
                if (candidate.isSat()) {
                    builder.append(" - ").append(candidate.getName()).append(" (").append(partyName).append(")\n");
                }
            }
        }
        return builder.toString();
    }

    /**
     * Displays the winners of the election and other info.
     */
    public void displayWinners() {
        System.out.println(generateElectionInfo());
        System.out.println(generatePartyCandidateList());
        System.out.println(generateWinnerList());
    }

    /**
     * Gets election type.
     *
     * @return the election type, {@code "CPL"} or {@code "OPL"}.
     */
    public String getElectionType() {
        return electionType;
    }

    /**
     * Gets a deep copy of the allocation data.
     *
     * @return a deep copy of {@code allocationData}.
     */
    public int[][] getAllocationData() {
        int[][] copy = new int[5][partyCount];
        for (int i = 0; i < allocationData.length; i++) {
            copy[i] = Arrays.copyOf(allocationData[i], partyCount);
        }
        return copy;
    }

    /**
     * Gets party count.
     *
     * @return the number of parties.
     */
    public int getPartyCount() {
        return partyCount;
    }

    /**
     * Gets candidate count.
     *
     * @return the number of candidates.
     */
    public int getCandidateCount() {
        return candidateCount;
    }

    /**
     * Gets ballot count.
     *
     * @return the number of ballots cast.
     */
    public int getBallotCount() {
        return ballotCount;
    }

    /**
     * Gets seat count.
     *
     * @return the number of seats available.
     */
    public int getSeatCount() {
        return seatCount;
    }

    /**
     * Gets a deep copy of the parties.
     *
     * @return a deep copy of {@code parties}.
     */
    public Party[] getParties() {
        Party[] copy = new Party[partyCount];
        for (int i = 0; i < partyCount; i++) {
            copy[i] = new Party(parties[i].isIndep(), parties[i].getName());
            copy[i].setVoteCount(parties[i].getVoteCount());
        }
        return copy;
    }

    /**
     * Gets a deep copy of the candidates.
     *
     * @return a deep copy of {@code candidates}.
     */
    public ArrayList<ArrayList<Candidate>> getCandidates() {
        ArrayList<ArrayList<Candidate>> copy = new ArrayList<>();
        for (int i = 0; i < partyCount; i++) {
            ArrayList<Candidate> partyCandidates = candidates.get(i);
            ArrayList<Candidate> copyPartyCandidates = new ArrayList<>();
            for (int j = 0; j < partyCandidates.size(); j++) {
                Candidate candidate = partyCandidates.get(j);
                Candidate newCandidate = new Candidate(candidate.getName());
                newCandidate.setVoteCount(candidate.getVoteCount());
                if (candidate.isSat()) newCandidate.giveSeat();
                copyPartyCandidates.add(j, newCandidate);
            }
            copy.add(i, copyPartyCandidates);
        }
        return copy;
    }
}
