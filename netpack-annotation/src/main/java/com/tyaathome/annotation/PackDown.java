package com.tyaathome.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * down包父类的标注
 * Created by tyaathome on 2019/11/04.
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface PackDown {
}
