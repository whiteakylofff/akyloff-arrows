package me.whiteakyloff.arrows.arrow;

import me.whiteakyloff.arrows.arrow.abilities.*;

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
}
