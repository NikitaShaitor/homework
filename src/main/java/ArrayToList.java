import java.util.ArrayList;
import java.util.List;

public class ArrayToList {
    public static <T> ArrayList<T> toArrayList(T[] array) {
        return new ArrayList<>(List.of(array));
    }

    public static void main(String[] args) {
        String[] array = {"Apple", "Banana", "Cherry"};
        ArrayList<String> mutableList = toArrayList(array);
        System.out.println(mutableList);
    }
}

