import java.util.*;

public class WordCounter {

    public static void main(String[] args) {
        String[] wordsArray = {
                "apple", "banana", "orange", "apple", "pineapple",
                "banana", "kiwi", "green", "apple", "java",
                "pineapple", "car", "kiwi", "cat", "dog"
        };
        Map<String, Integer> wordCountMap = new HashMap<>();
        for (String word : wordsArray) {
            wordCountMap.put(word, wordCountMap.getOrDefault(word, 0) + 1);
        }
        System.out.println("Spisok slov i kolichestvo:");
        System.out.println("---------------------------------------");
        for (Map.Entry<String, Integer> entry : wordCountMap.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + " raz");
        }
    }
}
