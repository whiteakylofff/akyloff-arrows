package me.whiteakyloff.arrows.arrow.abilities.data;

import me.whiteakyloff.arrows.arrow.CustomArrowType;

import java.lang.annotation.*;

@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ArrowProvider
{
    CustomArrowType arrowType();
}