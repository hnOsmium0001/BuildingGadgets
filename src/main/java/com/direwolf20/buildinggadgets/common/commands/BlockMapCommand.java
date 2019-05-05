package com.direwolf20.buildinggadgets.common.commands;

import com.direwolf20.buildinggadgets.common.network.PacketHandler;
import com.direwolf20.buildinggadgets.common.network.packets.PacketBlockMap;
import com.direwolf20.buildinggadgets.common.util.ref.NBTKeys;
import com.direwolf20.buildinggadgets.common.world.data.AbstractDataStorage;
import com.direwolf20.buildinggadgets.common.world.data.BlockMapStorage;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BlockMapCommand {

    public static LiteralArgumentBuilder<CommandSource> registerDelete() {
        return Commands.literal("DeleteBlockMaps")
                .requires(commandSource -> commandSource.hasPermissionLevel(4))
                .then(Commands.argument("targets", EntityArgument.singleEntity()))
                .executes(context -> execute(context, EntityArgument.getSingleEntity(context, "targets"), true));
    }


    public static LiteralArgumentBuilder<CommandSource> registerList() {
        return Commands.literal("FindBlockMaps")
                .requires(commandSource -> commandSource.hasPermissionLevel(4))
                .then(Commands.argument("targets", EntityArgument.singleEntity()))
                .executes(context -> execute(context, EntityArgument.getSingleEntity(context, "targets"), false));
    }

    // I don't get the logic going on here but this is basically what it was originally :P
    private static int execute(CommandContext<CommandSource> ctx, Entity entity, boolean removeData) throws CommandSyntaxException {
        EntityPlayerMP sender = ctx.getSource().asPlayer();

        if( !(entity instanceof EntityPlayer)) {
            sender.sendMessage(new TextComponentString("Entity not valid"));
            return 0;
        }

        BlockMapStorage blockMapStorage = BlockMapStorage.fromWorld(sender.getEntityWorld());
        Map<UUID, NBTTagCompound> tagMap = blockMapStorage.getTagMap();
        Map<UUID, NBTTagCompound> newMap = new HashMap<>(tagMap);

        int counter = 0;
        for (Map.Entry<UUID, NBTTagCompound> entry : tagMap.entrySet()) {
            NBTTagCompound tagCompound = entry.getValue();
            if (tagCompound.getString(NBTKeys.TEMPLATE_OWNER).equals(entity.getName().getString())) {
                //TODO Missing localisation
                sender.sendMessage(new TextComponentString(TextFormatting.RED + "Deleted stored map for " + tagCompound.getString(NBTKeys.TEMPLATE_OWNER) + " with UUID:" + tagCompound.getString(NBTKeys.GADGET_UUID)));
                counter++;
                if (removeData) newMap.remove(entry.getKey());
            }
        }

        if (removeData && counter > 0) {
            blockMapStorage.setTagMap(newMap);
            blockMapStorage.markDirty();
            if (entity.getName().equals(sender.getName())) {
                PacketHandler.sendTo(new PacketBlockMap(new NBTTagCompound()), sender);
            }
        }

        //TODO Missing localisation
        sender.sendMessage(new TextComponentString(TextFormatting.WHITE + "Deleted " + counter + " blockmaps in world data."));

        return 1;
    }
}
