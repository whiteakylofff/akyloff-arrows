package me.whiteakyloff.arrows.arrow.abilities;

import lombok.var;

import me.whiteakyloff.arrows.arrow.CustomArrow;

import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;

public class AimAbility implements CustomArrowAbility
{
    @Override
    public void apply(Player shooter, Arrow arrow, CustomArrow customArrow, Entity hitEntity) {
        var aimRadius = Double.parseDouble(customArrow.getArrowData().get("arrow-settings-aim"));

        var target = arrow.getNearbyEntities(aimRadius, aimRadius, aimRadius).stream()
                .filter(entity -> entity != shooter && !entity.isDead() && entity.getType().isAlive() && shooter.hasLineOfSight(entity))
                .findFirst().orElse(null);
        if (target == null) {
            return;
        }
        var vector = target.getLocation().clone().add(0.0, 0.5, 0.0).subtract(arrow.getLocation()).toVector();

        arrow.setVelocity(arrow.getVelocity().clone().normalize().clone().add(vector.clone().normalize()).normalize().add(new Vector(0.0, 0.0, 0.0)));
     }
}
