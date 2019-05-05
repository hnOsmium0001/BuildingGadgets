package com.direwolf20.buildinggadgets.common.util.helpers;

import com.direwolf20.buildinggadgets.common.util.ref.NBTKeys;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

/**
 * Utility class providing additional Methods for reading and writing array's which are not normally provided as
 * NBT-Objects by Minecraft.
 */
public class NBTHelper {

    public static NBTTagByteArray createBooleanList(boolean[] booleans) {
        byte[] bytes = new byte[booleans.length];
        for (int i = 0; i < booleans.length; ++i) {
            bytes[i] = (byte) (booleans[i] ? 0 : 1);
        }
        return new NBTTagByteArray(bytes);
    }

    public static NBTTagByteArray createBooleanList(Boolean[] booleans) {
        byte[] bytes = new byte[booleans.length];
        for (int i = 0; i < booleans.length; ++i) {
            bytes[i] = (byte) (booleans[i] ? 0 : 1);
        }
        return new NBTTagByteArray(bytes);
    }

    public static NBTTagList createShortList(short[] shorts) {
        NBTTagList list = new NBTTagList();
        for (short s : shorts) {
            list.add(new NBTTagShort(s));
        }
        return list;
    }

    public static NBTTagList createShortList(Short[] shorts) {
        NBTTagList list = new NBTTagList();
        for (short s : shorts) {
            list.add(new NBTTagShort(s));
        }
        return list;
    }

    @Nonnull
    public static NBTTagList createFloatList(float[] floats) {
        NBTTagList list = new NBTTagList();
        for (float f : floats) {
            list.add(new NBTTagFloat(f));
        }
        return list;
    }

    @Nonnull
    public static NBTTagList createFloatList(Float[] floats) {
        NBTTagList list = new NBTTagList();
        for (Float f : floats) {
            list.add(new NBTTagFloat(f));
        }
        return list;
    }

    @Nonnull
    public static NBTTagList createDoubleList(double[] doubles) {
        NBTTagList list = new NBTTagList();
        for (double d : doubles) {
            list.add(new NBTTagDouble(d));
        }
        return list;
    }

    @Nonnull
    public static NBTTagList createDoubleList(Double[] doubles) {
        NBTTagList list = new NBTTagList();
        for (Double d : doubles) {
            list.add(new NBTTagDouble(d));
        }
        return list;
    }

    @Nonnull
    public static short[] readShortList(NBTTagList shorts) {
        short[] res = new short[shorts.size()];
        IntList failed = new IntArrayList();
        for (int i = 0; i < shorts.size(); i++) {
            INBTBase nbt = shorts.get(i);
            if (nbt instanceof NBTTagShort) {
                res[i] = ((NBTTagShort) nbt).getShort();
            } else {
                res[i] = 0;
                failed.add(i);
            }
        }
        if (!failed.isEmpty()) {
            short[] shortened = new short[res.length - failed.size()];
            int shortenedCount = 0;
            for (int i = 0; i < res.length; i++) {
                if (failed.contains(i))
                    continue;
                shortened[shortenedCount] = res[i];
                ++shortenedCount;
            }
            res = shortened;
        }
        return res;
    }

    @Nonnull
    public static Short[] readBShortList(NBTTagList shorts) {
        Short[] res = new Short[shorts.size()];
        IntList failed = new IntArrayList();
        for (int i = 0; i < shorts.size(); i++) {
            INBTBase nbt = shorts.get(i);
            if (nbt instanceof NBTTagShort) {
                res[i] = ((NBTTagShort) nbt).getShort();
            } else {
                res[i] = 0;
                failed.add(i);
            }
        }
        if (!failed.isEmpty()) {
            Short[] shortened = new Short[res.length - failed.size()];
            int shortenedCount = 0;
            for (int i = 0; i < res.length; i++) {
                if (failed.contains(i))
                    continue;
                shortened[shortenedCount] = res[i];
                ++shortenedCount;
            }
            res = shortened;
        }
        return res;
    }

    @Nonnull
    public static NBTTagList createStringList(String[] strings) {
        NBTTagList list = new NBTTagList();
        for (String s : strings) {
            list.add(new NBTTagString(s));
        }
        return list;
    }

    @Nonnull
    public static float[] readFloatList(NBTTagList floats) {
        float[] res = new float[floats.size()];
        IntList failed = new IntArrayList();
        for (int i = 0; i < floats.size(); i++) {
            INBTBase nbt = floats.get(i);
            if (nbt instanceof NBTTagFloat) {
                res[i] = ((NBTTagFloat) nbt).getFloat();
            } else {
                res[i] = 0;
                failed.add(i);
            }
        }
        if (!failed.isEmpty()) {
            float[] shortened = new float[res.length - failed.size()];
            int shortenedCount = 0;
            for (int i = 0; i < res.length; i++) {
                if (failed.contains(i))
                    continue;
                shortened[shortenedCount] = res[i];
                ++shortenedCount;
            }
            res = shortened;
        }
        return res;
    }

    @Nonnull
    public static Float[] readBFloatList(NBTTagList floats) {
        Float[] res = new Float[floats.size()];
        IntList failed = new IntArrayList();
        for (int i = 0; i < floats.size(); i++) {
            INBTBase nbt = floats.get(i);
            if (nbt instanceof NBTTagFloat) {
                res[i] = ((NBTTagFloat) nbt).getFloat();
            } else {
                res[i] = 0f;
                failed.add(i);
            }
        }
        if (!failed.isEmpty()) {
            Float[] shortened = new Float[res.length - failed.size()];
            int shortenedCount = 0;
            for (int i = 0; i < res.length; i++) {
                if (failed.contains(i))
                    continue;
                shortened[shortenedCount] = res[i];
                ++shortenedCount;
            }
            res = shortened;
        }
        return res;
    }

    @Nonnull
    public static double[] readDoubleList(NBTTagList doubles) {
        double[] res = new double[doubles.size()];
        IntList failed = new IntArrayList();
        for (int i = 0; i < doubles.size(); i++) {
            INBTBase nbt = doubles.get(i);
            if (nbt instanceof NBTTagDouble) {
                res[i] = ((NBTTagDouble) nbt).getDouble();
            } else {
                res[i] = 0;
                failed.add(i);
            }
        }
        if (!failed.isEmpty()) {
            double[] shortened = new double[res.length - failed.size()];
            int shortenedCount = 0;
            for (int i = 0; i < res.length; i++) {
                if (failed.contains(i))
                    continue;
                shortened[shortenedCount] = res[i];
                ++shortenedCount;
            }
            res = shortened;
        }
        return res;
    }

    @Nonnull
    public static Double[] readBDoubleList(NBTTagList doubles) {
        Double[] res = new Double[doubles.size()];
        IntList failed = new IntArrayList();
        for (int i = 0; i < doubles.size(); i++) {
            INBTBase nbt = doubles.get(i);
            if (nbt instanceof NBTTagDouble) {
                res[i] = ((NBTTagDouble) nbt).getDouble();
            } else {
                res[i] = 0.0;
                failed.add(i);
            }
        }
        if (!failed.isEmpty()) {
            Double[] shortened = new Double[res.length - failed.size()];
            int shortenedCount = 0;
            for (int i = 0; i < res.length; i++) {
                if (failed.contains(i))
                    continue;
                shortened[shortenedCount] = res[i];
                ++shortenedCount;
            }
            res = shortened;
        }
        return res;
    }

    @Nonnull
    public static String[] readStringList(NBTTagList strings) {
        String[] res = new String[strings.size()];
        IntList failed = new IntArrayList();
        for (int i = 0; i < strings.size(); i++) {
            INBTBase nbt = strings.get(i);
            if (nbt instanceof NBTTagString) {
                res[i] = nbt.getString();
            } else {
                res[i] = "";
                failed.add(i);
            }
        }
        if (!failed.isEmpty()) {
            String[] shortened = new String[res.length - failed.size()];
            int shortenedCount = 0;
            for (int i = 0; i < res.length; i++) {
                if (failed.contains(i))
                    continue;
                shortened[shortenedCount] = res[i];
                ++shortenedCount;
            }
            res = shortened;
        }
        return res;
    }

    public static boolean[] readBooleanList(NBTTagByteArray booleans) {
        byte[] bytes = booleans.getByteArray();
        boolean[] res = new boolean[bytes.length];
        for (int i = 0; i < bytes.length; ++i) {
            res[i] = bytes[i] == 0;
        }
        return res;
    }

    public static Boolean[] readBBooleanList(NBTTagByteArray booleans) {
        byte[] bytes = booleans.getByteArray();
        Boolean[] res = new Boolean[bytes.length];
        for (int i = 0; i < bytes.length; ++i) {
            res[i] = bytes[i] == 0;
        }
        return res;
    }

    public static <K, V> NBTTagCompound serializeMap(Map<K, V> map, Function<K, NBTTagCompound> keySerializer, Function<V, NBTTagCompound> valueSerializer) {
        NBTTagCompound result = new NBTTagCompound();

        NBTTagList keys = new NBTTagList();
        NBTTagList values = new NBTTagList();
        for (Map.Entry<K, V> entry : map.entrySet()) {
            keys.add(keySerializer.apply(entry.getKey()));
            values.add(valueSerializer.apply(entry.getValue()));
        }
        result.setTag(NBTKeys.MAP_SERIALIZE_KEYS, keys);
        result.setTag(NBTKeys.MAP_SERIALIZE_VALUES, values);

        return result;
    }

    public static <K, V> Map<K, V> deserializeMap(NBTTagCompound map, Map<K, V> toAppendTo, Function<NBTTagCompound, K> keyDeserializer, Function<NBTTagCompound, V> valueDeserializer) {
        NBTTagList keys = map.getList(NBTKeys.MAP_SERIALIZE_KEYS, Constants.NBT.TAG_COMPOUND);
        NBTTagList values = map.getList(NBTKeys.MAP_SERIALIZE_VALUES, Constants.NBT.TAG_COMPOUND);
        Preconditions.checkArgument(keys.size() == values.size());

        for (int i = 0; i < keys.size(); i++) {
            toAppendTo.put(
                    keyDeserializer.apply(keys.getCompound(i)),
                    valueDeserializer.apply(values.getCompound(i))
            );
        }

        return toAppendTo;
    }

    public static UUID readItemUUID(ItemStack stack) {
        NBTTagCompound tag = NBTHelper.getOrNewTag(stack);
        UUID uuid = NBTHelper.getUUIDNullable(tag);

        if (uuid == null) {
            uuid = UUID.randomUUID();
            NBTHelper.writeUUID(tag, uuid);
        }
        return uuid;
    }

    /**
     * Read the least significant bits ({@code "least_bits}) and the most significant bits ({@code "most_bits"}), and
     * combine them to create an {@link UUID}. If these two values does not exist, it will return {@code null}.
     */
    @Nullable
    public static UUID getUUIDNullable(NBTTagCompound tag) {
        long leastSignificantBits = tag.getLong("least_bits");
        long mostSignificantBits = tag.getLong("most_bits");
        if (leastSignificantBits == 0L && mostSignificantBits == 0L)
            return null;

        return new UUID(leastSignificantBits, mostSignificantBits);
    }

    /**
     * Read the least significant bits ({@code "least_bits}) and the most significant bits ({@code "most_bits"}), and
     * combine them to create an {@link UUID}. If these two values does not exist, it will return {@code
     * UUID{leastSignificantBits=0L, mostSignificantBits=0L}}.
     */
    public static UUID readUUID(NBTTagCompound tag) {
        return new UUID(tag.getLong("least_bits"), tag.getLong("most_bits"));
    }

    /**
     * Write the least and most significant bits in to the given tag as {@code "least_bits"} and {@code "most_bits"}.
     *
     * @return The parameter {@code tag}
     * @see NBTUtil#writeUniqueId(UUID)
     */
    public static NBTTagCompound writeUUID(NBTTagCompound tag, UUID uuid) {
        tag.setLong("least_bits", uuid.getLeastSignificantBits());
        tag.setLong("most_bits", uuid.getMostSignificantBits());
        return tag;
    }

    /**
     * If the given stack has a tag, returns it. If the given stack does not have a tag, it will set a reference and
     * return the new tag compound.
     */
    public static NBTTagCompound getOrNewTag(ItemStack stack) {
        if (stack.hasTag()) {
            return stack.getTag();
        }
        NBTTagCompound tag = new NBTTagCompound();
        stack.setTag(tag);
        return tag;
    }

}
