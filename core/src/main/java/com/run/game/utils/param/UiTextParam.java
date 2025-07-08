package com.run.game.utils.param;

public class UiTextParam extends UiParam{

    public String text;

    public UiTextParam(float position_x_percent, float position_y_percent, float wight_percent, float height_percent, String text) {
        super(position_x_percent, position_y_percent, wight_percent, height_percent);

        this.text = text;
    }
}
