package cn.tzq0301.opensasspringbootstarter.sdk.subscriber;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Component
@Import({OnMessageCallbackRegister.class})
@ConditionalOnProperty(prefix = "open-sas.subscriber", name = "enable", havingValue = "true")
public class ChannelListenerProcessor {
    public ChannelListenerProcessor(@NonNull final ApplicationContext context,
                                    @NonNull final OnMessageCallbackRegister callbackRegister) {
        for (String beanName : context.getBeanDefinitionNames()) {
            Object obj = context.getBean(beanName);
            /*
             * As you are using AOP check for AOP proxying. If you are proxying with Spring CGLIB (not via Spring AOP)
             * Use org.springframework.cglib.proxy.Proxy#isProxyClass to detect proxy If you are proxying using JDK
             * Proxy use java.lang.reflect.Proxy#isProxyClass
             */
            Class<?> objClz = obj.getClass();
            if (org.springframework.aop.support.AopUtils.isAopProxy(obj)) {
                objClz = org.springframework.aop.support.AopUtils.getTargetClass(obj);
            }

            for (Method m : objClz.getDeclaredMethods()) {
                if (m.isAnnotationPresent(Listener.class)) {
                    // Should give you expected results
                    try {
                        callbackRegister.add((OnMessageCallback) m.invoke(obj));
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
//                        System.exit();
                    }
                }
            }
        }
    }
}
