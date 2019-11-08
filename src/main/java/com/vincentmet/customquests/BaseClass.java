package com.vincentmet.customquests;

import com.vincentmet.customquests.lib.Ref;
import com.vincentmet.customquests.lib.handlers.EventHandler;
import com.vincentmet.customquests.lib.handlers.JsonHandler;
import com.vincentmet.customquests.lib.handlers.PacketHandler;
import com.vincentmet.customquests.lib.handlers.StructureHandler;
import com.vincentmet.customquests.network.packets.MessageQuestsServerToClient;
import com.vincentmet.customquests.network.proxies.ClientProxy;
import com.vincentmet.customquests.network.proxies.IProxy;
import com.vincentmet.customquests.network.proxies.ServerProxy;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod(Ref.MODID)
public class BaseClass {
    public static IProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);
    public static BaseClass INSTANCE;

    public BaseClass(){
        INSTANCE = this;
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(EventHandler.class);
    }

    private void setup(final FMLCommonSetupEvent event){
        PacketHandler.init();
        proxy.init();
    }
}