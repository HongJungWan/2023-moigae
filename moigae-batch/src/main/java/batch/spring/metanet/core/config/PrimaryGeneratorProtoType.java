package batch.spring.metanet.core.config;

import java.util.Random;

public class PrimaryGeneratorProtoType {
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz0123456789";
    private static final Random RANDOM = new Random();

    public static String generate() {
        return "c" + encodeTimestamp(System.currentTimeMillis()) + generateRandomString(10);
    }

    public static String encodeTimestamp(long timestamp) {
        StringBuilder result = new StringBuilder();
        while (timestamp > 0) {
            result.append(ALPHABET.charAt((int) (timestamp % ALPHABET.length())));
            timestamp /= ALPHABET.length();
        }
        while (result.length() < 8) {
            result.append('0');
        }
        return result.toString();
    }

    public static String generateRandomString(int length) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < length; i++) {
            result.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return result.toString();
    }
}