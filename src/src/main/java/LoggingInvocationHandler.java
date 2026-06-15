import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

public class LoggingInvocationHandler implements InvocationHandler {

    private final Object target;

    public LoggingInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.isAnnotationPresent(Log.class)) {
            String paramsString = args == null ? "" :
                    Arrays.stream(args)
                            .map(Object::toString)
                            .collect(Collectors.joining(", "));

            System.out.println("executed method: " + method.getName() + ", params: " + paramsString);
        }

        return method.invoke(target, args);
    }
}
