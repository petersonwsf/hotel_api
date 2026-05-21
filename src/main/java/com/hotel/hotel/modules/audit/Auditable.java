package com.hotel.hotel.modules.audit;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Auditable {
    String action();
    String resourceType() default "";
    boolean captureArgs() default false;
    boolean captureResult() default false;
}
