package com.direwolf20.buildinggadgets.common.util.ref;

import net.minecraft.util.ResourceLocation;

public final class Reference {
    public static final String MODID = "buildinggadgets";
    public static final String CONFIG_FILE_SERVER = MODID + "-server.toml";
    public static final String CONFIG_FILE_CLIENT = MODID + "-client.toml";
    public static final String CONFIG_FILE_API = MODID + "-api.toml";

    public static final ResourceLocation CONDITION_PASTE_ID = new ResourceLocation(Reference.MODID, "enable_paste");
    public static final ResourceLocation CONDITION_DESTRUCTION_ID = new ResourceLocation(Reference.MODID, "enable_destruction");

    public static final ResourceLocation PROPERTY_OVERRIDE_LEVEL = new ResourceLocation("level");

    private Reference() {}

    public static final class ItemReference {
        // Gadgets
        public static final String GADGET_BUILDING = MODID + ":gadget_building";
        public static final String GADGET_COPY_PASTE = MODID + ":gadget_copy_paste";
        public static final String GADGET_DESTRUCTION = MODID + ":gadget_destruction";
        public static final String GADGET_EXCHANGING = MODID + ":gadget_exchanging";
        // Building Items
        public static final String CONSTRUCTION_PASTE = MODID + ":construction_paste";
        public static final String CONSTRUCTION_CHUNK_DENSE = MODID + ":construction_chunk_dense";
        public static final String TEMPLATE = MODID + ":template";
        // Construction Paste Containers
        public static final String PASTE_CONTAINER_T1 = MODID + ":construction_paste_container_t1";
        public static final String PASTE_CONTAINER_T2 = MODID + ":construction_paste_container_t2";
        public static final String PASTE_CONTAINER_T3 = MODID + ":construction_paste_container_t3";
        public static final String PASTE_CONTAINER_CREATIVE = MODID + ":construction_paste_container_creative";

        // Gadgets
        public static final ResourceLocation GADGET_BUILDING_RL = new ResourceLocation(GADGET_BUILDING);
        public static final ResourceLocation GADGET_COPY_PASTE_RL = new ResourceLocation(GADGET_COPY_PASTE);
        public static final ResourceLocation GADGET_DESTRUCTION_RL = new ResourceLocation(GADGET_DESTRUCTION);
        public static final ResourceLocation GADGET_EXCHANGING_RL = new ResourceLocation(GADGET_EXCHANGING);
        // Building Items
        public static final ResourceLocation CONSTRUCTION_PASTE_RL = new ResourceLocation(CONSTRUCTION_PASTE);
        public static final ResourceLocation CONSTRUCTION_CHUNK_DENSE_RL = new ResourceLocation(CONSTRUCTION_CHUNK_DENSE);
        public static final ResourceLocation TEMPLATE_RL = new ResourceLocation(TEMPLATE);
        // Construction Paste Containers
        public static final ResourceLocation PASTE_CONTAINER_T1_RL = new ResourceLocation(PASTE_CONTAINER_T1);
        public static final ResourceLocation PASTE_CONTAINER_T2_RL = new ResourceLocation(PASTE_CONTAINER_T2);
        public static final ResourceLocation PASTE_CONTAINER_T3_RL = new ResourceLocation(PASTE_CONTAINER_T3);
        public static final ResourceLocation PASTE_CONTAINER_CREATIVE_RL = new ResourceLocation(PASTE_CONTAINER_CREATIVE);

        private ItemReference() {}
    }

    public static final class BlockReference {
        public static final String EFFECT_BLOCK = MODID + ":effect_block";
        public static final String CONSTRUCTION_BLOCK = MODID + ":construction_block";
        public static final String CONSTRUCTION_BLOCK_DENSE = MODID + ":construction_block_dense";
        public static final String CONSTRUCTION_BLOCK_POWDER = MODID + ":construction_block_powder";
        public static final String TEMPLATE_MANAGER = MODID + ":template_manager";

        public static final ResourceLocation EFFECT_BLOCK_RL = new ResourceLocation(EFFECT_BLOCK);
        public static final ResourceLocation CONSTRUCTION_BLOCK_RL = new ResourceLocation(CONSTRUCTION_BLOCK);
        public static final ResourceLocation CONSTRUCTION_BLOCK_DENSE_RL = new ResourceLocation(CONSTRUCTION_BLOCK_DENSE);
        public static final ResourceLocation CONSTRUCTION_BLOCK_POWDER_RL = new ResourceLocation(CONSTRUCTION_BLOCK_POWDER);
        public static final ResourceLocation TEMPLATE_MANAGER_RL = new ResourceLocation(TEMPLATE_MANAGER);

        private BlockReference() {}
    }

    public static final class EntityReference {
        public static final String BUILD_BLOCK_ENTITY = Reference.MODID + ":build_block_entity";
        public static final String CONSTRUCTION_BLOCK_ENTITY = Reference.MODID + ":construction_block_entity";

        public static final ResourceLocation CONSTRUCTION_BLOCK_ENTITY_RL = new ResourceLocation(CONSTRUCTION_BLOCK_ENTITY);
        public static final ResourceLocation BUILD_BLOCK_ENTITY_RL = new ResourceLocation(BUILD_BLOCK_ENTITY);

        private EntityReference() {}
    }

    public static final class TileEntityReference {
        public static final String CONSTRUCTION_TILE = Reference.MODID + ":construction_tile";
        public static final String TEMPLATE_MANAGER_TILE = Reference.MODID + ":template_manager_tile";

        public static final ResourceLocation TEMPLATE_MANAGER_TILE_RL = new ResourceLocation(TEMPLATE_MANAGER_TILE);
        public static final ResourceLocation CONSTRUCTION_TILE_RL = new ResourceLocation(CONSTRUCTION_TILE);

        private TileEntityReference() {}
    }

    public static final class ContainerReference {
        public static final String TEMPLATE_MANAGER_CONTAINER = Reference.MODID + ":template_manager_container";

        public static final ResourceLocation TEMPLATE_MANAGER_CONTAINER_RL = new ResourceLocation(TEMPLATE_MANAGER_CONTAINER);

        private ContainerReference() {}
    }
}
