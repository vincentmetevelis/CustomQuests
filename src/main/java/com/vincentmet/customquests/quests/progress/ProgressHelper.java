package com.vincentmet.customquests.quests.progress;

import com.vincentmet.customquests.lib.Ref;
import com.vincentmet.customquests.quests.IQuestRequirement;
import com.vincentmet.customquests.quests.quest.Quest;
import java.util.Map;

public class ProgressHelper{
	public static boolean areAllRequirementsCompleted(String uuid, int questId){
		boolean isCompleted = true;
		for(Map.Entry<String, QuestUserProgress> userprogress : Ref.ALL_QUESTING_PROGRESS.entrySet()) {
			if (userprogress.getValue().getUuid().equals(uuid)) {
				for (Map.Entry<Integer, QuestStatus> playerStatus : userprogress.getValue().getQuestStatuses().entrySet()) {
					if(playerStatus.getValue().getQuestId() == questId){
						int countQrs = 0;
						for(QuestRequirementStatus qrs : playerStatus.getValue().getQuestRequirementStatuses()){
							if(!isRequirementCompleted(uuid, questId, countQrs)){
								isCompleted = false;
							}
							countQrs++;
						}
					}
				}
			}
		}
		return isCompleted;
	}
	
	public static QuestUserProgress getUserProgressForUuid(String uuid){
		for(Map.Entry<String, QuestUserProgress> userProgress : Ref.ALL_QUESTING_PROGRESS.entrySet()){
			if(userProgress.getValue().getUuid().equals(uuid)){
				return userProgress.getValue();
			}
		}
		return null;
	}
	
	public static boolean isRequirementCompleted(String uuid, int questId, int reqId){
		boolean isCompleted = true;
		for(Map.Entry<String, QuestUserProgress> userprogress : Ref.ALL_QUESTING_PROGRESS.entrySet()){
			if(userprogress.getKey().equals(uuid)){ // get player
				for(Map.Entry<Integer, QuestStatus> playerStatus : userprogress.getValue().getQuestStatuses().entrySet()){
					if(playerStatus.getKey() == questId){ //then for the quest id
						for(QuestRequirementStatus reqStatus : playerStatus.getValue().getQuestRequirementStatuses()){
							if(reqStatus.getRequirementId() == reqId){ // with the requirement id
								int countSubReqProgress = 0;
								for(Map.Entry<Integer, QuestSubrequirementStatus> subReqProgress : reqStatus.getProgress().entrySet()){ //get sub requirements
									int countSubReq = 0;
									for(IQuestRequirement subReq : Quest.getQuestFromId(questId).getRequirements().get(reqId).getSubRequirements()){ // then get the sub requirement from ids // make sure the subrequirements are the same
										if(countSubReq == countSubReqProgress){
											if(subReqProgress.getValue().getValue() < subReq.getCompletionNumber()){
												isCompleted = false;
											}
										}
										countSubReq++;
									}
									countSubReqProgress++;
								}
							}
						}
					}
				}
			}
		}
		return isCompleted;
	}
	
	public static int getItemCountLeftToHandIn(String uuid, int questId, int reqId, int subReqId){
		for(Map.Entry<String, QuestUserProgress> progress : Ref.ALL_QUESTING_PROGRESS.entrySet()){
			if(progress.getValue().getUuid().equals(uuid)){
				int currentCount = progress.getValue().getQuestStatuses().get(questId).getQuestRequirementStatuses().get(reqId).getProgress(subReqId);
				int questCompletionCount = Quest.getQuestFromId(questId).getRequirements().get(reqId).getSubRequirements().get(subReqId).getCompletionNumber();
				return questCompletionCount - currentCount;
			}
		}
		return 0;
	}
	
	public static boolean isRewardClaimed(String uuid, int questId){
		for(Map.Entry<String, QuestUserProgress> progress : Ref.ALL_QUESTING_PROGRESS.entrySet()){
			if(progress.getValue().getUuid().equals(uuid)){
				for(Map.Entry<Integer, QuestStatus> reqStatus : progress.getValue().getQuestStatuses().entrySet()){
					if(reqStatus.getValue().getQuestId() == questId){
						return reqStatus.getValue().isClaimed();
					}
				}
			}
		}
		return false;
	}
	
	public static void setRewardsClaimed(String uuid, int questId){
		for(Map.Entry<String, QuestUserProgress> progress : Ref.ALL_QUESTING_PROGRESS.entrySet()){
			if(progress.getValue().getUuid().equals(uuid)){
				for(Map.Entry<Integer, QuestStatus> reqStatus : progress.getValue().getQuestStatuses().entrySet()){
					if(reqStatus.getValue().getQuestId() == questId){
						reqStatus.getValue().setClaimed(true);
					}
				}
			}
		}
	}
	
	public static void setPlayerProgressToCompleted(String uuid, int questId, int reqId, int subReqId){
		for(Map.Entry<String, QuestUserProgress> userprogress : Ref.ALL_QUESTING_PROGRESS.entrySet()) {
			if(userprogress.getValue().getUuid().equals(uuid)) {
				for(Map.Entry<Integer, QuestStatus> status : userprogress.getValue().getQuestStatuses().entrySet()) {
					if(status.getValue().getQuestId() == questId){
						int reqCount = 0;
						for(QuestRequirementStatus reqStatus : status.getValue().getQuestRequirementStatuses()){
							if(reqCount == reqId){
								reqStatus.setProgress(subReqId, Quest.getQuestFromId(questId).getRequirements().get(reqId).getSubRequirements().get(subReqId).getCompletionNumber());
							}
							reqCount++;
						}
					}
				}
			}
		}
	}
	
	public static void addPlayerProgress(String uuid, int questId, int reqId, int subReqId, int amount){
		for(Map.Entry<String, QuestUserProgress> userprogress : Ref.ALL_QUESTING_PROGRESS.entrySet()) {
			if(userprogress.getValue().getUuid().equals(uuid)) {
				for(Map.Entry<Integer, QuestStatus> status : userprogress.getValue().getQuestStatuses().entrySet()) {
					if(status.getValue().getQuestId() == questId){
						int reqCount = 0;
						for(QuestRequirementStatus reqStatus : status.getValue().getQuestRequirementStatuses()){
							if(reqCount == reqId){
								reqStatus.setProgress(subReqId, reqStatus.getProgress(subReqId) + amount);
							}
							reqCount++;
						}
					}
				}
			}
		}
	}
}