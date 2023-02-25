package cn.tzq0301.opensasspringbootstarter.sdk.subscriber;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface Listener {
}

