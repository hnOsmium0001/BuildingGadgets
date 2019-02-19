package com.direwolf20.buildinggadgets.common.building.placement;

import com.direwolf20.buildinggadgets.common.building.Region;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import java.util.stream.Stream;

/**
 * Represents all block that should be changed for the build.
 * <p>
 * The yielding position should be inside the {@link #getBoundingBox()}. They do not have to be continuous, or ordered
 * however it should not yield any repeating positions and have a finite number of possible results.
 * </p>
 */
public interface IPlacementSequence extends Iterable<BlockPos> {

    /**
     * The bounding box such that all yielding positions are inside the region.
     */
    Region getBoundingBox();

    /**
     *
     * @return false if this PlacementSequence definitely doesn't contain the specified Position
     * @implSpec If the computation is costly, then this {@code IPlacementSequence} may choose to return always true
     */
    boolean mayContain(int x, int y, int z);

    /**
     *
     * @return a Stream representing the Positions in this PlacementSequence
     */
    Stream<BlockPos> stream();

    /**
     *
     * @return a copy of this Placement Sequence
     */
    IPlacementSequence copy();
}
