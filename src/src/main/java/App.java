import appcontainer.AppComponentsContainerImpl;
import appcontainer.api.AppComponentsContainer;
import config.AppConfig;
import services.GameProcessor;

@SuppressWarnings({"squid:S125", "squid:S106"})
public class App {

    public static void main(String[] args) {
        // Опциональные варианты
        // AppComponentsContainer container = new AppComponentsContainerImpl(AppConfig1.class, AppConfig2.class);

        // Тут можно использовать библиотеку Reflections (см. зависимости)
        // AppComponentsContainer container = new AppComponentsContainerImpl("ru.otus.config");

        // Обязательный вариант
        AppComponentsContainer container = new AppComponentsContainerImpl(AppConfig.class);

        // Приложение должно работать в каждом из указанных ниже вариантов
        GameProcessor gameProcessor = container.getAppComponent(GameProcessor.class);
        // GameProcessor gameProcessor = container.getAppComponent(GameProcessorImpl.class);
        // GameProcessor gameProcessor = container.getAppComponent("gameProcessor");

        gameProcessor.startGame();
    }
}
