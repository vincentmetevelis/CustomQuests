package com.vincentmet.customquests.lib;

import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;
import java.util.List;

public class UsernameUuidCache {
    private static List<Pair<String, String>> cache = new ArrayList<>();

    public static void registerNewPair(String displayname, String uuid){
        cache.add(new Pair<>(displayname, uuid));
    }

    public static boolean isNameAlreadyInCache(String displayname){
        for(Pair<String, String> pair : cache){
            if(pair.getFirst().toLowerCase().equals(displayname.toLowerCase())){
                return true;
            }
        }
        return false;
    }

    public static boolean isUuidAlreadyInCache(String uuid){
        for(Pair<String, String> pair : cache){
            if(pair.getSecond().toLowerCase().equals(uuid.toLowerCase())){
                return true;
            }
        }
        return false;
    }

    public static String getNameForUuid(String uuid){
        for (Pair<String, String> pair : cache){
            if(pair.getSecond().equals(uuid)){
                return pair.getFirst();
            }
        }
        return null;
    }

    public static String getUuidForName(String name){
        for (Pair<String, String> pair : cache){
            if(pair.getFirst().equals(name)){
                return pair.getSecond();
            }
        }
        return null;
    }
}
