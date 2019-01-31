package com.direwolf20.buildinggadgets.common;

import com.direwolf20.buildinggadgets.client.proxy.ClientProxy;
import com.direwolf20.buildinggadgets.common.commands.BlockMapCommand;
import com.direwolf20.buildinggadgets.common.config.Config;
import com.direwolf20.buildinggadgets.common.config.SyncedConfig;
import com.direwolf20.buildinggadgets.common.events.AnvilRepairHandler;
import com.direwolf20.buildinggadgets.common.network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.command.Commands;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

@Mod(value = BuildingGadgets.MODID)
public class BuildingGadgets {
    public static final String MODID = "buildinggadgets";
    public static final String MODNAME = "Building Gadgets";
    public static final String VERSION = "@VERSION@";
    public static final String UPDATE_JSON = "@UPDATE@";
    public static final String DEPENDENCIES = "required-after:forge@[14.23.3.2694,)";

    public static Logger logger = LogManager.getLogger();
    private static BuildingGadgets theMod = null;

    public static BuildingGadgets getInstance() {
        assert theMod != null;
        return theMod;
    }

    private Supplier<Minecraft> mcSupplier;
    public BuildingGadgets() {
        theMod = (BuildingGadgets) FMLModLoadingContext.get().getActiveContainer().getMod();
        final IEventBus eventBus = FMLModLoadingContext.get().getModEventBus();

        eventBus.addListener(this::setup);
        eventBus.addListener(this::serverLoad);

        // Client only registering
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
            eventBus.addListener(this::clientInit);
            eventBus.addListener(ClientProxy::clientSetup);
            eventBus.addListener(ClientProxy::renderWorldLastEvent);
            eventBus.addListener(ClientProxy::registerSprites);

            MinecraftForge.EVENT_BUS.addListener(ClientProxy::registerModels);
        });

        if (!SyncedConfig.poweredByFE) {
            MinecraftForge.EVENT_BUS.register(new AnvilRepairHandler());
        }

        MinecraftForge.EVENT_BUS.register(this);
        mcSupplier = null;
    }

    @Nonnull
    public Minecraft getMinecraft() {
        if (mcSupplier == null) throw new RuntimeException("Attempted to access Minecraft instance server Side");
        Minecraft mc = mcSupplier.get();
        assert mc != null;
        return mc;
    }

    private void setup(final FMLCommonSetupEvent event) {
        Config.load();

        DeferredWorkQueue.runLater(PacketHandler::register);
// @todo: reimplement @since 1.13.x
//        NetworkRegistry.INSTANCE.registerGuiHandler(BuildingGadgets.instance, new GuiProxy());
    }

    private void clientInit(final FMLClientSetupEvent event) {
        mcSupplier = event.getMinecraftSupplier();
    }

    private void serverLoad(FMLServerStartingEvent event) {

        event.getCommandDispatcher().register(
                Commands.literal(MODID)
                    .then(BlockMapCommand.registerList())
                    .then(BlockMapCommand.registerDelete())
        );

    }
}
