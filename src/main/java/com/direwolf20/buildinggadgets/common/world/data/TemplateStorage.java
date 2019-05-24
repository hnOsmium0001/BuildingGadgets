package com.direwolf20.buildinggadgets.common.world.data;

import com.direwolf20.buildinggadgets.common.util.helpers.NBTHelper;
import com.direwolf20.buildinggadgets.common.util.ref.NBTKeys;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TemplateStorage extends AbstractDataStorage implements IHasBlockMaps {

    private static final String NAME = "template_data";

    private Map<UUID, NBTTagCompound> tagMap = new HashMap<>();

    @Nonnull
    public static TemplateStorage fromWorld(World world) {
        DimensionType dim = world.getDimension().getType();
        TemplateStorage result = world.func_212411_a(dim, TemplateStorage::new, NAME);
        if (result == null) {
            result = new TemplateStorage(NAME);
            world.func_212409_a(dim, NAME, result);
        }
        return result;
    }

    public TemplateStorage(String name) {
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
                tagMap.put(uuid, tagCompound);
            }
        }
    }

    @Override
    public NBTTagCompound write(NBTTagCompound tag) {
        // tagMap.clear();
        // NBTHelper.deserializeMap((NBTTagCompound) tag.getTag("data"), tagMap, NBTUtil::readUniqueId, compound -> compound);
        // return tag;
        NBTTagList tagList = new NBTTagList();

        for (Map.Entry<UUID, NBTTagCompound> entry : tagMap.entrySet()) {
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
        tagMap.put(uuid, tag);
        markDirty();
    }

    @Override
    public Map<UUID, NBTTagCompound> getTagMap() {
        return tagMap;
    }

    @Override
    public void setTagMap(Map<UUID, NBTTagCompound> newMap) {
        tagMap = new HashMap<>(newMap);
    }

    @Override
    public NBTTagCompound getCompoundFromUUID(UUID uuid) {
        return tagMap.get(uuid);
    }

}
