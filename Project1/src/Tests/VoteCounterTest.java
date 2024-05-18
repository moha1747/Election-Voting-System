/*
VoteCounterTest.java

Unit tests for the VoteCounter class.

*/

import AES.VoteCounter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


class VoteCounterTest {
    /**
     * Tests the functionality of the addVoteCount() method.
     */
    @Test
    @DisplayName("addVoteCount")
    void addVoteCount() {
        VoteCounter vc = new FakeVoteCounter("name");
        vc.addVoteCount(11);
        assertEquals(11, vc.getVoteCount());
        vc.addVoteCount(5);
        assertEquals(16, vc.getVoteCount());
        vc.addVoteCount(99);
        assertEquals(115, vc.getVoteCount());
    }

    /**
     * Tests the functionality of the setVoteCount() method.
     */
    @Test
    @DisplayName("setVoteCount")
    void setVoteCount() {
        VoteCounter vc = new FakeVoteCounter("name");
        vc.setVoteCount(11);
        assertEquals(11, vc.getVoteCount());
        vc.setVoteCount(5);
        assertEquals(5, vc.getVoteCount());
        vc.setVoteCount(99);
        assertEquals(99, vc.getVoteCount());
    }

    private static class FakeVoteCounter extends VoteCounter {
        public FakeVoteCounter(String name) {
            super(name);
        }
    }
}