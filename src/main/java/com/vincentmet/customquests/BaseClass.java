package com.vincentmet.customquests;

import com.vincentmet.customquests.commands.CustomQuestsCommand;
import com.vincentmet.customquests.lib.Ref;
import com.vincentmet.customquests.lib.handlers.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod(Ref.MODID)
public class BaseClass {

    public BaseClass(){
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupCommon);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupClient);
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new EventHandler());
        MinecraftForge.EVENT_BUS.addListener(this::onServerStart);
        ConfigHandler.loadConfig(ConfigHandler.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve("customquests-common.toml"));
    }

    private void setupCommon(final FMLCommonSetupEvent event){
        PacketHandler.init();
    }
    private void setupClient(final FMLClientSetupEvent event){
        ClientRegistry.registerKeyBinding(Objects.KeyBindings.OPEN_QUESTINGDEVICE);
    }

    private void onServerStart(FMLServerStartingEvent event){
        new CustomQuestsCommand(event.getServer().getCommandManager().getDispatcher());
    }
}
//todo make the scrollable list gui element scroll per item, not by bitmap