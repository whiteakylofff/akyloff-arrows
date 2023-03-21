package me.whiteakyloff.arrows.utils.protocolapi.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;

@SuppressWarnings("unused")
public class WrapperPlayServerEntityDestroy extends AbstractPacket
{
    public static final PacketType TYPE = PacketType.Play.Server.ENTITY_DESTROY;

    public WrapperPlayServerEntityDestroy() {
        super(new PacketContainer(TYPE), TYPE);
        this.handle.getModifier().writeDefaults();
    }

    public WrapperPlayServerEntityDestroy(PacketContainer packet) {
        super(packet, TYPE);
    }

    public int getCount() {
        return this.handle.getIntegerArrays().read(0).length;
    }

    public int[] getEntityIDs() {
        return this.handle.getIntegerArrays().read(0);
    }

    public void setEntityIds(int[] value) {
        this.handle.getIntegerArrays().write(0, value);
    }
}
