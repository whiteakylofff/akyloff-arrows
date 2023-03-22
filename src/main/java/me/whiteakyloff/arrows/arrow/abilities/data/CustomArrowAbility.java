package me.whiteakyloff.arrows.arrow.abilities.data;

import me.whiteakyloff.arrows.AkyloffArrows;
import me.whiteakyloff.arrows.arrow.CustomArrow;
import me.whiteakyloff.arrows.arrow.events.ArrowHitEvent;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public interface CustomArrowAbility
{
    void apply(Player shooter, Arrow arrow, CustomArrow customArrow, Entity hitEntity);

    default void apply(ArrowHitEvent event) {
        this.apply(event.getShooter(), event.getArrow(), event.getCustomArrow(), event.getHitEntity());
    }

    default AkyloffArrows getJavaPlugin() {
        return AkyloffArrows.getPlugin(AkyloffArrows.class);
    }
}
