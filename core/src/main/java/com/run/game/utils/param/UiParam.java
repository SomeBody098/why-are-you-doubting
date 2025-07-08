package com.run.game.utils.param;

public class UiParam implements Param {

    public float position_x_percent;
    public float position_y_percent;
    public float wight_percent;
    public float height_percent;

    public UiParam(float position_x_percent, float position_y_percent, float wight_percent, float height_percent) {
        this.position_x_percent = position_x_percent;
        this.position_y_percent = position_y_percent;
        this.wight_percent = wight_percent;
        this.height_percent = height_percent;
    }
}
