package me.whiteakyloff.arrows.arrow.abilities;

import lombok.var;

import me.whiteakyloff.arrows.arrow.CustomArrow;
import me.whiteakyloff.arrows.utils.protocolapi.Sphere;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class FreezeAbility implements CustomArrowAbility
{
    @Override
    @SuppressWarnings("deprecation")
    public void apply(Player shooter, Arrow arrow, CustomArrow customArrow, Entity hitEntity) {
        if (!(hitEntity instanceof Player)) {
            return;
        }
        var sphere = new Sphere((Player) hitEntity);
        var sphereData = Arrays.stream(customArrow.getArrowData().get("arrow-settings-freeze").split(" "))
                .map(Integer::valueOf).toArray(Integer[]::new);

        sphere.create();
        Bukkit.getScheduler().scheduleAsyncDelayedTask(this.getJavaPlugin(), () -> {
            hitEntity.getLocation().getWorld().getNearbyEntities(hitEntity.getLocation(), 40, 40, 40).forEach(entity -> {
                if (!(entity instanceof Player)) {
                    return;
                }
                var player = (Player) entity;

                sphere.spawn(player);
                player.playSound(player.getLocation(), Sound.BLOCK_GLASS_BREAK, 1.0F, 1.0F);
            });
        });
        Bukkit.getScheduler().scheduleAsyncDelayedTask(this.getJavaPlugin(), sphere::explode, sphereData[0] * 20L);
        Bukkit.getScheduler().scheduleAsyncDelayedTask(this.getJavaPlugin(), sphere::destroy, (sphereData[0] + sphereData[1]) * 20L);
    }
}
