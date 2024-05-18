/*
CandidateTest.java

Unit tests for the Candidate class.

*/

import AES.Candidate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class CandidateTest {
    /**
     * Tests for the functionality of the constructor.
     */
    @Test
    @DisplayName("Constructor")
    void cons() {
        Candidate candidate = new Candidate("candidateName");
        assertEquals("candidateName", candidate.getName());
        assertFalse(candidate.isSat());
        assertEquals(0, candidate.getVoteCount());
    }


    /**
     * Tests for the functionality and idempotency of giveSeat().
     */
    @Test
    @DisplayName("giveSeat")
    void giveSeat() {
        Candidate candidate = new Candidate("candidateName");
        assertFalse(candidate.isSat());
        candidate.giveSeat();
        assertTrue(candidate.isSat());
        candidate.giveSeat();
        assertTrue(candidate.isSat());
    }

    /**
     * Tests for the correct output of toString().
     */
    @Test
    @DisplayName("toString")
    void testToString() {
        Candidate candidate = new Candidate("candidateName");
        candidate.giveSeat();
        candidate.setVoteCount(99);
        assertEquals("Candidate{hasSeat=true, name='candidateName', voteCount=99}", candidate.toString());
    }
}