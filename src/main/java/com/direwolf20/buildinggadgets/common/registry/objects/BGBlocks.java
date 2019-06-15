package com.direwolf20.buildinggadgets.common.registry.objects;

import com.direwolf20.buildinggadgets.common.blocks.*;
import com.direwolf20.buildinggadgets.common.blocks.templatemanager.TemplateManager;
import com.direwolf20.buildinggadgets.common.blocks.templatemanager.TemplateManagerTileEntity;
import com.direwolf20.buildinggadgets.common.registry.block.BlockBuilder;
import com.direwolf20.buildinggadgets.common.registry.block.BlockRegistryContainer;
import com.direwolf20.buildinggadgets.common.registry.block.tile.TileEntityBuilder;
import com.direwolf20.buildinggadgets.common.registry.block.tile.TileEntityRegistryContainer;
import com.direwolf20.buildinggadgets.common.registry.block.tile.TileEntityTypeBuilder;
import com.direwolf20.buildinggadgets.common.util.ref.Reference;
import com.direwolf20.buildinggadgets.common.util.ref.Reference.BlockReference;
import com.direwolf20.buildinggadgets.common.util.ref.Reference.TileEntityReference;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.ObjectHolder;

import static com.direwolf20.buildinggadgets.common.registry.objects.BGItems.itemProperties;
import static com.direwolf20.buildinggadgets.common.registry.objects.BuildingObjects.EFFECT_BLOCK_MATERIAL;

@ObjectHolder(Reference.MODID)
@EventBusSubscriber(modid = Reference.MODID, bus = Bus.MOD)
public final class BGBlocks {

    private BGBlocks() {
    }

    // Blocks
    @ObjectHolder(BlockReference.EFFECT_BLOCK)
    public static EffectBlock effectBlock;
    @ObjectHolder(BlockReference.CONSTRUCTION_BLOCK)
    public static ConstructionBlock constructionBlock;
    @ObjectHolder(BlockReference.CONSTRUCTION_BLOCK_DENSE)
    public static ConstructionBlockDense constructionBlockDense;
    @ObjectHolder(BlockReference.CONSTRUCTION_BLOCK_POWDER)
    public static ConstructionBlockPowder constructionBlockPowder;
    @ObjectHolder(BlockReference.TEMPLATE_MANAGER)
    public static TemplateManager templateManger;

    // No extracted block creation method, because property creation would just make any method call look lengthy
    static void init() {
        container.add(new BlockBuilder(BlockReference.EFFECT_BLOCK_RL)
                .builder(Block.Properties.create(EFFECT_BLOCK_MATERIAL).hardnessAndResistance(20f))
                .setHasNoItem()
                .factory(EffectBlock::new));
        container.add(new BlockBuilder(BlockReference.CONSTRUCTION_BLOCK_RL)
                .builder(Block.Properties.create(Material.ROCK).hardnessAndResistance(2f, 0f))
                .item(itemProperties())
                .withTileEntity(new TileEntityBuilder<>(TileEntityReference.CONSTRUCTION_TILE_RL)
                        // 1.13 -> 1.14 mapping missing (used to be create() )
                        .builder(new TileEntityTypeBuilder<>(ConstructionBlockTileEntity::new))
                        .factory(TileEntityTypeBuilder::build))
                .factory(ConstructionBlock::new));
        container.add(new BlockBuilder(BlockReference.CONSTRUCTION_BLOCK_DENSE_RL)
                .builder(Block.Properties.create(Material.ROCK).hardnessAndResistance(3f, 0f))
                .item(itemProperties())
                .factory(ConstructionBlockDense::new));
        container.add(new BlockBuilder(BlockReference.CONSTRUCTION_BLOCK_POWDER_RL)
                .builder(Block.Properties.create(Material.SAND).hardnessAndResistance(20f))
                .item(itemProperties())
                .factory(ConstructionBlockPowder::new));
        container.add(new BlockBuilder(BlockReference.TEMPLATE_MANAGER_RL)
                .builder(Block.Properties.create(Material.ROCK).hardnessAndResistance(2f))
                .item(itemProperties())
                .withTileEntity(new TileEntityBuilder<>(TileEntityReference.TEMPLATE_MANAGER_TILE_RL)
                        .builder(new TileEntityTypeBuilder<>(TemplateManagerTileEntity::new))
                        .factory(TileEntityTypeBuilder::build))
                .factory(TemplateManager::new));
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        container.register(event);
    }

    @SubscribeEvent
    public static void registerItemBlocks(RegistryEvent.Register<Item> event) {
        container.registerItemBlocks(event);
    }

    static void cleanup() {
        container.clear();
    }

    @ObjectHolder(Reference.MODID)
    @EventBusSubscriber(modid = Reference.MODID, bus = Bus.MOD)
    public static final class BGTileEntities {
        private static final TileEntityRegistryContainer container = new TileEntityRegistryContainer();

        private BGTileEntities() {}
        @ObjectHolder(TileEntityReference.CONSTRUCTION_TILE)
        public static TileEntityType<?> CONSTRUCTION_BLOCK_TYPE;
        @ObjectHolder(TileEntityReference.TEMPLATE_MANAGER_TILE)
        public static TileEntityType<?> TEMPLATE_MANAGER_TYPE;

        static void init() {

        }

        @SubscribeEvent
        public static void registerTiles(RegistryEvent.Register<TileEntityType<?>> event) {
            container.register(event);
        }

        static void clientInit() {
            container.clientInit();
        }

        static void cleanup() {
            container.clear();
        }
    }

    private static final BlockRegistryContainer container = new BlockRegistryContainer(BGTileEntities.container);
}
