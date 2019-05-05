package com.direwolf20.buildinggadgets.common.network.packets;

import com.direwolf20.buildinggadgets.common.network.PacketHandler;
import com.direwolf20.buildinggadgets.common.world.data.BlockMapStorage;
import com.direwolf20.buildinggadgets.common.world.data.TemplateStorage;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class PacketRequestBlockMap {

    private static int uuidLength = 128;

    private UUID uuid;
    private boolean isTemplate;

    public PacketRequestBlockMap(UUID uuid, boolean isTemplate) {
        this.uuid = uuid;
        this.isTemplate = isTemplate;
    }

    public static void encode(PacketRequestBlockMap msg, PacketBuffer buffer) {
        buffer.writeUniqueId(msg.uuid);
        buffer.writeBoolean(msg.isTemplate);
    }

    public static PacketRequestBlockMap decode(PacketBuffer buffer) {
        return new PacketRequestBlockMap(buffer.readUniqueId(), buffer.readBoolean());
    }

    public static class Handler {
        public static void handle(final PacketRequestBlockMap msg, Supplier<NetworkEvent.Context> ctx) {
            if (ctx.get().getDirection() != NetworkDirection.PLAY_TO_CLIENT)
                return;

            ctx.get().enqueueWork(() -> {
                EntityPlayerMP player = ctx.get().getSender();//TODO incorrect means of getting the player on the client
                if (player == null) return;

                NBTTagCompound tagCompound = (msg.isTemplate ? TemplateStorage.fromWorld(player.world) : BlockMapStorage.fromWorld(player.world)).getCompoundFromUUID(msg.uuid);
                if (tagCompound != null)
                    PacketHandler.sendTo(new PacketBlockMap(tagCompound), player);

            });
            ctx.get().setPacketHandled(true);
        }
    }
}
