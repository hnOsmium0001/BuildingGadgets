package com.direwolf20.buildinggadgets.common.world.data;

import com.direwolf20.buildinggadgets.common.util.ref.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.WorldSavedData;

public abstract class AbstractDataStorage extends WorldSavedData {

    public AbstractDataStorage(String name) {
        super(new ResourceLocation(Reference.MODID, name).toString());
    }

}
