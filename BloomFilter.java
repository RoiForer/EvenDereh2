package test;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.BitSet;
import java.util.List;
import java.util.Arrays;


public class BloomFilter {

    private BitSet bits;
    private List<String> algorithms;

    public BloomFilter(int size, String... algorithms) {
        this.bits = new BitSet(size);
        this.algorithms = Arrays.asList(algorithms);
    }
    
    public void add(String word) {
        for (String algorithm : algorithms) {
            int index = hash(word, algorithm) % bits.size();
            bits.set(index);
        }
    }

    private int hash(String word, String algorithm) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            byte[] bytes = md.digest(word.getBytes());
            BigInteger num = new BigInteger(1, bytes);
            return Math.abs(num.intValue());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public boolean contains(String word) {

        for (String algorithm : algorithms) {
            int index = hash(word, algorithm) % bits.size();
            if (!bits.get(index)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {

        int length = bits.length();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if (bits.get(i)) {
                sb.append("1");
            } else {
                sb.append("0");
            }
        }
        return sb.toString();
    }
}
