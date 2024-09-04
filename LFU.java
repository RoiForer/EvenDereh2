package test;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

public class LFU implements CacheReplacementPolicy {
    private final Map<String, Integer> frequencyMap;
    private final Map<Integer, LinkedHashSet<String>> frequencyToWordsMap;

    public LFU() {
        frequencyMap = new HashMap<>();
        frequencyToWordsMap = new HashMap<>();
    }

    @Override
    public void add(String word) {
        int frequency = frequencyMap.getOrDefault(word, 0) + 1;
        frequencyMap.put(word, frequency);

        LinkedHashSet<String> wordsAtFrequency = frequencyToWordsMap.computeIfAbsent(frequency, k -> new LinkedHashSet<>());
        wordsAtFrequency.remove(word); // Remove the word if it already exists in the set
        wordsAtFrequency.add(word); // Add the word to preserve the insertion order

        // Remove the word from any lower frequency sets
        for (int freq = 1; freq < frequency; freq++) {
            LinkedHashSet<String> lowerFrequencyWords = frequencyToWordsMap.get(freq);
            if (lowerFrequencyWords != null) {
                lowerFrequencyWords.remove(word);
            }
        }
    }

    @Override
    public String remove() {
        int minFrequency = Integer.MAX_VALUE;
        for (int frequency : frequencyToWordsMap.keySet()) {
            if (!frequencyToWordsMap.get(frequency).isEmpty()) {
                minFrequency = Math.min(minFrequency, frequency);
            }
        }

        if (minFrequency == Integer.MAX_VALUE) {
            return null; // Cache is empty
        }

        LinkedHashSet<String> leastFrequentWords = frequencyToWordsMap.get(minFrequency);
        String leastFrequentWord = leastFrequentWords.iterator().next();
        leastFrequentWords.remove(leastFrequentWord);

        if (leastFrequentWords.isEmpty()) {
            frequencyToWordsMap.remove(minFrequency);
        }

        frequencyMap.remove(leastFrequentWord);
        return leastFrequentWord;
    }
}
