package me.zhengjie.terminal;

import lombok.extern.slf4j.Slf4j;
import me.zhengjie.terminal.annotation.Button;
import me.zhengjie.terminal.annotation.Screen;
import me.zhengjie.terminal.annotation.Text;
import me.zhengjie.utils.SpringContextUtils;
import org.reflections.Reflections;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

@Slf4j
@Component
public class GobalListener {
    public void gateWay(String screenId, String controlId, String value, String type, String ip) {
        boolean isInvoke = false;
        //包名且不可忘记，不然扫描全部项目包，包括引用的jar
        Reflections reflections = new Reflections("me.zhengjie.terminal.terminal");
        //获取带Service注解的类
        Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(Screen.class);
        for (Class clazz : typesAnnotatedWith) {
            if (isInvoke) {
                break;
            }
            Screen screen = (Screen) clazz.getAnnotation(Screen.class);
            if (screen.id().equalsIgnoreCase(screenId)) {
                Method[] methods = clazz.getDeclaredMethods();

                Field[] fields = clazz.getDeclaredFields();
                if (type.equals("text")) {
                    for (Field field : fields) {
                        Text text = field.getAnnotation(Text.class);
                        if (text != null && text.id().equalsIgnoreCase(controlId)) {
                            String methodName = text.handler();
                            Method method = findMethod(methods, methodName);
                            if (method != null) {
                                invokeMethod(clazz, method, type, value, ip);
                            } else {
                                log.error("Text control screenId:" + screenId + ",controlId:" + controlId + " handler method:" + methodName + " is not found");
                            }
                            isInvoke = true;
                            break;
                        }
                    }
                } else if (type.equals("button")) {
                    for (Field field : fields) {
                        Button button = field.getAnnotation(Button.class);
                        if (button != null && button.id().equalsIgnoreCase(controlId)) {
                            String methodName = button.handler();
                            Method method = findMethod(methods, methodName);
                            if (method != null) {
                                invokeMethod(clazz, method, type, controlId, ip);
                            } else {
                                log.error("Button control screenId:" + screenId + ",controlId:" + controlId + " handler method:" + methodName + " is not found");
                            }
                            isInvoke = true;
                            break;
                        }
                    }
                }
            }
        }
    }

    private void invokeMethod(Class clazz, Method method, String type, String value, String ip) {
        try {
//            String clazzName = clazz.getName();
//            Object t = NettyTcpServer.terminalMap.get(ip).getClazzMap().get(clazzName);
//            if (t == null) {
//                t = clazz.newInstance();
//                NettyTcpServer.terminalMap.get(ip).getClazzMap().put(clazzName, t);
//            }
            String clazzName = clazz.getSimpleName();
            clazzName = clazzName.substring(0,1).toLowerCase()+clazzName.substring(1);
            Object obj = SpringContextUtils.getBean(clazzName);
            if (type.equals("text")) {
                method.invoke(obj, value, ip);
            } else if (type.equals("button")) {
                method.invoke(obj, value, ip);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private Method findMethod(Method[] methods, String methodName) {
        Method result = null;
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                result = method;
                break;
            }
        }
        return result;
    }
}