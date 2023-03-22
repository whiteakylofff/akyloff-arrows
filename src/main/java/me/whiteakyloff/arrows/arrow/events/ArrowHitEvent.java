package me.whiteakyloff.arrows.arrow.events;

import lombok.Getter;
import lombok.AllArgsConstructor;

import me.whiteakyloff.arrows.arrow.CustomArrow;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
@AllArgsConstructor
public class ArrowHitEvent extends Event
{
    private final Player shooter;

    private final Arrow arrow;

    private final CustomArrow customArrow;

    private final Entity hitEntity;

    private static final HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
