package com.vincentmet.customquests.lib;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class UsernameUuidCache {
    private static List<Pair<String, String>> cache = new ArrayList<>();

    public static void registerNewPair(String displayname, String uuid){
        cache.add(new Pair<>(displayname, uuid));
    }

    public static boolean isNameAlreadyInCache(String displayname){
        for(Pair<String, String> pair : cache){
            if(pair.getKey().toLowerCase().equals(displayname.toLowerCase())){
                return true;
            }
        }
        return false;
    }

    public static boolean isUuidAlreadyInCache(String uuid){
        for(Pair<String, String> pair : cache){
            if(pair.getValue().toLowerCase().equals(uuid.toLowerCase())){
                return true;
            }
        }
        return false;
    }

    public static String getNameForUuid(String uuid){
        for (Pair<String, String> pair : cache){
            if(pair.getValue().equals(uuid)){
                return pair.getKey();
            }
        }
        return null;
    }

    public static String getUuidForName(String name){
        for (Pair<String, String> pair : cache){
            if(pair.getKey().equals(name)){
                return pair.getValue();
            }
        }
        return null;
    }
}
