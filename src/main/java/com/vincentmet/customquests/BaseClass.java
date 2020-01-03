package com.vincentmet.customquests;

import com.vincentmet.customquests.commands.CustomQuestsCommand;
import com.vincentmet.customquests.lib.Ref;
import com.vincentmet.customquests.lib.handlers.EventHandler;
import com.vincentmet.customquests.lib.handlers.PacketHandler;
import com.vincentmet.customquests.network.proxies.ClientProxy;
import com.vincentmet.customquests.network.proxies.IProxy;
import com.vincentmet.customquests.network.proxies.ServerProxy;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.logging.Logger;

@Mod(Ref.MODID)
public class BaseClass {
    public static IProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);
    public static BaseClass INSTANCE;

    public BaseClass(){
        INSTANCE = this;
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(EventHandler.class);
        MinecraftForge.EVENT_BUS.addListener(this::onServerStart);

    }

    private void setup(final FMLCommonSetupEvent event){
        PacketHandler.init();
        proxy.init();
    }

    public void onServerStart(FMLServerStartingEvent event){
        new CustomQuestsCommand(event.getCommandDispatcher());
    }
}