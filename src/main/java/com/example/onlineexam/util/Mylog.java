package com.example.onlineexam.util;

import java.lang.annotation.*;
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Mylog {
    String value() default "";
}
