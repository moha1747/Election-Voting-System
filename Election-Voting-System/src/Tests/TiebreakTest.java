/*
Tiebreak.java

Unit tests for the tiebreak() method in the Election class.

*/

import AES.Election;
import AES.FileInput;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class TiebreakTest {
    private final Election election = new FakeElection();

    /**
     * Tests tiebreak on a given n and r.
     *
     * @param n number of options
     * @param r number of allowed winners
     */
    void testChoose(int n, int r) {
        int[] result = election.tiebreak(n, r);

        // check that result is not empty
        assertNotEquals(0, result.length);

        // check that r indices were chosen
        assertEquals(r, result.length);

        // check that array is sorted
        int[] sorted = result.clone();
        Arrays.sort(sorted);
        assertArrayEquals(sorted, result);

        // check that all entries are unique and in range
        boolean allUnique = true;
        boolean allInRange = true;
        for (int i = 1; i < r; i++) {
            if (result[i] == result[i - 1]) {
                allUnique = false;
                break;
            }
        }
        assertTrue(allUnique);
        assertTrue(allInRange);
    }

    @Test
    @DisplayName("n=2,r=1")
    void testN2R1() {
        testChoose(2, 1);
    }

    @Test
    @DisplayName("n=8,r=5")
    void testN8R5() {
        testChoose(8, 5);
    }

    @Test
    @DisplayName("n=8,r=1")
    void testN8R1() {
        testChoose(8, 1);
    }

    @Test
    @DisplayName("n=1,r=1")
    void testN1R1() {
        testChoose(1, 1);
    }

    /**
     * An implementation of Election to test the concrete tiebreak() method.
     */
    private static class FakeElection extends Election {
        @Override
        public void processBallotData(FileInput fin) {

        }

        @Override
        public void assignCandidateSeats() {

        }

        @Override
        public void generateAuditFile() {

        }

        @Override
        protected String generatePartyCandidateList() {
            return null;
        }

        @Override
        public void displayWinners() {

        }
    }
}