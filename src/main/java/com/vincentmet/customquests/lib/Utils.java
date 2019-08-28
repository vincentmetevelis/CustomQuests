package com.vincentmet.customquests.lib;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.sun.javafx.geom.Vec2f;
import com.vincentmet.customquests.screens.ScreenQuestingDevice;
import com.vincentmet.customquests.quests.Quest;
import com.vincentmet.customquests.quests.QuestLine;
import com.vincentmet.customquests.quests.QuestPosition;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.util.datafix.fixes.StringToUUID;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.opengl.GL11;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
        StringBuilder sb = new StringBuilder();
        String line;
        try(BufferedReader br = new BufferedReader(new InputStreamReader(Minecraft.getInstance().getResourceManager().getResource(Ref.defaultQuestsLocation).getInputStream()))){
            while((line = br.readLine())!= null){
                sb.append(line);
                sb.append("\n");
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static String getDefaultQuestBookJson(){
        StringBuilder sb = new StringBuilder();
        String line;
        try(BufferedReader br = new BufferedReader(new InputStreamReader(Minecraft.getInstance().getResourceManager().getResource(Ref.defaultQuestBookLocation).getInputStream()))){
            while((line = br.readLine())!= null){
                sb.append(line);
                sb.append("\n");
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static String getDefaultQuestingProgressJson(){
        StringBuilder sb = new StringBuilder();
        String line;
        try(BufferedReader br = new BufferedReader(new InputStreamReader(Minecraft.getInstance().getResourceManager().getResource(Ref.defaultQuestingProgressLocation).getInputStream()))){
            while((line = br.readLine())!= null){
                sb.append(line);
                sb.append("\n");
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
    
    public static <T extends ScreenQuestingDevice> void addQuestlineButton(int x, int y, double mouseX, double mouseY, String text, T gui, FontRenderer fontRenderer){
        x += Ref.GUI_QUESTLINES_MARGIN_LEFT;
        y += Ref.GUI_QUESTLINES_MARGIN_TOP;
        int width = 150;
        int height = 20;
        if((x + width) > mouseX && mouseX > x && (y + height) > mouseY && mouseY > y){
            gui.getMinecraft().getTextureManager().bindTexture(Ref.IMAGE_BUTTON_QUESTLINE_PRESSED);
        }else{
            gui.getMinecraft().getTextureManager().bindTexture(Ref.IMAGE_BUTTON_QUESTLINE_UNPRESSED);
        }
        gui.blit(x, y, 0, 0, width, height);
        Utils.addLabel((x+(width/2)-fontRenderer.getStringWidth(text)/2), (y+(height/2)) - (fontRenderer.FONT_HEIGHT/2), text, 0xFFFFFF, gui, fontRenderer);
    }
    
    public static <T extends ScreenQuestingDevice> void onQuestlineButtonClicked(int x, int y, double mouseX, double mouseY, T gui, QuestLine questLine){
        x += Ref.GUI_QUESTLINES_MARGIN_LEFT;
        y += Ref.GUI_QUESTLINES_MARGIN_TOP;
        int width = 150;
        int height = 20;
        if((x + width) > mouseX && mouseX > x && (y + height) > mouseY && mouseY > y){
            ScreenQuestingDevice.activeQuestline = questLine.getId();
            ScreenQuestingDevice.activeQuest = -1;
        }
    }
    
    public static <T extends ScreenQuestingDevice> void addHexaButton(int x, int y, double mouseX, double mouseY, ItemRenderer itemRender, T gui, ItemStack item, int questID, String uuid){
        x += Ref.GUI_QUESTING_MARGIN_LEFT;
        y += Ref.GUI_QUESTING_MARGIN_TOP;
        int width = 32;
        int height = 32;
        if(Quest.hasQuestUncompletedDependenciesForPlayer(uuid, questID)){
            gui.getMinecraft().getTextureManager().bindTexture(Ref.IMAGE_BUTTON_HEXAGON_DISABLED);
        }else{
            if((x + width) > mouseX && mouseX > x && (y + height) > mouseY && mouseY > y){
                gui.getMinecraft().getTextureManager().bindTexture(Ref.IMAGE_BUTTON_HEXAGON_PRESSED);
            }else{
                gui.getMinecraft().getTextureManager().bindTexture(Ref.IMAGE_BUTTON_HEXAGON_UNPRESSED);
            }
        }
        gui.blit(x, y, 0, 0, width, height);
        itemRender.renderItemIntoGUI(item, x + 8, y + 7);
    }
    
    public static <T extends ScreenQuestingDevice> void onHexaButtonClicked(int x, int y, double mouseX, double mouseY,  T gui, Quest quest, String uuid){
        x += Ref.GUI_QUESTING_MARGIN_LEFT;
        y += Ref.GUI_QUESTING_MARGIN_TOP;
        int width = 32;
        int height = 32;
        if(!Quest.hasQuestUncompletedDependenciesForPlayer(uuid, quest.getId())){
            if((x + width) > mouseX && mouseX > x && (y + height) > mouseY && mouseY > y){
                ScreenQuestingDevice.activeQuest = quest.getId();
            }
        }
    }
    
    public static <T extends ScreenQuestingDevice> void addCenteredLabel(int y, String text, int color, T gui, FontRenderer fontRendererObj){
        gui.drawString(fontRendererObj, text, (gui.width/2) - (fontRendererObj.getStringWidth(text)/2), y, color);
    }
    
    public static <T extends ScreenQuestingDevice> void addLabel(int x, int y, String text, int color, T gui, FontRenderer fontRendererObj){
        gui.drawString(fontRendererObj, text, x, y, color);
    }
    
    public static <T extends ScreenQuestingDevice> void drawConnectionLine(QuestPosition quest, QuestPosition dependency, T gui, LineColor lineColor){
        GL11.glPushMatrix();
        int dx = quest.getX() - dependency.getX();
        int dy = quest.getY() - dependency.getY();
        int length = (int)Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));

        int iconHeightCentered = (32 / 2) - (int)Math.floor((float)Ref.GUI_QUESTING_LINE_THICKNESS/2);
        int iconWidthCentered = (32 / 2) - (int)Math.floor((float)Ref.GUI_QUESTING_LINE_THICKNESS/2);

        int x = Ref.GUI_QUESTING_MARGIN_LEFT + iconWidthCentered + quest.getX();
        int y = Ref.GUI_QUESTING_MARGIN_TOP + iconHeightCentered + quest.getY();

        double angle = Math.toDegrees(Math.atan2(dy,dx))+180;

        GL11.glTranslatef(x, y, 0);
        GL11.glRotated(angle, 0, 0, 1);
        GL11.glTranslatef(-x, -y, 0);
        gui.getMinecraft().getTextureManager().bindTexture(lineColor.getResourceLocation());
        gui.blit(x, y, 0, 0, length, Ref.GUI_QUESTING_LINE_THICKNESS);
        GL11.glPopMatrix();
    }

    public static <T extends ScreenQuestingDevice> void drawLine(Vec2f point1, Vec2f point2, T gui, LineColor lineColor){
        GL11.glPushMatrix();
        int dx = (int)(point1.x - point2.x);
        int dy = (int)(point1.y - point2.y);
        int length = (int)Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));

        double angle = Math.toDegrees(Math.atan2(dy,dx))+180;

        GL11.glTranslatef(point1.x, point1.y, 0);
        GL11.glRotated(angle, 0, 0, 1);
        GL11.glTranslatef(-point1.x, -point1.y, 0);
        gui.getMinecraft().getTextureManager().bindTexture(lineColor.getResourceLocation());
        gui.blit((int)point1.x, (int)point1.y, 0, 0, length, Ref.GUI_QUESTING_LINE_THICKNESS);
        GL11.glPopMatrix();
    }
    
    public static <T extends ScreenQuestingDevice> void drawConnectionLine(QuestPosition quest, List<QuestPosition> dependencies, T gui, LineColor lineColor){
        for(QuestPosition qp : dependencies){
            drawConnectionLine(quest, qp, gui, lineColor);
        }
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
            return JsonToNBT.getTagFromJson(jsonString);
        }catch (CommandSyntaxException e){
            e.printStackTrace();
        }
        return new CompoundNBT();
    }

    public static <T extends ScreenQuestingDevice> void drawMultilineText(T gui, int x, int startingHeight, int maxWidth, FontRenderer fontRenderer, String text, int color){
        List<String> lines = new ArrayList<>();
        List<String> spaceSplittedText = Arrays.asList(text.split(" "));

        String lastLine = "";
        boolean isFirstWord = true;
        for(String word : spaceSplittedText){
            if(fontRenderer.getStringWidth(lastLine + " " + word) <= maxWidth){
                if(isFirstWord){
                    lastLine += word;
                    isFirstWord = false;
                }else{
                    lastLine += (" " + word);
                }
            }else{
                lines.add(lastLine);
                lastLine = word;
            }
        }
        lines.add(lastLine);

        int currentHeight = startingHeight;
        for(String line : lines){
            gui.drawString(fontRenderer, line, x, currentHeight, color);
            currentHeight += fontRenderer.FONT_HEIGHT;
        }
    }
}