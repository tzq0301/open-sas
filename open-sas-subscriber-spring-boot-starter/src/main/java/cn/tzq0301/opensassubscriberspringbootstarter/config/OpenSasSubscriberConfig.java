package cn.tzq0301.opensassubscriberspringbootstarter.config;

import cn.tzq0301.opensascore.listener.Listener;
import cn.tzq0301.opensascore.listener.SubscriberListenerRegistry;
import cn.tzq0301.opensascore.subscriber.SubscriberCallback;
import cn.tzq0301.opensascore.topic.Topic;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import static com.google.common.base.Preconditions.checkNotNull;

@SpringBootConfiguration
public class OpenSasSubscriberConfig implements ApplicationContextAware, ApplicationListener<ContextRefreshedEvent> {
    private final SubscriberListenerRegistry subscriberListenerRegistry;

    private ApplicationContext applicationContext;

    public OpenSasSubscriberConfig(SubscriberListenerRegistry subscriberListenerRegistry) {
        this.subscriberListenerRegistry = subscriberListenerRegistry;
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

            if (!SubscriberCallback.class.isAssignableFrom(objClz)) {
                throw new RuntimeException("Class annotated by @" + Listener.class.getSimpleName()
                        + " should implement interface " + SubscriberCallback.class.getSimpleName() + " " + objClz);
            }

            Topic topic = new Topic(objClz.getAnnotation(Listener.class).topic());

            subscriberListenerRegistry.add(topic, (SubscriberCallback) obj);
        }
    }
}
