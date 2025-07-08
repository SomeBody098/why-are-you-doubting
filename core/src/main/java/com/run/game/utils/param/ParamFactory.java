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

        DATA_STORAGE.put("ui", reader.parse(Gdx.files.internal("parameters/ui_property.json")));
    }

    public static UiParam getUiParam(String uiName) {
        if (ParamLate.contains(uiName)) return (UiParam) ParamLate.getParam(uiName);

        JsonValue uiValue = getJsonValue(uiName, "ui");

        UiParam param = new UiParam(
            uiValue.getFloat("position_x_percent"),
            uiValue.getFloat("position_y_percent"),
            uiValue.getFloat("wight_percent"),
            uiValue.getFloat("height_percent")
        );

        ParamLate.putParam(uiName, param);

        return param;
    }

    public static UiTextParam getUiTextParam(String uiName) { // FIXME: 07.07.2025 идентичен getUiParam! Придумай как не копировать код
        if (ParamLate.contains(uiName)) return (UiTextParam) ParamLate.getParam(uiName);

        JsonValue uiValue = getJsonValue(uiName, "ui");

        UiTextParam param = new UiTextParam(
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
