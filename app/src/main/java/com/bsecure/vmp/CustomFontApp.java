package com.bsecure.vmp;

import android.app.Application;

public class CustomFontApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FontsOverride.setDefaultFont(this, "DEFAULT", "fonts/Lato-Regular.ttf");
        FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/Lato-Regular.ttf");
        FontsOverride.setDefaultFont(this, "SERIF", "fonts/Lato-Regular.ttf");
        FontsOverride.setDefaultFont(this, "SANS_SERIF", "fonts/Lato-Regular.ttf");
        FontsOverride.setDefaultFont(this, "ROBOTO", "fonts/Lato-Regular.ttf");
    }
}
