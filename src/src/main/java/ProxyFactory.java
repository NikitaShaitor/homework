import java.lang.reflect.Proxy;

public class ProxyFactory {
    public static <T> T createProxy(T target) {
        return (T) Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                new LoggingInvocationHandler(target)
        );
    }
}
