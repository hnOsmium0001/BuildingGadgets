package com.direwolf20.buildinggadgets.common.world.data;

import com.direwolf20.buildinggadgets.common.util.helpers.NBTHelper;
import com.direwolf20.buildinggadgets.common.util.ref.NBTKeys;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
import java.util.*;

public class BlockMapStorage extends AbstractDataStorage implements IHasBlockMaps {

    private Map<UUID, NBTTagCompound> blockMaps = new HashMap<>();

    @Nonnull
    public static BlockMapStorage fromWorld(World world) {
        DimensionType dim = world.getDimension().getType();
        BlockMapStorage result = world.func_212411_a(dim, BlockMapStorage::new, NAME);
        if (result == null) {
            result = new BlockMapStorage(NAME);
            world.func_212409_a(dim, NAME, result);
        }
        return result;
    }

    private static final String NAME = "block_map_data";

    public BlockMapStorage(String name) {
        super(name);
    }

    //TODO cleanup
    @Override
    public void read(NBTTagCompound tag) {
        // tag.setTag("data", NBTHelper.serializeMap(tagMap, NBTUtil::writeUniqueId, compound -> compound));
        if (tag.hasKey(getName())) {
            NBTTagList tagList = tag.getList(getName(), Constants.NBT.TAG_COMPOUND);

            for (int i = 0; i < tagList.size(); i++) {
                NBTTagCompound mapTag = tagList.getCompound(i);
                UUID uuid = NBTHelper.getExistingUUID(mapTag);
                NBTTagCompound tagCompound = mapTag.getCompound(NBTKeys.WORLD_SAVE_TAG);
                blockMaps.put(uuid, tagCompound);
            }
        }
    }

    @Override
    public NBTTagCompound write(NBTTagCompound tag) {
        // blockMaps.clear();
        // NBTHelper.deserializeMap((NBTTagCompound) tag.getTag("data"), blockMaps, NBTUtil::readUniqueId, compound -> compound);
        // return tag;
        NBTTagList tagList = new NBTTagList();

        for (Map.Entry<UUID, NBTTagCompound> entry : blockMaps.entrySet()) {
            NBTTagCompound map = new NBTTagCompound();
            NBTHelper.writeUUID(map, entry.getKey());
            map.setTag(NBTKeys.WORLD_SAVE_TAG, entry.getValue());
            tagList.add(map);
        }
        tag.setTag(getName(), tagList);
        return tag;
    }

    @Override
    public void addToMap(UUID uuid, NBTTagCompound tag) {
        blockMaps.put(uuid, tag);
        markDirty();
    }

    @Override
    public Map<UUID, NBTTagCompound> getTagMap() {
        return blockMaps;
    }

    @Override
    public void setTagMap(Map<UUID, NBTTagCompound> newMap) {
        blockMaps = new HashMap<>(newMap);
    }

    @Override
    public NBTTagCompound getCompoundFromUUID(UUID uuid) {
        return blockMaps.get(uuid);
    }


}
