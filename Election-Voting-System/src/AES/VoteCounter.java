/*
VoteCounter.java

The VoteCounter abstract class defines fields and behaviors shared by Party and Candidate: name and vote counts

*/

package AES;

/**
 * Abstract class designed to count votes. It provides the attributes
 * and methods necessary for counting votes for entities like candidates or parties in
 * an election.
 */
public abstract class VoteCounter {
    /**
     * The name of this entity.
     */
    protected String name;
    /**
     * The votes received by this entity.
     */
    protected int voteCount;

    /**
     * Constructs a new VoteCounter with the specified name and vote count set to zero.
     *
     * @param name the name of the entity (candidate or party) for which votes will be counted.
     */
    public VoteCounter(String name) {
        this.name = name;
        voteCount = 0;
    }

    /**
     * Retrieves the name of the entity.
     *
     * @return the name of the entity.
     */
    public String getName() {
        return name;
    }

    /**
     * Adds a specified number of votes to the current vote count.
     *
     * @param count the number of votes to add.
     * @return the new total vote count.
     */
    public int addVoteCount(int count) {
        return voteCount += count;
    }

    /**
     * Retrieves the current vote count.
     *
     * @return the current number of votes counted.
     */
    public int getVoteCount() {
        return voteCount;
    }

    /**
     * Sets the vote count to a specific value.
     *
     * @param count the number of votes to be set.
     */
    public void setVoteCount(int count) {
        voteCount = count;
    }
}
