package cn.tzq0301.opensasspringbootstarter.sdk.middleware;

import cn.tzq0301.opensasspringbootstarter.common.Topic;
import cn.tzq0301.opensasspringbootstarter.sdk.common.Listener;
import cn.tzq0301.opensasspringbootstarter.sdk.subscriber.ListenableSubscriber;
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

import static com.google.common.base.Preconditions.checkNotNull;

@Component
@Import({MiddlewareCallbackRegistry.class})
@ConditionalOnProperty(prefix = "open-sas.middleware", name = "enable", havingValue = "true")
public class ListenableMiddlewareProcessor implements ApplicationContextAware, ApplicationListener<ContextRefreshedEvent> {
    private ApplicationContext applicationContext;

    private final MiddlewareCallbackRegistry middlewareCallbackRegistry;

    public ListenableMiddlewareProcessor(@NonNull final MiddlewareCallbackRegistry middlewareCallbackRegistry) {
        this.middlewareCallbackRegistry = middlewareCallbackRegistry;
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

            if (!ListenableMiddleware.class.isAssignableFrom(objClz)) {
                throw new RuntimeException("Class annotated by @" + Listener.class.getSimpleName()
                        + " should implement interface " + ListenableMiddleware.class.getSimpleName() + " " + objClz);
            }

            Topic topic = new Topic(objClz.getAnnotation(Listener.class).topic());

            middlewareCallbackRegistry.add(topic, (ListenableMiddleware) obj);
        }
    }
}
