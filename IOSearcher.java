package test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class IOSearcher {
    public static boolean search(String word, String... fileNames) throws IOException {
        // Iterate through each file name provided
        for (String fileName : fileNames) {
            // Use try-with-resources to ensure the BufferedReader is closed after use
            try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
                String line;
                // Read each line of the file
                while ((line = br.readLine()) != null) {
                    // Split the line into words using space as a delimiter
                    for (String st : line.split(" ")) {
                        // Check if the current word matches the target word
                        if (word.equals(st)) {
                            return true; // Word found
                        }
                    }
                }
            }
        }
        return false; // Word not found in any of the files
    }
}
