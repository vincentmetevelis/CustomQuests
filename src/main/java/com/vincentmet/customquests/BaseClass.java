package com.vincentmet.customquests;

import com.vincentmet.customquests.commands.CustomQuestsCommand;
import com.vincentmet.customquests.lib.Ref;
import com.vincentmet.customquests.lib.handlers.*;
import com.vincentmet.customquests.network.proxies.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod(Ref.MODID)
public class BaseClass {
    public static final IProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);

    public BaseClass(){
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(EventHandler.class);
        MinecraftForge.EVENT_BUS.addListener(this::onServerStart);
        ConfigHandler.loadConfig(ConfigHandler.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve("customquests-common.toml"));
    }

    private void setup(final FMLCommonSetupEvent event){
        PacketHandler.init();
        proxy.init();
    }

    private void onServerStart(FMLServerStartingEvent event){
        new CustomQuestsCommand(event.getCommandDispatcher());
    }
}
//todo make the scrollable list gui element scroll per item, not by bitmap