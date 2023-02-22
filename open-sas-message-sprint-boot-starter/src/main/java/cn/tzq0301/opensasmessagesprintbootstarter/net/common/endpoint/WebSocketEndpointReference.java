package cn.tzq0301.opensasmessagesprintbootstarter.net.common.endpoint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface WebSocketEndpointReference {
    Class<? extends Endpoint> value();
}
