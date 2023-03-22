package me.whiteakyloff.arrows.arrow.abilities;

import lombok.var;

import me.whiteakyloff.arrows.arrow.CustomArrow;
import me.whiteakyloff.arrows.arrow.CustomArrowType;
import me.whiteakyloff.arrows.arrow.abilities.data.ArrowProvider;
import me.whiteakyloff.arrows.arrow.abilities.data.CustomArrowAbility;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

@ArrowProvider(arrowType = CustomArrowType.TELEPORT)
public class TeleportAbility implements CustomArrowAbility
{
    @Override
    public void apply(Player shooter, Arrow arrow, CustomArrow customArrow, Entity hitEntity) {
        switch (customArrow.getArrowData().get("arrow-settings-teleport").toUpperCase()) {
            case "AT_POINT": {
                if (shooter.getWorld() != arrow.getWorld()) {
                    return;
                }
                var location = arrow.getLocation();

                location.setPitch(shooter.getLocation().getPitch());
                location.setYaw(shooter.getLocation().getYaw());

                shooter.teleport(location);
                break;
            }
            case "BY_ARROW": {
                if (shooter.hasMetadata("sneakedPlayer")) {
                    return;
                }
                if (shooter.isSneaking()) {
                    shooter.setVelocity(new Vector(0, 0, 0));
                    shooter.setMetadata("sneakedPlayer", new FixedMetadataValue(this.getJavaPlugin(), ""));
                }
                shooter.setVelocity(arrow.getLocation().subtract(shooter.getLocation()).toVector().normalize());
                break;
            }
            default: break;
        }
    }
}
