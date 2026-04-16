import java.util.*;

public class WordCounter {

    public static void main(String[] args) {
        String[] wordsArray = {
                "apple", "banana", "orange", "apple", "pineapple",
                "banana", "kiwi", "green", "apple", "java",
                "pineapple", "car", "kiwi", "cat", "dog", "orange"
        };
        Map<String, Integer> wordCountMap = new HashMap<>();
        for (String word : wordsArray) {
            wordCountMap.put(word, wordCountMap.getOrDefault(word, 0) + 1);
        }
        System.out.println("List of words and quantity:");
        System.out.println("---------------------------------------");
        for (Map.Entry<String, Integer> entry : wordCountMap.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + " once");
        }

        System.out.println("\nУникальные слова (встречаются только 1 раз):");
        boolean foundUnique = false;
        for (Map.Entry<String, Integer> entry : wordCountMap.entrySet()) {
            if (entry.getValue() == 1) {
                System.out.print(entry.getKey() + ", ");
                foundUnique = true;
            }
        }

        if (!foundUnique) {
            System.out.println("Уникальных слов нет.");
        }
    }
}
