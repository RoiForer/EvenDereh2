package test;
import java.util.HashSet;

public class CacheManager {
    
    private final int maxSize;
    private final CacheReplacementPolicy crp;
    private final HashSet<String> cache;

    public CacheManager(int maxSize, CacheReplacementPolicy crp) {
        this.maxSize = maxSize;
        this.crp = crp;
        this.cache = new HashSet<>();
    }

    public boolean query(String word) {
        if (cache.contains(word)) {
            return true;
        }
        return false;
    }

    public void add(String word) {
        crp.add(word);
        cache.add(word);

        // If the size exceeds the maximum size, remove a word from the cache
        if (cache.size() > maxSize) {
            String removedWord = crp.remove();
            cache.remove(removedWord);
        }
    }
    
}
