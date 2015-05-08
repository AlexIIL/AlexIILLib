package alexiil.mods.lib.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

import alexiil.mods.lib.AlexIILMod;
import alexiil.mods.lib.LangUtils;

public class ActualConfig extends GuiConfig {
    public ActualConfig(GuiScreen parent, AlexIILMod mod) {
        super(parent, getConfigElements(mod), mod.meta.modId, false, false, LangUtils.format("alexiillib.config.title"));
    }

    private static List<IConfigElement> getConfigElements(AlexIILMod mod) {
        List<IConfigElement> elements = new ArrayList<IConfigElement>();
        Configuration cfg = mod.cfg.cfg();
        for (String name : cfg.getCategoryNames()) {
            ConfigCategory cat = cfg.getCategory(name);
            if (!cat.isChild())
                elements.add(new ConfigElement(cfg.getCategory(name)));
        }
        return elements;
    }
}
