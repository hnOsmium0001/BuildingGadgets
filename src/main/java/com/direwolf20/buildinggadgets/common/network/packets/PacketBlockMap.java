package com.direwolf20.buildinggadgets.common.network.packets;

import com.direwolf20.buildinggadgets.common.util.buffers.PasteToolBufferBuilder;
import com.direwolf20.buildinggadgets.common.util.helpers.NBTHelper;
import com.direwolf20.buildinggadgets.common.util.ref.NBTKeys;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class PacketBlockMap {

    private final NBTTagCompound compound;

    public PacketBlockMap(NBTTagCompound compound) {
        this.compound = compound;
    }

    public static void encode(PacketBlockMap msg, PacketBuffer buffer) {
        buffer.writeCompoundTag(msg.compound);
    }

    public static PacketBlockMap decode(PacketBuffer buffer) {
        return new PacketBlockMap(buffer.readCompoundTag());
    }

    public static class Handler {
        public static void handle(final PacketBlockMap msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                if (msg.compound.equals(new NBTTagCompound())) {
                    PasteToolBufferBuilder.clearMaps();
                }

                UUID uuid = NBTHelper.readUUID(msg.compound);
                PasteToolBufferBuilder.addToMap(uuid, msg.compound);
                PasteToolBufferBuilder.addMapToBuffer(uuid);
            });

            ctx.get().setPacketHandled(true);
        }
    }
}
