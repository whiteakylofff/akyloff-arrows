package me.whiteakyloff.arrows.arrow.abilities;

import lombok.var;

import me.whiteakyloff.arrows.arrow.CustomArrow;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class ExplodeAbility implements CustomArrowAbility
{
    @Override
    public void apply(Player shooter, Arrow arrow, CustomArrow customArrow, Entity hitEntity) {
        var settings = customArrow.getArrowData().get("arrow-settings-explode").split(" ");

        arrow.getLocation().createExplosion(Integer.parseInt(settings[0]), Boolean.parseBoolean(settings[1]), Boolean.parseBoolean(settings[2]));
    }
}
