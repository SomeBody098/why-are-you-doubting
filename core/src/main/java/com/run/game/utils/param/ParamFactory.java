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

        DATA_STORAGE.put("ui", reader.parse(Gdx.files.internal("ui/ui_property.json")));
        DATA_STORAGE.put("music", reader.parse(Gdx.files.internal("music/music_property.json")));
    }

    public static BoundsParam getUiParam(String uiName) {
        if (ParamLate.contains(uiName)) return (BoundsParam) ParamLate.getParam(uiName);
        Float[] bounds = getBoundsParam(getJsonValue(uiName, "ui"));

        BoundsParam param = new BoundsParam(
            bounds[0],
            bounds[1],
            bounds[2],
            bounds[3]
        );
        ParamLate.putParam(uiName, param);

        return param;
    }

    public static BoundsTextParam getUiTextParam(String uiName) {
        if (ParamLate.contains(uiName)) return (BoundsTextParam) ParamLate.getParam(uiName);
        JsonValue uiValue = getJsonValue(uiName, "ui");
        Float[] bounds = getBoundsParam(uiValue);

        BoundsTextParam param = new BoundsTextParam(
            bounds[0],
            bounds[1],
            bounds[2],
            bounds[3],
            uiValue.getString("text")
        );
        ParamLate.putParam(uiName, param);

        return param;
    }

    private static Float[] getBoundsParam(JsonValue uiValue){ // FIXME: 18.07.2025 не очень хорошо, что приходиться использовать массив (ну - хардкодить последовательность параметров), но в целом - пойдет
        return new Float[]{
            uiValue.getFloat("position_x_percent"),
            uiValue.getFloat("position_y_percent"),
            uiValue.getFloat("wight_percent"),
            uiValue.getFloat("height_percent")
        };
    }

    private static JsonValue getJsonValue(String nameObject, String nameJson){
        if (!DATA_STORAGE.containsKey(nameJson)) throw new IllegalArgumentException("Unknown name Json file: " + nameJson + ".");
        JsonValue root = DATA_STORAGE.get(nameJson);

        if (!root.has(nameObject)) throw new IllegalArgumentException("Unknown name object in Json file: " + nameObject + ".");

        return root.get(nameObject);
    }
}
