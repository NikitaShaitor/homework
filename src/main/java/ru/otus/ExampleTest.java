package ru.otus;

public class ExampleTest {

    private int counter = 0;

    @Before
    public void setup() {
        System.out.println("Выполнение метода Before");
        this.counter++;
    }

    @Test
    public void testMethod1() throws Exception {
        if(counter != 1) throw new Exception();
        System.out.println("Метод Test #1 прошёл успешно");
    }

    @Test
    public void testMethod2() throws Exception {
        if(counter != 1) throw new Exception();
        System.out.println("Метод Test #2 прошёл успешно");
    }

    @After
    public void teardown() {
        System.out.println("Выполнение метода After");
    }
}
