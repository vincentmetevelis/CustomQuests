package com.vincentmet.customquests.lib.converters;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.MalformedJsonException;
import com.vincentmet.customquests.Objects;
import com.vincentmet.customquests.lib.Ref;
import com.vincentmet.customquests.lib.Utils;
import com.vincentmet.customquests.quests.*;
import net.minecraft.client.util.JSONException;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.nio.charset.MalformedInputException;
import java.util.ArrayList;
import java.util.List;

public class ConverterHelper {
	public static class QuestBook{
		public static class Questlines{
			public static int getId(JsonObject json){
				if(json.has("id") && json.get("id").isJsonPrimitive()){
					return json.get("id").getAsInt();
				}else{
					return Ref.ERR_MSG_INT_INVALID_JSON;
				}
			}

			public static String getTitle(JsonObject json){
				if(json.has("title") && json.get("title").isJsonPrimitive()){
					return json.get("title").getAsString();
				}else{
					return Ref.ERR_MSG_STR_INVALID_JSON;
				}
			}

			public static String getText(JsonObject json){
				if(json.has("text") && json.get("text").isJsonPrimitive()){
					return json.get("text").getAsString();
				}else{
					return Ref.ERR_MSG_STR_INVALID_JSON;
				}
			}

			public static List<Integer> getQuestIds(JsonObject json){
				if(json.has("quests") && json.get("quests").isJsonArray()){
					List<Integer> questIdList = new ArrayList<>();
					for(JsonElement quest : json.get("quests").getAsJsonArray()){
						questIdList.add(quest.getAsInt());
					}
					return questIdList;
				}else{
					return new ArrayList<>();
				}
			}
		}

		public static String getTitle(JsonObject json){
			if(json.has("title") && json.get("title").isJsonPrimitive()){
				return json.get("title").getAsString();
			}else{
				return Ref.ERR_MSG_STR_INVALID_JSON;
			}
		}

		public static String getText(JsonObject json){
			if(json.has("text") && json.get("text").isJsonPrimitive()){
				return json.get("text").getAsString();
			}else{
				return Ref.ERR_MSG_STR_INVALID_JSON;
			}
		}

		public static JsonArray getQuestLines(JsonObject json){
			if(json.has("questlines") && json.get("questlines").isJsonArray()){
				return json.get("questlines").getAsJsonArray();
			}else{
				return new JsonArray();
			}
		}
	}

	public static class Quests{
		public static Quest getQuestById(int id){
			for(int i=0; i<Ref.ALL_QUESTS.size(); i++){
				if(Ref.ALL_QUESTS.get(i).getId() == id){
					return Ref.ALL_QUESTS.get(i);
				}
			}
			return null;
		}

		public static int getId(JsonObject json){
			if(json.has("id") && json.get("id").isJsonPrimitive()){
				return json.get("id").getAsInt();
			}else{
				return Ref.ERR_MSG_INT_INVALID_JSON;
			}
		}

		public static Item getIcon(JsonObject json){
			if(json.has("icon") && json.get("icon").isJsonPrimitive()){
				return ForgeRegistries.ITEMS.getValue(new ResourceLocation(json.get("icon").getAsString()));
			}else{
				return Items.AIR;
			}
		}

		public static String getTitle(JsonObject json){
			if(json.has("title") && json.get("title").isJsonPrimitive()){
				return json.get("title").getAsString();
			}else{
				return Ref.ERR_MSG_STR_INVALID_JSON;
			}
		}

		public static String getText(JsonObject json){
			if(json.has("text") && json.get("text").isJsonPrimitive()){
				return json.get("text").getAsString();
			}else{
				return Ref.ERR_MSG_STR_INVALID_JSON;
			}
		}

		public static JsonArray getRequirements(JsonObject json){
			if(json.has("requirements") && json.get("requirements").isJsonArray()){
				return json.get("requirements").getAsJsonArray();
			}else{
				return new JsonArray();
			}
		}

		public static JsonArray getRewards(JsonObject json){
			if(json.has("rewards") && json.get("rewards").isJsonArray()){
				return json.get("rewards").getAsJsonArray();
			}else{
				return new JsonArray();
			}
		}

		public static class Dependencies{
			public static List<Integer> getDependencyQuestIds(JsonObject json){
				if(json.has("dependencies") && json.get("dependencies").isJsonArray()){
					List<Integer> questDependencyIdList = new ArrayList<>();
					for(JsonElement questDependencyId : json.get("dependencies").getAsJsonArray()){
						questDependencyIdList.add(questDependencyId.getAsInt());
					}
					return questDependencyIdList;
				}else{
					return new ArrayList<>();
				}
			}
		}

		public static class Requirements{
			public static QuestRequirementType getType(JsonObject json){
				if(json.has("type") && json.get("type").isJsonArray()){
					switch(json.get("type").getAsString()){
						case "ITEM_DETECT": return QuestRequirementType.ITEM_DETECT;
						case "ITEM_DELIVER": return QuestRequirementType.ITEM_DELIVER;
						case "CRAFTING_DETECT": return QuestRequirementType.CRAFTING_DETECT;
						case "KILL_MOB": return QuestRequirementType.KILL_MOB;
						case "TRAVEL_TO": return QuestRequirementType.TRAVEL_TO;
						default: return QuestRequirementType.ITEM_DETECT;
					}
				}else{
					return QuestRequirementType.ITEM_DETECT;
				}
			}

			public static JsonArray getSubRequirements(JsonObject json){
				if(json.has("sub_requirements") && json.get("sub_requirements").isJsonArray()){
					return json.get("sub_requirements").getAsJsonArray();
				}else{
					return new JsonArray();
				}
			}

			public static class ItemsDetect{
				public static Item getSubRequirementItem(JsonObject json){
					if(json.has("item") && json.get("item").isJsonPrimitive()){
						return ForgeRegistries.ITEMS.getValue(new ResourceLocation(json.get("item").getAsString()));
					}else{
						return Items.AIR;
					}
				}

				public static int getSubRequirementAmount(JsonObject json){
					if(json.has("amount") && json.get("amount").isJsonPrimitive()){
						return json.get("amount").getAsInt();
					}else{
						return Ref.ERR_MSG_INT_INVALID_JSON;
					}
				}

				public static CompoundNBT getSubRequirementNbt(JsonObject json){
					if(json.has("nbt") && json.get("nbt").isJsonPrimitive()){
						return Utils.getNbtFromJson(json.get("nbt").getAsString());
					}else{
						return new CompoundNBT();
					}
				}
			}

			public static class ItemsRetrieve{

			}

			public static class KillMob{

			}

			public static class TravelTo{

			}
		}

		public static class Rewards{
			public static QuestRewardType getType(JsonObject json){
				if(json.has("type") && json.get("type").isJsonArray()){
					switch(json.get("type").getAsString()){
						case "ITEMS": return QuestRewardType.ITEMS;
						case "COMMAND": return QuestRewardType.COMMAND;
						case "SPAWN_ENTITY": return QuestRewardType.SPAWN_ENTITY;
						default: return QuestRewardType.ITEMS;
					}
				}else{
					return QuestRewardType.ITEMS;
				}
			}

			public static class Items{
				public static Item getItem(JsonObject json){
					if(json.has("content") && json.get("content").getAsJsonObject().has("item") && json.get("content").getAsJsonObject().get("item").isJsonPrimitive()){
						return ForgeRegistries.ITEMS.getValue(new ResourceLocation(json.get("content").getAsJsonObject().get("item").getAsString()));
					}else{
						return net.minecraft.item.Items.AIR;
					}
				}

				public static int getAmount(JsonObject json){
					if(json.has("content") && json.get("content").getAsJsonObject().has("amount") && json.get("content").getAsJsonObject().get("amount").isJsonPrimitive()){
						return json.get("content").getAsJsonObject().get("amount").getAsInt();
					}else{
						return Ref.ERR_MSG_INT_INVALID_JSON;
					}
				}

				public static CompoundNBT getNbt(JsonObject json){
					if(json.has("content") && json.get("content").getAsJsonObject().has("nbt") && json.get("content").getAsJsonObject().get("nbt").isJsonPrimitive()){
						return Utils.getNbtFromJson(json.get("content").getAsJsonObject().get("nbt").getAsString());
					}else{
						return new CompoundNBT();
					}
				}
			}

			public static class Command{
				public static String getCommand(JsonObject json){
					if(json.has("content") && json.get("content").getAsJsonObject().has("command") && json.get("content").getAsJsonObject().get("command").isJsonPrimitive()){
						return json.get("content").getAsJsonObject().get("command").getAsString();
					}else{
						return Ref.ERR_MSG_STR_INVALID_JSON;
					}
				}
			}

			public static class SpawnEntity{
				public static EntityType getEntity(JsonObject json){
					if(json.has("content") && json.get("content").getAsJsonObject().has("entity") && json.get("content").getAsJsonObject().get("entity").isJsonPrimitive()){
						return ForgeRegistries.ENTITIES.getValue(new ResourceLocation(json.get("content").getAsJsonObject().get("entity").getAsString()));
					}else{
						return null;
					}
				}
			}

			public static class TeleportTo{

			}
		}

		public static class GuiPosition{
			public static QuestPosition getGuiPosition(JsonObject json){
				if(json.has("position") && json.get("position").isJsonArray()){
					JsonArray position = json.get("position").getAsJsonArray();
					return new QuestPosition(position.get(0).getAsInt(), position.get(1).getAsInt());
				}else{
					return new QuestPosition(0, 0);
				}
			}
		}
	}















	public static class Quests1{
		public static int getQuestIdFromQuestJsonElement(JsonElement quest){
			return quest.getAsJsonObject().get("id").getAsInt();
		}

		public static String getQuestTitleFromQuestJsonElement(JsonElement quest){
			return quest.getAsJsonObject().get("title").getAsString();
		}

		public static String getQuestDescriptionFromQuestJsonElement(JsonElement quest){
			return quest.getAsJsonObject().get("text").getAsString();
		}

		public static Item getQuestIconFromQuestJsonElement(JsonElement quest){
			return ForgeRegistries.ITEMS.getValue(new ResourceLocation(quest.getAsJsonObject().get("icon").getAsString()));
		}

		public static QuestRequirementType getQuestTypeFromString(String questType){
			/*switch(questType){
				case "ITEM_DETECT": return QuestRequirementType.ITEM_DETECT;
				case "ITEM_DELIVER": return QuestRequirementType.ITEM_DELIVER;
				case "CRAFTING_DETECT": return QuestRequirementType.CRAFTING_DETECT;
				case "KILL_MOB": return QuestRequirementType.KILL_MOB;
				case "TRAVEL_TO": return QuestRequirementType.TRAVEL_TO;
				default: return QuestRequirementType.ITEM_DETECT;
			}*/

			return QuestRequirementType.ITEM_DETECT;
		}

		public static QuestRewardType getQuestRewardTypeFromString(String rewardType){
			/*switch(rewardType){
				case "ITEMS": return QuestRewardType.ITEMS;
				case "COMMAND": return QuestRewardType.COMMAND;
				case "SPAWN_ENTITY": return QuestRewardType.SPAWN_ENTITY;
				default: return QuestRewardType.ITEMS;
			}*/
			return QuestRewardType.ITEMS;
		}

		public static QuestPosition getQuestPositionFromJsonArray(JsonArray xyArray){
			int x = xyArray.get(0).getAsInt();
			int y = xyArray.get(1).getAsInt();
			return new QuestPosition(x, y);
		}
	}
}