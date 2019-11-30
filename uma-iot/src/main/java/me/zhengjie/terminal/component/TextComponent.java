package me.zhengjie.terminal.component;

import me.zhengjie.terminal.annotation.Text;

import java.lang.annotation.Annotation;

public class TextComponent extends Base {
    String value;

    public void send() {
        Class clazz = this.getClass();
        Annotation[] array = clazz.getSuperclass().getDeclaredAnnotations();
        Text fieldNameAnno = (Text) clazz.getAnnotation(Text.class);
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}