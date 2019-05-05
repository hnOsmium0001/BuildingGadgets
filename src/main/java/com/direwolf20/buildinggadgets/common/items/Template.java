package com.direwolf20.buildinggadgets.common.items;

import com.direwolf20.buildinggadgets.client.events.EventTooltip;
import com.direwolf20.buildinggadgets.client.gui.GuiMod;
import com.direwolf20.buildinggadgets.common.util.GadgetUtils;
import com.direwolf20.buildinggadgets.common.util.helpers.NBTHelper;
import com.direwolf20.buildinggadgets.common.util.lang.Styles;
import com.direwolf20.buildinggadgets.common.util.lang.TooltipTranslation;
import com.direwolf20.buildinggadgets.common.util.ref.NBTKeys;
import com.direwolf20.buildinggadgets.common.world.data.IHasBlockMaps;
import com.direwolf20.buildinggadgets.common.world.data.TemplateStorage;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class Template extends Item implements ITemplate {

    public Template(Properties builder) {
        super(builder.maxStackSize(1));
    }

    @Override
    public IHasBlockMaps getWorldSave(World world) {
        return TemplateStorage.fromWorld(world);
    }

    @Override
    @Nullable
    public UUID getUUID(ItemStack stack) {
        return NBTHelper.readItemUUID(stack);
    }

    public static void setName(ItemStack stack, String name) {
        GadgetUtils.writeStringToNBT(stack, name, NBTKeys.TEMPLATE_NAME);
    }

    public static String getName(ItemStack stack) {
        return GadgetUtils.getStringFromNBT(stack, NBTKeys.TEMPLATE_NAME);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);

        tooltip.add(TooltipTranslation.TEMPLATE_NAME.componentTranslation(getName(stack)).setStyle(Styles.AQUA));
        EventTooltip.addTemplatePadding(stack, tooltip);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        if (world.isRemote)
            GuiMod.MATERIAL_LIST.openScreen(player);
        ItemStack itemstack = player.getHeldItem(hand);
        return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
    }

}
