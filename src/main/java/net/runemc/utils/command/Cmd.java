package net.runemc.utils.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Cmd {
    String[] names();
    String[] perms() default "default";
    boolean disabled() default false;
    boolean playerOnly() default false;
}
