package com.direwolf20.buildinggadgets.common.entities;

import com.direwolf20.buildinggadgets.common.blocks.ConstructionBlockTileEntity;
import com.direwolf20.buildinggadgets.common.registry.objects.BGBlocks;
import com.direwolf20.buildinggadgets.common.registry.objects.BGEntities;
import com.direwolf20.buildinggadgets.common.util.ref.NBTKeys;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Optional;

public class BlockBuildEntity extends EntityBase {

    public enum Mode {
        // Serialization and networking based on `ordinal()`, please DO NOT CHANGE THE ORDER of the enums
        PLACE() {
            @Override
            public void onBuilderEntityDespawn(BlockBuildEntity builder) {
                World world = builder.world;
                BlockPos targetPos = builder.targetPos;
                BlockState targetBlock = builder.setBlock;
                if (builder.isUsingPaste()) {
                    world.setBlockState(targetPos, BGBlocks.constructionBlock.getDefaultState());
                    TileEntity te = world.getTileEntity(targetPos);
                    if (te instanceof ConstructionBlockTileEntity) {
                        ((ConstructionBlockTileEntity) te).setBlockState(targetBlock, targetBlock);
                    }
                    world.addEntity(new ConstructionBlockEntity(world, targetPos, false));
                } else {
                    world.setBlockState(targetPos, targetBlock);
                    BlockPos upPos = targetPos.up();
                    world.getBlockState(targetPos).neighborChanged(world, targetPos, world.getBlockState(upPos).getBlock(), upPos, false);
                }
            }
        },
        REMOVE() {
            @Override
            public void onBuilderEntityDespawn(BlockBuildEntity builder) {
                builder.world.setBlockState(builder.targetPos, Blocks.AIR.getDefaultState());
            }
        },
        REPLACE() {
            @Override
            public void onBuilderEntityDespawn(BlockBuildEntity builder) {
                World world = builder.world;
                world.addEntity(new BlockBuildEntity(world, builder.targetPos, builder.spawnedBy, builder.originalSetBlock, PLACE, builder.isUsingPaste()));
            }
        };

        public static final Mode[] VALUES = values();

        public abstract void onBuilderEntityDespawn(BlockBuildEntity builder);
    }

    private static final DataParameter<Integer> MODE = EntityDataManager.createKey(BlockBuildEntity.class, DataSerializers.VARINT);
    private static final DataParameter<Optional<BlockState>> SET_BLOCK = EntityDataManager.createKey(BlockBuildEntity.class, DataSerializers.OPTIONAL_BLOCK_STATE);
    private static final DataParameter<Boolean> USE_PASTE = EntityDataManager.createKey(BlockBuildEntity.class, DataSerializers.BOOLEAN);

    private BlockState setBlock;
    private BlockState originalSetBlock;
    private LivingEntity spawnedBy;

    private Mode mode;
    private boolean useConstructionPaste;

    public BlockBuildEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    public BlockBuildEntity(World world, BlockPos spawnPos, LivingEntity player, BlockState spawnBlock, Mode mode, boolean usePaste) {
        this(BGEntities.BUILD_BLOCK, world);
        setPosition(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());

        BlockState currentBlock = world.getBlockState(spawnPos);
        TileEntity te = world.getTileEntity(spawnPos);
        targetPos = spawnPos;
        originalSetBlock = spawnBlock;

        if (mode == Mode.REPLACE)
            setBlock = te instanceof ConstructionBlockTileEntity ? te.getBlockState() : currentBlock;
        else
            setBlock = te instanceof ConstructionBlockTileEntity ? te.getBlockState() : spawnBlock;
        setSetBlock(setBlock);

        this.mode = mode;
        setToolMode(mode);

        spawnedBy = player;
        world.setBlockState(spawnPos, BGBlocks.effectBlock.getDefaultState());

        setUsingPaste(usePaste);
    }

    @Override
    protected int getMaxLife() {
        return 20;
    }

    public Mode getToolMode() {
        return Mode.VALUES[dataManager.get(MODE)];
    }

    public void setToolMode(Mode mode) {
        dataManager.set(MODE, mode.ordinal());
    }

    @Nullable
    public BlockState getSetBlock() {
        return dataManager.get(SET_BLOCK).orElse(null);
    }

    public void setSetBlock(@Nullable BlockState state) {
        dataManager.set(SET_BLOCK, Optional.ofNullable(state));
    }

    public void setUsingPaste(boolean paste) {
        dataManager.set(USE_PASTE, paste);
    }

    public boolean isUsingPaste() {
        return dataManager.get(USE_PASTE);
    }

    @Override
    protected void registerData() {
        dataManager.register(MODE, Mode.PLACE.ordinal());
        dataManager.register(SET_BLOCK, Optional.empty());
        dataManager.register(USE_PASTE, useConstructionPaste);
    }

    @Override
    protected void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        setBlock = NBTUtil.readBlockState(compound.getCompound(NBTKeys.ENTITY_BUILD_SET_BLOCK));
        originalSetBlock = NBTUtil.readBlockState(compound.getCompound(NBTKeys.ENTITY_BUILD_ORIGINAL_BLOCK));
        mode = Mode.VALUES[compound.getInt(NBTKeys.GADGET_MODE)];
        useConstructionPaste = compound.getBoolean(NBTKeys.ENTITY_BUILD_USE_PASTE);
    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);

        CompoundNBT blockStateTag = NBTUtil.writeBlockState(setBlock);
        compound.put(NBTKeys.ENTITY_BUILD_SET_BLOCK, blockStateTag);

        blockStateTag = NBTUtil.writeBlockState(originalSetBlock);

        compound.put(NBTKeys.ENTITY_BUILD_ORIGINAL_BLOCK, blockStateTag);
        compound.putInt(NBTKeys.GADGET_MODE, mode.ordinal());
        compound.putBoolean(NBTKeys.ENTITY_BUILD_USE_PASTE, useConstructionPaste);
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return new SSpawnObjectPacket(this);
    }

    @Override
    protected void onSetDespawning() {
        if (world.isRemote || targetPos == null || setBlock == null)
            return;
        mode.onBuilderEntityDespawn(this);
    }

}
