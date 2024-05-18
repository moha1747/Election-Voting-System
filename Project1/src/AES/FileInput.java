/*
FileInput.java

The FileInput class wraps the file name input, validation, and read/close behaviors.

*/

package AES;

import java.io.*;
import java.util.Scanner;

/**
 * Handles file input operations for election data processing. This class is responsible
 * for validating the input file path, reading data from the file, and providing utility
 * methods to facilitate file operations needed during election processing.
 */
public class FileInput {
    /**
     * The filename of the data.
     */
    protected String filename;
    /**
     * The internal buffer used by FileInput to read in data.
     */
    protected BufferedReader buffer;

    /**
     * Constructs a FileInput object with a specified filename.
     *
     * @param filename the name of the file to be processed.
     * @throws RuntimeException if the file is not accessible.
     */
    public FileInput(String filename) throws RuntimeException {
        this.filename = filename;
        this.buffer = null;
        if (!validateFile()) {
            throw new RuntimeException("Error: File \"" + filename + "\" does not exist or has incorrect permissions");
        }
    }

    /**
     * Default constructor. Initiates prompting the user for a filename and
     * validates it.
     */
    public FileInput() {
        this.filename = "";
        this.buffer = null;
        promptFilename();
    }

    /**
     * Gets filename.
     *
     * @return the filename String.
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Prompts the user to enter a valid ballot data filename until a valid file
     * is provided.
     */
    protected void promptFilename() {
        Scanner scan = new Scanner(System.in);
        boolean validFile = false;
        while (!validFile) {
            System.out.print("Enter ballot data filename: ");
            this.filename = scan.nextLine();
            validFile = validateFile();
            if (!validFile) {
                System.out.print("Filename not valid.\n\n");
            }
        }
    }

    /**
     * Validates the existence and readability of the specified file.
     *
     * @return true if the file exists and is readable, false otherwise.
     */
    protected boolean validateFile() {
        if (this.filename.equals("")) {
            return false;
        }

        File file = new File(this.filename);
        if (!(file.exists() && file.canRead())) {
            return false;
        }

        FileReader reader;
        try {
            reader = new FileReader(file);
        } catch (FileNotFoundException e) {
            return false;
        }

        buffer = new BufferedReader(reader);
        return true;
    }

    /**
     * Consumes and returns the next line from the file.
     *
     * @return the line read from the file.
     * @throws RuntimeException if the file could not be read.
     */
    public String getLine() throws RuntimeException {
        String line = "";
        try {
            line = buffer.readLine();
        } catch (IOException e) {
            throw new RuntimeException("Error: Could not read file \"" + filename + "\"");
        }
        return line;
    }

    /**
     * Closes the file.
     *
     * @throws RuntimeException if the file could not be closed.
     */
    public void close() {
        try {
            buffer.close();
        } catch (IOException e) {
            throw new RuntimeException("Error: Could not close file \"" + filename + "\"");
        }
    }

    /**
     * Determines whether the file content indicates an OPL (Open Party List) election type.
     *
     * @return true if the first line of the file specifies an OPL election, false otherwise.
     */
    public boolean isOPL() {
        String electionType = getLine();
        return electionType.equals("OPL");
    }

    /**
     * Tallies {@code ballotCount} ballots from the current file. The next unconsumed line must be the first ballot
     * to tally.
     *
     * @param votableCount the number of votable entities on the ballot
     * @param ballotCount the number of ballots to tally
     * @return an int array of all the vote counts
     */
    public int[] tallyVotes(int votableCount, int ballotCount) {
        // tally votes
        int[] votes = new int[votableCount];
        for (int i = 0; i < ballotCount; i++) {
            // if there are k commas before the '1', then the '1' is at index k.
            String[] tokens = getLine().split("1");
            int index = tokens[0].length();
            votes[index] += 1;
        }
        return votes;
    }
}
