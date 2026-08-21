package appcontainer;

import appcontainer.api.AppComponent;
import appcontainer.api.AppComponentsContainer;
import appcontainer.api.AppComponentsContainerConfig;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("squid:S1068")
public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();
    private final Map<Class<?>, Object> appComponentsByClass = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);

        if (appComponentsByName.size() != appComponents.size()) {
            throw new IllegalStateException("Found duplicate component names during container initialization.");
        }
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);

        Set<Class<?>> allConfigs = findAllRelatedConfigs(configClass);

        List<Class<?>> sortedConfigs = allConfigs.stream()
                .sorted(Comparator.comparingInt(c -> c.getAnnotation(AppComponentsContainerConfig.class).order()))
                .collect(Collectors.toList());

        for (Class<?> currentConfig : sortedConfigs) {
            createBeansFromConfig(currentConfig);
        }
    }

    private Set<Class<?>> findAllRelatedConfigs(Class<?> rootConfig) {
        Set<Class<?>> visited = new LinkedHashSet<>();
        Deque<Class<?>> queue = new ArrayDeque<>();
        queue.add(rootConfig);

        while (!queue.isEmpty()) {
            Class<?> current = queue.poll();
            if (visited.contains(current)) continue;

            checkConfigClass(current);
            visited.add(current);

            Method[] methods = current.getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(AppComponent.class)) {
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    for (Class<?> paramType : parameterTypes) {
                        Object dependencyInstance = appComponentsByClass.get(paramType);
                        if (dependencyInstance != null &&
                                dependencyInstance.getClass().isAnnotationPresent(AppComponentsContainerConfig.class)) {
                            queue.add(dependencyInstance.getClass());
                        }
                    }
                }
            }
        }
        return visited;
    }

    private void createBeansFromConfig(Class<?> configClass) {
        Object configInstance;
        try {
            configInstance = configClass.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to instantiate config class: " + configClass.getName(), e);
        }

        for (Method method : configClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(AppComponent.class)) {
                AppComponent annotation = method.getAnnotation(AppComponent.class);

                Object[] args = resolveDependencies(method.getParameterTypes());

                Object beanInstance;
                try {
                    method.setAccessible(true);
                    beanInstance = method.invoke(configInstance, args);
                } catch (ReflectiveOperationException e) {
                    throw new IllegalStateException("Failed to invoke factory method: " + method.getName(), e);
                }

                registerBean(annotation.name(), beanInstance);
            }
        }
    }

    private Object[] resolveDependencies(Class<?>[] parameterTypes) {
        Object[] args = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> requiredType = parameterTypes[i];
            Object dependency = appComponentsByClass.get(requiredType);
            if (dependency == null) {
                throw new IllegalStateException(String.format(
                        "No component of type %s found in the container for injection.", requiredType.getName()));
            }
            args[i] = dependency;
        }
        return args;
    }

    private void registerBean(String name, Object instance) {
        if (appComponentsByName.containsKey(name)) {
            throw new IllegalStateException("Duplicate component name detected: " + name);
        }
        appComponents.add(instance);
        appComponentsByName.put(name, instance);
        appComponentsByClass.put(instance.getClass(), instance);
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        Object component = appComponentsByClass.get(componentClass);
        if (component == null) {
            throw new IllegalStateException(String.format("Component of class %s not found", componentClass.getName()));
        }
        return componentClass.cast(component);
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        Object component = appComponentsByName.get(componentName);
        if (component == null) {
            throw new IllegalStateException(String.format("Component with name '%s' not found", componentName));
        }
        @SuppressWarnings("unchecked")
        C castedComponent = (C) component;
        return castedComponent;
    }
}