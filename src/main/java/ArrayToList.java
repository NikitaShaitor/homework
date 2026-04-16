import java.util.ArrayList;
import java.util.List;

public class ArrayToList {
    public static void main(String[] args) {
        String[] array = {"Apple", "Banana", "Chery"};
        List<String> immutableList = List.of(array);
        ArrayList<String> mutableList = new ArrayList<>(immutableList);

        System.out.println(mutableList);
    }
}
