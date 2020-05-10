package com.vincentmet.customquests.lib.handlers;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;

import java.nio.file.Path;

public class ConfigHandler {
    public static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
    public static ForgeConfigSpec COMMON_CONFIG;

    public static ForgeConfigSpec.BooleanValue giveDeviceOnLogin;
    public static ForgeConfigSpec.BooleanValue giveDeviceOnFirstLogin;

    static{
        giveDeviceOnLogin = COMMON_BUILDER.comment("Gives the player a Questing Device when loading into a world if the player doesn't have one yet in their inventory").define("giveDeviceOnLogin", false);
        giveDeviceOnFirstLogin = COMMON_BUILDER.comment("If the 'giveDeviceOnLogin' config setting is set to false but this one to true, you will still receive a Questing Device, but only when first loading into the server.").define("giveDeviceOnFirstLogin", true);

        COMMON_CONFIG = COMMON_BUILDER.build();
    }

    public static void loadConfig(ForgeConfigSpec spec, Path path){
        final CommentedFileConfig config = CommentedFileConfig.builder(path).sync().autosave().writingMode(WritingMode.REPLACE).build();
        config.load();
        spec.setConfig(config);
    }
}