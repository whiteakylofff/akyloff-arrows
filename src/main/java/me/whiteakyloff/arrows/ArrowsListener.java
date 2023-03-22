package me.whiteakyloff.arrows;

import lombok.var;
import lombok.AllArgsConstructor;

import me.whiteakyloff.arrows.arrow.CustomArrowType;
import me.whiteakyloff.arrows.arrow.events.ArrowFlyingEvent;
import me.whiteakyloff.arrows.arrow.events.ArrowHitEvent;

import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

@AllArgsConstructor
public class ArrowsListener implements Listener
{
    private AkyloffArrows javaPlugin;

    @EventHandler
    public void onLoadArrow(EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        var customArrow = this.javaPlugin.getArrowsManager().getArrow(event.getArrowItem());

        if (customArrow == null) {
            return;
        }
        var newArrow = event.getProjectile();

        newArrow.setMetadata("customArrow", new FixedMetadataValue(this.javaPlugin, ""));
        event.setProjectile(newArrow);

        Bukkit.getPluginManager().callEvent(new ArrowFlyingEvent((Arrow) newArrow, customArrow));
    }

    @EventHandler
    public void onFlyingArrow(ArrowFlyingEvent event) {
        new BukkitRunnable() {
            @Override
            public void run() {
                var arrow = event.getArrow();
                var customArrow = event.getCustomArrow();
                var shooter = (Player) arrow.getShooter();

                customArrow.getArrowAbilities().stream()
                        .filter(arrowAbility ->  CustomArrowType.isHasAbility(CustomArrowType.AIM, arrowAbility) || (CustomArrowType.isHasAbility(CustomArrowType.TELEPORT, arrowAbility) && customArrow.getArrowData().get("arrow-settings-teleport").equalsIgnoreCase("BY_ARROW")))
                        .forEach(arrowAbility -> arrowAbility.apply(shooter, arrow, customArrow, null));
                if (arrow.hasMetadata("hitArrow")) {
                    var hitEvent = (ProjectileHitEvent) arrow.getMetadata("hitArrow").get(0).value();

                    Bukkit.getPluginManager().callEvent(new ArrowHitEvent(shooter, arrow, customArrow, hitEvent.getHitEntity()));
                    this.cancel();
                }
            }
        }.runTaskTimer(this.javaPlugin, 1L, 1L);
    }

    @EventHandler
    public void onFallArrow(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Arrow && event.getEntity().hasMetadata("customArrow")) {
            event.getEntity().setMetadata("hitArrow", new FixedMetadataValue(this.javaPlugin, event));
        }
    }

    @EventHandler
    public void onHitArrow(ArrowHitEvent event) {
        var arrow = event.getArrow();
        var shooter = event.getShooter();

        event.getCustomArrow().getArrowAbilities().stream()
                .filter(arrowAbility -> !CustomArrowType.isHasAbility(CustomArrowType.AIM, arrowAbility))
                .forEach(customArrowAbility -> customArrowAbility.apply(event));
        if (shooter.hasMetadata("sneakedPlayer")) {
            shooter.removeMetadata("sneakedPlayer", this.javaPlugin);
        }
        arrow.remove();
    }

    @EventHandler(ignoreCancelled = true)
    public void onFreezeMoving(PlayerMoveEvent event) {
        if (this.javaPlugin.getArrowsManager().getSpheresStorage().containsKey(event.getPlayer())) {
            event.getPlayer().teleport(event.getFrom());
        }
    }

    @EventHandler
    public void onFreezeQuit(PlayerQuitEvent event) {
        this.javaPlugin.getArrowsManager().getSpheresStorage().remove(event.getPlayer());
    }
}
