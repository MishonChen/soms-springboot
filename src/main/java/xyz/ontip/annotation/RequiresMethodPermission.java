package xyz.ontip.annotation;

import aj.org.objectweb.asm.Type;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresMethodPermission {
    String[] value() default {}; // 定义方法级别需要的权限名称
}
