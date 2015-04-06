package alexiil.mods.lib.gui;

import net.minecraft.client.gui.GuiScreen;
import alexiil.mods.lib.AlexIILLib;
import alexiil.mods.lib.git.BaseConfig;

public class Config extends BaseConfig {
    public Config(GuiScreen screen) {
        super(screen, AlexIILLib.instance);
    }
}
