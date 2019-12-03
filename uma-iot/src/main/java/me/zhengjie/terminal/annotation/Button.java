package me.zhengjie.terminal.annotation;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Button {
    String id();
    String handler();
    String send() default "";
}
