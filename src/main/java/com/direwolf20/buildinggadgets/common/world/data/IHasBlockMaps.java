package com.direwolf20.buildinggadgets.common.world.data;

import net.minecraft.nbt.NBTTagCompound;

import java.util.Map;
import java.util.UUID;

public interface IHasBlockMaps {

    void addToMap(UUID uuid, NBTTagCompound tag);

    Map<UUID, NBTTagCompound> getTagMap();

    void setTagMap(Map<UUID, NBTTagCompound> newMap);

    NBTTagCompound getCompoundFromUUID(UUID uuid);

}
