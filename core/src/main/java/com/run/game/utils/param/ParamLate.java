package com.run.game.utils.param;

import com.badlogic.gdx.utils.ObjectMap;

public class ParamLate {
    private static final ObjectMap<String, Param> LATE_PARAM;

    static {
        LATE_PARAM = new ObjectMap<>();
    }

    public static boolean contains(String key){
        return LATE_PARAM.containsKey(key);
    }

    public static Param getParam(String key){
        return LATE_PARAM.get(key);
    }

    public static void putParam(String key, Param value){
        LATE_PARAM.put(key, value);
    }
}
