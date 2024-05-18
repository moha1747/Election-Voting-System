/*
PartyTest.java

Unit tests for the Party class.

*/

import AES.Party;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class PartyTest {
    /**
     * Tests for the functionality of the constructor with only the name.
     */
    @Test
    @DisplayName("Constructor 1")
    void cons1() {
        Party party = new Party("partyName");
        assertEquals("partyName", party.getName());
        assertFalse(party.isIndep());
        assertEquals(0, party.getVoteCount());
    }

    /**
     * Tests for the functionality of the constructor with the name and independence status.
     */
    @Test
    @DisplayName("Constructor 2")
    void cons2() {
        Party party = new Party(true, "partyName");
        assertEquals("partyName", party.getName());
        assertTrue(party.isIndep());
        assertEquals(0, party.getVoteCount());
    }

    /**
     * Tests for the functionality of setIndep().
     */
    @Test
    @DisplayName("setIndep")
    void setIndep() {
        Party party = new Party("partyName");
        party.setIndep(true);
        assertTrue(party.isIndep());
        party.setIndep(true);
        assertTrue(party.isIndep());
        party.setIndep(false);
        assertFalse(party.isIndep());
    }

    /**
     * Tests for the correct output of toString().
     */
    @Test
    @DisplayName("toString")
    void testToString() {
        Party party = new Party("partyName");
        party.setIndep(true);
        party.setVoteCount(99);
        assertEquals("Party{indep=true, name='partyName', voteCount=99}", party.toString());
    }
}