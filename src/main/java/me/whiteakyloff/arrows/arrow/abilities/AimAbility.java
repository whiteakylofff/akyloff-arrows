package me.whiteakyloff.arrows.arrow.abilities;

import lombok.var;

import me.whiteakyloff.arrows.arrow.CustomArrow;
import me.whiteakyloff.arrows.arrow.CustomArrowType;
import me.whiteakyloff.arrows.arrow.abilities.data.ArrowProvider;
import me.whiteakyloff.arrows.arrow.abilities.data.CustomArrowAbility;

import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;

import java.util.Comparator;

@ArrowProvider(arrowType = CustomArrowType.AIM)
public class AimAbility implements CustomArrowAbility
{
    @Override
    public void apply(Player shooter, Arrow arrow, CustomArrow customArrow, Entity hitEntity) {
        var aimRadius = Double.parseDouble(customArrow.getArrowData().get("arrow-settings-aim"));

        var target = shooter.getNearbyEntities(aimRadius, aimRadius, aimRadius).stream()
                .filter(entity -> entity != shooter && !entity.isDead() && entity.getType().isAlive() && shooter.hasLineOfSight(entity))
                .min(Comparator.comparingDouble(entity -> entity.getLocation().distance(shooter.getLocation())));
        if (!target.isPresent()) {
            return;
        }
        var vector = target.get().getLocation().clone().add(0.0, 0.5, 0.0).subtract(arrow.getLocation()).toVector();

        arrow.setVelocity(arrow.getVelocity().clone().normalize().clone().add(vector.clone().normalize()).normalize().add(new Vector(0.0, 0.0, 0.0)));
     }
}
