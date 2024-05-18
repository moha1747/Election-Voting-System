/*
Party.java

The Party class stores data about election parties, including vote count and independence status.

*/

package AES;

/**
 * Represents a political party in an election. This class tracks the party's name,
 * whether it is independent, and other attributes relevant to an election process.
 */
public class Party extends VoteCounter {
    /**
     * Whether a party is independent.
     */
    protected boolean indep;

    /**
     * Constructs a new Party with the specified name. By default, the party is
     * not marked as independent.
     *
     * @param name the name of the party
     */
    public Party(String name) {
        super(name);
        indep = false;
    }

    /**
     * Constructs a new Party with the specified independence status and name.
     *
     * @param isIndep flag indicating whether the party is independent
     * @param name    the name of the party
     */
    public Party(boolean isIndep, String name) {
        super(name);
        indep = isIndep;
    }

    /**
     * Returns the party's independence status.
     *
     * @return true if the party is independent, false otherwise
     */
    public boolean isIndep() {
        return indep;
    }

    /**
     * Sets the party's independence status.
     *
     * @param isIndep flag indicating whether the party is independent
     */
    public void setIndep(boolean isIndep) {
        indep = isIndep;
    }

    /**
     * Represents the Party as a String.
     *
     * @return the String.
     */
    @Override
    public String toString() {
        return "Party{" +
                "indep=" + indep +
                ", name='" + name + '\'' +
                ", voteCount=" + voteCount +
                '}';
    }
}
