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

import java.util.Arrays;

import static com.google.common.base.Preconditions.checkNotNull;

@Component
@Import({ListenableSubscriberRegistry.class})
@ConditionalOnProperty(prefix = "open-sas.subscriber", name = "enable", havingValue = "true")
public class ListenableSubscriberProcessor implements ApplicationContextAware, ApplicationListener<ContextRefreshedEvent> {
    private ApplicationContext applicationContext;

    private final ListenableSubscriberRegistry listenableSubscriberRegistry;

    public ListenableSubscriberProcessor(@NonNull final ListenableSubscriberRegistry listenableSubscriberRegistry) {
        this.listenableSubscriberRegistry = listenableSubscriberRegistry;
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

            if (!objClz.isAnnotationPresent(Listener.class)) {
                continue;
            }

            if (!ListenableSubscriber.class.isAssignableFrom(objClz)) {
                throw new RuntimeException("Class annotated by @" + Listener.class.getSimpleName()
                        + " should implement interface " + ListenableSubscriber.class.getSimpleName() + " " + objClz);
            }

            listenableSubscriberRegistry.add((ListenableSubscriber) obj);
        }
    }
}
