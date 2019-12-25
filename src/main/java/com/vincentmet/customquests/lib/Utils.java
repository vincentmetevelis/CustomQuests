package com.vincentmet.customquests.lib;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.vincentmet.customquests.lib.handlers.PacketHandler;
import com.vincentmet.customquests.network.packets.MessageUpdateQuestProgressServerToClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.PacketDistributor;
import org.apache.commons.lang3.StringUtils;
import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.util.UUID;

public class Utils {
    public static String colorify(String string){
        for(int i=0;i<StringUtils.countMatches(string, "~AQUA~");i++)           string = string.replace("~AQUA~",           TextFormatting.AQUA + "");
        for(int i=0;i<StringUtils.countMatches(string, "~BLACK~");i++)          string = string.replace("~BLACK~",          TextFormatting.BLACK + "");
        for(int i=0;i<StringUtils.countMatches(string, "~BLUE~");i++)           string = string.replace("~BLUE~",           TextFormatting.BLUE + "");
        for(int i=0;i<StringUtils.countMatches(string, "~BOLD~");i++)           string = string.replace("~BOLD~",           TextFormatting.BOLD + "");
        for(int i=0;i<StringUtils.countMatches(string, "~DARK_AQUA~");i++)      string = string.replace("~DARK_AQUA~",      TextFormatting.DARK_AQUA + "");
        for(int i=0;i<StringUtils.countMatches(string, "~DARK_BLUE~");i++)      string = string.replace("~DARK_BLUE~",      TextFormatting.DARK_BLUE + "");
        for(int i=0;i<StringUtils.countMatches(string, "~DARK_GRAY~");i++)      string = string.replace("~DARK_GRAY~",      TextFormatting.DARK_GRAY + "");
        for(int i=0;i<StringUtils.countMatches(string, "~DARK_GREEN~");i++)     string = string.replace("~DARK_GREEN~",     TextFormatting.DARK_GREEN + "");
        for(int i=0;i<StringUtils.countMatches(string, "~DARK_PURPLE~");i++)    string = string.replace("~DARK_PURPLE~",    TextFormatting.DARK_PURPLE + "");
        for(int i=0;i<StringUtils.countMatches(string, "~DARK_RED~");i++)       string = string.replace("~DARK_RED~",       TextFormatting.DARK_RED + "");
        for(int i=0;i<StringUtils.countMatches(string, "~GOLD~");i++)           string = string.replace("~GOLD~",           TextFormatting.GOLD + "");
        for(int i=0;i<StringUtils.countMatches(string, "~GRAY~");i++)           string = string.replace("~GRAY~",           TextFormatting.GRAY + "");
        for(int i=0;i<StringUtils.countMatches(string, "~GREEN~");i++)          string = string.replace("~GREEN~",          TextFormatting.GREEN + "");
        for(int i=0;i<StringUtils.countMatches(string, "~ITALIC~");i++)         string = string.replace("~ITALIC~",         TextFormatting.ITALIC + "");
        for(int i=0;i<StringUtils.countMatches(string, "~LIGHT_PURPLE~");i++)   string = string.replace("~LIGHT_PURPLE~",   TextFormatting.LIGHT_PURPLE + "");
        for(int i=0;i<StringUtils.countMatches(string, "~OBFUSCATED~");i++)     string = string.replace("~OBFUSCATED~",     TextFormatting.OBFUSCATED + "");
        for(int i=0;i<StringUtils.countMatches(string, "~RED~");i++)            string = string.replace("~RED~",            TextFormatting.RED + "");
        for(int i=0;i<StringUtils.countMatches(string, "~RESET~");i++)          string = string.replace("~RESET~",          TextFormatting.RESET + "");
        for(int i=0;i<StringUtils.countMatches(string, "~STRIKETHROUGH~");i++)  string = string.replace("~STRIKETHROUGH~",  TextFormatting.STRIKETHROUGH + "");
        for(int i=0;i<StringUtils.countMatches(string, "~UNDERLINE~");i++)      string = string.replace("~UNDERLINE~",      TextFormatting.UNDERLINE + "");
        for(int i=0;i<StringUtils.countMatches(string, "~WHITE~");i++)          string = string.replace("~WHITE~",          TextFormatting.WHITE + "");
        for(int i=0;i<StringUtils.countMatches(string, "~YELLOW~");i++)         string = string.replace("~YELLOW~",         TextFormatting.YELLOW + "");

        return string;
    }

    public static String getDefaultQuestsJson(){
        return "{\"quests\":[{\"id\":0,\"icon\":\"minecraft:glass\",\"title\":\"Quest 0\",\"text\":\"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.\",\"dependencies\":[2,3,4,5],\"requirements\":[{\"type\":\"ITEM_DELIVER\",\"sub_requirements\":[{\"item\":\"minecraft:apple\",\"amount\":1,\"nbt\":\"\"},{\"item\":\"minecraft:stick\",\"amount\":1,\"nbt\":\"\"}]},{\"type\":\"ITEM_DELIVER\",\"sub_requirements\":[{\"item\":\"minecraft:diamond\",\"amount\":2,\"nbt\":\"\"},{\"item\":\"minecraft:gold_ingot\",\"amount\":3,\"nbt\":\"\"}]}],\"rewards\":[{\"type\":\"ITEMS\",\"content\":{\"item\":\"minecraft:apple\",\"amount\":1,\"nbt\":\"\"}}],\"position\":[0,0]},{\"id\":1,\"icon\":\"minecraft:melon\",\"title\":\"Quest 1\",\"text\":\"Beta\",\"dependencies\":[],\"requirements\":[{\"type\":\"ITEM_DETECT\",\"sub_requirements\":[{\"item\":\"minecraft:apple\",\"amount\":1,\"nbt\":\"\"},{\"item\":\"minecraft:stick\",\"amount\":1,\"nbt\":\"\"}]}],\"rewards\":[{\"type\":\"ITEMS\",\"content\":{\"item\":\"minecraft:stick\",\"amount\":1,\"nbt\":\"\"}}],\"position\":[100,100]},{\"id\":2,\"icon\":\"minecraft:apple\",\"title\":\"Quest 2\",\"text\":\"Gamma\",\"dependencies\":[],\"requirements\":[{\"type\":\"TRAVEL_TO\",\"sub_requirements\":[{\"dim\":\"minecraft:overworld\",\"x\":0,\"y\":0,\"z\":0,\"radius\":20},{\"dim\":\"minecraft:the_nether\",\"x\":0,\"y\":100,\"z\":0,\"radius\":10}]}],\"rewards\":[{\"type\":\"COMMAND\",\"content\":{\"command\":\"give @a minecraft:diamond 64\"}}],\"position\":[100,0]},{\"id\":3,\"icon\":\"minecraft:apple\",\"title\":\"Quest 3\",\"text\":\"Delta\",\"dependencies\":[],\"requirements\":[{\"type\":\"KILL_MOB\",\"sub_requirements\":[{\"entity\":\"minecraft:zombie\",\"amount\":1}]}],\"rewards\":[{\"type\":\"SPAWN_ENTITY\",\"content\":{\"entity\":\"minecraft:sheep\",\"amount\":3}}],\"position\":[100,100]},{\"id\":4,\"icon\":\"minecraft:apple\",\"title\":\"Quest 4\",\"text\":\"Iota\",\"dependencies\":[],\"requirements\":[{\"type\":\"CRAFTING_DETECT\",\"sub_requirements\":[{\"item\":\"minecraft:iron_block\",\"amount\":1,\"nbt\":\"\"},{\"item\":\"minecraft:stick\",\"amount\":1,\"nbt\":\"\"}]}],\"rewards\":[{\"type\":\"COMMAND\",\"content\":{\"command\":\"give @a minecraft:diamond 64\"}}],\"position\":[200,50]},{\"id\":5,\"icon\":\"minecraft:diamond\",\"title\":\"Quest 5\",\"text\":\"Kappa\",\"dependencies\":[],\"requirements\":[{\"type\":\"KILL_MOB\",\"sub_requirements\":[{\"entity\":\"minecraft:chicken\",\"amount\":2},{\"entity\":\"minecraft:pig\",\"amount\":1}]}],\"rewards\":[{\"type\":\"COMMAND\",\"content\":{\"command\":\"give @a minecraft:diamond 64\"}}],\"position\":[50,300]},{\"id\":6,\"icon\":\"minecraft:diamond\",\"title\":\"Quest 6\",\"text\":\"Lambda\",\"dependencies\":[3],\"requirements\":[{\"type\":\"KILL_MOB\",\"sub_requirements\":[{\"entity\":\"minecraft:pig\",\"amount\":2},{\"entity\":\"minecraft:cow\",\"amount\":1}]}],\"rewards\":[{\"type\":\"COMMAND\",\"content\":{\"command\":\"give @a minecraft:diamond 64\"}}],\"position\":[500,300]}]}";
    }

    public static String getDefaultQuestBookJson(){
        return "{\"title\":\"Your Questing Screen\",\"text\":\"Test123\",\"questlines\":[{\"id\":0,\"title\":\"The Beginning\",\"text\":\"Throwback 101\",\"quests\":[0, 2, 3, 4, 5, 6]},{\"id\":1,\"title\":\"After the beginning\",\"text\":\"Pun intended\",\"quests\":[1]},{\"id\":2,\"title\":\"The madness begins\",\"text\":\"Lol\",\"quests\":[]}]}";
    }

    public static String getDefaultQuestingProgressJson(){
        return "{\"players\":[]}";
    }

    public static String getDefaultQuestingPartiesJson(){
        return "{\"parties\":[]}";
    }

    public static String getUUID(String username){
        if(UsernameUuidCache.isNameAlreadyInCache(username)){
            return UsernameUuidCache.getUuidForName(username);
        }
        try {
            HttpsURLConnection conn = (HttpsURLConnection) new URL("https://api.mojang.com/users/profiles/minecraft/" + username).openConnection();
            conn.setRequestMethod("GET");
            conn.setDoOutput(true);

            BufferedReader bufferedInputReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder data = new StringBuilder();
            String line;
            while((line = bufferedInputReader.readLine())!=null){
                data.append(line);
            }
            String result = data.toString();
            JsonParser parser = new JsonParser();
            JsonObject root = parser.parse(result).getAsJsonObject();
            String uuid = root.get("id").getAsString();
            UsernameUuidCache.registerNewPair(username, uuid);
            return uuid;
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public static String simplifyUUID(UUID uuid){
        return uuid.toString().replaceAll("-", "");
    }

    public static String getDisplayName(String uuid){
        if(UsernameUuidCache.isUuidAlreadyInCache(uuid)){
            return UsernameUuidCache.getNameForUuid(uuid);
        }
        try {
            HttpsURLConnection conn = (HttpsURLConnection) new URL("https://api.mojang.com/user/profiles/" + uuid + "/names").openConnection();
            conn.setRequestMethod("GET");
            conn.setDoOutput(true);

            BufferedReader bufferedInputReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder data = new StringBuilder();
            String line;
            while((line = bufferedInputReader.readLine())!=null){
                data.append(line);
            }
            String result = data.toString();
            JsonParser parser = new JsonParser();
            JsonArray root = parser.parse(result).getAsJsonArray();
            String lastName = "";
            int lastTime = 0;
            for(JsonElement entry : root){
                if(entry.getAsJsonObject().has("name") && entry.getAsJsonObject().has("changedToAt")){
                    if(entry.getAsJsonObject().get("changedToAt").getAsInt() > lastTime){
                        lastName = entry.getAsJsonObject().get("name").getAsString();
                    }
                }else if(entry.getAsJsonObject().has("name")){
                    lastName = entry.getAsJsonObject().get("name").getAsString();
                }
            }
            UsernameUuidCache.registerNewPair(lastName, uuid);
            return lastName;
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    //Just to not worry about the errors
    public static CompoundNBT getNbtFromJson(String jsonString){
        try {
            return JsonToNBT.getTagFromJson(jsonString); //todo check with funwayguy's json to nbt class: https://github.com/Funwayguy/BetterQuesting/blob/1.12/src/main/java/betterquesting/api/utils/NBTConverter.java
        }catch (CommandSyntaxException e){
            e.printStackTrace();
        }
        return new CompoundNBT();
    }

    public static boolean isMouseInBounds(double mouseX, double mouseY, int x1, int y1, int x2, int y2){
        return x2 > mouseX && mouseX > x1 && y2 > mouseY && mouseY > y1;
    }

    public static PlayerEntity getPlayerFromUuid(World world, String uuid){
        for(PlayerEntity player : world.getPlayers()){
            if(Utils.simplifyUUID(player.getUniqueID()).equals(uuid)){
                return player;
            }
        }
        return null;
    }
}