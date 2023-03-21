package me.whiteakyloff.arrows.utils.protocolapi.packets;

import org.bukkit.World;
import org.bukkit.entity.Entity;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;

@SuppressWarnings("unused")
public class WrapperPlayServerEntityVelocity extends AbstractPacket
{
    public static PacketType TYPE = PacketType.Play.Server.ENTITY_VELOCITY;

    public WrapperPlayServerEntityVelocity() {
        super(new PacketContainer(TYPE), TYPE);
        this.handle.getModifier().writeDefaults();
    }

    public WrapperPlayServerEntityVelocity(PacketContainer packet) {
        super(packet, TYPE);
    }

    public int getEntityID() {
        return this.handle.getIntegers().read(0);
    }

    public void setEntityID(int value) {
        this.handle.getIntegers().write(0, value);
    }

    public Entity getEntity(World world) {
        return this.handle.getEntityModifier(world).read(0);
    }

    public Entity getEntity(PacketEvent event) {
        return getEntity(event.getPlayer().getWorld());
    }

    public void setVelocityX(double value) {
        this.handle.getIntegers().write(1, (int) (value * 8000.0D));
    }

    public double getVelocityX() {
        return this.handle.getIntegers().read(1) / 8000.0D;
    }

    public void setVelocityY(double value) {
        this.handle.getIntegers().write(2, (int) (value * 8000.0D));
    }

    public double getVelocityY() {
        return this.handle.getIntegers().read(2) / 8000.0D;
    }

    public void setVelocityZ(double value) {
        this.handle.getIntegers().write(3, (int) (value * 8000.0D));
    }

    public double getVelocityZ() {
        return this.handle.getIntegers().read(3) / 8000.0D;
    }
}
