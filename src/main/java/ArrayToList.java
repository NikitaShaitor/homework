import java.util.ArrayList;
import java.util.List;

public class ArrayToList {
    public ArrayToList(String[] stringArray) {

    }

    public static void main(String[] args) {
        String[] stringArray = {"Apple", "Banan", "Cherry"};
        ArrayList<String> fruitList = new ArrayList<>(List.of(stringArray));
        fruitList.add("Pineapple");
        System.out.println(fruitList);
    }
}
