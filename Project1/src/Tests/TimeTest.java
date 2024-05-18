/*
TimeTest.java

System tests for the 100,000 ballots in 4 minutes time constraint.

*/

import AES.AESDriver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class TimeTest {
    /**
     * Verify CPL elections can handle at least 100,000 ballots in no more than 4 minutes
     */
    @Test
    @DisplayName("large CPL")
    void testCPL() {
        String[] CPLargs = new String[]{"Project1/testing/testfiles/CPLexampleLarge.csv"};

        final long startTime = System.currentTimeMillis();
        AESDriver.main(CPLargs);
        final long endTime = System.currentTimeMillis();

        assertTrue(endTime - startTime < 4 * 60 * 1000);
    }

    /**
     * Verify OPL elections can handle at least 100,000 ballots in no more than 4 minutes
     */
    @Test
    @DisplayName("large OPL")
    void testOPL() {
        String[] OPLargs = new String[]{"Project1/testing/testfiles/OPLexampleLarge.csv"};

        final long startTime = System.currentTimeMillis();
        AESDriver.main(OPLargs);
        final long endTime = System.currentTimeMillis();

        assertTrue(endTime - startTime < 4 * 60 * 1000);
    }
}
