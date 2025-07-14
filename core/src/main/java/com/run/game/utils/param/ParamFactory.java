package com.run.game.utils.param;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;

public class ParamFactory {

    private static final ObjectMap<String, JsonValue> DATA_STORAGE;

    static {
        DATA_STORAGE = new ObjectMap<>();
        JsonReader reader = new JsonReader();

        DATA_STORAGE.put("ui", reader.parse(Gdx.files.internal("ui/parameters/ui_property.json")));
    }

    public static BoundsParam getUiParam(String uiName) {
        if (ParamLate.contains(uiName)) return (BoundsParam) ParamLate.getParam(uiName);

        BoundsParam param = getBoundsParam(getJsonValue(uiName, "ui"));
        ParamLate.putParam(uiName, param);

        return param;
    }

    private static BoundsParam getBoundsParam(JsonValue uiValue){
        return new BoundsParam(
            uiValue.getFloat("position_x_percent"),
            uiValue.getFloat("position_y_percent"),
            uiValue.getFloat("wight_percent"),
            uiValue.getFloat("height_percent")
        );
    }

    public static BoundsTextParam getUiTextParam(String uiName) { // FIXME: 07.07.2025 идентичен getUiParam! Придумай как не копировать код
        if (ParamLate.contains(uiName)) return (BoundsTextParam) ParamLate.getParam(uiName);

        JsonValue uiValue = getJsonValue(uiName, "ui");

        BoundsTextParam param = new BoundsTextParam(
            uiValue.getFloat("position_x_percent"),
            uiValue.getFloat("position_y_percent"),
            uiValue.getFloat("wight_percent"),
            uiValue.getFloat("height_percent"),
            uiValue.getString("text")
        );

        ParamLate.putParam(uiName, param);

        return param;
    }

    private static JsonValue getJsonValue(String nameObject, String nameJson){
        if (!DATA_STORAGE.containsKey(nameJson)) throw new IllegalArgumentException("Unknown name Json file: " + nameJson + ".");
        JsonValue root = DATA_STORAGE.get(nameJson);

        if (!root.has(nameObject)) throw new IllegalArgumentException("Unknown name object in Json file: " + nameObject + ".");

        return root.get(nameObject);
    }
}
