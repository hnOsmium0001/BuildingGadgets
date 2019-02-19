package com.direwolf20.buildinggadgets.common.building;

import com.direwolf20.buildinggadgets.common.building.placement.IPlacementSequence;
import com.direwolf20.buildinggadgets.common.tools.GadgetUtils;
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.PeekingIterator;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import java.util.Spliterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Represents a region in the world with a finite and nonzero size.
 * <p>Javadoc are copied from {@link AxisAlignedBB} with some modifications.</p>
 */
public final class Region implements IPlacementSequence {
    private final int minX;
    private final int minY;
    private final int minZ;
    private final int maxX;
    private final int maxY;
    private final int maxZ;

    public Region(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        this.minX = Math.min(minX,maxX);
        this.minY = Math.min(minY,maxY);
        this.minZ = Math.min(minZ,maxZ);
        this.maxX = Math.max(minX,maxX);
        this.maxY = Math.max(minY,maxY);
        this.maxZ = Math.max(minZ,maxZ);
    }

    public Region(Vec3i vertex) {
        this(vertex, vertex);
    }

    public Region(Vec3i minVertex, Vec3i maxVertex) {
        this(minVertex.getX(),minVertex.getY(),minVertex.getZ(),maxVertex.getX(),maxVertex.getY(),maxVertex.getZ());
    }

    public int getMinX() {
        return minX;
    }

    public int getMinY() {
        return minY;
    }

    public int getMinZ() {
        return minZ;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMaxY() {
        return maxY;
    }

    public int getMaxZ() {
        return maxZ;
    }

    public long size() {
        return ((long)(getMaxX() - getMinX()))*((long)(getMaxY() - getMinY()))*((long)(getMaxZ() - getMinZ()));
    }

    /**
     * Translates this Region by the given amount
     * @param x how much to translate x
     * @param y how much to translate y
     * @param z how much to translate z
     * @return A new Region, translated by the given coordinates
     */
    public Region translate(int x, int y, int z) {
        return new Region(getMinX() + x,getMinY() + y,getMinZ() + z,getMaxX() + x,getMaxY() + y,getMaxZ() + z);
    }

    /**
     * @see #translate(int, int, int)
     */
    public Region translate(Vec3i direction) {
        return this.translate(direction.getX(), direction.getY(), direction.getZ());
    }


    /**
     *
     * @param x x-Growth
     * @param y y-Growth
     * @param z z-Growth
     * @return A new Region who's max Coordinates are increased by the given amount
     */
    public Region grow(int x, int y, int z) {
        return new Region(getMinX(),getMinY(),getMinZ(),getMaxX()+x,getMaxY()+y,getMaxZ()+z);
    }


    public Region grow(int size) {
        this.grow(size, size, size);
        return this;
    }

    /**
     * See {@link #grow(int, int, int)} subtracting instead of adding.
     *
     * @return this
     */
    public Region shrink(int x, int y, int z) {
        return this.grow(-x, -y, -z);
    }

    /**
     * See {@link #grow(int)} subtracting instead of adding.
     *
     * @return this
     */
    public Region shrink(int size) {
        return this.grow(-size);
    }

    /**
     * Expand the current region by the given amount in both directions. Negative
     * values will shrink the region instead of expanding it.
     * <p>
     * Side lengths will be increased by 2 times the value of the parameters, since both min and max are changed.
     * </p>
     * <p>
     * If contracting and the amount to contract by is larger than the length of a side, then the side will wrap (still
     * creating a valid region - see last ample).
     * </p>
     *
     * <h3>Samples:</h3>
     * <table>
     * <tr><th>Input</th><th>Result</th></tr>
     * <tr><td><pre><code>new Region(0, 0, 0, 1, 1, 1).grow(2, 2, 2)</code></pre></td><td><pre><samp>box[-2, -2, -2 -> 3, 3, 3]</samp></pre></td></tr>
     * <tr><td><pre><code>new Region(0, 0, 0, 6, 6, 6).grow(-2, -2, -2)</code></pre></td><td><pre><samp>box[2, 2, 2 -> 4, 4, 4]</samp></pre></td></tr>
     * <tr><td><pre><code>new Region(5, 5, 5, 7, 7, 7).grow(0, 1, -1)</code></pre></td><td><pre><samp>box[5, 4, 6 -> 7, 8, 6]</samp></pre></td></tr>
     * <tr><td><pre><code>new Region(1, 1, 1, 3, 3, 3).grow(-4, -2, -3)</code></pre></td><td><pre><samp>box[-1, 1, 0 -> 5, 3, 4]</samp></pre></td></tr>
     * </table>
     *
     * <h3>See Also:</h3>
     * <ul>
     * <li>{@link #grow(int)} - version of this that expands in all directions from one parameter.</li>
     * <li>{@link #shrink(int)} - contracts in all directions</li>
     * </ul>
     *
     * @return this
     */
    public Region expand(int x, int y, int z) {
        return new Region(getMinX() - x,getMinY() - y,getMinZ() - z,getMaxX() + x,getMaxY() + y,getMaxZ() + z);
    }

    public Region expand(Vec3i vec) {
        return expand(vec.getX(),vec.getY(),vec.getZ());
    }

    /**
     * Expand the current region by the given value in the max values. Equivalent to {@link
     * #expand(int)}  with the given value for all 3 params. Negative values will shrink the region.
     * <br/>
     * Side lengths will be increased by 2 times the value of the parameter, since both min and max are changed.
     * <br/>
     * If contracting and the amount to contract by is larger than the length of a side, then the side will wrap (still
     * creating a valid region - see samples on {@link #grow(int, int, int)}).
     *
     * @return this
     */
    public Region expand(int size) {
        return expand(size,size,size);
    }

    public Region collapse(int x, int y, int z) {
        return expand(-x,-y,-z);
    }

    public Region collapse(Vec3i vec) {
        return collapse(vec.getX(),vec.getY(),vec.getZ());
    }

    public Region collapse(int size) {
        return expand(-size);
    }

    /**
     * Create a new region with the intersecting part between the two regions.
     *
     * @return a new region
     */
    public Region intersect(Region other) {
        int minX = Math.max(this.getMinX(), other.getMinX());
        int minY = Math.max(this.getMinY(), other.getMinY());
        int minZ = Math.max(this.getMinZ(), other.getMinZ());
        int maxX = Math.min(this.getMaxX(), other.getMaxX());
        int maxY = Math.min(this.getMaxY(), other.getMaxY());
        int maxZ = Math.min(this.getMaxZ(), other.getMaxZ());
        return new Region(minX, minY, minZ, maxX, maxY, maxZ);
    }

    /**
     * Create a new region that encloses both regions
     *
     * @return a new region
     */
    public Region union(Region other) {
        int minX = Math.min(this.getMinX(), other.getMinX());
        int minY = Math.min(this.getMinY(), other.getMinY());
        int minZ = Math.min(this.getMinZ(), other.getMinZ());
        int maxX = Math.max(this.getMaxX(), other.getMaxX());
        int maxY = Math.max(this.getMaxY(), other.getMaxY());
        int maxZ = Math.max(this.getMaxZ(), other.getMaxZ());
        return new Region(minX, minY, minZ, maxX, maxY, maxZ);
    }

    /**
     * @return whether or not this {@link BlockPos} lies within this Region
     */
    @Override
    public boolean mayContain(int x, int y, int z) {
        return x>=getMinX() && y>=getMinY() && z>=getMaxZ() && x<=getMaxX() && y<=getMaxY() && z<=getMaxZ();
    }

    public boolean mayContain(Vec3i vec) {
        return mayContain(vec.getX(),vec.getY(),vec.getZ());
    }

    /**
     * @return a new Region with the exact same properties
     */
    @Override
    public Region copy() {
        return new Region(getMinX(), getMinY(), getMinZ(), getMaxX(), getMaxY(), getMaxZ());
    }

    /**
     * @return this
     */
    @Override
    public Region getBoundingBox() {
        return this;
    }

    /**
     * <p>
     * The first result will have the minimum x, y, and z value. In the process it will advance in positive z-y-x order as used in BG-Code on various other places.
     * Positions provided by this Iterator may be considered ordered.
     * </p>
     *
     * @return A {@link PeekingIterator} over all positions in this Region
     * @see GadgetUtils#POSITION_COMPARATOR
     * @implSpec starts at (minX, minY, minZ), ends at (maxX, maxY, maxZ)
     * @implNote This Iterator cannot support Removal Operations
     */
    @Override
    public PeekingIterator<BlockPos> iterator() {
        return new RegionIterator();
    }

    /**
     * @return a Stream representing the Positions in this PlacementSequence
     */
    @Override
    public Stream<BlockPos> stream() {
        return StreamSupport.stream(spliterator(),false);
    }

    /**
     * Creates a {@link Spliterator} over the Blocks described by this
     *  {@code Region}.
     *
     * @return a {@link Spliterator} over the Blocks described by this
     * {@code Region}.
     * @implSpec The returned {@link Spliterator} will be Immutable, Sorted and Sized
     * @since 1.8
     */
    @Override
    public Spliterator<BlockPos> spliterator() {
        return new RegionSpliterator(this);
    }

    private final class RegionIterator extends AbstractIterator<BlockPos> implements PeekingIterator<BlockPos>{
        private int posX = getMinX();
        private int posY = getMinY();
        private int posZ = getMinZ();

        private RegionIterator() {}

        @Override
        protected BlockPos computeNext() {
            if (this.isTerminated())
                return endOfData();

            if (!isZOverflowed()) {
                posZ++;
            } else if (!isYOverflowed()) {
                posZ = getMinZ();
                posY++;
            } else if (!isXOverflowed()) {
                posZ = getMinZ();
                posY = getMinY();
                posX++;
            }
            return new BlockPos(posX,posY,posZ);
        }

        private boolean isXOverflowed() {
            return posX >= maxX;
        }

        private boolean isYOverflowed() {
            return posY >= maxY;
        }

        private boolean isZOverflowed() {
            return posZ >= maxZ;
        }

        private boolean isTerminated() {
            return isXOverflowed() && isYOverflowed() && isZOverflowed();
        }
    }

}
