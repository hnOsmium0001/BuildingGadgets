package com.direwolf20.buildinggadgets.common.building.placement;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * Wraps an {@link IBlockProvider} such that all access to the provider will be translated by the given amount as the
 * BlockPos passed into the constructor.
 */
public final class OriginWrapper implements IBlockProvider {

    private final IBlockProvider provider;
    private final BlockPos origin;

    public OriginWrapper(@Nonnull IBlockProvider provider, @Nonnull BlockPos origin) {
        this.provider = Objects.requireNonNull(provider);
        this.origin = Objects.requireNonNull(origin);
    }

    @Override
    @Nonnull
    public BlockPos getTranslation() {
        return provider.getTranslation().add(origin);
    }

    /**
     * Redirects the call to the wrapped IBlockProvider.
     */
    @Override
    public IBlockState at(BlockPos pos) {
        // This should be fine since the parameter is @NoBorrow
        return provider.at(pos.add(origin));
    }

}
