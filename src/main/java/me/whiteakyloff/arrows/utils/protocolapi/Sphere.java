package me.whiteakyloff.arrows.utils.protocolapi;

import lombok.var;
import lombok.AllArgsConstructor;

import me.whiteakyloff.arrows.AkyloffArrows;
import me.whiteakyloff.arrows.utils.protocolapi.packets.*;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.comphenix.protocol.wrappers.WrappedDataWatcher;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang.math.RandomUtils;

@AllArgsConstructor
public class Sphere
{
    private Player hitPlayer;

    private final Set<Player> players = new HashSet<>();

    private final Map<Integer, Vector> blocks = new HashMap<>();

    private final AkyloffArrows javaPlugin = AkyloffArrows.getPlugin(AkyloffArrows.class);

    public void create() {
        var radius = 1.5;
        var invRadius = 1.0 / radius;
        var ceilRadius = (int) Math.ceil(radius);
        var vectorPos = new Vector(this.hitPlayer.getLocation().getX(), this.hitPlayer.getLocation().getY() + 1.0, this.hitPlayer.getLocation().getZ());

        for (var x = 0; x <= ceilRadius; x++) {
            var xn = x * invRadius;
            var distanceX = lengthSq(xn, 0, 0);

            if (distanceX > 1.0) {
                break;
            }
            for (var y = 0; y <= ceilRadius; y++) {
                var yn = y * invRadius;
                var distanceY = lengthSq(xn, yn, 0);

                if (distanceY > 1.0) {
                    break;
                }
                for (var z = 0; z <= ceilRadius; z++) {
                    var zn = z * invRadius;
                    var distanceZ = lengthSq(xn, yn, zn);

                    if (distanceZ > 1.0) {
                        break;
                    }
                    if (this.lengthSq(xn + invRadius, yn, zn) > 1.0 || this.lengthSq(xn, yn + invRadius, zn) > 1.0 || this.lengthSq(xn, yn, zn + invRadius) > 1.0) {
                        var vectors = Arrays.asList(
                                new Vector(x, y, z),
                                new Vector(-x, y, z),
                                new Vector(x, -y, z),
                                new Vector(x, y, -z),
                                new Vector(-x, -y, z),
                                new Vector(x, -y, -z),
                                new Vector(-x, y, -z),
                                new Vector(-x, -y, -z)
                        );
                        this.blocks.putAll(vectors.stream().map(vector -> vector.add(vectorPos.clone()))
                                .collect(Collectors.toMap(vector -> RandomUtils.nextInt(), Function.identity())));
                    }
                }
            }
        }
    }

    public void spawn(Player player) {
        this.players.add(player);
        this.javaPlugin.getArrowsManager().getSpheresStorage().put(this.hitPlayer, this);

        for (var block : this.blocks.entrySet()) {
            var vector = block.getValue();
            var entityId = block.getKey();

            var spawnPacket = new WrapperPlayServerSpawnEntity();

            spawnPacket.setEntityID(entityId);
            spawnPacket.setUniqueId(UUID.randomUUID());
            spawnPacket.setType(70);
            spawnPacket.setObjectData(79);

            spawnPacket.setX(vector.getX());
            spawnPacket.setY(vector.getY());
            spawnPacket.setZ(vector.getZ());

            spawnPacket.sendPacket(player);

            var dataWatcher = new WrappedDataWatcher();

            dataWatcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(0, BYTE_SERIALIZER), (byte) 0);
            dataWatcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(1, INTEGER_SERIALIZER), 300);
            dataWatcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(2, STRING_SERIALIZER), "");
            dataWatcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(3, BOOLEAN_SERIALIZER), false);
            dataWatcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(4, BOOLEAN_SERIALIZER), false);
            dataWatcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(5, BOOLEAN_SERIALIZER), true);

            var metadataPacket = new WrapperPlayServerEntityMetadata();

            metadataPacket.setEntityID(entityId);
            metadataPacket.setMetadata(dataWatcher.getWatchableObjects());
            metadataPacket.sendPacket(player);
        }
    }

    public void explode() {
        var velocityPackets = new ArrayList<WrapperPlayServerEntityVelocity>();

        for (var block : this.blocks.entrySet()) {
            var velocityPacket = new WrapperPlayServerEntityVelocity();
            var direction = this.getDirection(block.getValue(), this.hitPlayer.getLocation().toVector()).normalize().multiply(80);

            velocityPacket.setEntityID(block.getKey());
            velocityPacket.setVelocityX(direction.getX() / 250.0D);
            velocityPacket.setVelocityY(direction.getY() / 250.0D);
            velocityPacket.setVelocityZ(direction.getZ() / 250.0D);

            velocityPackets.add(velocityPacket);
        }
        this.players.forEach(player -> {
            velocityPackets.forEach(velocityPacket -> velocityPacket.sendPacket(player));
            player.playSound(player.getLocation(), Sound.BLOCK_GLASS_BREAK, 1.0F, 0.0F);
        });
    }

    public void destroy() {
        this.players.forEach(player -> {
            var destroyPacket = new WrapperPlayServerEntityDestroy();

            destroyPacket.setEntityIds(this.blocks.keySet().stream().mapToInt(Integer::intValue).toArray());
            destroyPacket.sendPacket(player);

            player.playSound(player.getLocation(), Sound.BLOCK_GLASS_BREAK, 1.0F, 2.0F);
        });
        this.players.clear();
        this.javaPlugin.getArrowsManager().getSpheresStorage().remove(hitPlayer, this);
    }

    private double lengthSq(double x, double y, double z) {
        return x * x + y * y + z * z;
    }

    private double toYaw(Vector vector) {
        var x = vector.getX();
        var z = vector.getZ();

        return Math.toDegrees(Math.atan2(-x, z)) % 360;
    }

    private double toPitch(Vector vector) {
        var x = vector.getX();
        var y = vector.getY();
        var z = vector.getZ();

        var xz = Math.sqrt(Math.pow(x, 2) + Math.pow(z, 2));
        return Math.toDegrees(Math.atan2(-y, xz));
    }

    private Vector getDirection(Vector vector, Vector other) {
        vector = vector.clone().subtract(other);

        var rotX = toYaw(vector);
        var rotY = toPitch(vector);
        var cosY = Math.cos(Math.toRadians(rotY));
        var sinY = Math.sin(Math.toRadians(rotY));
        var cosX = Math.cos(Math.toRadians(rotX));
        var sinX = Math.sin(Math.toRadians(rotX));

        vector.setY(-sinY);
        vector.setX(-cosY * sinX);
        vector.setZ(cosY * cosX);
        return vector;
    }

    protected static final WrappedDataWatcher.Serializer BYTE_SERIALIZER = WrappedDataWatcher.Registry.get(Byte.class);
    protected static final WrappedDataWatcher.Serializer STRING_SERIALIZER = WrappedDataWatcher.Registry.get(String.class);
    protected static final WrappedDataWatcher.Serializer BOOLEAN_SERIALIZER = WrappedDataWatcher.Registry.get(Boolean.class);
    protected static final WrappedDataWatcher.Serializer INTEGER_SERIALIZER = WrappedDataWatcher.Registry.get(Integer.class);
}
