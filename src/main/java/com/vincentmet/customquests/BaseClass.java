package com.vincentmet.customquests;

import com.vincentmet.customquests.lib.Ref;
import com.vincentmet.customquests.lib.handlers.JsonHandler;
import com.vincentmet.customquests.lib.handlers.StructureHandler;
import com.vincentmet.customquests.network.proxies.ClientProxy;
import com.vincentmet.customquests.network.proxies.IProxy;
import com.vincentmet.customquests.network.proxies.ServerProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod(Ref.MODID)
public class BaseClass {
    public static IProxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new ServerProxy());
    public static BaseClass INSTANCE;

    public BaseClass(){
        INSTANCE = this;
        //ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_CONFIG);
        //ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONFIG);
        //ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.SERVER_CONFIG);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::loadComplete);
        //Config.loadConfig(Config.CLIENT_CONFIG, FMLPaths.CONFIGDIR.get().resolve("customquests-client.toml"));
        //Config.loadConfig(Config.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve("customquests-common.toml"));
        //Config.loadConfig(Config.SERVER_CONFIG, FMLPaths.CONFIGDIR.get().resolve("customquests-server.toml"));
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event){
        proxy.init();
    }

    private void loadComplete(final FMLClientSetupEvent event){
        Ref.questsLocation = FMLPaths.CONFIGDIR.get().toString() + "\\Quests.json";
        Ref.questBookLocation = FMLPaths.CONFIGDIR.get().toString() + "\\QuestsBook.json";
        Ref.questingProgressLocation = FMLPaths.CONFIGDIR.get().toString() + "\\QuestingProgress.json";
        JsonHandler.loadJson();
        StructureHandler.initQuests();
        StructureHandler.initQuestbook();
        StructureHandler.initQuestingProgress();
    }
}