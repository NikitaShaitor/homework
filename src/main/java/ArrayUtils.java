public class ArrayUtils {
    public static <T> void swap(T[] array, int index1, int index2) {
        if (index1 < 0 || index1 >= array.length || index2 < 0 || index2 >= array.length) {
            throw new IndexOutOfBoundsException("Некорректные индексы");
        }

        T temp = array[index1];
        array[index1] = array[index2];
        array[index2] = temp;
    }

    public static void main(String[] args) {
        String[] strings = {"Dog", "Cat"};
        swap(strings, 0, 1);
        System.out.println(java.util.Arrays.toString(strings));

        Integer[] numbers = {123, 412, 643};
        swap(numbers, 0, 2);
        System.out.println(java.util.Arrays.toString(numbers));
    }
}
