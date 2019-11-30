package me.zhengjie.terminal.annotation;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Text {
    String id();
    String handler();
    String send() default "";
}
