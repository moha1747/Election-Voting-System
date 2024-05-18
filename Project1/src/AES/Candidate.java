/*
Candidate.java

The Candidate class stores data about election candidates, including vote count and seat status.

*/

package AES;

/**
 * Represents a candidate in an election. This class extends VoteCounter to include
 * vote counting capabilities and manages additional attributes specific to a candidate,
 * such as their seating status in the election.
 */
public class Candidate extends VoteCounter {
    /**
     * Whether the candidate has won a seat.
     */
    protected boolean hasSeat;

    /**
     * Constructs a new Candidate with the given name and without a seat.
     *
     * @param name the name of the candidate.
     */
    public Candidate(String name) {
        super(name);
        hasSeat = false;
    }

    /**
     * Assigns a seat to the candidate, indicating that they have won a seat in the election.
     */
    public void giveSeat() {
        hasSeat = true;
    }

    /**
     * Checks if the candidate has been allocated a seat.
     *
     * @return true if the candidate has a seat, false otherwise.
     */
    public boolean isSat() {
        return hasSeat;
    }

    /**
     * Represents the Candidate as a String.
     *
     * @return the String.
     */
    @Override
    public String toString() {
        return "Candidate{" +
                "hasSeat=" + hasSeat +
                ", name='" + name + '\'' +
                ", voteCount=" + voteCount +
                '}';
    }
}
