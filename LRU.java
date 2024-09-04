package test;
import java.util.LinkedHashMap;


public class LRU implements CacheReplacementPolicy {

    private final int maxSize;
    private final LinkedHashMap<String, Integer> cache;

    public LRU() {
        this.maxSize = 10; // You can adjust the maxSize as needed
        this.cache = new LinkedHashMap<>(maxSize, 0.75f, true); // Specify type arguments here
    }


    @Override
    public void add(String item) {
        cache.put(item, cache.size());
    }

    @Override
    public String remove() {
        return cache.keySet().iterator().next();
    }
}


