package cn.tzq0301.opensasspringbootstarter.sdk.subscriber;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

@Component
@Import({OnMessageCallbackRegistry.class})
@ConditionalOnProperty(prefix = "open-sas.subscriber", name = "enable", havingValue = "true")
public class ListenerProcessor implements ApplicationContextAware, ApplicationListener<ContextRefreshedEvent> {
    private ApplicationContext applicationContext;

    private final OnMessageCallbackRegistry callbackRegistry;

    public ListenerProcessor(@NonNull final OnMessageCallbackRegistry callbackRegistry) {
        this.callbackRegistry = callbackRegistry;

        System.out.println(11111); // FIXME
//        for (String beanName : context.getBeanDefinitionNames()) {
//            Object obj = context.getBean(beanName);
//            /*
//             * As you are using AOP check for AOP proxying. If you are proxying with Spring CGLIB (not via Spring AOP)
//             * Use org.springframework.cglib.proxy.Proxy#isProxyClass to detect proxy If you are proxying using JDK
//             * Proxy use java.lang.reflect.Proxy#isProxyClass
//             */
//            Class<?> objClz = obj.getClass();
//            if (org.springframework.aop.support.AopUtils.isAopProxy(obj)) {
//                objClz = org.springframework.aop.support.AopUtils.getTargetClass(obj);
//            }
//
//            for (Method m : objClz.getDeclaredMethods()) {
//                if (m.isAnnotationPresent(Listener.class)) {
//                    // Should give you expected results
//                    try {
//                        callbackRegister.add((OnMessageCallback) m.invoke(obj));
//                    } catch (IllegalAccessException | InvocationTargetException e) {
//                        e.printStackTrace();
////                        System.exit();
//                    }
//                }
//            }
//        }
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        checkNotNull(applicationContext);
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(@NonNull ContextRefreshedEvent event) {
        for (String beanName : applicationContext.getBeanDefinitionNames()) {
            Object obj = applicationContext.getBean(beanName);
            /*
             * As you are using AOP check for AOP proxying. If you are proxying with Spring CGLIB (not via Spring AOP)
             * Use org.springframework.cglib.proxy.Proxy#isProxyClass to detect proxy If you are proxying using JDK
             * Proxy use java.lang.reflect.Proxy#isProxyClass
             */
            Class<?> objClz = obj.getClass();
            if (AopUtils.isAopProxy(obj)) {
                objClz = AopUtils.getTargetClass(obj);
                System.out.println(123);
            } else if (AopUtils.isCglibProxy(obj)) {
                objClz = AopUtils.getTargetClass(obj);
                System.out.println(234);
            }

            for (Method m : objClz.getDeclaredMethods()) {
//                System.out.println(m.getName());

                if (m.isAnnotationPresent(Listener.class)) {
                    // Should give you expected results
                    try {
                        callbackRegistry.add((OnMessageCallback) m.invoke(obj));
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
//                        System.exit();
                    }
                }
            }
        }
    }
}
