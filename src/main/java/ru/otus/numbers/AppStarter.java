package ru.otus.numbers;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import ru.otus.numbers.client.NumbersClient;

public class AppStarter {
    public static void main(String[] args) throws Exception {
        var context = new SpringApplicationBuilder(NumbersClient.class)
                .web(WebApplicationType.NONE)
                .run(args);

        NumbersClient client = context.getBean(NumbersClient.class);

        client.run(0, 30);
    }
}