/*
FileInputTest.java

Unit tests for the FileInput class.

*/

import AES.FileInput;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class FileInputTest {
    private FileInput fin;

    /**
     * Tests the FileInput constructor, isOPL(), getLine(), and close() methods on an OPL election.
     */
    @Test
    @DisplayName("OPLexample.csv")
    void testOPL() {
        fin = new FileInput("Project1/testing/testfiles/OPLexample.csv");
        assertTrue(fin.isOPL());
        assertEquals("2", fin.getLine());
        assertDoesNotThrow(() -> fin.close());
    }

    /**
     * Tests the FileInput constructor, isOPL(), getLine(), and close() methods on a CPL election.
     */
    @Test
    @DisplayName("CPLexample.csv")
    void testCPL() {
        fin = new FileInput("Project1/testing/testfiles/CPLexample.csv");
        assertFalse(fin.isOPL());
        assertEquals("3", fin.getLine());
        assertDoesNotThrow(() -> fin.close());
    }

    /**
     * Tests the exception thrown for an empty filename
     */
    @Test
    @DisplayName("\"\"")
    void testEmptyString() {
        assertThrows(RuntimeException.class, () -> new FileInput(""));
    }

    /**
     * Tests the exception thrown for a nonexistent filename
     */
    @Test
    @DisplayName("garbageFILENAME0293v8woemlrivjm02")
    void testGarbageInput() {
        assertThrows(RuntimeException.class, () -> new FileInput("garbageFILENAME0293v8woemlrivjm02"));
    }

    /**
     * Tests the functionality of the user prompting logic
     */
    @Test
    @DisplayName("user prompting")
    void prompt1() {
        InputStream systemIn = System.in;

        String input = "garbage1\ngarbage2\nProject1/testing/testfiles/OPLexample.csv\n";
        ByteArrayInputStream testIn = new ByteArrayInputStream(input.getBytes());
        System.setIn(testIn);

        FileInput fin = new FileInput();
        System.setIn(systemIn);

        assertEquals("Project1/testing/testfiles/OPLexample.csv", fin.getFilename());
    }
}