package cn.tzq0301.opensasspringbootstarter.sdk.subscriber;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static com.google.common.base.Preconditions.checkNotNull;

@Component
@Import({SubscriberOnMessageCallbackRegistry.class})
@ConditionalOnProperty(prefix = "open-sas.subscriber", name = "enable", havingValue = "true")
public class ListenerProcessor implements ApplicationContextAware, ApplicationListener<ContextRefreshedEvent> {
    private ApplicationContext applicationContext;

    private final SubscriberOnMessageCallbackRegistry callbackRegistry;

    public ListenerProcessor(@NonNull final SubscriberOnMessageCallbackRegistry callbackRegistry) {
        this.callbackRegistry = callbackRegistry;
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

            Class<?> objClz = obj.getClass();
            if (AopUtils.isAopProxy(obj) || AopUtils.isCglibProxy(obj)) {
                objClz = AopUtils.getTargetClass(obj);
            }

            for (Method m : objClz.getDeclaredMethods()) {
                if (m.isAnnotationPresent(Listener.class)) {
                    try {
                        callbackRegistry.add((SubscriberOnMessageCallback) m.invoke(obj));
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
//                        System.exit();
                    }
                }
            }
        }
    }
}
