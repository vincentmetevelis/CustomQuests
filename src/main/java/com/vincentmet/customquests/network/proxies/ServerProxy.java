package com.vincentmet.customquests.network.proxies;

import com.vincentmet.customquests.lib.Ref;
import com.vincentmet.customquests.lib.handlers.JsonHandler;
import com.vincentmet.customquests.lib.handlers.StructureHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.loading.FMLPaths;

public class ServerProxy implements IProxy{

    @Override
    public void init() {
        Ref.questsLocation = FMLPaths.CONFIGDIR.get().toString() + "\\Quests.json";
        Ref.questBookLocation = FMLPaths.CONFIGDIR.get().toString() + "\\QuestsBook.json";
        Ref.questingProgressLocation = FMLPaths.CONFIGDIR.get().toString() + "\\QuestingProgress.json";
        JsonHandler.loadJson();
        StructureHandler.initQuests(JsonHandler.getQuestsJson());
        StructureHandler.initQuestbook(JsonHandler.getQuestbookJson());
        StructureHandler.initQuestingProgress(JsonHandler.getQuestingProgressJson());
    }

    @Override
    public World getClientWorld() {
        return null;
    }

    @Override
    public PlayerEntity getClientPlayer() {
        return null;
    }
}
