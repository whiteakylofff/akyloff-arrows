package me.whiteakyloff.arrows.utils.protocolapi.packets;

import lombok.Getter;
import org.bukkit.World;
import org.bukkit.entity.Entity;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.injector.PacketConstructor;
import com.comphenix.protocol.reflect.IntEnum;

import java.util.UUID;

@SuppressWarnings("unused")
public class WrapperPlayServerSpawnEntity extends AbstractPacket
{
    private static PacketConstructor entityConstructor;

    public static PacketType TYPE = PacketType.Play.Server.SPAWN_ENTITY;

    public WrapperPlayServerSpawnEntity() {
        super(new PacketContainer(TYPE), TYPE);
        this.handle.getModifier().writeDefaults();
    }

    public WrapperPlayServerSpawnEntity(PacketContainer packet) {
        super(packet, TYPE);
    }

    public WrapperPlayServerSpawnEntity(Entity entity, int type, int objectData) {
        super(fromEntity(entity, type, objectData), TYPE);
    }

    private static PacketContainer fromEntity(Entity entity, int type, int objectData) {
        if (entityConstructor == null) {
            entityConstructor = ProtocolLibrary.getProtocolManager().createPacketConstructor(TYPE, entity, type, objectData);
        }
        return entityConstructor.createPacket(entity, type, objectData);
    }

    public Entity getEntity(World world) {
        return this.handle.getEntityModifier(world).read(0);
    }

    public Entity getEntity(PacketEvent event) {
        return getEntity(event.getPlayer().getWorld());
    }

    public int getEntityID() {
        return this.handle.getIntegers().read(0);
    }

    public void setEntityID(int value) {
        this.handle.getIntegers().write(0, value);
    }

    public UUID getUniqueId() {
        return this.handle.getUUIDs().read(0);
    }

    public void setUniqueId(UUID value) {
        this.handle.getUUIDs().write(0, value);
    }

    public double getX() {
        return this.handle.getDoubles().read(0);
    }

    public void setX(double value) {
        this.handle.getDoubles().write(0, value);
    }

    public double getY() {
        return this.handle.getDoubles().read(1);
    }

    public void setY(double value) {
        this.handle.getDoubles().write(1, value);
    }

    public double getZ() {
        return this.handle.getDoubles().read(2);
    }

    public void setZ(double value) {
        this.handle.getDoubles().write(2, value);
    }

    public double getOptionalSpeedX() {
        return this.handle.getIntegers().read(1) / 8000.0D;
    }

    public void setOptionalSpeedX(double value) {
        this.handle.getIntegers().write(1, (int) (value * 8000.0D));
    }

    public double getOptionalSpeedY() {
        return this.handle.getIntegers().read(2) / 8000.0D;
    }

    public void setOptionalSpeedY(double value) {
        this.handle.getIntegers().write(2, (int) (value * 8000.0D));
    }

    public double getOptionalSpeedZ() {
        return this.handle.getIntegers().read(3) / 8000.0D;
    }

    public void setOptionalSpeedZ(double value) {
        this.handle.getIntegers().write(3, (int) (value * 8000.0D));
    }

    public float getPitch() {
        return this.handle.getIntegers().read(4) * 360.0F / 256.0F;
    }

    public void setPitch(float value) {
        this.handle.getIntegers().write(4, (int) (value * 256.0F / 360.0F));
    }

    public float getYaw() {
        return this.handle.getIntegers().read(5) * 360.0F / 256.0F;
    }

    public void setYaw(float value) {
        this.handle.getIntegers().write(5, (int) (value * 256.0F / 360.0F));
    }

    public int getType() {
        return this.handle.getIntegers().read(6);
    }

    public void setType(int value) {
        this.handle.getIntegers().write(6, value);
    }

    public int getObjectData() {
        return this.handle.getIntegers().read(7);
    }

    public void setObjectData(int value) {
        this.handle.getIntegers().write(7, value);
    }

    public static class ObjectTypes extends IntEnum
    {
        public static int BOAT = 1;

        public static int ITEM_STACK = 2;

        public static int AREA_EFFECT_CLOUD = 3;

        public static int MINECART = 10;

        public static int ACTIVATED_TNT = 50;

        public static int ENDER_CRYSTAL = 51;

        public static int TIPPED_ARROW_PROJECTILE = 60;

        public static int SNOWBALL_PROJECTILE = 61;

        public static int EGG_PROJECTILE = 62;

        public static int GHAST_FIREBALL = 63;

        public static int BLAZE_FIREBALL = 64;

        public static int THROWN_ENDERPEARL = 65;

        public static int WITHER_SKULL_PROJECTILE = 66;

        public static int SHULKER_BULLET = 67;

        public static int FALLING_BLOCK = 70;

        public static int ITEM_FRAME = 71;

        public static int EYE_OF_ENDER = 72;

        public static int THROWN_POTION = 73;

        public static int THROWN_EXP_BOTTLE = 75;

        public static int FIREWORK_ROCKET = 76;

        public static int LEASH_KNOT = 77;

        public static int ARMORSTAND = 78;

        public static int FISHING_FLOAT = 90;

        public static int SPECTRAL_ARROW = 91;

        public static int DRAGON_FIREBALL = 93;

        @Getter
        private static final ObjectTypes Instance = new ObjectTypes();
    }
}
