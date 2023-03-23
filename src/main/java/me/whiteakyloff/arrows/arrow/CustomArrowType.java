package me.whiteakyloff.arrows.arrow;

import lombok.var;

import me.whiteakyloff.arrows.arrow.abilities.*;
import me.whiteakyloff.arrows.arrow.abilities.data.ArrowProvider;
import me.whiteakyloff.arrows.arrow.abilities.data.CustomArrowAbility;

public enum CustomArrowType
{
    AIM, EXPLODE, TELEPORT, FREEZE;

    public CustomArrowAbility getArrowAbility() {
        switch (this) {
            case AIM: {
                return new AimAbility();
            }
            case EXPLODE: {
                return new ExplodeAbility();
            }
            case TELEPORT: {
                return new TeleportAbility();
            }
            case FREEZE: {
                return new FreezeAbility();
            }
            default: return null;
        }
    }

    public static boolean isHasAbility(CustomArrowType arrowType, CustomArrowAbility arrowAbility) {
        var clazz = arrowAbility.getClass();

        if (!clazz.isAnnotationPresent(ArrowProvider.class)) {
            throw new IllegalArgumentException("CustomArrowAbility class " + clazz.getName() + " is not annotated @ArrowProvider");
        }
        var arrowProvider = clazz.getAnnotation(ArrowProvider.class);

        return arrowProvider.arrowType() == arrowType;
    }
}
