package com.run.game.ui.action;

import com.badlogic.gdx.Screen;
import com.run.game.Main;

public class ScreenSwitchAction extends UiAction {

    private final Main main;
    private final Screen targetScreen;

    public ScreenSwitchAction(Main main, Screen targetScreen) {
        this.main = main;
        this.targetScreen = targetScreen;
    }

    @Override
    public void execute() {
        main.setScreen(targetScreen);
    }
}
