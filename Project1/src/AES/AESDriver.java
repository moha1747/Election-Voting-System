/*
AESDriver.java

This file is the entry point for the program.

*/

package AES;

import static java.lang.System.exit;

/**
 * The driver class that will run the election, the entry point of the program.
 */
public class AESDriver {
    /**
     * Runs the AES.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        try {
            FileInput fin = args.length == 0 ? new FileInput() : new FileInput(args[0]);
            Election election = fin.isOPL() ? new OPLElection() : new CPLElection();
            election.processBallotData(fin);
            election.allocatePartySeats();
            election.assignCandidateSeats();
            election.displayWinners();
            election.generateAuditFile();
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
            exit(1);
        }
    }
}