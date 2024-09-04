package test;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



public class Dictionary {

    private final CacheManager existingWordsCache;
    private final CacheManager nonExistingWordsCache;
    private final BloomFilter bloomFilter;
    private final String[] fileNames;

    public Dictionary(String... fileNames) {

        this.fileNames = fileNames;

        // Create CacheManager for existing words with LRU replacement policy
        existingWordsCache = new CacheManager(400, new LRU());

        // Create CacheManager for non-existing words with LFU replacement policy
        nonExistingWordsCache = new CacheManager(100, new LFU());

        // Create BloomFilter with size 256 and hash functions MD5 and SHA1
        bloomFilter = new BloomFilter(256, "MD5", "SHA1");

        // Insert every word from the files into BloomFilter
        for (String fileName : fileNames) {
            List<String> words = readFile(fileName);
            for (String word : words) {
                bloomFilter.add(word);
            }
        }

    }

    // Method to read words from a file
    private List<String> readFile(String fileName) {
        List<String> words = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Split the line into words
                String[] lineWords = line.split("\\s+");
                // Add each word to the list of words
                for (String word : lineWords) {
                    words.add(word);
                }
            }
        }catch (FileNotFoundException e) {
            // Handle the FileNotFoundException
            e.printStackTrace();
        } catch (IOException e) {
            // Handle other IOExceptions
            e.printStackTrace();
        }
        return words;
    }

    public boolean query(String word) {

         // Check if word exists in CacheManager for existing words
         if (existingWordsCache.query(word)) {
            return true;
        }

        // Check if word exists in CacheManager for non-existing words
        if (nonExistingWordsCache.query(word)) {
            return false;
        }

        // Check if word exists in BloomFilter
        boolean isExists = bloomFilter.contains(word);

        // Update the appropriate CacheManager based on the result from BloomFilter
        if (isExists) {
            existingWordsCache.add(word);
        }
        
        else {
            nonExistingWordsCache.add(word);
        }

        return isExists;

    }

    public boolean challenge(String word) {
        try {
            // Use IOSearcher to search for the word in the text files
            //boolean foundInFiles = IOSearcher.search(word,fileNames);

            for (String fileName : fileNames) {
                if (IOSearcher.search(word, fileName)) {
                    existingWordsCache.add(word);
                    return true; // Word found in this file, no need to search further
                }
            }
            nonExistingWordsCache.add(word);
            return false;
        } catch (IOException e) {
            // Handle any exceptions by returning false
            e.printStackTrace();
            return false;
        }
    }
    
}

